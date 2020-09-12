package com.github.lleuad0.userstesttask

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.Intent.*
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.github.lleuad0.userstesttask.database.UserEntity
import com.github.lleuad0.userstesttask.databinding.FragmentUserContentEditBinding
import timber.log.Timber


const val SELECT_IMAGE_TAG = 0

class UserContentEditFragment : Fragment(), MainActivity.UserFragment {
    private var binding: FragmentUserContentEditBinding? = null
    private val modelUsers: ViewModelUsers by activityViewModels()
    private var userId: Int = -1
    override lateinit var currentUser: UserEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.editor_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            resources.getString(R.string.menu_delete) -> (activity as MainActivity).deletePrompt(this)
            resources.getString(R.string.menu_save) -> savePrompt()
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserContentEditBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.setTitle(R.string.editor_title)

        binding!!.root

        userId = arguments?.getInt(ID_TAG) ?: -1
        modelUsers.getUserById(userId, fun(user: UserEntity) {
            currentUser = user

            binding!!.editFirstNameLayout.helperText = user.firstName
            binding!!.editFirstNameText.setText(user.firstName)

            binding!!.editLastNameLayout.helperText = user.lastName
            binding!!.editLastNameText.setText(user.lastName)

            binding!!.editEmailLayout.helperText = user.email
            binding!!.editEmailText.setText(user.email)

            binding!!.editImage.tag = user.avatarPath
            binding!!.editImage.setOnClickListener { selectImage() }
            Glide.with(this).load(user.avatarPath).into(binding!!.editImage)
        })
    }

    private fun selectImage() {
        val intent =
            Intent(ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_IMAGE_TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SELECT_IMAGE_TAG && resultCode == RESULT_OK && data != null) {
            val path = data.dataString
            Timber.i("Selected image path: $path")

            binding!!.editImage.tag = path
            binding!!.editImage.alpha = 1f
            binding!!.hintText.isVisible = false
            Glide.with(this).load(path).into(binding!!.editImage)
        }
    }

    fun savePrompt() {
        val changedUser = UserEntity(
            userId,
            binding!!.editEmailText.text.toString(),
            binding!!.editFirstNameText.text.toString(),
            binding!!.editLastNameText.text.toString(),
            binding!!.editImage.tag.toString()
        )
        if (currentUser != changedUser) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.save_dialog_title)
                .setPositiveButton(R.string.save_dialog_pos)
                { _, _ ->
                    modelUsers.updateUser(changedUser)
                    (activity as MainActivity).closeEditor()
                }

                .setNegativeButton(R.string.save_dialog_neg)
                { _, _ -> (activity as MainActivity).closeEditor() }

                .setNeutralButton(R.string.save_dialog_neu)
                { dialogInterface: DialogInterface, _ -> dialogInterface.dismiss() }

                .show()
        } else {
            (activity as MainActivity).closeEditor()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

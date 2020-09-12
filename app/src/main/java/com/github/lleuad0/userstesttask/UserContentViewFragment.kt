package com.github.lleuad0.userstesttask

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.github.lleuad0.userstesttask.database.UserEntity
import com.github.lleuad0.userstesttask.databinding.FragmentUserContentViewBinding


class UserContentViewFragment : Fragment(), MainActivity.UserFragment {
    private var binding: FragmentUserContentViewBinding? = null
    private val modelUsers: ViewModelUsers by activityViewModels()
    private var userId: Int = -1
    override lateinit var currentUser: UserEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.viewer_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val activity = activity as MainActivity
        when (item.title) {
            resources.getString(R.string.menu_delete) -> activity.deletePrompt(this)
            resources.getString(R.string.menu_edit) -> activity.openEditor(userId)
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserContentViewBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.setTitle(R.string.viewer_title)

        userId = arguments?.getInt(ID_TAG) ?: -1
        modelUsers.getUserById(userId, fun(user: UserEntity) {
            currentUser = user

            Glide.with(this).load(user.avatarPath).into(binding!!.viewImage)
            binding!!.viewFirstName.text = user.firstName
            binding!!.viewLastName.text = user.lastName
            binding!!.viewEmail.text = user.email
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

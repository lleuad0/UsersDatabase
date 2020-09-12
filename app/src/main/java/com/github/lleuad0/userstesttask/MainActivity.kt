package com.github.lleuad0.userstesttask

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.github.lleuad0.userstesttask.database.UserEntity
import com.github.lleuad0.userstesttask.databinding.ActivityMainBinding


const val ID_TAG = "ID_TAG"
const val FRAGMENT_VIEW_TAG = "FRAGMENT_VIEW_TAG"
const val FRAGMENT_EDIT_TAG = "FRAGMENT_EDIT_TAG"

class MainActivity : AppCompatActivity(), UserListAdapter.Listener {
    interface UserFragment {
        //necessary for sharing deletePrompt() method
        var currentUser: UserEntity
    }

    var binding: ActivityMainBinding? = null
    private val model by viewModels<ViewModelUsers>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        if (savedInstanceState == null) {
            model.refreshData()
            supportFragmentManager.beginTransaction()
                .add(R.id.main_activity, UserListFragment())
                .commit()
        }
    }

    override fun onUserCardClick(id: Int) {
        val bundle = Bundle()
        bundle.putInt(ID_TAG, id)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity, UserContentViewFragment()::class.java, bundle)
            .addToBackStack(FRAGMENT_VIEW_TAG)
            .commit()
    }

    fun openEditor(id: Int) {
        val bundle = Bundle()
        bundle.putInt(ID_TAG, id)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_activity, UserContentEditFragment()::class.java, bundle)
            .addToBackStack(FRAGMENT_EDIT_TAG)
            .commit()
    }

    fun closeEditor() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding!!.root.windowToken, 0)
        supportFragmentManager.popBackStack(FRAGMENT_VIEW_TAG, 0)
    }

    fun deletePrompt(userFragment: UserFragment) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.delete_dialog_title)
            .setPositiveButton(R.string.delete_dialog_pos)
            { _, _ ->
                model.deleteUser(userFragment.currentUser)
                supportFragmentManager.popBackStack(FRAGMENT_VIEW_TAG, POP_BACK_STACK_INCLUSIVE)
            }

            .setNegativeButton(R.string.delete_dialog_neg)
            { dialogInterface: DialogInterface, _ -> dialogInterface.dismiss() }

            .show()
    }

    override fun onBackPressed() {
        // avoid data loss in editor
        val backStackSize = supportFragmentManager.backStackEntryCount
        val currentFragment =
            if (backStackSize == 0) null
            else supportFragmentManager.fragments[backStackSize - 1]
        if (currentFragment is UserContentEditFragment) {
            currentFragment.savePrompt()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        model.clearResources()
    }
}

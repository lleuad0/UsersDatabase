package com.github.lleuad0.userstesttask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.lleuad0.userstesttask.databinding.FragmentUserListBinding


class UserListFragment : Fragment(), UserListAdapter.Listener {
    private var binding: FragmentUserListBinding? = null
    val modelUsers: ViewModelUsers by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.setTitle(R.string.app_name)

        val adapter = modelUsers.userListAdapter
        adapter.listener = this

        binding!!.recyclerView.adapter = adapter
        binding!!.recyclerView.layoutManager = LinearLayoutManager(context)
        binding!!.recyclerView.setHasFixedSize(true)
        binding!!.recyclerView.recycledViewPool.setMaxRecycledViews(0, 8)
        binding!!.recyclerView.onFlingListener =
            object : RecyclerView.OnFlingListener() {
                override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                    // swipe down
                    // excluding accidental touches
                    if (velocityY < -1000) {
                        Toast.makeText(
                            requireContext(),
                            R.string.toast_refreshing,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        modelUsers.refreshData()
                    }
                    return true
                }
            }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onUserCardClick(id: Int) = (activity as MainActivity).onUserCardClick(id)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
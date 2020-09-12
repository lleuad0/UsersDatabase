package com.github.lleuad0.userstesttask

import androidx.lifecycle.ViewModel
import com.github.lleuad0.userstesttask.database.UserEntity
import com.github.lleuad0.userstesttask.database.UsersRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class ViewModelUsers : ViewModel() {
    private val repository: UsersRepository
    private val compositeDisposable: CompositeDisposable
    val userListAdapter: UserListAdapter

    init {
        val dao = appDatabase.userDao()
        repository = UsersRepository(dao)
        compositeDisposable = CompositeDisposable()
        userListAdapter = UserListAdapter()
    }

    private fun getAllUsers() {
        val disposable = repository.getAllUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                Timber.i("Pulling all data from the database")
                userListAdapter.userList = it
            }
        compositeDisposable.add(disposable)
    }

    fun getUserById(id: Int, function: (user: UserEntity) -> Unit) {
        val disposable = repository.getUserById(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                Timber.i("Requested user by id $id, got $it")
                function(it)
            }
        compositeDisposable.add(disposable)
    }

    fun updateUser(user: UserEntity) {
        val disposable = repository.updateUser(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                Timber.i("Updated user $user")
            }
        compositeDisposable.add(disposable)
    }

    fun deleteUser(user: UserEntity) {
        val disposable = repository.deleteUser(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                Timber.i("Deleted user $user")
            }
        compositeDisposable.add(disposable)
    }

    fun refreshData() {
        repository.loadDataFromServer(getAllUsers())
    }

    fun clearResources() = compositeDisposable.clear()

}
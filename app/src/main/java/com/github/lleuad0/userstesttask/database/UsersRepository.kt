package com.github.lleuad0.userstesttask.database

import com.github.lleuad0.userstesttask.api.ApiHelper
import com.github.lleuad0.userstesttask.api.ServerResponse
import com.google.gson.GsonBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class UsersRepository(
    private val usersDao: UsersDao,
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
) {
    private var usersReceived: Int = 0
    private var usersInserted: Int = 0

    init {
        Timber.i("Repository initialized")
    }

    fun getAllUsers() = usersDao.getAllUsers()

    fun getUserById(id: Int) = usersDao.getUserById(id)

    fun updateUser(user: UserEntity) = usersDao.updateUser(user)

    fun deleteUser(user: UserEntity) = usersDao.deleteUser(user)

    fun loadDataFromServer(function: Unit? = null) {
        val disposable = ApiHelper.api.getServerResponse()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Timber.i("Received a response from the server: ${it.data}")
                    usersReceived = it.data.size()
                    parceResponse(it)
                },
                { exception -> Timber.e(exception) },
                { function })
        compositeDisposable.add(disposable)
    }

    private fun parceResponse(response: ServerResponse) {
        val data = response.data
        val builder = GsonBuilder().create()
        try {
            data.forEach {
                val user = builder.fromJson(it, UserEntity::class.java)
                insertUser(user)
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun insertUser(user: UserEntity) {
        val disposable = usersDao.insertUser(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnComplete {
                Timber.i("Inserted a user into the database: $user")
                usersInserted++
                if (usersInserted == usersReceived) {
                    compositeDisposable.clear()
                }
            }
            .subscribe()
        compositeDisposable.add(disposable)
    }

}

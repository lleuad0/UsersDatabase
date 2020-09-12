package com.github.lleuad0.userstesttask.database

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable


@Dao
interface UsersDao {
    @Query("SELECT * FROM $usersTable")
    fun getAllUsers(): Observable<List<UserEntity>>

    @Query("SELECT * FROM $usersTable WHERE id = :id")
    fun getUserById(id: Int): Maybe<UserEntity>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateUser(userEntity: UserEntity): Completable

    @Delete
    fun deleteUser(userEntity: UserEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(userEntity: UserEntity): Completable

}
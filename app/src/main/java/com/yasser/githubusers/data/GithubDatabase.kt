package com.yasser.githubusers.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yasser.githubusers.data.remote_key.local.UserRemoteKeyDao
import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity
import com.yasser.githubusers.data.user.data_source.local.UserDao
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import com.yasser.githubusers.data.user.model.useres.UserEntity

@Database(entities = [UserEntity::class, FollowUserCrossRef::class,UserRemoteKeyEntity::class], version = 1)
abstract class GithubDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun userRemoteKeyDao(): UserRemoteKeyDao

}
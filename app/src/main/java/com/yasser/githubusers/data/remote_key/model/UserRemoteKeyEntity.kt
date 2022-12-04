package com.yasser.githubusers.data.remote_key.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_remote_keys")
data class UserRemoteKeyEntity(
    @PrimaryKey val userName: String, val nextKey: Int?
)
package com.yasser.githubusers.data.remote_key.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yasser.githubusers.data.remote_key.model.UserRemoteKeyEntity

@Dao
interface UserRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceUserRemoteKey(remoteKey: UserRemoteKeyEntity)

    @Query("SELECT * FROM user_remote_keys WHERE userName =:userName")
    suspend fun findUserRemoteKeyByUserName(userName: String): UserRemoteKeyEntity?

    @Query("DELETE FROM user_remote_keys")
    suspend fun clearUserRemoteKey()
}
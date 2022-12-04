package com.yasser.githubusers.data.user.data_source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yasser.githubusers.data.user.model.useres.FollowUserCrossRef
import com.yasser.githubusers.data.user.model.useres.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceUsers(vararg users:UserEntity)

    @Query("SELECT * FROM users_table WHERE user_name LIKE :userName")
    fun findUserByUserNameAsFlow(userName:String):Flow<UserEntity?>

    @Query("SELECT * FROM users_table")
    fun getUsersAsPagingSource():PagingSource<Int,UserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowers(vararg followUserCrossRef:FollowUserCrossRef)

    @Query(
        "SELECT * FROM users_table " +
        "LEFT JOIN following_user_cross_ref ON users_table.user_name=following_user_cross_ref.following_user_name " +
        "WHERE following_user_cross_ref.user_name=:userName"
    )
    fun getUserFollowingAsPagingSource(userName: String):PagingSource<Int,UserEntity>

    @Query(
        "SELECT * FROM users_table " +
        "LEFT JOIN following_user_cross_ref ON users_table.user_name=following_user_cross_ref.user_name " +
        "WHERE following_user_cross_ref.following_user_name=:userName"
    )
    fun getUserFollowersAsPagingSource(userName:String):PagingSource<Int,UserEntity>

}
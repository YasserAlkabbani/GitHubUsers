package com.yasser.githubusers.manager

sealed class NavigationManager{

    object Users:NavigationManager(){
        const val route="users"
        const val userNameArg="userName"
        const val usersFilterArg="getUsersFilter"

        enum class UsersFilter{ Follower,Following }
    }

    object SearchForUser:NavigationManager(){
        const val route="search_for_user"
    }

}
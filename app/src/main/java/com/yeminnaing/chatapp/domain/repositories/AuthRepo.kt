package com.yeminnaing.chatapp.domain.repositories

interface AuthRepo {
    fun login(email:String,password:String,onSuccess:()->Unit,onFailure:(String)->Unit)
    fun register(email:String,password:String,userName:String,address:String,bio:String,onSuccess:()->Unit,onFailure:(String)->Unit)

    fun checkAuthStatus():Boolean

    fun signOut()
}
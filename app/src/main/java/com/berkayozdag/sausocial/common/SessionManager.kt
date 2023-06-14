package com.berkayozdag.sausocial.common

import android.content.SharedPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun setLogin(isLogin: Boolean) {
        sharedPreferences.edit().putBoolean(Constants.IS_LOGIN, isLogin).apply()
    }

    fun isLogin(): Boolean {
        return sharedPreferences.getBoolean(Constants.IS_LOGIN, false)
    }

    fun setUserId(id: Int) {
        sharedPreferences.edit().putInt(Constants.USER_ID, id).apply()
    }

    fun getUserId(): Int {
        return sharedPreferences.getInt(Constants.USER_ID, -1)
    }

    fun setUserName(name: String) {
        sharedPreferences.edit().putString(Constants.USER_NAME_AND_SURNAME, name).apply()
    }

    fun getUserName(): String {
        return sharedPreferences.getString(Constants.USER_NAME_AND_SURNAME, "") ?: ""
    }

    fun setUserProfileImage(profileImage: String) {
        sharedPreferences.edit().putString(Constants.USER_PROFILE_IMAGE, profileImage).apply()
    }

    fun getUserProfileImage(): String {
        return sharedPreferences.getString(Constants.USER_PROFILE_IMAGE, "") ?: ""
    }

}
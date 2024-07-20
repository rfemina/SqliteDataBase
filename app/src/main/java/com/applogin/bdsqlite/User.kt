package com.applogin.bdsqlite

class User(val id: Int = 0, var username: String = "", var password: String = "") {

    override fun toString(): String {
        return "User(id=$id, username='$username', password='$password')"
    }
}
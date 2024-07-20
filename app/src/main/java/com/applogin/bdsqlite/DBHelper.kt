package com.applogin.bdsqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "database.db", null, 1) {

    val sql = arrayOf(
        "CREATE TABLE user (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)",
        "INSERT INTO user (username, password) VALUES ('user', 'pass')",
        "INSERT INTO user (username, password) VALUES ('admin', 'pwd')"
    )

    override fun onCreate(db: SQLiteDatabase) {
        sql.forEach {
            db.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE user")
        onCreate(db)
    }

    fun userInsert(username: String, password: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        val res = db.insert("user", null, contentValues)
        db.close()
        return res
    }

    fun userUpdate(id: Int, username: String, password: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("password", password)
        val res = db.update("user", contentValues, "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun userDelete(id: Int): Int {
        val db = this.writableDatabase
        val res = db.delete("user", "id=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    fun userSelectAll(): Cursor {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM user", null)
        db.close()
        return c
    }

    fun userSelectByID(id: Int): Cursor {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM user WHERE id =?", arrayOf(id.toString()))
        db.close()
        return c
    }

    fun userObjectSelectByID(id: Int): User {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM user WHERE id =?", arrayOf(id.toString()))
        var user = User()
        if (c.count == 1) {
            c.moveToFirst()
            val idIndex = c.getColumnIndex("id")
            val usernameIndex = c.getColumnIndex("username")
            val passwordIndex = c.getColumnIndex("password")

            val id = c.getInt(idIndex)
            val username = c.getString(usernameIndex)
            val password = c.getString(passwordIndex)

            user = User(id, username, password)
        }
        db.close()
        return user
    }

    fun userListSelectAll(): ArrayList<User> {
        val db = this.readableDatabase
        val c = db.rawQuery("SELECT * FROM user", null)
        val listUser: ArrayList<User> = ArrayList()

        if (c.count > 0) {
            c.moveToFirst()
            do {
                val idIndex = c.getColumnIndex("id")
                val usernameIndex = c.getColumnIndex("username")
                val passwordIndex = c.getColumnIndex("password")

                val id = c.getInt(idIndex)
                val username = c.getString(usernameIndex)
                val password = c.getString(passwordIndex)
                listUser.add(User(id, username, password))
            } while (c.moveToNext())
        }
        db.close()
        return listUser
    }

}
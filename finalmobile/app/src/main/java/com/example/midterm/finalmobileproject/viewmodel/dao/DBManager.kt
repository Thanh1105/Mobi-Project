package com.example.midterm.finalmobileproject.viewmodel.dao

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList


class DBManager {
    private var dbHelper: DatabaseHelper? = null
    private var context: Context? = null
    private var database: SQLiteDatabase? = null
    constructor (c: Context) {
        context = c
    }

    @Throws(SQLException::class)
    fun open(): DBManager? {
        dbHelper = DatabaseHelper(context!!)
        database = dbHelper!!.getWritableDatabase()
        return this
    }
    fun close() {
        dbHelper!!.close()
    }
    fun insert(qrcode : String) {
        val contentValues = ContentValues()
        contentValues.put(dbHelper?.BOOKING_ID, qrcode)
        database!!.insert(dbHelper?.TABLE_NAME, null, contentValues)
    }

    fun read(): String {
        val cursorCourses = database!!.rawQuery("SELECT * FROM " + dbHelper?.TABLE_NAME, null)
        val bookingArrayList: ArrayList<String> = ArrayList<String>()
        if (cursorCourses.moveToFirst()) {
            do {
                bookingArrayList.add(
                    cursorCourses.getString(1)
                )
            } while (cursorCourses.moveToNext())
        }
        cursorCourses.close()
        return bookingArrayList[bookingArrayList.size-1]
    }

}
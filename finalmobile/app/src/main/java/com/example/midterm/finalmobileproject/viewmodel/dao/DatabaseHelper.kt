package com.example.midterm.finalmobileproject.viewmodel.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "SAVE.DB", null, 1) {
    val TABLE_NAME = "BOOKING"
    val _ID = "_id"
    val BOOKING_ID = "booking_id"
    val DB_NAME = "SAVE.DB"
    val DB_VERSION = 1

    private val CREATE_TABLE = ("create table "
            + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BOOKING_ID
            + " TEXT NOT NULL );")


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}
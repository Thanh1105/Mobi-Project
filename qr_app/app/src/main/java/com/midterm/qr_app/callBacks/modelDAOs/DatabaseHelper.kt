package com.midterm.qr_app.callBacks.modelDAOs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "QRSCAN.DB", null, 1) {
    val TABLE_NAME = "DEVICE"
    val _ID = "_id"
    val DEVICE_ID = "device_id"
    val DB_NAME = "QRSCAN.DB"
    val DB_VERSION = 1

    private val CREATE_TABLE = ("create table "
            + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DEVICE_ID
            + " TEXT NOT NULL );")


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}
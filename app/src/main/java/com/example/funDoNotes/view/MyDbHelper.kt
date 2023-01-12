package com.example.funDoNotes.view

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


class MyDbHelper(var context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "UserNotesDB"
        private val TABLE_NAME = "NotesTable"
        private val KEY_ID = "Note_Id"
        private val KEY_TITLE = "Title"
        private val KEY_SUBTITLE = "Sub_Title"
        private val KEY_CONTENT = "Content"
        private val KEY_TIMESTAMP = "Timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " TEXT," + KEY_TITLE + " TEXT,"
                + KEY_SUBTITLE + " TEXT," + KEY_CONTENT + " TEXT,"
                + KEY_TIMESTAMP + " TEXT" + ")")

        db?.execSQL(CREATE_CONTACTS_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)

    }

    fun addNote(
        noteid: String,
        title: String,
        subtitle: String,
        content: String,
        timestamp: String
    ) {
        var db: SQLiteDatabase = this.writableDatabase
        var cv = ContentValues()

        cv.put(KEY_ID, noteid)
        cv.put(KEY_TITLE, title)
        cv.put(KEY_SUBTITLE, subtitle)
        cv.put(KEY_CONTENT, content)
        cv.put(KEY_TIMESTAMP, timestamp)
        var result: Int = db.insert(TABLE_NAME, null, cv).toInt()

        if (result == -1) {
            Toast.makeText(context, "Failed to add to SQLite", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show()
        }

    }

    fun readAllSQLiteData(): android.database.Cursor? {
        val query: String = "SELECT * FROM " + TABLE_NAME
        var db: SQLiteDatabase = this.readableDatabase

        var cursor: android.database.Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }


    fun updateData(
        noteid: String,
        title: String,
        subtitle: String,
        content: String,
        timestamp: String
    ) {
        var db: SQLiteDatabase = this.writableDatabase
        var cv = ContentValues()

        cv.put(KEY_TITLE, title)
        cv.put(KEY_SUBTITLE, subtitle)
        cv.put(KEY_CONTENT, content)
        cv.put(KEY_TIMESTAMP, timestamp)

        var result = db.update(TABLE_NAME, cv, KEY_ID + "=?", arrayOf(noteid.toString())).toInt()
        if (result == -1) {
            Toast.makeText(context, "Failed to update", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteOneRow(noteid: String) {
        var db: SQLiteDatabase = this.writableDatabase
        var cv = ContentValues()

        var result = db.delete(TABLE_NAME, KEY_ID + "=?", arrayOf(noteid.toString())).toInt()
        if (result == -1) {
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Note Deleted!", Toast.LENGTH_SHORT).show()
        }
    }
}
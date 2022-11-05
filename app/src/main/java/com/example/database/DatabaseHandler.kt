package com.example.database

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "EmployeeDatabase"
        private const val TABLE_CONTACTS = "EmployeeTable"

        private const val KEY_ID = "_id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // SQL Command. To Create Table.
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }

    /*
    Function To Insert Data.
     */
    fun addEmployee(emp: EmpModelClass): Long {
        val db = this.writableDatabase

        // Content Values is kinda like a container to store the data for the database's table. initially.
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name) // EmpModelClass Name
        contentValues.put(KEY_EMAIL, emp.email) // EmpModelClass Email

        // Inserting Row
        //db.insert stores long value!
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack

        db.close() // Closing database connection
        return success
    }

    /*
    Method To Read Data
     */
    fun viewEmployee(): ArrayList<EmpModelClass> {

        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()

        val selectQuery = "SELECT * FROM $TABLE_CONTACTS"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL))

                val emp = EmpModelClass(id = id, name = name, email = email)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    /*
    Function To Update Employee
     */
    fun updateEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, emp.name) // EmpModelClass Name
        contentValues.put(KEY_EMAIL, emp.email) // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues, KEY_ID + "=" + emp.id, null)

        db.close() // Closing Database Connection

        return success
    }

    /*
    Function To Delete Database's Table record.
     */
    fun deleteEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id) // EmpModelClass Id

        // Deleting Row
        val success = db.delete(TABLE_CONTACTS, KEY_ID + "=" + emp.id, null)

        db.close() // Closing Database Connection
        return success
    }


}
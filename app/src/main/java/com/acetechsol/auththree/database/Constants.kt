package com.acetechsol.auththree.database

object Constants {
    //db name
    const val DB_NAME = "STAFF_DB"
    //db version
    const val DB_VERSION = 1
    //table staff Details
    const val STAFF_TABLE_NAME = "STAFF_TABLE"
    //columns/fields of table
    const val STAFF_ID = "STAFF_ID"
    const val STAFF_IMAGE = "STAFF_IMAGE"
    const val STAFF_SURNAME = "STAFF_SURNAME"
    const val STAFF_OTHER_NAMES = "STAFF_OTHER_NAMES"
    const val STAFF_PHONE_NUMBER = "STAFF_PHONE_NUMBER"
    const val STAFF_DEPARTMENT = "STAFF_DEPARTMENT"
    const val STAFF_ADDED_TIMESTAMP = "STAFF_ADDED_TIMESTAMP"
    const val STAFF_UPDATED_TIMESTAMP = "STAFF_UPDATED_TIMESTAMP"

    //table login
    const val LOGIN_TABLE_NAME = "LOGIN_TABLE"
    //columns/fields of table

    const val USER_ID = "USER_ID"
    const val USER_IMAGE = "IMAGE"
    const val FULL_NAME = "FULL_NAME"
    const val PASSWORD = "PASSWORD"
    const val USER_DEPARTMENT = "DEPARTMENT"
    const val USER_ADDED_TIMESTAMP = "STAFF_ADDED_TIMESTAMP"
    const val USER_UPDATED_TIMESTAMP = "STAFF_UPDATED_TIMESTAMP"


    //table attendance
    const val ATTENDANCE_TABLE_NAME = "ATTENDANCE_TABLE"
    //columns/fields of table
    const val ATTENDANCE_ID = "ATTENDANCE_ID"
    const val DATE_IN = "WORK_DATE_IN"
    const val DATE_OUT = "WORK_DATE_OUT"
    const val TIME_IN = "TIME_IN"
    const val TIME_OUT = "TIME_OUT"
    const val CASH_IN = "CASH_IN"
    const val CASH_OUT = "CASH_OUT"
    const val PHONE_DESC = "PHONE_DESC"
    const val SESSION = "SESSION"
    const val STATUS = "STATUS"
    const val PERMISSION = "PERMISSION"



    //create staff table query
    const val CREATE_TABLE = (
            " CREATE TABLE " + STAFF_TABLE_NAME + " ("
            + STAFF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + STAFF_IMAGE + " TEXT,"
            + STAFF_SURNAME + " TEXT, "
            + STAFF_OTHER_NAMES + " TEXT, "
            + STAFF_PHONE_NUMBER + " TEXT, "
            + STAFF_DEPARTMENT + " TEXT, "
            + STAFF_ADDED_TIMESTAMP + " TEXT, "
            + STAFF_UPDATED_TIMESTAMP + " TEXT "
            + ") "
            )



    //create login table query

    const val CREATE_LOGIN_TABLE = (
            " CREATE TABLE " + LOGIN_TABLE_NAME + " ("
                    + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + USER_IMAGE + " TEXT,"
                    + FULL_NAME + " TEXT, "
                    + PASSWORD + " TEXT, "
                    + USER_DEPARTMENT + " TEXT, "
                    + USER_ADDED_TIMESTAMP + " TEXT, "
                    + USER_UPDATED_TIMESTAMP + " TEXT"
                    + ")"
            )



    //create attendance table query
    const val CREATE_ATTENDANCE_TABLE = (
            " CREATE TABLE " + ATTENDANCE_TABLE_NAME + " ("
                    + ATTENDANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + STAFF_ID + " INTEGER,"
                    + DATE_IN + " TEXT,"
                    + DATE_OUT + " TEXT,"
                    + TIME_IN + " TEXT, "
                    + TIME_OUT + " TEXT, "
                    + CASH_IN + " TEXT, "
                    + CASH_OUT + " TEXT, "
                    + PHONE_DESC + " TEXT, "
                    + SESSION + " TEXT, "
                    + STATUS + " TEXT, "
                    + PERMISSION + " TEXT"
                    + ") "
            )



}
package com.acetechsol.auththree.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.acetechsol.auththree.models.*

class DbHelper(context: Context?):SQLiteOpenHelper(
    context,
    Constants.DB_NAME,
    null,
    Constants.DB_VERSION

) {

    private lateinit var modelStaffRecord: ModelStaffRecord
    private lateinit var modelAttendanceRecord: ModelAttendanceRecord
    private lateinit var modelLoginRecord: ModelLoginRecord

    override fun onCreate(db: SQLiteDatabase) {
        //create table on that db
        db.execSQL(Constants.CREATE_TABLE)
        db.execSQL(Constants.CREATE_ATTENDANCE_TABLE)
        db.execSQL(Constants.CREATE_LOGIN_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //upgrade database if there is any structure change, change in db version
        //drop older table if exist
        db.execSQL("DROP TABLE " + Constants.STAFF_TABLE_NAME)
        db.execSQL("DROP TABLE" + Constants.LOGIN_TABLE_NAME)
        db.execSQL("DROP TABLE" + Constants.ATTENDANCE_TABLE_NAME)
        onCreate(db)
    }

    //insert records into table
    fun insertRecord(
        image: String?,
        surname: String?,
        otherName: String?,
        phoneNumber: String?,
        department: String?,
        addedTimeStamp: String,
        updatedTimeStamp: String
    ):Long{
        //get writable database to write data
        val db = this.writableDatabase
        val values = ContentValues()
        //id will be inserted automatically
        //insert data
        values.put(Constants.STAFF_IMAGE, image)
        values.put(Constants.STAFF_SURNAME, surname)
        values.put(Constants.STAFF_OTHER_NAMES, otherName)
        values.put(Constants.STAFF_PHONE_NUMBER, phoneNumber)
        values.put(Constants.STAFF_DEPARTMENT, department)
        values.put(Constants.STAFF_ADDED_TIMESTAMP, addedTimeStamp)
        values.put(Constants.STAFF_UPDATED_TIMESTAMP, updatedTimeStamp)

        //insert row, it will return record id of saved record
        val id = db.insert(Constants.STAFF_TABLE_NAME, null, values)
        //close db connection
        db.close()
        //return id of inserted record
        return id
    }

    //update data to db
    fun updateStaffDetails(id:String,
                           image:String?,
                           surname: String?,
                           otherName: String?,
                           phoneNumber: String?,
                           department: String?,
                           addedTimeStamp: String,
                           updatedTimeStamp: String
    ):Long{
        //get writeable database
        val db = this.writableDatabase
        val values = ContentValues()

        //insert data

        values.put(Constants.STAFF_IMAGE, image)
        values.put(Constants.STAFF_SURNAME, surname)
        values.put(Constants.STAFF_OTHER_NAMES, otherName)
        values.put(Constants.STAFF_PHONE_NUMBER, phoneNumber)
        values.put(Constants.STAFF_DEPARTMENT, department)
        values.put(Constants.STAFF_ADDED_TIMESTAMP, addedTimeStamp)
        values.put(Constants.STAFF_UPDATED_TIMESTAMP, updatedTimeStamp)

        //update
        val idd =  db.update(
            Constants.STAFF_TABLE_NAME, values,
            "${Constants.STAFF_ID} =?",
            arrayOf(id)).toLong()
        db.close()
        return idd

    }

    //get Individual data
    fun getStaffDetails(id:String):ModelStaffRecord{
        ///it will return list or records since we have used return type ArrayList<ModelStaffRecord>

        //query to select all records
        val selectQuery = "SELECT * FROM ${Constants.STAFF_TABLE_NAME} WHERE ${Constants.STAFF_ID} = $id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            modelStaffRecord = ModelStaffRecord(
                ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_IMAGE)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_SURNAME)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_OTHER_NAMES)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_PHONE_NUMBER)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_DEPARTMENT)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_ADDED_TIMESTAMP)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_UPDATED_TIMESTAMP))
            )
        }
        //close db
        cursor.close()
        db.close()
        //return the queried result list
        return modelStaffRecord
    }
/*
    fun getSomeStaffDetails(id:String):ModelStaffRecord{
        ///it will return list or records since we have used return type ArrayList<ModelStaffRecord>

        //query to select all records
        val selectQuery = "SELECT ${Constants.STAFF_IMAGE}, " +
                "${Constants.STAFF_SURNAME}, " +
                "${Constants.STAFF_OTHER_NAMES}," +
                "${Constants.STAFF_DEPARTMENT} FROM " +
                "${Constants.STAFF_TABLE_NAME} WHERE " +
                "${Constants.STAFF_ID} = $id"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            modelStaffRecord = ModelStaffRecord(
                "",
                "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_IMAGE)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_SURNAME)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_OTHER_NAMES)),
                "",
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_DEPARTMENT)),
                "",
                ""
            )
        }
        //close db
        cursor.close()
        db.close()
        //return the queried result list
        return modelStaffRecord
    }

 */

    //get all data
    fun getAllStaffDetails(orderBy:String):ArrayList<ModelStaffRecord>{
        //order by query will allow to sort data e.g. newest first, name ascending/descending
        ///it will return list or records since we have used return type ArrayList<ModelStaffRecord>
        val recordList = ArrayList<ModelStaffRecord>()
        //query to select all records
        val selectQuery = "SELECT * FROM ${Constants.STAFF_TABLE_NAME} ORDER BY $orderBy"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val modelStaffRecord = ModelStaffRecord(
                    ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_IMAGE)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_SURNAME)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_OTHER_NAMES)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_PHONE_NUMBER)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_DEPARTMENT)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_ADDED_TIMESTAMP)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_UPDATED_TIMESTAMP))
                )
                //add record to list
                recordList.add(modelStaffRecord)
            }while (cursor.moveToNext())
        }
        //close db
        cursor.close()
        db.close()
        //return the queried result list
        return recordList
    }

    fun getAllStaffDetails():ArrayList<ModelStaffRecord>{
        //order by query will allow to sort data e.g. newest first, name ascending/descending
        ///it will return list or records since we have used return type ArrayList<ModelStaffRecord>
        val recordList = ArrayList<ModelStaffRecord>()
        //query to select all records
        val selectQuery = "SELECT * FROM ${Constants.STAFF_TABLE_NAME}"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val modelStaffRecord = ModelStaffRecord(
                    ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_IMAGE)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_SURNAME)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_OTHER_NAMES)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_PHONE_NUMBER)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_DEPARTMENT)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_ADDED_TIMESTAMP)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_UPDATED_TIMESTAMP))
                )
                //add record to list
                recordList.add(modelStaffRecord)
            }while (cursor.moveToNext())
        }
        //close db
        cursor.close()
        db.close()
        //return the queried result list
        return recordList
    }

    //search data
    fun searchStaffRecords(query: String, orderBy: String):ArrayList<ModelStaffRecord>{
        //search on the basis of value entered in the searchView
        ///it will return list or records since we have used return type ArrayList<ModelStaffRecord>
        val recordList = ArrayList<ModelStaffRecord>()
        //query to select all records
        val selectQuery = "SELECT * FROM ${Constants.STAFF_TABLE_NAME} " +
                "WHERE ${Constants.STAFF_SURNAME} LIKE '$query%' " +
                "OR ${Constants.STAFF_OTHER_NAMES} LIKE '$query%'" +
                "ORDER BY $orderBy"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val modelStaffRecord = ModelStaffRecord(
                    ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_IMAGE)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_SURNAME)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_OTHER_NAMES)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_PHONE_NUMBER)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_DEPARTMENT)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_ADDED_TIMESTAMP)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_UPDATED_TIMESTAMP))
                )
                //add record to list
                recordList.add(modelStaffRecord)
            }while (cursor.moveToNext())
        }
        cursor.close()
        //close db
        db.close()
        //return the queried result list
        return recordList
    }

    //get total number of records
    fun countStaffRecords():Int{
        val selectQuery = "SELECT * FROM ${Constants.STAFF_TABLE_NAME}"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val numberOfStaff = cursor.count
        cursor.close()
        db.close()
        return numberOfStaff
    }

    //delete (single) record using id
    fun deleteStaffRecord(id:String){
        val db = writableDatabase
        db.delete(
            Constants.STAFF_TABLE_NAME,
            "${Constants.STAFF_ID} = ?",
            arrayOf(id)
        )
        db.delete(
            Constants.ATTENDANCE_TABLE_NAME,
            "${Constants.STAFF_ID} = ?",
            arrayOf(id)
        )
        db.close()
    }

    //delete all records
    /*
    fun deleteAllStaffRecords(){
        val db = writableDatabase
        db.execSQL("DELETE FROM ${Constants.TABLE_NAME}")
        db.close()
    }

     */


    //ATTENDANCE
    //insert Attendance records
    fun insertAttendanceRecord(
        staffId: Int,
        dateIn: String?,
        dateOut: String?,
        timeIn: String,
        timeOut: String,
        cashIn: String,
        cashOut: String,
        phoneDesc: String,
        session: String,
        status: String,
        permission: String
    ):Long{
        val db = writableDatabase
        val values = ContentValues()

        values.put(Constants.STAFF_ID, staffId)
        values.put(Constants.DATE_IN, dateIn)
        values.put(Constants.DATE_OUT, dateOut)
        values.put(Constants.TIME_IN, timeIn)
        values.put(Constants.TIME_OUT, timeOut)
        values.put(Constants.CASH_IN, cashIn)
        values.put(Constants.CASH_OUT, cashOut)
        values.put(Constants.PHONE_DESC, phoneDesc)
        values.put(Constants.SESSION, session)
        values.put(Constants.STATUS, status)
        values.put(Constants.PERMISSION, permission)

        val id = db.insert(Constants.ATTENDANCE_TABLE_NAME, null, values)
        db.close()
        return id
    }


    //update Attendance records
    fun updateAttendanceRecord(
        attendanceId: String,
        staffId: String,
        dateIn: String,
        dateOut: String,
        timeIn: String,
        timeOut: String,
        cashIn: String,
        cashOut: String,
        session: String,
        status: String
    ):Long{
        val db = writableDatabase
        val values = ContentValues()
        values.put(Constants.STAFF_ID, staffId)
        values.put(Constants.DATE_IN, dateIn)
        values.put(Constants.DATE_OUT, dateOut)
        values.put(Constants.TIME_IN, timeIn)
        values.put(Constants.TIME_OUT, timeOut)
        values.put(Constants.CASH_IN, cashIn)
        values.put(Constants.CASH_OUT, cashOut)
        values.put(Constants.SESSION, session)
        values.put(Constants.STATUS, status)



        val id = db.update(Constants.ATTENDANCE_TABLE_NAME,values,
            "${Constants.ATTENDANCE_ID} = ?",
            arrayOf(attendanceId)).toLong()

        db.close()
        return id
    }

    fun getAttendanceRecords(staffID: String, currDate: String, session: String):ModelAttendanceRecord{
        val selectQuery = "SELECT * FROM ${Constants.ATTENDANCE_TABLE_NAME}" +
                " WHERE ${Constants.STAFF_ID} = '$staffID' AND ${Constants.DATE_IN} = '$currDate'" +
                " AND ${Constants.SESSION} = '$session' "
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor.moveToFirst()){
            modelAttendanceRecord = ModelAttendanceRecord(
                "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.ATTENDANCE_ID)),
                "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE_IN)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE_OUT)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_IN)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_OUT)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_IN)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_OUT)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.PHONE_DESC)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.SESSION)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.STATUS)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.PERMISSION))
            )
        }
        cursor.close()
        db.close()
        return modelAttendanceRecord

    }

    fun getStaffLastAttendanceRecords(staffID: String):ModelAttendanceRecord{
        val selectQuery = "SELECT * FROM ${Constants.ATTENDANCE_TABLE_NAME}" +
                " WHERE ${Constants.STAFF_ID} = $staffID ORDER BY ${Constants.ATTENDANCE_ID} DESC LIMIT 1"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if(cursor.moveToFirst()){
            modelAttendanceRecord = ModelAttendanceRecord(
                "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.ATTENDANCE_ID)),
                "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE_IN)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE_OUT)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_IN)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_OUT)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_IN)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_OUT)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.PHONE_DESC)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.SESSION)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.STATUS)),
                ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.PERMISSION))
            )
        }
        cursor.close()
        db.close()
        return modelAttendanceRecord

    }

    /*
    fun getStaffLastAttendanceId(staffID: String):Int{
        val selectQuery = "SELECT ${Constants.ATTENDANCE_ID} FROM ${Constants.ATTENDANCE_TABLE_NAME}" +
                " WHERE ${Constants.STAFF_ID} = $staffID ORDER BY ${Constants.ATTENDANCE_ID} DESC LIMIT 1"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        var attendanceId = 0
        if(cursor.moveToFirst()){
            attendanceId =  cursor.getInt(cursor.getColumnIndexOrThrow(Constants.ATTENDANCE_ID))
        }
        cursor.close()
        db.close()
        return attendanceId
    }

     */

    fun isStaffCheckedIn(id: String, currDate: String, session: String):Boolean{
        val selectQuery = "SELECT * FROM ${Constants.ATTENDANCE_TABLE_NAME}" +
                " WHERE ${Constants.STAFF_ID} = $id AND ${Constants.DATE_IN} = '$currDate'" +
                " AND ${Constants.SESSION} = '$session'"
        val response: Boolean
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        response = cursor.moveToFirst()
        cursor.close()
        db.close()
        return response
    }

    /*
    fun isStaffCheckedOut(id: String):Boolean{
        val selectQuery = "SELECT * FROM ${Constants.ATTENDANCE_TABLE_NAME}" +
                " WHERE ${Constants.ATTENDANCE_ID} = $id " +
                " AND ${Constants.DATE_OUT} IS NOT ''" +
                " AND ${Constants.STATUS} IS NOT 'ABSENT'"
        val response: Boolean
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        response = cursor.moveToFirst()
        cursor.close()
        db.close()
        return response
    }

     */

    /*
    fun isStaffAbsentWithPermission(id: String, currDate: String, session: String):Boolean{
        val selectQuery = "SELECT * FROM ${Constants.ATTENDANCE_TABLE_NAME}" +
                " WHERE ${Constants.ATTENDANCE_ID} = $id " +
                " AND ${Constants.STATUS} = 'ABSENT'" +
                " AND ${Constants.DATE_IN} = '$currDate'" +
                " AND ${Constants.SESSION} = '$session'" +
                " AND ${Constants.PERMISSION} IS NOT ''"
        val response: Boolean
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        response = cursor.moveToFirst()
        cursor.close()
        db.close()
        return response
    }

     */

    fun getDailyAttendanceRecords(fromDate:String, toDate:String, orderBy: String): ArrayList<ModelStaffAttendance>{
        val selectQuery = "SELECT A.STAFF_ID, A.STAFF_SURNAME || \" \" || A.STAFF_OTHER_NAMES AS STAFF_NAME, A.STAFF_DEPARTMENT," +
                " B.ATTENDANCE_ID, B.WORK_DATE_IN, B.SESSION, B.TIME_IN, B.CASH_IN, B.PHONE_DESC, B.TIME_OUT, B.CASH_OUT," +
                " B.STATUS, B.PERMISSION" +
                " FROM STAFF_TABLE AS A" +
                " JOIN ATTENDANCE_TABLE AS B" +
                " WHERE A.STAFF_ID = B.STAFF_ID" +
                " AND (substr(${Constants.DATE_IN}, 7, 4) || \"-\" || substr(${Constants.DATE_IN}, 4, 2) || \"-\" || substr(${Constants.DATE_IN}, 1, 2)) BETWEEN '$fromDate' AND '$toDate'" +
                "ORDER BY $orderBy"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val recordList = ArrayList<ModelStaffAttendance>()
        if(cursor.moveToFirst()){
            do {
                val modelStaffAttendance = ModelStaffAttendance(
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.ATTENDANCE_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow("STAFF_NAME")),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_DEPARTMENT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE_IN)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.SESSION)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_IN)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_IN)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.PHONE_DESC)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_OUT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_OUT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STATUS)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.PERMISSION))

                )
                recordList.add(modelStaffAttendance)
            }while (cursor.moveToNext())

        }
        cursor.close()
        db.close()
        return recordList

    }

    fun getPresentAttendanceRecords(fromDate:String, toDate:String, orderBy: String, department: String): ArrayList<ModelStaffAttendance>{
        val selectQuery = "SELECT A.STAFF_ID, A.STAFF_SURNAME || \" \" || A.STAFF_OTHER_NAMES AS STAFF_NAME, A.STAFF_DEPARTMENT," +
                " B.ATTENDANCE_ID, B.WORK_DATE_IN, B.SESSION, B.TIME_IN, B.CASH_IN, B.PHONE_DESC, B.TIME_OUT, B.CASH_OUT," +
                " B.STATUS, B.PERMISSION" +
                " FROM STAFF_TABLE AS A" +
                " JOIN ATTENDANCE_TABLE AS B" +
                " WHERE A.STAFF_ID = B.STAFF_ID AND A.STAFF_DEPARTMENT = '$department'" +
                " AND (substr(${Constants.DATE_IN}, 7, 4) || \"-\" || substr(${Constants.DATE_IN}, 4, 2) || \"-\" || substr(${Constants.DATE_IN}, 1, 2)) BETWEEN '$fromDate' AND '$toDate'" +
                " AND B.${Constants.STATUS} = 'PRESENT'" +
                "ORDER BY $orderBy"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val recordList = ArrayList<ModelStaffAttendance>()
        if(cursor.moveToFirst()){
            do {
                val modelStaffAttendance = ModelStaffAttendance(
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.ATTENDANCE_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow("STAFF_NAME")),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_DEPARTMENT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE_IN)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.SESSION)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_IN)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_IN)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.PHONE_DESC)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_OUT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_OUT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STATUS)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.PERMISSION))

                )
                recordList.add(modelStaffAttendance)
            }while (cursor.moveToNext())

        }
        cursor.close()
        db.close()
        return recordList

    }

    fun getAbsentAttendanceRecords(fromDate:String, toDate:String, orderBy: String, department: String): ArrayList<ModelStaffAttendance>{
        val selectQuery = "SELECT A.STAFF_ID, A.STAFF_SURNAME || \" \" || A.STAFF_OTHER_NAMES AS STAFF_NAME, A.STAFF_DEPARTMENT," +
                " B.ATTENDANCE_ID, B.WORK_DATE_IN, B.SESSION, B.TIME_IN, B.CASH_IN, B.PHONE_DESC, B.TIME_OUT, B.CASH_OUT," +
                " B.STATUS, B.PERMISSION" +
                " FROM STAFF_TABLE AS A" +
                " JOIN ATTENDANCE_TABLE AS B" +
                " WHERE A.STAFF_ID = B.STAFF_ID AND A.STAFF_DEPARTMENT = '$department'" +
                " AND (substr(${Constants.DATE_IN}, 7, 4) || \"-\" || substr(${Constants.DATE_IN}, 4, 2) || \"-\" || substr(${Constants.DATE_IN}, 1, 2)) BETWEEN '$fromDate' AND '$toDate'" +
                " AND ${Constants.STATUS} = 'ABSENT'" +
                "ORDER BY '$orderBy'"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val recordList = ArrayList<ModelStaffAttendance>()
        if(cursor.moveToFirst()){
            do {
                val modelStaffAttendance = ModelStaffAttendance(
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.ATTENDANCE_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow("STAFF_NAME")),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_DEPARTMENT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE_IN)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.SESSION)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_IN)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_IN)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.PHONE_DESC)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_OUT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_OUT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STATUS)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.PERMISSION))

                )
                recordList.add(modelStaffAttendance)
            }while (cursor.moveToNext())

        }
        cursor.close()
        db.close()
        return recordList

    }

    fun countStaffAttendance(fromDate: String, toDate: String) :ArrayList<ModelCountStaffAttendance>{
        val selectQuery = "SELECT ${Constants.STAFF_ID}, COUNT(${Constants.STAFF_ID}) as 'COUNT' FROM ${Constants.ATTENDANCE_TABLE_NAME}" +
                " WHERE (substr(${Constants.DATE_IN}, 7, 4) || \"-\" || substr(${Constants.DATE_IN}, 4, 2) || \"-\" || substr(${Constants.DATE_IN}, 1, 2)) BETWEEN '$fromDate' AND '$toDate'" +
                " AND ${Constants.STATUS} = 'PRESENT' GROUP BY ${Constants.STAFF_ID}"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val countList = ArrayList<ModelCountStaffAttendance>()
        if (cursor.moveToFirst()){
            do{
                val modelCountStaffAttendance = ModelCountStaffAttendance(
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                    "" + cursor.getInt(cursor.getColumnIndexOrThrow("COUNT"))
                )
                countList.add(modelCountStaffAttendance)
            }while(cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return countList
    }
/*
    fun getDailyAttendanceRecords(): ArrayList<ModelAttendanceRecord>{
        val selectQuery = "SELECT * FROM ${Constants.ATTENDANCE_TABLE_NAME}"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val recordList = ArrayList<ModelAttendanceRecord>()
        if(cursor.moveToFirst()){
            do {
                modelAttendanceRecord = ModelAttendanceRecord(
                    "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.ATTENDANCE_ID)),
                    "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.STAFF_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE_IN)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.DATE_OUT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_IN)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.TIME_OUT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_IN)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.CASH_OUT)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.SESSION)),
                    ""+cursor.getString(cursor.getColumnIndexOrThrow(Constants.STATUS))
                )
                recordList.add(modelAttendanceRecord)
            }while (cursor.moveToNext())

        }
        cursor.close()
        db.close()
        return recordList

    }

 */

    fun countDailyAttendancePresent(date:String, session: String):Int{
        val db = readableDatabase
        val selectQuery = "SELECT * FROM ${Constants.ATTENDANCE_TABLE_NAME} " +
                "WHERE ${Constants.DATE_IN} = '$date' AND ${Constants.SESSION} = '$session'" +
                " AND ${Constants.STATUS} = 'PRESENT'"
        val cursor = db.rawQuery(selectQuery, null)
        val numberPresent = cursor.count
        cursor.close()
        db.close()
        return numberPresent
    }
/*
    fun countTotalAttendance():Int{
        val db = readableDatabase
        val selectQuery = "SELECT * FROM ${Constants.ATTENDANCE_TABLE_NAME} "
        val cursor = db.rawQuery(selectQuery, null)
        val numberPresent = cursor.count
        cursor.close()
        db.close()
        return numberPresent
    }

 */

    fun countTotalStaffAttendance(staffID: String):Int{
        val db = readableDatabase
        val selectQuery = "SELECT * FROM ${Constants.ATTENDANCE_TABLE_NAME}" +
                "  WHERE ${Constants.STAFF_ID} = $staffID "
        val cursor = db.rawQuery(selectQuery, null)
        val numberPresent = cursor.count
        cursor.close()
        db.close()
        return numberPresent
    }


    //LOGIN
    fun getUserIDifExist(fullName: String, password: String):String{
        val userId: String
        val db = readableDatabase
        val selectQuery = "SELECT ${Constants.USER_ID} FROM" +
                " ${Constants.LOGIN_TABLE_NAME}" +
                " WHERE ${Constants.FULL_NAME} = '$fullName' AND" +
                " ${Constants.PASSWORD} = '$password'"
        val cursor = db.rawQuery(selectQuery, null)
        userId = if (cursor.moveToFirst()){ cursor.getString(cursor.getColumnIndexOrThrow(Constants.USER_ID))}
        else{""}
        cursor.close()
        db.close()
        return userId

    }

    fun getAppUserDetails(userId:String):ModelLoginRecord{
        ///it will return list or records since we have used return type ArrayList<ModelStaffRecord>

        //query to select all records
        val selectQuery = "SELECT * FROM ${Constants.LOGIN_TABLE_NAME} WHERE ${Constants.USER_ID} = '$userId'"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            modelLoginRecord = ModelLoginRecord(
                ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.USER_ID)),
                "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.USER_IMAGE)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.FULL_NAME)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.PASSWORD)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.USER_DEPARTMENT)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.USER_ADDED_TIMESTAMP)),
                ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.USER_UPDATED_TIMESTAMP))
            )
        }
        //close db
        cursor.close()
        db.close()
        //return the queried result list
        return modelLoginRecord
    }

    fun getUserRecords(): ArrayList<ModelLoginRecord>{
        val selectQuery = "SELECT * FROM ${Constants.LOGIN_TABLE_NAME}"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        val userList = ArrayList<ModelLoginRecord>()
        if(cursor.moveToFirst()){
            do {
                modelLoginRecord = ModelLoginRecord(
                    ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constants.USER_ID)),
                    "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.USER_IMAGE)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.FULL_NAME)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.PASSWORD)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.USER_DEPARTMENT)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.USER_ADDED_TIMESTAMP)),
                    ""+ cursor.getString(cursor.getColumnIndexOrThrow(Constants.USER_UPDATED_TIMESTAMP))
                )
                userList.add(modelLoginRecord)
            }while (cursor.moveToNext())

        }
        cursor.close()
        db.close()
        return userList

    }


    //insert records into table
    fun insertAppUser(
        image: String,
        fullName: String,
        password: String,
        department: String,
        addedTimeStamp: String,
        updatedTimeStamp: String
    ):Long{
        //get writable database to write data
        val db = writableDatabase
        val values = ContentValues()
        //id will be inserted automatically
        //insert data
        values.put(Constants.USER_IMAGE, image)
        values.put(Constants.FULL_NAME, fullName)
        values.put(Constants.PASSWORD, password)
        values.put(Constants.USER_DEPARTMENT, department)
        values.put(Constants.USER_ADDED_TIMESTAMP, addedTimeStamp)
        values.put(Constants.USER_UPDATED_TIMESTAMP, updatedTimeStamp)

        //insert row, it will return record id of saved record
        val id = db.insert(Constants.LOGIN_TABLE_NAME, null, values)
        //close db connection
        db.close()
        //return id of inserted record
        return id

    }

    //update records into table
    fun updateAppUser(
        userId: String,
        image: String,
        fullName: String,
        password: String,
        department: String,
        addedTimeStamp: String,
        updatedTimeStamp: String
    ):Long{
        //get writable database to write data
        val db = writableDatabase
        val values = ContentValues()
        values.put(Constants.USER_IMAGE, image)
        values.put(Constants.FULL_NAME, fullName)
        values.put(Constants.PASSWORD, password)
        values.put(Constants.USER_DEPARTMENT, department)
        values.put(Constants.USER_ADDED_TIMESTAMP, addedTimeStamp)
        values.put(Constants.USER_UPDATED_TIMESTAMP, updatedTimeStamp)

        //insert row, it will return record id of saved record
        val id = db.update(Constants.LOGIN_TABLE_NAME, values,
            "${Constants.USER_ID} = ?", arrayOf(userId)).toLong()

        //close db connection
        db.close()
        //return id of inserted record
        return id

    }

    //delete (single) record using id
    fun deleteUserRecord(userId:String){
        val db = writableDatabase
        db.delete(
            Constants.LOGIN_TABLE_NAME,
            "${Constants.USER_ID} = ?",
            arrayOf(userId)
        )

        db.close()
    }



}
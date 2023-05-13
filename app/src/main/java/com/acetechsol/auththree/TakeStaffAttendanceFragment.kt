package com.acetechsol.auththree

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.acetechsol.auththree.database.DbHelper
import com.acetechsol.auththree.models.ModelAttendanceRecord
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class TakeStaffAttendanceFragment : Fragment() {
    private lateinit var dbHelper: DbHelper
    private var staffID: String? = null
    private lateinit var  modelAttendanceRecord: ModelAttendanceRecord
    private var attendanceId: String = ""

    private lateinit var imageCIV: CircleImageView

    private lateinit var staffBioCV: CardView
    private lateinit var staffNameTV: TextView
    private lateinit var departmentTV: TextView
    private lateinit var workShiftTV: TextView

    private lateinit var checkInDetailsCV: CardView
    private lateinit var dateInTV: TextView
    private lateinit var timeInTV: TextView
    private lateinit var cashInTIET: TextInputEditText
    private lateinit var phoneDescTIET: TextInputEditText

    private lateinit var checkOutDetailsCV: CardView
    private lateinit var dateOutTV: TextView
    private lateinit var timeOutTV: TextView
    private lateinit var cashOutTIET: TextInputEditText

    private lateinit var cancelACBtn: AppCompatButton
    private lateinit var submitACBtn: AppCompatButton


    private lateinit var checkInOutLL: LinearLayout
    private lateinit var absentPermissionLL: LinearLayout
    private lateinit var absentReasonET: EditText
    private lateinit var switchViewBtn: Button
    private lateinit var permissionDateTV: TextView
    private var viewType = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(false)
        super.onCreate(savedInstanceState)
        dbHelper = DbHelper(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_take_staff_attendance, container, false)

        staffID = arguments?.getString("Staff_ID")

        switchViewBtn = view.findViewById(R.id.switch_view_Btn)

        imageCIV = view.findViewById(R.id.staff_image_CIV)

        staffBioCV = view.findViewById(R.id.staff_bio_CV)
        staffNameTV= view.findViewById(R.id.staff_name_TV)
        departmentTV= view.findViewById(R.id.staff_department_TV)
        workShiftTV = view.findViewById(R.id.work_shift_TV)

        checkInOutLL = view.findViewById(R.id.check_in_out_LL)
        checkInOutLL.isVisible = true
        absentPermissionLL = view.findViewById(R.id.permission_request_LL)
        absentPermissionLL.isVisible = false
        absentReasonET = view.findViewById(R.id.permission_request_ET)
        permissionDateTV = view.findViewById(R.id.permission_Date_TV)



        checkInDetailsCV= view.findViewById(R.id.check_in_CV)
        dateInTV= view.findViewById(R.id.date_in_TV)
        timeInTV= view.findViewById(R.id.time_in_TV)
        cashInTIET= view.findViewById(R.id.cash_in_ET)
        phoneDescTIET = view.findViewById(R.id.phone_description_ET)




        checkOutDetailsCV= view.findViewById(R.id.check_out_CV)
        checkOutDetailsCV.isVisible = false

        cancelACBtn = view.findViewById(R.id.cancel_Btn)
        submitACBtn = view.findViewById(R.id.submit_Btn)


        val modelStaffRecord = dbHelper.getStaffDetails(staffID.toString())
        val imageUri = Uri.parse(modelStaffRecord.imageUri)



        imageCIV = view.findViewById(R.id.staff_image_CIV)
        if(imageUri.toString() == "null"){
            imageCIV.setImageResource(R.drawable.ic_person)
        }
        else{
            imageCIV.setImageURI(imageUri)
        }

        val fullName = "${ modelStaffRecord.surname } ${modelStaffRecord.otherName}"
        staffNameTV.text = fullName
        departmentTV.text = modelStaffRecord.department

        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = System.currentTimeMillis()

        val timeOfDay  = calendar.get(Calendar.HOUR_OF_DAY)
        val session = if(timeOfDay in 0..15) "MORNING" else "EVENING"



        val currentDate = ""+ android.text.format.DateFormat.format("dd-MM-yyyy", calendar)
        val currentTime = ""+ android.text.format.DateFormat.format("hh:mm aa", calendar)

        //get last attendanceId of staff
        //val lastAttendanceId = dbHelper.getStaffLastAttendanceId(staffID.toString())
        if(dbHelper.countTotalStaffAttendance(staffID.toString()) > 0) {
            modelAttendanceRecord = dbHelper.getStaffLastAttendanceRecords(staffID.toString())
        }

        //check if staff has requested for permission to be absent
        if (dbHelper.countTotalStaffAttendance(staffID.toString()) > 0 &&
            modelAttendanceRecord.status == "ABSENT" &&
                modelAttendanceRecord.session == session
            ){

            (activity as AppCompatActivity).supportActionBar?.title = "Absent"
            absentPermissionLL.isVisible = true
            checkInOutLL.isVisible = false
            switchViewBtn.isVisible = false

            //modelAttendanceRecord = dbHelper.getStaffLastAttendanceRecords(lastAttendanceId.toString())
            permissionDateTV.text = modelAttendanceRecord.dateIn
            absentReasonET.setText(modelAttendanceRecord.permission)
            workShiftTV.text = modelAttendanceRecord.session
            absentReasonET.isFocusable = false
            submitACBtn.isVisible = false

            //set views
        } else if (dbHelper.countTotalStaffAttendance(staffID.toString()) > 0 &&
            modelAttendanceRecord.status == "PRESENT" && modelAttendanceRecord.dateOut == ""){
            //check out
            (activity as AppCompatActivity).supportActionBar?.title = "Check out"

            switchViewBtn.isVisible = false

            //get records of last attendance
            //modelAttendanceRecord = dbHelper.getStaffLastAttendanceRecords(lastAttendanceId.toString())

            //set data to variables and views
            attendanceId = modelAttendanceRecord.attendanceId
            workShiftTV.text = modelAttendanceRecord.session
            dateInTV.text = modelAttendanceRecord.dateIn
            timeInTV.text = modelAttendanceRecord.timeIn
            cashInTIET.setText(modelAttendanceRecord.cashIn)
            cashInTIET.isFocusable = false //deactivate editing
            phoneDescTIET.setText(modelAttendanceRecord.phoneDesc)
            phoneDescTIET.isFocusable = false

            //enable checkout views and set current data
            checkOutDetailsCV.isVisible = true
            dateOutTV= view.findViewById(R.id.date_out_TV)
            timeOutTV= view.findViewById(R.id.time_out_TV)
            cashOutTIET = view.findViewById(R.id.cash_out_ET)

            dateOutTV.text = currentDate
            timeOutTV.text = currentTime

            submitACBtn.setOnClickListener {
                if(validateCheckOutForm()) {
                    checkOut(currentDate, currentTime)
                    val navRegister = activity as FragmentNavigation
                    navRegister.navigateFrag(ManageStaffAttendanceFragment(), false)
                }
            }
        }
        //check if staff has not checked in in current date and session and view type is 1 - check in in
        else if(!dbHelper.isStaffCheckedIn(staffID.toString().trim(), currentDate.trim(), session)){

            //check in
            (activity as AppCompatActivity).supportActionBar?.title = "Check In"

            switchViewBtn.isVisible = true

            loadCheckInView(session, currentDate, currentTime)

            submitACBtn.setOnClickListener {
                if(validateCheckInForm()) {
                    checkIn(currentDate, currentTime, session)
                    (context as MainActivity).onBackPressed()
                }

            }

            switchViewBtn.setOnClickListener {
                if(viewType == 0){
                    loadCheckInView(session, currentDate, currentTime)
                }else{
                    loadPermissionRequestView(session, currentDate)
                }
            }

        }
        //display check in / check out data
        else{

            (activity as AppCompatActivity).supportActionBar?.title = "Checked Out"
            modelAttendanceRecord = dbHelper.getAttendanceRecords(staffID.toString(), currentDate, session)


            workShiftTV.text = modelAttendanceRecord.session
            dateInTV.text = modelAttendanceRecord.dateIn
            timeInTV.text = modelAttendanceRecord.timeIn

            cashInTIET.setText(modelAttendanceRecord.cashIn)
            cashInTIET.isFocusable = false

            phoneDescTIET.setText(modelAttendanceRecord.phoneDesc)
            phoneDescTIET.isFocusable = false

            checkOutDetailsCV.isVisible = true
            dateOutTV= view.findViewById(R.id.date_out_TV)
            timeOutTV= view.findViewById(R.id.time_out_TV)
            cashOutTIET = view.findViewById(R.id.cash_out_ET)

            dateOutTV.text = modelAttendanceRecord.dateOut
            timeOutTV.text = modelAttendanceRecord.timeOut
            cashOutTIET.setText(modelAttendanceRecord.cashOut)
            cashOutTIET.isFocusable = false

            submitACBtn.isVisible = false
            switchViewBtn.isVisible = false

        }



        cancelACBtn.setOnClickListener {
            //var navRegister = activity as FragmentNavigation
            //navRegister.navigateFrag(ManageStaffAttendanceFragment(), false)

            (context as MainActivity).onBackPressed()
        }

        return view
    }

    private fun loadPermissionRequestView(
        session: String,
        currentDate: String
    ) {

        (activity as AppCompatActivity).supportActionBar?.title = "Permission Request"
        switchViewBtn.text = getString(R.string.check_in_btn)
        viewType = 0
        checkInOutLL.isVisible = false
        absentPermissionLL.isVisible = true
        workShiftTV.text = session
        permissionDateTV.text = currentDate

    }

    private fun loadCheckInView(session: String, currentDate: String, currentTime: String) {

        (activity as AppCompatActivity).supportActionBar?.title = "Check in"
        switchViewBtn.text = getString(R.string.permission_btn)
        viewType = 1
        absentPermissionLL.isVisible = false
        checkInOutLL.isVisible = true
        workShiftTV.text = session
        dateInTV.text = currentDate
        timeInTV.text = currentTime


    }

    private fun checkOut(date: String, time: String) {
        val idd = dbHelper.updateAttendanceRecord(
            ""+ attendanceId ,
            ""+ staffID,
            "" + dateInTV.text.toString().trim(),
            "" + date,
            "" + timeInTV.text.toString().trim(),
            "" + time,
            "" + cashInTIET.text.toString().trim(),
            "" + cashOutTIET.text.toString().trim(),
            "" + workShiftTV.text.toString().trim(),
            "PRESENT"
        )
        Toast.makeText(context, "record updated...", Toast.LENGTH_LONG).show()
    }

    private fun checkIn(date: String, time: String, session: String) {
        if(viewType == 1) {
            val idd = dbHelper.insertAttendanceRecord(
                Integer.parseInt(staffID.toString()),
                "" + date,
                "",
                "" + time,
                "",
                cashInTIET.text.toString().trim(),
                "",
                "" + phoneDescTIET.text.toString().trim(),
                ""+ session,
                "PRESENT",
                ""
            )
            Toast.makeText(context, "Attendance record saved...", Toast.LENGTH_LONG).show()
        }else{
            val idd = dbHelper.insertAttendanceRecord(
                Integer.parseInt(staffID.toString()),
                "" + date,
                "",
                "",
                "",
                cashInTIET.text.toString().trim(),
                "",
                "",
                "" + session,
                "ABSENT",
                ""+ absentReasonET.text.toString().trim()

            )
            Toast.makeText(context, "permission record saved...", Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    private fun validateCheckInForm(): Boolean {
        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        if(viewType == 1) {
            when {
                TextUtils.isEmpty(cashInTIET.text.toString().trim()) -> {
                    cashInTIET.setError("Please Enter Username", icon)
                    return false
                }
                TextUtils.isEmpty(phoneDescTIET.text.toString().trim()) -> {
                    phoneDescTIET.setError("Please Enter Username", icon)
                    return false
                }

            }
        }else{
            when{
                TextUtils.isEmpty(absentReasonET.text.toString().trim())->{
                    absentReasonET.setError("Please Enter Username", icon)
                    return false
                }

            }
        }
        return true


    }

    private fun validateCheckOutForm(): Boolean {
        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when{

            TextUtils.isEmpty(cashOutTIET.text.toString().trim())->{
                cashOutTIET.setError("Please Enter Username", icon)
                return false
            }

        }
        return true


    }

    /*
    private fun validatePermissionForm(): Boolean {
        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when{
            TextUtils.isEmpty(absentReasonET.text.toString().trim())->{
                absentReasonET.setError("Please Enter Username", icon)
            }

        }
        return true

    }

     */


}
package com.acetechsol.auththree

import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.acetechsol.auththree.database.DbHelper
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class StaffDetailsFragment : DialogFragment() {

    private var dbHelper: DbHelper? = null
    private var staffID: String? = null

    private lateinit var imageCIV: CircleImageView
    private lateinit var fullNameTV: TextView
    private lateinit var departmentTV: TextView
    private lateinit var phoneNumberTV: TextView
    private lateinit var dateAddedTV: TextView
    private lateinit var dateUpdatedTV: TextView

    private lateinit var dismissBtn: AppCompatButton
    private lateinit var editBtn: AppCompatButton
    private lateinit var deleteBtn: AppCompatButton

    private var id: String = ""
    private var image: String = ""
    private var surname: String = ""
    private var otherName: String = ""
    private var phoneNumber: String = ""
    private var department: String = ""
    private var addTimestamp: String = ""
    private var updatedTimestamp: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
        dbHelper = DbHelper(context)
        staffID = arguments?.getString("RECORD_ID")
        //Toast.makeText(context, staffID.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_staff_details, container, false)

        imageCIV = view.findViewById(R.id.staff_detail_image_CIV)
        fullNameTV = view.findViewById(R.id.staff_name_TV)
        departmentTV = view.findViewById(R.id.staff_department_TV)
        phoneNumberTV = view.findViewById(R.id.staff_phone_number_TV)
        dateAddedTV = view.findViewById(R.id.staff_time_added_TV)
        dateUpdatedTV = view.findViewById(R.id.staff_time_updated_TV)
        dismissBtn = view.findViewById(R.id.cancel_action)
        editBtn = view.findViewById(R.id.submit_action)
        deleteBtn = view.findViewById(R.id.delete_action)

        getStaffDetails()

        dismissBtn.setOnClickListener {
            dismiss()
        }

        editBtn.setOnClickListener {
            val navRegister = activity as FragmentNavigation
            val bundle = Bundle()
            bundle.putString("Staff_ID", id)
            bundle.putInt("IS_EDIT_MODE", 1)

            val fragment = AddUpdateStaffRecordFragment()
            fragment.arguments = bundle
            navRegister.navigateFrag(fragment, true)
            dismiss()
        }

        deleteBtn.setOnClickListener {
            dbHelper?.deleteStaffRecord(id)
            //refresh record by calling activity's onResume method
            (context as MainActivity).goToManageStaff()
            dismiss()
        }

        return view
    }

   private fun getStaffDetails(){
       val modelStaffRecord = dbHelper?.getStaffDetails(staffID.toString())
       if (modelStaffRecord != null) {
           id = modelStaffRecord.id
           image = modelStaffRecord.imageUri
           surname = modelStaffRecord.surname
           otherName = modelStaffRecord.otherName
           phoneNumber = modelStaffRecord.phoneNumber
           department = modelStaffRecord.department
           addTimestamp = modelStaffRecord.addedTimestamp
           updatedTimestamp = modelStaffRecord.updatedTimestamp
           //convert timestamp
           val calendar1 = Calendar.getInstance(Locale.getDefault())
           calendar1.timeInMillis = addTimestamp.toLong()
           val timeAdded = ""+ android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar1)


           val calendar2 = Calendar.getInstance(Locale.getDefault())
           calendar2.timeInMillis = updatedTimestamp.toLong()
           val timeUpdated = ""+ android.text.format.DateFormat.format("dd/MM/yyyy hh:mm aa", calendar2)

           //set data
           val staffName = "$surname $otherName"
           fullNameTV.text =  staffName
           departmentTV.text = department
           phoneNumberTV.text = phoneNumber
           dateAddedTV.text = timeAdded
           dateUpdatedTV.text = timeUpdated
           //if user doesn't attach image, then imageUri will be null, se we set default image in that case
           if(image == "null"){
               //not image in record, set default
               imageCIV.setImageResource(R.drawable.ic_person)
           }else{
               //has image in record
               imageCIV.setImageURI(Uri.parse(image))
           }

       }else{
           Toast.makeText(context, "No record found...", Toast.LENGTH_SHORT).show()
       }

       }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
    }



}
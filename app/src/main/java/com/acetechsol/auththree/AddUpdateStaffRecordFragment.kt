package com.acetechsol.auththree

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.acetechsol.auththree.database.DbHelper
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File


class AddUpdateStaffRecordFragment : Fragment(), AdapterView.OnItemSelectedListener {

    //permission constants


    private lateinit var staffImageCIV: CircleImageView
    private lateinit var staffSurnameET: EditText
    private lateinit var staffOtherNamesET: EditText
    private lateinit var staffDepartmentSp: Spinner
    private lateinit var staffPhoneNumberET: EditText
    private lateinit var addStaffRecordFAB: FloatingActionButton

    //Variables that will contain data to store in the database
    private var imageUri: Uri? = null
    private var surname: String? = ""
    private var otherNames: String? = ""
    private var department: String? = ""
    private var phoneNumber: String? = ""
    private var addedTimestamp: String? = ""
    private var updatedTimestamp: String? = ""



    private lateinit var dbHelper: DbHelper
    private var staffID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //activity or fragment
        //initialize db helper class
        dbHelper = DbHelper(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_update_staff_record, container, false)

        initialiseViews(view)


        val adapter = context?.let {
            ArrayAdapter.createFromResource(
                it,
            R.array.Departments,
            android.R.layout.simple_spinner_item)
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        staffDepartmentSp.adapter = adapter
        staffDepartmentSp.onItemSelectedListener = this

        val isEditMode = arguments?.getInt("IS_EDIT_MODE")
        //Toast.makeText(context, isEditMode.toString(), Toast.LENGTH_SHORT).show()

        if(isEditMode == 1){
            (activity as AppCompatActivity).supportActionBar?.title = "Edit Staff Record"
            //get data from adapter

            staffID = arguments?.getString("Staff_ID")

            val modelStaffRecord = dbHelper.getStaffDetails(staffID.toString())
            imageUri = Uri.parse(modelStaffRecord.imageUri)
            surname = modelStaffRecord.surname
            otherNames = modelStaffRecord.otherName
            phoneNumber = modelStaffRecord.phoneNumber
            department = modelStaffRecord.department
            addedTimestamp = modelStaffRecord.addedTimestamp
            updatedTimestamp = modelStaffRecord.updatedTimestamp


            if(imageUri.toString() == "null"){
                staffImageCIV.setImageResource(R.drawable.ic_person)
            }else{
                staffImageCIV.setImageURI(imageUri)
            }
            staffSurnameET.setText(surname)
            staffOtherNamesET.setText(otherNames)
            staffPhoneNumberET.setText(phoneNumber)
            //staffDepartmentET.setText(department)
            val spinnerPosition = adapter!!.getPosition(department)
            if(spinnerPosition>0) {
                staffDepartmentSp.setSelection(spinnerPosition)
            }


        }else{
            (activity as AppCompatActivity).supportActionBar?.title = "Add New Record"

        }

        //click imageview to pick image
        staffImageCIV.setOnClickListener {
            showPictureDialog()
        }

        //click addStaffRecordFAB to save record
        addStaffRecordFAB.setOnClickListener{
            if(validateRegisterForm()) {
                inputData(isEditMode)
                (context as MainActivity).onBackPressed()
            }
        }

        return view
    }

    private fun inputData(isEditMode:Int?) {
        surname = ""+staffSurnameET.text.toString().trim()
        otherNames = ""+staffOtherNamesET.text.toString().trim()
        //department = ""+staffDepartmentET.text.toString().trim()
        phoneNumber = ""+staffPhoneNumberET.text.toString().trim()

        if(isEditMode == 1){
            //editing
            val timestamp = "${System.currentTimeMillis()}"
            dbHelper.updateStaffDetails(
                ""+ staffID,
                ""+ imageUri,
                ""+ surname,
                ""+ otherNames,
                ""+ phoneNumber,
                ""+ department,
                ""+ addedTimestamp,
                ""+ timestamp
            )
            Toast.makeText(context, "Record Updated...", Toast.LENGTH_SHORT).show()
        }else{
            //save data to db
            val timestamp = System.currentTimeMillis()
            val id = dbHelper.insertRecord(
                ""+imageUri,
                ""+surname,
                ""+otherNames,
                ""+phoneNumber,
                ""+department,
                ""+ timestamp,
                "" + timestamp
            )

            if(id > 0) {
                Toast.makeText(context, "Record Added...", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "Record not Added", Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(context)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { _, which ->
            when (which) {
                0 -> pickFromGallery()
                1 -> pickFromCamera()
            }
        }
        pictureDialog.show()
    }

    private fun pickFromCamera() {
        ImagePicker.with(this)
            .cameraOnly()
            .crop()
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
            .saveDir(File(
                context?.filesDir,
                "ImagePicker"
            ))
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }

    }

    private fun pickFromGallery() {
        ImagePicker.with(this)
            .galleryOnly()
            .crop()
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
            .saveDir(File(
                context?.filesDir,
                "ImagePicker"
            ))
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }

    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    imageUri = data?.data!!

                    //mProfileUri = fileUri
                    staffImageCIV.setImageURI(imageUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }



    private fun initialiseViews(view: View){
        staffImageCIV = view.findViewById(R.id.staff_profile_picture_CIV)
        staffSurnameET = view.findViewById(R.id.staff_Surname_name_ET)
        staffOtherNamesET = view.findViewById(R.id.staff_other_name_ET)
        staffDepartmentSp = view.findViewById(R.id.department_spinner)
        staffPhoneNumberET = view.findViewById(R.id.staff_phone_number_ET)
        addStaffRecordFAB = view.findViewById(R.id.add_staff_record_FAB)

    }


    private fun validateRegisterForm(): Boolean {
        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when{
            TextUtils.isEmpty(staffSurnameET.text.toString().trim())->{
                staffSurnameET.setError("Please Enter Username", icon)
                return false
            }
            TextUtils.isEmpty(staffOtherNamesET.text.toString().trim())->{
                staffOtherNamesET.setError("Please Enter Username", icon)
                return false
            }
            department?.trim() == "Choose Department" ->{
                Toast.makeText(context, "Please select Department", Toast.LENGTH_SHORT).show()
                return false
            }

        }
        return true


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        department = parent?.getItemAtPosition(position).toString()

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        department = "Choose Department"
    }


}

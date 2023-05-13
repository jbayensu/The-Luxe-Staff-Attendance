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
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import com.acetechsol.auththree.database.DbHelper
import com.github.dhaval2404.imagepicker.ImagePicker
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File


class RegisterFragment : Fragment() {
    private lateinit var userProfileImageCIV: CircleImageView
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var cnfPassword: EditText
    private lateinit var department: EditText
    private lateinit var registerBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var dbHelper:DbHelper
    private var imageUri: Uri? = null
    private var addedTimeStamp: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)

        dbHelper = DbHelper(context)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val userId = arguments?.getString("USER_ID")
        init(view)


        if(userId.toString() != ""){
            val modelLoginRecord = dbHelper.getAppUserDetails(userId.toString())
            addedTimeStamp = modelLoginRecord.addedTimestamp
            if(modelLoginRecord.imageUri == "null"){
                userProfileImageCIV.setImageResource(R.drawable.luxe_logo)
            }else{
                userProfileImageCIV.setImageURI(Uri.parse(modelLoginRecord.imageUri))
            }
            username.setText(modelLoginRecord.fullName)
            department.setText(modelLoginRecord.department)
            deleteBtn.isVisible = true

            deleteBtn.setOnClickListener {
                deleteUser(userId)
            }
        }


        //click imageview to pick image
        userProfileImageCIV.setOnClickListener {
            showPictureDialog()
        }

        registerBtn.setOnClickListener {
            if(validateEmptyForm()){
                inputData(userId)
            }
        }
        return view
    }

    private fun deleteUser(userId: String?) {
        dbHelper.deleteUserRecord(userId.toString())
    }

    private fun init(view: View){
        userProfileImageCIV = view.findViewById(R.id.user_image_CIV)
        username = view.findViewById(R.id.reg_username_edittext)
        password = view.findViewById(R.id.reg_password_edittext)
        cnfPassword = view.findViewById(R.id.reg_confirm_password_edittext)
        department = view.findViewById(R.id.department_ET)
        registerBtn = view.findViewById(R.id.reg_register_btn)
        deleteBtn = view.findViewById(R.id.reg_delete_btn)
        deleteBtn.isVisible = false
    }

    private fun inputData(userId: String?) {

        if(userId.toString() == "") {//save data to db
            val timestamp = System.currentTimeMillis()
            val id = dbHelper.insertAppUser(
                "" + imageUri,
                "" + username.text.toString(),
                "" + password.text.toString(),
                "" + department.text.toString(),
                "" + timestamp,
                "" + timestamp
            )

            Toast.makeText(context, "record saved against id $id", Toast.LENGTH_LONG).show()
            clearUtils()
        }else{
            val timestamp = System.currentTimeMillis()
            val idd = dbHelper.updateAppUser(
                "" + userId,
                "" + imageUri,
                "" + username.text.toString(),
                "" + password.text.toString(),
                "" + department.text.toString(),
                "" + addedTimeStamp,
                "" + timestamp

            )
            Toast.makeText(context, "record saved...", Toast.LENGTH_LONG).show()
            val navRegister = activity as FragmentNavigation
            navRegister.navigateFrag(ManageUsersFragment(), false)

        }
    }

    private fun validateEmptyForm():Boolean{
        val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        icon?.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
        when{
            TextUtils.isEmpty(username.text.toString().trim())->{
                username.setError("Please Enter Username", icon)
            }
            TextUtils.isEmpty(password.text.toString().trim())->{
                password.setError("Please Enter Password", icon)
            }
            TextUtils.isEmpty(cnfPassword.text.toString().trim())->{
                cnfPassword.setError("Please confirm password", icon)
            }

            username.text.toString().isNotEmpty() &&
                    password.toString().isNotEmpty() &&
                    cnfPassword.toString().isNotEmpty() ->{
                       // if(username.text.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))){
                            if(password.text.length >= 8){
                                if(password.text.toString() == cnfPassword.text.toString()){
                                    Toast.makeText(context, "Registration Successful", Toast.LENGTH_SHORT).show()
                                    return true
                                }else{
                                    cnfPassword.setError("Password does not match", icon)
                                }
                            }else{
                                password.setError("Please the length of the password should be 8 and above", icon)
                            }
                       // }else{
                           // username.setError("Please your username does not match the format of an email", icon)
                       // }
                    }
        }
        return false
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
            .saveDir(
                File(
                context?.filesDir,
                "ImagePicker"
            )
            )
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
            .saveDir(
                File(
                context?.filesDir,
                "ImagePicker"
            )
            )
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }

    }

    private val startForProfileImageResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    imageUri = data?.data!!

                    //mProfileUri = fileUri
                    userProfileImageCIV.setImageURI(imageUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    private fun clearUtils(){
        if(imageUri.toString() != "null"){
            userProfileImageCIV.setImageResource(R.drawable.ic_person)
        }
        username.setText("")
        password.setText("")
        cnfPassword.setText("")
        department.setText("")
    }


}
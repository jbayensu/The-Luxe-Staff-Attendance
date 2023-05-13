package com.acetechsol.auththree

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.acetechsol.auththree.database.DbHelper
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var dbHelper: DbHelper

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
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        init(view)

        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = System.currentTimeMillis()
        val currentDate = ""+ android.text.format.DateFormat.format("dd-MM-yyyy", calendar)



        loginBtn.setOnClickListener {
            if(validateEmptyForm()){

                    val userId = dbHelper.getUserIDifExist(
                        username.text.trim().toString(),
                        password.text.trim().toString()
                    )
                    if (password.text.trim().toString() == "12345678"
                        && username.text.trim().toString() == "bob"
                    ) {
                        resetText()
                        Toast.makeText(context, "login success", Toast.LENGTH_SHORT).show()
                        val navRegister = activity as FragmentNavigation
                        navRegister.navigateFrag(ManageUsersFragment(), true)

                    } else if (userId != "") {
                        val intent = Intent(context, MainActivity::class.java)
                        resetText()
                        Toast.makeText(context, "Login success...", Toast.LENGTH_SHORT).show()
                        intent.putExtra("USER_ID", userId)
                        startActivity(intent)
                    } else {
                        Toast.makeText(context, "Wrong username or password", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
        }

        return view
    }



    private fun init(view: View){
        username = view.findViewById(R.id.log_username_edittext)
        password = view.findViewById(R.id.log_password_edittext)
        loginBtn = view.findViewById(R.id.log_login_btn)
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

            username.text.toString().isNotEmpty() &&
                    password.toString().isNotEmpty() ->{
               // if(username.text.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))){
                    if(password.text.length >= 8 ){
                        //Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        return true
                    }else{
                        password.setError("Please the length of the password should be 8 and above", icon)
                    }
            }
        }
        return false
    }

    private fun resetText(){

        username.setText("")
        password.setText("")
    }


}
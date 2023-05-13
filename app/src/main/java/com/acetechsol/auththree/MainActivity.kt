package com.acetechsol.auththree

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.acetechsol.auththree.database.DbHelper
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity(), FragmentNavigation {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var userProfileImageCIV: CircleImageView
    private lateinit var userNameTV: TextView
    private lateinit var userDepartmentTV: TextView
    private lateinit var dbHelper: DbHelper


    var fragment: Fragment? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbHelper = DbHelper(this)

        val userId = intent.getStringExtra("USER_ID")


        supportActionBar?.title = "Dashboard"

        val fm: FragmentManager = supportFragmentManager
        var fragment: Fragment? = fm.findFragmentById(R.id.main_container)

        if (fragment == null) {
            fragment = DashboardFragment()
            /*bundle = Bundle()
            bundle.putString("userID", MainActivity.userID)
            bundle.putString("email", MainActivity.userID)
            bundle.putString("full name", MainActivity.userID)
            fragment.arguments = bundle*/
            fm.beginTransaction()
                .add(R.id.main_container, fragment)
                .commit()
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val headerView = navView.getHeaderView(0)



        userProfileImageCIV = headerView.findViewById(R.id.user_profile_CIV)
        userNameTV = headerView.findViewById(R.id.user_name_TV)
        userDepartmentTV = headerView.findViewById(R.id.department_TV)

        val modelLoginRecord = dbHelper.getAppUserDetails(userId.toString())

        if(modelLoginRecord.imageUri == "null"){
            userProfileImageCIV.setImageResource(R.drawable.luxe_logo)
        }else{
            userProfileImageCIV.setImageURI(Uri.parse(modelLoginRecord.imageUri))
        }

        userNameTV.text = modelLoginRecord.fullName
        userDepartmentTV.text = modelLoginRecord.department

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            val navRegister = this as FragmentNavigation
            when(it.itemId){

                R.id.nav_dashboard -> {
                    fragment = DashboardFragment()
                    supportActionBar?.setTitle("Dashboard")
                }
                R.id.nav_manage_staff -> {
                    fragment = ManageStaffRecordsFragment()
                    supportActionBar?.setTitle("Manage Staff")
                }
                R.id.nav_attendance -> {
                    fragment = ManageStaffAttendanceFragment()
                    supportActionBar?.title = "Staff Attendance"
                }R.id.nav_attendance_report-> {

                fragment = AttendanceReportQueryFragment()
                supportActionBar?.title = "Report Query"

                }
                /*R.id.nav_setting -> {
                    fragment = SettingFragment()
                    supportActionBar?.setTitle("Settings")
                }

                 */
                R.id.nav_log_out -> finish()

            }
            if (fragment != null) {
                //val fm = supportFragmentManager

                //fm.beginTransaction().replace(R.id.main_container, fragment!!).commit()
                    var addToStack = true
                when (fragment) {
                    TakeStaffAttendanceFragment() -> {
                        addToStack = false
                    }
                    AddUpdateStaffRecordFragment() -> {
                        addToStack = false
                    }
                    ManageStaffAttendanceFragment() -> {
                        fragment!!.onDestroyOptionsMenu()
                    }
                    TakeStaffAttendanceFragment() -> {
                        fragment!!.onDestroyOptionsMenu()
                    }
                }
                navRegister.navigateFrag(fragment!!, addToStack)
            }

            drawerLayout.closeDrawer(GravityCompat.START)

            true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }



    override fun navigateFrag(fragment: Fragment, addToStack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, fragment)
        if(addToStack){
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    fun goToManageStaff(){

        val navRegister = this as FragmentNavigation
        navRegister.navigateFrag(ManageStaffRecordsFragment(), false)
        //val fm = supportFragmentManager
        //fm.beginTransaction().replace(R.id.main_container, ManageStaffRecordsFragment()).commit()
    }

    private fun goToManageStaffAttendance(){

        val navRegister = this as FragmentNavigation
        navRegister.navigateFrag(ManageStaffAttendanceFragment(), false)
    }

    private fun goToManageStaffRecord(){
        val navRegister = this as FragmentNavigation
        navRegister.navigateFrag(ManageStaffAttendanceFragment(), false)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        if (fragment == TakeStaffAttendanceFragment()){
            goToManageStaffAttendance()
        }else if (fragment == AddUpdateStaffRecordFragment()){
            goToManageStaffRecord()
        }

        if(fragment == ManageStaffAttendanceFragment()){
            fragment!!.onDestroyOptionsMenu()
        }else if(fragment == TakeStaffAttendanceFragment()){
            fragment!!.onDestroyOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.clear()
        return super.onCreateOptionsMenu(menu)
    }





}
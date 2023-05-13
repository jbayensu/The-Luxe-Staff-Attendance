package com.acetechsol.auththree.adapters

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acetechsol.auththree.FragmentNavigation
import com.acetechsol.auththree.R
import com.acetechsol.auththree.RegisterFragment
import com.acetechsol.auththree.models.ModelLoginRecord

class UserRecordAdapter(): RecyclerView.Adapter<UserRecordAdapter.HolderRecord>() {
    private var context: Context? = null
    private var userRecordList: ArrayList<ModelLoginRecord>?=null


    constructor(context: Context?, userRecordList: ArrayList<ModelLoginRecord>?) : this() {
        this.context = context
        this.userRecordList = userRecordList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRecord {
        //inflate the layout staff_record.xml
        return HolderRecord(
            LayoutInflater.from(context).inflate(R.layout.user_record, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HolderRecord, position: Int) {
        //get data, set data, handle clicks

        //get data
        val model = userRecordList!![position]
        val userId = model.userId
        val image = model.imageUri
        val fullName = model.fullName
        val department = model.department
        //set data to views

        holder.fullName.text = fullName
        holder.department.text = department

        //if user doesn't attach image, then imageUri will be null, se we set default image in that case
        if(image == "null"){
            //no image in record, set default
            holder.profileImage.setImageResource(R.drawable.ic_person)
        }else{
            //has image in record
            holder.profileImage.setImageURI(Uri.parse(image))
        }

        //show record in new activity on clicking record
        holder.itemView.setOnClickListener{
            //pass id to next activity to show record
            val navRegister = context as FragmentNavigation
            val bundle = Bundle()
            bundle.putString("USER_ID", userId)
            val fragment = RegisterFragment()
            fragment.arguments = bundle
            navRegister.navigateFrag(fragment, true)

        }

        //handle more button click: show delete/edit options

        /*
        holder.moreAction.setOnClickListener {
            //show more options e.g. edit, delete
            //options to display in dialog

        }

         */
    }



    override fun getItemCount(): Int {
        //return items/records/list size
        return userRecordList!!.size
    }


    inner class HolderRecord(itemView: View): RecyclerView.ViewHolder(itemView) {
        //views from staff_record.xml
        var profileImage: ImageView = itemView.findViewById(R.id.user_image_CIV)
        var fullName: TextView = itemView.findViewById(R.id.user_full_name_TV)
        var department: TextView = itemView.findViewById(R.id.user_department_TV)

    }



}
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
import com.acetechsol.auththree.MainActivity
import com.acetechsol.auththree.R
import com.acetechsol.auththree.StaffDetailsFragment
import com.acetechsol.auththree.models.ModelStaffRecord

/*adapter class for recyclerView*/
class StaffRecordAdapter(): RecyclerView.Adapter<StaffRecordAdapter.HolderRecord>() {

    private var context: Context? = null
    private var staffRecordList: ArrayList<ModelStaffRecord>?=null


    constructor(context: Context?, staffRecordList: ArrayList<ModelStaffRecord>?) : this() {
        this.context = context
        this.staffRecordList = staffRecordList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRecord {
        //inflate the layout staff_record.xml
        return HolderRecord(
            LayoutInflater.from(context).inflate(R.layout.staff_record, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HolderRecord, position: Int) {
        //get data, set data, handle clicks

        //get data
        val model = staffRecordList!![position]
        val id = model.id
        val image = model.imageUri
        val surname = model.surname
        val otherName = model.otherName
        val phoneNumber = model.phoneNumber
        val department = model.department
        //set data to views
        val staffFullName = "$surname $otherName"

        holder.fullName.text = staffFullName
        holder.phoneNumber.text = phoneNumber
        holder.department.text = department

        //if user doesn't attach image, then imageUri will be null, se we set default image in that case
        if(image == "null"){
            //not image in record, set default
            holder.profileImage.setImageResource(R.drawable.ic_person)
        }else{
            //has image in record
            holder.profileImage.setImageURI(Uri.parse(image))
        }

        //show record in new activity on clicking record
        holder.itemView.setOnClickListener{
            //pass id to next activity to show record
            val dialog = StaffDetailsFragment()
            val activity = holder.itemView.context as? MainActivity

            val bundle = Bundle()
            bundle.putString("RECORD_ID", id)
            dialog.arguments = bundle
            dialog.show(activity!!.supportFragmentManager, "Staff Detail")

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
        return staffRecordList!!.size
    }


    inner class HolderRecord(itemView: View): RecyclerView.ViewHolder(itemView) {
        //views from staff_record.xml
        var profileImage: ImageView = itemView.findViewById(R.id.staff_record_profile_picture_CIV)
        var fullName: TextView = itemView.findViewById(R.id.staff_record_full_name_ET)
        var phoneNumber: TextView = itemView.findViewById(R.id.staff_record_phone_number_ET)
        var department: TextView = itemView.findViewById(R.id.staff_record_department_ET)

    }



}

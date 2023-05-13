package com.acetechsol.auththree.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.acetechsol.auththree.R
import com.acetechsol.auththree.database.DbHelper
import com.acetechsol.auththree.models.ModelStaffAttendance


/*
    Adapter class for recyclerview
 */
class AttendanceReportPreviewAdapter():RecyclerView.Adapter<AttendanceReportPreviewAdapter.HolderRecord>() {

    private var context: Context? = null
    private var reportList: ArrayList<ModelStaffAttendance>? = null
    private lateinit var dbHelper: DbHelper

    constructor(context: Context?, reportList: ArrayList<ModelStaffAttendance>?) : this() {
        this.context = context
        this.reportList = reportList
        this.dbHelper = DbHelper(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRecord {
        return HolderRecord(
            LayoutInflater.from(context).inflate(R.layout.staff_report, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HolderRecord, position: Int) {
        //get data
        val model = reportList!![position]

        //val staffId = model.staffId
        val timeIn = model.timeIn
        val timeOut = model.timeOut
        val cashIn = model.cashIn
        val cashOut = model.cashOut
        val session = model.session
        val status = model.status
        val dateIn = context?.getString(R.string.date) + "  " + model.dateIn


        holder.dateTV.text = dateIn
        holder.sessionTV.text = session
        holder.statusTV.text = status

        if(status == "PRESENT") {
            holder.checkInOutLL.isVisible = true
            holder.permissionDetailLL.isVisible = false
            holder.timeInTV.text = timeIn
            holder.timeOutTV.text = timeOut
            holder.cashInTV.text = cashIn
            holder.cashOutTV.text = cashOut
            holder.phoneDesc.text = model.phoneDesc
        }else{
            holder.checkInOutLL.isVisible = false
            holder.permissionDetailLL.isVisible = true
            holder.reasonForAbsenceTV.text = model.permission

        }

        //val modelStaffRecord = dbHelper.getSomeStaffDetails(staffId)
        //set data
        //val fullName = "${modelStaffRecord.surname} ${modelStaffRecord.otherName}"
        holder.staffNameTV.text = model.staffName
        holder.departmentTV.text = model.department

    }

    override fun getItemCount(): Int {
        return reportList!!.size
    }


    inner class HolderRecord(itemView: View):RecyclerView.ViewHolder(itemView){
        //views from staff report

        var staffNameTV: TextView = itemView.findViewById(R.id.staff_name_TV)
        var departmentTV: TextView = itemView.findViewById(R.id.department_TV)
        var dateTV: TextView = itemView.findViewById(R.id.date_TV)
        var statusTV: TextView = itemView.findViewById(R.id.status_TV)
        var checkInOutLL: LinearLayout = itemView.findViewById(R.id.check_in_out_details_LL)
        var sessionTV: TextView = itemView.findViewById(R.id.session_TV)
        var timeInTV: TextView = itemView.findViewById(R.id.time_in_TV)
        var timeOutTV: TextView = itemView.findViewById(R.id.time_out_TV)
        var cashInTV: TextView = itemView.findViewById(R.id.cash_in_TV)
        var cashOutTV: TextView = itemView.findViewById(R.id.cash_out_TV)
        var phoneDesc: TextView = itemView.findViewById(R.id.phone_desc_TV)
        var permissionDetailLL: LinearLayout = itemView.findViewById(R.id.permission_detail_LL)
        var reasonForAbsenceTV: TextView = itemView.findViewById(R.id.reason_for_absence_TV)
    }


}
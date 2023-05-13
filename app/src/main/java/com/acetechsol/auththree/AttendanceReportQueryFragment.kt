package com.acetechsol.auththree

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*


class AttendanceReportQueryFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(false)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_attendance_report_query, container, false)

        var selectedFromDate = ""+ android.text.format.DateFormat.format("yyyy-MM-dd",
            Calendar.getInstance(
                Locale.getDefault()).timeInMillis)
        var selectedToDate = ""+ android.text.format.DateFormat.format("yyyy-MM-dd",
            Calendar.getInstance(
                Locale.getDefault()).timeInMillis)

        val searchByDatesBtn:Button = view.findViewById(R.id.search_by_all_Btn)
        val searchByNamesBtn: Button = view.findViewById(R.id.search_by_name_Btn)
        val fromDatePicker: DatePicker = view.findViewById(R.id.query_date_from_DP)
        val toDatePicker: DatePicker = view.findViewById(R.id.query_date_to_DP)

        val today = Calendar.getInstance()
        fromDatePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            val thisMonth = month + 1
            selectedFromDate = convertDate(year, thisMonth, day)
            //Toast.makeText(context, selectedFromDate, Toast.LENGTH_SHORT).show()

            //Toast.makeText(context, "$selectedFromDate, $selectedToDate", Toast.LENGTH_LONG).show();
        }

        toDatePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            val thisMonth = month + 1
            selectedToDate = convertDate(year, thisMonth, day)
            //Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

            //Toast.makeText(context, "$selectedFromDate \n$selectedToDate", Toast.LENGTH_SHORT).show()
        }

        searchByNamesBtn.setOnClickListener {
            // Toast.makeText(context, "You selected $selectedDate", Toast.LENGTH_SHORT).show()
            if(validQueryDate(selectedFromDate, selectedToDate)){
                val intent =  Intent(context, AttendanceReportPreviewActivity::class.java)
                intent.putExtra("FROM_QUERY_DATE", selectedFromDate)
                intent.putExtra("TO_QUERY_DATE", selectedToDate)
                intent.putExtra("ORDER_BY", "Surname")

                startActivity(intent)
            }else{
                Toast.makeText(context, "check your date range please", Toast.LENGTH_LONG).show()
            }

        }

        searchByDatesBtn.setOnClickListener {
            // Toast.makeText(context, "You selected $selectedDate", Toast.LENGTH_SHORT).show()
            if(validQueryDate(selectedFromDate, selectedToDate)){
                val intent =  Intent(context, AttendanceReportPreviewActivity::class.java)
                intent.putExtra("FROM_QUERY_DATE", selectedFromDate)
                intent.putExtra("TO_QUERY_DATE", selectedToDate)
                intent.putExtra("ORDER_BY", "Dates")

                startActivity(intent)
            }else{
                Toast.makeText(context, "check your date range please", Toast.LENGTH_LONG).show()
            }

        }

        return view
    }

    private fun validQueryDate(from: String, to: String):Boolean{
        val simpleDateFormat= SimpleDateFormat("yyyy-MM-dd", Locale.UK)
        if (simpleDateFormat.parse(to)?.before(simpleDateFormat.parse(from)) == true) return false
        return true
    }



    private fun convertDate(year: Int, thisMonth: Int, day: Int): String {
        val stringMonth = if(thisMonth<10 && thisMonth.toString().length < 2){
            "0$thisMonth"
        }else{
            "$thisMonth"
        }

        val stringDay = if(day<10 && day.toString().length < 2){
            "0$day"
        }else{
            "$day"
        }
        return "$year-$stringMonth-$stringDay"
    }

}
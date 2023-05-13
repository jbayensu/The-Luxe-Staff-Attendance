package com.acetechsol.auththree

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class SettingFragment : Fragment() {

    private lateinit var backupBtn:Button
    private lateinit var restoreBtn:Button
/*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

 */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_setting, container, false)

        initViews(view)

        backupBtn.setOnClickListener {
            pickFolderLocation()
        }

        restoreBtn.setOnClickListener {
            pickFile()
        }


        return view
    }

    private fun pickFile() {


    }

    private fun pickFolderLocation() {

    }

    private fun initViews(view: View) {
        backupBtn = view.findViewById(R.id.backup_Btn)
        restoreBtn = view.findViewById(R.id.restore_Btn)
    }




}
package com.example.test101

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

data class DropItem(var name : String?=  null, var lat : Double = 2.0,
                    var lng : Double = 4.0, var category : String?= null, var time : String?= null) {

    fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            val netDate = Date(s.toLong())
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }

    }
}
/*
 * *******************************************************************************
 *  Expired Today
 *
 *  Copyright (c) 2024.
 *  MrKaplan
 *
 * ******************************************************************************
 *
 */

package com.mrkaplan.expiredtoday

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddProductActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    private lateinit var btnSave: Button
    private lateinit var etProductName: EditText
    private lateinit var etExpirationDate: DatePicker
    private lateinit var countitem: NumberPicker

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Initialize views
        btnSave = findViewById(R.id.btnSave)
        etProductName = findViewById(R.id.etProductName)
        etExpirationDate = findViewById(R.id.etExpirationDate)
        countitem = findViewById(R.id.countitem)

        countitem.maxValue = 100
        countitem.minValue = 1
        countitem.textColor = ContextCompat.getColor(this, R.color.white)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("products")

        // Set up click listener for save button
        btnSave.setOnClickListener {
            saveProduct()
        }
    }

    private fun saveProduct() {
        val productName = etProductName.text.toString().trim()
        val expirationDate = getDateFromDatePicker(etExpirationDate)

        if (productName.isEmpty() || expirationDate == null) {
            showToast("Please enter product name and expiration date")
            return
        }

        val productId = database.push().key ?: ""
        val timeStamp = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        val quant = countitem.value

        val product = Product(productId, productName, expirationDate, timeStamp, quant)

        database.child(productId).setValue(product)
            .addOnSuccessListener {
                showToast("Product saved successfully")
                finish()
            }
            .addOnFailureListener {
                showToast("Failed to save product")
            }
    }

    private fun getDateFromDatePicker(datePicker: DatePicker): Date? {
        val calendar = Calendar.getInstance()
        calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
        return calendar.time
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

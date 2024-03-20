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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Constants
    private lateinit var btnAddProduct: Button
    private lateinit var btnProductList: Button
    private lateinit var imgLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize buttons using View Binding (recommended) or Kotlin View Extensions
        btnAddProduct = findViewById(R.id.btnAddProduct)
        btnProductList = findViewById(R.id.btnProductList)

        // Set click listeners for menu buttons
        btnAddProduct.setOnClickListener {
            // Navigate to AddProductActivity
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        btnProductList.setOnClickListener {
            // Navigate to ProductListActivity
            startActivity(Intent(this, ProductListActivity::class.java))
        }

        imgLogo = findViewById(R.id.imgLogo)
        imgLogo.setOnClickListener {
            // Navigate to ProductListActivity
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.todaygroup.gr"))
            startActivity(browserIntent)
        }
    }
}

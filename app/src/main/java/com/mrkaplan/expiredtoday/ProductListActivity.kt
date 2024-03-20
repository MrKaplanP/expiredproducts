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

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProductListActivity : AppCompatActivity(), ProductListAdapter.OnDeleteClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var database: FirebaseDatabase
    private lateinit var productList: MutableList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        // Retrieve product list from Firebase
        retrieveProductList()
    }

    private fun retrieveProductList() {
        productList = mutableListOf()

        val ref = database.getReference("products")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let {
                        productList.add(it)
                    }
                }

                // Sort products
                productList.sortByDescending { it.expirationDate }

                // Populate RecyclerView
                val adapter = ProductListAdapter(productList, this@ProductListActivity)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                error.toException().printStackTrace()
                Toast.makeText(
                    this@ProductListActivity,
                    "Failed to retrieve product list from the database",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onDeleteClick(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete this product?")
            .setPositiveButton("Delete") { _, _ ->
                val productId = product.id ?: return@setPositiveButton
                val ref = database.getReference("products").child(productId)
                ref.removeValue()
                Toast.makeText(this@ProductListActivity, "Product deleted", Toast.LENGTH_SHORT)
                    .show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}


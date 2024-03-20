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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date?.isTomorrow(tomorrow: Date): Boolean {
    val cal = Calendar.getInstance()
    this?.let { cal.time = it }
    val tomorrowCal = Calendar.getInstance().apply { time = tomorrow }
    return cal.get(Calendar.YEAR) == tomorrowCal.get(Calendar.YEAR) &&
            cal.get(Calendar.MONTH) == tomorrowCal.get(Calendar.MONTH) &&
            cal.get(Calendar.DAY_OF_MONTH) == tomorrowCal.get(Calendar.DAY_OF_MONTH)
}

class ProductListAdapter(
    private var productList: List<Product> = emptyList(),
    private val onDeleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {


    interface OnDeleteClickListener {
        fun onDeleteClick(product: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = sortedProductList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int = sortedProductList.size

    private val sortedProductList: List<Product> by lazy {
        val today = Calendar.getInstance().time
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
        }.time

        productList.sortedWith(compareByDescending<Product> { it.expirationDate?.before(today) == true }
            .thenBy { it.expirationDate?.isTomorrow(tomorrow) == true }
            .thenBy { it.expirationDate })
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private val expirationDateTextView: TextView =
            itemView.findViewById(R.id.textViewExpirationDate)
        private val deleteButton: Button = itemView.findViewById(R.id.btnDelete)
        private val countItem: TextView = itemView.findViewById(R.id.textViewCount)
        private val tomorrow: Date = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1) // Get tomorrow's date
        }.time


        fun bind(product: Product) {
            nameTextView.text = product.name
            expirationDateTextView.text = product.expirationDate?.formatDate()

            deleteButton.setOnClickListener {
                onDeleteClickListener.onDeleteClick(product)
            }

            countItem.text = product.count.toString()


            val col = when {
                product.expirationDate?.before(Date()) == true -> {
                    ContextCompat.getColor(itemView.context, R.color.expired)
                }

                isToday(product.expirationDate) -> {
                    ContextCompat.getColor(itemView.context, R.color.expired)
                }

                product.expirationDate?.isTomorrow(tomorrow) == true -> {
                    ContextCompat.getColor(itemView.context, R.color.tomorrow)
                }

                else -> {
                    ContextCompat.getColor(itemView.context, R.color.valid)
                }
            }

            itemView.setBackgroundColor(col)
        }

        private fun Date?.formatDate(): String {
            return this?.let {
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(it)
            } ?: ""
        }

        private fun isToday(date: Date?): Boolean {
            val today = Calendar.getInstance().time
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(today) ==
                    date?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it) }
        }
    }
}

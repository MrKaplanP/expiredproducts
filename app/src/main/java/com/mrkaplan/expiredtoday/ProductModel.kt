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

import com.google.firebase.database.IgnoreExtraProperties
import java.util.Date

@IgnoreExtraProperties
data class Product(
    var id: String? = "",
    var name: String? = "",
    var expirationDate: Date? = null,
    var timestamp: String? = "",
    var count: Int? = 0
)

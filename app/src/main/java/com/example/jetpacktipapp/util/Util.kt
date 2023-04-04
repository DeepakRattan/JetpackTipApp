package com.example.jetpacktipapp.util

/**
 * Created by Deepak Rattan on 04/04/23
 */

fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage) / 100
    else
        0.0

}
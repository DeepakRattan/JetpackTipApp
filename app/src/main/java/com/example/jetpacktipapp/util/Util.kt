package com.example.jetpacktipapp.util

/**
 * Created by Deepak Rattan on 04/04/23
 */

// Total Tip Calculation
fun calculateTotalTip(totalBill: Double, tipPercentage: Int): Double {
    return if (totalBill > 1 && totalBill.toString().isNotEmpty())
        (totalBill * tipPercentage) / 100
    else
        0.0
}

// Total per person calculation

fun calculateTotalPerPerson(totalBill: Double, spiltBy: Int, tipPercentage: Int): Double {
    val bill = calculateTotalTip(totalBill = totalBill, tipPercentage = tipPercentage) + totalBill
    return (bill / spiltBy)
}
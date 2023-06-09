package com.example.jetpacktipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpacktipapp.MainActivity.Companion.TAG
import com.example.jetpacktipapp.components.InputField
import com.example.jetpacktipapp.ui.theme.JetpackTipAppTheme
import com.example.jetpacktipapp.util.calculateTotalPerPerson
import com.example.jetpacktipapp.util.calculateTotalTip
import com.example.jetpacktipapp.widgets.RoundIconButton

class MainActivity : ComponentActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetpackTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colors.background
        ) {
            content()
        }
    }
}

//@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 130.0) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(all = 15.dp)
            .clip(
                shape = CircleShape.copy(all = CornerSize(12.dp))
            ), color = Color(0xFFE9D7f7)
//            .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(all = 12.dp)

        ) {
            val total = "%.2f".format(totalPerPerson)

            Text(
                text = "Total Per Person", style = MaterialTheme.typography.h5
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent() {
    val splitByState = remember {
        mutableStateOf(1)
    }


    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    //val range = IntRange(start = 1, endInclusive = 100)

    Column(modifier = Modifier.padding(all = 12.dp)) {
        BillForm(
            splitByState = splitByState,
            tipAmountState = tipAmountState,
            totalPerPersonState = totalPerPersonState
        )
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    onValueChange: (String) -> Unit = {},
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>

) {
    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val sliderPositionState = remember {
        mutableStateOf(0f)
    }


    val tipPercentage = (sliderPositionState.value * 100).toInt()


    val keyboardController = LocalSoftwareKeyboardController.current
    // Top Header
    TopHeader(totalPerPerson = totalPerPersonState.value)

    Surface(
        modifier = modifier
            .padding(all = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(all = 5.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(valueState = totalBillState,
                label = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValueChange(totalBillState.value.trim())

                    // Hide the keyboard
                    keyboardController?.hide()
                })
            /* if (validState) {*/
            Row(
                modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Split",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(120.dp))
                Row(
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    RoundIconButton(imageVector = Icons.Default.Remove,
                        onClick = {
                            Log.d(TAG, "BillForm: Remove clicked")
                            if (splitByState.value > 1)
                                splitByState.value = splitByState.value - 1
                            else 1

                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBillState.value.toDouble(),
                                splitByState.value,
                                tipPercentage
                            )
                        }
                    )
                    Text(
                        text = splitByState.value.toString(),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp)
                    )
                    RoundIconButton(imageVector = Icons.Default.Add,
                        onClick = {
                            Log.d(TAG, "BillForm: Add Clicked")
                            if (splitByState.value < range.last)
                                splitByState.value = splitByState.value + 1

                            totalPerPersonState.value = calculateTotalPerPerson(
                                totalBillState.value.toDouble(),
                                splitByState.value,
                                tipPercentage
                            )
                        }
                    )
                }
            }
            /* } else {
                 Box() {
 
                 }
             }*/

            Row() {
                Text(
                    text = "Tip",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(200.dp))
                Text(
                    text = "$ ${tipAmountState.value}",
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "$tipPercentage%")
                Spacer(modifier = Modifier.height(14.dp))
                // Slider
                Slider(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                    value = sliderPositionState.value,
                    steps = 5,
                    onValueChange = { newSliderState ->
                        sliderPositionState.value = newSliderState

                        tipAmountState.value =
                            calculateTotalTip(totalBillState.value.toDouble(), tipPercentage)

                        totalPerPersonState.value = calculateTotalPerPerson(
                            totalBillState.value.toDouble(),
                            splitByState.value,
                            tipPercentage
                        )
                    },
                    onValueChangeFinished = {
                        Log.d(TAG, "BillForm: End of slider")
                    }
                )
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackTipAppTheme {
        MyApp {
            // TopHeader()
//            MainContent()
        }
    }
}
package com.zfml.bookworm.presentation.addBook.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun BoughtDateTextField(
    boughtDate: (Long) -> Unit,
    isLoading: Boolean
){
    Text(
        text = "Bought Date",
        fontSize = MaterialTheme.typography.titleMedium.fontSize,
        fontWeight = FontWeight.SemiBold
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    val currentDate = Date().toFormattedString()

    var selectedDate by remember{ mutableStateOf(currentDate) }


    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

    calendar.time = Date()

    val datePickerDialog =
        DatePickerDialog( context, { _: DatePicker, year: Int,month: Int, dayOfMonth: Int ->
            val newDate = Calendar.getInstance()
            newDate.set(year,month,dayOfMonth)
            boughtDate(newDate.timeInMillis)
            selectedDate = "${month.toMonthName()} $dayOfMonth, $year"
        },year, month, day)


    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        value = selectedDate,
        onValueChange = {},
        trailingIcon = {
              Icon(
                  imageVector = Icons.Default.DateRange,
                  contentDescription = "Date Icon"
              )
        },
        interactionSource = interactionSource,
        enabled = !isLoading
    )

    if (isPressed) {
        datePickerDialog.show()
    }



}
fun Int.toMonthName(): String {
    return DateFormatSymbols().months[this]
}
fun Date.toFormattedString(): String {
    val simpleDateFormat = SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault())
    return simpleDateFormat.format(this)
}
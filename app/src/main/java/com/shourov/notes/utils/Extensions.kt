package com.shourov.notes.utils

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty

fun Context.showWarningToast(message: String, duration: Int = Toast.LENGTH_SHORT) = Toasty.warning(this, message, duration, true).show()
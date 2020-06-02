package net.htlgrieskirchen.at.jeschl17.nfcdroid.util

import android.app.AlertDialog
import android.content.Context

fun showAlertDialog(viewId: Int, titleId: Int, context: Context) {
   AlertDialog.Builder(context)
      .setTitle(titleId)
      .setView(viewId)
      .setPositiveButton("Ok", null)
      .create()
      .show()
}
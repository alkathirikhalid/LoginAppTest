package my.com.fauzan.advisoryapps.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.ui_dialog_text_hint.*
import my.com.fauzan.advisoryapps.R
import my.com.fauzan.advisoryapps.model.Model

class CustomDialog(context: Context, person: Model.Person) :
    Dialog(context, R.style.UI_DefaultDialogStyle) {

    init {
        setContentView(R.layout.ui_dialog_text_hint)
        window!!.attributes.gravity = 17
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        if (context is Activity) {
            setOwnerActivity(context)
        }

        et_id.setText(person.id.toString())
        et_name.setText(person.name)
        et_distance.setText(person.distance)
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        tv_update!!.setOnClickListener(listener)
    }

    fun getUpdatedUser(): Model.Person? {
        return if (!et_id.text.isNullOrEmpty() && !et_name.text.isNullOrEmpty() && !et_distance.text.isNullOrEmpty()) {
            if (et_id.text.toString().trim().toIntOrNull() != null && et_distance.text.toString().trim().toDoubleOrNull() != null  ) {
                return Model.Person(
                    et_id.text.toString().trim().toInt(),
                    et_name.text.toString().trim(),
                    et_distance.text.toString().trim()
                )
            } else {
                Toast.makeText(context, "Id and distance must be number", Toast.LENGTH_SHORT).show()
                null
            }
        } else {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            null
        }
    }
}


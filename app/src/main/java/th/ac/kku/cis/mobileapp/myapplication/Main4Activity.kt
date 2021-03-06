package th.ac.kku.cis.mobileapp.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main4.*
import java.text.SimpleDateFormat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main4.side


class Main4Activity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    val event = event_data.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)

        mDatabase = FirebaseDatabase.getInstance().reference

        button4.setOnClickListener{
            addNewItemDialog()
        }
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            event.date_start = dayOfMonth.toString() +"/"+ month.toString()+"/" + year.toString()
        }
        calendarView2.setOnDateChangeListener { view, year, month, dayOfMonth ->
            event.date_end = dayOfMonth.toString() +"/"+ month.toString()+"/" + year.toString()
        }
        val spinner: Spinner = findViewById(R.id.side)
        val arrayList: ArrayList<String> = ArrayList()
        arrayList.add("ด้านที่1")
        arrayList.add("ด้านที่2")
        arrayList.add("ด้านที่3")
        arrayList.add("ด้านที่4")
        arrayList.add("ด้านที่5")
        val arrayAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayList)
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter
    }
    private fun addNewItemDialog() {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        if (event.date_start==null){
            event.date_start = sdf.format(calendarView.getDate())
        }
        if (event.date_end==null){
            event.date_end = sdf.format(calendarView2.getDate())
        }
        event.event_name = editText2.text.toString()
        event.event_detail = editText3.text.toString()
        event.event_side = this.side.selectedItem.toString()
        event.event_unit = this.unit.text.toString()
        // create new record
        val newItem = mDatabase.child("event_data").push()
        // add new key to todoobject
        event.objectId = newItem.key
        // set todoobject to new record on firebase db
        newItem.setValue(event)
        // display data to user
        Toast.makeText(this, event.event_name + " บันทึกสำเร็จ", Toast.LENGTH_SHORT).show()
        finish()
    }
}

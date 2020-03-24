package th.ac.kku.cis.mobileapp.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main6.*

class Main6Activity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var event_list: MutableList<event_data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)

        val objectId = getIntent().getExtras()!!.getString("objectId")

        mDatabase = FirebaseDatabase.getInstance().getReference("event_data")

        event_list = mutableListOf<event_data>()
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)

        add.setOnClickListener {
            val intent = Intent(this, Main7Activity::class.java)
            intent.putExtra("event_name",name.text)
            startActivity(intent)
        }

        delete.setOnClickListener {
            mDatabase.child(objectId!!).removeValue()
            Toast.makeText(this,"ลบ "+name.text+" แล้ว", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Main5Activity::class.java)
            startActivity(intent)
            finish()
        }

    }
    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            val objectId = getIntent().getExtras()!!.getString("objectId")
            val items = p0.child(objectId!!)
            val map = items.getValue() as HashMap<String, Any>
            val event_name = map.get("event_name") as String
            val event_detail = map.get("event_detail") as String
            val date_start = map.get("date_start") as String
            val date_end = map.get("date_end") as String
            name.text = event_name
            detail.text = event_detail
            start.text = "เริ่มวันที่ : " + date_start
            end.text = "สิ้นสุดวันที่ : " + date_end
        }
        override fun onCancelled(p0: DatabaseError) {

        }
    }
}

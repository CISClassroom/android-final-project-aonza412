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
        val event_name = getIntent().getExtras()!!.getString("event_name")

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
            FirebaseDatabase.getInstance().getReference("event_item").orderByChild("event_name")
                .equalTo(event_name)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        val event_item = p0.children.iterator()
                        if(event_item.hasNext()){
                            while (event_item.hasNext()){
                                val event_item = event_item.next().getValue() as HashMap<String, Any>
                                if(event_item.get("event_name")==event_name){
                                    FirebaseDatabase.getInstance().getReference("event_item").child(event_item.get("objectId")as String).removeValue()
                                }
                            }
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
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
            name.text = map.get("event_name") as String
            detail.text = map.get("event_detail") as String
            side.text = "กิจกรรม"+map.get("event_side") as String
            unit.text = "จำนวน "+map.get("event_unit") as String+" หน่วยกิจ"
            start.text = map.get("date_start") as String
            end.text = map.get("date_end") as String
        }
        override fun onCancelled(p0: DatabaseError) {

        }
    }
}

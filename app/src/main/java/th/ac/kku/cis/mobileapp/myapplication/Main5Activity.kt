package th.ac.kku.cis.mobileapp.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main5.*


class Main5Activity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var event_list: MutableList<event_data>
    lateinit var adapter: eventAdapter
    private var listViewItems: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        mDatabase = FirebaseDatabase.getInstance().getReference("event_data")
        listViewItems = findViewById<ListView>(R.id.list_event) as ListView

        event_list = mutableListOf<event_data>()
        adapter = eventAdapter(this, event_list!!)
        listViewItems!!.setAdapter(adapter)
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)

        list_event.setOnItemClickListener{parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as event_data
            Toast.makeText(this,selectedItem.event_name, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Main6Activity::class.java)
            intent.putExtra("objectId",selectedItem.objectId)
            intent.putExtra("event_name",selectedItem.event_name)
            startActivity(intent)
        }
    }
    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            val items = p0.children.iterator()
            while (items.hasNext()){
                val currentItem = items.next()
                val event = event_data.create()
                val map = currentItem.getValue() as HashMap<String, Any>
                event.event_name = map.get("event_name") as String?
                event.event_detail = map.get("event_detail") as String?
                event.objectId = currentItem.key
                event.date_start = map.get("date_start") as String?
                event.date_end = map.get("date_end") as String?
                event_list!!.add(event)
            }
            adapter.notifyDataSetChanged()
        }
        override fun onCancelled(p0: DatabaseError) {

        }
    }
}

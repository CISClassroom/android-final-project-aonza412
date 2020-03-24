package th.ac.kku.cis.mobileapp.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main3.*
import android.widget.ListView
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class Main3Activity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference

    var event_list: MutableList<event>? = null
    lateinit var adapter: event_adapter
    private var listViewItems: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        mDatabase = FirebaseDatabase.getInstance().reference
        listViewItems = findViewById<View>(R.id.list_view) as ListView

        event_list = mutableListOf<event>()
        adapter = event_adapter(this, event_list!!)
        listViewItems!!.setAdapter(adapter)
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)

        val id = getIntent().getExtras()!!.getString("id")
        val name = getIntent().getExtras()!!.getString("name")
        val icon = getIntent().getExtras()!!.getInt("icon")
        textView.text = name
        textView2.text = id
        imageView.setImageResource(icon)
    }
    var itemListener: ValueEventListener = object : ValueEventListener {

        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // call function
            addDataToList(dataSnapshot.child("event_item"))
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, display log a message
            Log.w("MainActivity", "loadItem:onCancelled", databaseError.toException())
        }
    }
    fun addDataToList(dataSnapshot: DataSnapshot) {
        val id = getIntent().getExtras()!!.getString("id")
        val items = dataSnapshot.children.iterator()
            while (items.hasNext()) {
                val currentItem = items.next()
                val map = currentItem.getValue() as HashMap<String, Any>
                val todoItem = event.create()
                todoItem.user_id = map.get("user_id") as String?
                if (todoItem.user_id == id && map.get("done") == true){
                    todoItem.objectId = currentItem.key
//                    todoItem.user_id = map.get("user_id") as String?
                    todoItem.event_name = map.get("event_name") as String?
                    event_list!!.add(todoItem)
                }
            }
            adapter.notifyDataSetChanged()
//        }
    }
}

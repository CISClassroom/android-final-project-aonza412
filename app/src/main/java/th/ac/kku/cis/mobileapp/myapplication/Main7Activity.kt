package th.ac.kku.cis.mobileapp.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.google.firebase.database.*

class Main7Activity : AppCompatActivity(), ItemRowListener{

    lateinit var mDatabase: DatabaseReference
    private var listViewItems: ListView? = null
    var toDoItemList: MutableList<add_event>? = null
    lateinit var adapter: AddeventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main7)

        mDatabase = FirebaseDatabase.getInstance().reference
        listViewItems = findViewById<View>(R.id.add_list) as ListView
        toDoItemList = mutableListOf<add_event>()
        adapter = AddeventAdapter(this, toDoItemList!!)
        listViewItems!!.setAdapter(adapter)
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)

    }
    var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            val event_name = getIntent().getExtras()!!.getString("event_name")
            val student = p0.child("student").children.iterator()
            val c_event = p0.child("event_item").children.iterator()
            // Check if current database contains any collection
//            if (student.hasNext()) {
                while (student.hasNext()) {
                    val add_event = add_event.create()
                    val event = event.create()
                    val studentItem = student.next().getValue() as HashMap<String, Any>
                    if (c_event.hasNext()){
                        val eventItem = c_event.next().getValue() as HashMap<String, Any>
                        if (eventItem.get("user_id")==studentItem.get("id") && eventItem.get("event_name") == event_name){
                            add_event.id = studentItem.get("id") as String?
                            add_event.name = studentItem.get("name") as String?
                            add_event.done = eventItem.get("done") as Boolean?
                            add_event.objectId = eventItem.get("objectId") as String?
                        }else{
                            val newItem = mDatabase.child("event_item").push()
                            add_event.id = studentItem.get("id") as String?
                            add_event.name = studentItem.get("name") as String?
                            event.event_name = event_name
                            event.done = false
                            event.objectId = newItem.key
                            event.user_id = studentItem.get("id") as String?
                            add_event.done = false
                            add_event.objectId = newItem.key
                            newItem.setValue(event)
                        }
                    }else{
                        val newItem = mDatabase.child("event_item").push()
                        add_event.id = studentItem.get("id") as String?
                        add_event.name = studentItem.get("name") as String?
                        event.event_name = event_name
                        event.done = false
                        event.objectId = newItem.key
                        event.user_id = studentItem.get("id") as String?
                        add_event.done = false
                        add_event.objectId = newItem.key
                        newItem.setValue(event)
                    }
                    toDoItemList!!.add(add_event)
                }
                adapter.notifyDataSetChanged()
//            }
        }
        override fun onCancelled(p0: DatabaseError) {

        }
    }
    override fun modifyItemState(itemObjectId: String, index: Int, isDone: Boolean) {
        //get child reference in database via the ObjectID
        toDoItemList!!.get(index).done = !isDone
        adapter.notifyDataSetChanged()
        val itemReference = mDatabase.child("event_item").child(itemObjectId)
        itemReference.child("done").setValue(!isDone)
    }
}

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
            FirebaseDatabase.getInstance().getReference("student")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val studentItem = dataSnapshot.children.iterator()
                        var i=0
                        if(studentItem.hasNext()) {
                            while (studentItem.hasNext()) {
                                val studentItem =
                                    studentItem.next().getValue() as HashMap<String, Any>
                                FirebaseDatabase.getInstance().getReference("event_item").orderByChild("user_id")
                                    .equalTo(studentItem.get("id") as String)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(p0: DataSnapshot) {
                                            val add_event = add_event.create()
                                            val event_item = p0.children.iterator()
                                            if(event_item.hasNext()){
                                                while (event_item.hasNext()){
                                                    val event_item = event_item.next().getValue() as HashMap<String, Any>
                                                    if(event_item.get("event_name")==event_name){
                                                        add_event.id = studentItem.get("id") as String?
                                                        add_event.name = studentItem.get("name") as String?
                                                        add_event.done = event_item.get("done") as Boolean?
                                                        add_event.objectId = event_item.get("objectId") as String?
                                                        i+=1
                                                        toDoItemList!!.add(add_event)
                                                    }
                                                }
                                                if(i==0){
                                                    val event = event.create()
                                                    val newItem = mDatabase.child("event_item").push()
                                                    event.event_name = event_name
                                                    event.done = false
                                                    event.objectId = newItem.key
                                                    event.user_id = studentItem.get("id") as String?
                                                    add_event.id = studentItem.get("id") as String?
                                                    add_event.name = studentItem.get("name") as String?
                                                    add_event.done = false
                                                    add_event.objectId = newItem.key
                                                    newItem.setValue(event)
                                                    toDoItemList!!.add(add_event)
                                                }
                                                adapter.notifyDataSetChanged()
                                            }
                                            else{
                                                val event = event.create()
                                                val newItem = mDatabase.child("event_item").push()
                                                event.event_name = event_name
                                                event.done = false
                                                event.objectId = newItem.key
                                                event.user_id = studentItem.get("id") as String?
                                                add_event.id = studentItem.get("id") as String?
                                                add_event.name = studentItem.get("name") as String?
                                                add_event.done = false
                                                add_event.objectId = newItem.key
                                                newItem.setValue(event)
                                                toDoItemList!!.add(add_event)
                                                adapter.notifyDataSetChanged()
                                            }
                                        }
                                        override fun onCancelled(p0: DatabaseError) {
//                                            val add_event = add_event.create()
//                                            val event = event.create()
//                                            val newItem = mDatabase.child("event_item").push()
//                                            event.event_name = event_name
//                                            event.done = false
//                                            event.objectId = newItem.key
//                                            event.user_id = studentItem.get("id") as String?
//                                            add_event.id = studentItem.get("id") as String?
//                                            add_event.name = studentItem.get("name") as String?
//                                            add_event.done = false
//                                            add_event.objectId = newItem.key
//                                            newItem.setValue(event)
//                                            toDoItemList!!.add(add_event)
//                                            adapter.notifyDataSetChanged()
                                        }
                                    })
                            }
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
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

package th.ac.kku.cis.mobileapp.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.ValueEventListener

class event_adapter(context: Context, toDoItemList: MutableList<event>) : BaseAdapter() {

    val mInflater = LayoutInflater.from(context)
    var itemList = toDoItemList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // create object from view
//        val objectId: String = itemList.get(position).objectId as String
        val event_name: String = itemList.get(position).event_name as String
        val view: View
        val vh: ListRowHolder

        // get list view
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_item, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        // add text to view
        vh.label.text = event_name

        return view
    }

    override fun getItem(index: Int): Any {
        return itemList.get(index)
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

    private class ListRowHolder(row: View?) {
        val label: TextView = row!!.findViewById<TextView>(R.id.tv_item_text) as TextView
    }
}

class eventAdapter(context: Context, toDoItemList: MutableList<event_data>) : BaseAdapter() {

    val mInflater = LayoutInflater.from(context)
    var itemList = toDoItemList

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // create object from view
        val event_name: String = itemList.get(position).event_name as String
        val view: View
        val vh: ListRowHolder

        // get list view
        if (convertView == null) {
            view = mInflater.inflate(R.layout.event_list, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        // add text to view
        vh.label.text = event_name

        //add button listenner
        return view
    }

    override fun getItem(index: Int): Any {
        return itemList.get(index)
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

    private class ListRowHolder(row: View?) {
        val label: TextView = row!!.findViewById<TextView>(R.id.textView3) as TextView
    }
}

class AddeventAdapter(context: Context, toDoItemList: MutableList<add_event>) : BaseAdapter() {

    val mInflater = LayoutInflater.from(context)
    var itemList = toDoItemList

    var rowListener: ItemRowListener = context as ItemRowListener

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // create object from view
        val name: String = itemList.get(position).name as String
        val id: String = itemList.get(position).id as String
        val status: Boolean = itemList.get(position).done as Boolean
        val objectId: String = itemList.get(position).objectId as String
        val view: View
        val vh: ListRowHolder

        // get list view
        if (convertView == null) {
            view = mInflater.inflate(R.layout.add_event, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        // add text to view
        vh.label1.text = name
        vh.label2.text = id
        vh.isDone.isChecked = status

        vh.isDone.setOnClickListener {
            rowListener.modifyItemState(objectId, position, status)//position have to change in list_item
        }
        //add button listenner
        return view
    }

    override fun getItem(index: Int): Any {
        return itemList.get(index)
    }

    override fun getItemId(index: Int): Long {
        return index.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

    private class ListRowHolder(row: View?) {
        val label1: TextView = row!!.findViewById<TextView>(R.id.name) as TextView
        val label2: TextView = row!!.findViewById<TextView>(R.id.id) as TextView
        val isDone: CheckBox = row!!.findViewById<CheckBox>(R.id.checkBox) as CheckBox
    }
}

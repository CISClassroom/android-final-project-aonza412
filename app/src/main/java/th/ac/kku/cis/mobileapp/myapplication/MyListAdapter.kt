package th.ac.kku.cis.mobileapp.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class MyListAdapter(var mCtx: Context, var resource:Int, var items:List<Student>)
    : ArrayAdapter<Student>( mCtx , resource , items ) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //super.getView(position, convertView, parent)

        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)

        val view: View = layoutInflater.inflate(resource, null)
        val ivIcon: ImageView = view.findViewById(R.id.icon)
        var tvStudentName : TextView = view.findViewById(R.id.studentname)
        var tvStudentID : TextView = view.findViewById(R.id.studentid)


        var student: Student = items[position]

        ivIcon.setImageDrawable(mCtx.resources.getDrawable(student.icon))
        tvStudentName.text = student.name
        tvStudentID.text = student.id

        return view
    }
}
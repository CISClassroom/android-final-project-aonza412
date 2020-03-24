package th.ac.kku.cis.mobileapp.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView


class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val listView: ListView = this.findViewById(R.id.listView)
        val students_name = resources.getStringArray(R.array.student_name_array)
        val students_id = resources.getStringArray(R.array.student_id_array)
        val students_icon = getResources().obtainTypedArray(R.array.student_icon_array)
        val student_list = mutableListOf<Student>()

        for (i in 0..students_name.size-1) {
            student_list.add(Student(students_name[i],students_id[i],students_icon.getResourceId(i, -1)))
        }

//        val footer = TextView(this)
//        footer.height = getResources().getDimensionPixelSize(R.dimen.text_view_height)
//        listView.addFooterView(footer)

        listView.adapter = MyListAdapter(
            this,
            R.layout.student_list,
            student_list
        )
        listView.setOnItemClickListener{parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as Student
//            Toast.makeText(this,selectedItem.name, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Main3Activity::class.java)
            intent.putExtra("id",selectedItem.id)
            intent.putExtra("icon",selectedItem.icon)
            intent.putExtra("name",selectedItem.name)
            startActivity(intent)
        }
    }
}

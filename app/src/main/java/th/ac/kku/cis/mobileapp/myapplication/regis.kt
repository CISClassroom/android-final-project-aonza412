package th.ac.kku.cis.mobileapp.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_regis.*

class regis : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regis)

        mDatabase = FirebaseDatabase.getInstance().reference

        register.setOnClickListener {
            val name = name.text.toString()
            val id = email.text.toString().trim { it <= ' ' }
            val password1 = pass1.text.toString().trim { it <= ' ' }
            val password2 = pass2.text.toString().trim { it <= ' ' }

            if (id.isEmpty()||id.length<11) {
                Toast.makeText(this,"Please enter your E-mail.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password1.isEmpty()||password2.isEmpty()) {
                Toast.makeText(this,"Please enter your pass.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (name.isEmpty()){
                Toast.makeText(this,"Please enter your name.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mDatabase.child("student").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val student = dataSnapshot.children.iterator()
                    if(student.hasNext()){
                        var i=0
                        while (student.hasNext()){
                            val studentItem = student.next().getValue() as HashMap<String, Any>
                            if (studentItem.get("id") == id){
                                i+=1
                            }
                        }
                        if(i==0){
                            if (password1==password2){
                                val user = user.create()
                                val newItem = mDatabase.child("user").push()
                                user.id=id
                                user.name=name
                                user.pass=password1
                                user.objectId = newItem.key
                                newItem.setValue(user)
                                Toast.makeText(this@regis,"Register Success!.", Toast.LENGTH_SHORT).show()
                                finish()
                            }else{
                                Toast.makeText(this@regis,"Password does not match.", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this@regis,"This E-mail registed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
    }
}

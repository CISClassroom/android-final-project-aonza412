package th.ac.kku.cis.mobileapp.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class login : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mDatabase = FirebaseDatabase.getInstance().reference

        button2.setOnClickListener {
            val id = user.text.toString().trim { it <= ' ' }
            val password = pass.text.toString().trim { it <= ' ' }

            if (id.isEmpty()) {
                Toast.makeText(this,"Please enter your E-mail.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this,"Please enter your password.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            mDatabase.child("user")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val student = dataSnapshot.children.iterator()
                        var i=0
                        if(student.hasNext()){
                            while (student.hasNext()){
                                val studentItem = student.next().getValue() as HashMap<String, Any>
                                if (studentItem.get("id")==id && studentItem.get("pass") == password){
                                    i=0
                                    val intent = Intent(this@login, MainActivity::class.java)
                                    intent.putExtra("id",studentItem.get("id") as String)
                                    intent.putExtra("name",studentItem.get("name") as String)
                                    startActivity(intent)
                                    user.text=null
                                    pass.text=null
                                    break
                                }else{
                                    i=1
                                }
                            }
                        }
                        if(i!=0){
                            Toast.makeText(this@login,"Wrong email or password.", Toast.LENGTH_SHORT).show()
                        }
//                        else{
//                            Toast.makeText(this@MainActivity,"Wrong email or password.", Toast.LENGTH_SHORT).show()
//                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
        }
        button3.setOnClickListener {
            startActivity(Intent(this, regis::class.java))
        }
    }
}

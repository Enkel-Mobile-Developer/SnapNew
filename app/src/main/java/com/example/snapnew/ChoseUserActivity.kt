package com.example.snapnew

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChoseUserActivity : AppCompatActivity() {
    var choseUserView: ListView? = null
   var emails:ArrayList<String> = ArrayList()
    var keys: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chose_user)
        val actionBar = supportActionBar
        actionBar!!.title = "chose User Activty"
        choseUserView = findViewById(R.id.chooseUserListView)
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        choseUserView?.adapter = adapter
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val email = snapshot?.child("email")?.value as String
                emails.add(email)
                keys.add(snapshot.key.toString())
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        choseUserView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
 val snapMap: Map<String, String?> = mapOf("from" to FirebaseAuth.getInstance().currentUser!!.email!!,"imageName" to intent.getStringExtra("imageName"), "imageURL" to intent.getStringExtra("imageURL"), "message" to intent.getStringExtra("message"))
            FirebaseDatabase.getInstance().getReference().child("users").child(keys.get(i)).child("snaps").push().setValue(snapMap)

            val intent = Intent(this, ViewSnapActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}
package com.example.a14vfilm.adminActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.ViewAdsAdapter
import com.example.a14vfilm.adapters.ViewGenreAdapter
import com.example.a14vfilm.models.Genre
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class ViewAdsActivity : AppCompatActivity() {
    private var rcvViewAds: RecyclerView? = null
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_ads)

        // Initialize the required components
        initComponent()
        supportActionBar!!.hide()

        //create a list of user for adapter
        val adsList = ArrayList<String>()
        //url for firebase database (change later)
        val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
        val ref = FirebaseDatabase.getInstance(url).getReference("ads")
        val adapterViewAds = ViewAdsAdapter(adsList)

        ref.orderByChild("ads").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                adsList.clear()
                for (singleSnapshot in snapshot.children) {
                    val ads = singleSnapshot.getValue<String>()
                    adsList.add(ads!!)
                }

                rcvViewAds!!.adapter!!.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        rcvViewAds!!.adapter = adapterViewAds

        //Add new genre floating button
        findViewById<FloatingActionButton>(R.id.viewads_fltbtnAddNewAds).setOnClickListener{
//            val intent = Intent(this, AddNewAdminActivity::class.java)
//            startActivity(intent)
            selectImage()
        }

    }

    private fun initComponent(){
        rcvViewAds = findViewById(R.id.viewads_rcvAdsManagement)
        rcvViewAds!!.layoutManager = LinearLayoutManager(this)
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data!!
            if (imageUri != null) {
                val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
                val imageRef = FirebaseDatabase.getInstance(url).getReference()
                val fileName = UUID.randomUUID().toString() + ".jpg"
                val storageRef =
                    FirebaseStorage.getInstance("gs://vfilm-83cf4.appspot.com").reference.child("Images/$fileName")
                storageRef.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        imageRef.child("ads").child(imageRef.push().key!!).setValue(it.toString())
                    }
                }.addOnProgressListener {
                }
            }
        }
    }
}
package com.example.a14vfilm.adminActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.a14vfilm.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class ViewAdsDetailActivity : AppCompatActivity() {
    private var ivGenreImage: ImageView? = null
    private var imageUri: Uri? = null
    private var ads: String? = null
    private var imgUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_ads_detail)

        //get data from intent
        ads = intent.getStringExtra("ads").toString()

        initComponent()
        supportActionBar!!.hide()


        //set image for genre
        if (ads != "")
            Picasso.get().load(ads).fit().centerCrop().into(ivGenreImage)

        //Update Ads information
        findViewById<Button>(R.id.viewadsdetail_BTNSave).setOnClickListener {
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference()
            if (imgUrl != null) {
                //update for "ads" collection
                val query1 = ref.child("ads")
                query1.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (singleSnapshot in snapshot.children) {
                            if (singleSnapshot.getValue<String>() == ads) {
                                singleSnapshot.ref.setValue(imgUrl)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
                Toast.makeText(it.context, "Cập nhật quảng cáo thành công", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        //Delete Ads
        findViewById<Button>(R.id.viewadsdetail_BTNDelete).setOnClickListener {
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference()

            //update for "ads" collection
            val query1 = ref.child("ads")
            query1.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot.children) {
                        if (singleSnapshot.getValue<String>() == ads ){
                            singleSnapshot.ref.removeValue()
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

            Toast.makeText(it.context, "Xóa quảng cáo thành công", Toast.LENGTH_SHORT).show()
            finish()
        }


        ivGenreImage!!.setOnClickListener {
            selectImage()
        }
    }

    private fun initComponent() {
        ivGenreImage = findViewById(R.id.viewadsdetail_IVDetail)
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
                        imgUrl = it.toString()
                        Picasso.get().load(imageUri).resize(150, 150).into(ivGenreImage)
                    }
                }.addOnProgressListener {
                }
            }
        }
    }
}
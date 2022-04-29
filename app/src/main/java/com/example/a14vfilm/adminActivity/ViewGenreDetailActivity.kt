package com.example.a14vfilm.adminActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Genre
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class ViewGenreDetailActivity : AppCompatActivity() {
    private var tvGenreName: EditText? = null
    private var ivGenreImage: ImageView? = null
    private var imageUri: Uri? = null
    private var genreID: String? = null
    private var genreName: String? = null
    private var genreImage: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_genre_detail)

        //get data from intent
        genreID = intent.getStringExtra("genreID").toString()
        genreName = intent.getStringExtra("genreName").toString()
        genreImage =  intent.getStringExtra("genreImage").toString()

        initComponent()

        //set layout with user information
        tvGenreName!!.setText(genreName)

        //set image for genre
        if (genreImage != "")
            Picasso.get().load(genreImage).resize(150, 150).into(ivGenreImage!!)

        //Update Genre information
        findViewById<Button>(R.id.viewgenredetail_BTNSave).setOnClickListener{
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference()

            //update for "genre" collection
            val query1 = ref.child("genre").child(genreID!!).child("name").setValue(tvGenreName!!.text.toString())

            //update for "film" collection
            val query2 = ref.child("film")
            query2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot.children) {
                        for (singleSnapshotChild in singleSnapshot.child("genre").children){
                            if (singleSnapshotChild.value.toString() == genreName){
                                singleSnapshotChild.ref.setValue(tvGenreName!!.text.toString())
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
            Toast.makeText(it.context, "Cập nhật thể loại thành công", Toast.LENGTH_SHORT).show()
//            val intent = Intent(it.context, ViewGenreActivity::class.java)
//            startActivity(intent)
            finish()
        }

        //Delete Genre
        findViewById<Button>(R.id.viewgenredetail_BTNDelete).setOnClickListener{
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference()

            //update for "genre" collection
            val query1 = ref.child("genre").child(genreID!!).removeValue()

            //update for "film" collection
            val query2 = ref.child("film")
            query2.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot.children) {
                        for (singleSnapshotChild in singleSnapshot.child("genre").children){
                            if (singleSnapshotChild.value.toString() == genreName){
                                singleSnapshotChild.ref.removeValue()
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
            Toast.makeText(it.context, "Xóa thể loại thành công", Toast.LENGTH_SHORT).show()
//            val intent = Intent(it.context, ViewGenreActivity::class.java)
//            startActivity(intent)
            finish()
        }


        ivGenreImage!!.setOnClickListener {
            selectImage()
        }
    }

    private fun initComponent(){
        tvGenreName = findViewById(R.id.viewgenredetail_TVDName)
        ivGenreImage = findViewById(R.id.viewgenredetail_IVDetail)

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
                val storageRef = FirebaseStorage.getInstance("gs://vfilm-83cf4.appspot.com").reference.child("avatars/$fileName")
                storageRef.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        imageRef.child("genre").child(genreID!!).child("image").setValue(it.toString())
                        Picasso.get().load(imageUri).resize(150, 150).into(ivGenreImage)
                    }
                }.addOnProgressListener {
                }
            }
        }
    }
}
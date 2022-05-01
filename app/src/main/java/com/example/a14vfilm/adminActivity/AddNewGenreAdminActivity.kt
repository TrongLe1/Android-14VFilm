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
import com.example.a14vfilm.models.Genre
import com.example.a14vfilm.models.User
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*

class AddNewGenreAdminActivity : AppCompatActivity() {
    private var etGenreName: EditText? = null
    private var ivImage: ImageView? = null
    var imageUri: Uri? = null
    var genreImage: String = ""
    val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    val ref = FirebaseDatabase.getInstance(url).getReference("genre")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_genre_admin)
        supportActionBar!!.hide()

        etGenreName = findViewById(R.id.addnewgenreadmin_TVDName)
        ivImage = findViewById(R.id.addnewgenreadmin_IVDetail)


        findViewById<Button>(R.id.addnewgenreadmin_BTNSave).setOnClickListener{
            var genreName = etGenreName!!.text.toString()


            val genre = Genre(ref.push().key!!, genreName, genreImage)
            ref.child(genre.id).setValue(genre)
            Toast.makeText(this, "Thêm thể loại thành công", Toast.LENGTH_SHORT).show()
//            val intent = Intent(this, ViewGenreActivity::class.java)
//            startActivity(intent)
            finish()
        }

        ivImage!!.setOnClickListener {
            selectImage()
        }

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
                val fileName = UUID.randomUUID().toString() + ".jpg"
                val storageRef = FirebaseStorage.getInstance("gs://vfilm-83cf4.appspot.com").reference.child("avatars/$fileName")
                storageRef.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                        genreImage = it.toString()

                        Picasso.get().load(imageUri).resize(150, 150).into (ivImage)
                    }
                }.addOnProgressListener {
                }
            }
        }
    }
}
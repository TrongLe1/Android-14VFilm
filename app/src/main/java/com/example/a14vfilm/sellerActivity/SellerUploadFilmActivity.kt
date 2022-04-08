package com.example.a14vfilm.sellerActivity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.example.a14vfilm.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI
import java.util.jar.Manifest

class SellerUploadFilmActivity : AppCompatActivity() {

    var filmImage: ImageView? = null

    var etFilmName: EditText? = null
    var etFilmDescription: EditText? = null
    var etFilmRentPrice: EditText? = null
    var etFilmBuyPrice: EditText? = null
    var etFilmDuration: EditText? = null

    var spnCategory: Spinner? = null
    var spnCountry: Spinner? = null
    var spnFilmLong: Spinner? = null
    var categoryAdapter: SpinnerAdapter? = null

    var btnFilmUpload: Button? = null

    private lateinit var actionBar: ActionBar
    private lateinit var ImageUri: Uri
    internal var storage: FirebaseStorage? = null
    internal var storageReference: StorageReference?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_upload_film)

        // Init
        initViewComponent()
        initOnCreateAdapter()

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        filmImage!!.setOnClickListener{
            Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
            selectFilmImage()
        }

        btnFilmUpload!!.setOnClickListener{
            Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
            uploadFilm()
        }

    }

    private fun selectFilmImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        gallery.type="images/*"
        startActivityForResult(gallery,1)
    }

    private fun uploadFilm() {
        TODO("Not yet implemented")
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==  1 && resultCode == Activity.RESULT_OK ){
            ImageUri =  data?.data!!
            filmImage!!.setImageURI(ImageUri)
        }
    }

    private fun initViewComponent(){

        filmImage = findViewById(R.id.ivFilmImage)

        etFilmName = findViewById(R.id.etFilmName)
        etFilmDescription = findViewById(R.id.etFilmDescription)
        etFilmRentPrice = findViewById(R.id.etFilmRentPrice)
        etFilmBuyPrice = findViewById(R.id.etFilmPrice)
        etFilmDuration = findViewById(R.id.etFilmDuration)

        spnCategory = findViewById(R.id.spnCategory)
        spnCountry = findViewById(R.id.spnCountry)
        spnFilmLong = findViewById(R.id.spnFilmLong)

        btnFilmUpload = findViewById(R.id.btnFilmUpload)

        actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

    }

    private fun initOnCreateAdapter(){
        val list = arrayListOf("1", "2", "3","4")
        categoryAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
        spnCategory!!.adapter = categoryAdapter
    }

}
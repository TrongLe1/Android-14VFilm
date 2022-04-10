package com.example.a14vfilm.sellerActivity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.example.a14vfilm.R
import com.example.a14vfilm.models.User
import com.example.a14vfilm.models.UserLogin
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.util.*


class SellerUploadFilmActivity : AppCompatActivity() {
    private val SELECT_VIDEO = 1

    private var filmImage: ImageView? = null
    private var filmTrailerVideo: VideoView? = null
    private var etFilmName: EditText? = null
    private var etFilmDescription: EditText? = null
    private var etFilmRentPrice: EditText? = null
    private var etFilmBuyPrice: EditText? = null
    private var etFilmDuration: EditText? = null

    private var spnCategory: Spinner? = null
    private var spnCountry: Spinner? = null
    private var spnFilmLong: Spinner? = null
    private var categoryAdapter: SpinnerAdapter? = null

    private var btnFilmUpload: Button? = null
    private var btnSelectTrailer: Button? = null
    private var btnSelectImage: Button? = null

    private lateinit var actionBar: ActionBar
    private lateinit var imageUri: Uri
    private lateinit var videoUri: Uri

    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var selectedVideoPath: String? = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_upload_film)

        // Init
        initViewComponent()
        initOnCreateAdapter()

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        filmImage!!.setOnClickListener {
            imageRequestPermission()
        }

        filmTrailerVideo!!.setOnClickListener {
            filmTrailerVideo!!.start()
        }

        btnSelectTrailer!!.setOnClickListener {
            videoRequestPermission()
        }

        btnFilmUpload!!.setOnClickListener {
            uploadFilm()
        }

    }

    private fun imageRequestPermission() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                selectFilmImage()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(this@SellerUploadFilmActivity,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .check();

    }

    private fun videoRequestPermission() {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                selectFilmTrailerVideo()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(this@SellerUploadFilmActivity,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .check();

    }

    private fun selectFilmImage() {
        val kwikPicker = KwikPicker.Builder(this@SellerUploadFilmActivity,
            imageProvider = { imageView, uri ->
                Glide.with(this@SellerUploadFilmActivity)//Any image provider here!
                    .load(uri)
                    .into(imageView)
            },
            onImageSelectedListener = { uri: Uri ->
//                filmImage!!.visibility = View.VISIBLE
                val bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                filmImage!!.setImageBitmap(bitmap)
                imageUri = uri
            },
            peekHeight = 1200)
            .create(this@SellerUploadFilmActivity)
        kwikPicker.show(supportFragmentManager)
    }

    private fun selectFilmTrailerVideo() {
        val i = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(i, SELECT_VIDEO)
    }

    private fun uploadFilm() {

        val fileName = UUID.randomUUID().toString() + ".jpg"
        storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
        Log.e("ppp", imageUri.toString())
        storageReference!!.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                    val imageUrl = it.toString()
                }
            }
            .addOnFailureListener { e ->
//                print(e.message)
            }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_VIDEO && resultCode == Activity.RESULT_OK) {
            selectedVideoPath = getPath(data?.data!!)
            filmTrailerVideo!!.setVideoURI(Uri.parse(selectedVideoPath))
        }

    }

    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = managedQuery(uri, projection, null, null, null)
        return if (cursor != null) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } else null
    }

    private fun initViewComponent() {

        filmImage = findViewById(R.id.ivFilmImage)
        filmTrailerVideo = findViewById(R.id.vvVideoUpload)

        etFilmName = findViewById(R.id.etFilmName)
        etFilmDescription = findViewById(R.id.etFilmDescription)
        etFilmRentPrice = findViewById(R.id.etFilmRentPrice)
        etFilmBuyPrice = findViewById(R.id.etFilmPrice)
        etFilmDuration = findViewById(R.id.etFilmDuration)

        spnCategory = findViewById(R.id.spnCategory)
        spnCountry = findViewById(R.id.spnCountry)
        spnFilmLong = findViewById(R.id.spnFilmLong)

        btnFilmUpload = findViewById(R.id.btnFilmUpload)
        btnSelectTrailer = findViewById(R.id.btnSelectTrailer)

        actionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

    }

    private fun initOnCreateAdapter() {
        val list = arrayListOf("1", "2", "3", "4")
        categoryAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
        spnCategory!!.adapter = categoryAdapter
    }

}
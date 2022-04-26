package com.example.a14vfilm.sellerActivity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import java.util.*
import kotlin.collections.HashMap


class SellerUploadFilmActivity : AppCompatActivity() {

    /*constant code value to pick video*/
    private val VIDEO_PICK_GALLERY_CODE = 100
    private val VIDEO_PICK_CAMERA_CODE = 101
    private val FIREBASE_DATABASE_URL =
        "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val FIREBASE_STORAGE_URL = "gs://vfilm-83cf4.appspot.com"

    /*constant to request camera permission to record video from camera*/
    private val CAMERA_REQUEST_CODE = 102

    /*Array for camera request permissions*/
    private lateinit var cameraPermissions: Array<String>

    /*Define variable for component of one video upload*/
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

    private lateinit var actionBar: ActionBar
    private var imageUri: Uri? = null
    private var videoUri: Uri? = null

    /*Define variable for Firebase upload videos to Storage and Realtime*/
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var mAuth: FirebaseAuth? = null

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_upload_film)

        // Init while running application
        initViewComponent()
        initOnCreateAdapter()

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()


        //Handle click to ImageView (select film representatives image)
        filmImage!!.setOnClickListener {
            imageRequestPermission()
        }

        //Handle click to start Video Trailer
        filmTrailerVideo!!.setOnClickListener {
            filmTrailerVideo!!.resume()
        }

        //Handle click to select Video Trailer
        btnSelectTrailer!!.setOnClickListener {
            videoPickDialog()
        }

        //Handle click to upload Film Detail to Realtime and Storage Database
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

    /*=========================Video trailer picker====================*/

    /*set Video to Video Video by videoUri */
    private fun setVideoToVideoView() {
        /*video play controller*/
        val mediaController = MediaController(this)

        /*set media controller for video*/
        mediaController.setAnchorView(filmTrailerVideo)
        filmTrailerVideo!!.setMediaController(mediaController)
        filmTrailerVideo!!.setVideoURI(videoUri)
        filmTrailerVideo!!.requestFocus()
        filmTrailerVideo!!.setOnPreparedListener {
            filmTrailerVideo!!.pause()
        }

    }

    private fun videoPickDialog() {
        /*options to display in dialog*/
        val options = arrayOf<String>("Camera", "Gallery")

        /*alert dialog to pick method*/
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Chọn video từ")
            .setItems(options) { dialogInterface, i ->
                //Pick up by Camera
                if (i == 0) {
                    //camera clicked
                    if (!checkCameraPermission()) {
                        /*Camera isn't permission, get request permission*/
                        cameraRequestPermission()
                    } else {
                        /*Permission to access camera, so turn on pick camera mode*/
                        videoPickCamera()
                    }
                } else {
                    //gallery clicked. Turn on gallery video picker
                    videoPickGallery()
                }
            }.show()
    }

    private fun videoPickGallery() {
        /*Create new intent to pick up video trailer*/
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT

        /*Start intent for result to get value return*/
        startActivityForResult(
            Intent.createChooser(intent, "Chọn video"),
            VIDEO_PICK_GALLERY_CODE
        )
    }

    private fun videoPickCamera() {
        /*Create new intent to pick camera*/
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        startActivityForResult(intent, VIDEO_PICK_CAMERA_CODE)
    }

    /* ===============================Camera permission check========================= */
    private fun cameraRequestPermission() {
        ActivityCompat.requestPermissions(
            this,
            cameraPermissions,
            CAMERA_REQUEST_CODE
        )
    }

    private fun checkCameraPermission(): Boolean {
        /*check if camera permissions i.e, camera and storage is allowed or not*/
        val result = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val result2 = ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        /*return to value true/false*/
        return result && result2
    }

    /*===========================================*/

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

    /*Upload film and its information to firebase */
    private fun uploadFilm() {

        /*auto generate film id for this film by UUID*/
        val filmID = UUID.randomUUID().toString()
        if(checkEmptyFilmInputField()) {
                /*notify to user fill all input*/
                Toast.makeText(this, "Please fill all film information!", Toast.LENGTH_LONG).show()
                return;
        }else if(videoUri == null ) {
                /*notify Trailer Video is not selected*/
                Toast.makeText(this, "Pick film trailer!", Toast.LENGTH_LONG).show()
                return;
        }
        else if(imageUri == null){
                /*notify Film Image is not selected*/
                Toast.makeText(this, "Pick film image!", Toast.LENGTH_LONG).show()
                return;

        }

        /*Everything is OK
            Save data to firebase*/
        uploadFilmToFirebase(filmID)

    }

    /*upload data to firebase*/
    private fun uploadFilmToFirebase(filmID: String) {

        val timestamp = "" + System.currentTimeMillis()
        val videoFileAndPathName = "Videos/video_$timestamp"
        val imageFileAndPathName = "Images/image_$timestamp"


        Log.e("Image Uri", imageUri.toString())
        Log.e("Video Uri", videoUri.toString())

        storageReference = FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).getReference(videoFileAndPathName)
        storageReference!!.putFile(videoUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // uploaded, get video url of uploaded trailer video
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val downloadUri = uriTask.result

                /*uploaded trailer successfully*/
                if (uriTask.isSuccessful) {
                    /*Successfully upload video*/
                    Log.e("Film ID: ", filmID)

                    /*Upload Image*/
                    val stReference = FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).getReference(imageFileAndPathName)
                    stReference!!.putFile(imageUri!!)
                        .addOnSuccessListener { taskSnapshot ->
                            /*waiting uploading and get uri of image uploaded to firebase */
                            val imageUriTask = taskSnapshot.storage.downloadUrl
                            while(!imageUriTask.isSuccessful);
                            val imageUri = imageUriTask.result

                            /*upload success*/
                            if (imageUriTask.isSuccessful) {

                                /*upload information and add video*/
                                val hashMap = HashMap<String, Any>()
                                hashMap["id"] = filmID
                                hashMap["name"] = "${etFilmName!!.text.trim()}"
                                hashMap["description"] = "${etFilmDescription!!.text.trim()}"
                                hashMap["length"] = "${etFilmDuration!!.text.trim()}"
                                hashMap["seller"] = ""
                                hashMap["image"] = "$imageUri"
                                hashMap["trailer"] = "$downloadUri"
                                hashMap["country"] = spnCountry!!.selectedItem.toString()
//
//                                val film = Film(filmID,"","${etFilmName!!.text.trim()}","${etFilmDescription!!.text.trim()}",
//                                    "0".toFloat(), etFilmDuration!!.text.toString().toInt(), spnCountry!!.selectedItem.toString(), Date(), etFilmRentPrice.toString().toInt(),
//                                    "19".toInt(), Date(), "$imageUri", "$downloadUri", arrayListOf("Tâm lý", "Kinh dị"), 1)
                                /*accessing to Firebase Database -> Model film*/
                                val dbReference =
                                    FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
                                        .getReference("film")
                                dbReference
                                    .child(filmID)
                                    .setValue(hashMap)
                                    .addOnSuccessListener {
                                        progressDialog.dismiss()
                                        Toast.makeText(this,
                                            "Succesfully Uploading to Firebase",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        progressDialog.dismiss()
                                        Toast.makeText(this,
                                            "Failure Uploading to Firebase",
                                            Toast.LENGTH_SHORT).show()
                                    }

                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("ppppps", e.message.toString())
                        }

                }
            }
            .addOnFailureListener { e ->
                /*failed uploading*/
                progressDialog.dismiss()
                Toast.makeText(this, "${e.message}", Toast.LENGTH_LONG).show()
            }

    }


    /*Check if any input field empty*/
    private fun checkEmptyFilmInputField(): Boolean {
        return TextUtils.isEmpty(etFilmName!!.text.trim()) || TextUtils.isEmpty(etFilmDescription!!.text.trim())
                || TextUtils.isEmpty(etFilmBuyPrice!!.text.trim()) || TextUtils.isEmpty(
            etFilmDuration!!.text.trim())
                || TextUtils.isEmpty(etFilmRentPrice!!.text.trim())
    }

    private fun initViewComponent() {

        // Init view component
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


        // Init actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Đăng sản phẩm"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // Init camera permission array
        cameraPermissions = arrayOf(android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        etFilmName!!.requestFocus()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Vui lòng đợi!")
        progressDialog.setMessage("")
        progressDialog.setCanceledOnTouchOutside(false)
    }

    private fun initOnCreateAdapter() {
        val list = arrayListOf("1", "2", "3", "4")
        categoryAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
        spnCategory!!.adapter = categoryAdapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    /*Handle video pick result*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {

        /**/
        when (requestCode) {
            CAMERA_REQUEST_CODE ->
                if (grantResults.size > 0) {
                    //Check if permissions allowed or denied
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted) {
                        // Both Camera and Storage is allowed
                        videoPickCamera()
                    } else {
                        // One of them or both is denied, Notify
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    }

                }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            /*Video is picked from camera or gallery*/
            if (requestCode == VIDEO_PICK_CAMERA_CODE) {

                /*picking video from camera*/
                videoUri = data?.data!!
                setVideoToVideoView()
            } else if (requestCode == VIDEO_PICK_GALLERY_CODE) {
                /*picking video from gallery*/
                videoUri = data?.data!!
                setVideoToVideoView()
            }

        } else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        }


    }


}
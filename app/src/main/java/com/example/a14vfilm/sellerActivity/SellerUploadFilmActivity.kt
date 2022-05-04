package com.example.a14vfilm.sellerActivity

import android.Manifest
import android.annotation.SuppressLint
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
import com.example.a14vfilm.models.Genre
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SellerUploadFilmActivity : AppCompatActivity() {

    /*constant code value to pick video*/
    private val TRAILER_PICK_GALLERY_CODE = 100
    private val TRAILER_PICK_CAMERA_CODE = 101
    private val VIDEO_PICK_GALLERY_CODE = 102
    private val VIDEO_PICK_CAMERA_CODE = 103
    private val IMAGE_PICK_GALLERY_CODE = 104
    private val IMAGE_PICK_CAMERA_CODE = 105

    /*constant url of accessing Firebase*/
    private val FIREBASE_DATABASE_URL =
        "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
    private val FIREBASE_STORAGE_URL = "gs://vfilm-83cf4.appspot.com"

    /*constant to request camera permission to record video from camera*/
    private val VIDEO_CAMERA_REQUEST_CODE = 106
    private val IMAGE_CAMERA_REQUEST_CODE = 107


    /*Array for camera request permissions*/
    private lateinit var cameraPermissions: Array<String>

    /*Define variable for component of one video upload*/
    private var filmImage: ImageView? = null
    private var filmTrailerVideo: VideoView? = null
    private var filmFullVideo: VideoView? = null

    private var tvSelectCountry: TextView? = null
    private var tvSelectGenre: TextView? = null

    private var etFilmName: EditText? = null
    private var etFilmDescription: EditText? = null
    private var etFilmRentPrice: EditText? = null
//    private var etFilmQuantity: EditText? = null
    private var etFilmDuration: EditText? = null

    private var btnFilmUpload: Button? = null
    private var btnSelectTrailer: Button? = null
    private var btnSelectFull: Button? = null

    private lateinit var actionBar: ActionBar
    private var imageUri: Uri? = null
    private var trailerUri: Uri? = null
    private var videoUri: Uri? = null
    private var isTrailerFilm: Boolean = true

    /*Define variable for Firebase upload videos to Storage and Realtime*/
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private var mAuth: FirebaseAuth? = null

    private lateinit var progressDialog: ProgressDialog

    /*Define array stored genre value*/
    private var genreChoiceList = ArrayList<String>()

    /*media control to play video*/
    private var trailerMediaController: MediaController? = null
    private var fullMediaController: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_upload_film)

        // Init while running application
        initViewComponent()

        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()


        //Handle click to ImageView (select film representatives image)
        filmImage!!.setOnClickListener {
//            imageRequestPermission()
            imagePickDialog()
        }

        //Handle click to start Video Trailer
        filmTrailerVideo!!.setOnClickListener {
            filmTrailerVideo!!.resume()
            filmFullVideo!!.pause()
        }

        /*Handle click to resume the full video*/
        filmFullVideo!!.setOnClickListener {
            filmFullVideo!!.resume()
            filmTrailerVideo!!.pause()
        }

        //Handle click to select Video Trailer
        btnSelectTrailer!!.setOnClickListener {
            isTrailerFilm = true
            videoPickDialog(isTrailerFilm)
        }

        //Handle click to select Video Full Film
        btnSelectFull!!.setOnClickListener {
            isTrailerFilm = false
            videoPickDialog(isTrailerFilm)
        }

        //Handle click to upload Film Detail to Realtime and Storage Database
        btnFilmUpload!!.setOnClickListener {
            uploadFilm()
        }

    }

    /*=========================Video picker====================*/

    /*set Video to Video View by Uri */
    private fun setVideoToVideoView(view: VideoView?, uri: Uri?) {
        /*set media controller for video*/
        view!!.setVideoURI(uri!!)
        view.requestFocus()
        view.setOnPreparedListener {
            view.pause()
        }

    }

    private fun videoPickDialog(isTrailer: Boolean) {
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
                        videoPickCamera(isTrailer)
                    }
                } else {
                    //gallery clicked. Turn on gallery video picker
                    videoPickGallery(isTrailer)
                }
            }.show()
    }

    private fun videoPickGallery(isTrailer: Boolean) {
        /*Create new intent to pick up video trailer*/
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT

        val resultValue = if (isTrailer) TRAILER_PICK_GALLERY_CODE else VIDEO_PICK_GALLERY_CODE

        /*Start intent for result to get value return*/
        startActivityForResult(
            Intent.createChooser(intent, "Chọn video"),
            resultValue
        )
    }

    private fun videoPickCamera(isTrailer: Boolean) {
        /*Create new intent to pick camera*/
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        val resultValue = if (isTrailer) TRAILER_PICK_CAMERA_CODE else VIDEO_PICK_CAMERA_CODE
        startActivityForResult(intent, resultValue)
    }

    /*=====================================Image picker====================================*/
    /*set Image to Image View by Uri*/
    private fun setImageToImageView(uri: Uri?) {
        Picasso.get().load(imageUri).resize(200, 200).into(filmImage)
    }

    private fun imagePickDialog() {
        /*options to display in dialog*/
        val options = arrayOf<String>( /*"Camera", */ "Gallery")

        /*alert dialog to pick method*/
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Chọn video từ")
            .setItems(options) { dialogInterface, i ->
                //Pick up by Camera
//                if (i == 0) {
//                    //camera clicked
//                    if (!checkCameraPermission()) {
//                        /*Camera isn't permission, get request permission*/
//                        imageCameraRequestPermission()
//                    } else {
//                        /*Permission to access camera, so turn on pick camera mode*/
//                        imagePickCamera()
//                    }
//                } else {
                if ( i == 0){
                    //gallery clicked. Turn on gallery video picker
                    imagePickGallery()
                }
            }.show()
    }

    private fun imagePickGallery() {
        /*Create new intent to pick up video trailer*/
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        /*Start intent for result to get value return*/
        startActivityForResult(
            Intent.createChooser(intent, "Chọn hình ảnh"),
            IMAGE_PICK_GALLERY_CODE
        )
    }

    private fun imagePickCamera() {
        /*Create new intent to pick camera*/
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE)
    }

    /* ===============================Camera permission check========================= */

    private fun imageCameraRequestPermission() {
        ActivityCompat.requestPermissions(
            this,
            cameraPermissions,
            IMAGE_CAMERA_REQUEST_CODE
        )
    }

    private fun cameraRequestPermission() {
        ActivityCompat.requestPermissions(
            this,
            cameraPermissions,
            VIDEO_CAMERA_REQUEST_CODE
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

    /*Upload film and its information to firebase */
    private fun uploadFilm() {

        /*auto generate film id for this film by UUID*/
        val filmID = UUID.randomUUID().toString()
        if (checkEmptyFilmInputField()) {
            /*notify to user fill all input*/
            Toast.makeText(this, "Please fill all film information!", Toast.LENGTH_LONG).show()
            return;
        } else if (trailerUri == null) {
            /*notify Trailer Video is not selected*/
            Toast.makeText(this, "Pick film trailer!", Toast.LENGTH_LONG).show()
            return;
        } else if (videoUri == null) {
            /*notify Full Video is not selected*/
            Toast.makeText(this, "Pick film full video!", Toast.LENGTH_LONG).show()
            return;
        } else if (imageUri == null) {
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
        val trailerFileAndPathName = "Videos/trailer_$timestamp"
        val videoFileAndPathName = "Videos/video_$timestamp"
        val imageFileAndPathName = "Images/image_$timestamp"

        /*upload full video to Firebase first*/
        storageReference =
            FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).getReference(videoFileAndPathName)
        storageReference!!.putFile(videoUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // uploaded full Video -> get url of uploaded full video
                val videoUriTask = taskSnapshot.storage.downloadUrl
                while (!videoUriTask.isSuccessful);

                /*uploaded full video succesfully*/
                if (videoUriTask.isSuccessful) {
                    /*store url to videoUri to save to film Firebase Database*/
                    videoUri = videoUriTask.result
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }

        /*upload trailer and film detail*/
        storageReference =
            FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).getReference(trailerFileAndPathName)
        storageReference!!.putFile(trailerUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // uploaded, get video url of uploaded trailer video
                val trailerUriTask = taskSnapshot.storage.downloadUrl
                while (!trailerUriTask.isSuccessful);
                val trailerDownloadUri = trailerUriTask.result

                /*uploaded trailer successfully*/
                if (trailerUriTask.isSuccessful) {
                    /*Successfully upload trailer video*/

                    /*Upload Image*/
                    val stReference = FirebaseStorage.getInstance(FIREBASE_STORAGE_URL)
                        .getReference(imageFileAndPathName)
                    stReference!!.putFile(imageUri!!)
                        .addOnSuccessListener { taskSnapshot ->
                            /*waiting uploading and get uri of image uploaded to firebase */
                            val imageUriTask = taskSnapshot.storage.downloadUrl
                            while (!imageUriTask.isSuccessful);
                            val imageUri = imageUriTask.result

                            /*upload success*/
                            if (imageUriTask.isSuccessful) {

                                /*upload information and add video*/
                                val hashMap = HashMap<String, Any?>() /*hashmap contains value of film upload*/
                                hashMap["id"] = filmID
                                hashMap["name"] = "${etFilmName!!.text.trim()}"
                                hashMap["description"] = "${etFilmDescription!!.text.trim()}"
                                hashMap["length"] = etFilmDuration!!.text.trim().toString().toInt()
                                hashMap["seller"] = UserLogin.info!!.id
                                hashMap["image"] = "$imageUri"
                                hashMap["trailer"] = "$trailerDownloadUri"
                                hashMap["video"] = "$videoUri"
                                hashMap["datePublished"] = Date()
                                hashMap["dateUpdated"] = Date(0,0,0)
                                hashMap["price"] = etFilmRentPrice!!.text.trim().toString().toInt()
                                hashMap["rate"] = 0.0f
                                hashMap["rateTime"] = 0
                                hashMap["genre"] = genreChoiceList
                                hashMap["country"] = "${tvSelectCountry!!.text.trim()}"
                                hashMap["status"] = false


//                                val film = Film(filmID,"","${etFilmName!!.text.trim()}","${etFilmDescription!!.text.trim()}", 0F,
//                                    etFilmDuration!!.text.toString().toInt(), tvSelectCountry!!.text.toString(), Date(), etFilmRentPrice.toString().toInt(),
//                                    etFilmQuantity!!.text.toString().toInt(), Date(""), "$imageUri", "$trailerUri", genreChoiceList, 0)

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
                                        finish();
                                        startActivity(intent);
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
//                || TextUtils.isEmpty(etFilmQuantity!!.text.trim())
                || TextUtils.isEmpty(etFilmDuration!!.text.trim())
                || TextUtils.isEmpty(etFilmRentPrice!!.text.trim()) || genreChoiceList.size == 0 || tvSelectCountry!!.text.equals(
            "Chọn quốc gia")
    }

    /*config variable with component of xml (SellerFilmUploadActivity layout)*/
    private fun initViewComponent() {

        // Init view component
        filmImage = findViewById(R.id.ivFilmImage)
        filmTrailerVideo = findViewById(R.id.vvVideoUpload)
        filmFullVideo = findViewById(R.id.vvFullVideoUpload)

        tvSelectCountry = findViewById(R.id.tvCountry)
        tvSelectGenre = findViewById(R.id.tvGenre)

        etFilmName = findViewById(R.id.etFilmName)
        etFilmDescription = findViewById(R.id.etFilmDescription)
        etFilmRentPrice = findViewById(R.id.etFilmRentPrice)
//        etFilmQuantity = findViewById(R.id.etFilmQuantity)
        etFilmDuration = findViewById(R.id.etFilmDuration)

        btnFilmUpload = findViewById(R.id.btnFilmUpload)
        btnSelectTrailer = findViewById(R.id.btnSelectTrailer)
        btnSelectFull = findViewById(R.id.btnSelectFullVideo)

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

        // Init TextView country selection
        InitCountrySelection()

        // Init TextView genre multiple selection
        InitGenreMultipleSelection()

        // Init mediaController


        trailerMediaController = MediaController(this)
        trailerMediaController!!.setAnchorView(filmTrailerVideo)
        filmTrailerVideo!!.setMediaController(trailerMediaController)

        fullMediaController = MediaController(this)
        fullMediaController!!.setAnchorView(filmFullVideo)
        filmFullVideo!!.setMediaController(fullMediaController)

    }

    /*Accessing to 14VFilm Firebase Database to get all Genre*/
    @SuppressLint("SetTextI18n")
    private fun InitGenreMultipleSelection() {

        /*get genre from Firebase Database and save to Array*/
        val genreFirebaseDatabase =
            FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).getReference("genre")
        genreFirebaseDatabase.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val genreList: ArrayList<String> = ArrayList()
                for (singleSnapshot in dataSnapshot.children) {
//                        val id = singleSnapshot.child("id").getValue<String>()
//                        val image = singleSnapshot.child("image").getValue<String>()
                    val name = singleSnapshot.child("name").getValue<String>()
//                        genreList.add(Genre(id="$id", image = "$image", name="$name"))
                    genreList.add(name.toString())

                }

                /*init default checked items to multiple choice select dialog*/
                val genreArray = genreList.toTypedArray()
                val checkedGenreArray = BooleanArray(genreArray.size)
                checkedGenreArray.fill(false)

                /*show genre multichoice dialog when select*/
                tvSelectGenre!!.setOnClickListener {

                    /*create DialogBuilder for user select genre*/
                    val genreDialogBuilder = AlertDialog.Builder(this@SellerUploadFilmActivity)
                    genreDialogBuilder.setTitle("Chọn thể loại phim")
                    genreDialogBuilder.setMultiChoiceItems(genreArray,
                        checkedGenreArray) { dialogInterface, i, isChecked ->
                        checkedGenreArray[i] = isChecked
                    }.setPositiveButton("Chọn") { dialog, which ->
                        genreChoiceList.clear()
                        /*count how many choice was selected and save choice to list genre choice to push it upto Firebase later */
                        var count = 0
                        for (i in checkedGenreArray.indices) {
                            val checked = checkedGenreArray[i]
                            if (checked) {
                                count++
                                genreChoiceList.add(genreArray[i])
                            }
                        }

                        if (count != 0)
                            tvSelectGenre!!.text = "Chọn thể loại (${count})"
                        else
                            tvSelectGenre!!.text = "Chọn thể loại"
                    }

                    /*show dialog*/
                    genreDialogBuilder.create().show()
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("pppp", "loadPost:onCancelled", databaseError.toException())
            }

        })



    }

    /*Init clicked country textview to select country*/
    private fun InitCountrySelection() {
        tvSelectCountry!!.setOnClickListener {

            /*generate a country ArrayList*/
            val genreArray = arrayOf("Việt Nam",
                "Âu - Mỹ",
                "Hàn Quốc",
                "Nhật Bản",
                "Thái Lan",
                "Trung Quốc",
                "Ấn Độ",
                "Đài Loan",
                "Quốc gia khác")

            /*create DialogBuilder for user select genre*/
            val genreDialogBuilder = AlertDialog.Builder(this)
            genreDialogBuilder.setTitle("Chọn thể loại phim")
            genreDialogBuilder.setItems(genreArray) { dialogInterface, i: Int ->
                /*set value to TextView*/
                tvSelectCountry!!.text = genreArray[i].toString()
            }

            genreDialogBuilder.show()
        }
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


        /*Check permission trailer camera*/
       when (requestCode){
           VIDEO_CAMERA_REQUEST_CODE -> {
               /*Check permission full camera*/
               if (grantResults.size > 0) {
                   //Check if permissions allowed or denied
                   val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                   val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                   if (cameraAccepted && storageAccepted) {
                       // Both Camera and Storage is allowed
                       videoPickCamera(isTrailerFilm)
                   } else {
                       // One of them or both is denied, Notify
                       Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                   }

               }
           }
           IMAGE_CAMERA_REQUEST_CODE -> {
               /*Check permission full camera*/
               if (grantResults.size > 0) {
                   //Check if permissions allowed or denied
                   val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                   val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                   if (cameraAccepted && storageAccepted) {
                       // Both Camera and Storage is allowed
                    imagePickCamera()
                   } else {
                       // One of them or both is denied, Notify
                       Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                   }

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
            if (requestCode == TRAILER_PICK_CAMERA_CODE) {

                /*picking video from camera*/
                trailerUri = data?.data!!
                setVideoToVideoView(filmTrailerVideo, trailerUri)

            } else if (requestCode == TRAILER_PICK_GALLERY_CODE) {

                /*picking video from gallery*/
                trailerUri = data?.data!!
                setVideoToVideoView(filmTrailerVideo, trailerUri)

            } else if (requestCode == VIDEO_PICK_CAMERA_CODE) {

                /*picking video from camera*/
                videoUri = data?.data!!
                setVideoToVideoView(filmFullVideo, videoUri)

            } else if (requestCode == VIDEO_PICK_GALLERY_CODE) {

                /*picking video from gallery*/
                videoUri = data?.data!!
                setVideoToVideoView(filmFullVideo, videoUri)

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                /*picking video from gallery*/
                imageUri = data?.data!!
                setImageToImageView(imageUri)

            } else if (requestCode == IMAGE_PICK_GALLERY_CODE) {

                /*picking video from gallery*/
                imageUri = data?.data!!
                setImageToImageView(imageUri)

            }

        } else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        }

    }

}



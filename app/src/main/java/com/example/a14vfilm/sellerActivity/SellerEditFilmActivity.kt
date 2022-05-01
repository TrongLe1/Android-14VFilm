package com.example.a14vfilm.sellerActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SellerEditFilmActivity : AppCompatActivity() {
    
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
    
    /*init variable from view of activity*/
    private var etEditName: EditText? = null
    private var etEditDescription: EditText? = null
    private var tvEditGenre: TextView? = null
    private var tvEditCountry: TextView? = null
    private var etEditPrice: EditText? = null
    private var etEditDuration: EditText? = null

    private var ivEditImage: ImageView? = null
    private var vvFilmTrailer: VideoView? = null
    private var vvFilmVideo: VideoView? = null

    private var btnSelectTrailer: Button? = null
    private var btnSelectVideo: Button? = null
    private var btnSaveChange: Button? = null

    private var imageUri: Uri? = null
    private var trailerUri: Uri? = null
    private var videoUri: Uri? = null
    private var isTrailerFilm: Boolean = true
    
    /*init variable stored detail film*/
    private var filmDetail: Film? = null

    /*Define variable for Firebase upload videos to Storage and Realtime*/
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var mAuth: FirebaseAuth? = null

    private lateinit var progressDialog: ProgressDialog
    
    /*media controller to play video*/
    private var trailerMediaController: MediaController? = null
    private var videoMediaController: MediaController? = null

    /*Define array stored genre value*/
    private var genreChoiceList = ArrayList<String>()

    /*define variable check if image,trailer, video have changed?*/
    private var trailerChange: Boolean = false
    private var videoChange: Boolean = false
    private var imageChange: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_edit_film)

        /*Init*/
        initViewComponent() 
        
        /*init firebase database*/
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.getReference()
        
        /*get data from previous activity*/
        getDataIntent()
        displayDataToViewComponent()

        /*handling button*/
        handleButtonClick()

    }

    /*handling action of button component*/
    private fun handleButtonClick() {

        /*handling click to save change of detail film*/
        btnSaveChange!!.setOnClickListener {
            saveFilmDetailChange()
        }

        /*handling click to choose new trailer*/
        btnSelectTrailer!!.setOnClickListener {
            isTrailerFilm = true
            videoPickDialog(isTrailerFilm)
        }

        /*handling click to choose new video*/
        btnSelectVideo!!.setOnClickListener {
            isTrailerFilm = false
            videoPickDialog(isTrailerFilm)
        }

        /*handling click to edit image*/
        ivEditImage!!.setOnClickListener{
            imagePickDialog()
        }

        //Handle click to start Video Trailer
        vvFilmTrailer!!.setOnClickListener {
            vvFilmTrailer!!.resume()
            vvFilmVideo!!.pause()
        }

        /*Handle click to resume the full video*/
        vvFilmVideo!!.setOnClickListener {
            vvFilmVideo!!.resume()
            vvFilmTrailer!!.pause()
        }

        
    }

    /*=========================================Update Film=======================================*/

    /*update film and its information to firebase */
    private fun saveFilmDetailChange() {

        /*check conditions of view component*/
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
        updateFilmToFirebase(filmDetail!!)

    }

    /*upload data to firebase*/
    private fun updateFilmToFirebase(film: Film) {

        /*timestamp when update film to firebase*/
        val timestamp = "" + System.currentTimeMillis()

        val videoCheck = updateNewVideoToFirebase(timestamp)
        val trailerCheck = updateNewTrailerToFirebase(timestamp)
        val imageCheck = updateNewImageToFirebase(timestamp)
        while (!videoCheck);
        while (!trailerCheck);
        while (!imageCheck);
        /*doneTask*/
        if(videoCheck && trailerCheck && imageCheck) {

            /*upload information and add video*/
            val hashMap = HashMap<String, Any>() /*hashmap contains value of film upload*/
            hashMap["id"] = filmDetail!!.id
            hashMap["name"] = "${etEditName!!.text.trim()}"
            hashMap["description"] = "${etEditDescription!!.text.trim()}"
            hashMap["length"] = etEditDuration!!.text.trim().toString().toInt()
            hashMap["seller"] = filmDetail!!.seller
            hashMap["image"] = "$imageUri"
            hashMap["trailer"] = "$trailerUri"
            hashMap["video"] = "$videoUri"
            hashMap["datePublished"] = filmDetail!!.datePublished
            hashMap["dateUpdated"] = filmDetail!!.dateUpdated
            hashMap["price"] = etEditPrice!!.text.trim().toString().toInt()
            hashMap["rate"] = filmDetail!!.rate
            hashMap["rateTime"] = filmDetail!!.rateTime
            hashMap["genre"] = genreChoiceList
            hashMap["country"] = "${tvEditCountry!!.text.trim()}"
            hashMap["status"] = filmDetail!!.status

            /*accessing to Firebase Database -> Model film*/
            val dbReference =
                FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL)
                    .getReference("film/${filmDetail!!.id}")
//                .child(filmDetail!!.id)
            dbReference
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

    private fun updateNewImageToFirebase(timestamp: String): Boolean {

        /*check if image changes*/
        if (imageChange) {

            /*new path for image*/
            val imageFileAndPathName = "Images/image_$timestamp"

            /*Upload Image*/
            val stReference = FirebaseStorage.getInstance(FIREBASE_STORAGE_URL)
                .getReference(imageFileAndPathName)
            val check = stReference!!.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    /*waiting uploading and get uri of image uploaded to firebase */
                    val imageUriTask = taskSnapshot.storage.downloadUrl
                    while (!imageUriTask.isSuccessful);

                    /*upload success*/
                    if (imageUriTask.isSuccessful) {
                        imageUri = imageUriTask.result
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("ppppps", e.message.toString())
                }
            while(!check.isComplete);
        }
        return true
    }

    private fun updateNewTrailerToFirebase(timestamp: String): Boolean {
        /*check if video changes*/
        if(trailerChange) {
            /*new path for trailer*/
            val trailerFileAndPathName = "Videos/trailer_$timestamp"

            /*upload trailer and film detail*/
            storageReference =
                FirebaseStorage.getInstance(FIREBASE_STORAGE_URL)
                    .getReference(trailerFileAndPathName)
            val check = storageReference!!.putFile(trailerUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // uploaded, get video url of uploaded trailer video
                    val trailerUriTask = taskSnapshot.storage.downloadUrl
                    while (!trailerUriTask.isSuccessful);

                    /*uploaded trailer successfully*/
                    if (trailerUriTask.isSuccessful) {
                        /*Successfully upload trailer video*/

                        /*save new trailer Uri*/
                        trailerUri = trailerUriTask.result
                    }
                }
                .addOnFailureListener { e ->
                    /*failed uploading*/
                    progressDialog.dismiss()
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_LONG).show()
                }
            while(!check.isComplete);
        }
        return true
    }

    private fun updateNewVideoToFirebase(timestamp: String): Boolean {
        /*check if video changes*/
        if(videoChange){
            /*new path for video*/
            val videoFileAndPathName = "Videos/video_$timestamp"
            /*upload full video to Firebase first*/
            storageReference =
                FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).getReference(videoFileAndPathName)
            val check = storageReference!!.putFile(videoUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // uploaded full Video -> get url of uploaded full video
                    val videoUriTask = taskSnapshot.storage.downloadUrl
                    while (!videoUriTask.isSuccessful);

                    /*uploaded full video succesfully*/
                    if (videoUriTask.isSuccessful) {
                        /*store new url to videoUri to save to film Firebase Database*/
                        videoUri = videoUriTask.result
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                }
            while(!check.isComplete);
        }
        return true
    }

    /*Check if any input field empty*/
    private fun checkEmptyFilmInputField(): Boolean {
        return TextUtils.isEmpty(etEditName!!.text.trim()) || TextUtils.isEmpty(etEditDescription!!.text.trim())
//                || TextUtils.isEmpty(etFilmQuantity!!.text.trim())
                || TextUtils.isEmpty(etEditPrice!!.text.trim())
                || TextUtils.isEmpty(etEditDuration!!.text.trim()) || genreChoiceList.size == 0 || tvEditCountry!!.text.equals(
            "Chọn quốc gia")
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
        Picasso.get().load(imageUri).resize(200, 200).into(ivEditImage)
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
            }
            .setCancelable(true)
            .show()
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

    /*display film detail stored in film variable to view*/
    @SuppressLint("SetTextI18n")
    private fun displayDataToViewComponent() {

        etEditName!!.text = SpannableStringBuilder(filmDetail!!.name)
        etEditDescription!!.text = SpannableStringBuilder(filmDetail!!.description)
        etEditPrice!!.text = SpannableStringBuilder(filmDetail!!.price.toString())
        etEditDuration!!.text = SpannableStringBuilder(filmDetail!!.length.toString())

        tvEditCountry!!.text = filmDetail!!.country
        tvEditGenre!!.text = "Chọn thể loại (${filmDetail!!.genre.size})"

        genreChoiceList = filmDetail!!.genre

        Picasso.get().load(filmDetail!!.image).into(ivEditImage)

        vvFilmTrailer!!.setVideoPath(filmDetail!!.trailer)
        vvFilmTrailer!!.setOnPreparedListener {
            vvFilmTrailer!!.pause()
        }

        vvFilmVideo!!.setVideoPath(filmDetail!!.video)
        vvFilmVideo!!.setOnPreparedListener {
            vvFilmVideo!!.pause()
        }

        imageUri = filmDetail!!.image.toUri()
        videoUri = filmDetail!!.video.toUri()
        trailerUri = filmDetail!!.trailer.toUri()

    }


    /*init view component to variable*/
    private fun initViewComponent() {

        /*set view to variable*/
        etEditName = findViewById(R.id.etFilmEditDetailName)
        etEditDescription = findViewById(R.id.etFilmEditDetailDescription)
        etEditPrice = findViewById(R.id.etFilmEditDetailRentPrice)
        etEditDuration = findViewById(R.id.etFilmEditDetailDuration)

        tvEditCountry = findViewById(R.id.tvFilmEditDetailCountry)
        tvEditGenre = findViewById(R.id.tvFilmEditDetailGenre)

        ivEditImage = findViewById(R.id.ivFilmEditDetailImage)
        vvFilmTrailer =findViewById(R.id.vvVideoUpload)
        vvFilmVideo = findViewById(R.id.vvFullVideoUpload)

        btnSelectTrailer = findViewById(R.id.btnSelectTrailer)
        btnSelectVideo = findViewById(R.id.btnSelectFullVideo)
        btnSaveChange = findViewById(R.id.btnSaveChange)

        /*set up Media Controller*/
        trailerMediaController = MediaController(this)
        trailerMediaController!!.setAnchorView(vvFilmTrailer)
        vvFilmTrailer!!.setMediaController(trailerMediaController)

        videoMediaController = MediaController(this)
        videoMediaController!!.setAnchorView(vvFilmVideo)
        vvFilmVideo!!.setMediaController(videoMediaController)

        etEditName!!.requestFocus()
        /*Init progress dialog*/

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Vui lòng đợi!")
        progressDialog.setMessage("")
        progressDialog.setCanceledOnTouchOutside(false)

        // Init TextView country selection
        InitCountrySelection()

        // Init TextView genre multiple selection
        InitGenreMultipleSelection()

    }

    /*get data from previous activity*/
    private fun getDataIntent() {
        filmDetail = intent.getSerializableExtra("Film") as Film?
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
                for(idx in genreChoiceList.indices){
                    if(genreArray.contains(genreChoiceList[idx]))
                        checkedGenreArray[idx] = true
                }

                /*show genre multichoice dialog when select*/
                tvEditGenre!!.setOnClickListener {

                    /*create DialogBuilder for user select genre*/
                    val genreDialogBuilder = AlertDialog.Builder(this@SellerEditFilmActivity)
                    genreDialogBuilder.setTitle("Chọn thể loại phim")
                    genreDialogBuilder.setMultiChoiceItems(genreArray,
                        checkedGenreArray) { dialogInterface, i, isChecked ->
                        checkedGenreArray[i] = isChecked
                    }.setPositiveButton("Chọn") { dialog, which ->
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
                            tvEditGenre!!.text = "Chọn thể loại (${count})"
                        else
                            tvEditGenre!!.text = "Chọn thể loại"
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
        tvEditCountry!!.setOnClickListener {

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
                tvEditCountry!!.text = genreArray[i].toString()
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
                setVideoToVideoView(vvFilmTrailer, trailerUri)
                trailerChange = true

            } else if (requestCode == TRAILER_PICK_GALLERY_CODE) {

                /*picking video from gallery*/
                trailerUri = data?.data!!
                setVideoToVideoView(vvFilmTrailer, trailerUri)
                trailerChange = true

            } else if (requestCode == VIDEO_PICK_CAMERA_CODE) {

                /*picking video from camera*/
                videoUri = data?.data!!
                setVideoToVideoView(vvFilmVideo, videoUri)
                videoChange = true

            } else if (requestCode == VIDEO_PICK_GALLERY_CODE) {

                /*picking video from gallery*/
                videoUri = data?.data!!
                setVideoToVideoView(vvFilmVideo, videoUri)
                videoChange = true

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                /*picking video from gallery*/
                imageUri = data?.data!!
                setImageToImageView(imageUri)
                imageChange = true

            } else if (requestCode == IMAGE_PICK_GALLERY_CODE) {

                /*picking video from gallery*/
                imageUri = data?.data!!
                setImageToImageView(imageUri)
                imageChange =true

            }

        } else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        }

    }

}
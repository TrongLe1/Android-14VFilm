package com.example.a14vfilm.sellerActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.a14vfilm.R

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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller_upload_film)

        // Init
        initViewComponent()
        initOnCreateAdapter()


        filmImage!!.setOnClickListener{
            Toast.makeText(this,"Hello",2).show()
        }
        btnFilmUpload!!.setOnClickListener{
            Toast.makeText(this,"Hello",2).show()
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

    }

    private fun initOnCreateAdapter(){
        val list = arrayListOf("1", "2", "3","4")
        categoryAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
        spnCategory!!.adapter = categoryAdapter
    }

}
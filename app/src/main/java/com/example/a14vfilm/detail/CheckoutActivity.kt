package com.example.a14vfilm.detail

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class CheckoutActivity : AppCompatActivity() {

    var IVCheckout: ImageView? = null
    var TVCName: TextView? = null
    var TVCPrice: TextView? = null
    var TVCRentDate: TextView? = null
    var TVCEndDate: TextView? = null
    var TVCNameCus: TextView? = null
    var TVCAddress: TextView? = null
    var TVCPhone: TextView? = null
    var TVCTotal: TextView? = null
    var BTNCheckout: Button? = null
    var rentDate = Date()
    var endDate = Date()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        IVCheckout = findViewById(R.id.IVCheckout)
        TVCName = findViewById(R.id.TVCName)
        TVCPrice = findViewById(R.id.TVCPrice)
        TVCRentDate = findViewById(R.id.TVCRDDetail)
        TVCEndDate = findViewById(R.id.TVCEDDate)
        TVCNameCus = findViewById(R.id.TVNCDetail)
        TVCAddress = findViewById(R.id.TVCADetail)
        TVCPhone = findViewById(R.id.TVCPDetail)
        TVCTotal = findViewById(R.id.TVCTDetail)
        BTNCheckout = findViewById(R.id.BTNCheckout)

        val film = intent.getSerializableExtra("Film") as Film

        if (film.image != "")
            Picasso.get().load(film.image).resize(200, 200).into(IVCheckout)
        TVCName!!.text = film.name
        TVCPrice!!.text = film.price.toString() + " VNĐ"
        TVCRentDate!!.text = SimpleDateFormat("dd/MM/yyy").format(rentDate)
        BTNCheckout!!.isEnabled = false
        BTNCheckout!!.isClickable = false
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        TVCEndDate!!.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val temp = LocalDate.of(year, month + 1, dayOfMonth)
                endDate = Date.from(temp.atStartOfDay(ZoneId.systemDefault()).toInstant())
                TVCEndDate!!.text = SimpleDateFormat("dd/MM/yyy").format(endDate)
                var diff = endDate.time - rentDate.time
                diff /= 1000
                diff /= 60
                diff /= 60
                diff /= 24
                val price = film.price * (diff + 1)
                TVCTotal!!.text = price.toString() + " VNĐ"
                if (price > 0 && TVCNameCus!!.text != "" && TVCAddress!!.text != "" && TVCPhone!!.text != "") {
                    BTNCheckout!!.isEnabled = true
                    BTNCheckout!!.isClickable = true
                }
                else {
                    BTNCheckout!!.isEnabled = false
                    BTNCheckout!!.isClickable = false
                    TVCTotal!!.text = "0 VNĐ"
                }

            }, year, month, day)
            dpd.show()
        }
        TVCTotal!!.text = film.price.toString() + " VNĐ"


        supportActionBar!!.hide()
    }
}
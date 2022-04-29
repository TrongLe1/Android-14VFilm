package com.example.a14vfilm.detail

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.a14vfilm.BuildConfig
import com.example.a14vfilm.R
import com.example.a14vfilm.models.Film
import com.example.a14vfilm.models.Transaction
import com.example.a14vfilm.models.UserLogin
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PayPalButton
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.math.roundToInt

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
    var price: Long? = null
    var TVCPayment: TextView? = null
    var BTNPaypal: PayPalButton? = null



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val config = CheckoutConfig(
            application = application,
            clientId = "Aa9fI7vfrjtYJ9TCmB4F-cqbvPdtUCVCtHRMX22KAndwi5daQNLECf90cTzHUetmaLIQq8sycHcFTSAr",
            environment = Environment.SANDBOX,
            returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)

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
        TVCPayment = findViewById(R.id.TVCPayDetail)
        BTNPaypal = findViewById(R.id.BTNPaypal)

        //BTNCheckout!!.isEnabled = false
        BTNCheckout!!.isClickable = false
        BTNPaypal!!.visibility = View.GONE

        val film = intent.getSerializableExtra("Film") as Film

        if (film.image != "")
            Picasso.get().load(film.image).resize(200, 200).into(IVCheckout)
        TVCName!!.text = film.name
        val formatter = DecimalFormat("#,###")

        TVCPrice!!.text = formatter.format(film.price) + " VNĐ"
        TVCRentDate!!.text = SimpleDateFormat("dd/MM/yyy").format(rentDate)
        //BTNCheckout!!.isEnabled = false
        //BTNCheckout!!.isClickable = false
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
                price = film.price * (diff + 1)
                TVCTotal!!.text = formatter.format(price) + " VNĐ"
                if (price!! > 0 /*&& TVCNameCus!!.text != "" && TVCAddress!!.text != "" && TVCPhone!!.text != ""*/) {
                    //BTNCheckout!!.isEnabled = true
                    //BTNCheckout!!.isClickable = true
                    if (TVCPayment!!.text != "Chọn hình thức thanh toán")
                        BTNCheckout!!.visibility = View.GONE
                }
                else {
                    //BTNCheckout!!.isEnabled = false
                    //BTNCheckout!!.isClickable = false
                    TVCTotal!!.text = "0 VNĐ"
                }
            }, year, month, day)
            dpd.show()
        }
        TVCNameCus!!.text = UserLogin.info!!.name
        TVCPhone!!.text = UserLogin.info!!.phone
        TVCAddress!!.text = UserLogin.info!!.address
        TVCTotal!!.text = formatter.format(film.price) + " VNĐ"
        BTNCheckout!!.setOnClickListener {
            /*
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference("transaction")
            val trans = Transaction(ref.push().key!!, UserLogin.info!!.id, film.id, rentDate, endDate, price!!, -1F, true, "")
            ref.child(trans.id).setValue(trans)

            /*
            val sRef = FirebaseDatabase.getInstance(url).getReference("film")
            sRef.child(film.id).child("quantity").setValue(film.quantity - 1)

            */
            Toast.makeText(this, "Giao dịch thành công", Toast.LENGTH_SHORT).show()
            setResult(Activity.RESULT_OK)
            finish()

            */
        }

        TVCPayment!!.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(R.layout.dialog_payment)
            val RGPayment = bottomSheetDialog.findViewById<RadioGroup>(R.id.RGPayment)
            val BTNPayment = bottomSheetDialog.findViewById<Button>(R.id.BTNPayment)
            BTNPayment!!.setOnClickListener {
                val selected = RGPayment!!.checkedRadioButtonId
                val RBPayment = bottomSheetDialog.findViewById<RadioButton>(selected)
                bottomSheetDialog.dismiss()
                TVCPayment!!.text = RBPayment!!.text
                if (TVCPayment!!.text == "Paypal" && TVCEndDate!!.text != "Chọn ngày trả")
                    BTNCheckout!!.visibility = View.GONE
            }
            bottomSheetDialog.show()
        }

        BTNPaypal!!.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val amount = (price!! * 0.000044).roundToInt()
                val order =
                    Order(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = amount.toString())
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
                    val ref = FirebaseDatabase.getInstance(url).getReference("transaction")
                    val trans = Transaction(ref.push().key!!, UserLogin.info!!.id, film.id, rentDate, endDate, price!!, -1F, true, "")
                    ref.child(trans.id).setValue(trans)
                    Toast.makeText(this, "Giao dịch thành công", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()

                }
            }
        )
        supportActionBar!!.hide()
    }
}
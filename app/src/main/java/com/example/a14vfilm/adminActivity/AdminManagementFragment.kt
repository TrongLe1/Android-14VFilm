package com.example.a14vfilm.more

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.a14vfilm.MainActivity
import com.example.a14vfilm.R
import com.example.a14vfilm.adminActivity.VerifyFilmAdminActivity
import com.example.a14vfilm.adminActivity.ViewFilmsActivityAdmin
import com.example.a14vfilm.adminActivity.ViewGenreActivity
import com.example.a14vfilm.adminActivity.ViewUserActivity
import com.example.a14vfilm.models.UserLogin
import com.example.a14vfilm.sellerActivity.SellerUploadFilmActivity
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AdminManagementFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_management, container, false)

        view.findViewById<Button>(R.id.button1).setOnClickListener{
            val intent = Intent(context, ViewUserActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<Button>(R.id.button2).setOnClickListener{
            val intent = Intent(context, ViewFilmsActivityAdmin::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.button3).setOnClickListener{
            val intent = Intent(context, ViewGenreActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.button4).setOnClickListener{
            val intent = Intent(context, ViewGenreActivity::class.java)
            startActivity(intent)
        }
        view.findViewById<Button>(R.id.button5).setOnClickListener{
            val intent = Intent(context, VerifyFilmAdminActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminManagementFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}
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
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.example.a14vfilm.MainActivity
import com.example.a14vfilm.R
import com.example.a14vfilm.adminActivity.AdminHomeActivity
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MoreFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_more, container, false)
        val BTNLogin = view.findViewById<Button>(R.id.BTNLogin)
        val BTNLogout = view.findViewById<Button>(R.id.BTNLogout)
        val TVInfo = view.findViewById<TextView>(R.id.TVInfo)
        val TVFavourite = view.findViewById<TextView>(R.id.TVFavourite)
        val TVChangePass = view.findViewById<TextView>(R.id.TVChangePass)
        val IVAvatar = view.findViewById<ImageView>(R.id.IVAvatar)



        if (UserLogin.info == null) {
            TVInfo.isEnabled = false
            TVInfo.isClickable = false
            TVInfo.visibility = View.GONE
            TVFavourite.isEnabled = false
            TVFavourite.isClickable = false
            TVFavourite.visibility = View.GONE
            TVChangePass.isEnabled = false
            TVChangePass.isClickable = false
            TVChangePass.visibility = View.GONE
            BTNLogout.isEnabled = false
            BTNLogout.isClickable = false
            BTNLogout.visibility = View.GONE
        }
        else {
            if (UserLogin.info!!.password == "") {
                TVChangePass.isEnabled = false
                TVChangePass.isClickable = false
                TVChangePass.visibility = View.GONE
            }
            //if admin, hide favourite
            if (UserLogin.info!!.role == 2){
                TVFavourite!!.visibility = View.GONE
            }
            BTNLogin.text = "Xin ch??o, " + UserLogin.info!!.name
            BTNLogin.isClickable = false
            BTNLogin.isEnabled = false
            if (UserLogin.info!!.image != "")
                Picasso.get().load(UserLogin.info!!.image).resize(150, 150).into(IVAvatar)

        }

        BTNLogout.setOnClickListener {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            UserLogin.info = null

            val sharedPreference =  requireActivity().getSharedPreferences("UserLogin", MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.remove("user")
            editor.commit()


            /*
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.BNVMain)
            bottomNavigationView.setSelectedItemId(R.id.home)
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.FLMain, OrderFragment())
                commit()
            }
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.FLMain, HomeFragment())
                commit()
            }

            */
            requireActivity().finish()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
        }


        /*
        BTNLogin.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivityForResult(intent, 100)
        }

        */

        TVFavourite.setOnClickListener {
            val intent = Intent(requireActivity(), FavoriteActivity::class.java)
            startActivity(intent)
        }

        TVInfo.setOnClickListener {
            val intent = Intent(requireActivity(), InfoActivity::class.java)
            //startActivityForResult(intent, 101)
            startActivity(intent)
        }
        //Log.i("d", UserLogin.info.toString())
        TVChangePass.setOnClickListener {
            val intent = Intent(requireActivity(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*
        if (requestCode == 100) {
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.BNVMain)
            bottomNavigationView.selectedItemId = R.id.more
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.FLMain, OrderFragment())
                commit()
            }
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.FLMain, MoreFragment())
                commit()
            }
        }


        if (requestCode == 101) {
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.BNVMain)
            bottomNavigationView.selectedItemId = R.id.home
        }
        */
    }

}
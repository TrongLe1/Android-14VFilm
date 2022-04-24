package com.example.a14vfilm.order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.TransactionAdapter
import com.example.a14vfilm.home.HomeFragment
import com.example.a14vfilm.login.LoginActivity
import com.example.a14vfilm.models.Transaction
import com.example.a14vfilm.models.TransactionExtend
import com.example.a14vfilm.models.UserLogin
import com.example.a14vfilm.more.MoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OrderedFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var viewFragment: View? = null

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
        viewFragment = inflater.inflate(R.layout.fragment_ordered, container, false)
        val RVOrdered = viewFragment!!.findViewById<RecyclerView>(R.id.RVOrdered)
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        RVOrdered.layoutManager = layoutManager
        RVOrdered.addItemDecoration(LayoutMarginDecoration(1, 50))
        return viewFragment
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun getTransactionList() {
        val RVOrdered = viewFragment!!.findViewById<RecyclerView>(R.id.RVOrdered)
        if (UserLogin.info != null) {
            val transList = ArrayList<TransactionExtend>()
            val url = "https://vfilm-83cf4-default-rtdb.asia-southeast1.firebasedatabase.app/"
            val ref = FirebaseDatabase.getInstance(url).getReference("transaction")
            val query = ref.orderByChild("user").equalTo(UserLogin.info!!.id)
            query.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (singleSnapshot in snapshot.children) {
                        val id = singleSnapshot.child("id").getValue<String>()
                        val user = singleSnapshot.child("user").getValue<String>()
                        val rentDate = singleSnapshot.child("rentDate").getValue<Date>()
                        val total = singleSnapshot.child("total").getValue<Long>()
                        val rate = singleSnapshot.child("rate").getValue<Float>()
                        val expired = singleSnapshot.child("expired").getValue<Date>()
                        val type = singleSnapshot.child("type").getValue<Boolean>()
                        val film = singleSnapshot.child("film").getValue<String>()
                        val comment = singleSnapshot.child("comment").getValue<String>()
                        val trans = Transaction(id!!, user!!, film!!, rentDate!!, expired!!, total!!, rate!!, type!!, comment!!)
                        if (expired > Date() && type) {
                            val sRef = FirebaseDatabase.getInstance(url).getReference("film")
                            val sQuery = sRef.orderByChild("id").equalTo(film)
                            sQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (singleSnapshot in snapshot.children) {
                                        val name = singleSnapshot.child("name").getValue<String>()
                                        val image = singleSnapshot.child("image").getValue<String>()
                                        transList.add(TransactionExtend(trans, name!!, image!!))
                                    }
                                    RVOrdered.adapter = TransactionAdapter(transList, "ordered")
                                }
                                override fun onCancelled(error: DatabaseError) {}
                            })
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
        else {
            RVOrdered.adapter = TransactionAdapter(ArrayList(), "ordered")
        }
    }

    override fun onResume() {
        super.onResume()
        getTransactionList()
    }
}
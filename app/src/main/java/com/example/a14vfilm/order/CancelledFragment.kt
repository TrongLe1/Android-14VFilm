package com.example.a14vfilm.order

/*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.TransactionAdapter
import com.example.a14vfilm.models.Transaction
import com.example.a14vfilm.models.TransactionExtend
import com.example.a14vfilm.models.UserLogin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CancelledFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CancelledFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        // Inflate the layout for this fragment
        viewFragment = inflater.inflate(R.layout.fragment_cancelled, container, false)
        val RVCancelled = viewFragment!!.findViewById<RecyclerView>(R.id.RVCancelled)
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        RVCancelled.layoutManager = layoutManager
        RVCancelled.addItemDecoration(LayoutMarginDecoration(1, 50))
        return viewFragment
    }

    private fun getTransactionList() {
        val RVCancelled = viewFragment!!.findViewById<RecyclerView>(R.id.RVCancelled)
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
                        val trans = Transaction(id!!, user!!, film!!, rentDate!!, expired!!, total!!, rate!!, type!!)
                        if (!type) {
                            val sRef = FirebaseDatabase.getInstance(url).getReference("film")
                            val sQuery = sRef.orderByChild("id").equalTo(film)
                            sQuery.addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (singleSnapshot in snapshot.children) {
                                        val name = singleSnapshot.child("name").getValue<String>()
                                        val image = singleSnapshot.child("image").getValue<String>()
                                        transList.add(TransactionExtend(trans, name!!, image!!))
                                    }
                                    RVCancelled.adapter = TransactionAdapter(transList, "cancelled")
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
            RVCancelled.adapter = TransactionAdapter(ArrayList(), "cancelled")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CancelledFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        getTransactionList()
    }
}

*/
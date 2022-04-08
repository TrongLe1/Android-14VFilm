package com.example.a14vfilm.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a14vfilm.R
import com.example.a14vfilm.adapters.TransactionAdapter
import com.example.a14vfilm.home.HomeFragment
import com.example.a14vfilm.models.Transaction
import com.example.a14vfilm.models.UserLogin
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OrderedFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ordered, container, false)
        if (UserLogin.info != null) {
            val trans1 = Transaction(
                "1",
                1,
                Date(),
                Date(),
                10000.0,
                "B5314. Scream 2022 - Tiếng Thét 2022 2D25G (DTS-HD MA 7.1)"
            )
            val trans2 = Transaction(
                "1",
                2,
                Date(),
                Date(),
                20000.0,
                "B5306. Turning Red 2022 - Gấu Đỏ Biến Hình 2D25G (DTS-HD MA 7.1)"
            )
            val trans3 = Transaction(
                "1",
                3,
                Date(),
                Date(),
                30000.0,
                "B5299. Blacklight 2022 - Phi Vụ Đen 2D25G (DTS-HD MA 7.1)"
            )
            val RVOrdered = view.findViewById<RecyclerView>(R.id.RVOrdered)
            val layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            RVOrdered.adapter = TransactionAdapter(arrayListOf(trans1, trans2, trans3))
            RVOrdered.layoutManager = layoutManager
            RVOrdered.addItemDecoration(LayoutMarginDecoration(1, 50))
        }
        return view
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

}
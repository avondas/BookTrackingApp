package com.example.booktrackingapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktrackingapp.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var bookNav: String
    private lateinit var db: BooksDatabaseHelper
    private lateinit var booksAdapter: BookAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        bookNav = "all"

        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.add -> {
                    Log.d("HomeFragment", "Add button clicked")
                    findNavController().navigate(R.id.action_HomeFragment_to_AddBookFragment)
                    true
                }
                R.id.exit -> {
                    Log.d("HomeFragment", "Exit button clicked")
                    findNavController().navigate(R.id.action_HomeFragment_to_StartFragment)
                    true
                }
                R.id.bookSeries -> {
                    Log.d("HomeFragment", "Book series button clicked")
                    bookNav = "series"
                    val bookSeries = db.getBookSeries()
                    booksAdapter.refreshData(bookSeries)
                    true
                }
                R.id.standaloneBooks -> {
                    Log.d("HomeFragment", "standalone button clicked")
                    bookNav = "standalone"
                    val standaloneBooks = db.getStandaloneBooks()
                    booksAdapter.refreshData(standaloneBooks)
                    true
                }
                R.id.home -> {
                    Log.d("HomeFragment", "all button clicked")
                    bookNav = "all"
                    booksAdapter.refreshData(db.getAllBooks())
                    true
                }
                else -> false
            }
        }

        val bottomNavigationView = binding.bottomNavView
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = BooksDatabaseHelper(requireContext())
        booksAdapter = BookAdapter(db.getAllBooks(), requireContext())

        binding.booksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.booksRecyclerView.adapter = booksAdapter
    }

    override fun onResume() {
        super.onResume()
        if(bookNav == "all") {
            booksAdapter.refreshData(db.getAllBooks())
        } else if(bookNav == "standalone") {
            booksAdapter.refreshData(db.getStandaloneBooks())
        } else if(bookNav == "series") {
            booksAdapter.refreshData(db.getBookSeries())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
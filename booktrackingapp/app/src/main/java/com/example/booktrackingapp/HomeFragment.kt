package com.example.booktrackingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.booktrackingapp.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var db: BooksDatabaseHelper
    private lateinit var booksAdapter: BookAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // go back button
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_HomeFragment_to_StartFragment)
        }

        db = BooksDatabaseHelper(requireContext())
        booksAdapter = BookAdapter(db.getAllBooks(), requireContext())

        binding.booksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.booksRecyclerView.adapter = booksAdapter

        // this is the floating action button
        binding.fab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_HomeFragment_to_AddBookFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        booksAdapter.refreshData(db.getAllBooks())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
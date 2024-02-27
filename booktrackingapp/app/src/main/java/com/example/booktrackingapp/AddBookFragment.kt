package com.example.booktrackingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.booktrackingapp.databinding.FragmentAddBookBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

class AddBookFragment : Fragment() {
    private var _binding: FragmentAddBookBinding? = null
    // All the input fields for the books
    lateinit var authorInput: EditText
    lateinit var titleInput: EditText
    lateinit var readInput: CheckBox
    lateinit var seriesInput: EditText

    // Database helper
    private lateinit var db: BooksDatabaseHelper

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the database helper
        db = BooksDatabaseHelper(requireContext())

        // Where we handle the series checkbox, to show or hide the series name input
        val seriesCheckbox: CheckBox = requireView().findViewById(R.id.seriesCheckbox)
        val seriesText: TextView = requireView().findViewById(R.id.seriesTextView)
        val seriesName: EditText = requireView().findViewById(R.id.bookSeries)

        seriesCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                seriesText.visibility = View.VISIBLE
                seriesName.visibility = View.VISIBLE
            } else {
                seriesText.visibility = View.GONE
                seriesName.visibility = View.GONE
            }
        }

        // Where we handle the add button, to add the book to the database
        authorInput = requireView().findViewById(R.id.bookAuthor)
        titleInput = requireView().findViewById(R.id.bookTitle)
        readInput = requireView().findViewById(R.id.readCheckbox)
        seriesInput = seriesName

        binding.addButton.setOnClickListener {
            // add book to database
            val book = Book(
                titleInput.text.toString(),
                authorInput.text.toString(),
                readInput.isChecked,
                if (seriesCheckbox.isChecked) seriesInput.text.toString() else null
            )
            db.insertBook(book)

            // go back to home
            findNavController().navigate(R.id.action_AddBookFragment_to_HomeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
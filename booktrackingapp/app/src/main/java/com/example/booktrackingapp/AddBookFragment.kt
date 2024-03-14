package com.example.booktrackingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.booktrackingapp.databinding.FragmentAddBookBinding

class AddBookFragment : Fragment() {
    private var _binding: FragmentAddBookBinding? = null
    // All the input fields for the books
    lateinit var authorInput: AutoCompleteTextView
    lateinit var titleInput: EditText
    lateinit var readInput: CheckBox
    lateinit var seriesInput: AutoCompleteTextView

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

        val addBookTextView: ImageView = requireView().findViewById(R.id.backArrowAddBook)
        addBookTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        // Initialize the database helper
        db = BooksDatabaseHelper(requireContext())

        // Where we handle the series autocomplete
        val seriesNames = db.getSeriesNames()
        val seriesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, seriesNames)
        val seriesAutoComplete = requireView().findViewById<AutoCompleteTextView>(R.id.bookSeries)
        seriesAutoComplete?.setAdapter(seriesAdapter)

        // where we handle the author autocomplete
        val authorNames = db.getAuthorNames()
        val authorAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, authorNames)
        val authorAutoComplete = requireView().findViewById<AutoCompleteTextView>(R.id.bookAuthor)
        authorAutoComplete?.setAdapter(authorAdapter)

        // Where we handle the series checkbox, to show or hide the series name input
        val seriesCheckbox: CheckBox = requireView().findViewById(R.id.seriesCheckbox)
        val seriesText: TextView = requireView().findViewById(R.id.seriesTextView)
        val seriesName: AutoCompleteTextView = requireView().findViewById(R.id.bookSeries)

        seriesCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                seriesText.visibility = View.VISIBLE
                seriesName.visibility = View.VISIBLE
            } else {
                seriesText.visibility = View.GONE
                seriesName.visibility = View.GONE
                seriesAutoComplete.setText("")
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
                0,
                titleInput.text.toString(),
                authorInput.text.toString(),
                readInput.isChecked,
                if (seriesCheckbox.isChecked) seriesInput.text.toString() else null,
                0
            )
            val bookInsert = db.insertBook(book)
            // go back to home
            findNavController().navigate(R.id.action_AddBookFragment_to_HomeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
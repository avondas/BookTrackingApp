package com.example.booktrackingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import androidx.navigation.fragment.findNavController
import com.example.booktrackingapp.databinding.FragmentBookUpdateBinding


class UpdateBookFragment : Fragment() {
    private var _binding: FragmentBookUpdateBinding? = null

    // All the input fields for the books
    lateinit var authorInput: EditText
    lateinit var titleInput: EditText
    lateinit var readInput: CheckBox
    lateinit var seriesInput: EditText

    // Get id of the book to update
    private var bookId: Int = -1

    // Database helper
    private lateinit var db: BooksDatabaseHelper
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val updateBookTextView: ImageView = requireView().findViewById(R.id.backArrowUpdateBook)
        updateBookTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        // Initialize the database helper
        db = BooksDatabaseHelper(requireContext())

        // Get the book id from the arguments
        bookId = requireArguments().getInt("bookId")
        val book = db.getBook(bookId)

        // find the fields and set the text
        authorInput = requireView().findViewById(R.id.editBookAuthor)
        titleInput = requireView().findViewById(R.id.editBookTitle)
        readInput = requireView().findViewById(R.id.editReadCheckbox)

        val seriesNames = db.getSeriesNames();
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, seriesNames)
        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.editBookSeries)
        autoCompleteTextView?.setAdapter(adapter)

        // set the fields
        if(book != null) {
            val seriesCheckbox: CheckBox = requireView().findViewById(R.id.editSeriesCheckbox)
            val seriesText: TextView = requireView().findViewById(R.id.editSeriesTextView)
            val seriesName: AutoCompleteTextView = requireView().findViewById(R.id.editBookSeries)
            seriesInput = seriesName

            authorInput.setText(book.author)
            titleInput.setText(book.title)
            readInput.isChecked = book.read
            if(book.series != null) {
                seriesCheckbox.isChecked = true
                seriesText.visibility = View.VISIBLE
                seriesName.visibility = View.VISIBLE
                seriesName.setText(book.series)
            }

            seriesCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    seriesText.visibility = View.VISIBLE
                    seriesName.visibility = View.VISIBLE
                } else {
                    seriesText.visibility = View.GONE
                    seriesName.visibility = View.GONE
                    seriesName.setText("")
                }
            }

            binding.editButton.setOnClickListener {
                // add edited book to database
                val updatedBook = Book(
                    bookId,
                    titleInput.text.toString(),
                    authorInput.text.toString(),
                    readInput.isChecked,
                    if (seriesCheckbox.isChecked) seriesInput.text.toString() else null,
                    book.image
                )
                val bookEdit = db.updateBook(updatedBook)

                // go back to home
                findNavController().navigate(R.id.action_BookUpdateFragment_to_HomeFragment)
            }
        } else {
            findNavController().navigate(R.id.action_BookUpdateFragment_to_HomeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
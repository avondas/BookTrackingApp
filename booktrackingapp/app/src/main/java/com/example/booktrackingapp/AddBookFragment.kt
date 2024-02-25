package com.example.booktrackingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.booktrackingapp.databinding.FragmentAddBookBinding
import com.example.booktrackingapp.databinding.FragmentStartBinding

class AddBookFragment : Fragment() {
    private var _binding: FragmentAddBookBinding? = null
    lateinit var authorInput: EditText
    lateinit var titleInput: EditText

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
        authorInput = requireView().findViewById(R.id.bookAuthor)
        titleInput = requireView().findViewById(R.id.bookTitle)
        binding.addButton.setOnClickListener {
//            println(authorInput.text)
//            println(titleInput.text)
            findNavController().navigate(R.id.action_AddBookFragment_to_HomeFragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
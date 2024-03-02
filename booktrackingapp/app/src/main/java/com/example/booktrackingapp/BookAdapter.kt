package com.example.booktrackingapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private var books: List<Book>, context: Context) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    private var db = BooksDatabaseHelper(context)

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.author)
        val readCheckBox: CheckBox = itemView.findViewById(R.id.readCheckBox)
        val seriesTextView: TextView = itemView.findViewById(R.id.seriesTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.go_edit_button)
        val deleteButton: ImageView = itemView.findViewById(R.id.go_delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        holder.readCheckBox.isChecked = book.read
        if(book.series != null) {
            holder.seriesTextView.visibility = View.VISIBLE
            holder.seriesTextView.text = book.series
        }

        holder.updateButton.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("bookId", book.id)
            }
            it.findNavController().navigate(R.id.action_HomeFragment_to_UpdateBookFragment, bundle)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteBook(book.id)
            refreshData(db.getAllBooks())
            Toast.makeText(it.context, "Book deleted", Toast.LENGTH_SHORT).show()
        }
    }

    fun refreshData(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
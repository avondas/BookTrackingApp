package com.example.booktrackingapp

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import java.util.Random

class BookAdapter(private var books: List<Book>, context: Context) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    private var db = BooksDatabaseHelper(context)

    private val images = listOf(
        R.drawable.blue,
        R.drawable.blue_straight,
        R.drawable.blue_triangle,
        R.drawable.red,
        R.drawable.red_straight,
        R.drawable.red_triangle,
        R.drawable.green,
        R.drawable.green_straight,
        R.drawable.green_triangle
    )
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.author)
        val readCheckBox: CheckBox = itemView.findViewById(R.id.readCheckBox)
        val seriesTextView: TextView = itemView.findViewById(R.id.seriesTextView)
        val updateButton: ImageView = itemView.findViewById(R.id.go_edit_button)
        val deleteButton: ImageView = itemView.findViewById(R.id.go_delete_button)
        val cardView: androidx.cardview.widget.CardView = itemView.findViewById(R.id.book_item)
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
        } else {
            holder.seriesTextView.visibility = View.GONE
        }

        val drawable = ContextCompat.getDrawable(holder.cardView.context, images[book.image]) as BitmapDrawable
        drawable.gravity = Gravity.FILL
        holder.cardView.background = drawable

        holder.updateButton.setOnClickListener {
            val bundle = Bundle().apply {
                putInt("bookId", book.id)
            }
            it.findNavController().navigate(R.id.action_HomeFragment_to_UpdateBookFragment, bundle)
        }

        holder.deleteButton.setOnClickListener {
            db.deleteBook(book.id)
            refreshData(db.getAllBooks())
        }
    }

    fun refreshData(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
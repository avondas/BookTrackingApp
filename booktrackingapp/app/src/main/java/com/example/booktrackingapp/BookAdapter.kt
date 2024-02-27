package com.example.booktrackingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private var books: List<Book>, context: Context) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.author)
        val readCheckBox: CheckBox = itemView.findViewById(R.id.readCheckBox)
        val seriesTextView: TextView = itemView.findViewById(R.id.seriesTextView)
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
    }

    fun refreshData(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
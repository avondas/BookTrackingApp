package com.example.booktrackingapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Random

class BooksDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        val DATABASE_NAME = "books.db"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "books"
        val COL_ID = "id"
        val COL_TITLE = "title"
        val COL_AUTHOR = "author"
        val COL_READ = "read"
        val COL_SERIES = "series"
        val COL_IMAGE = "image"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY, $COL_TITLE TEXT, $COL_AUTHOR TEXT, $COL_READ BOOLEAN, $COL_SERIES TEXT, $COL_IMAGE INTEGER)"
        p0?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0)
    }

    fun insertBook(book: Book){
        val randomIndex = Random().nextInt(9)

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, book.title)
            put(COL_AUTHOR, book.author)
            put(COL_READ, book.read)
            put(COL_SERIES, book.series)
            put(COL_IMAGE, randomIndex)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $COL_AUTHOR ASC"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR))
            val read = cursor.getInt(cursor.getColumnIndexOrThrow(COL_READ))
            val series = cursor.getString(cursor.getColumnIndexOrThrow(COL_SERIES))
            val image = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE))

            if(series != null){
                val book = Book(id, title, author, read == 1, series, image)
                books.add(book)
            } else {
                val book = Book(id, title, author, read == 1, null, image)
                books.add(book)
            }
        }
        cursor.close()
        db.close()
        return books
    }

    fun getStandaloneBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME  WHERE $COL_SERIES IS NULL ORDER BY $COL_AUTHOR ASC"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR))
            val read = cursor.getInt(cursor.getColumnIndexOrThrow(COL_READ))
            val series = cursor.getString(cursor.getColumnIndexOrThrow(COL_SERIES))
            val image = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE))

            books.add(Book(id, title, author, read == 1, null, image))
        }
        cursor.close()
        db.close()
        return books
    }

    fun getBookSeries(): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME  WHERE $COL_SERIES IS NOT NULL ORDER BY $COL_SERIES ASC"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
            val author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR))
            val read = cursor.getInt(cursor.getColumnIndexOrThrow(COL_READ))
            val series = cursor.getString(cursor.getColumnIndexOrThrow(COL_SERIES))
            val image = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE))

            books.add(Book(id, title, author, read == 1, series, image))
        }
        cursor.close()
        db.close()
        return books
    }

    fun updateBook(book: Book){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_TITLE, book.title)
            put(COL_AUTHOR, book.author)
            put(COL_READ, book.read)
            put(COL_SERIES, book.series)
        }
        db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(book.id.toString()))
        db.close()
    }

    fun getBook(id: Int): Book {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = $id"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE))
        val author = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTHOR))
        val read = cursor.getInt(cursor.getColumnIndexOrThrow(COL_READ))
        val series = cursor.getString(cursor.getColumnIndexOrThrow(COL_SERIES))
        val image = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IMAGE))

        cursor.close()
        db.close()
        return Book(id, title, author, read == 1, series, image)
    }

    fun deleteBook(id: Int){
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(id.toString()))
        db.close()
    }
}
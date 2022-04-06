package com.example.androidroom

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase(){

    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
            // jika INSTANCE bukan null, kembalikan,
            // jika ya, buat databasenya
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    // Menghapus dan membangun kembali alih-alih bermigrasi jika tidak ada objek Migrasi.
                    // Migrasi bukan bagian dari codelab ini.
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Ganti metode onCreate untuk mengisi database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.wordDao())
                    }
                }
            }
        }

        /**
         * Isi database dalam coroutine baru.
         * Jika Anda ingin memulai dengan lebih banyak kata, tambahkan saja.
         */
        suspend fun populateDatabase(wordDao: WordDao) {
            // Mulai aplikasi dengan database yang bersih setiap saat.
            // Tidak diperlukan jika Anda hanya mengisi kreasi.
            wordDao.deleteAll()

            var word = Word("AND-4")
            wordDao.insert(word)
            word = Word("Semangat!")
            wordDao.insert(word)
        }
    }
}
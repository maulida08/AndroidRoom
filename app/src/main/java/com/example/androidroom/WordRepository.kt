package com.example.androidroom

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {

    // Room mengeksekusi semua kueri pada utas terpisah.
    // Aliran yang Diamati akan memberi tahu pengamat ketika data telah berubah.
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    // Secara default, Room menjalankan kueri penangguhan dari utas utama, oleh karena itu, kami tidak perlu
    // mengimplementasikan hal lain untuk memastikan kami tidak melakukan pekerjaan
    // basis data yang berjalan lama di utas utama.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}

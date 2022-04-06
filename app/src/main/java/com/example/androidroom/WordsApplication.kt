package com.example.androidroom

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class WordsApplication : Application() {
    // Tidak perlu membatalkan lingkup ini karena akan diruntuhkan dengan proses
    val applicationScope = CoroutineScope(SupervisorJob())

    // Menggunakan oleh lazy sehingga database dan repositori hanya dibuat saat dibutuhkan
    // daripada saat aplikasi dimulai
    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }
}

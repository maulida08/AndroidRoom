package com.example.androidroom

import androidx.lifecycle.*
import kotlinx.coroutines.launch

/**
 * ViewModel untuk menyimpan referensi ke repositori kata dan daftar semua kata terbaru.
 */

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    /**
     * Menggunakan LiveData dan menyimpan apa yang dikembalikan oleh allWords memiliki beberapa manfaat:
     * - Kami dapat menempatkan pengamat pada data (bukan polling untuk perubahan) dan
     * hanya memperbarui UI saat data benar-benar berubah.
     * - Repositori benar-benar terpisah dari UI melalui ViewModel.
     */
    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    /**
     * Meluncurkan coroutine baru untuk memasukkan data dengan cara yang tidak memblokir
     */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}

class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

package com.example.sdmp_united.ui.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AboutViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Huffman Fragment"
    }
    val text: LiveData<String> = _text
}
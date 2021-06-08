package com.example.sdmp_united.ui.lab1.huffman

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HuffmanViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Huffman Fragment"
    }
    val text: LiveData<String> = _text
}
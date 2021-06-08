package com.example.sdmp_united.ui.lab2.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SelectViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is DataTable Fragment"
    }
    val text: LiveData<String> = _text
}
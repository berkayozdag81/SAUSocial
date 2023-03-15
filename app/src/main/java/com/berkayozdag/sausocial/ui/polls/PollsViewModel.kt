package com.berkayozdag.sausocial.ui.polls

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PollsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is polls Fragment"
    }
    val text: LiveData<String> = _text
}
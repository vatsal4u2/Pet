package com.vatsal.master.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vatsal.master.model.AppDatabase
import com.vatsal.master.model.DogBreed
import kotlinx.coroutines.launch

class DetailViewModel(application : Application) : BaseViewModel(application) {
    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(uuId : Int){
        launch {
            val dog : DogBreed = AppDatabase.invoke(getApplication()).dogDao().getDog(uuId)
            dogLiveData.value = dog
        }
    }
}
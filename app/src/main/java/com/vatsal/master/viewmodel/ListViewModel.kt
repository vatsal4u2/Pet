package com.vatsal.master.viewmodel

import android.app.Application
import android.database.Observable
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.vatsal.master.model.ApiService
import com.vatsal.master.model.AppDatabase
import com.vatsal.master.model.DogBreed
import com.vatsal.master.util.SharedPreferencesHelper
import io.reactivex.Observable.*
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.function.Function

class ListViewModel(application: Application) : BaseViewModel(application) {
    val dogs = MutableLiveData<List<DogBreed>>()
    val loadingErrorMessage = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private val apiService = ApiService()
    private val disposable  = CompositeDisposable()
    private var prefHelper = SharedPreferencesHelper(getApplication())
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    fun refreshDogsList(){
        val updateTime  = prefHelper.getUpdateTime()
        updateTime?.let { it ->
            if(!it.equals(0) && System.nanoTime() - it < refreshTime){
                fetchFromLocalDatabase()
            }else{
                fetchFromRemote()
            }
        }
    }

    // When user swipe and refresh, It always gets the data from remote.
    fun refreshByPassCache(){
        fetchFromRemote()
    }

    private fun fetchFromLocalDatabase(){
        loading.value = true
        launch {
            val dogs = AppDatabase
                .invoke(getApplication())
                .dogDao()
                .getAllDogs()
            dogsRetrieved(dogs)
        }
    }

    private fun fetchFromRemote(){
        loading.value = true
        disposable.add(apiService.getDogs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<DogBreed>>(){
                override fun onSuccess(dogListResponse: List<DogBreed>) {
                    storeDogsListLocally(dogListResponse)
                }
                override fun onError(e: Throwable) {
                    loadingErrorMessage.value = true
                    loading.value = false
                    e.printStackTrace()
                }
            })
        )
    }
    private fun dogsRetrieved(dogsList : List<DogBreed>){
        dogs.value = dogsList
        loading.value = false
        loadingErrorMessage.value = false
    }

    private fun storeDogsListLocally(list : List<DogBreed>) {
        launch {
            val dao = AppDatabase(getApplication()).dogDao()
            dao.deleteAllDogs()
            val result: List<Long> = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        // Clear all observables.
        disposable.clear()
    }
}
package com.vatsal.master.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vatsal.master.R
import com.vatsal.master.model.AppDatabase
import com.vatsal.master.util.getProgressDrawable
import com.vatsal.master.util.loadImage
import com.vatsal.master.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_dog.*


class DetailFragment : Fragment() {

    private lateinit var vm : DetailViewModel
    private var dogUuid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            dogUuid = it.getInt("dogUuid", 0)
        }

        vm = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        vm.fetch(dogUuid)

        observerViewModel()
    }

    private fun observerViewModel() {
        vm.dogLiveData.observe(this, Observer { dog ->
            dog.let {
                dogName.text = it.dogBreed
                dogPurpose.text = it.breedGroup
                dogLifeSpan.text = it.lifeSpan
                dogTemperament.text = it.temperament
                dogImage.loadImage(dog.imageUrl, context?.let { it1 -> getProgressDrawable(it1) })
            }
        })
    }
}
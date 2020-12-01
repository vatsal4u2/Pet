package com.vatsal.master.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.vatsal.master.R
import com.vatsal.master.databinding.FragmentDetailBinding
import com.vatsal.master.model.AppDatabase
import com.vatsal.master.util.getProgressDrawable
import com.vatsal.master.util.loadImage
import com.vatsal.master.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_dog.*


class DetailFragment : Fragment() {

    private lateinit var vm : DetailViewModel
    private var dogUuid = 0
   lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         dataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false)
        return dataBinding.root
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
            dataBinding.dog = dog
        })
    }
}
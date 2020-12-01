package com.vatsal.master.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.vatsal.master.R
import com.vatsal.master.model.DogBreed
import com.vatsal.master.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private lateinit var vm :ListViewModel
    private val dogListAdapter : DogListAdapter = DogListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProviders.of(this).get(ListViewModel::class.java)
        vm.refreshDogsList()

        dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dogListAdapter
        }
        refresh.setOnRefreshListener {
            dogsList.visibility = View.GONE
            listViewError.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            vm.refreshByPassCache()
            refresh.isRefreshing = false
        }
        observeViewModel()

    }

    private fun observeViewModel(){
        vm.dogs.observe(this, Observer {dogs ->
            dogs.let {
                dogsList.visibility = View.VISIBLE
                dogListAdapter.updateDogList(dogs as ArrayList<DogBreed>)
            }
        })

        vm.loadingErrorMessage.observe(this, Observer { isError ->
            isError?.let {
                listViewError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        vm.loading.observe(this, Observer {
            it.let {
                progressBar.visibility = if(it as Boolean) View.VISIBLE else View.GONE
                if(it){
                    listViewError.visibility = View.GONE
                    dogsList.visibility = View.GONE
                }
            }
        })
    }
}
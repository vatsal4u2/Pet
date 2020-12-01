package com.vatsal.master.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.vatsal.master.R
import com.vatsal.master.model.DogBreed
import com.vatsal.master.util.getProgressDrawable
import com.vatsal.master.util.loadImage
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_dog.view.*
import java.util.zip.Inflater

class DogListAdapter(val dogsList : ArrayList<DogBreed>) : RecyclerView.Adapter<DogListAdapter.DogViewHolder>() {

    private val TAG = DogListAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_dog,parent,false)
        return DogViewHolder(view)
    }

    override fun getItemCount() = dogsList.size


    fun updateDogList(dogs: ArrayList<DogBreed>){
        this.dogsList.clear()
        this.dogsList.addAll(dogs)
        notifyDataSetChanged()
        Log.d(TAG,"Dogs list updated.. new size -> ${dogs.size}")
    }
    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.name.text = dogsList[position].dogBreed
        holder.view.lifeSpan.text = dogsList[position].lifeSpan
        holder.view.setOnClickListener({
            val bundle = bundleOf("dogUuid" to dogsList[position].uuid)
            Navigation.findNavController(it).navigate(R.id.action_listFragment_to_detailFragment,bundle)
        })

        holder.view.image.loadImage(dogsList[position].imageUrl,
            getProgressDrawable(holder.view.image.context))
    }

    class DogViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
    }
}
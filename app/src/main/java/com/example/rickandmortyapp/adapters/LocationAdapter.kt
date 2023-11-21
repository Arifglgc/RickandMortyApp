package com.example.rickandmortyapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.LocationItemBinding
import com.example.rickandmortyapp.pojo.location.Result

class LocationAdapter(private var selectedPosition: Int) : RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    private lateinit var binding: LocationItemBinding
    private var locationList = ArrayList<Result>()


    var onItemClick : ((Result,Int) -> Unit)? = null

    fun setList(locationList: ArrayList<Result>){
        Log.d("LocationAdapter","The item count of location list !! "+ this.locationList.size+ " " +itemCount)
        this.locationList=locationList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
         binding = LocationItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LocationViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return locationList.size
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {


        holder.binding.locationBt.text = locationList[position].name
        Log.d("LocationAdapter","Success !! "+ locationList[position].name)

        holder.binding.locationBt.setBackgroundColor(
            if (position == selectedPosition) {
                holder.itemView.context.getColor(R.color.button_clicked_color)
            } else {
                holder.itemView.context.getColor(R.color.newlol)
            }
        )

        holder.binding.locationBt.setOnClickListener {
            Log.d("LocationAdapterOnClick","Success !! "+ locationList[position].name)
            selectedPosition = holder.adapterPosition
            notifyDataSetChanged()

            onItemClick!!.invoke(locationList[position],selectedPosition)

        }
    }

    inner class LocationViewHolder( val binding: LocationItemBinding): ViewHolder(binding.root)
}
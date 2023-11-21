package com.example.rickandmortyapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.R
import com.example.rickandmortyapp.databinding.CharacterItemBinding
import com.example.rickandmortyapp.pojo.character.Result
class CharacterAdapter(): RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {
    private lateinit var binding: CharacterItemBinding
    private var characterList = ArrayList<Result>()
    var onItemClick : ((Result) -> Unit)? = null


    fun setList(characterList: ArrayList<Result>){
        this.characterList= characterList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        binding=CharacterItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CharacterViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return characterList.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {


        val model: Result = characterList[position]
        when (model.gender) {
            "Female" -> holder.binding.imgVChaGen.setImageResource(R.drawable.female)
            "Male" -> holder.binding.imgVChaGen.setImageResource(R.drawable.male)
            "unknown" -> holder.binding.imgVChaGen.setImageResource(R.drawable.unknown)
            "genderless" -> holder.binding.imgVChaGen.setImageResource(R.drawable.genderless)
        }

        Glide.with(holder.itemView).load(model.image).into(holder.binding.imgVCha)
        holder.binding.txtVCha.text=model.name

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(characterList[position])
        }
    }
    inner class CharacterViewHolder(val binding: CharacterItemBinding): ViewHolder(binding.root)

}
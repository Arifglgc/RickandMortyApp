package com.example.rickandmortyapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.rickandmortyapp.databinding.ActivityCharacterDetailBinding
import com.example.rickandmortyapp.pojo.character.Result
import com.example.rickandmortyapp.viewModels.CharacterDetailsViewModel

@Suppress("DEPRECATION")
class CharacterDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterDetailBinding
    private lateinit var characterDetailsViewModel: CharacterDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        characterDetailsViewModel = ViewModelProvider(this)[CharacterDetailsViewModel::class.java]


        val intent: Intent = intent
        val selectedCharacter: Result = intent.getParcelableExtra("selectedCharacter")!!
        characterDetailsViewModel.setSelectedCharacter(selectedCharacter)

        observeSelectedCharacterItem()
    }

    private fun observeSelectedCharacterItem() {
        characterDetailsViewModel.observeSelectedCharacter().observe(this) { character ->

            binding.chaDetName.text = character.name
            binding.episodesTextView.text = character.episode.toString()
            binding.genderTextView.text = character.gender
            binding.locationTextView.text=character.location.name
            binding.originTextView.text=character.origin.name
            binding.speciesTextView.text=character.species
            binding.statusTextView.text=character.status
            binding.epla.text=character.created

            var list: List<String> = ArrayList()
            for (item in character.episode) {
                val parts = item.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()
                val lastPart = parts[parts.size - 1]
                list += lastPart
            }

            binding.episodesTextView.text = list.toString().replace("[", "").replace("]", "")
            Glide.with(this).load(character.image).into(binding.imageView)

        }

    }


}
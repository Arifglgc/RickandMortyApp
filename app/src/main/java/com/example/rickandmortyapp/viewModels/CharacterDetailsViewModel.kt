package com.example.rickandmortyapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmortyapp.pojo.character.Result

class CharacterDetailsViewModel(): ViewModel() {
    private var selectedCharacter= MutableLiveData<Result>()

      fun setSelectedCharacter(selectedCharacter:Result){
         this.selectedCharacter.value=selectedCharacter
     }

     fun observeSelectedCharacter() : LiveData<Result>{
         return selectedCharacter
     }
}
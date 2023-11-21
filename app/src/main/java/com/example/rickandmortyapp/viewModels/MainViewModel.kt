package com.example.rickandmortyapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmortyapp.pojo.location.Location
import com.example.rickandmortyapp.pojo.location.Result
import com.example.rickandmortyapp.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel(){

    private var locationList =MutableLiveData<List<Result>>()
    private var characterList =MutableLiveData<List<com.example.rickandmortyapp.pojo.character.Result>>()
    private var firstLocItemCharacter= MutableLiveData<List<String>> ()
    private var totalPages = MutableLiveData<Int>()
    private var currentPage= MutableLiveData<Int>(1)
    private var scrollPosition= MutableLiveData<Int>()
    private var selectedLocationButtonIndex= MutableLiveData<Int> (0)

    fun getLocations(){
         RetrofitClient.apiService.getLocations(currentPage.value!!).enqueue(object: Callback<Location>{
             override fun onResponse(call: Call<Location>, response: Response<Location>) {
                 if (response.body()!= null){

                     val newResults = response.body()!!.results
                     val currentList = locationList.value?.toMutableList() ?: mutableListOf()
                     // Eğer sayfa numarası 1 ise, mevcut listeyi sıfırla
                     if (currentPage.value == 1) {
                         firstLocItemCharacter.value=response.body()!!.results[0].residents
                         currentList.clear()
                     }

                     currentList.addAll(newResults)
                     locationList.value = currentList.toList()
                     scrollPosition.value= currentList.toList().size
                     Log.d("LocationViewModel","The count of location list: !! "+ locationList.value!!.size)
                     Log.d("LocationViewModel","scroll position"+ scrollPosition.value)


                    // locationList.value= response.body()!!.results
                     totalPages.value=response.body()!!.info.pages
                     Log.d("LocationViewModel","First Item Residents: !! "+ response.body()!!.results[0].residents)

                     currentPage.value = currentPage.value!! + 1
                     Log.d("LocationViewModel","Currnet Page !! "+ currentPage.value)
                 }
                 else  return
             }
             override fun onFailure(call: Call<Location>, t: Throwable) {
                 Log.d("LocationViewModel","Error in getLocations!!")
             }
         })
    }

    fun observeLocationsLiveData(): LiveData<List<Result>> {
        return locationList
    }

    fun getCharactersByLocation(ids: List<String>){
        val idList= extractIDsFromStringList(ids)

        RetrofitClient.apiService.getCharactersByLocId(idList).enqueue(object: Callback<List<com.example.rickandmortyapp.pojo.character.Result>>{
            override fun onResponse(
                call: Call<List<com.example.rickandmortyapp.pojo.character.Result>>,
                response: Response<List<com.example.rickandmortyapp.pojo.character.Result>>
            ) {
                if (response.body()!= null){
                    characterList.value=response.body()
                    Log.d("LVM","Success !! "+ characterList.value)
                }
                else {characterList.value= emptyList()
                Log.d("LVM","The list empty !! "+ characterList.value)}
            }

            override fun onFailure(
                call: Call<List<com.example.rickandmortyapp.pojo.character.Result>>,
                t: Throwable
            ) {
                Log.d("LocationViewModel","Error in getCharactersByLocation!!")
            }
        })
    }
    private fun extractIDsFromStringList(urls: List<String>): ArrayList<String> {
        val numbersList = ArrayList<String>()

        for (url in urls) {
            val regex = Regex("""\d+$""") // Match if there is digit at the end of the string
            val matchResult = regex.find(url)

            matchResult?.let {
                val number = it.value
                numbersList.add(number)
            }
        }
        return numbersList
    }

    fun observeCharactersLiveData(): LiveData<List<com.example.rickandmortyapp.pojo.character.Result>> {
        return characterList
    }
    fun observeFirstCharactersLiveData(): LiveData<List<String>> {
        return firstLocItemCharacter
    }
    fun observeSelectedButtonLiveData(): LiveData<Int> {
        return selectedLocationButtonIndex
    }

    fun setSelectedLocationButtonIndex(index: Int) {
        selectedLocationButtonIndex.value = index
    }

    fun observeCurrentPageLiveData(): LiveData<Int>{
        return currentPage
    }

    fun observeScrollPositionLiveData(): LiveData<Int>{
        return scrollPosition
    }

}
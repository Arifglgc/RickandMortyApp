package com.example.rickandmortyapp.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmortyapp.adapters.CharacterAdapter
import com.example.rickandmortyapp.adapters.LocationAdapter
import com.example.rickandmortyapp.databinding.ActivityMainBinding
import com.example.rickandmortyapp.pojo.location.Result
import com.example.rickandmortyapp.viewModels.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainVM: MainViewModel
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var characterAdapter: CharacterAdapter
    private var loadJob: Job? = null
    private var isLoading= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainVM= ViewModelProvider(this)[MainViewModel::class.java]


       locationAdapter=LocationAdapter(mainVM.observeSelectedButtonLiveData().value!!)
        Log.d("MainActivity","The bottom of locAdapter !! ")

       characterAdapter= CharacterAdapter()


    if (savedInstanceState == null) {
       // Load data or initialize UI for the first time
        observeFirstCharacterLiveData()
        mainVM.getLocations()
    }
        prepareLocationRecView()
        prepareCharacterRecView()
        observeLocationsLiveData()

        observeCharactersLiveData()
        onLocationClick()
        onCharacterClick()
        recyclerViewListener()

    }


    // The listener for keep track if user scrolled the recyView to end of the list
    private fun recyclerViewListener() {
        binding.recVLoc.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val isWithinPageLimit= (mainVM.observeCurrentPageLiveData().value!! <= mainVM.observeTotalPageLiveData().value!!)

                if (!recyclerView.canScrollHorizontally(1) && newState == RecyclerView.SCROLL_STATE_IDLE
                    && isWithinPageLimit &&  loadJob == null) {
                    Log.d("MainActivity","The end of the scrool !! ")
                    showProgressBar(true)
                    isLoading=true

                    loadJob= lifecycleScope.launch {
                        try {
                            delay(1000)
                            // delay for progress bar
                            mainVM.getLocations()
                            prepareLocationRecView()
                            binding.recVLoc.itemAnimator = null
                            binding.recVLoc.scrollToPosition((mainVM.observeScrollPositionLiveData().value!!) - 1)
                        }
                        finally {
                            showProgressBar(false)
                            isLoading = false
                            loadJob = null
                        }
                    }
                }
            }
        })
    }

    private fun onCharacterClick() {
        characterAdapter.onItemClick={
            character->
            intent= Intent(this,CharacterDetailsActivity::class.java)
            intent.putExtra("selectedCharacter",character)
            startActivity(intent)
        }
    }

    private fun onLocationClick() {
        locationAdapter.onItemClick={
            location, index->
            mainVM.getCharactersByLocation(location.residents)
            mainVM.setSelectedLocationButtonIndex(index)
            prepareCharacterRecView()
        }
    }

    private fun prepareLocationRecView() {
        binding.recVLoc.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=locationAdapter
            Log.d("RecyclerLoc","Yeapppp its done !! ")
        }
    }

    private fun prepareCharacterRecView() {
        binding.recVCha.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter=characterAdapter

        }
    }

    private fun observeLocationsLiveData() {
        mainVM.observeLocationsLiveData().observe(this
        ) { locList ->
            locList?.let {
                Log.d("LocLiveData","Yeapppp the list size is!!"+locList.size)

                locationAdapter.setList(it as ArrayList<Result>)
            }
        }
    }

    private fun observeCharactersLiveData() {
        mainVM.observeCharactersLiveData().observe(this
        ) { chaList ->
            chaList?.let {
                val arrayList: ArrayList<com.example.rickandmortyapp.pojo.character.Result> = ArrayList(chaList)
                if (arrayList.isEmpty()){
                    binding.recVCha.visibility= View.GONE
                    binding.emptyView.linearLay.visibility=View.VISIBLE
                }
                else
                {
                    binding.recVCha.visibility=View.VISIBLE
                    binding.emptyView.linearLay.visibility=View.GONE
                }
                    characterAdapter.setList(arrayList)
            }
        }
    }
    private fun showProgressBar(show: Boolean) {
        binding.progressBarRecVLoc.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun observeFirstCharacterLiveData() {
        mainVM.observeFirstCharactersLiveData().observe(this
        ) { firstLocation ->
            mainVM.getCharactersByLocation(firstLocation)
        }
    }
}
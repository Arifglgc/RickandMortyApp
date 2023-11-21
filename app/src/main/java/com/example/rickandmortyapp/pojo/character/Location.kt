package com.example.rickandmortyapp.pojo.character

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Location(
    val name: String,
    val url: String
): Parcelable
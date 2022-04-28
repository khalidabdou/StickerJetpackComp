package com.example.stickerjetpackcomp.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stickerjetpackcomp.data.Remote
import com.example.stickerjetpackcomp.model.Stickers
import com.example.stickerjetpackcomp.utils.HandleResponse
import com.example.stickerjetpackcomp.utils.NetworkResults
import com.example.stickerjetpackcomp.utils.StickersUtils
import com.example.testfriends_jetpackcompose.di.NetworkModule
import com.green.china.sticker.features.sticker.models.StickerPack
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StickerViewModel @Inject constructor(
    private val remote: Remote
):ViewModel(){

    private val stickersFromApi= mutableStateOf<NetworkResults<Stickers>>(NetworkResults.Loading())

    val stickers= mutableStateOf<List<StickerPack>?>(null)

    private val list= arrayListOf<StickerPack>()

    fun getStickers()=viewModelScope.launch {
        if (stickersFromApi.value is NetworkResults.Error || stickersFromApi.value is NetworkResults.Loading ){
            val response=remote.getStickers()
            val handleStickers=HandleResponse(response)
            stickersFromApi.value=handleStickers.handleResult()
            Log.d("results",stickersFromApi.value.toString())
        }

        if (stickersFromApi.value is NetworkResults.Success){
            Log.d("results",list.size.toString())
            stickersFromApi.value.data!!.results.forEach { sticker->
                val pack:StickerPack=StickersUtils.convertStickerToPack(sticker)
                list.add(pack)
            }
            stickers.value=list

        }
       
    }
}
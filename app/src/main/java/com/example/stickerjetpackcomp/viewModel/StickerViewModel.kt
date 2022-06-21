package com.example.stickerjetpackcomp.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.stickerjetpackcomp.data.Remote
import com.example.stickerjetpackcomp.model.Categories
import com.example.stickerjetpackcomp.model.Category
import com.example.stickerjetpackcomp.model.Stickers
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.utils.HandleResponse
import com.example.stickerjetpackcomp.utils.NetworkResults
import com.example.stickerjetpackcomp.utils.StickersUtils
import com.example.stickerjetpackcomp.utils.core.utils.hawk.Hawk
import com.green.china.sticker.core.extensions.others.getLastBitFromUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
class StickerViewModel @Inject constructor(
    private val remote: Remote
) : ViewModel() {


    private lateinit var stickerPackView: StickerPack

    private val stickersFromApi = mutableStateOf<NetworkResults<Stickers>>(NetworkResults.Loading())
    private val catsFromApi = mutableStateOf<NetworkResults<Categories>>(NetworkResults.Loading())

    val stickers = mutableStateOf<List<StickerPack>?>(null)
    var stickerByCat = mutableStateOf<List<StickerPack>?>(null)
    val categories = mutableStateOf<List<Category>?>(null)
    var cid= 0

    private val list = arrayListOf<StickerPack>()

    var detailsPack = mutableStateOf<StickerPack?>(null)

    var index = 0
    var progress = mutableStateOf(0)
    var isReady = mutableStateOf(false)

    fun getStickers() = viewModelScope.launch {
        if (stickersFromApi.value is NetworkResults.Error || stickersFromApi.value is NetworkResults.Loading) {
            val response = remote.getStickers()
            val handleStickers = HandleResponse(response)
            stickersFromApi.value = handleStickers.handleResult()

        }
        if (stickersFromApi.value is NetworkResults.Success) {
            Log.d("results", list.size.toString())
            stickersFromApi.value.data!!.results.forEach { sticker ->
                val pack: StickerPack = StickersUtils.convertStickerToPack(sticker)
                list.add(pack)
                Hawk.delete("sticker_packs")
            }
            stickers.value = list

            Hawk.put("sticker_packs", list)
                //Log.d("hwak",Hawk.get<String?>("sticker_packs").toString())

        }
    }

    fun getCategories() = viewModelScope.launch {
        //Log.d("cats","begin")
        if (catsFromApi.value is NetworkResults.Error || catsFromApi.value is NetworkResults.Loading) {
            val response = remote.getCategories()
            val handleCats = HandleResponse(response)
            catsFromApi.value = handleCats.handleResult()
            //Log.d("cats",catsFromApi.value.toString())
        }
        if (catsFromApi.value is NetworkResults.Success) {
            categories.value = catsFromApi.value.data!!.results.shuffled()
            //Log.d("cats",categories.value.toString())
        }
    }

    fun setDetailPack(pack: StickerPack) {
        detailsPack.value = pack
        stickerPackView = pack
    }

    fun stickersByCat(){
        stickerByCat.value= stickers.value!!.filter { stickerPack -> stickerPack.catId ==cid }
    }


    fun incrementAddToWhatsapp(identifier :Int){
       viewModelScope.launch {
           remote.incrementStickerAddTo(identifier)
       }
    }

    fun incrementViews(identifier: Int){
        viewModelScope.launch {
            remote.incrementStickerViews(identifier)
        }
    }

    fun download() {
        if (index < stickerPackView.stickers.size) {
            var fileName = getLastBitFromUrl(stickerPackView.stickers[index].image_file)
            var url = stickerPackView.stickers[index].image_file
            Log.d("TGG", url + " ---//-- " + fileName)
            PRDownloader.download(
                url,
                "${StickersUtils.path}/${stickerPackView.identifier}/",
                fileName
            ).build().setOnProgressListener {

            }.start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val file = File(
                        "${StickersUtils.path}/${stickerPackView.identifier}/",
                        fileName
                    )
                    Log.d("TGG", "download " + index)
                    Log.d("TGG", file.exists().toString())
                    //Log.d("TGG", file.absolutePath.toString())
                    //Log.d("TGG",stickerPackView.stickers[index].image_file)
                    index++
                    progress.value=(index*100/detailsPack.value!!.stickers.size)

                    var lengthbmp = file.length() /1024

                    Log.d("TAG",lengthbmp.toString() + " ${fileName}")

                    download()

                }

                override fun onError(error: com.downloader.Error?) {
                    Log.d("TGG", "error $error.")
                }
            })
        } else {
            Log.d("TGG", "finish")
            isReady.value = true
            //openWahatsappActivityForResult()
        }

    }
}
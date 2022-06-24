package com.example.stickerjetpackcomp.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.stickerjetpackcomp.data.Local
import com.example.stickerjetpackcomp.data.Remote
import com.example.stickerjetpackcomp.model.*
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.utils.HandleResponse
import com.example.stickerjetpackcomp.utils.NetworkResults
import com.example.stickerjetpackcomp.utils.StickersUtils
import com.example.stickerjetpackcomp.utils.StickersUtils.Companion.isOnline
import com.example.stickerjetpackcomp.utils.core.utils.hawk.Hawk
import com.green.china.sticker.core.extensions.others.getLastBitFromUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.math.log


@HiltViewModel
class StickerViewModel @Inject constructor(
    private val remote: Remote,
    private val local: Local
) : ViewModel() {


    private lateinit var stickerPackView: StickerPack

    private val stickersFromApi = mutableStateOf<NetworkResults<Stickers>>(NetworkResults.Loading())
    private val catsFromApi = mutableStateOf<NetworkResults<Categories>>(NetworkResults.Loading())
    private val languagesFromApi = mutableStateOf<NetworkResults<ListLanguages>>(NetworkResults.Loading())



    val stickers = mutableStateOf<List<StickerPack>?>(null)
    var stickerByCat = mutableStateOf<List<StickerPack>?>(null)
    val categories = mutableStateOf<List<Category>?>(null)
    val languages = mutableStateOf<List<Languages>?>(null)
    var cid= 0

    private val list = arrayListOf<StickerPack>()

    var detailsPack = mutableStateOf<StickerPack?>(null)

    var index = 0
    var progress = mutableStateOf(0)
    var isReady = mutableStateOf(false)

    fun getStickers(context: Context) = viewModelScope.launch {
        if (!isOnline(context)){
            stickersFromApi.value=NetworkResults.Error("No internet connexion")
            return@launch
        }
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
            Log.d("stickerssi",stickers.value!!.size.toString())
            Hawk.put("sticker_packs", list)
                //Log.d("hwak",Hawk.get<String?>("sticker_packs").toString())

        }
    }

    fun getCategories(context: Context) = viewModelScope.launch {
        if (!isOnline(context)){
            stickersFromApi.value=NetworkResults.Error("No internet connexion")
            return@launch
        }
        if (catsFromApi.value is NetworkResults.Error || catsFromApi.value is NetworkResults.Loading) {
            val response = remote.getCategories()
            val handleCats = HandleResponse(response)
            catsFromApi.value = handleCats.handleResult()
            //Log.d("cats",catsFromApi.value.toString())
        }
        if (catsFromApi.value is NetworkResults.Success) {
            var stickersFromCat= ArrayList<StickerPack>()
            categories.value = catsFromApi.value.data!!.results.shuffled()
            categories.value!!.forEach { category ->
                category.pack.forEach { pack ->
                    val packConverter: StickerPack = StickersUtils.convertStickerToPack(pack)
                    stickersFromCat.add(packConverter)
                }
            }
            //Hawk.deleteAll()
            //Hawk.put("sticker_packs", stickersFromCat.toList())

            stickers.value=stickersFromCat.toList()
            //Log.d("cats",categories.value.toString())
        }
    }

    fun getLanguages(context: Context)=viewModelScope.launch {
        if (!isOnline(context)){
            languagesFromApi.value=NetworkResults.Error("No internet connexion")
            return@launch
        }
        if (languagesFromApi.value is NetworkResults.Error || languagesFromApi.value is NetworkResults.Loading) {
            val response = remote.getLanguages()
            val handleCats = HandleResponse(response)
            languagesFromApi.value = handleCats.handleResult()
            //Log.d("cats",catsFromApi.value.toString())
        }
        if (languagesFromApi.value is NetworkResults.Success) {
            languages.value = languagesFromApi.value.data!!.result
            Log.d("lang",languages.value.toString())
        }

    }

    fun setDetailPack(pack: StickerPack) {
        detailsPack.value = pack
        stickerPackView = pack
        incrementViews(pack.identifier.toInt())
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

    fun saveLanguage(lang:Int)=viewModelScope.launch {
        catsFromApi.value=NetworkResults.Loading()
        categories.value= emptyList()
        stickers.value= emptyList()
        local.saveLanguage(lang)

    }




    fun getLanguage()={}

    fun download() {
        if (index < stickerPackView.stickers.size) {
            var fileName = getLastBitFromUrl(stickerPackView.stickers[index].image_file)
            var url = stickerPackView.stickers[index].image_file
            //Log.d("TGG", url + " ---//-- " + fileName)
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
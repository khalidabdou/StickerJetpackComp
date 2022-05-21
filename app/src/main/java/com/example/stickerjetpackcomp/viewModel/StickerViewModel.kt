package com.example.stickerjetpackcomp.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.stickerjetpackcomp.data.Remote
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


@HiltViewModel
class StickerViewModel @Inject constructor(
    private val remote: Remote
) : ViewModel() {


    private lateinit var stickerPackView: StickerPack

    private val stickersFromApi = mutableStateOf<NetworkResults<Stickers>>(NetworkResults.Loading())

    val stickers = mutableStateOf<List<StickerPack>?>(null)

    private val list = arrayListOf<StickerPack>()

    var detailsPack = mutableStateOf<StickerPack?>(null)

    var index =0
    var progress= mutableStateOf(0)
    var isReady= mutableStateOf(false)

    fun getStickers() = viewModelScope.launch {
        if (stickersFromApi.value is NetworkResults.Error || stickersFromApi.value is NetworkResults.Loading) {
            val response = remote.getStickers()
            val handleStickers = HandleResponse(response)
            stickersFromApi.value = handleStickers.handleResult()
            Log.d("results", stickersFromApi.value.toString())
        }

        if (stickersFromApi.value is NetworkResults.Success) {
            Log.d("results", list.size.toString())
            stickersFromApi.value.data!!.results.forEach { sticker ->
                val pack: StickerPack = StickersUtils.convertStickerToPack(sticker)
                list.add(pack)
            }
            stickers.value = list
            Hawk.put("sticker_packs", list)
        }
    }

    fun setDetailPack(pack: StickerPack) {
        detailsPack.value = pack
        stickerPackView = pack
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
                // Update the progress
                //    binding.  progressBar.max = it.totalBytes.toInt()
                //    progressBar.progress = it.currentBytes.toInt()
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
                        progress.value++
                        download()

                    }
                    override fun onError(error: com.downloader.Error?) {
                        Log.d("TGG", "error $error.")
                    }
                })
        } else {
            Log.d("TGG","finish")
            isReady.value=true
            //openWahatsappActivityForResult()
        }

    }
}
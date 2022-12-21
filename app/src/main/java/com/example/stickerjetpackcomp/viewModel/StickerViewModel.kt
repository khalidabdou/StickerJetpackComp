package com.example.stickerjetpackcomp.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.stickerjetpackcomp.R
import com.example.stickerjetpackcomp.data.Remote
import com.example.stickerjetpackcomp.model.*
import com.example.stickerjetpackcomp.model.AdProvider.Companion.Banner
import com.example.stickerjetpackcomp.model.AdProvider.Companion.BannerFAN
import com.example.stickerjetpackcomp.model.AdProvider.Companion.Inter
import com.example.stickerjetpackcomp.model.AdProvider.Companion.InterFAN
import com.example.stickerjetpackcomp.model.AdProvider.Companion.OpenAd
import com.example.stickerjetpackcomp.model.AdProvider.Companion.Rewarded
import com.example.stickerjetpackcomp.model.AdProvider.Companion.RewardedFAN
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.utils.HandleResponse
import com.example.stickerjetpackcomp.utils.NetworkResults
import com.example.stickerjetpackcomp.utils.StickersUtils
import com.example.stickerjetpackcomp.utils.core.utils.hawk.Hawk
import com.green.china.sticker.core.extensions.others.getLastBitFromUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject


@HiltViewModel
class StickerViewModel @Inject constructor(
    private val remote: Remote,

    ) : ViewModel() {


    private lateinit var stickerPackView: StickerPack


    private val stickersFromApi =
        mutableStateOf<NetworkResults<Categories>>(NetworkResults.Loading())


    private val catsFromApi = mutableStateOf<NetworkResults<Categories>>(NetworkResults.Loading())

    val infos = mutableStateOf<NetworkResults<Ads>>(NetworkResults.Loading())
    val adsList = mutableStateOf<List<Ad>?>(null)
    val apps = mutableStateOf<List<App>?>(null)

    private val _message = MutableStateFlow<String>("Good Morning")
    val message: StateFlow<String> get() = _message

    val stickers = mutableStateOf<List<StickerPack>?>(null)
    var stickerByCat = mutableStateOf<List<StickerPack>?>(null)
    val categories = mutableStateOf<List<Category>?>(null)
    var cid = 0

    private val list = arrayListOf<StickerPack>()

    var detailsPack = mutableStateOf<StickerPack?>(null)

    var index = 0
    var progress = mutableStateOf(0)
    var isReady = mutableStateOf(false)

    fun getStickers(packageName: String) = viewModelScope.launch {
        if (stickersFromApi.value is NetworkResults.Error) {

        }
        if (stickersFromApi.value is NetworkResults.Loading) {
            try {
                val response = remote.getStickers(packageName)
                val handleStickers = HandleResponse(response)
                stickersFromApi.value = handleStickers.handleResult()
            } catch (ex: Exception) {
            }

        }
        if (stickersFromApi.value is NetworkResults.Success) {
            //Log.d("results", list.size.toString())
            categories.value = stickersFromApi.value.data!!.results.shuffled()
            stickersFromApi.value.data!!.results.forEach { cat ->
                cat.pack_stickers.forEach { sticker ->
                    val pack: StickerPack = StickersUtils.convertStickerToPack(sticker)
                    list.add(pack)
                    //Log.d("AN_pack",pack.android_play_store_link)
                }
            }
            stickers.value = list
            Hawk.put("sticker_packs", list)
        }
    }

    fun getAds() = viewModelScope.launch {
        if (infos.value is NetworkResults.Error) {
            //Log.e("ads", ads.value.toString())
        }
        if (infos.value is NetworkResults.Loading) {
            try {
                val response = remote.getAds()
                val handle = HandleResponse(response)
                infos.value = handle.handleResult()

                apps.value=infos.value.data!!.apps
                infos.value.data!!.ads.forEach {
                    when (it.type) {
                        "banner" -> {
                            Banner = it
                            Log.d("ads", Banner.toString())
                        }
                        "inter" -> {
                            Inter = it
                            Log.d("ads", Inter.toString())
                        }
                        "open" -> {
                            OpenAd = it
                            Log.d("ads", OpenAd.toString())
                        }
                        "rewarded" -> {
                            Rewarded = it
                            Log.d("ads", OpenAd.toString())
                        }
                        "banner_fan" -> {
                            Log.d("FAN", it.ad_id)
                            BannerFAN = it
                            //Log.d("ads", Banner.toString())
                        }
                        "inter_fan" -> {
                            InterFAN=it
                            //Log.d("ads", Inter.toString())
                        }
                        "rewarded_fan" -> {
                            RewardedFAN=it
                            //Log.d("ads", Inter.toString())
                        }
                    }
                }

            } catch (ex: Exception) {
            }

        }
        if (stickersFromApi.value is NetworkResults.Success) {
            //Log.d("results", list.size.toString())
            categories.value = stickersFromApi.value.data!!.results.shuffled()
            stickersFromApi.value.data!!.results.forEach { cat ->
                cat.pack_stickers.forEach { sticker ->
                    val pack: StickerPack = StickersUtils.convertStickerToPack(sticker)
                    list.add(pack)
                    //Log.d("AN_pack",pack.android_play_store_link)
                }
            }
            stickers.value = list
            Hawk.put("sticker_packs", list)
        }
    }

    /*  fun getCategories() = viewModelScope.launch {
          Log.d("cats","begin")
          if (catsFromApi.value is NetworkResults.Error || catsFromApi.value is NetworkResults.Loading) {
              val response = remote.getCategories()
              val handleCats = HandleResponse(response)
              catsFromApi.value = handleCats.handleResult()
              Log.d("cats",catsFromApi.value.toString())
          }
          if (catsFromApi.value is NetworkResults.Success) {
              categories.value = catsFromApi.value.data!!.results.shuffled()
              Log.d("cats",categories.value.toString())
          }
      }*/

    fun setDetailPack(pack: StickerPack) {
        detailsPack.value = pack
        stickerPackView = pack
    }

    fun incrementAddToWhatsapp(identifier: Int) {
        viewModelScope.launch {
            try {
                remote.incrementStickerAddTo(identifier)
            } catch (ex: Exception) {
            }

        }
    }

    fun incrementView(identifier: Int) {
        viewModelScope.launch {
            try {
                remote.incrementStickerViews(identifier)
            } catch (ex: Exception) {
            }

        }
    }


    fun stickersByCat() {
        stickerByCat.value = stickers.value!!.filter { stickerPack -> stickerPack.catId == cid }
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

                    //Log.d("TGG", "download " + index)
                    //Log.d("TGG", file.exists().toString())
                    //Log.d("TGG", file.absolutePath.toString())
                    //Log.d("TGG",stickerPackView.stickers[index].image_file)
                    index++
                    progress.value = (index * 100 / detailsPack.value!!.stickers.size)
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


    fun setMessage(context: Context) {
        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)
        when (timeOfDay) {
            in 0..11 -> {
                _message.value = context.getString(R.string.morning)
            }
            in 12..15 -> {
                _message.value = context.getString(R.string.afternoon)
            }
            in 16..20 -> {
                _message.value = context.getString(R.string.evening)
            }
            in 21..23 -> {
                _message.value = context.getString(R.string.night)
            }

        }
//        if (timeOfDay in 0..11) {
//            _message.value = context.getString(R.string.morning)
//            //message = "Good Morning"
//        } else if (timeOfDay in 12..15) {
//            _message.value = context.getString(R.string.afternoon)
//            //message = "Good Afternoon"
//        } else if (timeOfDay in 16..20) {
//            _message.value = context.getString(R.string.evening)
//            //message = "Good Evening"
//        } else if (timeOfDay in 21..23) {
//            _message.value = context.getString(R.string.night)
//            //message = "Good Night"
//        }
    }
}
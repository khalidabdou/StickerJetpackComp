package com.wishes.jetpackcompose.admob

import android.app.Activity
import android.view.View.GONE
import android.widget.FrameLayout
import com.example.stickerjetpackcomp.model.AdProvider.Companion.InterFAN
import com.facebook.ads.*



class Facebook {


    companion object {

        lateinit var interstitialAd: InterstitialAd

        fun showInterstitial(activity: Activity) {
            if (!this::interstitialAd.isInitialized) {
                loadInterstitialFAN(activity)
                return
            }
            if (!interstitialAd.isAdLoaded) {
                loadInterstitialFAN(activity)
                return
            }
            if (countShow % InterFAN.show_count!! != 0) {
                return
            }
            interstitialAd.show()


        }

        fun loadInterstitialFAN(activity: Activity) {
            interstitialAd = InterstitialAd(
                activity,
                InterFAN.ad_id
            )
            val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
                override fun onError(p0: Ad?, p1: AdError?) {

                }

                override fun onAdLoaded(inter: Ad?) {

                }

                override fun onAdClicked(p0: Ad?) {

                }

                override fun onLoggingImpression(p0: Ad?) {

                }

                override fun onInterstitialDisplayed(p0: Ad?) {

                }

                override fun onInterstitialDismissed(p0: Ad?) {

                }
            }
            interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                    .withAdListener(interstitialAdListener)
                    .build()
            );

        }

    }


}
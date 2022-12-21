package com.ringtones.compose.feature.admob


import android.content.Context
import android.util.Log
import com.example.stickerjetpackcomp.model.AdProvider
import com.example.stickerjetpackcomp.model.AdProvider.Companion.Rewarded
import com.example.stickerjetpackcomp.model.AdProvider.Companion.RewardedFAN
import com.example.stickerjetpackcomp.utils.findActivity
import com.facebook.ads.Ad
import com.facebook.ads.RewardedVideoAd
import com.facebook.ads.RewardedVideoAdListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


private var mRewardedAd: RewardedAd? = null

private val TAG = "ADFAN"
lateinit var rewardedVideoAd: RewardedVideoAd

fun loadRewarded(context: Context) {

    if (!Rewarded.ad_status) {
        loadRewardedFAN(context)
        return
    }

    var adRequest = AdRequest.Builder().build()
    RewardedAd.load(
        context,
        Rewarded.ad_id,
        adRequest,
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                //Log.d(TAG, adError?.toString())
                mRewardedAd = null
                if (RewardedFAN.ad_status)
                    loadRewardedFAN(context)
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                //Log.d(TAG, "Ad was loaded.")
                mRewardedAd = rewardedAd
            }
        })
}

fun showRewardedAdmob(context: Context) {


    val activity = context.findActivity()
    mRewardedAd?.show(activity!!, OnUserEarnedRewardListener {
        fun onUserEarnedReward(rewardItem: RewardItem) {
            var rewardAmount = rewardItem.amount
            var rewardType = rewardItem.type
            //Log.d(TAG, "User earned the reward.")
        }
    })
    mRewardedAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdClicked() {
            // Called when a click is recorded for an ad.
            Log.d("MainActivity", "Ad was clicked.")
        }

        override fun onAdDismissedFullScreenContent() {
            // Called when ad is dismissed.
            // Set the ad reference to null so you don't show the ad a second time.
            Log.d("MainActivity", "Ad dismissed fullscreen content.")
            mRewardedAd = null
            loadRewarded(context)
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            // Called when ad fails to show.
            Log.e("MainActivity", "Ad failed to show fullscreen content.")
            mRewardedAd = null
        }

        override fun onAdImpression() {
            // Called when an impression is recorded for an ad.
            Log.d("MainActivity", "Ad recorded an impression.")
        }

        override fun onAdShowedFullScreenContent() {
            // Called when ad is shown.
            Log.d("MainActivity", "Ad showed fullscreen content.")
        }
    }


}

fun loadRewardedFAN(context: Context) {
    if (!RewardedFAN.ad_status) {
        return
    }

    rewardedVideoAd = RewardedVideoAd(context, AdProvider.RewardedFAN.ad_id)

    val rewardedVideoAdListener: RewardedVideoAdListener = object : RewardedVideoAdListener {
        override fun onError(p0: Ad?, p1: com.facebook.ads.AdError?) {
            Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!")
        }


        override fun onAdLoaded(ad: Ad?) {
            // Rewarded video ad is loaded and ready to be displayed
            Log.d(TAG, "Rewarded video ad is loaded and ready to be displayed!")
        }

        override fun onAdClicked(ad: Ad?) {
            // Rewarded video ad clicked
            Log.d(TAG, "Rewarded video ad clicked!")
        }

        override fun onLoggingImpression(ad: Ad?) {
            // Rewarded Video ad impression - the event will fire when the
            // video starts playing
            Log.d(TAG, "Rewarded video ad impression logged!")
        }

        override fun onRewardedVideoCompleted() {
            // Rewarded Video View Complete - the video has been played to the end.
            // You can use this event to initialize your reward
            Log.d(TAG, "Rewarded video completed!")

            // Call method to give reward
            // giveReward();
        }

        override fun onRewardedVideoClosed() {
            // The Rewarded Video ad was closed - this can occur during the video
            // by closing the app, or closing the end card.
            if (!rewardedVideoAd.isAdLoaded) {
                loadRewardedFAN(context)
            }
            Log.d(TAG, "Rewarded video ad closed!")
        }

    }

    rewardedVideoAd.loadAd(
        rewardedVideoAd.buildLoadAdConfig()
            .withAdListener(rewardedVideoAdListener)
            .build()
    );
}


fun showRewardedFAN() {
    try {
        if (rewardedVideoAd.isAdLoaded) {
            rewardedVideoAd.show()
        }
    }catch (ex:Exception){}

}

fun showRewarded(context: Context) {
    if (mRewardedAd != null) {
        showRewardedAdmob(context)
        return
    }

    showRewardedFAN()

}


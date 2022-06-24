package com.example.stickerjetpackcomp.utils

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.text.TextUtils
import android.util.Log
import com.example.stickerjetpackcomp.BuildConfig
import com.green.china.sticker.core.extensions.others.getLastBitFromUrl
import com.example.stickerjetpackcomp.sticker.StickerPack
import com.example.stickerjetpackcomp.utils.core.utils.hawk.Hawk
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class StickerContentProvider : ContentProvider() {
    private lateinit var packList: ArrayList<StickerPack>

    override fun onCreate(): Boolean {
        Hawk.init(context).build()
        val authority = BuildConfig.CONTENT_PROVIDER_AUTHORITY
        check(authority.startsWith(context!!.packageName)) { "your authority (" + authority + ") for the content provider should start with your package name: " + context!!.packageName }

        MATCHER.addURI(authority, METADATA, METADATA_CODE)
        MATCHER.addURI(authority, "$METADATA/*", METADATA_CODE_FOR_SINGLE_PACK)
        MATCHER.addURI(authority, "$STICKERS/*", STICKERS_CODE)
        packList = ArrayList()
        packList = getStickerPackList()


        for (stickerPack in packList) {
            Log.e("stickerPack", stickerPack.toString())
            MATCHER.addURI(
                authority,
                STICKERS_ASSET + "/" + stickerPack.identifier + "/" + getLastBitFromUrl(stickerPack.tray_image_file),
                STICKER_PACK_TRAY_ICON_CODE
            )
            for (sticker in stickerPack.stickers) {
                MATCHER.addURI(
                    authority,
                    STICKERS_ASSET + "/" + stickerPack.identifier + "/" + getLastBitFromUrl(sticker.image_file),
                    STICKERS_ASSET_CODE
                )
            }
        }
        return true
    }

    @SuppressLint("LongLogTag")
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor {
        Log.e("MATCHER", MATCHER.toString())
        return when (MATCHER.match(uri)) {
            METADATA_CODE -> {
                Log.e("METADATA_CODE", METADATA_CODE.toString())

                getPackForAllStickerPacks(uri)
            }
            METADATA_CODE_FOR_SINGLE_PACK -> {
                Log.e("METADATA_CODE_FOR_SINGLE_PACK", METADATA_CODE_FOR_SINGLE_PACK.toString())
                getCursorForSingleStickerPack(uri)
            }
            STICKERS_CODE -> {
                Log.e("STICKERS_CODE", STICKERS_CODE.toString())
                getStickersForAStickerPack(uri)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }


    @Throws(FileNotFoundException::class)
    override fun openAssetFile(uri: Uri, mode: String): AssetFileDescriptor? {
        MATCHER.match(uri)
        val matchCode = MATCHER.match(uri)
        val pathSegments = uri.pathSegments
        Log.e(
            TAG, """
     openFile: $matchCode$uri
     ${uri.authority}
     ${pathSegments[pathSegments.size - 3]}/
     ${pathSegments[pathSegments.size - 2]}/
     ${pathSegments[pathSegments.size - 1]}
     """.trimIndent()
        )

        return getImageAsset(uri)
    }

    @Throws(IllegalArgumentException::class)
    private fun getImageAsset(uri: Uri): AssetFileDescriptor? {
        Log.e("getImageAsset", uri.toString())

        val pathSegments = uri.pathSegments
        require(pathSegments.size == 3) { "path segments should be 3, uri is: $uri" }

        val fileName = pathSegments[pathSegments.size - 1]
        val identifier = pathSegments[pathSegments.size - 2]
        require(!TextUtils.isEmpty(identifier)) { "identifier is empty, uri: $uri" }
        require(!TextUtils.isEmpty(fileName)) { "file name is empty, uri: $uri" }
        if (packList.isEmpty())
            packList = getStickerPackList()
        Log.d("TGGG",getStickerPackList().size.toString())
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier.toString()) {
                if (fileName == getLastBitFromUrl(stickerPack.tray_image_file)) {
                    return fetchFile(uri, fileName, identifier)
                } else {
                    for (sticker in stickerPack.stickers) {
                        if (fileName == getLastBitFromUrl(sticker.image_file)) {
                            return fetchFile(uri, fileName, identifier)
                        }
                    }
                }
            }
        }
        Log.e("return_null", identifier)

        return null
    }

    private fun fetchFile(uri: Uri, fileName: String, identifier: String): AssetFileDescriptor? {
        try {

            val file = File(
                context!!.filesDir.toString() + "/" + "stickers_asset" + "/" + identifier + "/",
                fileName
            )

            if (!file.exists()) {
                val file1 = File(
                    context!!.filesDir.toString() + "/" + "stickers_asset" + "/" + identifier,
                    "im.png"
                )
                Log.d("fetFile", "StickerPack dir not found ${file.absoluteFile}")
                return AssetFileDescriptor(
                    ParcelFileDescriptor.open(
                        file1,
                        ParcelFileDescriptor.MODE_READ_ONLY
                    ), 0L, -1L
                )
            }
            Log.d("fetchFile", "StickerPack " + file.path)

            return AssetFileDescriptor(
                ParcelFileDescriptor.open(
                    file,
                    ParcelFileDescriptor.MODE_READ_ONLY
                ), 0L, -1L
            )
        } catch (e: IOException) {
            Log.e(context!!.packageName, "IOException when getting asset file, uri:$uri", e)
            return null
        }

//        val file: File = if (fileName.endsWith(".png")) {
//            File(
//                StickersFragment.path + "/" + identifier + "/try/",
//                fileName
//            )
//        } else {
//            File(
//                "${StickersFragment.path}/$identifier/",
//                fileName
//            )
//        }
        //E/fetchFile: StickerPack /data/user/0/com.green.photoschina/files/stickers_asset/10/try/im.png
        //StickerPack /data/user/0/com.green.photoschina/files/stickers_asset/7/try/im.png
        /*
        E/fetchFile: StickerPack /data/user/0/com.green.photoschina/files/stickers_asset/7/sticker_25.webp

        E/fetchFile: StickerPack /data/user/0/com.green.photoschina/files/stickers_asset/7/sticker_26.webp

        E/fetchFile: StickerPack /data/user/0/com.green.photoschina/files/stickers_asset/7/sticker_27.webp

         */
//        if (!file.exists()) {
//            Log.e("fetFile", "StickerPack dir not found")
//        }
//        Log.e("fetchFile", "StickerPack " + file.path)
//        return AssetFileDescriptor(
//            ParcelFileDescriptor.open(
//                file,
//                ParcelFileDescriptor.MODE_READ_ONLY
//            ), 0L, -1L
//        )

    }


    override fun getType(uri: Uri): String =
        when (MATCHER.match(uri)) {
            METADATA_CODE -> "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA
            METADATA_CODE_FOR_SINGLE_PACK -> "vnd.android.cursor.item/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + METADATA
            STICKERS_CODE -> "vnd.android.cursor.dir/vnd." + BuildConfig.CONTENT_PROVIDER_AUTHORITY + "." + STICKERS
            STICKERS_ASSET_CODE -> "image/png"
            STICKER_PACK_TRAY_ICON_CODE -> "image/png"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

    private fun getStickerPackList(): ArrayList<StickerPack> =
        Hawk.get("sticker_packs", ArrayList())



    private fun getPackForAllStickerPacks(uri: Uri): Cursor =
        getStickerPackInfo(uri, packList)

    @SuppressLint("LongLogTag")
    private fun getCursorForSingleStickerPack(uri: Uri): Cursor {

        val identifier = uri.lastPathSegment
        Log.e("getCursorForSingleStickerPack", identifier.toString())
        Log.e("packList", packList.size.toString())
        if (packList.size == 0) {
            packList = getStickerPackList()
        }
        for (stickerPack in packList) {
            if (identifier == stickerPack.identifier) {
                Log.e("getCursorForSingleStickerPack", "founded")
                return getStickerPackInfo(uri, listOf(stickerPack))
            }
        }
        throw IllegalArgumentException("getCursorForSingleStickerPack: $uri")
        return getStickerPackInfo(uri, ArrayList())
    }

    private fun getStickerPackInfo(uri: Uri, stickerPackList: List<StickerPack>): Cursor {
        val cursor = MatrixCursor(
            arrayOf(
                STICKER_PACK_IDENTIFIER_IN_QUERY,
                STICKER_PACK_NAME_IN_QUERY,
                STICKER_PACK_PUBLISHER_IN_QUERY,
                STICKER_PACK_ICON_IN_QUERY,
                ANDROID_APP_DOWNLOAD_LINK_IN_QUERY,
                IOS_APP_DOWNLOAD_LINK_IN_QUERY,
                PUBLISHER_EMAIL,
                PUBLISHER_WEBSITE,
                PRIVACY_POLICY_WEBSITE,
                LICENSE_AGREENMENT_WEBSITE,
                ANIMATED_STICKER_PACK,
            )
        )
        for (stickerPack in stickerPackList) {
            Log.e("stickerProvider", stickerPack.toString())
            val builder = cursor.newRow()
            builder.add(stickerPack.identifier)
            builder.add(stickerPack.name)
            builder.add(stickerPack.publisher)
            builder.add(getLastBitFromUrl(stickerPack.tray_image_file))
            builder.add(stickerPack.android_play_store_link)
            builder.add(stickerPack.ios_app_store_link)
            builder.add(stickerPack.publisher_email)
            builder.add(stickerPack.publisher_website)
            builder.add(stickerPack.privacy_policy_website)
            builder.add(stickerPack.license_agreement_website)
            builder.add(if (stickerPack.animated_sticker_pack) 1 else 0)
        }

        Log.e(TAG, "getStickerPackInfo: " + stickerPackList.size)
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    private fun getStickersForAStickerPack(uri: Uri): Cursor {
        val emojis: List<String> = arrayListOf("", "")
        val identifier = uri.lastPathSegment
        val cursor = MatrixCursor(arrayOf(STICKER_FILE_NAME_IN_QUERY, STICKER_FILE_EMOJI_IN_QUERY))
        for (stickerPack in getStickerPackList()) {
            if (identifier == stickerPack.identifier) {
                for (sticker in stickerPack.stickers) {
                    cursor.addRow(
                        arrayOf<Any>(
                            getLastBitFromUrl(sticker.image_file),
                            TextUtils.join(",", emojis)
                        )
                    )
                }
            }
        }
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int =
        throw UnsupportedOperationException("Not supported")

    override fun insert(uri: Uri, values: ContentValues?): Uri =
        throw UnsupportedOperationException("Not supported")

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int =
        throw UnsupportedOperationException("Not supported")

    private

    companion object {
        const val STICKER_PACK_IDENTIFIER_IN_QUERY = "sticker_pack_identifier"
        const val STICKER_PACK_NAME_IN_QUERY = "sticker_pack_name"
        const val STICKER_PACK_PUBLISHER_IN_QUERY = "sticker_pack_publisher"
        const val STICKER_PACK_ICON_IN_QUERY = "sticker_pack_icon"
        const val ANDROID_APP_DOWNLOAD_LINK_IN_QUERY = "android_play_store_link"
        const val IOS_APP_DOWNLOAD_LINK_IN_QUERY = "ios_app_download_link"
        const val PUBLISHER_EMAIL = "sticker_pack_publisher_email"
        const val PUBLISHER_WEBSITE = "sticker_pack_publisher_website"
        const val PRIVACY_POLICY_WEBSITE = "sticker_pack_privacy_policy_website"
        const val LICENSE_AGREENMENT_WEBSITE = "sticker_pack_license_agreement_website"
        const val STICKER_FILE_NAME_IN_QUERY = "sticker_file_name"
        const val STICKER_FILE_EMOJI_IN_QUERY = "sticker_emoji"
        private val TAG = StickerContentProvider::class.java.simpleName
        private val MATCHER = UriMatcher(UriMatcher.NO_MATCH)
        const val METADATA = "metadata"
        private const val METADATA_CODE = 1
        private const val METADATA_CODE_FOR_SINGLE_PACK = 2
        const val STICKERS = "stickers"
        private const val STICKERS_CODE = 3
        const val STICKERS_ASSET = "stickers_asset"
        private const val STICKERS_ASSET_CODE = 4
        private const val STICKER_PACK_TRAY_ICON_CODE = 5
        val ANIMATED_STICKER_PACK = "animated_sticker_pack"

    }
}
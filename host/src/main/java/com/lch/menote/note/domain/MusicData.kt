package com.lch.menote.note.domain

/**
 * Created by Administrator on 2017/9/25.
 */
data class MusicData internal constructor(val links: MutableList<LinkData>, var tuneTexts: String, var uid: String) {
    override fun hashCode(): Int {

        return uid.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        val that = other as? MusicData ?: return false
        return uid == that.uid
    }
}

data class LinkData(val data: MusicData, val text: String? = null) {

    override fun hashCode(): Int {
        return data.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        val that = other as? LinkData ?: return false
        return data == that.data
    }
}
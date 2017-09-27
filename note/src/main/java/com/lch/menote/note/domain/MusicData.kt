package com.lch.menote.note.domain

/**
 * Created by Administrator on 2017/9/25.
 */
data class MusicData internal constructor(val links: MutableList<LinkData>, val tuneTexts: String)

data class LinkData(val id: Int, val text: String? = null) {

    override fun hashCode(): Int {
        return id
    }

    override fun equals(other: Any?): Boolean {
        val that = other as? LinkData ?: return false
        return id == that.id
    }
}
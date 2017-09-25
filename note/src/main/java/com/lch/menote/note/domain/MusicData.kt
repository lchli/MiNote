package com.lch.menote.note.domain

/**
 * Created by Administrator on 2017/9/25.
 */
data class MusicData(val links:List<LinkData>?,val resID:Int)

data class LinkData(val id:Int,val text:String?=null)
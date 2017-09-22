package com.lch.menote.note.domain

import com.lchli.pinedrecyclerlistview.library.ListSectionData

/**
 * Created by Administrator on 2017/9/22.
 */
internal data class NotePinedData(val viewType: Int, val noteType: String): ListSectionData(viewType)
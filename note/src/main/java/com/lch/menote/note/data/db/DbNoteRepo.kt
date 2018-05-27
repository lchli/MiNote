package com.lch.menote.note.data.db

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import com.lch.menote.common.toast
import com.lch.menote.note.data.NoteSource
import com.lch.menote.note.data.db.gen.NoteDao
import com.lch.menote.note.domain.Mapper
import com.lch.menote.note.domain.Note
import com.lch.menote.note.domain.NoteModel
import com.lch.menote.note.domain.QueryNoteResponse
import com.lch.netkit.common.mvc.MvcError
import com.lch.netkit.common.mvc.ResponseValue

/**
 * Created by Administrator on 2017/9/22.
 */
class DbNoteRepo(private val context: Context) : NoteSource {

    override fun queryNotes(tag: String?, title: String?, sortTimeAsc: Boolean,useId:String): ResponseValue<QueryNoteResponse> {
        val res=ResponseValue<QueryNoteResponse>()

        try {

            val builder = Dsession.noteDao(context).queryBuilder()
            if (!TextUtils.isEmpty(title)) {
                builder.where(NoteDao.Properties.Title.like("%$title%"))
            }

            if (!TextUtils.isEmpty(tag)) {
                builder.where(NoteDao.Properties.Type.eq(tag))
            }

            if (sortTimeAsc) {
                builder.orderAsc(NoteDao.Properties.Type).orderAsc(NoteDao.Properties.LastModifyTime);
            } else {
                builder.orderAsc(NoteDao.Properties.Type).orderDesc(NoteDao.Properties.LastModifyTime);
            }

            var r= QueryNoteResponse()
            var models= mutableListOf<NoteModel>()

           var notes= builder.list()
            if(notes!=null){
                for( n in notes){
                    models.add(Mapper.from(n))
                }
            }

            r.data=models

            res.data= r

        } catch (e: Exception) {
            e.printStackTrace()
            detectLockError(e)
            res.err= MvcError(e.message)
        }

        return res
    }

    override fun save(note: NoteModel):ResponseValue<Void> {
        var ret=ResponseValue<Void>()

        try {

            Dsession.noteDao(context).insertOrReplace(Mapper.from(note))
        } catch (e: Exception) {
            e.printStackTrace()
            detectLockError(e)
            ret.setErrMsg(e.message)
        }

        return ret
    }

    override fun delete(note: Note) {
        try {
            Dsession.noteDao(context).delete(note)
        } catch (e: Exception) {
            e.printStackTrace()
            detectLockError(e)
        }
    }

    private fun detectLockError(e: Exception) {
        if (e.message?.contains("encrypted") == true) {
            context.toast("数据解锁失败！如果忘记密码只能重置", Toast.LENGTH_LONG)
        }
    }
}
package com.lch.menote

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bl.wst.R
import kotlinx.coroutines.*

class KotlinActivity : AppCompatActivity() {
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
      val tv= findViewById<TextView>(R.id.tv)


        scope.launch(Dispatchers.Main) {

            val user = withContext(Dispatchers.IO) {//这个执行完成才会执行下面对job。
                delay(10_000)
                println("1 finished")
                1
            }
            val currentFriends = async(Dispatchers.IO) {//这个启动后马上执行后面的。
                delay(10_000)
                println("2 finished")
                2
            }
            val suggestedFriends = async(Dispatchers.IO) {//这个启动后马上执行后面的。
                delay(10_000)
                println("3 finished")
                3
            }

            val finalUser = currentFriends.await() + suggestedFriends.await()//等待结果，但是不会阻塞ui。
            tv.text="ct${finalUser}"
            println("all finished")
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}

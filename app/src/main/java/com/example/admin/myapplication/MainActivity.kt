package com.example.admin.myapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    //可变变量定义：var 关键字  var <标识符> : <类型> = <初始化值>
    var a: Int = 0;
    //不可变变量定义：val 关键字，只能赋值一次的变量(类似Java中final修饰的变量)
    val b: Int = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_show.setText(printText(a,b).toString())
    }

    fun printText(a: Int, b: Int): Int {
        return a + b;
    }
}

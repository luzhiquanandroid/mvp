package com.nsbd.zc.shapetv

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.noober.background.BackgroundLibrary

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        BackgroundLibrary.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

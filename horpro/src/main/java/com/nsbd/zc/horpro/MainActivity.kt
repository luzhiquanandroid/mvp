package com.nsbd.zc.horpro

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    var p: Int = 800
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        Log.e("p", p1.toString());
        if (p1 <= 800) {
            seekBar.progress = 800
        }
        if (800 < p1 && p1 < seekBar.max) {
            p = (p1 / 100) * 100 + 100
        }
        Log.e("p1", p.toString());
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //pb.progress = (2 * 100 / 7).toInt()
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setPadding(0, 0, 0, 0);
        seekBar.thumbOffset = 0;

        seekBar.progress = p
    }
}


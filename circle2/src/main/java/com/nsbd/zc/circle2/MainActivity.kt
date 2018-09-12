package com.nsbd.zc.circle2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.widget.Toast
import com.nsbd.zc.circle2.View.CircleView
import com.nsbd.zc.circle2.View.ProgressBarView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var startF: Float = 0f
    var endF: Float = -30f
    var animStart: Float = 30f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_click.setOnClickListener {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
            //circleView.startCustomAnimation(animStart)
            circleView.setValue(animStart)
            rotateAnim(startF, endF)
            startF = endF
            endF += -30f
            animStart += 30f
        }


        val view = findViewById<View>(R.id.progress) as ProgressBarView
        view.setDraggingEnabled(true)
        view.setMax(1000)
    }

    private fun rotateAnim(startF: Float, endF: Float) {
        val animationSet = AnimationSet(true)
        val rotateAnimation = RotateAnimation(startF, endF, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        rotateAnimation.duration = 1000
        rotateAnimation.fillAfter = true
        animationSet.addAnimation(rotateAnimation)
        circle.startAnimation(rotateAnimation)
    }
}

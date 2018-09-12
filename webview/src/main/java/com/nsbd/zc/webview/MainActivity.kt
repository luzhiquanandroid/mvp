package com.nsbd.zc.webview

import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import com.nsbd.zc.webview.fragments.HomeFragment
import com.nsbd.zc.webview.fragments.MeFragment
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    var sparseArray: SparseArray<Fragment>? = null

    companion object {
        const val HOME: Int = 0
        const val ME: Int = 1
    }

    @Type
    var mType: Int = -1

    @IntDef(HOME, ME)
    annotation class Type {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
    }

    private fun initData() {
        if (sparseArray == null) {
            sparseArray = SparseArray<Fragment>()
            sparseArray!!.put(HOME, HomeFragment())
            sparseArray!!.put(ME, MeFragment())
        }
        if (intent != null && intent.extras != null) {
            when (intent.extras.get("KEY_TYPE")) {
                HOME -> showFragment(HOME)
                ME -> showFragment(ME)
                else -> showFragment(HOME)
            }
        } else {
            showFragment(HOME)
            Thread(Runnable {
                Thread.sleep(3000)
                showFragment(ME)
            }).start()
        }
    }

    private fun initView() {
    }

    private fun showFragment(@Type type: Int) {
        if (sparseArray == null || mType == type) {
            return
        }
        val transaction = supportFragmentManager.beginTransaction()
       // transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.setCustomAnimations(R.anim.push_right_out,R.anim.push_right_in)
        if (sparseArray!!.get(mType) != null) {
            transaction.hide(sparseArray!!.get(mType))
        }
        mType = type
        val f = sparseArray!!.get(mType)
        if (f != null) {
            if (f!!.isAdded) {
                transaction.show(f)
            } else {
                transaction.add(R.id.frameLayout, f)
            }
        }
        transaction.commitAllowingStateLoss()
    }


}

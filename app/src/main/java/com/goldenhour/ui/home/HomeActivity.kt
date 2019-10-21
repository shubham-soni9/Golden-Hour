package com.goldenhour.ui.home

import android.os.Bundle
import com.goldenhour.R
import com.goldenhour.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        init()
    }

    private fun init() {
        setSupportActionBar(toolbar)
    }
}

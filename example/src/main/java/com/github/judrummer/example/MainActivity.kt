package com.github.judrummer.example

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        btnSimple.setOnClickListener {
            startActivity(Intent(this@MainActivity, SimpleActivity::class.java))
        }
        btnRxJava.setOnClickListener {
            startActivity(Intent(this@MainActivity, RxJavaActivity::class.java))
        }
    }

}

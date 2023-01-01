package com.harshul.permissionsapp

import android.app.Application
import com.chibatching.kotpref.Kotpref

class AppController : Application(){

    override fun onCreate() {
        super.onCreate()
        Kotpref.init(this)
    }
}
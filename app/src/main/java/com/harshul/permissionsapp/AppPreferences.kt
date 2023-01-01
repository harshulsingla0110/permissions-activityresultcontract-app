package com.harshul.permissionsapp

import com.chibatching.kotpref.KotprefModel

object AppPreferences : KotprefModel() {

    var cameraPermissionDeniedOnce by booleanPref()
}
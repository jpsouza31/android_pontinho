package com.example.pontinho

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permission {

    companion object{
        fun validPermissions(permissions: Array<String>, activity: Activity) : Boolean{
            if(Build.VERSION.SDK_INT >= 23){
                val permissionsList: ArrayList<String> = ArrayList<String>()

                permissions.forEach {
                    val hasPermission: Boolean = ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
                    if (!hasPermission) permissionsList.add(it)
                }

                if(permissionsList.isEmpty()) return true

                ActivityCompat.requestPermissions(activity, permissionsList.toTypedArray(), 1)
            }

            return true
        }
    }

}
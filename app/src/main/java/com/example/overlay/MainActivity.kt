package com.example.overlay

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Switch
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationManagerCompat

import android.app.NotificationManager

import android.app.NotificationChannel

import android.content.Context
import android.widget.CompoundButton

import androidx.core.app.NotificationCompat




class MainActivity : AppCompatActivity() {

    lateinit var btn : Button
    lateinit var switch1 : Switch
    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        checkPermissionForOverLay()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeNotification()
    }

    private fun init(){
        btn = findViewById(R.id.btn1);
        switch1 = findViewById(R.id.switch1)
        btn.setOnClickListener(btnListener)
        switch1.setOnCheckedChangeListener(switchListener)

        /*
         * startActivity(Intent intent) : new Activity
         * startActivityForResult(Intent intent, int requestCode) : new Activity, return result - deprecated
         * registerForActivityResult - ActivityResultContract, ActivityResultCallback
        */
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult>(){ result ->
                // ? - nullable
                if(result.resultCode == RESULT_CANCELED){
                    alertDialogForOverlayPermission()
                }
            })
    }

    /*
    * . (Dot)
    * Java Reflection
    * :: (Double Colon)
    * Kotlin Reflection
    * */
    private var btnListener = View.OnClickListener { v ->
        if(v.id == R.id.btn1){

        }
    }

    private var switchListener = CompoundButton.OnCheckedChangeListener{
        // v - CompoundButton switch
        // c - boolean check
        v, c ->
        if(v.id == R.id.switch1){
            if(c){
                createNotification()
                startLayoutService()
            }else{
                removeNotification()
            }
        }
    }
    private fun checkPermissionForOverLay(){
        // SDK VERSION ( >= Marshmallow)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Overlay
            if(!Settings.canDrawOverlays(this)){
                var intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                activityResultLauncher.launch(intent)
            }else{

            }
        }
    }

    private fun alertDialogForOverlayPermission(){
        var builder = AlertDialog.Builder(this)
        builder.setTitle("권한 요청").setMessage("앱을 사용하려면 '다른 앱 위에 표시(오버레이)' 권한이 필요합니다")
        builder.setPositiveButton("취소"){
                _, _ ->
            finish()
        }
        builder.setNegativeButton("확인"){
            _, _ ->
            checkPermissionForOverLay()
        }
        var alertDialog = builder.create()
        alertDialog.show()
    }

    private fun createNotification() {
        val builder = NotificationCompat.Builder(this, "default")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Title")
        builder.setContentText("content text")
        builder.setAutoCancel(false)
        builder.setOngoing(true)
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel("default","default", NotificationManager.IMPORTANCE_DEFAULT))
        }

        notificationManager.notify(1, builder.build())
    }

    private fun removeNotification() {
        NotificationManagerCompat.from(this).cancel(1)
    }

    private fun startLayoutService(){
        startService(Intent(this, aService::class.java))
    }
}
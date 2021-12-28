package com.example.overlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.widget.Button
import android.widget.ImageView

class aService : Service() {

    lateinit var wm:WindowManager
    lateinit var v: View

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init(){
        wm = getSystemService(WINDOW_SERVICE) as WindowManager

        var params = WindowManager.LayoutParams(
            //width, height
            100, 100,
            // xpos, ypos
            20, 20,
            // _type
            TYPE_APPLICATION_OVERLAY,
            // _flags
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE and
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL and
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            // _format
            // TRANSLUCENT,TRANSPARENT : 투명한
            PixelFormat.TRANSLUCENT
        )

        // inflate layout_aservice.xml
        var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        v = inflater.inflate(R.layout.layout_aservice, null)
        var btn1: Button = v.findViewById(R.id.btn1_aserv)
        var img1: ImageView = v.findViewById(R.id.img1_aserv)
        btn1.setOnClickListener(btnClicklistener)

        wm.addView(v, params)
    }

    private var btnClicklistener = View.OnClickListener { view ->
        if(view.id == R.id.btn1_aserv){
            // do something....
            wm.removeView(v)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wm.removeView(v)
    }
}
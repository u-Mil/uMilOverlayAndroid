package com.example.overlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
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
        var params = WindowManager.LayoutParams(
            // SDK_INT >= O
            TYPE_APPLICATION_OVERLAY,
            // _flags
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            // _format
            // TRANSLUCENT,TRANSPARENT : 투명한
            PixelFormat.TRANSLUCENT
        )
        params.width = 300
        params.height = 300
        params.gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
        // inflate layout_aservice.xml
        var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        v = inflater.inflate(R.layout.layout_aservice, null)
        v.setOnTouchListener(viewTouchListener)
        var btn1: Button = v.findViewById(R.id.btn1_aserv)
        var img1: ImageView = v.findViewById(R.id.img1_aserv)
        btn1.setOnClickListener(btnClicklistener)

        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.addView(v, params)
    }

    private var btnClicklistener = View.OnClickListener { v ->
        if(v.id == R.id.btn1_aserv){
            // do something....
            wm.removeView(v)
        }
    }


    private var x_val = 0.0f
    private var y_val = 0.0f
    private var viewTouchListener = View.OnTouchListener { v, event ->
        if(v.id == R.id.layout_aserv){
            when(event.action){
                MotionEvent.ACTION_DOWN -> {
                    x_val = v.x - event.rawX
                    y_val = v.y - event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    v.animate().x(event.rawX + x_val)
                        .y(event.rawY + y_val)
                        .setDuration(0)
                        .start()
                }
                MotionEvent.ACTION_UP -> {

                }
            }
            true
        }
        true
    }

    override fun onDestroy() {
        super.onDestroy()
        wm.removeView(v)
    }
}
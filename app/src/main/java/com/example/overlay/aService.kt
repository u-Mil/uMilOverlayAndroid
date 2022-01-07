package com.example.overlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.opengl.Visibility
import android.os.Build
import android.os.IBinder
import android.view.*
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout

class aService : Service() {

    lateinit var wm : WindowManager
    lateinit var v : View
    lateinit var rel_v : RelativeLayout
    lateinit var params : WindowManager.LayoutParams
    lateinit var inflater : LayoutInflater
    private var x_val = 0.0f
    private var y_val = 0.0f

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun init(){
        setLayoutParamsToInit()
        setLayoutInflater()
        initInflatedViews()
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        wm.addView(v, params)
    }

    private var btnClicklistener = View.OnClickListener { v ->

    }

    private fun setLayoutParamsToInit(){
        params = WindowManager.LayoutParams(
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
    }

    private fun setLayoutInflater(){
        // inflate layout_aservice.xml
        inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    private fun initInflatedViews(){
        v = inflater.inflate(R.layout.layout_aservice, null)
        var img1: ImageView = v.findViewById(R.id.img_aserv)
        rel_v = v.findViewById(R.id.layout_aserv)
        rel_v.setOnTouchListener(viewTouchListener)
        img1.setImageResource(R.drawable.ic_launcher_foreground)
        img1.visibility = View.VISIBLE
    }

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
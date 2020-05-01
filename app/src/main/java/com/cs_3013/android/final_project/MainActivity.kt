package com.cs_3013.android.final_project

import android.content.Context
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.sqrt


class MainActivity : AppCompatActivity(), SensorEventListener {
    private var mediaPlayer: MediaPlayer? = null
    private var soundCount = 0
    private var firstPress = true
    lateinit var mSensorManager: SensorManager
    private var mAccel = 0f
    private var mAccelCurrent = 0f
    private var mAccelLast = 0f
    private var mTimer : Timer? = null
    private var arrayofAwards = BooleanArray(10)
    private var scoreCount = 0
    private var cb: ChalkBoard? = null
    private var cb2: ChalkBoard? = null
    private var cb3: ChalkBoard? = null
    var scoreCanChange = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cb = ChalkBoard(this)
        cb2 = ChalkBoard(this)
        cb3 = ChalkBoard(this)
        backgroundLayout.addView(cb)
        backgroundLayout.addView(cb2)
        backgroundLayout.addView(cb3)

        val tvHighScore: TextView = findViewById(R.id.high_score_number)
        val btnClickMe: Button = findViewById(R.id.click_me_btn)
        val scoreTextView: TextView = findViewById(R.id.scoreText)

        tvHighScore.text = getHighScore().toString().padStart(3, '0')


        mTimer = Timer(30000)
        setUpSensor()


        //set up stress button click listener
        btnClickMe.setOnClickListener{

            //
            cb!!.wander()
            cb2!!.wander()
            cb3!!.wander()
            vibrate(10)
            if(firstPress){
                mTimer!!.start()
                firstPress = false

            }
            playSound()
            scoreCount += 1
            scoreTextView.text = scoreCount.toString().padStart(3, '0')
            checkScore(scoreCount)
            if (scoreCount > getHighScore()){
                setHighScore(scoreCount)
                tvHighScore.text = scoreCount.toString().padStart(3, '0')

            }


        }
    }

    private fun vibrate(duration: Int){
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration.toLong(), VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(duration.toLong())
        }
    }

    private fun setUpSensor(){
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager.registerListener(
            this,
            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // Success!
            Log.v("success","yes")
        } else {
            // Failure!
            Log.v("Failure","No sensor found")
        }
    }


    private fun playSound(){
        val btnClick :View = findViewById(R.id.click_me_btn)
        val xyPoint : Point? = getCenterPointOfView(btnClick)
        if (xyPoint != null) {
            Log.v("XY","x: ${xyPoint.x}    y: ${xyPoint.y}")
        }
        soundCount += 1
        mediaPlayer = MediaPlayer.create(this, R.raw.alien_click)
        try{
            if(mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                playSound()
            }
        }catch(e: Exception){
            e.printStackTrace()
        }
        mediaPlayer?.start()
        mediaPlayer!!.setOnCompletionListener {
            it.release()
        }


    }


    private fun setHighScore(score: Int){
        val prefs = getSharedPreferences("puffNstuff", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("highScore", score)
        editor.apply()
    }
    private fun getHighScore(): Int {
        val prefs = getSharedPreferences("puffNstuff", Context.MODE_PRIVATE)
        return prefs.getInt("highScore", 0)
    }

    private fun checkScore(score: Int){

        when(score){
            10 -> {
                Toast.makeText(this@MainActivity, "Guess what!?", Toast.LENGTH_SHORT).show()
                if(!arrayofAwards[0]) {
                    arrayofAwards[0] = true
                    editTimer(mTimer, 10)
                }
            }
            25 ->{
                Toast.makeText(this@MainActivity, "Chicken Butt!!", Toast.LENGTH_SHORT).show()
                if(!arrayofAwards[1]) {
                    arrayofAwards[1] = true
                    editTimer(mTimer, 5)
                }
            }
            50 ->{
                Toast.makeText(this@MainActivity, "Wanna hear a joke?", Toast.LENGTH_SHORT).show()
                if(!arrayofAwards[2]) {
                    arrayofAwards[2] = true
                    editTimer(mTimer, 5)
                }
            }
            100 ->{
                Toast.makeText(this@MainActivity, "Your LIFE!!!", Toast.LENGTH_SHORT).show()
                if(!arrayofAwards[3]) {
                    arrayofAwards[3] = true
                    editTimer(mTimer, 5)
                }
            }
            20 ->{
                if(!arrayofAwards[3]) {
                    arrayofAwards[3] = true
                    editTimer(mTimer, -5)
                }
            }
            150 ->{
                Toast.makeText(this@MainActivity, "You trying to go to the PARK?!?", Toast.LENGTH_SHORT).show()
                if(!arrayofAwards[4]) {
                    arrayofAwards[4] = true
                    editTimer(mTimer, 5)
                }
            }
            200 ->{
                Toast.makeText(this@MainActivity, "What about the strip club?!", Toast.LENGTH_SHORT).show()
                if(!arrayofAwards[5]) {
                    arrayofAwards[5] = true
                    editTimer(mTimer, 10)
                }
            }
            400 ->{
                Toast.makeText(this@MainActivity, "You like toast?", Toast.LENGTH_SHORT).show()
                if(!arrayofAwards[6]) {
                    arrayofAwards[6] = true
                    editTimer(mTimer, 5)
                }
            }
            800 ->{
                Toast.makeText(this@MainActivity, "Cinnamon Rolls?", Toast.LENGTH_SHORT).show()
                if(!arrayofAwards[7]) {
                    arrayofAwards[7] = true
                    editTimer(mTimer, 5)
                }
            }
            1000 ->{
                Toast.makeText(this@MainActivity, "Pootie Tang?", Toast.LENGTH_SHORT).show()
                if(!arrayofAwards[8]) {
                    arrayofAwards[8] = true
                    editTimer(mTimer, 5)
                }
            }
            1500 ->{
                Toast.makeText(this@MainActivity, "Sa da tay, ma damies, sa da tay!", Toast.LENGTH_SHORT).show()
                if(!arrayofAwards[9]) {
                    arrayofAwards[9] = true
                    editTimer(mTimer, 5)
                }
            }
        }
    }


    inner class Timer(millis: Long): CountDownTimer(millis, 100) {
        var millisUntilFinished: Long = 0

        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
            timeNum.text = timeString(millisUntilFinished)
        }


        override fun onFinish() {
            try {
                val notification: Uri =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r =
                    RingtoneManager.getRingtone(applicationContext, notification)
                r.play()
                vibrate(3000)
                mTimer?.cancel()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun editTimer(prevTimer: Timer?, addTime: Int){

        if(prevTimer != null){
            val millis = prevTimer.millisUntilFinished + TimeUnit.SECONDS.toMillis(addTime.toLong())
            Log.v("Timer", "millis: $millis")
            prevTimer.cancel()
            mTimer = Timer(millis)
            mTimer!!.start()
        }

    }


    private fun startTimer(){
        mTimer = Timer(30000)
        mTimer!!.start()
    }


    private fun timeString(millisUntilFinished:Long):String{
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(minutes)
                       //round seconds
        // Format the string
        return String.format(
            Locale.getDefault(),
            "%02d:%02d",
            minutes, seconds
        )
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val x = event!!.values[0]
        val y = event.values[1]
        val z = event.values[2]

        mAccelLast = mAccelCurrent
        mAccelCurrent = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta = mAccelCurrent - mAccelLast
        mAccel = mAccel * 0.9f + delta // perform low-cut filter
        if(mAccel > 25){
            if(firstPress){
                return
            }
            else {
                if(scoreCount - 5 >= 0 && scoreCanChange) {
                    editTimer(mTimer, 15)
                    Toast.makeText(this@MainActivity, "Added some Time Bitch!", Toast.LENGTH_SHORT)
                        .show()
                    scoreCount -= 5
                    scoreText.text = scoreCount.toString().padStart(3, '0')
                    scoreCanChange = false
                    Handler().postDelayed({
                        scoreCanChange = true
                    }, 5000)
                }
//            val notification: Uri =
//                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//            val r =
//                RingtoneManager.getRingtone(applicationContext, notification)
//            r.play()
            }
        }
    }

    private fun getCenterPointOfView(view: View): Point? {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = location[0] + view.width / 2
        val y = location[1] + view.height / 2
        return Point(x, y)
    }



    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }




}

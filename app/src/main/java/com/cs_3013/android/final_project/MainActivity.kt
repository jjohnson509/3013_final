package com.cs_3013.android.final_project

import android.content.Context
import android.content.Intent
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
    private var firstPress = true
    private lateinit var mSensorManager: SensorManager
    private var mAccel = 0f
    private var mAccelCurrent = 0f
    private var mAccelLast = 0f
    private var mTimer: Timer? = null
    private var arrayofAwards = BooleanArray(10)
    private var cb: ChalkBoard? = null
    private var cb1: ChalkBoard? = null
    private var cb2: ChalkBoard? = null
    private var cb3: ChalkBoard? = null
    private var cb4: ChalkBoard? = null
    private var cb5: ChalkBoard? = null
    private var cb6: ChalkBoard? = null
    private var cb7: ChalkBoard? = null
    private var cb8: ChalkBoard? = null
    private var cb9: ChalkBoard? = null
    private val openURL = Intent(Intent.ACTION_VIEW)
    private var scoreCanChange = true
    private var daEnd = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cb = ChalkBoard(this)
        backgroundLayout.addView(cb)

        val tvHighScore: TextView = findViewById(R.id.high_score_number)
        val btnClickMe: Button = findViewById(R.id.click_me_btn)
        val scoreTextView: TextView = findViewById(R.id.scoreText)

        tvHighScore.text = getHighScore().toString().padStart(3, '0')


        mTimer = Timer(30000)
        setUpSensor()


        //set up stress button click listener
        btnClickMe.setOnClickListener {
            cb!!.wander()
            cb1?.wander()
            cb2?.wander()
            cb3?.wander()
            cb4?.wander()
            cb5?.wander()
            cb6?.wander()
            cb7?.wander()
            cb8?.wander()
            cb9?.wander()
            vibrate(10)
            if (firstPress) {
                mTimer!!.start()
                firstPress = false

            }
            playSound()
            scoreCount += 1
            scoreTextView.text = scoreCount.toString().padStart(3, '0')
            checkScore(scoreCount)
            if (scoreCount > getHighScore()) {
                setHighScore(scoreCount)
                tvHighScore.text = scoreCount.toString().padStart(3, '0')

            }

        }
        cb!!.setOnClickListener {
            scoreTextView.text = scoreCount.toString().padStart(3, '0')
        }

    }

    private fun vibrate(duration: Int) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    duration.toLong(),
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            vibrator.vibrate(duration.toLong())
        }
    }


    private fun setUpSensor() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager.registerListener(
            this,
            mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // Success!
            Log.v("success", "yes")
        } else {
            // Failure!
            Log.v("Failure", "No sensor found")
        }
    }


    private fun playSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.alien_click)
        try {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                playSound()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer?.start()
        mediaPlayer!!.setOnCompletionListener {
            it.release()
        }


    }

    private fun playSoundBuzz() {
        mediaPlayer = MediaPlayer.create(this, R.raw.annoy_buzz)
        try {
            if (mediaPlayer!!.isPlaying) {
                mediaPlayer!!.stop()
                mediaPlayer!!.release()
                playSound()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer?.start()
        mediaPlayer!!.setOnCompletionListener {
            it.release()
        }


    }



    private fun setHighScore(score: Int) {
        val prefs = getSharedPreferences("puffNstuff", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("highScore", score)
        editor.apply()
    }

    private fun getHighScore(): Int {
        val prefs = getSharedPreferences("puffNstuff", Context.MODE_PRIVATE)
        return prefs.getInt("highScore", 0)
    }

    override fun onStop() {
        val scoreTextView: TextView = findViewById(R.id.scoreText)
        super.onStop()
        scoreCount = 0
        scoreTextView.text = scoreCount.toString().padStart(3, '0')
        daEnd = false
    }

    private fun checkScore(score: Int) {
        when (score) {
            10 -> {
                Toast.makeText(this@MainActivity, "Might as well give up now, loser...", Toast.LENGTH_SHORT).show()
                if (!arrayofAwards[0]) {
                    arrayofAwards[0] = true
                    editTimer(mTimer, 10)
                }

            }
            25 -> {
                Toast.makeText(this@MainActivity, "Hah, is that what you call clickin'?", Toast.LENGTH_SHORT).show()
                if (!arrayofAwards[1]) {
                    arrayofAwards[1] = true
                    editTimer(mTimer, 5)
                    cb1 = ChalkBoard(this)
                    backgroundLayout.addView(cb1)
                }

            }
            50 -> {
                Toast.makeText(this@MainActivity, "Wanna hear a joke?", Toast.LENGTH_SHORT).show()
                if (!arrayofAwards[2]) {
                    arrayofAwards[2] = true
                    editTimer(mTimer, 5)
                    cb2 = ChalkBoard(this)
                    backgroundLayout.addView(cb2)
                }
            }
            100 -> {
                Toast.makeText(this@MainActivity, "Your LIFE!!!", Toast.LENGTH_SHORT).show()
                if (!arrayofAwards[3]) {
                    arrayofAwards[3] = true
                    editTimer(mTimer, 5)
                    cb3 = ChalkBoard(this)
                    backgroundLayout.addView(cb3)
                }

            }
            120 -> {
                if (!arrayofAwards[3]) {
                    arrayofAwards[3] = true
                    editTimer(mTimer, -5)
                    cb4 = ChalkBoard(this)
                    backgroundLayout.addView(cb4)
                }

            }
            150 -> {
                Toast.makeText(
                    this@MainActivity,
                    "You should try to go to the park!",
                    Toast.LENGTH_SHORT
                ).show()
                if (!arrayofAwards[4]) {
                    arrayofAwards[4] = true
                    editTimer(mTimer, 5)
                    cb5 = ChalkBoard(this)
                    backgroundLayout.addView(cb5)
                }

            }
            200 -> {
                Toast.makeText(this@MainActivity, "This many clicks? Still more than your bank account! haha", Toast.LENGTH_SHORT)
                    .show()
                if (!arrayofAwards[5]) {
                    arrayofAwards[5] = true
                    editTimer(mTimer, 10)
                    cb6 = ChalkBoard(this)
                    backgroundLayout.addView(cb6)
                }

            }
            400 -> {
                Toast.makeText(this@MainActivity, "Still clickin???? WHY, DUDE!?!", Toast.LENGTH_SHORT).show()
                if (!arrayofAwards[6]) {
                    arrayofAwards[6] = true
                    editTimer(mTimer, 5)
                    cb7 = ChalkBoard(this)
                    backgroundLayout.addView(cb7)
                }
            }
            420 -> {
                Toast.makeText(this@MainActivity, "Pause for the cause!", Toast.LENGTH_SHORT).show()
                cb8 = ChalkBoard(this)
                backgroundLayout.addView(cb8)
            }
            800 -> {
                Toast.makeText(this@MainActivity, "Cinnamon Rolls?", Toast.LENGTH_SHORT).show()
                if (!arrayofAwards[7]) {
                    arrayofAwards[7] = true
                    editTimer(mTimer, 5)
                    cb9 = ChalkBoard(this)
                    backgroundLayout.addView(cb9)
                }
            }
            1000 -> {
                Toast.makeText(this@MainActivity, "Pootie Tang?", Toast.LENGTH_SHORT).show()
                if (!arrayofAwards[8]) {
                    arrayofAwards[8] = true
                    editTimer(mTimer, 5)
                }
            }
            1500 -> {
                Toast.makeText(
                    this@MainActivity,
                    "Sa da tay, ma damies, sa da tay!",
                    Toast.LENGTH_SHORT
                ).show()
                if (!arrayofAwards[9]) {
                    arrayofAwards[9] = true
                    editTimer(mTimer, 5)
                }
            }
        }

        if (daEnd) {
            when {
                scoreCount <= 10 -> {
                    openURL.data =
                        Uri.parse("https://www.thenation.com/wp-content/uploads/2019/12/mbeubeuss-fernelius-4-otu-img.jpg")
                    startActivity(openURL)
                }
                scoreCount in 11..50 -> {
                    openURL.data =
                        Uri.parse("https://media.distractify.com/brand-img/GXluu_Kub/0x0/carole-baskin-memes-3-1585580530588.jpeg")
                    startActivity(openURL)
                }
                scoreCount in 101..200 -> {
                    openURL.data =
                        Uri.parse("https://d18ufwot1963hr.cloudfront.net/wp-content-production/uploads/2020/04/img_5e8f953f82600.gif")
                    startActivity(openURL)

                }
                scoreCount in 201..300 -> {
                    openURL.data =
                        Uri.parse("https://c7.uihere.com/files/169/350/488/ducky-dinosaur-foot-the-land-before-time-meme-dinosaur-cartoon.jpg")
                    startActivity(openURL)

                }
                scoreCount in 301..400 -> {
                    openURL.data =
                        Uri.parse("https://www.syfy.com/sites/syfy/files/styles/1200x680_hero/public/2017/08/the-jetsons-1962.jpg")
                    startActivity(openURL)

                }
                scoreCount in 401..419-> {
                    openURL.data =
                        Uri.parse("https://media.makeameme.org/created/break-time-wheres.jpg")
                    startActivity(openURL)
                }
                scoreCount >= 420 -> {
                    openURL.data =
                        Uri.parse("https://i.kym-cdn.com/entries/icons/mobile/000/010/879/420d.jpg")
                    startActivity(openURL)
                }
            }
        }
    }

    companion object {
        var scoreCount = 0
    }


    inner class Timer(millis: Long) : CountDownTimer(millis, 100) {
        var millisUntilFinished: Long = 0

        override fun onTick(millisUntilFinished: Long) {
            this.millisUntilFinished = millisUntilFinished
            timeNum.text = timeString(millisUntilFinished)
        }


        override fun onFinish() {
            try {
                vibrate(3000)
                daEnd = true
                playSoundBuzz()
                checkScore(scoreCount)
                mTimer?.cancel()
                onStop()
                recreate()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun editTimer(prevTimer: Timer?, addTime: Int) {

        if (prevTimer != null) {
            val millis = prevTimer.millisUntilFinished + TimeUnit.SECONDS.toMillis(addTime.toLong())
            Log.v("Timer", "millis: $millis")
            prevTimer.cancel()
            mTimer = Timer(millis)
            mTimer!!.start()
        }

    }


    private fun startTimer() {
        mTimer = Timer(30000)
        mTimer!!.start()
    }


    private fun timeString(millisUntilFinished: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                minutes
            )
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
        if (mAccel > 25) {
            if (firstPress) {
                return
            } else {
                if (scoreCount - 5 >= 0 && scoreCanChange) {
                    editTimer(mTimer, 15)
                    Toast.makeText(this@MainActivity, "Added some time!", Toast.LENGTH_SHORT)
                        .show()
                    scoreCount -= 5
                    scoreText.text = scoreCount.toString().padStart(3, '0')
                    scoreCanChange = false
                    Handler().postDelayed({
                        scoreCanChange = true
                    }, 5000)
                }
            }
        }
    }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


}

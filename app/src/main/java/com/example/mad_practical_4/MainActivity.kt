package com.example.mad_practical_4

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.View
import android.widget.TextView
import android.widget.TextClock
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var card2: MaterialCardView
    private lateinit var btnCreate: MaterialButton
    private lateinit var btnCancel: MaterialButton
    private lateinit var textAlarmTime: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        card2 = findViewById(R.id.card2)
        btnCreate = findViewById(R.id.create_alarm)
        btnCancel = findViewById(R.id.stop_alarm)
        textAlarmTime = findViewById(R.id.alarm_time)

        // Hide alarm card initially
        card2.visibility = View.GONE

        // Create alarm button
        btnCreate.setOnClickListener {
            showTimePickerDialog()
        }

        // Cancel alarm button
        btnCancel.setOnClickListener {
            setAlarm(-1,"Stop")
            card2.visibility = View.GONE
        }
    }

    private fun showTimePickerDialog() {
        val calendar : Calendar = Calendar.getInstance()
        val hour : Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute : Int = calendar.get(Calendar.MINUTE)

        val picker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            setAlarmTime(selectedHour, selectedMinute)
        }, hour, minute, false)
        picker.show()
    }

    private fun setAlarmTime(hour: Int, minute: Int)
    {
        val alarmCalendar = Calendar.getInstance()
        val year: Int= alarmCalendar.get(Calendar.YEAR)
        val month: Int= alarmCalendar.get(Calendar.MONTH)
        val day: Int= alarmCalendar.get(Calendar.DATE)

        alarmCalendar.set(year,month,day,hour,minute,0)
        textAlarmTime.text= SimpleDateFormat("hh:mm ss a").format(alarmCalendar.time)
        setAlarm(alarmCalendar.timeInMillis,"Start")
        Toast.makeText(this,"Time set",Toast.LENGTH_SHORT).show()
        card2.visibility = View.VISIBLE
    }

    private fun setAlarm(millisTime:Long,str: String)
    {
        val intent= Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1",str)
        val pendingIntent= PendingIntent.getBroadcast(applicationContext,234324243,intent,PendingIntent.FLAG_IMMUTABLE)
        val alarmManager=getSystemService(ALARM_SERVICE) as AlarmManager
        if (str=="Start")
        {
            if (alarmManager.canScheduleExactAlarms())
            {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,millisTime,pendingIntent)
                Toast.makeText(this,"Start Alarm", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Can't set Alarm", Toast.LENGTH_SHORT).show()
                startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
            }
        }
        else if (str=="Stop")
        {
            alarmManager.cancel(pendingIntent)
            sendBroadcast(intent)
            Toast.makeText(this,"Alarm Stopped", Toast.LENGTH_SHORT).show()
        }
    }
}
package com.azharie.alzaini.githubuser3.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azharie.alzaini.githubuser3.R
import com.azharie.alzaini.githubuser3.alarmnotif.AlarmReceiver
import com.azharie.alzaini.githubuser3.databinding.ActivitySettingAlarmBinding

class SettingAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingAlarmBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var mSharedPrererence: SharedPreferences

    companion object{
        private const val PREF_NAME = "SettingPref"
        private const val DAILY = "daily"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alarmReceiver = AlarmReceiver()
        mSharedPrererence = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        setSwitch()

        binding.switchAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                alarmReceiver.repeatingAlarm(applicationContext, getString(R.string.wanna))


            } else{
                alarmReceiver.cancelAlarm(applicationContext)
            }
            saveChecked(isChecked)
        }
    }

    private fun saveChecked(checked: Boolean){
        val edit = mSharedPrererence.edit()
        edit.putBoolean(DAILY, checked)
        edit.apply()
    }

    private fun setSwitch(){
        binding.switchAlarm.isChecked = mSharedPrererence.getBoolean(DAILY, false)
    }
}
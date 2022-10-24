package com.shounak.music_player

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shounak.music_player.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.MyMusicThemeNav)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Settings"

        binding.versionName.text = setVersionDetails()

        binding.sortBtn.setOnClickListener {
            val menuList = arrayOf("Recently Added", "Song Title", "File Size")
            var currentSort = MainActivity.sortOrder
            val builder = MaterialAlertDialogBuilder(this)
            builder.setTitle("Sorting")
                .setPositiveButton("OK"){ _, _ ->
                    val editor = getSharedPreferences("SORTING", MODE_PRIVATE).edit()
                    editor.putInt("sortOrder", currentSort)
                    editor.apply()
                }
                .setSingleChoiceItems(menuList, currentSort){ _,which->
                    currentSort = which
                }
            val customDialog = builder.create()
            customDialog.show()

        }
    }
    private fun setVersionDetails():String{
        return "Version Name: ${BuildConfig.VERSION_NAME}"
    }
}
package ro.ubb.exam_template

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settings_button.setOnClickListener()
        {
            startActivity(Intent( this, SettingsActivity::class.java))

        }
        post_get_screen_btn.setOnClickListener()
        {
            startActivity(Intent( this, SecondActivity::class.java))

        }
        manage_button.setOnClickListener()
        {
            startActivity(Intent( this, StudentActivity::class.java))

        }

    }
}
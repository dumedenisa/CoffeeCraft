package com.example.coffeecraft.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.coffeecraft.R

class ThankYouActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thank_you)

        findViewById<Button>(R.id.backToShopButton).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

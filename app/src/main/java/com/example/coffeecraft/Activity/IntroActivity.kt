package com.example.coffeecraft.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.coffeecraft.databinding.ActivityIntroBinding
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : BaseActivity() {
    lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener{
            startActivity(Intent(this@IntroActivity, MainActivity::class.java))
        }
    }

}




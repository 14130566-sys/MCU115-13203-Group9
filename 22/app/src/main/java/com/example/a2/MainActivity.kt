package com.example.a2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etInput: EditText
    private lateinit var btnSwitch: Button
    private lateinit var tvResult: TextView

    private val secActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val replyText = result.data?.getStringExtra("reply_data")
            tvResult.text = replyText
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etInput = findViewById(R.id.etInput)
        btnSwitch = findViewById(R.id.btnSwitch)
        tvResult = findViewById(R.id.tvResult)

        btnSwitch.setOnClickListener {
            val textToSend = etInput.text.toString()
            val intent = Intent(this, SecActivity::class.java)
            intent.putExtra("send_data", textToSend)
            secActivityLauncher.launch(intent)
        }
    }
}

package com.example.a2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecActivity : AppCompatActivity() {

    private lateinit var tvReceived: TextView
    private lateinit var etReply: EditText
    private lateinit var btnReturn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sec)

        tvReceived = findViewById(R.id.tvReceived)
        etReply = findViewById(R.id.etReply)
        btnReturn = findViewById(R.id.btnReturn)

        val receivedData = intent.getStringExtra("send_data")
        tvReceived.text = receivedData

        btnReturn.setOnClickListener {
            val replyText = etReply.text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra("reply_data", replyText)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}

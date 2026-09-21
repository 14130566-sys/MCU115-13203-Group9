package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private var firstNum = 0.0
    private var operator = ""
    private var isNewOp = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)

        // 綁定數字按鈕 0 ~ 9
        val numButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )

        for (id in numButtons) {
            findViewById<Button>(id).setOnClickListener { btn ->
                val text = (btn as Button).text.toString()
                if (isNewOp) {
                    tvResult.text = text
                    isNewOp = false
                } else {
                    tvResult.append(text)
                }
            }
        }

        // 綁定加減乘除按鈕
        findViewById<Button>(R.id.btnPlus).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("÷") }

        // 綁定等號與清除按鈕
        findViewById<Button>(R.id.btnEqual).setOnClickListener { calculate() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clear() }
    }

    private fun setOperator(op: String) {
        firstNum = tvResult.text.toString().toDoubleOrNull() ?: 0.0
        operator = op
        isNewOp = true
    }

    private fun calculate() {
        val secondNum = tvResult.text.toString().toDoubleOrNull() ?: 0.0
        var result = 0.0
        when (operator) {
            "+" -> result = firstNum + secondNum
            "-" -> result = firstNum - secondNum
            "×" -> result = firstNum * secondNum
            "÷" -> result = if (secondNum != 0.0) firstNum / secondNum else 0.0
        }
        // 如果計算結果是整數，就不顯示 .0
        tvResult.text = if (result % 1 == 0.0) result.toInt().toString() else result.toString()
        isNewOp = true
    }

    private fun clear() {
        tvResult.text = "0"
        firstNum = 0.0
        operator = ""
        isNewOp = true
    }
}

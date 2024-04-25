package com.example.kards

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class beep_acc : AppCompatActivity() {

    private lateinit var amountInput: EditText
    private lateinit var confirmButton: Button
    private lateinit var switchButton: SwitchCompat
    private lateinit var incomeExpenseTextView: TextView
    private lateinit var beepBalanceTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_beep_acc)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = Intent("com.example.kards.TRANSACTION_ADDED")
        sendBroadcast(intent)

        amountInput = findViewById(R.id.inputBEEP)
        confirmButton = findViewById(R.id.confirm)
        switchButton = findViewById(R.id.switchButton)
        incomeExpenseTextView = findViewById(R.id.incexp)
        beepBalanceTextView = findViewById(R.id.beepBAL)

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            incomeExpenseTextView.text = if (isChecked) "Expense" else "Income"
        }

        confirmButton.setOnClickListener {
            val amount = amountInput.text.toString().toDoubleOrNull() ?: 0.0
            val multiplier = if (switchButton.isChecked) -1 else 1
            val result = amount * multiplier

            val db = beepDATA(this).writableDatabase
            val values = ContentValues().apply {
                put("amount", result)
            }

            try {
                if (result > 0) {
                    db.insert("income", null, values)
                } else {
                    db.insert("expenses", null, values)
                }
            } catch (e: Exception) {
                // Handle the exception, e.g., show an error message
            } finally {
                db.close()
            }

            updateBalance()
            amountInput.text.clear()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Update balance when the activity is created
        updateBalance()
    }

    private fun updateBalance() {
        val db = beepDATA(this).readableDatabase
        val totalIncome = db.rawQuery("SELECT SUM(amount) FROM income", null)
            .use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getDouble(0)
                } else {
                    0.0
                }
            }

        val totalExpenses = db.rawQuery("SELECT SUM(amount) FROM expenses", null)
            .use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getDouble(0)
                } else {
                    0.0
                }
            }

        val balance = totalIncome + totalExpenses
        beepBalanceTextView.text = "₱ $balance"

        db.close()
    }
}

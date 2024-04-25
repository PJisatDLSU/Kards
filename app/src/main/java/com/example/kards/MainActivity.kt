package com.example.kards

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.cashBTN).setOnClickListener {
            startActivity(Intent(this, cash_acc::class.java))
        }
        findViewById<Button>(R.id.gcashBTN).setOnClickListener {
            startActivity(Intent(this, gcash_acc::class.java))
        }
        findViewById<Button>(R.id.beepBTN).setOnClickListener {
            startActivity(Intent(this, beep_acc::class.java))
        }

        // CASH STUFF
        val cashDB = cashDATA(this).readableDatabase
        val incomeSum = cashDB.rawQuery("SELECT SUM(amount) FROM income", null)
            .use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getDouble(0)
                } else {
                    0.0
                }
            }
        val expenseSum = cashDB.rawQuery("SELECT SUM(amount) FROM expenses", null)
            .use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getDouble(0)
                } else {
                    0.0
                }
            }
        cashDB.close()
        // Update the TextViews with the fetched values
        findViewById<TextView>(R.id.cashIN).text = "₱ $incomeSum"
        findViewById<TextView>(R.id.cashOUT).text = "₱ ${-1 * expenseSum}"
        findViewById<TextView>(R.id.cashBAL).text = "₱ ${incomeSum + expenseSum}"

        // GCASH STUFF
        val gcashDB = gcashDATA(this).readableDatabase
        val incomeSumG = gcashDB.rawQuery("SELECT SUM(amount) FROM income", null)
            .use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getDouble(0)
                } else {
                    0.0
                }
            }
        val expenseSumG = gcashDB.rawQuery("SELECT SUM(amount) FROM expenses", null)
            .use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getDouble(0)
                } else {
                    0.0
                }
            }
        gcashDB.close()
        // Update the TextViews with the fetched values
        findViewById<TextView>(R.id.gcashIN).text = "₱ $incomeSumG"
        findViewById<TextView>(R.id.gcashOUT).text = "₱ ${-1 * expenseSumG}"
        findViewById<TextView>(R.id.gcashBAL).text = "₱ ${incomeSumG + expenseSumG}"

        // BEEP STUFF
        val beepDB = beepDATA(this).readableDatabase
        val incomeSumB = beepDB.rawQuery("SELECT SUM(amount) FROM income", null)
            .use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getDouble(0)
                } else {
                    0.0
                }
            }
        val expenseSumB = beepDB.rawQuery("SELECT SUM(amount) FROM expenses", null)
            .use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getDouble(0)
                } else {
                    0.0
                }
            }
        beepDB.close()
        // Update the TextViews with the fetched values
        findViewById<TextView>(R.id.beepIN).text = "₱ $incomeSumB"
        findViewById<TextView>(R.id.beepOUT).text = "₱ ${-1 * expenseSumB}"
        findViewById<TextView>(R.id.beepBAL).text = "₱ ${incomeSumB + expenseSumB}"
    }
}

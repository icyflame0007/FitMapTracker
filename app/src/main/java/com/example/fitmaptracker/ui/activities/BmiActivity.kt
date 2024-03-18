package com.example.fitmaptracker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.cardview.widget.CardView
import com.example.fitmaptracker.R
import kotlin.math.pow

class BmiActivity : AppCompatActivity() {

    lateinit var edt_weight: EditText
    lateinit var edt_height: EditText
    lateinit var btn_calculate: Button
    lateinit var txt_result: TextView
    lateinit var txt_cardview_result: TextView
    lateinit var  cardview :CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)
        enableEdgeToEdge()

        edt_weight = findViewById(R.id.edt_weight)
        edt_height = findViewById(R.id.edt_height)
        btn_calculate = findViewById(R.id.btn_calculate)
        txt_result = findViewById(R.id.bmiTextView)
        txt_cardview_result = findViewById(R.id.txtcardviewresult)
        cardview = findViewById(R.id.cardView)

        btn_calculate.setOnClickListener {
            val height = edt_height.text.toString().toFloat() / 100 // Convert cm to m
            val weight = edt_weight.text.toString().toFloat()

            if (height != 0f) {
                val bmi = calculateBMI(weight, height)

                txt_result.text = "Your BMI: $bmi"
                cardview.visibility= View.VISIBLE
                getBMICondition(bmi)
            }
        }
    }

    private fun calculateBMI(weight: Float, height: Float): Float {
        return weight / (height.pow(2))
    }

    private fun getBMICondition(bmi: Float) {

            if(bmi < 18.5 ){

                txt_cardview_result.text = "You're Underweight"

            }

           else if( bmi in 18.5..24.9)  {

              txt_cardview_result.text = "You're Healthy"
            }

          else if(bmi in 25.0..29.9) {

                txt_cardview_result.text = "You're Overweight"
            }

            else  {

                txt_cardview_result.text = "You're Obese"
            }
        }
    }

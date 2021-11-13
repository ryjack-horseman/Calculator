package com.lp.android.calculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.lang.NumberFormatException

import kotlinx.android.synthetic.main.activity_main.*

private const val PENDING_OPERATION = "pending_operation"
private const val OPERAND1 = "operand1"
private const val OPERAND1_STORED = "operand1_stored"
private const val NEWCLEAR = "CLR"
private const val RESULTCLEAR = "RST"
private const val OPCLEAR = "OPC"
class MainActivity : AppCompatActivity() {



    private var operand1: Double? = null
    private var pendingOperation = "="


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listener = View.OnClickListener {v ->
            val b = v as Button
            newNumber.append(b.text)
            buttonClear.text = NEWCLEAR
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDecimal.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException){
                newNumber.setText("")
            }


            pendingOperation = op
            operation.text = pendingOperation
            buttonClear.text = OPCLEAR
        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)

        val negListener = View.OnClickListener { v ->
            //turn input negative if it exists, toggle the sign otherwise

            val value = newNumber.text.toString()
            if(value.isEmpty()){
                newNumber.setText("-")
            }else{
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                }catch (e: NumberFormatException){
                    //newNumber was "-' of ".", so clear it
                    newNumber.setText("")
                }
            }
        }

        buttonNeg.setOnClickListener(negListener)

        val clearListener = View.OnClickListener { v ->

            when(buttonClear.text.toString()){
                NEWCLEAR -> {
                    newNumber.setText("")
                    if(operation.text.isEmpty()){
                        buttonClear.text = RESULTCLEAR
                    } else{
                        buttonClear.text = OPCLEAR
                    }
                }
                OPCLEAR -> {
                    operation.text = ""
                    buttonClear.text = RESULTCLEAR

                }
                RESULTCLEAR -> {
                    result.setText("")
                    operand1 = null
                    operation.text = ""
                }
            }
        }

        buttonClear.setOnClickListener(clearListener)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PENDING_OPERATION, pendingOperation)
        if(operand1 != null){
            outState.putDouble(OPERAND1, operand1!!)
            outState.putBoolean(OPERAND1_STORED, true)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if(savedInstanceState.getBoolean(OPERAND1_STORED, false)){
            savedInstanceState.getDouble(OPERAND1)
        } else{
            null
        }
        pendingOperation = savedInstanceState. getString(PENDING_OPERATION, "")
        operation.text = pendingOperation

    }
    private fun performOperation(value: Double, operation: String){
        if (operand1 == null){
            operand1 = value
        } else{

            if(pendingOperation == "="){
                pendingOperation = operation
            }
            when (pendingOperation){
                "=" -> operand1 = value
                "/" -> operand1 = if(value == 0.0){
                    Double.NaN
                } else{
                    operand1!! / value
                }
                "*" -> operand1 = operand1!! * value
                "-" -> operand1 = operand1!! - value
                "+" -> operand1 = operand1!! + value
            }
        }
        result.setText(operand1.toString())
        newNumber.setText("")
        buttonClear.text = RESULTCLEAR
    }

}
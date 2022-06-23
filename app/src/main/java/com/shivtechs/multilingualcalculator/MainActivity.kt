package com.shivtechs.multilingualcalculator

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.WindowManager
import java.util.*


class MainActivity : AppCompatActivity() {
    private var operationAfterResult: String = ""
    private var resultByOpertion: Boolean = false
    private var result: Double = 0.0
    private var num2data: Double = 0.0
    private var num1data: Double = 0.0
    private var data = ""
    private lateinit var languages: Array<String>
    private lateinit var languageCodes: Array<String>
    //This is shivpujan ;
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        languages = resources.getStringArray(R.array.languages_array)
        languageCodes = resources.getStringArray(R.array.languages_code_array)

        feedBack.setOnClickListener {
            AlertDialog.Builder(this)
                    .setItems(arrayOf("Give FeedBack","Privacy Policy")) { _, i ->
                        when(i){
                            0 -> {
                                val intent = Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:shivpujanyadav12@gmail.com"))
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Multilingual Calculator")
                                intent.putExtra(Intent.EXTRA_TEXT, "Have Something on Mind regarding the app list it down")
                                startActivity(intent)
                            }
                            1 -> {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse("https://daydecider.firebaseapp.com/")
                                startActivity(intent)
                            }
                        }
                    }
                    .show()
        }

        translateTool.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(R.string.change_language)
                .setItems(languages) { _, i ->
                    val languageToLoad = languageCodes[i]

                    if (LocaleHelper.getLanguage(this@MainActivity) != languageToLoad) {
                        LocaleHelper.setLocale(this@MainActivity, languageToLoad)
                        recreate()
                    }

                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                    dialog.cancel()
                }
                .show()
        }

        backspace.setOnClickListener {
            data = data.dropLast(1)
            num1.text = num1.text.toString().dropLast(1)
        }

        cut.setOnClickListener{
            data = ""
            num1.text = ""
            num2.text = ""
            num1data = 0.0
            num2data = 0.0
            result = 0.0
            operationTxt.text = ""
        }

        equal.setOnClickListener {
            equalFun()
        }
    }


    private fun equalFun(){
        if (num1.text.isNotEmpty() && data!="") {
            num1data = data.toDouble()
        } else {
            Toast.makeText(this@MainActivity,"Please second number",Toast.LENGTH_LONG).show()
        }


        if (data != "" && operationTxt.text.isNotEmpty()) {
            // num1 op num2
            result = 0.0
            when (operationTxt.text.toString()) {
                "/" -> result = num2data / num1data
                "x" -> result = num2data * num1data
                "+" -> result = num2data + num1data
                "-" -> result = num2data - num1data
            }
            //convert the result in to Locale Languauge
            var resultOnScreenVal = ""
            for( i in result.toString()){
                when(i){
                    '1' -> resultOnScreenVal += resources.getString(R.string.one)
                    '2' -> resultOnScreenVal += resources.getString(R.string.two)
                    '3' -> resultOnScreenVal += resources.getString(R.string.three)
                    '4' -> resultOnScreenVal += resources.getString(R.string.four)
                    '5' -> resultOnScreenVal += resources.getString(R.string.five)
                    '6' -> resultOnScreenVal += resources.getString(R.string.six)
                    '7' -> resultOnScreenVal += resources.getString(R.string.seven)
                    '8' -> resultOnScreenVal += resources.getString(R.string.eight)
                    '9' -> resultOnScreenVal += resources.getString(R.string.nine)
                    '0' -> resultOnScreenVal += resources.getString(R.string.zero)
                    '.' -> resultOnScreenVal += resources.getString(R.string.dot)
                    'E' -> resultOnScreenVal += 'e'
                }
            }

            if(resultByOpertion && operationAfterResult!=""){
                num2.text = resultOnScreenVal
                num2data = result
                num1.text = ""
                num1data = 0.0
                operationTxt.text = operationAfterResult
                operationAfterResult =""
                resultByOpertion = false
            } else {
                num1.text = resultOnScreenVal //put it on screen
                num2.text = ""
                operationTxt.text = ""
            }
            data = ""
        }
    }

    fun clickerNum(v: View) {
        when (v.id) {
            R.id.one -> data = data.plus("1")
            R.id.two -> data =data.plus("2")
            R.id.three -> data =data.plus("3")
            R.id.four -> data =data.plus("4")
            R.id.five -> data =data.plus("5")
            R.id.six -> data =data.plus("6")
            R.id.seven -> data =data.plus("7")
            R.id.eight -> data =data.plus("8")
            R.id.nine -> data =data.plus("9")
            R.id.zero ->data = data.plus("0")
            R.id.dot -> data =data.plus(resources.getString(R.string.dot))
        }
        num1.append((v as Button).text.toString())
    }

    fun clickerOperation(v: View) {

        if(num1.text.isNotEmpty() && num2.text.isNotEmpty()){
            resultByOpertion = true
            operationAfterResult = (v as Button).text.toString()
            equalFun()
        }

        if(num2.text.isNotEmpty() && num1.text.isEmpty()){
            operationTxt.text = (v as Button).text.toString()
        }

        if(num2.text.isEmpty() && num1.text.isNotEmpty()) {
            if(result!=0.0 && data==""){
                data = result.toString()
            }
            if (data != "") {
                operationTxt.text = (v as Button).text.toString()
                num2.text = num1.text
                num2data = data.toDouble()
                data = ""
                num1.text = ""
            } else {
                Toast.makeText(this@MainActivity, "Please insert some number first", Toast.LENGTH_LONG).show()
            }
        }
    }
}


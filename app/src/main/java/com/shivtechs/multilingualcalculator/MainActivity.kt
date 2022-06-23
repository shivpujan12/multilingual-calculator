package com.shivtechs.multilingualcalculator

/*import com.apptracker.android.listener.AppModuleListener
import com.apptracker.android.track.AppTracker*/

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.DrawableContainer.DrawableContainerState
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var operationAfterResult: String = ""
    private var resultByOpertion: Boolean = false
    private var result: Double = 0.0
    private var num2data: Double = 0.0
    private var num1data: Double = 0.0
    private var data = ""
    private lateinit var languages: Array<String>
    private lateinit var languageCodes: Array<String>
    private lateinit var sp: SharedPreferences

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
        }
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        sp = this@MainActivity.getSharedPreferences("calulator", Context.MODE_PRIVATE)

        var defaultTheme: String = "default theme"
        when (this@MainActivity.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                defaultTheme = "Dark Red"
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                defaultTheme = "default theme"
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                defaultTheme = "default theme"
            }
        }

        defaultTheme = sp.getString("theme", defaultTheme)!!
        themeSelector(defaultTheme)

        languages = resources.getStringArray(R.array.languages_array)
        languageCodes = resources.getStringArray(R.array.languages_code_array)

        options.setOnClickListener {
            AlertDialog.Builder(this)
                .setItems(arrayOf("Themes", "Give FeedBack", "Privacy Policy")) { _, i ->
                    when (i) {
                        0 -> {
                            AlertDialog.Builder(this)
                                .setItems(arrayOf("Default", "Dark Red")) { _, i2 ->
                                    when (i2) {
                                        0 -> {
                                            themeSelector("default theme")
                                        }
                                        1 -> {
                                            themeSelector("Dark Red")
                                        }
                                        2 -> {
//                                                    functionPallet("#0c00eb", "#030361", "#0c00eb", "#000000")

                                        }
                                    }
                                }.show()
                        }
                        1 -> {
                            val intent = Intent(
                                Intent.ACTION_SENDTO,
                                Uri.parse("mailto:shivpujanyadav12@gmail.com")
                            )
                            intent.putExtra(
                                Intent.EXTRA_SUBJECT,
                                "Feedback for Multilingual Calculator"
                            )
                            intent.putExtra(
                                Intent.EXTRA_TEXT,
                                "Have Something on Mind regarding the app list it down"
                            )
                            startActivity(intent)
                        }
                        2 -> {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("https://daydecider.firebaseapp.com/")
                            startActivity(intent)
                        }
                    }
                }.show()
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

        cut.setOnClickListener {
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
            /*Thread {
                Thread.sleep(2000)
                AppTracker.loadModule(applicationContext, "inapp")
            }.start()*/
        }
    }

    private fun themeSelector(theme: String?) {
        when (theme) {
            "default theme" -> {
                functionPallet("", "", "#911a13", "#000000")
                numPallet("num", "num", "#292828", "#000000")
                backScreen.setBackgroundColor(Color.WHITE)
                num1.setBackgroundColor(Color.WHITE)
                num1.setTextColor(Color.parseColor("#1b1b1c"))
                num2.setBackgroundColor(Color.WHITE)
                num2.setTextColor(Color.parseColor("#1b1b1c"))
                operationTxt.setBackgroundColor(Color.WHITE)
                operationTxt.setTextColor(Color.parseColor("#1b1b1c"))
                activityBackground.setBackgroundColor(Color.WHITE)
                sp.edit().putString("theme", "default theme").apply()
            }
            "Dark Red" -> {
                functionPallet("#9c0b10", "#b02121", "#911a13", "#000000")
                numPallet("#1b1b1c", "#292828", "#292828", "#000000")
                backScreen.setBackgroundColor(Color.parseColor("#1b1b1c"))
                num1.setBackgroundColor(Color.parseColor("#1b1b1c"))
                num1.setTextColor(Color.WHITE)
                num2.setBackgroundColor(Color.parseColor("#1b1b1c"))
                num2.setTextColor(Color.WHITE)
                operationTxt.setBackgroundColor(Color.parseColor("#1b1b1c"))
                operationTxt.setTextColor(Color.WHITE)
                activityBackground.setBackgroundColor(Color.parseColor("#1b1b1c"))
                sp.edit().putString("theme", "Dark Red").apply()
            }
        }
    }

    private fun changeTheme(
        view: View,
        ubkColor: String,
        pbkColor: String,
        uStrokeColor: String,
        pStrokeColor: String
    ) {
        val gradientDrawable = view.background as StateListDrawable
        val drawableContainerState = gradientDrawable.constantState as DrawableContainerState
        val children = drawableContainerState.children
        val pressed = children[0] as GradientDrawable
        val unpressed = children[1] as GradientDrawable

        if (ubkColor == "" || pbkColor == "") {
            unpressed.setColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    android.R.color.holo_orange_dark
                )
            )
            pressed.setColor(
                ContextCompat.getColor(
                    this@MainActivity,
                    android.R.color.holo_orange_light
                )
            )
            pressed.setStroke(1, Color.parseColor("#EB7C00"))
            unpressed.setStroke(1, Color.parseColor("#EB7C00"))
        } else if (ubkColor == "num" || pbkColor == "num") {
            unpressed.setColor(Color.parseColor("#FFFFFF"))
            pressed.setColor(Color.parseColor("#C7C7C7"))
            pressed.setStroke(1, Color.parseColor("#E9E9E9"))
            unpressed.setStroke(1, Color.parseColor("#E9E9E9"))
        } else {
            pressed.setColor(Color.parseColor(pbkColor))
            unpressed.setColor(Color.parseColor(ubkColor))
            pressed.setStroke(1, Color.parseColor(pStrokeColor))
            unpressed.setStroke(1, Color.parseColor(uStrokeColor))
        }

    }

    private fun changeTextColor(
        view: View,
        ubkColor: String,
        resourceBlack: Int,
        resourceWhite: Int
    ) {
        val contrastColor: Int = if (ubkColor == "" || ubkColor == "num") {
            getContrastColor(Color.WHITE)
        } else {
            getContrastColor(Color.parseColor(ubkColor))
        }
        try {
            (view as Button).setTextColor(contrastColor)
        } catch (e: Exception) {
            if (contrastColor == -0x1000000) {
                (view as ImageButton).setImageDrawable(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        resourceBlack
                    )
                )
            } else {
                (view as ImageButton).setImageDrawable(
                    ContextCompat.getDrawable(
                        this@MainActivity,
                        resourceWhite
                    )
                )
            }
        }
    }

    private fun functionPallet(
        ubkColor: String,
        pbkColor: String,
        uStrokeColor: String,
        pStrokeColor: String
    ) {
        changeTextColor(
            options,
            ubkColor,
            R.drawable.ic_settings_black_24dp,
            R.drawable.ic_settings_white_24dp
        )
        changeTextColor(
            translateTool,
            ubkColor,
            R.drawable.ic_translate_black_24dp,
            R.drawable.ic_translate_white_24dp
        )
        changeTextColor(
            backspace,
            ubkColor,
            R.drawable.ic_backspace_black_24dp,
            R.drawable.ic_backspace_white_24dp
        )
        changeTextColor(cut, ubkColor, 0, 0)
        changeTextColor(divide, ubkColor, 0, 0)
        changeTextColor(multiply, ubkColor, 0, 0)
        changeTextColor(minus, ubkColor, 0, 0)
        changeTextColor(plus, ubkColor, 0, 0)

        changeTheme(options, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(translateTool, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(cut, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(backspace, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(divide, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(multiply, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(minus, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(plus, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
    }

    private fun numPallet(
        ubkColor: String,
        pbkColor: String,
        uStrokeColor: String,
        pStrokeColor: String
    ) {
        changeTheme(one, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(two, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(three, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(four, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(five, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(six, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(seven, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(eight, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(nine, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(zero, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(dot, ubkColor, pbkColor, uStrokeColor, pStrokeColor)
        changeTheme(equal, ubkColor, pbkColor, uStrokeColor, pStrokeColor)

        changeTextColor(one, ubkColor, 0, 0)
        changeTextColor(two, ubkColor, 0, 0)
        changeTextColor(three, ubkColor, 0, 0)
        changeTextColor(four, ubkColor, 0, 0)
        changeTextColor(five, ubkColor, 0, 0)
        changeTextColor(six, ubkColor, 0, 0)
        changeTextColor(seven, ubkColor, 0, 0)
        changeTextColor(eight, ubkColor, 0, 0)
        changeTextColor(nine, ubkColor, 0, 0)
        changeTextColor(dot, ubkColor, 0, 0)
        changeTextColor(zero, ubkColor, 0, 0)
    }

    private fun getContrastColor(colorIntValue: Int): Int {
        val red = Color.red(colorIntValue)
        val green = Color.green(colorIntValue)
        val blue = Color.blue(colorIntValue)
        val lum = 0.299 * red + (0.587 * green + 0.114 * blue)
        return if (lum > 186) -0x1000000 else -0x1
    }

    fun equalFun() {

        var resultOnScreenVal = ""

        if (num1.text.isNotEmpty() && data != "") {
            num1data = data.toDouble()
        } else {
            Toast.makeText(this@MainActivity, "Please second number", Toast.LENGTH_LONG).show()
        }


        if (data != "" && operationTxt.text.isNotEmpty()) {
            // num1 op num2
            result = 0.0
            when (operationTxt.text.toString()) {
                "/" -> {
                    //check for zero division
                    //if(num1data==0.0) {resultOnScreenVal = "Not defined"}
                    //else
                    result = num2data / num1data
                }
                "x" -> result = num2data * num1data
                "+" -> result = num2data + num1data
                "-" -> result = num2data - num1data
            }
            //convert the result in to Locale Languauge
            for (i in result.toString()) {
                when (i) {
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

            if (resultByOpertion && operationAfterResult != "") {
                num2.text = resultOnScreenVal
                num2data = result
                num1.text = ""
                num1data = 0.0
                operationTxt.text = operationAfterResult
                operationAfterResult = ""
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
            R.id.two -> data = data.plus("2")
            R.id.three -> data = data.plus("3")
            R.id.four -> data = data.plus("4")
            R.id.five -> data = data.plus("5")
            R.id.six -> data = data.plus("6")
            R.id.seven -> data = data.plus("7")
            R.id.eight -> data = data.plus("8")
            R.id.nine -> data = data.plus("9")
            R.id.zero -> data = data.plus("0")
            R.id.dot -> {
                if (!data.contains(resources.getString(R.string.dot)))
                    data = data.plus(resources.getString(R.string.dot))
                else
                    Toast.makeText(
                        this@MainActivity,
                        "You can't have two decimal points",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
        num1.append((v as Button).text.toString())
    }

    fun clickerOperation(v: View) {

        if (num1.text.isNotEmpty() && num2.text.isNotEmpty()) {
            resultByOpertion = true
            operationAfterResult = (v as Button).text.toString()
            equalFun()
        }

        if (num2.text.isNotEmpty() && num1.text.isEmpty()) {
            operationTxt.text = (v as Button).text.toString()
        }

        if (num2.text.isEmpty() && num1.text.isNotEmpty()) {
            if (result != 0.0 && data == "") {
                data = result.toString()
            }
            if (data != "") {
                operationTxt.text = (v as Button).text.toString()
                num2.text = num1.text
                num2data = data.toDouble()
                data = ""
                num1.text = ""
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Please insert some number first",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}


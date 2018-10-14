package kutz.connor.metroid

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.location.Geocoder
import android.media.Image
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import java.util.*

class HomeScreenActivity : AppCompatActivity() {
    private val welcomeScreenShownPref = "welcomeScreenShown"
    private val sourceRememberedPref = "sourceRemembered"
    private val destinationRememberedPref = "destinationRemembered"
    private val sourcePref = "source"
    private val destinationPref = "destination"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)


        //initial setup and variable declarations
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val welcomeScreenShown = prefs.getBoolean(welcomeScreenShownPref, false)
        val sourceRemembered = prefs.getBoolean(sourceRememberedPref, false)
        val destinationRemembered = prefs.getBoolean(destinationRememberedPref, false)
        val sourceSwitch = findViewById<Switch>(R.id.sourceSwitch)
        val sourceText = findViewById<EditText>(R.id.sourceText)
        val destinationSwitch = findViewById<Switch>(R.id.destinationSwitch)
        val destinationText = findViewById<EditText>(R.id.destinationText)
        val sourceButton = findViewById<ImageButton>(R.id.sourceButton)
        val destinationButton = findViewById<ImageButton>(R.id.destinationButton)
        val geocoder = Geocoder(this, Locale.getDefault())


        if (!welcomeScreenShown) {
            val welcome = WelcomeDialogFragment()
            welcome.show(supportFragmentManager, "@string/welcome_fragment")
            val editor = prefs.edit()
            editor.putBoolean(welcomeScreenShownPref, true)
            editor.apply()
        }

        if (sourceRemembered) {
            sourceSwitch.setChecked(true)
            sourceText.setText((prefs.getString(sourcePref, "")))
        } else {
            sourceSwitch.setChecked(false)
            sourceText.setText("")
        }
        if (destinationRemembered) {
            destinationSwitch.setChecked(true)
            destinationText.setText(prefs.getString(destinationPref, ""))
        } else {
            destinationSwitch.setChecked(false)
            destinationText.setText("")
        }
        //setup finished

        val onClickListener = View.OnClickListener { view ->
            when(view.getId()){
                R.id.sourceButton -> {

                }
                R.id.destinationButton -> {

                }
                R.id.goButton -> {
                    Toast.makeText(this, "you clicked GO", Toast.LENGTH_SHORT).show()
                }
                R.id.alertsButton -> {

                }
            }
        }
        sourceButton.setOnClickListener(onClickListener)




    }

    override fun onPause() {
        super.onPause()
        setPrefs()
    }

    override fun onStop() {
        super.onStop()
        setPrefs()
    }

    fun setPrefs() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (findViewById<Switch>(R.id.sourceSwitch).isChecked()) {
            val editor = prefs.edit()
            editor.putBoolean(sourceRememberedPref, true)
            editor.putString(sourcePref, findViewById<EditText>(R.id.sourceText).text.toString())
            editor.apply()
        } else {
            val editor = prefs.edit()
            editor.putBoolean(sourceRememberedPref, false)
            editor.apply()
        }
        if (findViewById<Switch>(R.id.destinationSwitch).isChecked()) {
            val editor = prefs.edit()
            editor.putBoolean(destinationRememberedPref, true)
            editor.putString(destinationPref, findViewById<EditText>(R.id.destinationText).text.toString())
            editor.apply()
        } else {
            val editor = prefs.edit()
            editor.putBoolean(destinationRememberedPref, false)
            editor.apply()
        }

    }


    class WelcomeDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setMessage(R.string.welcome)
                        .setPositiveButton(R.string.lets_go,
                                DialogInterface.OnClickListener { _, _ ->
                                    //continue to app
                                })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}

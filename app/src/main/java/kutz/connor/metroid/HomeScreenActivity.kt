package kutz.connor.metroid

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager

class HomeScreenActivity : AppCompatActivity() {
    private val welcomeScreenShownPref = "welcomeScreenShown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val welcomeScreenShown = prefs.getBoolean(welcomeScreenShownPref, false);

        if (!welcomeScreenShown) {
            val welcome = WelcomeDialogFragment()
            welcome.show(supportFragmentManager, "@string/welcome_fragment")
            val editor = prefs.edit()
            editor.putBoolean(welcomeScreenShownPref, true)
            editor.apply()
        }
    }
}

class WelcomeDialogFragment : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.welcome)
                    .setPositiveButton(R.string.lets_go,
                            DialogInterface.OnClickListener{dialog, id ->
                                //continue to app
                            })
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }
}

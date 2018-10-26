package kutz.connor.metroid

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import org.jetbrains.anko.doAsync
import java.lang.Thread.sleep

class AlertsActivity : AppCompatActivity() {
    private lateinit var alertRecyclerView: RecyclerView
    private lateinit var alertViewAdapter: AlertViewAdapter
    private lateinit var alertViewManager: RecyclerView.LayoutManager

    fun userPressedOk(){
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)

        val metroManager = MetroManager(this)
        var alertData : MutableList<Incident>? = arrayListOf()
        doAsync {
            alertData = metroManager.getAlerts()
        }
        sleep(1000)
        if (alertData!!.isEmpty()){
            val alertDialog = AlertDialogFragment()
            alertDialog.show(supportFragmentManager,"@string/alert_fragment")
        }
        else {
            //alertData.add(AlertData("Green", "green line is out of service between L'enfant and Gallery Place"))
            //alertData.add(AlertData("Blue", "escalator broken at Foggy Bottom/GWU"))

            alertViewManager = LinearLayoutManager(this)
            alertViewAdapter = AlertViewAdapter(alertData)


            alertRecyclerView = findViewById<RecyclerView>(R.id.alert_recycler_view).apply {
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                setHasFixedSize(true)

                // use a linear layout manager
                layoutManager = alertViewManager

                adapter = alertViewAdapter
            }
        }
    }


    class AlertViewAdapter(private val dataset: List<Incident>?) : RecyclerView.Adapter<AlertViewAdapter.AlertViewHolder>(){
        class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): AlertViewAdapter.AlertViewHolder {
            // create a new view
            val alertView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycler_view_item, parent, false) as View
            // set the view's size, margins, paddings and layout parameters

            return AlertViewHolder(alertView)
        }

        override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
            //load data into a new row
            //holder.itemView.alert_image.setImageDrawable(ResourcesCompat.getDrawable(R.drawable.ic_warning_black_24dp))
            val lineString = "Line: "
            holder.itemView.alert_title.setText(lineString + dataset!!.get(position).linesAffected)
            holder.itemView.alert_message.setText(dataset.get(position).description)
        }


        // Return the size of your dataset (invoked by the layout manager)
        //no dataset yet, need to hit metro API
        override fun getItemCount() = dataset!!.size
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    class AlertDialogFragment : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.setMessage(R.string.no_alerts)
                        .setPositiveButton(R.string.OK,
                                DialogInterface.OnClickListener { _, _ ->
                                    //user clicked ok
                                })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}

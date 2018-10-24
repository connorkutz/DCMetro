package kutz.connor.metroid

import android.content.Context
import android.content.res.Resources
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class AlertsActivity : AppCompatActivity() {
    private lateinit var alertRecyclerView: RecyclerView
    private lateinit var alertViewAdapter: AlertViewAdapter
    private lateinit var alertViewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)

        val okHttpClient: OkHttpClient = OkHttpClient.Builder().let { builder ->

            // For printing request / response to logs
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)

            builder.connectTimeout(20, TimeUnit.SECONDS)
            builder.readTimeout(20, TimeUnit.SECONDS)
            builder.writeTimeout(20, TimeUnit.SECONDS)
            builder.build()
        }

        val metroAPIToken = getString(R.string.metro_api_token)
        val alertData = mutableListOf<AlertData>()
        alertData.add(AlertData("Green", "green line is out of service between L'enfant and Gallery Place"))
        alertData.add(AlertData("Blue", "escalator broken at Foggy Bottom/GWU"))

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


    class AlertViewAdapter(private val dataset: List<AlertData>) : RecyclerView.Adapter<AlertViewAdapter.AlertViewHolder>(){
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
            holder.itemView.alert_title.setText(lineString + dataset.get(position).line)
            holder.itemView.alert_message.setText(dataset.get(position).message)
        }


        // Return the size of your dataset (invoked by the layout manager)
        //no dataset yet, need to hit metro API
        override fun getItemCount() = dataset.size
    }


   data class AlertData(val line: String, val message: String)
}

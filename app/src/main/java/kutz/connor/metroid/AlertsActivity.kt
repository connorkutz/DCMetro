package kutz.connor.metroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class AlertsActivity : AppCompatActivity() {
    private lateinit var alertRecyclerView: RecyclerView
    private lateinit var alertViewAdapter: RecyclerView.Adapter<*>
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


            alertRecyclerView = findViewById<RecyclerView>(R.id.alert_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = alertViewManager

            adapter = alertViewAdapter

        }
    }


    class AlertViewAdapter() : RecyclerView.Adapter<AlertViewAdapter.AlertViewHolder>(){
        class AlertViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): AlertViewAdapter.AlertViewHolder {
            // create a new view
            val textView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.my_text_view, parent, false) as TextView
            // set the view's size, margins, paddings and layout parameters

            return AlertViewHolder(textView)
        }

        override fun onBindViewHolder(p0: AlertViewHolder, p1: Int) {
            //do nothing
        }


        // Return the size of your dataset (invoked by the layout manager)
        //no dataset yet, need to hit metro API
        override fun getItemCount() = 0
    }
}

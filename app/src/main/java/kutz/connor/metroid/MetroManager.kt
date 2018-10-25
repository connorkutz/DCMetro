package kutz.connor.metroid

import android.content.Context
import android.location.Address
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import org.json.JSONObject


class MetroManager(context: Context) {

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().let { builder ->
        // For printing request / response to logs
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        // Network timeouts
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.readTimeout(20, TimeUnit.SECONDS)
        builder.writeTimeout(20, TimeUnit.SECONDS)
        builder.build()
    }
    private var apiToken = context.getString(R.string.metro_api_token)

    fun getNearestStation(address: Address): Entrance? {
        val lon = address.longitude
        val lat = address.latitude
        val radius = 1600
        val request = Request.Builder()
                .url("https://api.wmata.com/Rail.svc/json/jStationEntrances?Lat=$lat&Lon=$lon&Radius=$radius")
                .header("api_key", apiToken).build()

        val call = okHttpClient.newCall(request)
        val response = call.execute()
        val body = response.body()
        val responseString: String? = response.body()?.string()
        if (response.isSuccessful && body != null) {
            val jsonEntrances = JSONObject(responseString).getJSONArray("Entrances")
            val curr = jsonEntrances.getJSONObject(0)
            val entrance = Entrance(curr.getString("Description"))
            entrance.lat = curr.getDouble("Lat")
            entrance.lon = curr.getDouble("Lon")
            entrance.name = curr.getString("Name")
            entrance.stationCode1 = curr.getString("StationCode1")
            entrance.stationCode2 = curr.getString("StationCode2")
            return entrance
        }
        return null

    }


    fun getAlerts(){

    }



}


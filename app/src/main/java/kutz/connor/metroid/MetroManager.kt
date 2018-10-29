package kutz.connor.metroid

import android.content.Context
import android.location.Address
import com.google.android.gms.maps.model.LatLng
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
            if (jsonEntrances.length() < 1){return null}
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

    fun getAlerts(): MutableList<Incident>? {
        val request = Request.Builder()
                .header("api_key", apiToken)
                .url("https://api.wmata.com/Incidents.svc/json/Incidents").build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()
        val body = response.body()
        val responseString = response.body()?.string()
        if(response.isSuccessful && body != null){
            val incidents : MutableList<Incident> = arrayListOf()
            val jsonIncidents = JSONObject(responseString).getJSONArray("Incidents")
            for(i in 0 until jsonIncidents.length()){
                val curr = jsonIncidents.getJSONObject(i)
                val incident = Incident(curr.getString("Description"))
                incident.dateUpdated = curr.getString("DateUpdated")
                incident.incidentID = curr.getString("IncidentID")
                incident.incidentType = curr.getString("IncidentType")
                incident.linesAffected = curr.getString("LinesAffected")
                incidents.add(incident)
            }
            return incidents
        }
        else {
            return null
        }
    }

    fun getPath(fromStationCode : String?, toStationCode : String?) :MutableList<MetroPathItem>?{
        if(fromStationCode.equals("") || toStationCode.equals("")){
            return null
        }
        val request = Request.Builder()
                .header("api_key", apiToken)
                .url("https://api.wmata.com/Rail.svc/json/jPath?FromStationCode=$fromStationCode&ToStationCode=$toStationCode").build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()
        val body = response.body()
        val responseString = response.body()?.string()
        if(response.isSuccessful && body != null){
            val pathItems : MutableList<MetroPathItem> = arrayListOf()
            val jsonPathItems = JSONObject(responseString).getJSONArray("Path")
            if(jsonPathItems.length() == 0){
                return null
            }
            for(i in 0 until jsonPathItems.length()){
                val curr = jsonPathItems.getJSONObject(i)
                val pathItem = MetroPathItem(curr.getInt("DistanceToPrev"))
                pathItem.lineCode = curr.getString("LineCode")
                pathItem.seqNum = curr.getInt("SeqNum")
                pathItem.stationCode = curr.getString("StationCode")
                pathItem.stationName = curr.getString("StationName")
                pathItems.add(pathItem)
            }
            return pathItems
        }
        else {
            return null
        }
    }

    fun getStationLatLng(stationCode : String?) : LatLng?{
        if(stationCode.equals("") || stationCode == null){
            return null
        }
        val request = Request.Builder()
                .header("api_key", apiToken)
                .url("https://api.wmata.com/Rail.svc/json/jStationInfo?StationCode=$stationCode").build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()
        val body = response.body()
        val responseString = response.body()?.string()
        if(response.isSuccessful && body != null){
            val responseJSONObject = JSONObject(responseString)
            val lat = responseJSONObject.getDouble("Lat")
            val lon = responseJSONObject.getDouble("Lon")
            return LatLng(lat, lon)
        }
        else {
            return null
        }
    }


}


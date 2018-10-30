package kutz.connor.metroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.support.design.widget.FloatingActionButton
import com.google.android.gms.maps.model.PolylineOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val intentSourceStationLon = "source_station_lon"
        const val intentSourceStationLat = "source_station_lat"
        const val intentDestinationStationLon = "destination_station_lon"
        const val intentDestinationStationLat = "destination_station_lat"
        const val intentDestinationLat = "destination_lat"
        const val intentDestinationLon = "destination_lon"
        const val intentDestinationName = "destination_name"
        const val intentPathList = "path_list"
        const val intentPathColor = "path_color"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val destinationLat = intent.getDoubleExtra(intentDestinationLat, 0.0)
        val destinationLon = intent.getDoubleExtra(intentDestinationLon, 0.0)
        val destinationName = intent.getStringExtra(intentDestinationName)
        val googleMapsIntentButton = findViewById<FloatingActionButton>(R.id.mapIntentButton)
        val shareButton = findViewById<FloatingActionButton>(R.id.shareButton)
        val lineColorInt = intent.getIntExtra(intentPathColor, 0)
        val lineColorString = getColorString(lineColorInt)

        googleMapsIntentButton.setOnClickListener{
            val intent = Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=$destinationLat,$destinationLon"))
            startActivity(intent)
        }
        shareButton.setOnClickListener {
            val intent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Metroing to $destinationName via the $lineColorString line")
                type = "text/plain"
            }
            startActivity(intent)
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        val sourceLat = intent.getDoubleExtra(intentSourceStationLat, 0.0)
        val sourceLon = intent.getDoubleExtra(intentSourceStationLon, 0.0)
        val destinationLat = intent.getDoubleExtra(intentDestinationStationLat, 0.0)
        val destinationLon = intent.getDoubleExtra(intentDestinationStationLon, 0.0)
        val latLngList = intent.getParcelableArrayListExtra<LatLng>(intentPathList)
        val pathColor = intent.getIntExtra(intentPathColor, 0)


        val polyLineOptions = PolylineOptions()
        polyLineOptions.addAll(latLngList)
        polyLineOptions.color(pathColor)

        googleMap.addPolyline(polyLineOptions)

        if(sourceLat != 0.0 && destinationLat != 0.0) {
            googleMap.addMarker(
                    MarkerOptions().position(LatLng(sourceLat, sourceLon)).title("Source Station")
            )
            googleMap.addMarker(
                    MarkerOptions().position(LatLng(destinationLat, destinationLon)).title("Destination Station")
            )
            val latLngBounds = LatLngBounds.builder()
                    .include(LatLng(sourceLat, sourceLon))
                    .include(LatLng(destinationLat, destinationLon))
                    .build()
            val padding = 175
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, width, height, padding))
        }
    }

    private fun getColorString(colorInt : Int) : String{
        if (colorInt == Color.BLUE){
            return "blue"
        }
        if (colorInt == Color.RED){
            return "red"
        }
        if (colorInt == Color.YELLOW){
            return "yellow"
        }
        if (colorInt == Integer.parseInt("FF8C00")){
            return "orange"
        }
        if (colorInt == Color.GREEN){
            return "green"
        }
        if (colorInt == Color.GRAY){
            return "silver"
        }
        return "oops"
    }
}

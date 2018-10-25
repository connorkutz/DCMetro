package kutz.connor.metroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        val intentSourceStationLon = "source_station_lon"
        val intentSourceStationLat = "source_station_lat"
        val intentDestinationStationLon = "destination_station_lon"
        val intentDestinationStationLat = "destination_station_lat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)

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

        if(sourceLat != 0.0 && destinationLat != 0.0) {
            googleMap.addMarker(
                    MarkerOptions().position(LatLng(sourceLat, sourceLon)).title("Source Station")
            )
            googleMap.addMarker(
                    MarkerOptions().position(LatLng(destinationLat, destinationLon)).title("Destination Station")
            )
        }
    }
}

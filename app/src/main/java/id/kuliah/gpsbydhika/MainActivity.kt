package id.kuliah.gpsbydhika

import android.content.pm.PackageManager
import android.location.LocationProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    val REQUEST_CODE = 1000

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            REQUEST_CODE->{
                if(grantResults.size>0){
                    if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this@MainActivity, "Permission Dennied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
        else{
            buildLocationRequest()
            buildLocationCallBack()
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        bt_start_update.setOnClickListener {

            if(ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                return@setOnClickListener
            }

            fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
            bt_start_update.isEnabled != bt_start_update.isEnabled
            bt_stop_update.isEnabled != bt_stop_update.isEnabled
        }

        bt_stop_update.setOnClickListener {

            if(ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                return@setOnClickListener
            }

            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            bt_start_update.isEnabled != bt_start_update.isEnabled
            bt_stop_update.isEnabled != bt_stop_update.isEnabled
        }
    }

    private fun buildLocationCallBack() {
        locationCallback = object:LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                var location = p0!!.locations.get(p0!!.locations.size-1)
                tv_location.text = location.latitude.toString()+"/"+location.longitude.toString()
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }
}

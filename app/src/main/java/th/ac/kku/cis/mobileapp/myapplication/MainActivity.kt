package th.ac.kku.cis.mobileapp.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = getIntent().getExtras()!!.getString("name")

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
        user_name.text=name

        button.setOnClickListener{
            val intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
        }
        button5.setOnClickListener{
            val intent = Intent(this, Main4Activity::class.java)
            startActivity(intent)
        }
        button6.setOnClickListener{
            val intent = Intent(this, Main5Activity::class.java)
            startActivity(intent)
        }
    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result

                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val id = getIntent().getExtras()!!.getString("id")
                        val calendar: Calendar = Calendar.getInstance()
                        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        val formattedDate = format.format(calendar.time)
                        mDatabase = FirebaseDatabase.getInstance().reference
                        val newItem: DatabaseReference = mDatabase.child("user_point").push()
                        val user_point = user_point.create()
                        user_point.id = id
                        user_point.date_time = formattedDate
                        user_point.lat = location.latitude.toString()
                        user_point.lon = location.longitude.toString()
                        user_point.objectId = newItem.key
                        newItem.setValue(user_point)
//                        point.text = "Latitude : "+location.latitude.toString()+"\nLongtitude : "+location.longitude.toString()
//                        lat = location.latitude.toString()
//                        lon = location.longitude.toString()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback, Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
//            point.text = "Latitude : "+mLastLocation.latitude.toString()+"\nLongtitude : "+mLastLocation.longitude.toString()
//            lat = mLastLocation.latitude.toString()
//            lon = mLastLocation.longitude.toString()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}

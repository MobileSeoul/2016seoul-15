package kr.edcan.buspolis

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import org.jetbrains.anko.startActivity

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        title = getString(R.string.title_activity_maps)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mapHelp.setOnClickListener {
            startActivity<HelpActivity>("name" to intent.getStringExtra("name_ko"), "id" to intent.getIntExtra("id", 0)
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val station = LatLng(intent.getDoubleExtra("y", 0.0), intent.getDoubleExtra("x", 0.0))
        mMap!!.addMarker(MarkerOptions().position(station).title(intent.getStringExtra("name")).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_busstop_maps_location)))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(station,18f))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

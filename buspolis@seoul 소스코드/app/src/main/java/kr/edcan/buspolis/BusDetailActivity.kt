package kr.edcan.buspolis

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.MenuItem
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_bus_detail.*
import kr.edcan.buspolis.databinding.ActivityBusDetailBinding
import kr.edcan.buspolis.model.RM_Bus
import kr.edcan.buspolis.util.Utils

class BusDetailActivity : AppCompatActivity() {

    var color = "#54A953"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityBusDetailBinding>(this, R.layout.activity_bus_detail)
        setLayout()
        setData()
    }

    private fun setLayout() {
        setSupportActionBar(toolbar)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        title = getString(R.string.information)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setData() {
        var realm = Realm.getDefaultInstance()
        var bus = realm.where(RM_Bus::class.java).equalTo("id", intent.getIntExtra("id", 0)).findFirst()
        busRoute.text = "${bus.start} -> ${bus.end}"
        busRouteContent.text = Utils.getBusType(this, bus.type).getLocalName()
        // TODO Bus Service Hour Text
        busServiceHour.text = Html.fromHtml(
                "<font color='#4db6ac'>${getString(R.string.first_bus)} : </font>${bus.first.substring(0..1)}:${bus.first.substring(2..3)} ~ <font color='#4db6ac'>${getString(R.string.last_bus)} : </font>${bus.last.substring(0..1)}:${bus.last.substring(2..3)}"
        )
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

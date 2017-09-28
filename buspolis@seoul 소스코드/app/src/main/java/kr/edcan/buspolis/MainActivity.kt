package kr.edcan.buspolis

import android.app.Activity
import android.content.Intent
import android.databinding.ObservableArrayList
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.github.nitrico.lastadapter.LastAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.loopj.android.http.AsyncHttpResponseHandler
import com.yayandroid.locationmanager.LocationBaseActivity
import com.yayandroid.locationmanager.LocationConfiguration
import com.yayandroid.locationmanager.constants.ProviderType
import cz.msebera.android.httpclient.Header
import es.dmoral.prefs.Prefs
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kr.edcan.buspolis.model.BusStop
import kr.edcan.buspolis.model.MultiString
import kr.edcan.buspolis.model.RM_Station
import kr.edcan.buspolis.model.SearchItem
import kr.edcan.buspolis.util.Utils
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.textColor
import org.json.XML
import java.util.*

class MainActivity : LocationBaseActivity() {

    var sList = ObservableArrayList<Any>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLayout()
        getLocation()
    }

    private fun setLayout() {
        setSupportActionBar(toolbar)
        sList.clear()
        title = ""
        MultiString(this, "Where are you going?", "你去哪里?", "どこに行くの？").let {
            toolbarTitle.text = it.getLocalName()
            toolbarSubtitle.text = it.getEngSub()
        }
        sList.add(Utils.loadBS(this))
        var hisText = Prefs.with(this).read("searchHistory", "")
        var history = if(hisText == "") ArrayList<SearchItem>()
        else Gson().fromJson(hisText,  object: TypeToken<ArrayList<SearchItem>>() {}.type)
        for(i in history.reversed()){
            sList.add(i)
        }
        LinearLayoutManager(this).let {
            it.orientation = LinearLayoutManager.VERTICAL
            mainRecycler.layoutManager = it
        }
        LastAdapter.with(sList, BR.item)
                .map<SearchItem>(R.layout.item_search)
                .map<BusStop>(R.layout.content_main_header)
                .onBind {
                    if (item is BusStop){
                        view.find<TextView>(R.id.refreshNear).setOnClickListener {
                            getLocation()
                        }
                    }
                    if(item is SearchItem){
                        var keyword = view.find<TextView>(R.id.searchKeyword)
                        var sub = view.find<TextView>(R.id.searchSub)
                        var sData = (item as SearchItem)
                        when(sData.type){
                            SearchItem.listType.BUS ->{
                                keyword.textColor = ContextCompat.getColor(this@MainActivity, Utils.backgroundColor(sData.option))
                                sub.text = Utils.getBusType(this@MainActivity, sData.option).getLocalName()
                            }
                            SearchItem.listType.BUSSTOP ->{
                                keyword.textColor = ContextCompat.getColor(this@MainActivity, R.color.textNormal)
                                sub.text = sData.num
                            }
                        }
                    }
                }
                .onClickListener(object : LastAdapter.OnClickListener {
                    override fun onClick(item: Any, view: View, type: Int, position: Int) {
                        if (position == 0) return
                        item as SearchItem
                        if(type == R.layout.item_search){
                            Utils.putHistory(this@MainActivity, item)
                            when(item.type){
                                SearchItem.listType.BUS ->{
                                    startActivityForResult<BusInfoActivity>(100, "id" to item.id)
                                }
                                SearchItem.listType.BUSSTOP ->{
                                    startActivityForResult<BusStopInfoActivity>(100, "id" to item.id)
                                }
                            }
                        }
                    }
                })
                .into(mainRecycler)

        searchLay.setOnClickListener {
            startActivityForResult<SearchActivity>(300)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.mainSettings -> {
                startActivityForResult<SettingActivity>(200)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 200 && resultCode == Activity.RESULT_OK){
            setLayout()
            getLocation()
        }
        if(requestCode == 300 && resultCode == Activity.RESULT_OK){
            setLayout()
            getLocation()
        }
        if(requestCode == 100){
            setLayout()
            getLocation()
        }
    }

    var inGetLocation = false
    override fun onLocationChanged(location: Location) {
        if(inGetLocation) return
        inGetLocation = true
        Utils.getNearStop(location.longitude, location.latitude, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {
                val result = XML.toJSONObject(String(responseBody)).getJSONObject("ServiceResult")
                if(result.getJSONObject("msgHeader").getInt("headerCd") != 0){
                    onFailure(statusCode, headers, responseBody, null)
                    return
                }else{
                    var item = result.getJSONObject("msgBody").getJSONArray("itemList").getJSONObject(0)
                    var station = Realm.getDefaultInstance().where(RM_Station::class.java).equalTo("id", item.getInt("stationId")).findFirst()
                    sList[0] = BusStop(this@MainActivity, station)
                    inGetLocation = false
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                sList[0] = Utils.errBS(this@MainActivity)
                inGetLocation = false
            }
        })
        mainRecycler.adapter.notifyDataSetChanged()
    }

    override fun onLocationFailed(failType: Int) {
    }

    override fun getLocationConfiguration(): LocationConfiguration {
        return LocationConfiguration()
                .keepTracking(true)
                .askForGooglePlayServices(true)
                .setMinAccuracy(200.0f)
                .setTimeInterval((Prefs.with(this).readInt("autoRef", 30) * 1000).toLong())
                .setWaitPeriod(ProviderType.GOOGLE_PLAY_SERVICES, 5 * 1000)
                .setWaitPeriod(ProviderType.GPS, 10 * 1000)
                .setWaitPeriod(ProviderType.NETWORK, 5 * 1000)
                .setGPSMessage("Would you mind to turn GPS on?")
                .setRationalMessage("Gimme the permission!")!!
    }
}

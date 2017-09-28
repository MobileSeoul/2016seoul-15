package kr.edcan.buspolis

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.TextView
import com.github.nitrico.lastadapter.LastAdapter
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_search.*
import kr.edcan.buspolis.databinding.ActivitySearchBinding
import kr.edcan.buspolis.model.*
import kr.edcan.buspolis.util.Utils
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import java.util.*
import kotlin.properties.Delegates

class SearchActivity : AppCompatActivity() {

    var sList  = ObservableArrayList<SearchItem>()

    var realm by Delegates.notNull<Realm>()
    var busType = ArrayList<MultiString>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)
        realm = Realm.getDefaultInstance()
        setLayout()
    }

    private fun setLayout() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        LinearLayoutManager(this).let {
            it.orientation = LinearLayoutManager.VERTICAL
            searchRecycler.layoutManager = it
        }

        LastAdapter.with(sList, BR.item)
                .map<SearchItem>(R.layout.item_search)
                .onBind {
                    var keyword = view.find<TextView>(R.id.searchKeyword)
                    var sub = view.find<TextView>(R.id.searchSub)
                    var sData = (item as SearchItem)

                    when(sData.type){
                        SearchItem.listType.BUS ->{
                            keyword.textColor = ContextCompat.getColor(this@SearchActivity, Utils.backgroundColor(sData.option))
                            sub.text = Utils.getBusType(this@SearchActivity, sData.option).getLocalName()
                        }
                        SearchItem.listType.BUSSTOP ->{
                            keyword.textColor = ContextCompat.getColor(this@SearchActivity, R.color.textNormal)
                            sub.text = sData.num
                        }
                    }
                }
                .onClick {
                    var sData = (item as SearchItem)
                    Utils.putHistory(this@SearchActivity, sData)
                    setResult(Activity.RESULT_OK)
                    when(sData.type){
                        SearchItem.listType.BUS ->{
                            startActivity<BusInfoActivity>("id" to sData.id)
                        }
                        SearchItem.listType.BUSSTOP ->{
                            startActivity<BusStopInfoActivity>("id" to sData.id)
                        }
                    }
                }
                .into(searchRecycler)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                sList.clear()
                if(s.toString() == ""){
                    searchRecycler.adapter.notifyDataSetChanged()
                    return
                }
                for( bus in realm.where(RM_Bus::class.java).contains("num", s.toString()).findAll() ){
                    sList.add(SearchItem(bus.id, bus.num, SearchItem.listType.BUS, bus.type))
                }
                for( rmSt in realm.where(RM_Station::class.java).contains("name", s.toString()).or().contains("name_en", s.toString()).or().contains("name_cn", s.toString()).or().contains("name_jp", s.toString()).or().contains("num", s.toString()).findAll() ){
                    var station = BusStop(this@SearchActivity, rmSt)
                    sList.add(SearchItem(station.id, station.name.getLocalName(), SearchItem.listType.BUSSTOP, station.code))
                }
                searchRecycler.adapter.notifyDataSetChanged()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            android.R.id.home -> {
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

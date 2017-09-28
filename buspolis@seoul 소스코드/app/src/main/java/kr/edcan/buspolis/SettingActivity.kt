package kr.edcan.buspolis

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.github.nitrico.lastadapter.LastAdapter
import es.dmoral.prefs.Prefs
import kotlinx.android.synthetic.main.activity_setting.*
import kr.edcan.buspolis.databinding.ContentSettingsHeaderBinding
import kr.edcan.buspolis.databinding.ItemSettingsBinding
import kr.edcan.buspolis.model.ListContent
import org.jetbrains.anko.selector
import java.util.*

class SettingActivity : AppCompatActivity(), LastAdapter.OnClickListener {
    var arrayList: ArrayList<Any> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        title = "Settings"
        setLayout()
    }

    private fun setLayout() {
        LinearLayoutManager(this).let {
            it.orientation = LinearLayoutManager.VERTICAL
            settingsRecycler.layoutManager = it
        }

        arrayList.run {
            add(getString(R.string.setting_general))
            add(ListContent(getString(R.string.setting_refresh),
                    getString(R.string.setting_refresh_sub),
                    "${Prefs.with(this@SettingActivity).readInt("autoRef", 30)}s"))
            add(ListContent(getString(R.string.setting_lang),
                    getString(R.string.setting_lang_sub),
                    getLangString()))
//            add(getString(R.string.setting_term))
//            add(ListContent(getString(R.string.setting_tos)))
//            add(ListContent(getString(R.string.setting_osl)))
            add(getString(R.string.setting_about))
            add(ListContent(getString(R.string.setting_ver),
                    "",getVersion()))
            add(ListContent(getString(R.string.setting_email),
                    getString(R.string.setting_email_sub)))
        }

        LastAdapter.with(arrayList, BR.item)
                .onBindListener(object: LastAdapter.OnBindListener{
                    override fun onBind(item: Any, view: View, type: Int, position: Int) {
                        when(type){
                            R.layout.content_settings_header -> {
                                val binding = DataBindingUtil.getBinding<ContentSettingsHeaderBinding>(view)
                                binding.title.text = arrayList[position].toString()
                            }
                            R.layout.item_settings -> {
                                val contentBinding = DataBindingUtil.getBinding<ItemSettingsBinding>(view)
                                item as ListContent
                                if(item.content.equals("")) contentBinding.content.visibility = View.GONE
                            }
                        }
                    }
                })
                .onClickListener(this)
                .map<String>(R.layout.content_settings_header)
                .map<ListContent>(R.layout.item_settings)
                .into(settingsRecycler)
    }

    fun  getVersion() = packageManager.getPackageInfo(packageName, 0).versionName!!

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun getLangString() =
            when(Prefs.with(this).read("lang")){
                "en" -> "ENG"
                "cn" -> "中文"
                "jp" -> "日本語"
                else -> ""
            }
    override fun onClick(item: Any, view: View, type: Int, position: Int) {
        when(position){
            1 ->{ //auto
                selector(getString(R.string.setting_refresh), listOf("15s", "30s", "60s")){i ->
                    var secs = arrayOf(15, 30, 60)
                    Prefs.with(this@SettingActivity).writeInt("autoRef", secs[i])
                    arrayList[1] = ListContent(getString(R.string.setting_refresh),
                            getString(R.string.setting_refresh_sub),
                            "${Prefs.with(this@SettingActivity).readInt("autoRef", 30)}s")
                    settingsRecycler.adapter.notifyDataSetChanged()
                }
            }
            2 ->{ //lang
                selector(getString(R.string.setting_refresh), listOf("ENG", "中文", "日本語")){i ->
                    var langs = arrayOf("en", "cn", "jp")
                    Prefs.with(this).write("lang", langs[i])
                    arrayList[2] = ListContent(getString(R.string.setting_lang),
                            getString(R.string.setting_lang_sub),
                            getLangString())
                    setResult(Activity.RESULT_OK)
                    settingsRecycler.adapter.notifyDataSetChanged()
                }
            }
//            4 ->{ //ToS
//
//            }
//            5 ->{ //OSL
//
//            }
            5 ->{ //contact
                val uri = Uri.parse("mailto:lntcs.dev@gmail.com")
                val it = Intent(Intent.ACTION_SENDTO, uri)
                startActivity(it)
            }
        }
    }
}

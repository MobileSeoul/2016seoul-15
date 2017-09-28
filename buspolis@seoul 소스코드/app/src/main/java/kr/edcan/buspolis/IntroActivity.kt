package kr.edcan.buspolis

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.github.jksiezni.permissive.Permissive
import es.dmoral.prefs.Prefs
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_intro.*
import kr.edcan.buspolis.adapter.IntroPagerAdapter
import kr.edcan.buspolis.model.RM_Bus
import kr.edcan.buspolis.model.RM_Station
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset

class IntroActivity : AppCompatActivity() {

    var chkedNum = 0
    var isCovered = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.activity_intro)

        Glide.with(this).load(R.drawable.bg_start).into(introBg)

        var langs = arrayOf(langEng, langCn, langJp)
        langs.forEachIndexed { i, view ->
            view.setOnClickListener {
                if(!isCovered) return@setOnClickListener
                chkedNum = i
                for (j in 0..langs.size - 1) {
                    if (i == j) {
                        langs[j].setBackgroundResource(R.drawable.selector_fill)
                        langs[j].textColor = Color.BLACK
                    } else {
                        langs[j].setBackgroundResource(R.drawable.selector_border)
                        langs[j].textColor = Color.WHITE
                    }
                }
            }
        }
        introNext.setOnClickListener {
            if(isCovered) {
                isCovered = false
                Prefs.with(this).write("lang", arrayOf("en","cn","jp")[chkedNum])
                introPager.adapter = IntroPagerAdapter(this)
                introCover.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out))
                introNext.textColor = ContextCompat.getColor(this, R.color.colorAccent)

                setRealmData()
            }else{
                if(introPager.currentItem == 1){
                    if(android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP){
                        Prefs.with(this).writeBoolean("isFirst", false)
                        startActivity<MainActivity>()
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                        finish()
                    }
                    introNext.text = getString(R.string.set_permission)
                }else if(introPager.currentItem == 2){
                    Permissive.Request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                            .whenPermissionsGranted {
                                Prefs.with(this).writeBoolean("isFirst", false)
                                startActivity<MainActivity>()
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                                finish()
                            }
                            .whenPermissionsRefused({ toast(getString(R.string.perm_denied)) })
                            .execute(this)
                }
                introPager.setCurrentItem(introPager.currentItem+1, true)
            }
        }
    }

    fun setRealmData(){
        var pDlg = indeterminateProgressDialog(getString(R.string.wait), getString(R.string.patch), {
            setCancelable(false)
        })
        var realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync(Realm.Transaction {
            it.createOrUpdateAllFromJson(RM_Station::class.java, loadJSONFromAsset("stations.json", "stations")!!)
            it.createOrUpdateAllFromJson(RM_Bus::class.java, loadJSONFromAsset("routes.json", "routes")!!)
        }, Realm.Transaction.OnSuccess {
            pDlg.dismiss()
        })
    }

    fun loadJSONFromAsset(name: String, array: String): JSONArray? {
        var json: JSONArray? = null
        try {
            val `is` = assets.open(name)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = JSONObject(String(buffer, Charset.forName("UTF-8"))).getJSONArray(array)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return json
    }
}

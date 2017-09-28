package kr.edcan.buspolis

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import es.dmoral.prefs.Prefs
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            if (Prefs.with(this).readBoolean("isFirst", true)) {
                startActivity<IntroActivity>()
            } else {
                startActivity<MainActivity>()
            }
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, 1500)
    }
}

package kr.edcan.buspolis

import android.app.Application
import io.realm.Realm

/**
 * Created by LNTCS on 2016-10-28.
 */
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}
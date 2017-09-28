package kr.edcan.buspolis.model

import android.content.Context
import android.content.Intent
import android.view.View
import kr.edcan.buspolis.BusStopInfoActivity
import kr.edcan.buspolis.HelpActivity
import kr.edcan.buspolis.util.Utils
import org.jetbrains.anko.selector
import org.jetbrains.anko.startActivity

/**
 * Created by LNTCS on 2016-10-23.
 */
class BusStop{
    var id = 0
    var name = MultiString()
    var code = ""
    var x = 0.0
    var y = 0.0

    constructor(name: MultiString, code: String){
        this.name = name
        this.code = code
    }

    constructor(name: MultiString) {
        this.name = name
    }

    var helpListener = View.OnClickListener{
        if(id != 0)
            it.context.startActivity<HelpActivity>("name" to name.ko, "id" to id)
    }

    var infoListener = View.OnClickListener{
        if(id != 0)
            it.context.startActivity<BusStopInfoActivity>("id" to id)
    }

    var shareListener = View.OnClickListener{
        if(id != 0){
            it.context.run {
                selector("", listOf("ENG", "中文", "日本語", "한국어"),{ i ->
                    var name = this@BusStop.name
                    var msg: String = when(i){
                        0 -> {
                            "Now I'm near '${name.en}' bus stop."
                        }
                        1 -> {
                            "我现在是附近的“${name.cn}”巴士站。"
                        }
                        2 -> {
                            "私は今、「${name.jp}」バス停の近くにあります。"
                        }
                        else -> {
                            "나는 지금 '${name.ko}' 정류장 근처에 있어."
                        }
                    }
                    val sendIntent = Intent()
                    msg += "\nhttps://maps.google.com/?q=@$y,$x"
                    sendIntent.putExtra(Intent.EXTRA_TEXT, msg)
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                })
            }
        }
    }

    constructor(context: Context, station: RM_Station){
        this.id = station.id
        this.name = MultiString(context, station.name_en, station.name_cn, station.name_jp, station.name)
        this.code = Utils.convertCode(station.num)
        this.x = station.x
        this.y = station.y
    }
}
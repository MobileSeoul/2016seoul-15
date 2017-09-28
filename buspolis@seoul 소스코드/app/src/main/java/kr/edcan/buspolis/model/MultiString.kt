package kr.edcan.buspolis.model

import android.content.Context
import kr.edcan.buspolis.util.Utils
import kotlin.properties.Delegates

/**
 * Created by LNTCS on 2016-10-25.
 */

class MultiString {
    var mContext by Delegates.notNull<Context>()

    var en = "'"
    var cn = "'"
    var jp = "'"
    var ko = "'"

    constructor()

    constructor(context: Context, en: String, cn: String, jp: String){
        this.mContext = context
        this.en = en
        this.cn = cn
        this.jp = jp
    }

    constructor(context: Context, cn: String, jp: String){
        this.mContext = context
        this.cn = cn
        this.jp = jp
    }

    constructor(context: Context, en: String, cn: String, jp: String, ko: String){
        this.mContext = context
        this.en = en
        this.cn = cn
        this.jp = jp
        this.ko = ko
    }

    constructor(context: Context, main: Int, sub: Int){
        this.mContext = context
        this.en = context.getString(main)
        this.cn = context.getString(main)
        this.jp = context.getString(main)
        this.ko = context.getString(sub)
    }

    fun get(key: String): String{
        return when(key){
            "en" -> en
            "cn" -> cn
            "jp" -> jp
            "ko" -> ko
            else -> ""
        }
    }

    fun getLocalName() = get(Utils.lang(mContext))

    fun getEngSub(): String{
        return if(Utils.lang(mContext) == "en" || en == jp){
            ""
        }else {
            get("en")
        }
    }
}

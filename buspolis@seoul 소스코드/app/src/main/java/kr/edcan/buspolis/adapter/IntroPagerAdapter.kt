package kr.edcan.buspolis.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kr.edcan.buspolis.R
import kr.edcan.buspolis.model.MultiString
import kr.edcan.buspolis.util.Utils
import org.jetbrains.anko.find

/**
 * Created by LNTCS on 2016-03-11.
 */
class IntroPagerAdapter(internal var mContext: Context) : PagerAdapter() {

    private val mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
        return 3
    }

    override fun instantiateItem(pager: View?, position: Int): Any {
        val v = mInflater.inflate(R.layout.content_intro_pager, null)
        when(position){
            0 -> {
                v.find<TextView>(R.id.introTitle).text = MultiString(mContext, "Hello!", "你好!", "ようこそ!").getLocalName()
                v.find<TextView>(R.id.introSubtitle).text = MultiString(mContext, mContext.getString(R.string.intro_subtitle1_en), mContext.getString(R.string.intro_subtitle1_cn), mContext.getString(R.string.intro_subtitle1_jp)).getLocalName()
                Glide.with(mContext).load(R.drawable.img_tutorial1).into(v.find<ImageView>(R.id.introImg))
            }
            1 -> {
                v.find<TextView>(R.id.introTitle).text = MultiString(mContext, "Name Translated", "名称已翻译", "名前翻訳").getLocalName()
                v.find<TextView>(R.id.introSubtitle).text = MultiString(mContext, mContext.getString(R.string.intro_subtitle2_en), mContext.getString(R.string.intro_subtitle2_cn), mContext.getString(R.string.intro_subtitle2_jp)).getLocalName()
                var imgs = mapOf("en" to R.drawable.img_tutorial2_en, "cn" to R.drawable.img_tutorial2_cn, "jp" to R.drawable.img_tutorial2_jp)
                Glide.with(mContext).load(imgs[Utils.lang(mContext)]).into(v.find<ImageView>(R.id.introImg))
            }
            2 -> {
                v.find<TextView>(R.id.introTitle).text = MultiString(mContext, "Please Permission!", "許可を確認！", "检查许可！").getLocalName()
                v.find<TextView>(R.id.introSubtitle).text = MultiString(mContext, mContext.getString(R.string.intro_subtitle3_en), mContext.getString(R.string.intro_subtitle3_cn), mContext.getString(R.string.intro_subtitle3_jp)).getLocalName()
                Glide.with(mContext).load(R.drawable.img_tutorial3).into(v.find<ImageView>(R.id.introImg))
            }
        }
        (pager as ViewPager).addView(v, null)
        return v
    }

    override fun destroyItem(pager: View?, position: Int, view: Any?) {
        (pager as ViewPager).removeView(view as View?)
    }

    override fun isViewFromObject(v: View, obj: Any): Boolean {
        return v === obj
    }
}
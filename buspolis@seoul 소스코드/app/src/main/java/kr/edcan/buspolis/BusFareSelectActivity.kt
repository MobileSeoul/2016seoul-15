package kr.edcan.buspolis

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_bus_fare_select.*
import kr.edcan.buspolis.databinding.ActivityBusFareSelectBinding
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor
import kr.edcan.buspolis.util.Utils as util

class BusFareSelectActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.blueBus -> selectedType = 0
            R.id.greenBus -> selectedType = 1
            R.id.yellowBus -> selectedType = 2
            R.id.payByCard -> paymentType = 0
            R.id.payByCash -> paymentType = 1
        }
        updateLayout()
    }

    /* selectedType 로 버스 종류 구분, 기본값 0
    * 0 간선
    * 1 지선
    * 2 노랭이버스
    * 3 광역버스
    * 광역버스는 종현이가 안한다고해서 gone처리시켜뒀음
    *
    * paymentType
    * 0 카드
    * 1 캐시
    * */
    var selectedType = 0
    var paymentType = 0
    var adultCount = 0
    var youthCount = 0
    var childCount = 0
    var mainColor = 0
    var countArr = arrayListOf(adultCount, youthCount, childCount)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityBusFareSelectBinding>(this, R.layout.activity_bus_fare_select)
        initLayout()
        updateLayout()
    }

    private fun initLayout() {
        icBack.setOnClickListener { finish() }
        blueBus.setOnClickListener(this)
        greenBus.setOnClickListener(this)
        yellowBus.setOnClickListener(this)
        payByCard.setOnClickListener(this)
        payByCash.setOnClickListener(this)
        adultFare.setOnClickListener { showPeopleDialog(0) }
        youthFare.setOnClickListener { showPeopleDialog(1) }
        childFare.setOnClickListener { showPeopleDialog(2) }
        calculate.setOnClickListener {
            startActivity<BusFareShowActivity>(
                    /* TODO Money Calculate */
                    "money" to kr.edcan.buspolis.util.Utils.busFare(paymentType, selectedType, adultCount, youthCount, childCount),
                    "busType" to selectedType,
                    "adultCount" to adultCount,
                    "youthCount" to youthCount,
                    "childCount" to childCount,
                    "paymentType" to paymentType
            )

        }
    }

    private fun showPeopleDialog(i: Int) {
        val choosing = arrayOf(adultFareCnt, youthFareCnt, childFareCnt)[i]
        MaterialDialog.Builder(this)
                .title(R.string.people_count)
                .items(arrayListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
                .itemsCallbackSingleChoice(countArr[i]) { dialog, itemView, which, text ->
                    choosing.text = which.toString()
                    when(i) {
                        0-> adultCount = which
                        1-> youthCount = which
                        2-> childCount = which
                    }
                    true
                }
                .show()
    }

    private fun updateLayout() {
        when (selectedType) {
            0 -> {
                blueBus.run {
                    setBackgroundResource(R.drawable.circle_white_full)
                    mainColor = ContextCompat.getColor(this@BusFareSelectActivity, R.color.busBlue)
                    textColor = mainColor
                }
                greenBus.run {
                    setBackgroundResource(R.drawable.circle_white)
                    textColor = Color.WHITE
                }
                yellowBus.run {
                    setBackgroundResource(R.drawable.circle_white)
                    textColor = Color.WHITE
                }
            }
            1 -> {
                blueBus.run {
                    setBackgroundResource(R.drawable.circle_white)
                    textColor = Color.WHITE
                }
                greenBus.run {
                    setBackgroundResource(R.drawable.circle_white_full)
                    mainColor = ContextCompat.getColor(this@BusFareSelectActivity, R.color.busGreen)
                    textColor = mainColor
                }
                yellowBus.run {
                    setBackgroundResource(R.drawable.circle_white)
                    textColor = Color.WHITE
                }
            }
            2 -> {
                blueBus.run {
                    setBackgroundResource(R.drawable.circle_white)
                    textColor = Color.WHITE
                }
                greenBus.run {
                    setBackgroundResource(R.drawable.circle_white)

                    textColor = Color.WHITE
                }
                yellowBus.run {
                    setBackgroundResource(R.drawable.circle_white_full)
                    mainColor = ContextCompat.getColor(this@BusFareSelectActivity, R.color.busYellow)
                    textColor = mainColor
                }
            }
        }
        when (paymentType) {
            0 -> {
                payByCard.setBackgroundResource(R.drawable.circle_white_full)
                payByCash.setBackgroundResource(R.drawable.circle_white)
                payByCard.textColor = mainColor
                payByCash.textColor = Color.WHITE
            }
            1 -> {
                payByCard.setBackgroundResource(R.drawable.circle_white)
                payByCash.setBackgroundResource(R.drawable.circle_white_full)
                payByCard.textColor = Color.WHITE
                payByCash.textColor = mainColor
            }
        }
        adultFareCnt.textColor = mainColor
        youthFareCnt.textColor = mainColor
        childFareCnt.textColor = mainColor
        busFareBackground.backgroundColor = ContextCompat.getColor(this, util.busFareColor(selectedType))
        bottomBarBackground.backgroundColor = ContextCompat.getColor(this, util.busFareColorDark(selectedType))
        calculate.setTextColorForceFully(ContextCompat.getColor(this, util.busFareColor(selectedType)))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.statusBarColor = ContextCompat.getColor(this, util.busFareColorDark(selectedType))
    }
}

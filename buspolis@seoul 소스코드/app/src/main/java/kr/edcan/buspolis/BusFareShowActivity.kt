package kr.edcan.buspolis

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_bus_fare_show.*
import kr.edcan.buspolis.util.Utils as utils
import org.jetbrains.anko.startActivity


class BusFareShowActivity : AppCompatActivity() {

    var moneyResult = 0
    var busType = 0
    var paymentType = 0
    var adultCount = 0
    var youthCount = 0
    var childCount = 0
    var mainColor = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_fare_show)
        setData()
        initLayout()
    }


    private fun setData() {
        val intent = intent
        moneyResult = intent.getIntExtra("money", 0)
        busType = intent.getIntExtra("busType", 0)

        adultCount = intent.getIntExtra("adultCount", 0)
        childCount = intent.getIntExtra("childCount", 0)
        youthCount = intent.getIntExtra("youthCount", 0)
        paymentType = intent.getIntExtra("paymentType", 0)
    }

    private fun initLayout() {
        var busStrArr = arrayOf(R.string.blue_bus_en, R.string.green_bus_en, R.string.yellow_bus_en)
        icBack.setOnClickListener { finish() }
        mainColor = utils.busFareColor(busType)
        busFareBackground.setBackgroundColor(ContextCompat.getColor(this, mainColor))
        speak.setTextColorForceFully(ContextCompat.getColor(this, mainColor))
        close.setOnClickListener { finish() }
        speak.setOnClickListener {
            startActivity<BusFareSpeakActivity>(
                    "busType" to busType,
                    "adultCount" to adultCount,
                    "youthCount" to youthCount,
                    "childCount" to childCount
                    )
        }
        money.text = moneyResult.toString()
        resultText.text = getString(busStrArr.get(busType)) + ", " + (if (adultCount != 0) adultCount.toString() + " " + getString(R.string.adult) else "") + " " + (if (youthCount != 0) youthCount.toString() + " " + getString(R.string.youth) else "") + " " + (if (childCount != 0) childCount.toString() + " " + getString(R.string.child) else "") + ", " + getString(R.string.pay_to) + " " + (getString(if (paymentType == 0) R.string.card else R.string.cash))
    }
}

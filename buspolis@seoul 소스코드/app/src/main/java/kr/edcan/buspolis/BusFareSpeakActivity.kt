package kr.edcan.buspolis

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bus_fare_speak.*
import kr.edcan.buspolis.util.KoreanRomanizer

class BusFareSpeakActivity : AppCompatActivity() {


    var busType = 0
    var adultCount = 0
    var youthCount = 0
    var childCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_fare_speak)
        val intent = intent
        busType = intent.getIntExtra("busType", 0)
        adultCount = intent.getIntExtra("adultCount", 0)
        childCount = intent.getIntExtra("childCount", 0)
        youthCount = intent.getIntExtra("youthCount", 0)

        helpKor.text = (if (adultCount != 0) "어른 " + adultCount + "명 " else "") +(if (youthCount != 0) "청소년 " + youthCount + "명 " else "") +(if (childCount != 0) "아이 " + childCount + "명 " else "") +"이요."
                helpRomaji.text = KoreanRomanizer.romanize(helpKor.text.toString())
        icBack.setOnClickListener { finish() }
        close.setOnClickListener { finish() }
    }
}

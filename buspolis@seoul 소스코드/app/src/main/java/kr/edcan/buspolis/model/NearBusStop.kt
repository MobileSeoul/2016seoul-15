package kr.edcan.buspolis.model

/**
 * Created by JunseokOh on 2016. 10. 27..
 */
class NearBusStop {
    constructor(name: MultiString, code: String, stationLeft: Int) {
        this.name = name
        this.code = code
        this.stationLeft = stationLeft
    }

    var name = MultiString()
    var code = ""
    var stationLeft = 0
    fun getStationString(): String {
        return stationLeft.toString() + " away"
    }
}

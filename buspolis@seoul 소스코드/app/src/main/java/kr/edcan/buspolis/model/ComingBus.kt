package kr.edcan.buspolis.model

/**
 * Created by LNTCS on 2016-10-31.
 */
class ComingBus{
    var id = 0
    var num = ""
    var type = 0
    var remainTime = ""
    var remainStat = ""

    constructor(id: Int, num: String, type: Int, remainTime: String, remainStat: String){
        this.id = id
        this.num = num
        this.type = type
        this.remainStat = remainStat
        this.remainTime = remainTime
    }
}
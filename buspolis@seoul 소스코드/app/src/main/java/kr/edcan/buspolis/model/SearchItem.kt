package kr.edcan.buspolis.model

/**
 * Created by LNTCS on 2016-10-23.
 */

class SearchItem {
    var type = listType.BUS
    var id = 0
    var keyword = ""
    var option = 0
    var num = ""

    constructor(keyword: String) {
        this.keyword = keyword
    }

    constructor(id: Int, keyword: String) {
        this.id = id
        this.keyword = keyword
    }

    constructor(id: Int, keyword: String, type: listType) {
        this.id = id
        this.keyword = keyword
        this.type = type
    }
    constructor(id: Int, keyword: String, type: listType, option: Int) {
        this.id = id
        this.keyword = keyword
        this.type = type
        this.option = option
    }
    constructor(id: Int, keyword: String, type: listType, option: String) {
        this.id = id
        this.keyword = keyword
        this.type = type
        this.num = option
    }

    enum class listType {
        BUS, BUSSTOP
    }
}

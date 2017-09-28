package kr.edcan.buspolis.model

/**
 * Created by Junseok on 2016-10-26.
 */

class ListContent(title: String, content: String, item: String) {
    var title = ""
    var content = ""
    var item = ""

    init {
        this.content = content
        this.title = title
        this.item = item
    }
    constructor(title: String): this(title,"","")
    constructor(title: String, content: String): this(title,content,"")
}

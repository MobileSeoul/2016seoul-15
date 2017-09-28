package kr.edcan.buspolis.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by LNTCS on 2016-10-28.
 */
open class RM_Bus : RealmObject() {
    
    @PrimaryKey
    open var id = 0
    open var num = ""
    open var type = 0
    open var start = ""
    open var end = ""
    open var first = ""
    open var last = ""
    open var peek = ""
    open var npeek = ""
    open var region = ""
    open var district = 0
}
package kr.edcan.buspolis.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by LNTCS on 2016-10-28.
 */
open class RM_Station : RealmObject() {

    @PrimaryKey
    open var id = 0
    open var name = ""
    open var name_en = ""
    open var name_cn = ""
    open var name_jp = ""
    open var num = ""
    open var x = 0.0
    open var y = 0.0
    open var district = ""
}
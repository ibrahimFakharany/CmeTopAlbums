package com.ifakharany.datasource.local.entity

import io.realm.kotlin.types.RealmObject

class AlbumEntity : RealmObject {
    var index: Int = 1
    var id: String = ""
    var albumUrl: String = ""
    var artistName: String = ""
    var albumName: String = ""
    var genres: String = ""
    var image: String = ""
    var releaseDate: String = ""
}

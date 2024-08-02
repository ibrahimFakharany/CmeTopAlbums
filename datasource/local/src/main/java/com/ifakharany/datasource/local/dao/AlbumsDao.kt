package com.ifakharany.datasource.local.dao

import com.ifakharany.datasource.local.entity.AlbumEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AlbumsDao @Inject constructor(private val realm: Realm) {
    private val className = AlbumEntity::class

    fun getAlbumsByPagingNotify(): Flow<ResultsChange<AlbumEntity>> {
        return try {
            realm.query(clazz = className).asFlow()
        } catch (ex: Exception) {
            throw ex
        }
    }

    fun getAlbumsByPaging(page: Int, limit: Int): RealmResults<AlbumEntity> {
        return try {
            val pageCalc = (page - 1) * limit
            realm.query(clazz = className, "index >= $pageCalc").limit(limit).find()
        } catch (ex: Exception) {
            throw ex
        }
    }

    fun getById(id: String): AlbumEntity? {
        try {

            val album: AlbumEntity? =
                realm.query(clazz = className, query = "id == $0", id).first().find()

            return album
        } catch (ex: Exception) {
            throw ex
        }
    }

    suspend fun insertAll(list: List<AlbumEntity>) {
        realm.write {
            list.forEach { data ->
                copyToRealm(data)
            }
        }
    }

    suspend fun deleteAll() {
        realm.write {
            val albums: RealmResults<AlbumEntity> = this.query(className).find()
            delete(albums)
        }
    }
}
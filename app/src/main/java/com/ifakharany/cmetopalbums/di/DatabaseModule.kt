package com.ifakharany.cmetopalbums.di

import com.ifakharany.datasource.local.entity.AlbumEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun providesRealmDatabase(): Realm {
        val realmConfiguration = RealmConfiguration.Builder(schema = setOf(AlbumEntity::class))
            .deleteRealmIfMigrationNeeded().build()

        return Realm.open(realmConfiguration)
    }
}
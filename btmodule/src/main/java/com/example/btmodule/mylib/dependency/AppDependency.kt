package com.example.btmodule.mylib.dependency

import android.app.Application
import com.example.btmodule.mylib.adapter.BtAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMyBt(app:Application): BtAdapter {
        return BtAdapter(app)
    }
}
package com.example.stockproject.di

import android.content.Context
import androidx.room.Room
import com.example.stockproject.data.AppDatabase
import com.example.stockproject.data.WishlistDao
import com.example.stockproject.data.WishlistStockDao
import com.example.stockproject.data.repoImpl.WishlistRepositoryImpl
import com.example.stockproject.domain.WishlistRepository
import com.example.stockproject.data.apiCall.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Provides
    fun provideBaseUrl(): String = "https://www.alphavantage.co/"

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideStockApi(retrofit: Retrofit): RetrofitService =
        retrofit.create(RetrofitService::class.java)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "stock_project_db"
        ).build()

    @Provides
    fun provideWishlistDao(db: AppDatabase): WishlistDao = db.wishlistDao()

    @Provides
    fun provideWishlistStockDao(db: AppDatabase): WishlistStockDao = db.wishlistStockDao()

    @Provides
    @Singleton
    fun provideWishlistRepository(
        wishlistDao: WishlistDao,
        wishlistStockDao: WishlistStockDao
    ): WishlistRepository = WishlistRepositoryImpl(wishlistDao, wishlistStockDao)

    @Provides
    @Singleton
    fun provideRepoImpl(api: RetrofitService): com.example.stockproject.domain.RepoInterface =
        com.example.stockproject.data.repoImpl.RepoImpl(api)

}
package me.rerere.tension.provider

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object DataProviderManager {
    fun getSubscriptionDataSource(): Flow<DataProvider> {
        return flow {

        }
    }
}
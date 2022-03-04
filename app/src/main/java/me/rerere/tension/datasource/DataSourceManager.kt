package me.rerere.tension.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object DataSourceManager {
    fun getSubscriptionDataSource(): Flow<IDataSource> {
        return flow {

        }
    }
}
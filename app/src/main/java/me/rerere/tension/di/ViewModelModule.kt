package me.rerere.tension.di

import me.rerere.tension.ui.screen.index.IndexViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        IndexViewModel()
    }
}
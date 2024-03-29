package com.example.kotlinarhitecturecomponents.mvvm

interface IView{
    fun showLoading()
    fun hideLoading()
    fun loadError(e: Throwable)
    fun loadError(msg: String)
}
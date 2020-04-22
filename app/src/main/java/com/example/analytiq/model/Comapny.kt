package com.example.analytiq.model

class Comapny(
    symbol: String,
    open: String,
    high: String,
    low: String,
    price: String,
    volume: String,
    latestTradingDay: String,
    previousClose: String,
    change: String,
    changePercent: String
) {

    var mSymbol = symbol
    var mOpen = open
    var mHigh = high
    var mLow = low
    var mPrice = price
    var mVolume = volume
    var mLatestTradingDay = latestTradingDay
    var mPreviousClose = previousClose
    var mChange = change
    var mChangePercent = changePercent


    private fun getSymbol(): String {
        return mSymbol
    }

    private fun getHigh(): String {
        return mHigh
    }

    private fun getLow(): String {
        return mLow
    }

    private fun getOpen(): String {
        return mOpen
    }

    private fun getPrice(): String {
        return mPrice
    }

    private fun getVolume(): String {
        return mVolume
    }

    private fun getLatestTradingDay(): String {
        return mLatestTradingDay
    }

    private fun getPreviousClose(): String {
        return mPreviousClose
    }

    private fun getChange(): String {
        return mChange
    }

    private fun getChangePercent(): String {
        return mChangePercent
    }

}
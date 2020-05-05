package com.example.analytiq.model

import java.io.Serializable

class Company : Serializable {

    lateinit var mSymbol: String
    lateinit var mName: String
    lateinit var mLow: String
    lateinit var mHigh: String
    lateinit var mLatestTradingDay: String
    lateinit var mChangePercent: String
    lateinit var mTicker: String
    lateinit var mVolume: String
    lateinit var mOpen: String
    lateinit var mPreviousClose: String

    lateinit var mExchange: String
    lateinit var mIndustry: String
    lateinit var mWebsite: String
    lateinit var mDescription: String
    lateinit var mCeo: String
    lateinit var mSector: String
    var mPrice: Double = 0.0
    var mChangesPercentage: Double = 0.0
    var mChange: Double = 0.0
    var mDayLow: Double = 0.0
    var mDayHigh: Double = 0.0
    var mYearHigh: Double = 0.0
    var mYearLow: Double = 0.0
    var dVolume: Double = 0.0
    var mAvgVolume: Double = 0.0
    var dOpen: Double = 0.0
    var dPreviousClose: Double = 0.0

    constructor(
        ticker: String,
        name: String,
        price: Double,
        changePercent: String,
        change: Double
    ) {

        this.mTicker = ticker
        mName = name
        this.mPrice = price
        this.mChange = change
        this.mChangePercent = changePercent
    }

    constructor(
        ticker: String,
        name: String,
        price: Double,
        exchange: String
    ) {

        this.mTicker = ticker
        mName = name
        this.mPrice = price
        this.mExchange = exchange
    }

    constructor(
        exchange: String,
        industry: String,
        website: String,
        description: String,
        ceo: String,
        sector: String,
        price: Double,
        changesPercentage: Double,
        change: Double,
        dayLow: Double,
        dayHigh: Double,
        yearHigh: Double,
        yearLow: Double,
        volume: Double,
        avgVolume: Double,
        open: Double,
        previousClose: Double
    ) {
        this.mExchange = exchange
        this.mIndustry = industry
        this.mWebsite = website
        this.mDescription = description
        this.mCeo = ceo
        this.mSector = sector
        this.mPrice = price
        this.mChangesPercentage = changesPercentage
        this.mChange = change
        this.mDayLow = dayLow
        this.mDayHigh = dayHigh
        this.mYearHigh = yearHigh
        this.mYearLow = yearLow
        dVolume = volume
        this.mAvgVolume = avgVolume
        dOpen = open
        dPreviousClose = previousClose
    }

    fun getTicker(): String {
        return mTicker
    }

    fun getHigh(): String {
        return mHigh
    }

    fun getLow(): String {
        return mLow
    }

    fun getOpen(): String {
        return mOpen
    }

    fun getVolume(): String {
        return mVolume
    }

    fun getLatestTradingDay(): String {
        return mLatestTradingDay
    }

    fun getPreviousClose(): String {
        return mPreviousClose
    }

    fun getChange(): Double {
        return mChange
    }

    fun getChangePercent(): String {
        return mChangePercent
    }

    fun getCompanyName(): String {
        return mName
    }


    fun getExchange(): String {
        return mExchange
    }

    fun getIndustry(): String {
        return mIndustry
    }

    fun getWebsite(): String {
        return mWebsite
    }

    fun getDescription(): String {
        return mDescription
    }

    fun getCeo(): String {
        return mCeo
    }

    fun getSector(): String {
        return mSector
    }

    fun getPrice(): Double {
        return mPrice
    }

    fun getChangesPercentage(): Double {
        return mChangesPercentage
    }

    fun getdChange(): Double {
        return mChange
    }

    fun getDayLow(): Double {
        return mDayLow
    }

    fun getDayHigh(): Double {
        return mDayHigh
    }

    fun getYearHigh(): Double {
        return mYearHigh
    }

    fun getYearLow(): Double {
        return mYearLow
    }

    fun getdVolume(): Double {
        return dVolume
    }

    fun getAvgVolume(): Double {
        return mAvgVolume
    }

    fun getdOpen(): Double {
        return dOpen
    }

    fun getdPreviousClose(): Double {
        return dPreviousClose
    }
}

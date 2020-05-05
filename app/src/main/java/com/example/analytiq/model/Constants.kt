package com.example.analytiq.model

class Constants {

    internal object Currency {

        val API_KEY = "7664c7d82dmsh4b3b729631c9965p10e9dajsn35ce3563b21a"
        val API_BASE_URL = "https://currency-converter5.p.rapidapi.com/currency/"
        val API_CONVERT = "convert?format=json"
        val API_LIST = "list?format=json"
        val API_HISTORY_INITIAL = "historical/"
        val API_HISTORY_END = "?format=json"

    }

    internal object ShareMarket {
        val API_BASE_URL_STOCK = "https://financialmodelingprep.com/api/v3/stock/"
        val API_MOST_ACTIVE = "actives"
        val API_MOST_GAINER = "gainers"
        val API_MOST_LOSER = "losers"

        val API_BASE_URL_COMPANY_PROFILE =
            "https://financialmodelingprep.com/api/v3/company/profile/"
        val API_BASE_URL_QUOTE = "https://financialmodelingprep.com/api/v3/quote/"

        val API_STOCK_LIST = "https://financialmodelingprep.com/api/v3/company/stock/list"
    }

}
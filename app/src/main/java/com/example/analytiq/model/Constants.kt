package com.example.analytiq.model

class Constants {

    internal object Currency {

        val API_KEY = "7664c7d82dmsh4b3b729631c9965p10e9dajsn35ce3563b21a"
        val API_BASE_URL = "https://currency-converter5.p.rapidapi.com/currency/"
        val API_CONVERT = "convert?format=json"
        val API_LIST = "list?format=json"
        val API_HISTORY_INITIAL = "historical/"
        val API_HISTORY_END = "?format=json"

        //    public static final String API_URL = API_BASE_URL + API_KEY;
        //    public static final String API_KEY = "a1218ef2ab484b7597b786caec010502";
        //    public static final String API_BASE_URL = "https://openexchangerates.org/api/latest.json?app_id=";

    }

    internal object ShareMarket {
        val API_BASE_URL = "https://www.alphavantage.co/query?"

        val API_FUNCTION_TIME_SERIES_INTRADAY = "function=TIME_SERIES_INTRADAY"
        val API_FUNCTION_TIME_SERIES_DAILY = "function=TIME_SERIES_DAILY"
        val API_FUNCTION_TIME_SERIES_DAILY_ADJUSTED = "function=TIME_SERIES_DAILY_ADJUSTED"
        val API_FUNCTION_TIME_SERIES_WEEKLY = "function=TIME_SERIES_WEEKLY"
        val API_FUNCTION_TIME_SERIES_WEEKLY_ADJUSTED = "function=TIME_SERIES_WEEKLY_ADJUSTED"
        val API_FUNCTION_TIME_SERIES_MONTHLY = "function=TIME_SERIES_MONTHLY"
        val API_FUNCTION_TIME_SERIES_MONTHLY_ADJUSTED = "function=TIME_SERIES_MONTHLY_ADJUSTED"


        val API_KEY = "apikey=31D9ROE7OO4XDML1"

    }

}
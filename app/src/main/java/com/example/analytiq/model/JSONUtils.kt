package com.example.analytiq.model

//import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset


class JSONUtils {
    val LOG_TAG = JSONUtils::class.java.simpleName


    fun createURL(stringURL: String): URL {

        var url: URL

        try {
            url = URL(stringURL)
        } catch (e: MalformedURLException) {
            Log.e(LOG_TAG, "Error with creating URL ", e)

        }
        return URL("")
    }

    @Throws(IOException::class)
    fun getJsonString(url: URL?): String {
        var jsonResponse = ""

        if (url == null) {
            return jsonResponse
        }

        var httpURLConnection: HttpURLConnection? = null
        var inputStream: InputStream? = null

        try {
            httpURLConnection = url!!.openConnection() as HttpURLConnection
            httpURLConnection!!.setReadTimeout(10000)
            httpURLConnection!!.setConnectTimeout(15000)
            httpURLConnection!!.setRequestMethod("GET")
            httpURLConnection!!.connect()

            if (httpURLConnection!!.getResponseCode() === 200) {
                inputStream = httpURLConnection!!.getInputStream()
                jsonResponse = readFromStream(inputStream)
            } else {
                Log.e(LOG_TAG, "Error Resonse Code:" + httpURLConnection!!.getResponseCode())

            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, "Problem retriving JSON", e)
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection!!.disconnect()
            }
            if (inputStream != null) {
                inputStream!!.close()
            }
        }
        return jsonResponse
    }

    @Throws(IOException::class)
    fun readFromStream(inputStream: InputStream?): String {
        val builder = StringBuilder()
        if (inputStream != null) {
            val inutStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val reader = BufferedReader(inutStreamReader)

            var line = reader.readLine()

            while (line != null) {
                builder.append(line)
                line = reader.readLine()
            }
        }
        return builder.toString()
    }
}
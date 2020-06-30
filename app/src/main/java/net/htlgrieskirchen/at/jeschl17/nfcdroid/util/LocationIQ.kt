package net.htlgrieskirchen.at.jeschl17.nfcdroid.util

import android.os.AsyncTask
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.net.ssl.HttpsURLConnection


class DetermineLocationTask: AsyncTask<String, Void, DetermineLocationTaskOut?>() {
    override fun doInBackground(vararg params: String?): DetermineLocationTaskOut? {
        return try {
            GsonBuilder().create().fromJson<List<DetermineLocationTaskOut>>(
                get("$API_BASE?" +
                        "key=$API_KEY&" +
                        "q=${URLEncoder.encode(params[0], StandardCharsets.UTF_8.toString())}&" +
                        "format=json"
                ),
                object: TypeToken<ArrayList<DetermineLocationTaskOut>>() {}.type
            )[0]
        } catch (e: Exception) {
            null
        }
    }
}

data class DetermineLocationTaskOut(
    val lat: Double,
    val lon: Double
)

fun get(url: String): String {
    val connection: HttpsURLConnection = URL(url).openConnection() as HttpsURLConnection
    connection.requestMethod = "GET"
    connection.setRequestProperty("Content-Type", "application/json")
    return connection.inputStream.readString()
}

fun InputStream.readString(): String {
    return this.bufferedReader()
        .readLines()
        .reduce { s1, s2 -> s1 + s2}
}

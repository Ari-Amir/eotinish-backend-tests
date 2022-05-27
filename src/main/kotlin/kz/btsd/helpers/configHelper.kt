package kz.btsd.helpers

import Environment
import org.json.JSONObject
import java.io.File

class ConfigParser(json: String) : JSONObject(json) {
    val data = this.optJSONArray("data")?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
}

object ConfigHelper {
    val path = "${Environment.currentDir}/src/main/resources/configurations.conf"
    val config = File(path).bufferedReader().use { it.readText() }

    val BACKEND_HOST = (ConfigParser(config)["backend"] as JSONObject).get("host").toString()
    val BACKEND_PORT = (ConfigParser(config)["backend"] as JSONObject).get("port").toString().toInt()

    val DB_HOST = (ConfigParser(config)["db"] as JSONObject).get("host").toString()
    val DB_NAME = (ConfigParser(config)["db"] as JSONObject).get("name").toString()
    val DB_PORT = (ConfigParser(config)["db"] as JSONObject).get("port").toString()
    val DB_USER = (ConfigParser(config)["db"] as JSONObject).get("user").toString()
    val DB_PASS = (ConfigParser(config)["db"] as JSONObject).get("pass").toString()

    val IMAGES_BACKEND = (ConfigParser(config)["images"] as JSONObject).get("backend").toString()
}
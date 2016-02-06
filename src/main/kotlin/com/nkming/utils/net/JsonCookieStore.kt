package com.nkming.utils.net

import android.util.Base64
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.io.*
import java.lang.reflect.Type
import java.net.CookieStore
import java.net.HttpCookie
import java.net.URI

class JsonCookieStore(dir: String) : CookieStore
{
	override fun remove(uri: URI?, cookie: HttpCookie): Boolean
	{
		val map = getMap(uri)
		map.remove(cookie.name) ?: return false
		commit(uri, map)
		return true
	}

	override fun removeAll(): Boolean
	{
		if (_dir.listFiles().isEmpty())
		{
			return false
		}
		else
		{
			_dir.deleteRecursively()
			_dir.mkdir()
			return true
		}
	}

	override fun add(uri: URI?, cookie: HttpCookie)
	{
		val map = getMap(uri)
		map[cookie.name] = JsonCookie(cookie)
		commit(uri, map)
	}

	override fun getURIs(): List<URI?>
	{
		val product = arrayListOf<URI?>()
		for (f in listFiles())
		{
			product += getUri(f.name)
		}
		return product
	}

	override fun get(uri: URI?): List<HttpCookie>
	{
		return getMutable(uri)
	}

	override fun getCookies(): List<HttpCookie>
	{
		val product = arrayListOf<HttpCookie>()
		for (f in listFiles())
		{
			product += getMutable(f)
		}
		return product
	}

	// TODO HttpOnly is missing from HttpCookie's interface and thus not
	// persisted. Should we reflect it?
	private class JsonCookie(cookie: HttpCookie,
			bornAt: Long = System.currentTimeMillis() / 1000)
	{
		constructor(name: String, value: String)
				: this(HttpCookie(name, value))

		constructor(name: String, value: String, bornAt: Long)
				: this(HttpCookie(name, value), bornAt = bornAt)

		@Transient
		var c = cookie
			private set

		var comment: String?
			get() = c.comment
			set(value)
			{
				c.comment = value
			}

		var commentURL: String?
			get() = c.commentURL
			set(value)
			{
				c.commentURL = value
			}

		var discard: Boolean
			get() = c.discard
			set(value)
			{
				c.discard = value
			}

		var domain: String?
			get() = c.domain
			set(value)
			{
				c.domain = value
			}

		var maxAge: Long
			get() = c.maxAge
			set(value)
			{
				c.maxAge = value
			}

		val bornAt: Long = bornAt
		val dieAt: Long
			get()
			{
				return bornAt + maxAge
			}

		val hasExpired: Boolean
			get() = c.hasExpired()

		val name: String
			get() = c.name

		var path: String?
			get() = c.path
			set(value)
			{
				c.path = value
			}

		var portlist: String?
			get() = c.portlist
			set(value)
			{
				c.portlist = value
			}

		var secure: Boolean
			get() = c.secure
			set(value)
			{
				c.secure = value
			}

		var value: String
			get() = c.value
			set(value)
			{
				c.value = value
			}

		var version: Int
			get() = c.version
			set(value)
			{
				c.version = value
			}
	}

	private class JsonCookieSerializer : JsonSerializer<JsonCookie>
	{
		override fun serialize(src: JsonCookie, typeOfSrc: Type,
				context: JsonSerializationContext): JsonElement
		{
			val obj = JsonObject()
			obj.addProperty("comment", src.comment)
			obj.addProperty("commentURL", src.commentURL)
			obj.addProperty("dieAt", src.dieAt)
			obj.addProperty("discard", src.discard)
			obj.addProperty("domain", src.domain)
			obj.addProperty("name", src.name)
			obj.addProperty("path", src.path)
			obj.addProperty("portlist", src.portlist)
			obj.addProperty("secure", src.secure)
			obj.addProperty("value", src.value)
			obj.addProperty("version", src.version)
			return obj
		}
	}

	private class JsonCookieDeserializer : JsonDeserializer<JsonCookie>
	{
		override fun deserialize(json: JsonElement, typeOfT: Type,
				context: JsonDeserializationContext): JsonCookie
		{
			val obj = json.asJsonObject
			val name = obj["name"].asString
			val value = obj["value"].asString
			val now = System.currentTimeMillis() / 1000

			val product = JsonCookie(name, value, now)
			product.comment = obj["comment"]?.asString
			product.commentURL = obj["commentURL"]?.asString
			product.discard = obj["discard"].asBoolean
			product.domain = obj["domain"]?.asString
			product.maxAge = obj["dieAt"].asLong - now
			product.path = obj["path"]?.asString
			product.portlist = obj["portlist"]?.asString
			product.secure = obj["secure"].asBoolean
			product.version = obj["version"].asInt
			return product
		}
	}

	private fun commit(uri: URI?, map: MutableMap<String, JsonCookie>)
	{
		val f = File(_dir, getFilename(uri))
		f.writer().buffered().use(
		{
			it.write(getJson(map))
		})
	}

	private fun getMutable(uri: URI?): MutableList<HttpCookie>
	{
		val f = File(_dir, getFilename(uri))
		if (!f.exists())
		{
			return arrayListOf()
		}
		else
		{
			return getMutable(f)
		}
	}

	private fun getMutable(f: File): MutableList<HttpCookie>
	{
		val map = getMap(f)
		val product = arrayListOf<HttpCookie>()
		for (entry in map)
		{
			if (!entry.value.hasExpired)
			{
				product += entry.value.c
			}
		}
		return product
	}

	private fun getMap(uri: URI?): MutableMap<String, JsonCookie>
	{
		val f = File(_dir, getFilename(uri))
		if (!f.exists())
		{
			return hashMapOf()
		}
		else
		{
			return getMap(f)
		}
	}

	private fun getMap(f: File): MutableMap<String, JsonCookie>
	{
		f.reader().buffered().use(
		{
			val gson = newGson()
			val mapType = object: TypeToken<MutableMap<String, JsonCookie>>(){}
					.type
			return gson.fromJson<MutableMap<String, JsonCookie>>(it, mapType)
		})
	}

	private fun getJson(map: MutableMap<String, JsonCookie>): String
	{
		val gson = newGson()
		val mapType = object: TypeToken<MutableMap<String, JsonCookie>>(){}.type
		return gson.toJson(map, mapType)
	}

	private fun getFilename(uri: URI?): String
	{
		if (uri == null)
		{
			return "null"
		}
		else
		{
			return Base64.encodeToString(uri.toString().toByteArray(),
					Base64.NO_WRAP or Base64.URL_SAFE)
		}
	}

	private fun getUri(filename: String): URI?
	{
		if (filename == "null")
		{
			return null
		}
		else
		{
			val uriStr = Base64.decode(filename,
					Base64.NO_WRAP or Base64.URL_SAFE).toString(Charsets.UTF_8)
			return URI.create(uriStr)
		}
	}

	private fun listFiles(): Array<File>
	{
		return _dir.listFiles() ?: arrayOf()
	}

	private fun newGson(): Gson
	{
		val inst = GsonBuilder()
		inst.registerTypeAdapter(JsonCookie::class.java, JsonCookieSerializer())
		inst.registerTypeAdapter(JsonCookie::class.java, JsonCookieDeserializer())
		return inst.create()
	}

	private val _dir = File(dir)

	init
	{
		if (!_dir.exists())
		{
			_dir.mkdirs()
		}
		else
		{
			_dir.isDirectory || throw IllegalArgumentException("Require dir")
		}
	}
}

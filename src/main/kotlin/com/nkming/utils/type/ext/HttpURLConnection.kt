package com.nkming.utils.type.ext

import java.net.HttpURLConnection

inline fun <T> HttpURLConnection.use(block: (HttpURLConnection) -> T): T
{
	try
	{
		this.connect()
		return block(this)
	}
	finally
	{
		this.disconnect()
	}
}

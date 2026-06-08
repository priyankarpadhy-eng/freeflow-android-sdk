package com.freeflow.sdk

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class FreeFlow(
    private val projectId: String,
    private val apiKey: String,
    private val host: String = "https://freeflow-gateway.vercel.app",
    private val packageName: String? = null
) {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val JSON = "application/json; charset=utf-8".toMediaType()

    init {
        require(projectId.isNotBlank()) { "FreeFlow SDK: projectId is required" }
        require(apiKey.isNotBlank()) { "FreeFlow SDK: apiKey is required" }
    }

    private fun buildUrl(endpoint: String): String {
        val cleanEndpoint = endpoint.replace(Regex("/{2,}"), "/")
        return "$host/v1/$projectId$cleanEndpoint"
    }

    /**
     * Sends a WhatsApp OTP to the specified phone number.
     * Ensure you append the country code (e.g., "+919876543210").
     */
    suspend fun sendOTP(phone: String): OTPResponse = withContext(Dispatchers.IO) {
        val url = buildUrl("/api/v1/otp/send")
        val jsonBody = gson.toJson(mapOf("phone" to phone))
        val body = jsonBody.toRequestBody(JSON)

        val requestBuilder = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")

        if (packageName != null) {
            requestBuilder.addHeader("X-Android-Package", packageName)
        }

        val request = requestBuilder.build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: "{}"
            if (!response.isSuccessful) {
                throw Exception("FreeFlow API Error: ${response.code} - $responseBody")
            }
            gson.fromJson(responseBody, OTPResponse::class.java)
        }
    }

    /**
     * Verifies the 6-digit OTP code sent to the phone number.
     */
    suspend fun verifyOTP(phone: String, otp: String): OTPResponse = withContext(Dispatchers.IO) {
        val url = buildUrl("/api/v1/otp/verify")
        val jsonBody = gson.toJson(mapOf("phone" to phone, "otp" to otp))
        val body = jsonBody.toRequestBody(JSON)

        val requestBuilder = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", "Bearer $apiKey")

        if (packageName != null) {
            requestBuilder.addHeader("X-Android-Package", packageName)
        }

        val request = requestBuilder.build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string() ?: "{}"
            if (!response.isSuccessful) {
                throw Exception("FreeFlow API Error: ${response.code} - $responseBody")
            }
            gson.fromJson(responseBody, OTPResponse::class.java)
        }
    }
}

data class OTPResponse(
    val success: Boolean,
    val message: String? = null,
    val error: String? = null
)

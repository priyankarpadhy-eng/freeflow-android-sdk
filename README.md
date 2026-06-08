# FreeFlow Android SDK

The official native Android SDK for **FreeFlow** — the decentralized, zero-cost WhatsApp OTP and authentication system.

## 🚀 Installation

Add the JitPack repository to your build file.

### Settings.gradle
```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### App build.gradle
```gradle
dependencies {
    implementation 'com.github.priyankarpadhy-eng:freeflow-android-sdk:v1.0.0'
}
```

## 🛠️ Usage

This SDK uses Kotlin Coroutines for fast, non-blocking network requests.

### 1. Initialize the SDK
Initialize FreeFlow with your Project ID and API Key from the FreeFlow Desktop App.

```kotlin
import com.freeflow.sdk.FreeFlow

val auth = FreeFlow(
    projectId = "YOUR_PROJECT_ID",
    apiKey = "YOUR_API_KEY"
)
```

### 2. Send an OTP
```kotlin
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// Inside your CoroutineScope (e.g., viewModelScope)
GlobalScope.launch {
    try {
        val response = auth.sendOTP("+919876543210")
        if (response.success) {
            println("OTP sent to WhatsApp!")
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}
```

### 3. Verify the OTP
```kotlin
GlobalScope.launch {
    try {
        val response = auth.verifyOTP("+919876543210", "123456")
        if (response.success) {
            println("Successfully verified! User is logged in.")
        } else {
            println("Invalid OTP.")
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}
```

## 🔒 Security Note
FreeFlow API keys are currently backend keys. If you use them directly in an Android app, they can be extracted by malicious actors. Ensure you use the **App Package Restriction** feature in the FreeFlow Gateway to secure frontend usage.

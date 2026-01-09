# AI Padala - Android

A modern Android application that helps Overseas Filipino Workers (OFWs) find and compare the best remittance providers for sending money to the Philippines.

## Features

- **Rate Comparison**: Compare exchange rates and fees from 10+ international remittance providers
- **Price Alerts**: Set notifications for when exchange rates reach your target
- **Favorite Corridors**: Save your preferred currency pairs for quick access
- **Rate History**: View 30-day historical rate charts
- **Offline Support**: Cached rates available for 24 hours offline
- **Background Sync**: Automatic hourly rate updates via WorkManager
- **Deep Linking**: Open specific screens via URLs
- **Multi-language**: English and Filipino (Tagalog) support

## Supported Remittance Providers

| Provider | Payout Methods |
|----------|----------------|
| Wise | Bank, GCash, Maya |
| Remitly | Bank, GCash, Maya, Cash Pickup |
| Western Union | Bank, GCash, Maya, Cash Pickup, Debit Card |
| WorldRemit | Bank, GCash, Maya, Cash Pickup, Mobile Load |
| MoneyGram | Bank, Cash Pickup, Mobile Wallet |
| Xoom | Bank, GCash, Maya, Cash Pickup |
| Instarem | Bank, Cash Pickup |
| OFX | Bank |
| Pangea | Bank, Cash Pickup |
| Taptap Send | Bank, Mobile Wallet |

## Tech Stack

- **Language**: Kotlin 2.0
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: Clean Architecture with MVVM
- **Dependency Injection**: Hilt
- **Database**: Room with TypeConverters
- **Networking**: Retrofit + OkHttp + Moshi
- **Async**: Kotlin Coroutines + Flow
- **Background Work**: WorkManager
- **Image Loading**: Coil (with SVG support)
- **Charts**: Vico
- **Navigation**: Jetpack Navigation Compose

## Project Structure

```
app/src/main/java/com/aipadala/android/
├── core/                    # Core infrastructure
│   ├── database/           # Room database, DAOs, entities
│   ├── di/                 # Hilt dependency injection modules
│   ├── network/            # Network monitoring
│   └── util/               # Utilities, constants, extensions
├── data/                    # Data layer
│   ├── model/              # Domain models
│   ├── remote/             # API interfaces and DTOs
│   └── repository/         # Repository implementations
├── domain/                  # Domain layer
│   ├── repository/         # Repository interfaces
│   └── usecase/            # Business logic use cases
├── presentation/            # Presentation layer
│   ├── components/         # Reusable Compose components
│   ├── navigation/         # Navigation graph and routes
│   ├── screens/            # Screen composables and ViewModels
│   └── theme/              # Material theme configuration
└── service/                 # Background services
    ├── FCMService.kt       # Firebase Cloud Messaging
    ├── RateSyncWorker.kt   # Background rate sync
    └── AIPadalaNotificationManager.kt
```

## Requirements

- Android Studio Ladybug (2024.2.1) or later
- JDK 17
- Android SDK 34
- Minimum SDK: 24 (Android 7.0)

## Setup

### 1. Clone the Repository

```bash
git clone https://github.com/your-org/aipadala-android.git
cd aipadala-android
```

### 2. Configure API Endpoints

Update `app/build.gradle.kts` with your API URLs:

```kotlin
buildConfigField("String", "BASE_URL", "\"https://api.aipadala.com/\"")
```

### 3. Certificate Pinning (Production)

Before releasing to production, update the certificate pins in `NetworkModule.kt`:

```kotlin
val certificatePinner = CertificatePinner.Builder()
    .add("api.aipadala.com", "sha256/YOUR_CERTIFICATE_HASH_HERE")
    .build()
```

To obtain your certificate hash:
```bash
openssl s_client -servername api.aipadala.com -connect api.aipadala.com:443 | \
  openssl x509 -pubkey -noout | \
  openssl pkey -pubin -outform der | \
  openssl dgst -sha256 -binary | openssl enc -base64
```

### 4. Firebase Configuration (Optional)

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com)
2. Add an Android app with package name `com.aipadala.android`
3. Download `google-services.json` and place it in `app/`
4. Uncomment Firebase plugins in `build.gradle.kts`:
   ```kotlin
   alias(libs.plugins.google.services)
   alias(libs.plugins.firebase.crashlytics)
   ```
5. Uncomment Firebase dependencies

### 5. Build the Project

```bash
./gradlew assembleDebug
```

## Build Variants

| Variant | API URL | Features |
|---------|---------|----------|
| Debug | api-staging.aipadala.com | HTTP logging, no cert pinning |
| Release | api.aipadala.com | Minified, cert pinning enabled |

## Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# All tests with coverage
./gradlew jacocoTestReport
```

## Deep Links

The app supports the following deep links:

| URL | Description |
|-----|-------------|
| `https://aipadala.com/compare?from=USD&amount=500` | Open comparison with parameters |
| `https://aipadala.com/alerts` | Open rate alerts screen |
| `https://aipadala.com/provider/wise` | Open provider details |

## Architecture

### Clean Architecture Layers

1. **Presentation Layer**: Compose UI, ViewModels, Navigation
2. **Domain Layer**: Use cases, repository interfaces
3. **Data Layer**: Repository implementations, API, database

### Data Flow

```
UI (Composable) → ViewModel → UseCase → Repository → API/Database
         ↑                                    ↓
         └────────── StateFlow ←──────────────┘
```

### Caching Strategy

1. Emit cached data immediately for instant UI
2. Fetch fresh data from API in background
3. Update cache and emit fresh data
4. Cache expires after 24 hours

## Security Features

- **Certificate Pinning**: Prevents MITM attacks (release builds)
- **URL Validation**: Whitelist-based affiliate URL validation
- **No Sensitive Data Logging**: Logging disabled in release builds
- **ProGuard**: Code obfuscation and shrinking enabled

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit changes: `git commit -am 'Add my feature'`
4. Push to branch: `git push origin feature/my-feature`
5. Submit a pull request

### Code Style

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Write unit tests for new features
- Document public APIs with KDoc

## License

Copyright 2024 AI Padala. All rights reserved.

## Support

- Issues: [GitHub Issues](https://github.com/your-org/aipadala-android/issues)
- Email: support@aipadala.com

---

## Post-Deployment Checklist

After deploying or committing to the GitHub repository, complete the following:

### Immediate Actions

- [ ] Verify CI/CD pipeline runs successfully
- [ ] Check that all tests pass
- [ ] Review build artifacts are generated correctly

### Before Production Release

- [ ] Replace placeholder certificate pins with actual hashes
- [ ] Configure Firebase for production environment
- [ ] Enable Firebase Crashlytics for crash reporting
- [ ] Set up Firebase Analytics events
- [ ] Configure App Signing in Google Play Console
- [ ] Prepare store listing (screenshots, descriptions)
- [ ] Complete privacy policy and terms of service URLs

### API Configuration

- [ ] Verify production API endpoint is correct
- [ ] Ensure API rate limiting is configured
- [ ] Test all API endpoints with production credentials

### Monitoring Setup

- [ ] Set up Crashlytics alerts
- [ ] Configure ANR monitoring
- [ ] Set up performance monitoring baselines
- [ ] Create dashboard for key metrics

### Security Review

- [ ] Run static analysis (detekt, lint)
- [ ] Verify ProGuard rules don't break functionality
- [ ] Test certificate pinning in release build
- [ ] Verify no sensitive data in logs

### Testing

- [ ] Run full regression test suite
- [ ] Test on multiple device configurations
- [ ] Test offline functionality
- [ ] Test notification permissions flow
- [ ] Verify deep links work correctly

### Documentation

- [ ] Update API documentation if changed
- [ ] Document any new environment variables
- [ ] Update changelog with version notes

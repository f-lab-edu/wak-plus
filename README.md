<h1 align="center">Wak+</h1>

<p align="center">
  <a href="https://kotlinlang.org"><img alt="Kotlin Version" src="https://img.shields.io/badge/Kotlin-1.6.21-blueviolet.svg?style=flat"/></a>
  <a href="https://android-arsenal.com/api?level=23"><img alt="API" src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://developer.android.com/studio/releases/gradle-plugin"><img alt="AGP" src="https://img.shields.io/badge/AGP-7.2.1-blue?style=flat"/></a>
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat"/></a>
</p>

Wak+ is an application that helps you to conveniently access to [WAKTAVERSE](https://www.youtube.com/c/welshcorgimessi) member's SNS content such as YouTube, Twitch, Instagram, and Twitter in one app.

## Tech stack & Open-source libraries

- Minimum SDK level 23
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- JetPack
    - Lifecycle - Create a UI that automatically responds to lifecycle events.
    - LiveData - Build data objects that notify views when the underlying database changes.
    - ViewModel - Store UI related data that isn't destroyed on app rotations.
    - Room - Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
    - DataBinding - Useful to bind data directly through layouts xml file, so no `findViewById()` anymore.
    - [Navigation](https://developer.android.com/guide/navigation) - Handles navigating between your app's destinations.
    - [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Data storage solution that uses Kotlin coroutines and Flow to store data asynchronously, consistently, and transactionally.
- [Hilt](https://dagger.dev/hilt/) - Dependency injection.
- [Coil](https://coil-kt.github.io/coil/) - An image loading library for Android backed by Kotlin Coroutines.
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit) - Construct the REST APIs.

## MAD Score

![MAD Score](https://user-images.githubusercontent.com/39554623/181916429-6c50c917-79de-4557-999a-daa48016b378.png)
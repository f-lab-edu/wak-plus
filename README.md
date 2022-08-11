<h1 align="center">Wak+</h1>

<p align="center">
  <a href="https://kotlinlang.org"><img alt="Kotlin Version" src="https://img.shields.io/badge/Kotlin-1.6.21-blueviolet.svg?style=flat"/></a>
  <a href="https://android-arsenal.com/api?level=23"><img alt="API" src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://developer.android.com/studio/releases/gradle-plugin"><img alt="AGP" src="https://img.shields.io/badge/AGP-7.2.1-blue?style=flat"/></a>
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
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

## Features

> ### SNS & Streamer Selection

<div align="center">

| Switch from Twitch to Youtube Content | Show Content from the Selected Streamer | Show Content from All Streamers |
| :---------------: | :---------------: | :---------------: |
| <img src="https://github.com/june0122/webp-test/blob/master/docs/sns_change.webp" align="center" width="300px"/> | <img src="https://github.com/june0122/webp-test/blob/master/docs/streamer_selection.webp" align="center" width="300px"/> | <img src="https://github.com/june0122/webp-test/blob/master/docs/streamer_selection_all.webp" align="center" width="300px"/> |

</div>

> ### Themes

<div align="center">

| Multiple Color Themes | Change Dark Themes by System Default | Dark / Light / System Default Options |
| :---------------: | :---------------: | :---------------: |
| <img src="https://github.com/f-lab-edu/wak-plus/blob/main/docs/theme_change.webp" align="center" width="300px"/> | <img src="https://github.com/june0122/webp-test/blob/master/docs/dark_mode_setting2.webp" align="center" width="300px"/> | <img src="https://github.com/f-lab-edu/wak-plus/blob/main/docs/dark_mode_setting1.webp" align="center" width="300px"/> |

</div>

> ### Favorites

<div align="center">

| Add or Remove Content from Favorites  | Filter by SNS |
| :---------------: | :---------------: |
| <img src="https://github.com/june0122/webp-test/blob/master/docs/favorite_add_remove.webp" align="center" width="300px"/> | <img src="https://github.com/june0122/webp-test/blob/master/docs/favorite_filter.webp" align="center" width="300px"/> |

</div>

## Design in Figma

![Figma](https://github.com/f-lab-edu/wak-plus/blob/main/docs/figma.png)

## MAD Score

![MAD Score](https://user-images.githubusercontent.com/39554623/181916429-6c50c917-79de-4557-999a-daa48016b378.png)

## License

```xml
Designed and developed by 2022 Junseop Lim

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
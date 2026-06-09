# 🎬 LeeStreamTV — System Prompt & Build Specification

You can copy and paste the contents of this document as a system prompt or a detailed build specification for an AI coder to construct, rebuild, or extend **LeeStreamTV**—a high-performance, D-pad optimized, hybrid streaming media application.

---

## 🚀 System Prompt / Instructions

```text
You are an expert developer specializing in Android Native Development (Kotlin), Hybrid Web architectures, and Media Streaming. 

Your task is to build or extend a hybrid streaming media application named StreamTV. The application consists of two main parts:
1. A fullscreen, D-pad nav-friendly, glassmorphic Single Page Web App (HTML5 + CSS3 + JS) that aggregates movie/show metadata, manages settings, scrapes streaming links in parallel, parses audio capabilities, and handles IPTV playlists.
2. A native Android wrapper (Kotlin Activity with fullscreen WebView) that exposes JavaScript Interfaces to bridge local operations (external player launching, APK updates, YouTube integration, and system back-button handlers).

Adhere strictly to the architecture, endpoints, keys, and implementation logic detailed in the technical specification below.
```

---

## 🛠️ Technical Specifications & Assets

### 1. External API Keys & Metadata Aggregator (TMDB)
* **TMDB API Key**: `8711e2c6b0504a3277a840e1dde5ed86`
* **TMDB Base URL**: `https://api.themoviedb.org/3`
* **TMDB Image CDN**: `https://image.tmdb.org/t/p/w500` (Posters) or `https://image.tmdb.org/t/p/original` (Backdrops)
* **Primary Endpoints used**:
  - Trending Movies: `/trending/movie/week?api_key={apiKey}&page={page}`
  - Trending TV Shows: `/trending/tv/week?api_key={apiKey}&page={page}`
  - Multi Search (Query-based): `/search/multi?api_key={apiKey}&query={query}&page={page}`
  - Discover Catalog (Filters, Providers, Years): `/discover/movie` or `/discover/tv` with parameters:
    - `&with_watch_providers={providerId}&watch_region=US`
    - `&with_genres={genreId}`
    - `&primary_release_year={year}` (Movies) or `&first_air_date_year={year}` (TV)
  - Detail Lookups: `/movie/{id}` or `/tv/{id}`
  - TV Season/Episode Guides: `/tv/{id}/season/{seasonNumber}`
  - External IDs (Resolve IMDb ID from TMDB ID): `/movie/{id}/external_ids` or `/tv/{id}/external_ids`
  - Trailer Video Lookups: `/movie/{id}/videos` or `/tv/{id}/videos` (Filter for YouTube Trailers)
  - Direct collections lookup: `/collection/{id}`

---

### 2. Multi-Threaded Parallel Scraping Engine
The app uses asynchronous parallel fetching (`Promise.allSettled`) to query multiple Stremio/Web-compatible metadata resolvers. It resolves search queries using the movie's **IMDb ID** (for movies) or **IMDb ID:Season:Episode** format (for TV series).

#### Scraper Endpoints:
1. **Torrentio (P2P & Real-Debrid cached torrents)**:
   - Format: `https://torrentio.strem.fun/{config}/stream/{type}/{queryId}.json`
   - `{type}`: `movie` or `series`
   - `{queryId}`: `ttXXXXXXX` (Movies) or `ttXXXXXXX:{season}:{episode}` (TV Shows)
   - `{config}` (Public torrents): `providers=yts,eztv,rarbg,1337x,thepiratebay,torrentgalaxy`
   - `{config}` (Real-Debrid Auth): `providers=yts,eztv,rarbg,1337x,thepiratebay,torrentgalaxy|realdebrid={rdToken}`
2. **NoTorrent (Direct HTTP HLS Resolver)**:
   - URL: `https://addon.notorrent2.workers.dev/stream/{type}/{queryId}.json`
3. **StreamViX (Direct Stream Aggregator)**:
   - URL: `https://streamvix.hayd.uk/{"tmdbApiKey":"","mediaFlowProxyUrl":"","mediaFlowProxyPassword":"","animeunityEnabled":"on","animesaturnEnabled":"on","animeworldEnabled":"on"}/stream/{type}/{queryId}.json`
4. **HdHub (Premium Direct HTTP Links)**:
   - URL: `https://hdhub.thevolecitor.qzz.io/eyJ0b3Jib3giOiJ1bnNldCIsInF1YWxpdGllcyI6IjIxNjBwLDEwODBwLDcyMHAiLCJzb3J0IjoiZGVzYyJ9/stream/{type}/{queryId}.json`

#### Stremio Collections Catalogs:
Surfaces curated catalogs by reading from collections servers:
- **Vercel Catalog**: `https://ntl-collections-en.vercel.app/catalog/series/ntl_collections_catalog.json`
- **Collection Metadata**: `https://ntl-collections-en.vercel.app/meta/series/{collectionId}.json`

#### CORS Proxy Fallback Handler:
When direct fetching fails due to browser CORS policies, requests are automatically routed through a chain of public proxies:
```javascript
const proxies = [
    (url) => `https://corsproxy.io/?${encodeURIComponent(url)}`,
    (url) => `https://api.codetabs.com/v1/proxy?quest=${encodeURIComponent(url)}`,
    (url) => `https://api.allorigins.win/raw?url=${encodeURIComponent(url)}`,
    (url) => `https://thingproxy.freeboard.io/fetch/${url}`
];
```

---

### 3. Real-Debrid Premium Integration
To secure direct premium links and bypass torrent bandwidth limits, a Real-Debrid flow is integrated:
* **OAuth Client ID**: `CEZWNFZ6BSSMK` (Standard community Client ID)
* **API Flows**:
  - Request Pairing Code: `POST` to `https://api.real-debrid.com/oauth/v2/device/code?client_id=CEZWNFZ6BSSMK`
  - Poll Pairing Status: `GET` to `https://api.real-debrid.com/oauth/v2/device/credentials?client_id=CEZWNFZ6BSSMK&code={deviceCode}`
  - Exchange Credentials: `POST` to `https://api.real-debrid.com/oauth/v2/token` passing `client_id`, `client_secret`, and `deviceCode` via FormData.
  - Validate / Get User Profile: `GET` to `https://api.real-debrid.com/rest/1.0/user?auth_token={accessToken}`
* **Manual Entry**: Provide a direct input panel for pasting a static API Key (`https://real-debrid.com/apitoken`) because third-party remote scraping servers can experience IP-locking issues with transient OAuth codes.

---

### 4. Playback, Resolution, & Audio Codec Filters
1. **P2P/HTTP Stream Sorting**: Aggregated links are parsed and sorted by descending priority weight:
   - Type weight: Real-Debrid Premium Cache (`rd`) > High-Quality Web Direct (`hd`) > Free Public/P2P (`free`).
   - Resolution weight: `4K Premium` > `4K HTTP` > `4K P2P` > `1080p Premium` > `1080p HTTP` > `1080p P2P` > `720p Premium` > `720p HTTP`.
2. **Audio Codec Classifier**:
   WebViews cannot process multi-channel HD audio codecs. Parse stream titles to distinguish audio types:
   - **Silent/HD Audio Alert**: Flag formats containing `DTS`, `TrueHD`, `Atmos`, `AC3`, `AC-3`, `DDP`, `EAC3`, `E-AC-3`, `5.1ch`, `7.1ch`, `5.1`, `7.1`. Label these as `⚠️ HD Audio (Silent on Web)` and prompt the user to use external players.
   - **Web Stereo Approved**: Tag formats containing `AAC`, `MP3`, `Opus`, `Vorbis`, `2.0`, `Stereo` as `🔊 Browser Stereo`.

---

### 5. Native Android Wrapper (Kotlin WebView Bridge)
Exposes the JS interfaces `LeeStreamTVBridge` and `LeePrimeBridge` to index files.

#### Manifest Requirements (`AndroidManifest.xml`):
- `<uses-permission android:name="android.permission.INTERNET" />`
- `<uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />` (For APK updates)
- `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />`
- `<uses-feature android:name="android.software.leanback" android:required="false" />` (Android TV support)
- `android:usesCleartextTraffic="true"` inside `<application>` (Allows unsecure HTTP streaming).

#### JavaScript Bridge Interfaces (`@JavascriptInterface`):
1. `isAvailable(): Boolean` - Confirms native Android environment is present.
2. `playInMXPlayer(url, title)` - Launches intent for MX Player Free/Pro (`com.mxtech.videoplayer.ad`, `com.mxtech.videoplayer.pro`).
3. `playInVLC(url, title)` - Launches intent for VLC (`org.videolan.vlc`).
4. `playInJustPlayer(url, title)` - Launches intent for Just Player (`com.brouken.player`).
5. `playInDefaultPlayer(url, title)` - Launches generic intent using system chooser (`video/*`).
6. `openYoutubeTrailer(videoId)` - Launches native `vnd.youtube:{videoId}` intent or web fallback.
7. `downloadAndInstallAPK(apkUrl)` - Enqueues package download via Android's `DownloadManager`, registers a broadcast receiver for `ACTION_DOWNLOAD_COMPLETE`, and triggers the package installer (`application/vnd.android.package-archive`) with read URI permissions.

#### Extra Intent Headers (Bypassing Hotlink Blocking):
To ensure external players can load protected streams, inject headers into the player intents:
```kotlin
// VLC Intent format
putExtra("headers", arrayOf("User-Agent: Mozilla/5.0..."))
// MX Player Intent format
putExtra("headers", arrayOf("User-Agent", "Mozilla/5.0..."))
// Standard Android bundle fallback
val bundle = Bundle().apply { putString("User-Agent", "Mozilla/5.0...") }
putExtra("extra_headers", bundle)
```

#### D-Pad Navigation & System Back Button Hook:
- Intercept the system physical remote controller back button inside `onKeyDown()`:
  ```kotlin
  if (keyCode == KeyEvent.KEYCODE_BACK) {
      webView.evaluateJavascript("javascript:if(typeof window.handleAndroidBackPress === 'function') { window.handleAndroidBackPress(); } else { false; }") { result ->
          if (result == "false" || result == "null") {
              showExitDialog() // standard native alert dialog to exit app
          }
      }
      return true
  }
  ```

---

### 6. IPTV / Live TV Engine
- **Playlist Source**: Parses standard IPTV `.m3u` playlists using simple line-by-line parsing:
  - `#EXTINF` groups extract `tvg-logo` (channel icon URL), `group-title` (category tag), and commas split the channel name.
  - Successive line starting with `http` maps the live TV stream source URL.
- **Categorization**: Groups channels dynamically based on `group-title`.
- **Playback**: Feeds the channel stream URL into the HTML5 video player (support for standard HLS `.m3u8` playlists) or launches it via external Android players.

---

### 7. UX, Layout, and D-Pad Controls Guidelines
- **Responsive Layout**: Designed specifically for 1080p TV viewports (no vertical/horizontal overflow on the main window; scrolling is isolated to lists and detail boxes).
- **Glassmorphism**: Minimal translucent panels with subtle blurs (`backdrop-filter: blur(16px)`), thin glowing borders (`1px solid rgba(255,255,255,0.06)`), and heavy backdrop glows.
- **D-Pad Engine**: Implement visual focus highlights (typically orange shadows and scale shifts `.scale(1.05)`) on all key grid items. Use remote D-pad click listener simulation when navigating.
- **Auto-scroll focus target**: When moving between horizontal movie shelves, the viewport should center the currently focused card to prevent off-screen focus elements.
- **Backpress Hook**: Inside `window.handleAndroidBackPress()`, if a modal (like details, scraper, settings, or player) is open, close the modal first and return `true` to notify the Android wrapper that the event was handled. Only return `false` if the user is on the main catalog screen.

# Ultimate LLM Prompt: Recreate LeeStreamTV Standalone Web App

Copy and paste this markdown prompt into any advanced LLM (such as Gemini 1.5 Pro, Gemini 2.0 Flash, Claude 3.5 Sonnet, etc.) to recreate the complete **LeeStreamTV** standalone web application.

***

```markdown
You are a senior front-end engineer specializing in Smart TV application design (Android TV, Fire TV, Apple TV) and HTPC web interfaces. Your task is to build a complete, production-ready, single-file standalone HTML5 web application named **LeeStreamTV**.

This application is designed to run inside an Android WebView container (with native TV remote integration) as well as standard desktop and mobile browsers.

---

## 🛠️ High-Level Architecture & Technical Rules
1. **Single File Structure**: Output a single `index.html` file containing all HTML structure, inline CSS styles (in `<style>`), and JavaScript logic (in `<script>`). No local JavaScript or CSS imports are allowed.
2. **Third-Party CDN Dependencies**:
   - **Google Fonts**: Load Outfit, Space Grotesk, and JetBrains Mono.
   - **Lucide Icons**: Include the Lucide icons library (`https://unpkg.com/lucide@latest`) and initialize it dynamically using `lucide.createIcons()` whenever new DOM nodes are rendered.
3. **Vanilla Development**: Write clean, modern Vanilla HTML5/CSS3/JavaScript (ES6+). Do not use external framework code (React, Vue, Tailwind, etc.).
4. **Resiliency & Fallbacks**: Provide structured offline/fallback mock data for catalogs, IPTV, and guides so that the app remains functional and visually complete even when APIs fail.

---

## 🎨 Design System & UI Aesthetics
- **Theme & Palette**: Curate a premium, dark-mode, glassmorphic space/cinema aesthetic:
  - Base Background: Deep dark slate/black (`#0a0a0c`).
  - Card Containers: Semi-transparent panels (`rgba(22, 22, 28, 0.7)`) with blur filters.
  - Active Elements: Accent orange (`#ff7a00`) with glow effects (`rgba(255, 122, 0, 0.4)`).
  - Premium Streams: Accent green (`#3ecf8e`).
  - Free Streams: Accent blue (`#3b82f6`).
- **Interactive Startup Splash Screen**:
  - Show a full-screen brand splash overlay with an animated progress loader.
  - Automatically fade out and **remove the element from the DOM** after 3.2 seconds. *Crucial:* The element must be deleted, not just hidden, so that it does not intercept D-pad navigation or mouse clicks.
- **Ambient Glows**: Place two large blur-filtered backdrop glow rings (orange in top-left, purple in bottom-right) behind the viewport.
- **Sidebar Layout**:
  - Left panel: Sidebar navigation with sub-categories: **Discover Movies**, **TV Shows**, **Collections**, **Watchlist**, **Settings**.
  - Provider section: Quick access filters for **Netflix**, **HBO Max**, **Disney+**, **Prime Video**, and **Apple TV+**.
  - Right panel: Main viewport rendering search bars, filter shelves, dynamic content grids, or detail modals.

---

## 🎮 Android TV D-Pad Spatial Navigation Engine
Implement a robust spatial navigation system to make the app fully controllable using an Android TV/Firestick remote control (handling D-pad key events and keyboard keys):

1. **Key Listeners**: Intercept `ArrowLeft`, `ArrowRight`, `ArrowUp`, `ArrowDown`, `Enter`, `Backspace`, and `Escape`.
2. **Focus Classes**: Manage a global `.focused-tv` CSS class that applies a distinct highlight ring (1px solid `#ff7a00` with glow) and slight scaling (1.02x) on the currently selected item.
3. **Spatial Mapping Algorithm**:
   - On pressing arrow keys, calculate the bounding rectangle of the current active element.
   - Loop through all focusable elements (buttons, inputs, cards, pills, menu items) and calculate the Euclidean distance (`primaryDist + orthoDist * 5.0`) in the direction of travel.
   - Select the closest candidate element and call `setFocus(element)`.
4. **Auto-Scrollable Parent Container Handler**:
   - In `setFocus()`, climb the DOM tree to locate the nearest scrollable parent (such as a modal or container styled with `overflow-y: auto`).
   - If the newly focused element is positioned off-screen, trigger a smooth `scrollTo` to center it vertically.
5. **Modal/Overlay Interception Hooks**:
   - **Details Modal**: On opening, immediately focus the close button (for movies/collections) or the active season pill (for TV shows). On close, restore focus to the previously active media card.
   - **Scraper Modal**: Focus the close button first. On close, restore focus to the selected episode card or details button.
   - **Settings Modal**: Focus the first toggle on open; restore focus to the active tab menu on close.
6. **Pointer Hover Sync**: Synchronize mouse/touch hover events so that hovering over any focusable element automatically aligns the D-pad focus state to that element.
7. **Android Back Button Bridge**: Define `window.handleAndroidBackPress()` returning `true` or `false` to let a native wrapper intercept back events. It must sequentially close: open video players, player selection sheets, scraper overlays, modals, and finally fall back to switching to the "Discover Movies" tab.

---

## 📺 Feature Implementations & Logic Rules

### 1. TMDB Catalog & Search API
- **API Config**: Use TMDB API Key `8711e2c6b0504a3277a840e1dde5ed86` with base URL `https://api.themoviedb.org/3`.
- **Discovery**: Fetch trending lists, category lists (Action, Comedy, Sci-Fi, etc.), and filter parameters (by year using a dropdown, by genre using a horizontal pill bar).
- **Search**: Bind search inputs to query TMDB dynamically.
- **Details modal**: Load synopses, backdrops, ratings, credits (cast names and headshots), and similar titles.

### 2. Stremio & TMDB Collections Integration
- **Catalog Source**: When the "Collections" sidebar tab is clicked, fetch collection data from `https://ntl-collections-en.vercel.app/catalog/series/ntl_collections_catalog.json`.
- **Collections View**: Allow browsing curated collection cards (e.g. John Wick, Lord of the Rings, The Dark Knight, The Matrix, Marvel).
- **Direct Lookup**: Integrate collection fetching. If a collection starts with `tmdb:col:`, query TMDB directly using `${TMDB_BASE_URL}/collection/{id}`. Otherwise, fetch Stremio-mapped collection data from `https://ntl-collections-en.vercel.app/meta/series/{id}.json`.
- **Included Movies Shelf**: When a collection is clicked, open the details modal. Hide standard streaming play buttons and replace them with an **Included Movies** horizontal shelf. Clicking any item on this shelf must fetch and launch details for that specific movie. Auto-focus the first movie card on load.

### 3. Continue Watching & Watchlist
- **LocalStorage State**: Maintain a watchlist array and a continue watching dictionary in `localStorage`.
- **Progress Tracking**: Keep track of `id`, `title`, `poster`, `type` (movies vs shows), `season`, `episode`, `currentTime`, `duration`, `percentage`, and `timestamp`.
- **UI Integration**: Display a "Continue Watching" shelf at the top of the main catalog. Render a custom progress bar on each card's bottom edge.

### 4. Real-Debrid Integration
- **OAuth Device Flow**:
  - Implement the RD device code activation workflow. Fetch a code from `https://api.real-debrid.com/oauth/v2/device/code`, display the user-code to the user, and begin polling the `/credentials` and `/token` endpoints.
  - Provide a manual token entry field in settings.
- **Token Verification**: Verify token validity on boot, refresh active sessions, and display user account profiles (expiration dates, premium statuses).

### 5. Multi-indexer Parallel Stream Scraper
- **IMDb ID Resolution**: Query TMDB's external IDs endpoint `/external_ids` to translate TMDB IDs into IMDb IDs.
- **Parallel Scrapers**: Query the following indexers simultaneously using `Promise.allSettled`:
  - **Torrentio**: `https://torrentio.strem.fun/{config}/stream/{type}/{imdbId}.json` (passes the Real-Debrid token inside the configuration segment if paired).
  - **NoTorrent**: `https://addon.notorrent2.workers.dev/stream/{type}/{imdbId}.json`
  - **StreamViX**: `https://streamvix.hayd.uk/...`
  - **HdHub**: `https://hdhub.thevolecitor.qzz.io/...`
- **Link Parser & Sorting**:
  - Extract stream URLs, titles, and resolutions (4K, 1080p, 720p).
  - Sort the resulting links: Priority 1: Real-Debrid cached premium links (`rd`), Priority 2: Direct HTTP HLS links (`hd`), Priority 3: P2P magnet streams (`free`). Order by resolution descending inside each priority.
- **Audio Codec Compatibility Checking**:
  - Parse the stream title for Dolby Digital, DTS, Atmos, TrueHD, AC3, EAC3, and 5.1/7.1 channel markers.
  - Mark matching titles with a `⚠️ HD Audio (Silent on Web)` badge (since standard web browsers/WebViews cannot decode these audio codecs directly, leading to a silent stream).
  - Mark standard stereo/AAC streams with a `🔊 Browser Stereo` badge.
- **User Settings**: Support options to:
  - Toggle "Web Sound Only" (hides HD Audio links).
  - Toggle "Autoplay" (automatically launches the first compatible link).
  - Toggle "Autoplay HdHub" (prefers HdHub links).

### 6. HTML5 Video Player & External Player Intents
- **Built-in Player**:
  - Full-screen custom HTML5 video overlay with progress slider, playback timers, play/pause controls, and volume control.
  - Automatically loads and resumes playback from the `localStorage` continue-watching timestamp.
- **External Player Chooser**:
  - On Android devices, when launching a stream, prompt the user to choose between: **Built-in Player**, **VLC**, **MX Player**, **Just Player**, or **System Default**. Let them tick a checkbox to "Remember Choice".
  - If a player is chosen, trigger native Android Intents:
    - VLC package: `org.videolan.vlc`
    - MX Player package: `com.mxtech.videoplayer.ad`
    - Just Player package: `com.brouken.player`
    - Construct the intent string (e.g. `intent://{stream_url}#Intent;scheme={http/https};type=video/*;package={package_name};S.title={title};end`).
  - Calculate and append the resume position in milliseconds (`resumePositionMs`) to the Intent extras.
- **WebView Native Bridge**:
  - Check if `window.LeeStreamTVBridge` or `window.LeePrimeBridge` exists. If present, use native calls instead of intent schemes: `bridge.playInVLC(url, title, resumePositionMs)`, `bridge.playInMXPlayer(...)`, etc.
  - Expose `window.onExternalPlayerResult(positionSec, durationSec)` so that the host Android app can pass back playback updates to save progress when a user exits an external player.

### 7. IPTV Live TV Section
- **Playlist Downloader**: Parse standard `.m3u` or `.m3u8` playlists, extracting group tags, logo paths, and stream URLs.
- **Guide Parser (EPG)**: Fetch and parse standard XMLTV `.xml` or `.xml.gz` guides. Parse start/stop tags, title summaries, and descriptions.
- **Schedule Simulator**: If the guide link fails, automatically generate simulated 24-hour schedules based on the channel name for continuous user feedback.
- **Live Interface**: Left sidebar listing categories (News, Sports, Movies, etc.), center list showing channels, right container displaying the live video player and the EPG grid schedule.

### 8. Application Auto-Updates
- Fetch `update.json` from the repository. Compare its `versionCode` against the current application `versionCode`.
- If an update is available, prompt the user with a modal containing the changelog, a "Skip" button, and an "Update Now" button (which downloads the updated APK file).

---

Please output the complete, fully realized, standalone HTML code inside a markdown block. Include all styling, layout classes, scripting engines, spatial navigation coordinates logic, API integrations, and parsers. Do not truncate the Javascript logic or CSS styling. Write comments explaining crucial modules.
```

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
4. **Resiliency & Fallbacks**: Provide structured offline/fallback mock data for catalogs and guides so that the app remains functional and visually complete even when APIs fail.

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
  - Left panel: Sidebar navigation with sub-categories: **Discover Movies**, **TV Shows**, **Collections**, **Debrid History**, **Watchlist**, **Settings**, **Info**.
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
- **TV Show Episode Guide**: Build an episodic grid for TV Shows allowing users to navigate through specific seasons and episodes.
- **Search**: Bind search inputs to query TMDB dynamically.
- **Details modal**: Load synopses, backdrops, ratings, credits (cast names and headshots), and similar titles.

### 2. Stremio & TMDB Collections Integration
- **Catalog Source**: When the "Collections" sidebar tab is clicked, fetch collection data from `https://ntl-collections-en.vercel.app/catalog/series/ntl_collections_catalog.json`.
- **Collections View**: Allow browsing curated collection cards.
- **Direct Lookup**: Integrate collection fetching. If a collection starts with `tmdb:col:`, query TMDB directly using `${TMDB_BASE_URL}/collection/{id}`. Otherwise, fetch Stremio-mapped collection data.
- **Included Movies Shelf**: When a collection is clicked, open the details modal. Hide standard streaming play buttons and replace them with an **Included Movies** horizontal shelf. Auto-focus the first movie card on load.

### 3. Continue Watching & Watchlist
- **LocalStorage State**: Maintain a watchlist array and a continue watching dictionary in `localStorage`.
- **Progress Tracking**: Keep track of `id`, `title`, `poster`, `type`, `season`, `episode`, `currentTime`, `duration`, `percentage`, and `timestamp`.
- **UI Integration**: Display a "Continue Watching" shelf at the top of the main catalog. Render a custom progress bar on each card's bottom edge. Auto-mark items as watched if >90% complete.

### 4. Real-Debrid & All-Debrid Integration
- **OAuth Device Flow**:
  - Implement the RD and AD device code activation workflows. Display the user-code to the user, and begin polling their respective endpoints.
  - Provide manual token entry fields in settings.
- **Token Verification**: Verify token validity on boot, refresh active sessions, and display user account profiles.
- **Cross-Device Debrid History Tab**:
  - Fetch streaming history from Real-Debrid and All-Debrid APIs.
  - Cross-reference torrent/link names against TMDB to automatically scrape and assign TMDB movie/show posters instead of displaying blank thumbnails.
  - Allow direct 1-click playback parsing from this history view.

### 5. Multi-indexer Parallel Stream Scraper
- **IMDb ID Resolution**: Query TMDB's external IDs endpoint `/external_ids` to translate TMDB IDs into IMDb IDs.
- **Parallel Scrapers**: Query indexers simultaneously (Torrentio, NoTorrent, StreamViX, HdHub) using `Promise.allSettled`.
- **Link Parser & Sorting**:
  - Extract stream URLs, titles, resolutions (4K, 1080p, 720p).
  - Sort links: Priority 1: Debrid cached premium links (`rd`/`ad`), Priority 2: Direct HTTP HLS links (`hd`), Priority 3: P2P magnet streams (`free`). Order by resolution descending.
- **Audio Codec Compatibility Checking**:
  - Mark matching titles with a `⚠️ HD Audio (Silent on Web)` badge for unplayable formats.
  - Mark standard stereo/AAC streams with a `🔊 Browser Stereo` badge.
- **User Settings & Info**:
  - Provide toggles for "Web Sound Only", "Auto-Select 4K", "Autoplay", and "Autoplay HdHub".
  - Include an `Info` page tab dynamically explaining all these toggles.
  - Implement a "Show Scraper Logs" diagnostic overlay toggle.

### 6. HTML5 Video Player & External Player Intents
- **Built-in Player**:
  - Full-screen custom HTML5 video overlay with progress slider, timers, and volume control.
  - Stream Download support (Download buttons overlay).
- **External Player Chooser**:
  - Prompt user to choose between: **Built-in Player**, **VLC**, **MX Player**, **Just Player**, or **System Default** via intent strings.
  - Calculate and append the resume position in milliseconds to the Intent extras for external player tracking.
- **WebView Native Bridge**:
  - Use `bridge.playInVLC()`, `bridge.downloadAndInstallAPK()`, etc., if native wrapper detected.
  - Expose `window.onExternalPlayerResult(positionSec, durationSec)` so that the host app can report exit positions back to the Continue Watching progress system.

### 7. Application Auto-Updates & Changelog History
- Fetch `update.json` from the repository. Compare its `versionCode` against the current application `versionCode`.
- If an update is available, prompt the user with an Update modal.
- Render the complete historical changelog (from v1.2.0 upwards) inside the `Info` screen. If a new update is found, dynamically inject its updated changelog string into the DOM, avoiding older CDN-cached versions.

---

Please output the complete, fully realized, standalone HTML code inside a markdown block. Include all styling, layout classes, scripting engines, spatial navigation coordinates logic, API integrations, and parsers. Do not truncate the Javascript logic or CSS styling. Write comments explaining crucial modules.
```

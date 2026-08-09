# Debrid Downloader App Prompt (Discover, Resolve & Download)

Copy and paste the markdown block below into any advanced coding AI (such as Gemini 1.5 Pro, Claude 3.5 Sonnet, or GPT-4o) to generate a complete, premium-grade standalone media downloader application.

***

```markdown
You are a senior frontend engineer specializing in HTPC web interfaces, responsive mobile layout design, and hybrid Android WebView integrations. Your task is to build a complete, production-ready, single-file standalone HTML5 web application named **LeeDebridDownloader** that lists movies and TV shows, scrapes stream sources, resolves them using Debrid services (Real-Debrid and All-Debrid), and downloads the files directly to a computer or phone without streaming.

---

## 🎨 UI/UX Design System: Futuristic & Cinematic Download Dashboard

Design a fresh, premium, and highly modern user experience (e.g., using glassmorphism, glowing card grids, smooth sliders, custom dark-mode theme variables, and rich transitions). The interface must look visually spectacular and work flawlessly on both desktop computers and mobile/phone screens.

### 1. Overall Style
* **Theme**: Ultra-modern, premium dark mode only.
* **Colors**: Deep Black (`#050505`) and Charcoal (`#111111`) backgrounds. Accent colors: Electric Violet (`#8B5CF6`), Neon Cyan (`#06B6D4`), and Emerald Green (`#10B981`) for download status indicators.
* **Aesthetics**: Glassmorphism frosted panels (`backdrop-filter: blur(12px)`), subtle neon glow outlines, and smooth hover scales (`scale(1.05)`).
* **Typography**: Clean sans-serif fonts ('Outfit' and 'Space Grotesk' via Google Fonts).

### 2. Layout & Navigation
* **Sidebar / Navigation Bar**: Floating glassmorphic navigation bar containing links for:
  * **Home (Discover)**: Media rows (Trending, Popular, Genres).
  * **Search**: Quick access search bar with live suggestion results.
  * **Transfers / Active Queue**: Real-time listing of active Debrid cloud torrent cache tasks.
  * **Completed History**: Locally persisted download history.
  * **Settings**: Debrid credentials, download preferences, and diagnostic details.
* **Responsive Layout**: Fluid flex/grid structures that adapt instantly between wide desktop viewports and narrow vertical mobile phone screens.

---

## 🛠️ High-Level Technical Rules
1. **Single File Structure**: Produce a single, complete `index.html` file containing the document structure, CSS styling (in `<style>`), and JavaScript logic (in `<script>`). No external files or local module imports are allowed.
2. **Third-Party CDN Dependencies**:
   - **Google Fonts**: Load font families 'Outfit', 'Space Grotesk', and 'JetBrains Mono'.
   - **Lucide Icons**: Load Lucide icons from CDN `https://unpkg.com/lucide@latest`. Initialize or update icons dynamically using `lucide.createIcons()` whenever new DOM nodes are rendered.
3. **Vanilla Development**: Use modern ES6+ Vanilla JavaScript. Do not use framework wrappers (React, Vue, Tailwind, etc.).
4. **Resiliency & Fallbacks**: If external API calls fail or credentials are empty, implement robust fallback mock data (such as simulated catalogs, collections, and mock scraper lists) so the app remains fully interactive.
5. **CORS Request Routing**: Implement a transparent CORS fetch helper called `fetchWithCORS(url, options)` that first attempts a direct fetch, and falls back to a chain of public CORS proxies if it encounters network or origin restrictions. The helper must handle public proxies like `corsproxy.io`, `codetabs`, `allorigins` (unwrapping the `.contents` envelope if returned), and `thingproxy`.

---

## 📺 Core Catalog Features (TMDB API)
* **Base Config**: URL `https://api.themoviedb.org/3`, API Key `8711e2c6b0504a3277a840e1dde5ed86`.
* **Discover & Search**:
  * Fetch trending lists (`/trending/movie/day`, `/trending/tv/day`) and display them in horizontal, inertia-scroll rows.
  * Implement an intuitive search container that filters listings dynamically using the search query API.
* **Media Details**:
  * Show key meta-parameters: title, overview, rating, release year, genre badges, and backdrop art.
  * **TV Show Episode Selector**: Fetch series metadata (`/tv/{id}`) to list seasons. On season selection, fetch `/tv/{id}/season/{season_number}` to list all episodes with detailed titles, numbers, and descriptive metadata.
  * Provide a prominent "Get Download Links" button instead of a "Play Stream" option.

---

## 🔗 Scraper & Indexer Integration
1. **IMDb Mapping**: Query TMDB external IDs (`/${type}/${id}/external_ids`) to resolve the media's IMDb identifier (e.g., `tt1234567`).
2. **Parallel Indexer Queries**: Query Torrentio's stream endpoint in parallel using `Promise.allSettled`:
   * **Torrentio Endpoint**: `https://torrentio.strem.fun/providers=yts,eztv,rarbg,1337x,thepiratebay,torrentgalaxy/stream/{type}/{imdbId}.json`
   * *For TV shows, format the IMDb query as `{imdbId}:{season}:{episode}`*
3. **Candidate Stream Standardization**: Map the response streams to a clean schema:
   `{ fileTitle, size (parsed to GB/MB), resolution, seeds, magnetUrl / infoHash, provider }`
   * Sort links descending: Resolution (`4K` > `1080p` > `720p` > `SD`), and within resolution, sort by file size descending.
   * Display audio codec indicator badges (e.g., DTS, TrueHD, Dolby 5.1, AAC) parsed from the file titles using regex mappings.

---

## 📥 Debrid-Assisted Download Workflow

The core functionality of this app is to retrieve direct, high-speed HTTP download links by utilizing Debrid services as intermediaries to download torrents.

### 1. Real-Debrid API Integration
Support both OAuth 2.0 Device Flow (using client ID `CEZWNFZ6BSSMK`) and static Private API tokens (retrieved from `https://real-debrid.com/apitoken`).
When the user clicks a scraped magnet/torrent stream:
1. **Add Magnet to Cloud**: POST `https://api.real-debrid.com/rest/1.0/torrents/addMagnet` with `magnet={magnetUrl}`. Returns a torrent `id`.
2. **Select Files**: POST `https://api.real-debrid.com/rest/1.0/torrents/selectFiles/{id}` with post body `files=all`.
3. **Monitor Cache Status**: Query GET `https://api.real-debrid.com/rest/1.0/torrents/info/{id}`.
   * If `status` is `downloaded` (meaning the torrent is 100% cached on Real-Debrid's servers), extract the resulting link from the `links` array.
   * If `status` is `downloading`, add this task to the **Active Transfers** dashboard and poll progress percentage until complete.
4. **Unrestrict Link**: Send the cached torrent link to POST `https://api.real-debrid.com/rest/1.0/unrestrict/link` with `link={link}`. This returns a direct, unthrottled HTTP download link (`download`).

### 2. All-Debrid API Integration
Authenticate using the user's API Key (`apikey`) retrieved from account settings.
1. **Add Magnet to Cloud**: GET `https://api.alldebrid.com/v4/magnet/upload?agent=LeeStream&apikey={apikey}&magnets[]={magnetUrl}`. Returns a magnet ID.
2. **Monitor Status**: GET `https://api.alldebrid.com/v4/magnet/status?agent=LeeStream&apikey={apikey}&id={id}`.
   * Parse status. Once complete, retrieve the unrestricted download link from the `links` array.
   * If the torrent is still downloading to All-Debrid's servers, display download progress, speeds, and seed count in the active queue.

---

## 📲 Downloader Client Implementation

Once the unthrottled direct HTTP download link is resolved, provide the user with the following download mechanisms:

### A. Web Browser Direct Downloads (Computer / Phone)
1. **Standard Browser Trigger**: Dynamically generate an `<a>` tag with the resolved link, and set `download="{filename}"`. Trigger a programmatic click to pass the URL to the browser's native download manager.
2. **Download Manager Compatibility**: Display a prominent "Copy High-Speed Link" button. Include a one-click copy to clipboard action so users can easily paste the download link directly into third-party managers (like Internet Download Manager (IDM), JDownloader, or ADM).

### B. Hybrid Native Android Bridge (For Android WebView Packages)
If running inside a native Android container, detect and communicate with the web host using JS interfaces:
* Look for an injected bridge interface (`window.bridge` or similar).
* Trigger the native download manager:
  ```javascript
  if (window.bridge && typeof window.bridge.downloadAndInstallAPK === 'function') {
      // Use the native bridge to pass the download task directly to Android's native system DownloadManager
      // In a production build, the native app side should catch standard file links and feed them into Android DownloadManager
  }
  ```

### C. Active Cloud Transfers & History Dashboard
* **Cloud Transfers Screen**: Allow users to monitor torrents currently downloading on their Debrid accounts.
  * Real-Debrid Endpoint: `GET https://api.real-debrid.com/rest/1.0/torrents` (polls every 5-10 seconds).
  * All-Debrid Endpoint: `GET https://api.alldebrid.com/v4/magnet/status?agent=LeeStream&apikey={apikey}`.
  * Display a beautiful grid showing: File Name, Transfer Speed (MB/s), Percent Complete (progress bar), Seeders, and a Cancel/Delete button.
* **Completed Downloads Log**: Store a local log inside `localStorage` (`leedebrid_downloads_history`) recording files successfully downloaded, showing their size, download timestamp, and the direct download links.

---

## 💾 LocalStorage State Management
Persist the following variables:
* `rd_access_token` & `ad_access_token` for Debrid authentication.
* `leedebrid_downloads_history` for completed downloads history.
* `leedebrid_settings` (preferred Debrid provider, default resolution target, preferred copy-vs-download behavior).
```

---

## 🛠️ How to Use This File
1. Copy the markdown block above starting from `You are a senior frontend engineer...` to the end of the code block.
2. Paste it directly into your preferred advanced coding assistant (Gemini, Claude, GPT) to build the custom downloader application.

# Recreate LeeStreamTV App Prompt (Logic-Focus, Custom UI)

Copy and paste the markdown block below into any advanced LLM (such as Gemini 1.5 Pro, Claude 3.5 Sonnet, or GPT-4o) to recreate the complete functional logic of the **LeeStreamTV** standalone web application while styling it with a completely new and custom UI.

***

```markdown
You are a senior front-end engineer specializing in Smart TV application design (Android TV, Fire TV, Apple TV), HTPC web interfaces, and hybrid Android WebView integration. Your task is to build a complete, production-ready, single-file standalone HTML5 web application named **LeeStreamTV** that implements all the functional logic, scraper pipelines, API integrations, and native bridge communications detailed below.

### 🎨 UI Design Requirement: A completely new style!
Do NOT copy the legacy layout or styling. Design a fresh, premium, and highly modern user experience (e.g., using glassmorphism, glowing card grids, smooth slide-ins, modern typography, custom theme variables, and rich transitions) of your own creation. The interface must look visually spectacular and work flawlessly for both TV D-pad remote controls and pointers/mice.

---

## 🛠️ High-Level Technical Rules
1. **Single File Structure**: Produce a single, complete `index.html` file containing the document structure, CSS styling (in `<style>`), and JavaScript logic (in `<script>`). No external files or local module imports are allowed.
2. **Third-Party CDN Dependencies**:
   - **Google Fonts**: Load font families 'Outfit', 'Space Grotesk', and 'JetBrains Mono'.
   - **Lucide Icons**: Load Lucide icons from CDN `https://unpkg.com/lucide@latest`. Initialize or update icons dynamically using `lucide.createIcons()` whenever new DOM nodes are rendered.
3. **Vanilla Development**: Use modern ES6+ Vanilla JavaScript. Do not use framework wrappers (React, Vue, Tailwind, etc.).
4. **Resiliency & Fallbacks**: If external API calls fail or credentials are empty, implement robust fallback mock data (such as simulated catalogs, collections, mock IPTV channel playlists, and generated TV guides) so the app remains fully interactive.
5. **CORS Request Routing**: Implement a transparent CORS fetch helper called `fetchWithCORS(url, options)` that first attempts a direct fetch (for CORS-disabled environments or local testing), and falls back to a chain of public CORS proxies if it encounters network or origin restrictions. The helper must handle public proxies like `corsproxy.io`, `codetabs`, `allorigins` (unwrapping the `.contents` envelope if returned), and `thingproxy`.

---

## 🎮 D-Pad Spatial Navigation Engine
The application must be fully controllable using a standard TV remote control (sending Arrow keys, Enter, Backspace, and Escape) as well as a mouse/pointer.

1. **Key Listeners**: Intercept `ArrowLeft`, `ArrowRight`, `ArrowUp`, `ArrowDown`, `Enter`, `Backspace`, and `Escape`.
2. **Focus State CSS Class**: Apply a class `.focused-tv` to the active focusable element. Ensure mouse hover events sync with this focus state (hovering over a focusable element shifts D-pad focus to it).
3. **Euclidean Spatial Algorithm**: When an arrow key is pressed, calculate the center coordinate of the currently focused element. Search all visible focusable elements (selectors: `.menu-item, .filter-pill, .genre-pill, .media-card, .continue-watching-card, .livetv-channel-card, select, input, button, a`, etc.).
   - Calculate distance vectors: `dx = candCenter.x - currentCenter.x`, `dy = candCenter.y - currentCenter.y`.
   - Weigh distance based on direction: `primaryDist + (orthoDist * 3.0)` (or `primaryDist + (orthoDist * 5.0)` as diagonal fallback).
   - Find the closest valid element in the arrow's direction and trigger `setFocus(element)`.
4. **Auto-Scrolling Containment**: In `setFocus()`, look up the DOM tree for scrollable containers (`overflow-y` or `overflow-x` set to `auto` or `scroll`). Smoothly scroll the container (horizontally or vertically) to center the focused element within the viewport.
5. **Overlay Focus Interception**:
   - On opening any modal/overlay (e.g., details panel, settings, link scraper, update prompt), immediately focus its primary action or close button.
   - On closing, restore focus to the previously active element.
6. **Back Button Action Handlers**: Intercept `Backspace` or `Escape` to close overlays in hierarchical order: video player -> link chooser -> details modal -> debrid modal -> exit confirmation.

---

## 📺 Core Feature Logic & Integrations

### 1. TMDB Catalog & Search API
- **Base Config**: URL `https://api.themoviedb.org/3`, API Key `8711e2c6b0504a3277a840e1dde5ed86`.
- **Query Paths**:
  - Trending: `/trending/movie/day` and `/trending/tv/day`
  - Genre discovery: `/discover/movie` and `/discover/tv` with `with_genres` parameter.
  - Search: `/search/movie` and `/search/tv` querying TMDB based on search inputs.
- **TV Show Episode Guide**:
  - Fetch show details (`/tv/{id}`) to obtain seasons list.
  - Fetch season details (`/tv/{id}/season/{season_number}`) to list all episodes.
  - Save cached seasons in `currentSeasonsCached` to avoid redundant API queries.
- **Recommendations & Cast**:
  - Fetch movie/show credits (`/movie/{id}/credits` or `/tv/{id}/credits`) to list top cast with profile images.
  - Fetch similar titles (`/movie/{id}/similar` or `/tv/{id}/similar`).

### 2. Stremio & TMDB Collections Integration
- **Collections Catalog**: Fetch curated collections metadata from: `https://ntl-collections-en.vercel.app/catalog/series/ntl_collections_catalog.json`
- **Direct Collection Fetching**:
  - If a collection ID matches `tmdb:col:{id}`, query TMDB directly: `${TMDB_BASE_URL}/collection/{id}`.
  - Otherwise, query Stremio metadata: `https://ntl-collections-en.vercel.app/meta/series/{id}.json`.
- **Display**: Render a grid of films within the selected collection inside the details modal. Disable standard scraper triggers, and instead offer a horizontal shelf of films. Clicking a collection movie card queries the film's details (`/movie/{id}`) and opens its respective details view.

### 3. LocalStorage State Management
Keep track of these states in `localStorage`:
- **Watchlist**: Array of mapped TMDB media objects: `cinema_watchlist_objects` (with legacy fallback check from `cinema_watchlist` ID string array).
- **Continue Watching**: Array of objects: `leestreamtv_continue_watching` capped at 20 items. Each item tracks: `id`, `type` (movie/shows), `title`, `poster`, `season`, `episode`, `currentTime` (seconds), `duration` (seconds), `percentage`, and `timestamp`.
- **Preferences**:
  - `rd_access_token` (Real-Debrid API Key)
  - `ad_access_token` (All-Debrid API Key)
  - `leestreamtv_preferred_player` / `leeprime_preferred_player` ('builtin', 'vlc', 'mx', 'just', 'default', 'ask')
  - `autoplay_hdhub_enabled` (boolean)
  - `show_scraper_logs_enabled` (boolean)
  - `autoselect_4k_enabled` (boolean)
  - `websound_only_enabled` (boolean)
  - `iptv_playlist_url` (M3U subscription URL)
  - `iptv_epg_url` (XMLTV television guide URL)

### 4. Multi-indexer Parallel Stream Scraper
1. **IMDb Mapping**: Query TMDB external IDs endpoint (`/${type}/${id}/external_ids`) to resolve the IMDb identifier (e.g., `tt1234567`).
2. **Parallel Indexer Queries**: Retrieve candidate streams in parallel using `Promise.allSettled` from:
   - **Torrentio**: `https://torrentio.strem.fun/{config}/stream/{type}/{imdbId}.json`
   - **NoTorrent**: `https://addon.notorrent2.workers.dev/stream/{type}/{imdbId}.json`
   - **StreamViX**: `https://streamvix.hayd.uk/.../stream/{type}/{imdbId}.json`
   - **HdHub**: `https://hdhub.thevolecitor.qzz.io/.../stream/{type}/{imdbId}.json`
   *(For TV shows, pass IMDb query in format `tt1234567:season:episode`)*
3. **Parsers**: Standardize candidate streams into a common object model:
   `{ resolution, type (rd/hd/free), size, provider, seeds, fileTitle, url }`
   - **Sorting Rules**: Sort resolved links descending. Weight premium debrid cached links (`rd`) first, direct HTTP HLS links (`hd`) second, and public P2P magnet links (`free`) third. Within each tier, order by resolution descending (`4K` > `1080p` > `720p` > `SD`).
4. **Audio Compatibility Warning**: Run a regex parser against filenames (`getAudioBadgeInfo`) to check for audio formats that are incompatible with standard web browsers (such as `DTS`, `TrueHD`, `Atmos`, `AC3`/`AC-3`, `DDP`, `EAC3`/`E-AC3`/`E-AC-3`, `5.1ch`, `7.1ch`). Mark them with a warning badge. Highlight standard browser-compatible audio formats (`AAC`, `MP3`, `Opus`, `Vorbis`, `2.0`, `Stereo`) with a stereo badge.
5. **Autoplay/Autoselect Settings**: If `autoselect_4k_enabled` is active, automatically select and play the first compatible stream. If `websound_only_enabled` is checked, skip incompatible audio streams during autoselect. If `autoplay_hdhub_enabled` is checked, auto-launch the first English HdHub stream.

### 5. Debrid Service Configurations
- **Real-Debrid OAuth Pairing**:
  - Request user code: `https://api.real-debrid.com/oauth/v2/device/code?client_id=CEZWNFZ6BSSMK`.
  - Display code & poll every 5 seconds: `https://api.real-debrid.com/oauth/v2/device/credentials?client_id=CEZWNFZ6BSSMK&code={device_code}`.
  - Swap credentials: POST to `https://api.real-debrid.com/oauth/v2/token` with post body fields: `client_id`, `client_secret`, `code`, and `grant_type="http://oauth.net/grant_type/device/1.0"`.
  - Validate profile: `https://api.real-debrid.com/rest/1.0/user?auth_token={token}`.
- **All-Debrid PIN Pairing**:
  - Request PIN: `https://api.alldebrid.com/v4/pin/get?agent=leestreamtv`.
  - Display PIN & poll: `https://api.alldebrid.com/v4/pin/check?agent=leestreamtv&check={check}&pin={pin}`.
  - Verify user key: `https://api.alldebrid.com/v4/user?agent=leestreamtv&apikey={apikey}`.
- **All-Debrid Streaming History Shelf**:
  - Fetch recent history streams: `https://api.alldebrid.com/v4/user/history?agent=leestreamtv&apikey={adToken}`.
  - Parse filename (`parseFilenameForTMDB`) with TV/Season/Episode and Movie/Year regexes. Query TMDB search endpoints (`/search/tv` or `/search/movie`) to dynamically retrieve backdrop/poster paths to display instead of blank items.
  - Playback: Click to unlock link using `https://api.alldebrid.com/v4/link/unlock?agent=leestreamtv&apikey={adToken}&link={link}` and route to player.

### 6. IPTV / Live TV Player System
- **Playlist Downloader**: Retrieve M3U file contents from user-input URL.
- **M3U Parser**: Loop through lines of the playlist. Match `#EXTINF:` entries to extract `tvg-logo` (logos), `group-title` (categories), name (comma-separated at the end of the line), and stream URLs on subsequent lines.
- **XMLTV EPG Parser**: Fetch XML TV guide data. Search XML for `<programme>` tags with a matching `channel` property. Extract `start`, `stop`, `<title>`, and `<desc>` fields to render TV guide timelines. Provide a local EPG scheduling simulator fallback (`generateMockEPG`) when EPG downloads fail.

### 7. Playback Engines & Intent Routing
- **Built-in HTML5 Video Player**: Custom overlays with progress slider, volume controls, play/pause states, and resume timing checks. If a stream fails (e.g., MKV files, which are unsupported in WebViews), display a warning and fallback to the player chooser on Android.
- **External Android TV Player Intent Routing**: If running on Android/Fire TV and an external player is preferred, compile a custom URI intent to launch the video stream:
  - **MX Player** (`com.mxtech.videoplayer.ad` or `com.mxtech.videoplayer.pro`):
    `intent://{cleanUrl}#Intent;scheme={http/https};type=video/*;package={mxPackage};S.title={title};i.position={resumePositionMs};S.headers={userAgentHeaders};end`
  - **VLC** (`org.videolan.vlc`):
    `intent://{cleanUrl}#Intent;scheme={http/https};type=video/*;package=org.videolan.vlc;S.title={title};B.from_start=false;S.headers={userAgentHeaders};end`
  - **Just Player** (`com.brouken.player`):
    `intent://{cleanUrl}#Intent;scheme={http/https};type=video/*;package=com.brouken.player;S.title={title};i.position={resumePositionMs};end`
  - **Default Player** (Intent scheme without package parameter).
- **WebView Native Bridge Communications**:
  - Expose JavaScript interface: `window.onExternalPlayerResult(positionSec, durationSec)`. When the native wrapper returns this result (e.g., on player exit), update the continue watching progress. If the video was watched >90%, mark it as watched (remove from continue watching history). Otherwise, update the percentage and saved resume timestamps.
  - Detect Android bridges named `LeeStreamTVBridge` or `LeePrimeBridge` which expose native wrapper tools:
    `bridge.isAvailable()`, `bridge.playInMXPlayer(url, title, positionMs)`, `bridge.playInVLC(url, title, positionMs)`, `bridge.playInJustPlayer(url, title, positionMs)`, `bridge.playInDefaultPlayer(url, title, positionMs)`, `bridge.downloadAndInstallAPK(url)`, `bridge.openYoutubeTrailer(videoId)`.

### 8. App Auto-Updates & Changelog History
- **Version Checks**: Fetch `update.json` from the repository with a cache-buster query string. Compare the JSON's `versionCode` against `CURRENT_VERSION_CODE = 24`.
- **Updates UI**: If a newer version is found, display an update modal. Clicking update calls the native Android bridge method `bridge.downloadAndInstallAPK(apkUrl)` or opens the URL in a browser fallback.
- **Dynamic Changelog**: Render the release changelog within the Info tab. Ensure that when a new update is detected, its new changelog details are rendered dynamically to prevent old cached versions from displaying.

---

## 🔬 Skeletons of Crucial Logic Functions
Use the exact implementations below to ensure logic correctness, routing, and data parsing work flawlessly:

### CORS Fetch Helper
```javascript
// Paste this verbatim to ensure all cross-origin requests bypass CORS blocks
async function fetchWithCORS(url, options = {}) {
    const timeout = options.timeout || 10000;
    
    async function fetchWithTimeout(fetchUrl, fetchOptions) {
        const controller = new AbortController();
        const id = setTimeout(() => controller.abort(), timeout);
        try {
            const response = await fetch(fetchUrl, {
                ...fetchOptions,
                signal: controller.signal
            });
            clearTimeout(id);
            if (response.ok) {
                const originalJson = response.json.bind(response);
                response.json = async function() {
                    const data = await originalJson();
                    if (data && typeof data === 'object' && 'contents' in data) {
                        try {
                            return JSON.parse(data.contents);
                        } catch (e) {
                            return data.contents;
                        }
                    }
                    return data;
                };
                const originalClone = response.clone.bind(response);
                response.clone = function() {
                    const clonedRes = originalClone();
                    const originalClonedJson = clonedRes.json.bind(clonedRes);
                    clonedRes.json = async function() {
                        const data = await originalClonedJson();
                        if (data && typeof data === 'object' && 'contents' in data) {
                            try {
                                return JSON.parse(data.contents);
                            } catch (e) {
                                return data.contents;
                            }
                        }
                        return data;
                    };
                    return clonedRes;
                };
            }
            return response;
        } catch (err) {
            clearTimeout(id);
            throw err;
        }
    }
    
    try {
        return await fetchWithTimeout(url, options);
    } catch (directError) {
        console.warn(`Direct fetch to ${url} failed. Trying CORS proxy...`, directError);
        const proxies = [
            (u) => `https://corsproxy.io/?${encodeURIComponent(u)}`,
            (u) => `https://api.codetabs.com/v1/proxy?quest=${encodeURIComponent(u)}`,
            (u) => `https://api.allorigins.win/raw?url=${encodeURIComponent(u)}`,
            (u) => `https://thingproxy.freeboard.io/fetch/${u}`
        ];
        
        for (let i = 0; i < proxies.length; i++) {
            try {
                const proxiedUrl = proxies[i](url);
                const response = await fetchWithTimeout(proxiedUrl, options);
                return response;
            } catch (proxyError) {
                console.warn(`Proxy ${i} failed for ${url}`);
            }
        }
        throw new Error("All CORS proxies failed to fetch URL: " + url);
    }
}
```

### Filename Parser for TMDB Posters (History Tab)
```javascript
function parseFilenameForTMDB(filename) {
    let clean = filename.replace(/\.(mkv|mp4|avi|mov)$/i, '');
    clean = clean.replace(/\./g, ' ');

    // TV shows: S01E01, S1E1, etc.
    const tvMatch = clean.match(/(.*?)\s*[Ss](\d{1,2})\s*[Ee](\d{1,2})/i);
    if (tvMatch) {
        return {
            title: tvMatch[1].trim(),
            isTv: true,
            season: parseInt(tvMatch[2], 10),
            episode: parseInt(tvMatch[3], 10)
        };
    }

    // Movies with release year
    const yearMatch = clean.match(/(.*?)\s*\(?((?:19|20)\d{2})\)?/);
    if (yearMatch) {
        return {
            title: yearMatch[1].trim(),
            year: parseInt(yearMatch[2], 10),
            isTv: false
        };
    }

    // Fallback cleaning
    let baseTitle = clean.replace(/(1080p|720p|2160p|4k|HDR|DV|H264|H265|x264|x265|DTS|DD5\.1|DDP5\.1|AAC|Web-DL|WEBRip|BluRay|BDRip|YIFY|RARBG|DL|ENG|ITA|SUB|MIRCrew)/gi, '');
    baseTitle = baseTitle.replace(/[\(\)\[\]]/g, '').replace(/\s+/g, ' ').trim();
    
    return { title: baseTitle || filename, isTv: false };
}
```

### Audio Codec Web Browser Compatibility Checker
```javascript
function getAudioBadgeInfo(fileTitle) {
    if (!fileTitle) {
        return { text: "🔊 Web Audio", className: "badge-audio-unknown", isWebCompatible: true };
    }
    const titleLower = fileTitle.toLowerCase();
    
    // Check unsupported web audio formats
    if (titleLower.includes('dts') || 
        titleLower.includes('truehd') || 
        titleLower.includes('atmos') || 
        titleLower.includes('ac3') || 
        titleLower.includes('ac-3') ||
        titleLower.includes('ddp') ||
        titleLower.includes('eac3') ||
        titleLower.includes('e-ac3') ||
        titleLower.includes('e-ac-3') ||
        titleLower.includes('5.1ch') ||
        titleLower.includes('7.1ch') ||
        titleLower.includes(' 5.1') ||
        titleLower.includes(' 7.1')
    ) {
        return { text: "⚠️ HD Audio (Silent on Web)", className: "badge-audio-hd", isWebCompatible: false };
    }
    
    // Explicitly browser-compatible formats
    if (titleLower.includes('aac') || 
        titleLower.includes('mp3') || 
        titleLower.includes('opus') || 
        titleLower.includes('vorbis') ||
        titleLower.includes('2.0') ||
        titleLower.includes('stereo')
    ) {
        return { text: "🔊 Browser Stereo", className: "badge-audio-web", isWebCompatible: true };
    }
    
    return { text: "🔊 Web Audio", className: "badge-audio-unknown", isWebCompatible: true };
}
```

### Android TV Intent Routing Launch
```javascript
function launchExternalPlayer(playerType) {
    const linkData = pendingLinkData;
    const url = linkData ? linkData.url : activeStreamUrl;
    const title = selectedMedia ? selectedMedia.title : 'LeeStreamTV Stream';
    
    if (playerType === 'builtin') {
        playInBuiltinPlayer(linkData || { url, provider: 'Stream', resolution: 'HD' });
        return;
    }
    
    // Calculate resume position in milliseconds
    let resumePositionMs = 0;
    if (selectedMedia) {
        const savedProgress = getContinueWatchingItem(selectedMedia.id);
        if (savedProgress && savedProgress.currentTime > 0) {
            resumePositionMs = Math.floor(savedProgress.currentTime * 1000);
        }
    }
    
    if (hasNativeBridge()) {
        const bridge = window.LeeStreamTVBridge || window.LeePrimeBridge;
        switch (playerType) {
            case 'mx': bridge.playInMXPlayer(url, title, resumePositionMs); break;
            case 'vlc': bridge.playInVLC(url, title, resumePositionMs); break;
            case 'just': bridge.playInJustPlayer(url, title, resumePositionMs); break;
            case 'default':
            default: bridge.playInDefaultPlayer(url, title, resumePositionMs); break;
        }
        return;
    }
    
    // Intent URL scheme fallback for direct WebView redirections
    const cleanUrl = url.replace(/^https?:\/\//i, '');
    const scheme = url.startsWith('https') ? 'https' : 'http';
    let intentUrl = '';
    
    switch (playerType) {
        case 'mx':
            intentUrl = `intent://${cleanUrl}#Intent;scheme=${scheme};type=video/*;package=com.mxtech.videoplayer.ad;S.title=${encodeURIComponent(title)};end`;
            break;
        case 'vlc':
            intentUrl = `intent://${cleanUrl}#Intent;scheme=${scheme};type=video/*;package=org.videolan.vlc;S.title=${encodeURIComponent(title)};end`;
            break;
        case 'just':
            intentUrl = `intent://${cleanUrl}#Intent;scheme=${scheme};type=video/*;package=com.brouken.player;S.title=${encodeURIComponent(title)};end`;
            break;
        default:
            intentUrl = `intent://${cleanUrl}#Intent;scheme=${scheme};type=video/*;S.title=${encodeURIComponent(title)};end`;
            break;
    }
    window.location.href = intentUrl;
}
```

Please output a complete, production-ready, beautiful standalone HTML5 page matching the description. Focus on designing an extraordinary new UI while keeping every part of the spatial navigation engine, CORS handlers, parallel scraper interfaces, debrid APIs, IPTV parsers, and WebView intents perfectly functioning.
```

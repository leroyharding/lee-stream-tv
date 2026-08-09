# 🚀 Real-Debrid & Torrentio Stream Resolution Recipe (AI Generation Prompt)

Copy the prompt below to instruct any coding AI to implement a robust, premium-grade Real-Debrid and Torrentio scraping engine in your target application.

---

```markdown
You are an expert software engineer specializing in media streaming architectures. Build a robust, highly performant stream-scraping and caching engine using **Torrentio** (P2P resolver) and **Real-Debrid** (Premium link cache). The engine must run natively in the frontend (supporting CORS fallbacks) or backend, and include premium user diagnostics.

### 1. Architectural Goal
Implement a stream-gathering system that takes a media item (Movie or TV Show episode), resolves its IMDb identifier, queries Torrentio's indexer network with Real-Debrid premium configuration segments, and renders sorted resolved streams ready for video playback.

---

### 2. Real-Debrid Integration Requirements

#### A. Dual-Authentication Flow
The system must support two secure methods for retrieving and verifying the user's Real-Debrid account status:

1. **OAuth 2.0 Device Code Authorization (Standard App pairing)**:
   * **Endpoint 1 (Get Device Code)**: `GET https://api.real-debrid.com/oauth/v2/device/code?client_id=CEZWNFZ6BSSMK`
     * *Response*: Contains `code` (8-character user code), `device_code` (polling identifier), `interval` (polling interval in seconds), and `expires_in`.
   * **Endpoint 2 (Poll Credentials)**: `GET https://api.real-debrid.com/oauth/v2/device/credentials?client_id=CEZWNFZ6BSSMK&code={device_code}`
     * *Interval*: Poll every `{interval}` seconds.
     * *Response*: If approved, returns `client_id` and `client_secret`.
   * **Endpoint 3 (Token Exchange)**: `POST https://api.real-debrid.com/oauth/v2/token`
     * *Body (FormData)*: `client_id`, `client_secret`, `code` (device_code), `grant_type=http://oauth.net/grant_type/device/1.0`
     * *Response*: Returns `access_token` (active for 7 days).

2. **Private API Token Entry (Recommended for stable Torrentio integration)**:
   * Provide a manual password-hidden text input allowing users to paste their static developer token from `https://real-debrid.com/apitoken`.
   * Explain to users in the UI that Torrentio runs on remote servers and will often fail with OAuth device codes due to IP-locking policies, making the static API token the most stable choice.

#### B. Profile Verification & Premium Status Diagnostics
Verify every token (OAuth or API Key) by fetching the Real-Debrid user profile:
* **Endpoint**: `GET https://api.real-debrid.com/rest/1.0/user?auth_token={token}`
  * *Note*: Pass the token as a query parameter (`auth_token=...`) instead of an HTTP header to bypass browser CORS preflight (OPTIONS) handshakes.
* **Fields to parse & store**:
  * `username` (Account identifier)
  * `type` (Value is `"premium"` or `"free"`)
  * `premium` (Seconds remaining as premium. Calculate days remaining: `Math.ceil(premium / 86400)`)
* **UI Status Handler**:
  * If the user is premium with active days, display a green success state showing `Premium: X Days Active`.
  * If the user has a free account or `premium <= 0`, display a prominent warning banner explaining that Real-Debrid cached torrents will fail to resolve and prompt them to renew their plan.

---

### 3. Torrentio Scraping Engine

#### A. Query Construction
To fetch stream files, formulate the Torrentio query parameters dynamically:
* **Movie ID Format**: `{imdbId}` (e.g. `tt0111161`)
* **TV Show Episode Format**: `{imdbId}:{season}:{episode}` (e.g. `tt0944947:1:1` for Game of Thrones S1E1)
* **Configuration Segment**:
  * If Real-Debrid token is active:
    `providers=yts,eztv,rarbg,1337x,thepiratebay,torrentgalaxy|realdebrid={rdToken}`
  * If no debrid token:
    `providers=yts,eztv,rarbg,1337x,thepiratebay,torrentgalaxy`
* **Scraper Request URL**:
  `GET https://torrentio.strem.fun/{configSegment}/stream/{movie|series}/{queryId}.json`

#### B. Response Stream Parsing
Parse the resulting Torrentio stream list into structured stream objects:
* Resolve quality/resolution badges from the stream title/name: `4K PREMIUM`, `1080p PREMIUM`, `720p PREMIUM`, `SD`, or P2P/Magnet falls.
* Parse metadata indicators: File size (`💾 14.2 GB`), Seeds/Peers count (`👤 128`), and indexer source/provider.
* **Stream Sorting Algorithm**:
  1. Primary: Prioritize Premium Cached Links (`rd` type) over P2P/Magnet links.
  2. Secondary: Sort by Resolution quality (`4K` > `1080p` > `720p` > `SD`).
  3. Tertiary: Sort by File size or Seeds availability.

---

### 4. CORS Network Defense (Frontend Environments)
If executing requests directly from a client browser:
* Implement an asynchronous `fetchWithCORS(url, options)` handler.
* First attempt a direct fetch. On failure (e.g., origin checks or block), cascade through a list of public transparent CORS proxies (such as `corsproxy.io` and `allorigins.win`) using fully URL-encoded endpoints to guarantee 100% request success without complex server setups.

---

### 5. Local State & Auto-Validation
* Save credentials (`access_token`, `username`, `user_type`, `premium_seconds`) inside a persistent storage adapter (`localStorage` or config file).
* On application load, asynchronously trigger a silent check to verify token status. If the profile API returns a `401 Unauthorized` status, automatically purge local credentials, update the state, and alert the user that their session has expired.
```

---

## 🛠️ How to use this file
1. **Copy the code block above** in its entirety.
2. **Paste it directly into any advanced coding AI** (along with details on the language/framework you are using, e.g., *React/TypeScript*, *Electron*, *Flutter/Dart*, or *Node.js*).
3. The AI will generate a complete, premium-grade scraping screen matching this exact professional implementation.

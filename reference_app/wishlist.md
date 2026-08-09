# LeePrime Feature Wishlist 🎬

A curated list of features and enhancements that would elevate the LeePrime streaming experience. Organized by priority and category.

---

## 🔥 High Priority — Core Experience

### 1. Watchlist / My List
- Let users save movies and shows to a personal "My List" watchlist.
- Persist using `localStorage` or sync via a lightweight cloud backend (Firebase/Supabase).
- Add a heart/bookmark icon on every media card and inside the details modal.
- Dedicated sidebar tab for quick access to saved titles.

### 2. Continue Watching (Resume Playback) [COMPLETED]
- Track playback progress for each title (timestamp + duration).
- Display a progress bar overlay on media cards showing how much has been watched.
- Add a "Continue Watching" shelf row at the top of the home screen.
- On play, offer to resume from where the user left off or restart.

### 3. Watch History
- Automatically log every title the user has played.
- Display a "Recently Watched" section or dedicated history tab.
- Include timestamps (e.g., "Watched 2 hours ago") and allow clearing individual entries or the full history.

### 4. TV Show Episode Tracker
- For TV series, track which episodes have been watched per season.
- Visually mark watched episodes with a checkmark or dimmed overlay.
- Auto-suggest the next unwatched episode ("Up Next: S2E04").

### 5. Subtitle Support
- Integrate OpenSubtitles API to fetch subtitle files (.srt/.vtt) for the current title.
- Allow language selection from a dropdown before or during playback.
- For the built-in HTML5 player, render subtitles using a `<track>` element.
- For external players, pass subtitle file URI as an intent extra.

---

## ⭐ Medium Priority — User Experience Polish

### 6. Trakt.tv Integration
- Sync watched history, watchlists, and ratings with Trakt.tv accounts.
- Auto-scrobble: mark content as watched on Trakt when playback completes.
- Import existing Trakt watchlists into LeePrime on first sync.

### 7. Multiple User Profiles
- Support 2–5 user profiles per device (like Netflix).
- Each profile gets its own watchlist, history, continue watching, and preferences.
- Profile selection screen on app launch with avatar icons.

### 8. Smart Recommendations Engine
- "Because you watched X" shelves based on genre/actor/director matching from watch history.
- Trending + Popular sections already exist; add "Recommended For You" using TMDB's recommendation API endpoint.

### 9. Favourites by Category
- Let users favourite specific actors, directors, or genres.
- Generate dynamic shelves like "New from [Favourite Actor]" or "Latest in [Favourite Genre]".

### 10. Search Improvements
- Add voice search support (especially useful on Firestick remotes with voice buttons).
- Show search suggestions/autocomplete as the user types.
- Filter search results by type (Movies / TV Shows / Live TV).
- Recent search history with quick-tap pills.

### 11. Content Ratings & Parental Controls
- Display MPAA/certification ratings (PG, R, 18+) on media cards and details.
- Optional PIN-protected parental lock to restrict mature content.
- Kid-safe mode that filters to family-friendly titles only.

---

## 🎨 Visual & UI Enhancements

### 12. Auto-Playing Hero Banner
- A large, Netflix-style auto-rotating hero banner at the top of the home screen.
- Showcase trending or featured titles with backdrop images, synopsis snippets, and "Play" / "More Info" buttons.
- Smooth crossfade transitions every 8–10 seconds.

### 13. Trailer Playback [COMPLETED]
- Fetch YouTube trailer keys from TMDB and embed a trailer player inside the details modal.
- "Watch Trailer" button with a cinematic play icon.

### 14. Cast & Crew Deep Links
- Tapping an actor/director name in the details modal opens a filtered view showing all their other movies/shows available via TMDB.

### 15. Dark/Light/AMOLED Theme Toggle
- Currently dark-only. Add a true AMOLED black mode (pure `#000` backgrounds for OLED screens to save battery).
- Optional light theme for daytime/tablet use.

### 16. Custom Accent Colour Picker
- Allow users to change the primary accent from orange to their preferred colour (blue, red, green, purple, etc.).
- Store preference in `localStorage` and apply via CSS custom properties.

### 17. Grid vs List View Toggle [COMPLETED]
- Let users switch between the current poster grid layout and a compact list view with more metadata visible per row.

---

## 🔧 Technical & Performance

### 18. Offline Cache / Download Manager
- Allow users to cache resolved stream URLs for offline playback (where legally permitted).
- Download progress indicator with pause/resume support.
- Dedicated "Downloads" tab in the sidebar.

### 19. Background Playback (Picture-in-Picture)
- Support PiP mode so users can browse other content while video plays in a floating window.
- Implement using the Android PiP API via a Cordova plugin.

### 20. Stream Auto-Selection
- Instead of showing the full resolved links list, offer a "Quick Play" button that automatically picks the best available link based on rules:
  - Prefer highest resolution (4K > 1080p > 720p).
  - Prefer smallest file size at same resolution.
  - Prefer direct HTTP over torrent sources.
  - Skip sources flagged with audio codec warnings.

### 21. Resolver Source Manager
- Settings panel where users can enable/disable individual scraper sources (Torrentio, NoTorrent, StreamViX, HdHub, etc.).
- Drag-to-reorder priority of sources.
- Show health/status indicators for each source (online/offline/slow).

### 22. AllDebrid / Premiumize Support
- Expand beyond Real-Debrid to support other premium link resolvers.
- Unified debrid settings page with account status for each service.

### 23. App Update Checker
- On launch, check a remote JSON endpoint for the latest version number.
- If a newer version exists, show a non-intrusive notification banner with a download link.
- In-app changelog viewer.

---

## 📺 TV-Specific Enhancements

### 24. Screensaver / Ambient Mode
- After 5 minutes of idle, display a beautiful screensaver showing random backdrop images from trending movies with slow ken-burns pan effects.
- Dismiss on any remote button press.

### 25. Quick Settings Sidebar
- Long-press the menu button to open a quick settings drawer.
- Toggle options: subtitle language, preferred resolution, audio filter, debrid status, and theme.

### 26. Channel/Source Zapping for Live TV
- Channel up/down buttons on remote cycle through live TV channels without returning to the channel grid.
- Mini overlay showing current/next channel info during zapping.

### 27. Voice Command Integration
- "Play Breaking Bad Season 3" → auto-search, find, and begin playback.
- Leverage Android's speech recognition API via Cordova plugin.

---

## 🌐 Social & Community

### 28. Ratings & Reviews
- Show TMDB/IMDb ratings on media cards.
- Let users rate titles (thumbs up/down or 5-star) and optionally sync to Trakt.

### 29. Share Content
- "Share" button in the details modal to send a deep link or title info via messaging apps, social media, or clipboard.

### 30. What's New / Changelog Screen
- A dedicated section showing recently added features, bug fixes, and version history.
- Display once after each app update.

---

## 📋 Implementation Notes

| Feature | Complexity | Dependencies |
|---------|-----------|-------------|
| Watchlist | Low | localStorage |
| Continue Watching | Medium | localStorage + player events |
| Subtitle Support | Medium | OpenSubtitles API |
| Trakt Integration | High | OAuth + Trakt API |
| Multiple Profiles | High | localStorage or backend DB |
| Voice Search | Medium | Cordova speech plugin |
| Stream Auto-Select | Low | Existing resolver logic |
| PiP Mode | Medium | Cordova PiP plugin |
| Auto-Update Checker | Low | Remote JSON endpoint |
| Screensaver | Low | CSS animations + idle timer |
| Streaming Services Catalog | Medium | Stremio Catalog Add-on API |

---

## 🌐 Catalog & Add-ons

### 31. Streaming Services Catalog Add-on [COMPLETED]
- Connects directly to the Stremio Streaming Services Catalog Add-on API.
- Integrated Netflix, HBO Max, Disney+, Prime Video, and Apple TV+ catalogs.
- Custom grid and list layout rendering with full TV remote control support and brand-specific glows.

---

> **Tip:** Start with features 1–5 (Watchlist, Continue Watching, History, Episode Tracker, Subtitles) — these are the highest-impact improvements that users expect from any modern streaming app.

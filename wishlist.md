# 🎬 LeeStreamTV — Feature Wishlist

This file tracks potential future features and improvements for **LeeStreamTV**. Features are grouped by theme and tagged with an estimated complexity level.

> **Legend**: 🟢 Easy &nbsp;|&nbsp; 🟡 Medium &nbsp;|&nbsp; 🔴 Hard

---

## 🎯 Content Discovery

| # | Feature | Description | Priority | Effort |
|---|---------|-------------|----------|--------|
| 1 | **Filter by Rating** | Add a minimum TMDB score selector (e.g. 7+, 8+, 9+) next to the year filter dropdown | High | 🟢 |
| 2 | **Filter by Language** | Add a language dropdown to discover foreign language films and shows (e.g. Korean, Spanish, Japanese) | Medium | 🟢 |
| 3 | **Sort By** | Allow sorting catalog results by: Popularity, Release Date, Rating, Title A-Z | High | 🟡 |
| 4 | **"New This Week" Shelf** | A dedicated section showing titles added to TMDB in the last 7 days | Medium | 🟡 |
| 5 | **Trending on Social Media** | Pull trending titles from X/Twitter or Reddit discussions using a social data API | Low | 🔴 |
| 6 | **TMDB Collections** | (Completed) Allow users to browse movie collections (e.g. Marvel, Fast & Furious, Mission Impossible) | Medium | 🟡 |
| 7 | **"Hidden Gems" Shelf** | A shelf that specifically surfaces highly rated but less popular titles (low vote count, high score) | Medium | 🟡 |

---

## 📋 Watchlist & Viewing Tracking

| # | Feature | Description | Priority | Effort |
|---|---------|-------------|----------|--------|
| 8 | **Continue Watching** | (Completed) Remember where you left off in a movie or episode, and display a progress bar on the card | High | 🔴 |
| 9 | **Watched History Tab** | A dedicated history tab showing recently played titles with timestamps | Medium | 🟡 |
| 10 | **"Mark as Watched"** | Allow marking individual titles on the watchlist as watched with a green checkmark badge | High | 🟢 |
| 11 | **Watchlist Folders / Tags** | Organise watchlist into named groups (e.g. "Date Night", "Kids", "Anime") | Low | 🟡 |
| 12 | **Export Watchlist** | Export your watchlist as a JSON or CSV file for backup or sharing | Low | 🟢 |
| 13 | **Import from Trakt.tv / Letterboxd** | Sync or import a watchlist from popular movie tracking apps | Low | 🔴 |

---

## 🎭 Movie & Show Detail Improvements

| # | Feature | Description | Priority | Effort |
|---|---------|-------------|----------|--------|
| 14 | **Watch Trailer** | (Completed) Embed a YouTube trailer button in the movie detail panel using TMDB's video endpoint | High | 🟡 |
| 15 | **Cast & Crew Section** | Show the top cast list with headshots and character names inside the detail modal | Medium | 🟡 |
| 16 | **Episode Guide for TV Shows** | (Completed) Full season/episode browser with air dates, episode synopses, and season artwork | High | 🔴 |
| 17 | **Similar Titles Row** | Horizontal scroll row of TMDB-recommended similar titles at the bottom of the details panel (already scaffolded!) | High | 🟢 |
| 18 | **TMDB User Reviews** | Show top community reviews from TMDB's review endpoint inside the details panel | Low | 🟡 |
| 19 | **Where to Watch (Legitimately)** | Show official streaming provider availability badges (Netflix, Prime etc.) inside the detail panel | Medium | 🟡 |

---

## 📺 Live TV Improvements

| # | Feature | Description | Priority | Effort |
|---|---------|-------------|----------|--------|
| 20 | **Saved Playlists** | Allow saving and naming multiple IPTV playlist URLs that persist across sessions | High | 🟢 |
| 21 | **Favourite Channels** | Star/pin favourite channels to appear at the top of the channel list | Medium | 🟢 |
| 22 | **EPG Timeline View** | Full 7-day programme guide timeline view similar to a TV guide grid layout | Medium | 🔴 |
| 23 | **Channel Logos** | Auto-resolve and display provider channel logos from TVG data in the M3U playlist | Low | 🟡 |
| 24 | **PIP (Picture in Picture)** | Minimise live TV to a corner PIP overlay while browsing the catalog | Low | 🔴 |

---

## 🎛️ Player & Streaming

| # | Feature | Description | Priority | Effort |
|---|---------|-------------|----------|--------|
| 25 | **Subtitle Selection** | When playing via the built-in player, allow selecting subtitle tracks from the stream | High | 🔴 |
| 26 | **Audio Track Selection** | Switch audio language tracks when multiple are available in a stream | Medium | 🔴 |
| 27 | **Playback Speed Control** | Speed up or slow down playback (0.5x, 1x, 1.25x, 1.5x, 2x) | Low | 🟡 |
| 28 | **Stream Quality Selector** | Allow manually selecting 480p / 720p / 1080p / 4K when multiple quality sources are available | Medium | 🟡 |
| 29 | **Torrentio / Orion Integration** | Pull additional stream sources from Torrentio or Orion addons for wider content coverage | High | 🔴 |

---

## 🔗 Real-Debrid & Premium

| # | Feature | Description | Priority | Effort |
|---|---------|-------------|----------|--------|
| 30 | **Auto-Refresh Token** | Automatically refresh the RD OAuth access token using the refresh_token before it expires | High | 🟡 |
| 31 | **RD Downloads Manager** | Browse your Real-Debrid cloud downloads and active transfers directly inside the app | Medium | 🔴 |
| 32 | **RD Restricted Link Resolver** | Let users paste any torrent or hosters link and resolve it to a direct stream URL via RD | Medium | 🟡 |
| 33 | **All-Debrid / Premiumize Support** | Add alternative premium link resolver support for other debrid services | Low | 🔴 |

---

## 🎨 UI & Navigation

| # | Feature | Description | Priority | Effort |
|---|---------|-------------|----------|--------|
| 34 | **Dark / Light Theme Toggle** | Offer a light mode or alternative colour theme option in settings | Low | 🟡 |
| 35 | **Accent Color Picker** | Let users customise the orange accent colour with a few preset palettes (blue, purple, teal) | Low | 🟢 |
| 36 | **Animated Hero Banner** | A full-width featured/hero banner at the top of the catalog that rotates trending titles with backdrop art | Medium | 🟡 |
| 37 | **Poster Hover Preview Card** | When hovering over a movie card, show a glassmorphism popup with rating, genre tags, and overview text | High | 🟡 |
| 38 | **Remote Shortcut Map** | A help overlay accessible from settings that shows all D-pad keyboard shortcuts | Medium | 🟢 |
| 39 | **Voice Search** | Add microphone voice input for searching content using the Web Speech API | Medium | 🟡 |
| 40 | **Screensaver Mode** | After 5 minutes idle, show a beautiful slideshow of movie backdrops as a screensaver | Low | 🟡 |

---

## ⚙️ Settings & Personalization

| # | Feature | Description | Priority | Effort |
|---|---------|-------------|----------|--------|
| 41 | **Content Region Selector** | Let users set their country for localised TMDB watch provider results and trending content | Medium | 🟢 |
| 42 | **Parental Controls / Content Rating Filter** | Filter out content above a certain MPAA/BBFC rating (e.g. max PG-13) | Medium | 🟡 |
| 43 | **Notifications** | Background reminders for new episodes of shows on your watchlist | Low | 🔴 |
| 44 | **Cloud Sync / Backup** | Sync watchlist and settings to the cloud (e.g. via GitHub Gist, Firebase, or a custom endpoint) | Medium | 🔴 |
| 45 | **Privacy Mode** | A toggle to clear watch history/logs without affecting other settings | Low | 🟢 |

---

## 📦 Housekeeping & Infrastructure

| # | Feature | Description | Priority | Effort |
|---|---------|-------------|----------|--------|
| 46 | **Release Notes Modal** | After updating, show a "What's New" modal with the latest changelog automatically | Medium | 🟢 |
| 47 | **Performance Profiling** | Add optional debug panel showing load times, API response speeds, and cache hit rates | Low | 🟡 |
| 48 | **Error Reporting** | Graceful error boundaries that show a friendly "Something went wrong" message and retry option | High | 🟡 |
| 49 | **Offline Mode** | Fully functional watchlist and settings access when there's no internet connection | Medium | 🟡 |
| 50 | **Multiple User Profiles** | Support for multiple user profiles on the same device with separate watchlists and preferences | Low | 🔴 |

---

## 🚀 Advanced Next-Gen Features

| # | Feature | Description | Priority | Effort |
|---|---------|-------------|----------|--------|
| 51 | **Dedicated Provider Shelves** | (Completed) Dynamic TMDB shelves for specific networks like Netflix, Apple TV+, and HBO Max | High | 🟢 |
| 52 | **Anime Integration** | Dedicated Anime tab pulling from AniList/Kitsu with Nyaa.si torrent integration | Medium | 🔴 |
| 53 | **Trakt.tv Community Lists** | Pull dynamic Trakt catalogs (e.g., "Most Anticipated Movies", "IMDb Top 250") | High | 🟡 |
| 54 | **Award Winners Catalog** | Dynamic catalog showing only Oscar/Emmy-winning movies and shows | Medium | 🟢 |
| 55 | **Stremio Addon Parser** | Parse standard Stremio Addon URLs to dynamically add indexers without hardcoding | High | 🔴 |
| 56 | **Live Sports Schedule** | Integrate a sports API to show live games and cross-reference with IPTV channels | Medium | 🔴 |

---

## 📚 Planned Local Collections

The following popular movie franchises have been identified as great candidates for the `localCollections` array to expand the default discovery feed.

### 🍿 Sci-Fi & Fantasy
- **Star Wars Collection** (`tmdb:col:10`)
- **Harry Potter Collection** (`tmdb:col:1241`)
- **Jurassic Park Collection** (`tmdb:col:328`)
- **The Hunger Games Collection** (`tmdb:col:131635`)

### 💥 Action & Superheroes
- **The Avengers Collection** (`tmdb:col:86311`)
- **Mission: Impossible Collection** (`tmdb:col:87359`)
- **Fast & Furious Collection** (`tmdb:col:9485`)
- **James Bond (Daniel Craig) Collection** (`tmdb:col:645`)
- **Spider-Man (Tom Holland) Collection** (`tmdb:col:531241`)

### 🗡️ Adventure & Classics
- **Indiana Jones Collection** (`tmdb:col:84`)
- **Pirates of the Caribbean Collection** (`tmdb:col:295`)
- **The Terminator Collection** (`tmdb:col:528`)

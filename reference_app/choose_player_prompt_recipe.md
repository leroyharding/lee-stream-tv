# Prompt: Premium TV-Optimized External Player Selection Modal for Cordova/Android WebView

Copy and paste the prompt below into your AI coding assistant to generate or integrate the "Choose Player" modal feature into your streaming application.

---

```markdown
Build a premium, TV-optimized "Choose Player" modal selection dialog for a single-page HTML5/CSS/JavaScript streaming application packaged inside a Cordova Android wrapper. The modal should allow the user to select their preferred player for streaming media links, persist their choice if desired, and launch external Android media players using intents/URI schemes.

Here are the requirements:

### 1. Visual Design & Theme (Premium Dark Glassmorphism)
- **Overlay Container**: Fullscreen dark overlay (`background: rgba(0, 0, 0, 0.85)`) with a blur filter (`backdrop-filter: blur(10px)`). 
- **Modal Card**: A centered card with rounded corners (`border-radius: 20px`), a subtle white border (`border: 1px solid rgba(255, 255, 255, 0.08)`), and a translucent dark background (`rgba(20, 20, 28, 0.65)`).
- **Typography**: Clean sans-serif hierarchy (e.g., Inter or Roboto). Subtitle should show the currently selected stream name and details in muted gray.
- **Close Button**: A circular 'X' close button at the top right of the card.
- **Row Styling**: 
  - Each player choice should be a wide card/button row containing:
    - A colored square icon container (e.g., orange for MX, reddish-orange for VLC, purple for Just Player, teal for System Default).
    - Bold player name and a smaller description line.
  - Hover/Focus state: When focused (using a `.focused-tv` class or CSS hover), scale the card slightly (`transform: translateY(-2px)`), increase background brightness, and add an accent outline or strong box-shadow.

### 2. Player Options & Launch Logic
Include the following options and implement the launch logic using Cordova Intents (e.g., `cordova-plugin-webintent` or URL schemes):
- **MX Player**: 
  - Description: "Best for Firestick — supports all codecs & audio"
  - Android Intent: Package `com.mxtech.videoplayer.ad` or `com.mxtech.videoplayer.pro`. Pass the video URL, mime-type (e.g., `video/*`), and header extras if required.
- **VLC Player**:
  - Description: "Open source — plays virtually anything"
  - Android Intent: Package `org.videolan.vlc`.
- **Just Player**:
  - Description: "Lightweight & fast with hardware decoding"
  - Android Intent: Package `com.brouken.player`.
- **System Default**:
  - Description: "Let Android choose the best installed player"
  - Action: Generic `ACTION_VIEW` intent with mime-type `video/*` to trigger the system's "Open With..." chooser.
- **Built-in Player**:
  - Description: "WebView HTML5 — limited codec support on TV"
  - Action: Play the video locally inside the app's HTML5 `<video>` player overlay.

### 3. Logic & State Management
- **Remember My Choice Toggle**:
  - Implement a toggle switch/checkbox at the bottom labeled "Remember my choice".
  - If checked when a player is selected, save the player choice to `localStorage` (e.g., `saved_player_preference`).
  - On subsequent playback clicks, bypass this modal completely and launch the saved preferred player.
  - Provide a settings menu reset option to clear `localStorage.removeItem('saved_player_preference')` so users can change their choice later.
- **Back Button & Close Actions**:
  - The modal must close if the user clicks the 'X' button or presses the hardware/remote 'Back' button (or `Backspace`/`Escape`).

### 4. TV D-Pad Spatial Navigation Compatibility
- Each choice button and the "Remember my choice" toggle must have a shared class (e.g., `.btn-player-option` or `focusable`) so that a custom TV Remote spatial D-pad engine can navigate up and down through the list and toggle the switch.
- When the modal opens, automatically set spatial focus to the first player button.
- Lock focus movement inside the bounds of the modal while it is open.
```

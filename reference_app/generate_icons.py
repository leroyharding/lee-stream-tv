"""Generate Android mipmap icons from the source LeePrime icon image."""
import os
from PIL import Image

script_dir = os.path.dirname(os.path.abspath(__file__))
SOURCE_ICON = r"C:\Users\leroy\.gemini\antigravity\brain\f5f48163-0ec0-4e8c-ae30-0e4065dcca74\new_icon_1779226461619.png"
RES_DIR = os.path.join(script_dir, "cordova_project", "platforms", "android", "app", "src", "main", "res")

# Android standard launcher icon sizes per density bucket
DENSITIES = {
    "mipmap-ldpi": 36,
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192,
}

# Adaptive icon foreground/background sizes (v26) - 108dp * density
ADAPTIVE_DENSITIES = {
    "mipmap-ldpi-v26": 162,
    "mipmap-mdpi-v26": 216,
    "mipmap-hdpi-v26": 324,
    "mipmap-xhdpi-v26": 432,
    "mipmap-xxhdpi-v26": 648,
    "mipmap-xxxhdpi-v26": 864,
}

def main():
    img = Image.open(SOURCE_ICON).convert("RGBA")
    print(f"Source icon loaded: {img.size}")

    # Generate standard launcher icons
    for density, size in DENSITIES.items():
        out_dir = os.path.join(RES_DIR, density)
        os.makedirs(out_dir, exist_ok=True)
        resized = img.resize((size, size), Image.LANCZOS)
        out_path = os.path.join(out_dir, "ic_launcher.png")
        resized.save(out_path, "PNG")
        print(f"  {density}/ic_launcher.png -> {size}x{size}")

    # Generate adaptive icon foreground images
    for density, size in ADAPTIVE_DENSITIES.items():
        out_dir = os.path.join(RES_DIR, density)
        os.makedirs(out_dir, exist_ok=True)
        resized = img.resize((size, size), Image.LANCZOS)
        out_path = os.path.join(out_dir, "ic_launcher_foreground.png")
        resized.save(out_path, "PNG")
        print(f"  {density}/ic_launcher_foreground.png -> {size}x{size}")

        # Dark background for adaptive icon
        bg = Image.new("RGBA", (size, size), (10, 10, 12, 255))
        bg_path = os.path.join(out_dir, "ic_launcher_background.png")
        bg.save(bg_path, "PNG")
        print(f"  {density}/ic_launcher_background.png -> {size}x{size}")

        # Monochrome version (grayscale)
        mono = resized.convert("L").convert("RGBA")
        mono_path = os.path.join(out_dir, "ic_launcher_monochrome.png")
        mono.save(mono_path, "PNG")
        print(f"  {density}/ic_launcher_monochrome.png -> {size}x{size}")

    print("\nAll icons generated successfully!")

if __name__ == "__main__":
    main()

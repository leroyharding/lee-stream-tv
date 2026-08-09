import os

def main():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    master_path = os.path.join(script_dir, "cinema_hd_web_portal.html")
    target_path = os.path.join(script_dir, "cordova_project", "www", "index.html")
    
    if not os.path.exists(master_path):
        print(f"Error: Master file not found at {master_path}")
        return
        
    with open(master_path, 'r', encoding='utf-8') as f:
        content = f.read()
        
    lucide_tag = '<script src="https://unpkg.com/lucide@latest"></script>'
    cordova_tag = '\n\n    <!-- Cordova Platform Bridge -->\n    <script src="cordova.js"></script>'
    
    if lucide_tag in content:
        # Insert cordova script right after lucide script
        content = content.replace(lucide_tag, lucide_tag + cordova_tag)
        print("Injected Cordova bridge script after Lucide tag.")
    else:
        # Fallback to before </head>
        content = content.replace('</head>', cordova_tag + '\n</head>')
        print("Fallback: Injected Cordova bridge script before </head>.")
        
    import shutil
    mp4_source = os.path.join(script_dir, "LeePrimeTV2.mp4")
    mp4_target = os.path.join(script_dir, "cordova_project", "www", "LeePrimeTV2.mp4")
    if os.path.exists(mp4_source):
        shutil.copy2(mp4_source, mp4_target)
        print(f"Successfully copied video {mp4_target}")

    with open(target_path, 'w', encoding='utf-8') as f:
        f.write(content)
        
    print(f"Successfully synchronized master HTML to {target_path}")

if __name__ == '__main__':
    main()

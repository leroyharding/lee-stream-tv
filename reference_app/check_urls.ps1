$urls = @(
"https://image.tmdb.org/t/p/w500/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg",
"https://image.tmdb.org/t/p/w500/39wmItIWsg5sZMyRUHLkBg8lWOb.jpg",
"https://image.tmdb.org/t/p/w500/1Q5Xz930S5Iu4T2nZkX5t4aPqM0.jpg",
"https://image.tmdb.org/t/p/w500/pgqgaUx1cJb5oZQQ5v0tNARCeBp.jpg",
"https://image.tmdb.org/t/p/w500/8kSHqQ5kP0N0ZcInUfR1630Hn1n.jpg",
"https://image.tmdb.org/t/p/w500/jRXYjXNq0Cs2TcJjLkki24MLp7u.jpg",
"https://image.tmdb.org/t/p/w500/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
"https://image.tmdb.org/t/p/w500/gajva2L0rPYkEWjzgFlBXCAVBE5.jpg",
"https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s53Jheo8WeP8.jpg",
"https://image.tmdb.org/t/p/w500/1XyqA4vEukTIfc7iLOr7d2v26Xp.jpg"
)
foreach ($url in $urls) {
    try {
        $res = Invoke-WebRequest -Uri $url -Method Head -UseBasicParsing
        Write-Host "$url : $($res.StatusCode)"
    } catch {
        Write-Host "$url : Error"
    }
}

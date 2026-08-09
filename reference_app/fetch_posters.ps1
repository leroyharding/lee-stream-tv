$apiKey = '8711e2c6b0504a3277a840e1dde5ed86'
$baseUrl = 'https://api.themoviedb.org/3'

$collections = @(
    @{ id = "mcu"; type = "discover_movie"; endpoint = "/discover/movie?with_keywords=180547&sort_by=release_date.asc" },
    @{ id = "dc"; type = "discover_movie"; endpoint = "/discover/movie?with_keywords=349&sort_by=release_date.asc" },
    @{ id = "star-wars"; type = "tmdb_collection"; endpoint = "/collection/10?" },
    @{ id = "james-bond"; type = "tmdb_collection"; endpoint = "/collection/645?" },
    @{ id = "harry-potter"; type = "tmdb_collection"; endpoint = "/collection/1241?" },
    @{ id = "lord-of-rings"; type = "tmdb_collection"; endpoint = "/collection/119?" },
    @{ id = "hobbit"; type = "tmdb_collection"; endpoint = "/collection/121938?" },
    @{ id = "fast-furious"; type = "tmdb_collection"; endpoint = "/collection/9485?" },
    @{ id = "mission-impossible"; type = "tmdb_collection"; endpoint = "/collection/87359?" },
    @{ id = "john-wick"; type = "tmdb_collection"; endpoint = "/collection/404609?" },
    @{ id = "matrix"; type = "tmdb_collection"; endpoint = "/collection/2344?" },
    @{ id = "jurassic-park"; type = "tmdb_collection"; endpoint = "/collection/328?" },
    @{ id = "pirates-caribbean"; type = "tmdb_collection"; endpoint = "/collection/295?" },
    @{ id = "indiana-jones"; type = "tmdb_collection"; endpoint = "/collection/84?" },
    @{ id = "back-to-future"; type = "tmdb_collection"; endpoint = "/collection/264?" },
    @{ id = "terminator"; type = "tmdb_collection"; endpoint = "/collection/534?" },
    @{ id = "alien"; type = "tmdb_collection"; endpoint = "/collection/8091?" },
    @{ id = "predator"; type = "tmdb_collection"; endpoint = "/collection/399?" },
    @{ id = "rocky"; type = "tmdb_collection"; endpoint = "/collection/1575?" },
    @{ id = "nolan"; type = "person_movies"; endpoint = "/person/525/movie_credits?" },
    @{ id = "tarantino"; type = "person_movies"; endpoint = "/person/138/movie_credits?" },
    @{ id = "scorsese"; type = "person_movies"; endpoint = "/person/1032/movie_credits?" },
    @{ id = "oscar-winners"; type = "discover_movie"; endpoint = "/discover/movie?with_keywords=10704&sort_by=vote_average.desc&vote_count.gte=500" },
    @{ id = "top-tv"; type = "discover_tv"; endpoint = "/discover/tv?sort_by=vote_average.desc&vote_count.gte=1000" }
)

$results = @{}

foreach ($col in $collections) {
    $url = "$baseUrl$($col.endpoint)&api_key=$apiKey"
    try {
        $res = Invoke-RestMethod -Uri $url -Method Get
        $posterPath = $null
        if ($col.type -eq "tmdb_collection") {
            $posterPath = $res.poster_path
        } elseif ($col.type -eq "person_movies") {
            if ($res.cast -and $res.cast.Count -gt 0) {
                # Find a cast entry with a poster
                foreach ($c in $res.cast) {
                    if ($c.poster_path) {
                        $posterPath = $c.poster_path
                        break
                    }
                }
            }
        } else {
            # discover
            if ($res.results -and $res.results.Count -gt 0) {
                foreach ($r in $res.results) {
                    if ($r.poster_path) {
                        $posterPath = $r.poster_path
                        break
                    }
                }
            }
        }
        $results[$col.id] = $posterPath
    } catch {
        Write-Host "Failed for $($col.id)"
    }
}

$results | ConvertTo-Json

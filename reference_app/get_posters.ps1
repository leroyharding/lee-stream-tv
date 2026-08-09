$apiKey = '8711e2c6b0504a3277a840e1dde5ed86'
$queries = @("Spirited Away", "The Hunger Games", "E.T. the Extra-Terrestrial", "The Matrix", "Halloween")

foreach ($q in $queries) {
    $url = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$([uri]::EscapeDataString($q))"
    try {
        $res = Invoke-RestMethod -Uri $url -Method Get
        if ($res.results.Count -gt 0) {
            Write-Host "$q : $($res.results[0].poster_path)"
        }
    } catch {
        Write-Host "$q : Error"
    }
}

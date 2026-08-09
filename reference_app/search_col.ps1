$apiKey = '8711e2c6b0504a3277a840e1dde5ed86'
$url = "https://api.themoviedb.org/3/collection/131635?api_key=$apiKey"
try {
    $res = Invoke-RestMethod -Uri $url -Method Get
    Write-Host "Hunger games is valid"
} catch {
    Write-Host "Hunger games failed"
}

$url = "https://api.alldebrid.com/v4/pin/get?agent=LeePrimeTv"
$res = Invoke-RestMethod -Uri $url -Method Get
$res | ConvertTo-Json -Depth 10

$url = "https://api.alldebrid.com/v4/pin/check?check=804f3d08f0146252d677e5afa57bd7cab7a8f5a0&pin=2HDF&agent=LeePrimeTv"
$res = Invoke-RestMethod -Uri $url -Method Get
$res | ConvertTo-Json -Depth 10

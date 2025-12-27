Set-Location "e:\DSDrive\CodingVibe\Cursor\BaseBench-for-multiple-dbms"

$files = Get-ChildItem -Path "src/main/java" -Recurse -Filter "*.java"
$count = 0

foreach ($f in $files) {
    $path = $f.FullName
    $bytes = [System.IO.File]::ReadAllBytes($path)

    if ($bytes.Length -ge 3 -and $bytes[0] -eq 0xEF -and $bytes[1] -eq 0xBB -and $bytes[2] -eq 0xBF) {
        $newBytes = $bytes[3..($bytes.Length - 1)]
        [System.IO.File]::WriteAllBytes($path, $newBytes)
        Write-Host "Removed BOM from: $path"
        $count++
    }
}

Write-Host "Done! Removed BOM from $count files."

param(
    [Parameter(Mandatory = $true)]
    [string]$JarPath,

    [string]$OutDir = "work/mcpatchclient-deobf",

    [string]$VineflowerJar = "$env:USERPROFILE/.gradle/caches/modules-2/files-2.1/org.vineflower/vineflower/1.12.0/85570609a0a5941a7d2918b6260b209de810f66f/vineflower-1.12.0.jar"
)

$ErrorActionPreference = "Stop"

if (!(Test-Path -LiteralPath $JarPath)) {
    throw "JAR not found: $JarPath"
}

if (!(Test-Path -LiteralPath $VineflowerJar)) {
    throw "Vineflower not found: $VineflowerJar"
}

$root = Resolve-Path .
$out = Join-Path $root $OutDir
$classes = Join-Path $out "classes"
$src = Join-Path $out "src-vineflower"
$reports = Join-Path $out "reports"

New-Item -ItemType Directory -Force -Path $classes, $src, $reports | Out-Null

$hash = Get-FileHash -Algorithm SHA256 -LiteralPath $JarPath
$hash | Format-List | Out-File -Encoding UTF8 (Join-Path $reports "sha256.txt")

Push-Location $classes
try {
    & jar xf $JarPath
}
finally {
    Pop-Location
}

& java -jar $VineflowerJar $JarPath $src

$coreFiles = Get-ChildItem -Path (Join-Path $src "mcpatch") -Recurse -File -ErrorAction SilentlyContinue |
    Sort-Object FullName |
    ForEach-Object { $_.FullName.Substring($src.Length + 1).Replace("\", "/") }

$coreFiles | Out-File -Encoding UTF8 (Join-Path $reports "mcpatch-source-index.txt")

@"
# McPatchClient Deobfuscation Reproduction

Input JAR: $JarPath
SHA256: $($hash.Hash)

Generated:

- classes: $classes
- decompiled source: $src
- mcpatch source index: $(Join-Path $reports "mcpatch-source-index.txt")

Notes:

- This script performs static extraction/decompilation only.
- Do not run the target JAR on a real Minecraft installation during investigation.
- Decompiled output is generated locally and is intentionally not committed.
"@ | Out-File -Encoding UTF8 (Join-Path $reports "README.md")

Write-Host "Done."
Write-Host "SHA256: $($hash.Hash)"
Write-Host "Output: $out"

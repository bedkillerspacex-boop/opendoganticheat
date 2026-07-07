# OpenDogAntiCheat

OpenDogAntiCheat is a public static-research archive for `DogAntiCheat-Client-prod.jar`.

## Clear Finding

This client contains backdoor-style capabilities.

The evidence in this repository shows remote-control and surveillance-capable behavior, including:

- Java agent startup before normal mod initialization.
- Native DLL loading through `System.load`.
- Java module and Forge file-system patching.
- A remote-configured local TCP proxy launched through `SocketProxy`.
- POSTing the Minecraft login name plus native-generated payload data to a decrypted remote endpoint.
- Client-side resource pack, shaderpack, and `tacz` directory fingerprinting.
- Server-triggered file writes into the client's `mods` directory.
- A native DLL import surface that includes process memory access, remote thread creation, window enumeration, screenshot-capable APIs, registry access, TPM APIs, and anti-debug APIs.

This repository does not claim author intent. It documents concrete static evidence that the client has backdoor-like remote control and privacy-risk capabilities.

## What Is Included

- `src-decompiled/`: best-effort decompiled Java source, merged from CFR, Vineflower, and the provided DeepSeek output.
- `reports/AUDIT.md`: evidence-backed static audit report.
- `reports/DEEPSEEK_MERGE.md`: how the DeepSeek result was reviewed and merged.
- `reports/coverage.json`: extraction/decompilation coverage.
- `reports/dll-sha256.txt`: native DLL hashes.
- `reports/dll-imports.txt`: native DLL import table summary.
- `reports/sensitive-java-hits.txt`: Java sensitive-pattern hits.
- `reports/jar-file-list.txt`: original JAR file listing.

No native DLL, executable, JAR, or class binaries are included in this repository.

## Coverage

Original target:

- JAR `.class` files: 185
- Hidden bytecode resource: 1 (`classes/SocketProxy`)
- Total bytecode units covered: 186
- Merged restored Java files: 186
- javap fallback files in the private working bundle: 186

Target SHA256:

`EF2D0DA9AEB2DD8C53E9AD7AB7D2D4E782F2A4E8B1D5A3F5B06B1789C59452D9`

## Important Limitations

This is decompiled research source. It is not guaranteed to compile.

Most original local variable names are not recoverable from bytecode unless debug symbols, original source, or an official mapping are available. The `src-decompiled/` tree therefore preserves bytecode-aligned class and method names where needed, while restoring semantic variable names in selected high-risk areas for readability.

## Safety Boundary

This repository is for defensive research, privacy review, and malware/backdoor-style behavior analysis.

It does not provide:

- anti-cheat bypass instructions
- detection-disabling instructions
- server-check forgery
- environment spoofing
- ban evasion
- exploit guidance

Do not run the original mod on a real account or daily-use system. If dynamic analysis is necessary, use an isolated virtual machine, a disposable Minecraft profile, a separate game directory, and no primary credentials.

## Recommended User Action

Do not install this client on a primary system. Do not join servers that force installation of an unaudited native anti-cheat client with this behavior.


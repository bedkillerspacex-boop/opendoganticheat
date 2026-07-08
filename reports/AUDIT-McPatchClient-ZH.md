# McPatchClient.jar 静态反编译审计报告

## 范围

- 目标文件：`E:/download/[v1.5.1p]LesRaisins & Limitless/McPatchClient.jar`
- SHA256：`1C51C39C1D074439396756A4FB9BD7C376D109C64E6435E30595736AA7A72F45`
- 分析方式：仅静态分析，未执行该 JAR。
- 反编译输出：`work/mcpatchclient-deobf/src-vineflower`
- 解包资源：`work/mcpatchclient-deobf/classes`

## 初步定性

`McPatchClient.jar` 是一个 Kotlin/Java fat JAR，核心功能是 Minecraft 客户端更新/补丁器。它不是 native 二进制，也不是普通资源包。

该 JAR 同时声明了普通入口和 Java Agent 入口：

- `Main-Class: mcpatch.McPatchClient`
- `Premain-Class: mcpatch.McPatchClient`

这意味着它既可以作为普通程序运行，也可以通过 `-javaagent` 在 Minecraft/启动器主程序之前执行。

## 入口证据

反编译后可见：

- `mcpatch/McPatchClient.kt:41`：主运行逻辑 `run(...)`
- `mcpatch/McPatchClient.kt:510`：Java Agent 入口 `premain(agentArgs, ins)`
- `mcpatch/McPatchClient.kt:551`：普通程序入口 `main(args)`

风险点：如果被启动参数加载为 `-javaagent`，它会在主程序之前运行，用户通常不容易从游戏界面感知。

## 内置更新源

JAR 内嵌 `mc-patch-config.yml`，其中包含硬编码远端更新源：

```yaml
server: http://47.98.173.79:56600
server-versions-file-name: versions.txt
quiet-mode: false
no-throwing: false
ignore-https-certificate: false
base-path: ''
```

配置注释说明它支持三类更新源：

- HTTP / HTTPS
- WebDAV / WebDAVS
- SFTP

实现证据：

- `mcpatch/server/MultipleServers.kt:25-33`：按 server 字符串选择 HTTP、SFTP 或 WebDAV 实现。
- `mcpatch/server/impl/HttpSupport.kt:63`：从远端读取文本元数据。
- `mcpatch/server/impl/HttpSupport.kt:138`：从远端下载文件。
- `mcpatch/server/impl/SFTPSupport.kt:99`、`:160`：SFTP 读取和下载。
- `mcpatch/server/impl/WebdavSupport.kt:59`、`:113`：WebDAV 读取和下载。

## 文件修改能力

该更新器会先下载远端版本列表，再下载每个版本对应的补丁包：

- `mcpatch/WorkThread.kt:78`：读取 `options.versionsFileName`，默认是 `versions.txt`。
- `mcpatch/WorkThread.kt:135`：下载 `${version}.mcpatch.zip`。
- `mcpatch/WorkThread.kt:500`、`:506`：执行补丁包下载。

补丁元数据支持以下远端控制字段：

- `old-files`
- `new-files`
- `old-folders`
- `new-folders`
- `move-files`

反编译证据：

- `mcpatch/data/VersionData.kt:9-13`：定义 `oldFiles`、`newFiles`、`oldFolders`、`newFolders`、`moveFiles`。
- `mcpatch/WorkThread.kt:217-221`：移动文件到临时路径。
- `mcpatch/WorkThread.kt:225-233`：删除旧文件。
- `mcpatch/WorkThread.kt:236-244`：删除旧文件夹。
- `mcpatch/WorkThread.kt:247-255`：创建新文件夹。
- `mcpatch/WorkThread.kt:264-407`：写入或 patch 新文件，校验 SHA1 后移动到最终路径。

底层文件操作封装：

- `mcpatch/util/File2.kt:204`：`delete()`，包含递归删除逻辑。
- `mcpatch/util/File2.kt:216`：`move(target)`。
- `mcpatch/util/File2.kt:61`：`mkdirs()`。

结论：远端更新服务器可以通过补丁元数据指示客户端在更新目录下删除、创建、移动、修改文件。

## 默认更新目录

配置中 `base-path: ''` 为空。此时程序会搜索 `.minecraft`，并把 `.minecraft` 的父目录作为更新根目录。

反编译证据：

- `mcpatch/McPatchClient.kt:269`：定义 `searchDotMinecraft(...)`。
- `mcpatch/McPatchClient.kt:427`：定义 `getUpdateDirectory(...)`。
- `mcpatch/McPatchClient.kt:434`：当未显式配置 `base-path` 时调用 `searchDotMinecraft(workDir)`。

结论：默认行为不是只更新一个独立缓存目录，而是面向 Minecraft 客户端安装目录进行文件修改。

## TLS / 证书校验

内置默认更新源是明文 HTTP：

```text
http://47.98.173.79:56600
```

代码中还存在忽略证书校验的辅助逻辑：

- `mcpatch/webdav/McPatchSardineImplKt.kt:8`：定义 `CreateIgnoreVerifySsl()`。
- `mcpatch/webdav/McPatchSardineImplKt.kt:14`：覆盖 `checkServerTrusted(...)`。
- `mcpatch/server/impl/HttpSupport.kt:52-55`：构造带该 SSL context / trust manager 的 OkHttp 客户端。

虽然配置默认写着 `ignore-https-certificate: false`，但代码中确实存在忽略证书校验的实现，并且 HTTP 支持类构造时会创建相关对象。该部分需要继续审查具体运行分支。

## 关于 Mojang / MCP 分发问题

仅检查这个本地 `McPatchClient.jar`，暂未发现它直接打包 Mojang 原版类或资源：

- 未发现 `net/minecraft/**`
- 未发现 `com/mojang/**`
- 未发现 `assets/minecraft/**`
- 未发现 `versions/**` 或 `libraries/**` 整体客户端目录

因此，不能仅凭这个本地 JAR 直接证明它已经分发 Mojang 客户端源码、类文件或完整资源。

但是：该 JAR 会从 `http://47.98.173.79:56600` 下载远端 `versions.txt` 和对应的 `*.mcpatch.zip`。如果这些远端补丁包包含 Mojang 原版类、资源、库、完整客户端 JAR，或未经授权的修改版客户端，则 Mojang 分发问题应以远端 payload 为证据继续确认。

## 已确认风险

本地静态分析已经确认：

- 该 JAR 可作为 Java Agent 通过 `premain` 提前运行。
- 内置远端更新源：`http://47.98.173.79:56600`。
- 支持 HTTP、WebDAV、SFTP 更新源。
- 会下载远端补丁包。
- 远端补丁元数据可以指示客户端删除、创建、移动、修改本地文件。
- 默认更新目录定位到 Minecraft 客户端安装上下文。
- 代码中存在忽略 TLS 证书校验的辅助实现。

## 当前未确认事项

仅凭本地 JAR，暂未确认：

- 本地 JAR 直接包含 Mojang 原版客户端类或资源。
- 本地 JAR 直接包含 native DLL 行为。
- 本地 JAR 直接窃取账号、token、浏览器数据。
- 本地 JAR 自身直接执行任意远端代码；但它具备远端控制文件更新/替换能力。

## 建议继续取证

1. 保留原始 `McPatchClient.jar`，记录 SHA256。
2. 保留 `META-INF/MANIFEST.MF`、`mc-patch-config.yml`、反编译后的 `mcpatch/**` 核心源码。
3. 在隔离环境中只拉取远端元数据和补丁包，不直接运行更新器。
4. 对每个远端 `*.mcpatch.zip` 先计算 SHA256，再检查是否包含：
   - `net/minecraft/**`
   - `com/mojang/**`
   - `assets/minecraft/**`
   - 原版/修改版客户端 JAR
   - 启动器、mods、libraries 目录的大规模替换内容
5. 在未完成审计前，不建议玩家在真实客户端环境运行该更新器。

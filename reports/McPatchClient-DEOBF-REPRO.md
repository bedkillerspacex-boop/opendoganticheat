# McPatchClient 反编译复现说明

本仓库不直接提交 `McPatchClient.jar` 的完整反编译源码。

原因：

- 该 JAR 是 fat JAR，包含约 11942 个 class，大量内容是第三方依赖。
- 直接公开完整反编译结果可能扩大版权和分发风险。
- 审计需要的是可复现证据，而不是把整包依赖源码重新发布。

## 本地复现

在拥有原始 JAR 的机器上运行：

```powershell
.\tools\deobf-mcpatchclient.ps1 -JarPath "E:\download\[v1.5.1p]LesRaisins & Limitless\McPatchClient.jar"
```

默认输出：

```text
work/mcpatchclient-deobf/classes
work/mcpatchclient-deobf/src-vineflower
work/mcpatchclient-deobf/reports
```

脚本会生成：

- `sha256.txt`：原始 JAR SHA256。
- `mcpatch-source-index.txt`：反编译后 `mcpatch/**` 核心源码文件索引。
- `README.md`：本次反编译输出说明。

## 已审计的核心源码路径

重点查看：

- `mcpatch/McPatchClient.kt`
- `mcpatch/WorkThread.kt`
- `mcpatch/data/GlobalOptions.kt`
- `mcpatch/data/VersionData.kt`
- `mcpatch/data/NewFile.kt`
- `mcpatch/data/MoveFile.kt`
- `mcpatch/server/MultipleServers.kt`
- `mcpatch/server/impl/HttpSupport.kt`
- `mcpatch/server/impl/SFTPSupport.kt`
- `mcpatch/server/impl/WebdavSupport.kt`
- `mcpatch/util/File2.kt`
- `mcpatch/webdav/McPatchSardineImplKt.kt`

## 对应审计报告

中文报告见：

- `reports/AUDIT-McPatchClient-ZH.md`

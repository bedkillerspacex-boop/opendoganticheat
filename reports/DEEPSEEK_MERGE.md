# DeepSeek 合并与命名恢复说明

## 输入来源

- CFR 全量输出：E:\deobf-doganticheat\sources-cfr-full
- Vineflower 全量输出：E:\deobf-doganticheat\sources-vineflower-full
- DeepSeek deobf 输出：E:\deobf-doganticheat\sources-deepseek
- javap 兜底：E:\deobf-doganticheat\javap

## 合并结果

- 合并目录：E:\deobf-doganticheat\sources-merged-restored
- Java 文件数：186
- DeepSeek zip 内 Java 文件数：185
- DeepSeek 缺少隐藏字节码资源 classes/SocketProxy；该文件已从原 JAR 资源单独反编译并纳入合并目录。

## 合并策略

1. DeepSeek 输出用于交叉核对 CFR 结果。
2. Vineflower 输出用于结构更接近源码的 try-with-resources、lambda 和控制流。
3. CFR 输出用于核对反编译边界和中文字符串，因为它保留了更稳定的 Unicode escape。
4. javap 输出作为每个字节码单元的兜底，防止反编译器误构造控制流。
5. 对高价值类做人工语义命名恢复，不强行改全项目类名，避免破坏与字节码/包路径的对应关系。

## 已人工恢复较多变量名的文件

- org/ywzj/doganticheat/mixin/JoinMultiplayerScreenMixin.java
  - 重点恢复 initProxy 逻辑中的 encryptedGatewayUrl、realServerAddress、nativePayload、loginName、proxyHostPort、localPort、proxyCommand 等变量。
- org/ywzj/doganticheat/b/a/g.java
  - 重点恢复客户端更新分片写入逻辑中的 updateExecutor、pendingChunks、updateBuffer、nextChunkIndex、targetModFile 等变量。
- SocketProxy.java
  - 使用 Vineflower 结构化输出，变量名已经较清楚：clientSocket、remoteSocket、remoteHost、remotePort、forwardClientToRemote 等。

## 无法真实恢复的部分

大多数 class 不含可用 LocalVariableTable，真实原始变量名已经不在字节码中。除非拿到原始源码、带调试符号的 class、混淆映射或构建产物，否则只能按上下文做语义推断。本目录中的“恢复命名”属于审计阅读用的 best-effort，不代表作者原始命名。

## 语义映射建议

- org.ywzj.doganticheat.core.a：NativeLibraryLoader / DllExtractor
- org.ywzj.doganticheat.core.b：JavaModuleAccessPatcher
- org.ywzj.doganticheat.core.c：NativeApiBridge
- org.ywzj.doganticheat.core.EncryptedFileSystem：EncryptedJarFileSystem
- org.ywzj.doganticheat.a.a：ResourcePackHasher
- org.ywzj.doganticheat.a.b：ShaderPackHasher
- org.ywzj.doganticheat.a.c：TextureAndSkinInspector
- org.ywzj.doganticheat.b.a.g：ClientUpdatePacketHandler
- org.ywzj.doganticheat.mixin.JoinMultiplayerScreenMixin：ProxyBootstrapAndServerListRewriteMixin

## 安全边界

本合并只为后门/隐私/native 风险审计服务，不包含绕过反作弊、伪造校验、禁用检测或欺骗服务器的方法。

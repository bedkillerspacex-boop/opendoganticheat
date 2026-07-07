# DogAntiCheat 静态审计报告

## 结论摘要

目标文件：E:\download\DogAntiCheat-Client-prod.jar

SHA256：EF2D0DA9AEB2DD8C53E9AD7AB7D2D4E782F2A4E8B1D5A3F5B06B1789C59452D9，与计划中的 EF2D0DA9AEB2DD8C53E9AD7AB7D2D4E782F2A4E8B1D5A3F5B06B1789C59452D9 一致。

本次只做静态分析：未运行目标 JAR、未加载 native DLL、未启动 Minecraft、未连接其远端服务。反编译覆盖率满足 best-effort 要求：JAR 内 185 个 .class 加 1 个伪装为资源的 classes/SocketProxy 字节码，共 186 个字节码单元；CFR、Vineflower、merged-restored 和 javap 兜底均为 186/186。DeepSeek 输入包为 185 个 Java，缺少 SocketProxy，已合并补齐。

总体判断：该 mod 具有高风险行为。已确认的 Java 层行为包括 Java agent premain、native DLL 强制加载、修改 Java 模块/Forge 文件系统逻辑、启动 native 后台线程、创建本地 TCP 转发代理、向远端 POST 登录名和 native 生成数据、扫描资源包/shader/tacz 内容 hash、服务端触发客户端写入 mods 目录文件。native DLL 的导入表还显示进程枚举、远程线程、读写进程内存、截图、注册表、TPM、反调试等能力；由于未动态执行，DLL 具体调用路径需进一步在隔离环境中验证。

## 高风险发现

### 1. Java agent + native DLL 在游戏初始化前执行

证据：META-INF/MANIFEST.MF 声明 Premain-Class: org.ywzj.doganticheat.Premain、Can-Retransform-Classes: true、Can-Redefine-Classes: true。org/ywzj/doganticheat/Premain.java:16-27 调用 DogAntiCheat.loadDlls()，成功后启动 new Thread(NativeApi::core0).start()，并设置 RSA public key 到 native 方法。

影响：这使反作弊在普通 mod 初始化前获得 Java agent 能力和 native 执行能力，可改写 class、访问 JVM 内部并运行不透明 native 逻辑。

### 2. DLL 提取和加载路径异常，主 DLL 伪装为 .zip

证据：org/ywzj/doganticheat/core/a.java:24-40 把多个 DLL 写入 java.home/bin，并将 dac-1.20.dll 写入 %TEMP%/dac。core/a.java:68-126 将 DLL 字节写入 .zip 文件，再追加 BMP zip entries，最后 System.load(file3.getAbsolutePath())。

影响：写入 JRE bin 和用 .zip 扩展名加载 native 库都属于强风险行为，降低用户可见性，也增加误判、持久化和清理困难。

### 3. 修改 Forge/Java 文件系统路径，支持加密或伪装 JAR 读取

证据：Premain transformer 修改 cpw/mods/niofs/union/UnionFileSystem 和 org/spongepowered/asm/launch/platform/MainAttributes；EncryptedFileSystem.java:45-58 在普通 Zip 打开失败时调用 native 解密 c.d(Files.readAllBytes(path)) 并在 Jimfs 内重建 jar 文件系统。

影响：可让客户端加载普通工具无法直接读取的加密或伪装 mod/资源，降低透明度和可审计性。

### 4. 远端代理下发与本地 TCP 转发

证据：JoinMultiplayerScreenMixin.java:150-180 读取 config/doganticheat.properties 中默认 Base64 密文，用 native 解密 URL/目标参数，POST JSON；字段包含 loginName 和 data。JoinMultiplayerScreenMixin.java:212-240 将资源 classes/SocketProxy 写入 java.home/bin/SocketProxy.class，使用 ProcessBuilder 启动 java -cp <java.home/bin> SocketProxy <localPort> <remoteHost> <remotePort>。SocketProxy.java 显示它监听本地端口并双向转发到远端 host/port。

影响：这是已确认的网络转发/中间代理能力，会改写玩家服务器地址并把连接经由远端返回的地址转发。它可能用于网关、校验或流量控制，但对用户来说是高隐私和安全风险。

### 5. 服务端可触发客户端扫描和上报资源/mod 环境信息

证据：org/ywzj/doganticheat/a/a.java 收集资源包和 gameDir/tacz 目录的 hash 与文件名；org/ywzj/doganticheat/a/b.java 扫描 shaderpacks；org/ywzj/doganticheat/b/a/d.java 在收到服务端消息后把资源包、shader、tacz 结果交给 native c.a(list) 后发回。

影响：会采集玩家客户端资源、材质、shader 和特定目录清单摘要。虽然主要是 hash+文件名，不是直接全量上传文件内容，但仍属于环境指纹和隐私采集。

### 6. 服务端可向客户端 mods 目录写文件

证据：org/ywzj/doganticheat/b/a/g.java 接收分片数据，校验 hash 后写入 FMLPaths.MODSDIR.resolve(fileName)，随后向服务端报告客户端更新保存成功/失败。

影响：这是远端触发的本地文件写入能力。虽然有 hash 校验，但 hash 和文件名来自同一远端消息，不能构成用户侧授权边界。若服务端或链路被滥用，可能向 mods 目录投放新 JAR 或覆盖文件。

### 7. native DLL 具备进程、内存、截图、注册表和反调试相关能力

证据：report/dll-imports.txt 中 dac-1.20.dll 导入 CreateRemoteThread、OpenProcess、ReadProcessMemory、WriteProcessMemory、VirtualAllocEx、VirtualProtectEx、CreateToolhelp32Snapshot、SetThreadContext、SuspendThread、ResumeThread、TerminateProcess、AdjustTokenPrivileges、注册表 API、FindWindowA、GetForegroundWindow、GetWindowTextA、BitBlt、GDI+、SetupAPI、TPM API、IsDebuggerPresent、AddVectoredExceptionHandler。

影响：静态导入表显示其具备进程检测、注入式操作、窗口枚举、屏幕捕获、硬件指纹和反调试能力。仅凭导入表不能证明每项都会运行，但能力面远超普通 Minecraft mod。

## 处置建议

普通用户建议卸载该 mod，不加入强制安装该 mod 的服务器。研究人员只在虚拟机/沙箱中分析，禁用主账号登录，使用独立 gameDir 和一次性 Java runtime，监控网络、文件系统、注册表、进程创建。

## 覆盖率

详见 report/coverage.json 和 report/DEEPSEEK_MERGE.md。

## 不包含内容

本报告不提供绕过反作弊检测、禁用校验、伪造环境、欺骗服务器或规避封禁的方法。

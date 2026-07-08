# dac-1.20.dll recovered notes

Target: `E:\download\dac 有毒有后门.jar`

- JAR SHA256: `ef2d0da9aeb2dd8c53e9ad7ab7d2d4e782f2a4e8b1d5a3f5b06b1789c59452d9`
- DLL entry: `dlls/dac-1.20.dll`
- DLL SHA256: `c6e05ea44a3f7f033313822186f35f4b80e255a2f85e36b8672d75a50c110369`
- Method: static analysis only. The DLL was not loaded or executed.

## Scope

This directory is for the non-public DogAntiCheat native layer. Public or third-party code is not expanded here:

- `libcrypto-3-x64.dll` / `libssl-3-x64.dll`: OpenSSL runtime dependency.
- `msvcp140d.dll`, `ucrtbased.dll`, `vcruntime140*.dll`: MSVC debug runtime.
- `zlib.dll`: public compression library.
- `asmjit` symbols exported from `dac-1.20.dll`: public JIT assembler library symbols linked or exported by the native DLL.
- `org/ywzj/doganticheat/jimfs/*`: shaded Java Jimfs implementation, already public-source equivalent and not part of the native backdoor.

## JNI exports

`dac-1.20.dll` exports these JNI entry points:

- `Java_org_ywzj_doganticheat_core_NativeApi_core0`
- `Java_org_ywzj_doganticheat_core_NativeApi_core1`
- `Java_org_ywzj_doganticheat_core_NativeApi_op0`
- `Java_org_ywzj_doganticheat_core_NativeApi_op1`
- `Java_org_ywzj_doganticheat_core_NativeApi_op2`
- `Java_org_ywzj_doganticheat_core_NativeApi_op3`
- `Java_org_ywzj_doganticheat_core_NativeApi_op4`
- `Java_org_ywzj_doganticheat_core_NativeApi_op5`
- `Java_org_ywzj_doganticheat_core_NativeApi_op6`
- `Java_org_ywzj_doganticheat_core_NativeApi_op7`
- `Java_org_ywzj_doganticheat_core_NativeApi_op8`
- `Java_org_ywzj_doganticheat_core_NativeApi_op9`
- `Java_org_ywzj_doganticheat_core_NativeApi_op10`
- `Java_org_ywzj_doganticheat_core_NativeApi_test`

The Java bridge is `src-decompiled/org/ywzj/doganticheat/core/c.java`; the native declarations are in `src-decompiled/org/ywzj/doganticheat/core/NativeApi.java`.

## Recovered role map

| Native API | Java caller meaning | Evidence |
| --- | --- | --- |
| `core0()` | Native background engine thread started by premain. | `Premain.java` starts `new Thread(NativeApi::core0)`. |
| `op2(byte[])` | Stores RSA public key bytes in native state. | `Premain.java` passes an X.509 RSA public key. |
| `op3(byte)` | Sets native state flag after key install. | `Premain.java` calls `op3((byte)1)`. |
| `op0()` | Produces native payload sent to gateway. | `JoinMultiplayerScreenMixin.java` Base64-encodes `c.a()` into JSON field `data`. |
| `op5(byte[])` | Decrypts Base64 config values, including gateway URL and real server address. | `JoinMultiplayerScreenMixin.java` calls `c.d(Base64.decode(w/f))`. |
| `op6(List)` | Converts resource/shader/TACZ scan list to bytes for server report. | Packet handler `b/a/d.java` calls `c.a(list)`. |
| `op8(byte[])` / `op9(int, byte[])` | Packet crypto/signing or validation helpers. | Used by packet classes through the `c` bridge. |
| `op10()` | Returns native environment/process detection set. | Bridge returns `HashSet`; imports and strings show process/window/VM detection capability. |

## Confirmed high-risk native capabilities

The DLL import table shows capability for:

- Process enumeration and control: `CreateToolhelp32Snapshot`, `OpenProcess`, `OpenThread`, `SuspendThread`, `ResumeThread`, `TerminateProcess`.
- Cross-process memory and injection primitives: `VirtualAllocEx`, `ReadProcessMemory`, `WriteProcessMemory`, `CreateRemoteThread`, `GetThreadContext`, `SetThreadContext`.
- Anti-debug and exception manipulation: `IsDebuggerPresent`, `AddVectoredExceptionHandler`, `SetUnhandledExceptionFilter`, `OutputDebugStringW`, `QueryPerformanceCounter`.
- Screen/window inspection: `FindWindowA/W`, `GetForegroundWindow`, `GetWindowTextA/W`, `BitBlt`, `GdiplusStartup`.
- Registry and hardware fingerprinting: `Reg*`, `SetupDi*`, `Tbsip_Submit_Command`, `EnumSystemFirmwareTables`, `GetSystemFirmwareTable`.
- VM/sandbox detection strings: VMware, KVM, Hyper-V, QEMU, Xen, VirtualBox-style artifacts and registry/service names.

This does not prove every path always runs, but it proves the native DLL contains the ability surface.

## Current static status

`op5` was inspected at export RVA `0x1cd460`. The start of the function is JNI byte-array plumbing followed by calls into internal obfuscated helper functions. The DLL imports OpenSSL symbols such as `EVP_aes_128_ecb`, `EVP_Decrypt*`, `RSA_public_decrypt`, `RSA_public_encrypt`, and `d2i_RSA_PUBKEY`, but `op5` does not directly reference the crypto IAT in normal RIP-relative form. That suggests one of:

- crypto function pointers are copied through a runtime table,
- the relevant call path is obfuscated behind indirect calls,
- or the imported OpenSSL routines are used by other native APIs while `op5` uses a custom VM/helper.

The default encrypted `w` and `f` values decode to 320 and 288 bytes respectively, both AES block multiples. They are not simple single-block RSA-2048 ciphertexts.

See `NativeApi_recovered.cpp` for readable pseudocode of the native boundary and known semantics.

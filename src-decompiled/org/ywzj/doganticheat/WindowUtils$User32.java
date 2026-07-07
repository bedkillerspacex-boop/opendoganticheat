package org.ywzj.doganticheat;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser.WNDENUMPROC;
import com.sun.jna.win32.StdCallLibrary;

public interface WindowUtils$User32 extends StdCallLibrary {
   WindowUtils$User32 INSTANCE = (WindowUtils$User32)Native.loadLibrary("user32", WindowUtils$User32.class);

   boolean EnumWindows(WNDENUMPROC var1, Pointer var2);

   int GetWindowTextA(HWND var1, byte[] var2, int var3);
}

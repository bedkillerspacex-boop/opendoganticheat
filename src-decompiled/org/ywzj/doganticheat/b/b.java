package org.ywzj.doganticheat.b;

enum b {
   a(100),
   b(101),
   c(102),
   d(103),
   e(104),
   f(105),
   g(106),
   h(107),
   i(108),
   j(109),
   k(110),
   l(111),
   m(112),
   n(113),
   o(114);

   private final int p;

   public static b[] a() {
      return (b[])q.clone();
   }

   public static b a(String var0) {
      return Enum.valueOf(b.class, var0);
   }

   b(int var3) {
      this.p = var3;
   }

   int b() {
      return this.p;
   }
}

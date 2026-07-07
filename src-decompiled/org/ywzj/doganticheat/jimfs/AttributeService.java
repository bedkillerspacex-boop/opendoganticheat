package org.ywzj.doganticheat.jimfs;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableMap.Builder;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import org.checkerframework.checker.nullness.qual.Nullable;

final class AttributeService {
   private static final String ALL_ATTRIBUTES = "*";
   private final ImmutableMap providersByName;
   private final ImmutableMap providersByViewType;
   private final ImmutableMap providersByAttributesType;
   private final ImmutableList defaultValues;
   private static final Splitter ATTRIBUTE_SPLITTER = Splitter.on(',');

   public AttributeService(Configuration var1) {
      this(getProviders(var1), var1.defaultAttributeValues);
   }

   public AttributeService(Iterable var1, Map var2) {
      Builder var3 = ImmutableMap.builder();
      Builder var4 = ImmutableMap.builder();
      Builder var5 = ImmutableMap.builder();
      com.google.common.collect.ImmutableList.Builder var6 = ImmutableList.builder();

      for (AttributeProvider var8 : var1) {
         var3.put(var8.name(), var8);
         var4.put(var8.viewType(), var8);
         if (var8.attributesType() != null) {
            var5.put(var8.attributesType(), var8);
         }

         UnmodifiableIterator var9 = var8.defaultValues(var2).entrySet().iterator();

         while (var9.hasNext()) {
            Entry var10 = (Entry)var9.next();
            var6.add(new AttributeService$SimpleFileAttribute((String)var10.getKey(), var10.getValue()));
         }
      }

      this.providersByName = var3.build();
      this.providersByViewType = var4.build();
      this.providersByAttributesType = var5.build();
      this.defaultValues = var6.build();
   }

   private static Iterable getProviders(Configuration var0) {
      HashMap var1 = new HashMap();
      UnmodifiableIterator var2 = var0.attributeProviders.iterator();

      while (var2.hasNext()) {
         AttributeProvider var3 = (AttributeProvider)var2.next();
         var1.put(var3.name(), var3);
      }

      var2 = var0.attributeViews.iterator();

      while (var2.hasNext()) {
         String var5 = (String)var2.next();
         addStandardProvider(var1, var5);
      }

      addMissingProviders(var1);
      return Collections.unmodifiableCollection(var1.values());
   }

   private static void addMissingProviders(Map var0) {
      HashSet var1 = new HashSet();

      for (AttributeProvider var3 : var0.values()) {
         UnmodifiableIterator var4 = var3.inherits().iterator();

         while (var4.hasNext()) {
            String var5 = (String)var4.next();
            if (!var0.containsKey(var5)) {
               var1.add(var5);
            }
         }
      }

      if (!var1.isEmpty()) {
         for (String var7 : var1) {
            addStandardProvider(var0, var7);
         }

         addMissingProviders(var0);
      }
   }

   private static void addStandardProvider(Map var0, String var1) {
      AttributeProvider var2 = StandardAttributeProviders.get(var1);
      if (var2 == null) {
         if (!var0.containsKey(var1)) {
            throw new IllegalStateException("no provider found for attribute view '" + var1 + "'");
         }
      } else {
         var0.put(var2.name(), var2);
      }
   }

   public ImmutableSet supportedFileAttributeViews() {
      return this.providersByName.keySet();
   }

   public boolean supportsFileAttributeView(Class var1) {
      return this.providersByViewType.containsKey(var1);
   }

   public void setInitialAttributes(File var1, FileAttribute... var2) {
      for (int var3 = 0; var3 < this.defaultValues.size(); var3++) {
         FileAttribute var4 = (FileAttribute)this.defaultValues.get(var3);
         int var5 = var4.name().indexOf(58);
         String var6 = var4.name().substring(0, var5);
         String var7 = var4.name().substring(var5 + 1);
         var1.setAttribute(var6, var7, var4.value());
      }

      for (FileAttribute var11 : var2) {
         this.setAttribute(var1, var11.name(), var11.value(), true);
      }
   }

   public void copyAttributes(File var1, File var2, AttributeCopyOption var3) {
      switch (var3) {
         case ALL:
            var1.copyAttributes(var2);
            break;
         case BASIC:
            var1.copyBasicAttributes(var2);
      }
   }

   public Object getAttribute(File var1, String var2) {
      String var3 = getViewName(var2);
      String var4 = getSingleAttribute(var2);
      return this.getAttribute(var1, var3, var4);
   }

   public Object getAttribute(File var1, String var2, String var3) {
      Object var4 = this.getAttributeInternal(var1, var2, var3);
      if (var4 == null) {
         throw new IllegalArgumentException("invalid attribute for view '" + var2 + "': " + var3);
      } else {
         return var4;
      }
   }

   private @Nullable Object getAttributeInternal(File var1, String var2, String var3) {
      AttributeProvider var4 = (AttributeProvider)this.providersByName.get(var2);
      if (var4 == null) {
         return null;
      }

      Object var5 = var4.get(var1, var3);
      if (var5 == null) {
         UnmodifiableIterator var6 = var4.inherits().iterator();

         while (var6.hasNext()) {
            String var7 = (String)var6.next();
            var5 = this.getAttributeInternal(var1, var7, var3);
            if (var5 != null) {
               break;
            }
         }
      }

      return var5;
   }

   public void setAttribute(File var1, String var2, Object var3, boolean var4) {
      String var5 = getViewName(var2);
      String var6 = getSingleAttribute(var2);
      this.setAttributeInternal(var1, var5, var6, var3, var4);
   }

   private void setAttributeInternal(File var1, String var2, String var3, Object var4, boolean var5) {
      AttributeProvider var6 = (AttributeProvider)this.providersByName.get(var2);
      if (var6 != null) {
         if (var6.supports(var3)) {
            var6.set(var1, var2, var3, var4, var5);
            return;
         }

         UnmodifiableIterator var7 = var6.inherits().iterator();

         while (var7.hasNext()) {
            String var8 = (String)var7.next();
            AttributeProvider var9 = (AttributeProvider)this.providersByName.get(var8);
            if (var9.supports(var3)) {
               var9.set(var1, var2, var3, var4, var5);
               return;
            }
         }
      }

      throw new UnsupportedOperationException("cannot set attribute '" + var2 + ":" + var3 + "'");
   }

   public @Nullable FileAttributeView getFileAttributeView(FileLookup var1, Class var2) {
      AttributeProvider var3 = (AttributeProvider)this.providersByViewType.get(var2);
      return var3 != null ? var3.view(var1, this.createInheritedViews(var1, var3)) : null;
   }

   private FileAttributeView getFileAttributeView(FileLookup var1, Class var2, Map var3) {
      AttributeProvider var4 = (AttributeProvider)this.providersByViewType.get(var2);
      this.createInheritedViews(var1, var4, var3);
      return var4.view(var1, ImmutableMap.copyOf(var3));
   }

   private ImmutableMap createInheritedViews(FileLookup var1, AttributeProvider var2) {
      if (var2.inherits().isEmpty()) {
         return ImmutableMap.of();
      }

      HashMap var3 = new HashMap();
      this.createInheritedViews(var1, var2, var3);
      return ImmutableMap.copyOf(var3);
   }

   private void createInheritedViews(FileLookup var1, AttributeProvider var2, Map var3) {
      UnmodifiableIterator var4 = var2.inherits().iterator();

      while (var4.hasNext()) {
         String var5 = (String)var4.next();
         if (!var3.containsKey(var5)) {
            AttributeProvider var6 = (AttributeProvider)this.providersByName.get(var5);
            FileAttributeView var7 = this.getFileAttributeView(var1, var6.viewType(), var3);
            var3.put(var5, var7);
         }
      }
   }

   public ImmutableMap readAttributes(File var1, String var2) {
      String var3 = getViewName(var2);
      ImmutableList var4 = getAttributeNames(var2);
      if (var4.size() > 1 && var4.contains("*")) {
         throw new IllegalArgumentException("invalid attributes: " + var2);
      }

      HashMap var5 = new HashMap();
      if (var4.size() == 1 && var4.contains("*")) {
         AttributeProvider var10 = (AttributeProvider)this.providersByName.get(var3);
         readAll(var1, var10, var5);
         UnmodifiableIterator var11 = var10.inherits().iterator();

         while (var11.hasNext()) {
            String var8 = (String)var11.next();
            AttributeProvider var9 = (AttributeProvider)this.providersByName.get(var8);
            readAll(var1, var9, var5);
         }
      } else {
         for (String var7 : var4) {
            var5.put(var7, this.getAttribute(var1, var3, var7));
         }
      }

      return ImmutableMap.copyOf(var5);
   }

   public BasicFileAttributes readAttributes(File var1, Class var2) {
      AttributeProvider var3 = (AttributeProvider)this.providersByAttributesType.get(var2);
      if (var3 != null) {
         return var3.readAttributes(var1);
      } else {
         throw new UnsupportedOperationException("unsupported attributes type: " + var2);
      }
   }

   private static void readAll(File var0, AttributeProvider var1, Map var2) {
      UnmodifiableIterator var3 = var1.attributes(var0).iterator();

      while (var3.hasNext()) {
         String var4 = (String)var3.next();
         Object var5 = var1.get(var0, var4);
         if (var5 != null) {
            var2.put(var4, var5);
         }
      }
   }

   private static String getViewName(String var0) {
      int var1 = var0.indexOf(58);
      if (var1 == -1) {
         return "basic";
      } else if (var1 != 0 && var1 != var0.length() - 1 && var0.indexOf(58, var1 + 1) == -1) {
         return var0.substring(0, var1);
      } else {
         throw new IllegalArgumentException("illegal attribute format: " + var0);
      }
   }

   private static ImmutableList getAttributeNames(String var0) {
      int var1 = var0.indexOf(58);
      String var2 = var0.substring(var1 + 1);
      return ImmutableList.copyOf(ATTRIBUTE_SPLITTER.split(var2));
   }

   private static String getSingleAttribute(String var0) {
      ImmutableList var1 = getAttributeNames(var0);
      if (var1.size() == 1 && !"*".equals(var1.get(0))) {
         return (String)var1.get(0);
      } else {
         throw new IllegalArgumentException("must specify a single attribute: " + var0);
      }
   }
}

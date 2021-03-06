package me.minidigger.mappingviewer.minimappingviewer.util;

import org.cadixdev.bombe.type.signature.MethodSignature;
import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.merge.MappingSetMergerHandler;
import org.cadixdev.lorenz.merge.MergeContext;
import org.cadixdev.lorenz.merge.MergeResult;
import org.cadixdev.lorenz.model.ClassMapping;
import org.cadixdev.lorenz.model.FieldMapping;
import org.cadixdev.lorenz.model.InnerClassMapping;
import org.cadixdev.lorenz.model.MethodMapping;
import org.cadixdev.lorenz.model.TopLevelClassMapping;

import java.util.HashMap;
import java.util.Map;

// from https://github.com/DemonWav/paperweight/blob/master/src/main/kotlin/tasks/GenerateSpigotSrgs.kt
public class SpigotPackageMergerHandler implements MappingSetMergerHandler {

  private Map<String, Map<String, Map<String, String>>> synths = new HashMap<>();

    @Override
    public MergeResult<TopLevelClassMapping> mergeTopLevelClassMappings(TopLevelClassMapping left, TopLevelClassMapping right, MappingSet target, MergeContext context) {
        throw new IllegalStateException("Unexpectedly merged class: " + left.getFullObfuscatedName());
    }

    @Override
    public MergeResult<TopLevelClassMapping> mergeDuplicateTopLevelClassMappings(TopLevelClassMapping left, TopLevelClassMapping right, TopLevelClassMapping rightContinuation, MappingSet target, MergeContext context) {
        // If both are provided, keep spigot
        return new MergeResult<>(target.createTopLevelClassMapping(left.getObfuscatedName(), prependPackage(left.getDeobfuscatedName())), right);
    }

    @Override
    public MergeResult<TopLevelClassMapping> addLeftTopLevelClassMapping(TopLevelClassMapping left, MappingSet target, MergeContext context) {
        throw new IllegalStateException("Unexpected added class from Spigot:" + left.getFullDeobfuscatedName());
    }

    @Override
    public MergeResult<TopLevelClassMapping> addRightTopLevelClassMapping(TopLevelClassMapping right, MappingSet target, MergeContext context) {
        // This is a mapping Spigot is totally missing, but Spigot maps all classes without a package to
        // /net/minecraft regardless if there are mappings for the classes or not
        return new MergeResult<>(target.createTopLevelClassMapping(right.getObfuscatedName(), prependPackage(right.getObfuscatedName())), right);
    }

    @Override
    public MergeResult<InnerClassMapping> mergeInnerClassMappings(InnerClassMapping left, InnerClassMapping right, ClassMapping<?, ?> target, MergeContext context) {
        throw new IllegalStateException("Unexpectedly merged class: " + left.getFullObfuscatedName());
    }

    @Override
    public MergeResult<InnerClassMapping> mergeDuplicateInnerClassMappings(InnerClassMapping left, InnerClassMapping right, InnerClassMapping rightContinuation, ClassMapping<?, ?> target, MergeContext context) {
        return new MergeResult<>(target.createInnerClassMapping(left.getObfuscatedName(), left.getDeobfuscatedName()), right);
    }

    @Override
    public MergeResult<InnerClassMapping> addLeftInnerClassMapping(InnerClassMapping left, ClassMapping<?, ?> target, MergeContext context) {
        throw new IllegalStateException("Unexpected added class from Spigot:" + left.getFullDeobfuscatedName());
    }

    @Override
    public MergeResult<InnerClassMapping> addRightInnerClassMapping(InnerClassMapping right, ClassMapping<?, ?> target, MergeContext context) {
        // We want to get all of the inner classes from mojang, but not the mojang names
        return  new MergeResult<>(target.createInnerClassMapping(right.getObfuscatedName(), right.getObfuscatedName()), right);
    }

    @Override
    public FieldMapping mergeFieldMappings(FieldMapping left, FieldMapping strictRight, FieldMapping looseRight, ClassMapping<?, ?> target, MergeContext context) {
        throw new IllegalStateException("Unexpectedly merged field: " + left.getFullObfuscatedName());
    }

    @Override
    public FieldMapping mergeDuplicateFieldMappings(FieldMapping left, FieldMapping strictRightDuplicate, FieldMapping looseRightDuplicate, FieldMapping strictRightContinuation, FieldMapping looseRightContinuation, ClassMapping<?, ?> target, MergeContext context) {
        FieldMapping right;
        if (strictRightDuplicate != null) {
            right = strictRightDuplicate;
        } else {
            if (looseRightDuplicate != null) {
                right = looseRightDuplicate;
            } else {
                if (strictRightContinuation != null) {
                    right = strictRightContinuation;
                } else {
                    if (looseRightContinuation != null) {
                        right = looseRightContinuation;
                    } else {
                        right = left;
                    }
                }
            }
        }
        return target.createFieldMapping(right.getSignature(), left.getDeobfuscatedName());
    }

    @Override
    public MergeResult<MethodMapping> mergeMethodMappings(MethodMapping left, MethodMapping strictRight, MethodMapping looseRight, ClassMapping<?, ?> target, MergeContext context) {
        throw new IllegalStateException("Unexpectedly merged method: " + left);
    }

    @Override
    public MergeResult<MethodMapping> mergeDuplicateMethodMappings(MethodMapping left, MethodMapping strictRightDuplicate, MethodMapping looseRightDuplicate, MethodMapping strictRightContinuation, MethodMapping looseRightContinuation, ClassMapping<?, ?> target, MergeContext context) {
      // Check if Spigot calls this mapping something else
      Map<String, String> synthMethods = synths.getOrDefault(left.getParent().getFullDeobfuscatedName(), new HashMap<>()).getOrDefault(left.getObfuscatedDescriptor(), new HashMap<>());
      String newName = synthMethods.get(left.getObfuscatedName());
      if (newName != null) {
          MethodMapping newLeftMapping = (MethodMapping) left.getParent().getMethodMapping(new MethodSignature(newName, left.getDescriptor())).orElse(null);
          MethodMapping newMapping;
          if (newLeftMapping != null) {
              newMapping = target.getOrCreateMethodMapping(newLeftMapping.getSignature());
              newMapping.setDeobfuscatedName(left.getDeobfuscatedName());
          } else {
              newMapping = target.getOrCreateMethodMapping(left.getSignature());
              newMapping.setDeobfuscatedName(newName);
          }
          return new MergeResult<>(newMapping);
      } else {
          MethodMapping newMapping = target.getOrCreateMethodMapping(left.getSignature());
          newMapping.setDeobfuscatedName(left.getDeobfuscatedName());
          return new MergeResult<>(newMapping);
      }
    }

    @Override
    public MergeResult<MethodMapping> addLeftMethodMapping(MethodMapping left, ClassMapping<?, ?> target, MergeContext context) {
        // Check if Spigot maps this from a synthetic method name
        String obfName = null;
        Map<String, String > synthMethods = synths.getOrDefault(left.getParent().getFullObfuscatedName(), new HashMap<>()).get(left.getObfuscatedDescriptor());
        if (synthMethods != null) {
            // This is a reverse lookup
            for (Map.Entry<String, String> entry : synthMethods.entrySet()) {
                if (left.getObfuscatedName().equals(entry.getValue())) {
                    obfName = entry.getKey();
                    break;
                }
            }
        }

        if (obfName == null) {
            return new MergeResult<>(null);
        }

        MethodMapping newMapping = target.getOrCreateMethodMapping(obfName, left.getDescriptor());
        newMapping.setDeobfuscatedName(left.getDeobfuscatedName());
        return new MergeResult<>(newMapping);
    }

    @Override
    public FieldMapping addLeftFieldMapping(FieldMapping left, ClassMapping<?, ?> target, MergeContext context) {
        // We don't want mappings Spigot thinks exist but don't
        return null;
    }

    @Override
    public FieldMapping addRightFieldMapping(FieldMapping right, ClassMapping<?, ?> target, MergeContext context) {
        // disable mojang member mappings
        return null;
    }

    @Override
    public MergeResult<MethodMapping> addRightMethodMapping(MethodMapping right, ClassMapping<?, ?> target, MergeContext context) {
      // Check if spigot changes this method automatically
      Map<String, String> synthMethods = synths.getOrDefault(right.getParent().getFullObfuscatedName(), new HashMap<>()).getOrDefault(right.getObfuscatedDescriptor(), new HashMap<>());
      String newName = synthMethods.get(right.getObfuscatedName());
      if (newName == null) {
          return new MergeResult<>(null);
      }

      ClassMapping<?, ?> newClassMapping = context.getLeft().getClassMapping(right.getParent().getFullObfuscatedName()).orElse(null);
      MethodMapping newMethodMapping = newClassMapping != null ? newClassMapping.getMethodMapping(new MethodSignature(newName, right.getDescriptor())).orElse(null) : null;
      MethodMapping newMapping = target.getOrCreateMethodMapping(right.getSignature());
      if (newMethodMapping != null) {
          newMapping.setDeobfuscatedName(newMethodMapping.getDeobfuscatedName());
      } else {
          newMapping.setDeobfuscatedName(newName);
      }
      return new MergeResult<>(newMapping);
    }

    private String prependPackage(String name) {
        if (name.contains("/")) {
            return name;
        } else {
           return "net/minecraft/server/" + name;
        }
    }
}

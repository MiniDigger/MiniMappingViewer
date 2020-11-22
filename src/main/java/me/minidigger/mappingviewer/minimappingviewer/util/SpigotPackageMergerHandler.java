package me.minidigger.mappingviewer.minimappingviewer.util;

import org.cadixdev.lorenz.MappingSet;
import org.cadixdev.lorenz.merge.MappingSetMergerHandler;
import org.cadixdev.lorenz.merge.MergeContext;
import org.cadixdev.lorenz.merge.MergeResult;
import org.cadixdev.lorenz.model.ClassMapping;
import org.cadixdev.lorenz.model.FieldMapping;
import org.cadixdev.lorenz.model.InnerClassMapping;
import org.cadixdev.lorenz.model.MethodMapping;
import org.cadixdev.lorenz.model.TopLevelClassMapping;

// from https://github.com/DemonWav/paperweight/blob/master/src/main/kotlin/tasks/GenerateSpigotSrgs.kt
public class SpigotPackageMergerHandler implements MappingSetMergerHandler {

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
        return target.createFieldMapping(left.getObfuscatedName(), left.getDeobfuscatedName());
    }

    @Override
    public MergeResult<MethodMapping> mergeMethodMappings(MethodMapping left, MethodMapping strictRight, MethodMapping looseRight, ClassMapping<?, ?> target, MergeContext context) {
        throw new IllegalStateException("Unexpectedly merged method: " + left);
    }

    @Override
    public MergeResult<MethodMapping> mergeDuplicateMethodMappings(MethodMapping left, MethodMapping strictRightDuplicate, MethodMapping looseRightDuplicate, MethodMapping strictRightContinuation, MethodMapping looseRightContinuation, ClassMapping<?, ?> target, MergeContext context) {
        return new MergeResult<>(target.createMethodMapping(left.getSignature(), left.getObfuscatedName()));
    }

    @Override
    public FieldMapping addRightFieldMapping(FieldMapping right, ClassMapping<?, ?> target, MergeContext context) {
        // disable mojang member mappings
        return null;
    }

    @Override
    public MergeResult<MethodMapping> addRightMethodMapping(MethodMapping right, ClassMapping<?, ?> target, MergeContext context) {
        // disable mojang member mappings
        return new MergeResult<>(null);
    }

    private String prependPackage(String name) {
        if (name.contains("/")) {
            return name;
        } else {
           return "net/minecraft/server/" + name;
        }
    }
}

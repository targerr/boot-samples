package com.mamba.common.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树形结构构建工具类
 */
public class TreeUtil {

    private TreeUtil() {
    }

    /**
     * 构建树形结构（根节点 parentId == 0）
     *
     * @param list        数据列表
     * @param getId       获取ID的方法
     * @param getParentId 获取父ID的方法
     * @param setChildren 设置子节点的方法
     * @param <T>         节点类型
     * @param <C>         子节点类型
     * @return 树形结构列表
     */
    public static <T, C> List<T> buildTree(List<T> list,
                                           Function<T, Integer> getId,
                                           Function<T, Integer> getParentId,
                                           BiConsumer<T, List<T>> setChildren) {
        return buildTree(list, getId, getParentId, setChildren, 0);
    }

    /**
     * 构建树形结构（指定根节点 parentId）
     *
     * @param list        数据列表
     * @param getId       获取ID的方法
     * @param getParentId 获取父ID的方法
     * @param setChildren 设置子节点的方法
     * @param parentId    根节点父ID
     * @param <T>         节点类型
     * @param <C>         子节点类型
     * @return 树形结构列表
     */
    public static <T, C> List<T> buildTree(List<T> list,
                                           Function<T, Integer> getId,
                                           Function<T, Integer> getParentId,
                                           BiConsumer<T, List<T>> setChildren,
                                           Integer parentId) {
        // 按 parentId 分组
        Map<Integer, List<T>> childrenMap = list.stream()
                .collect(Collectors.groupingBy(getParentId));

        // 过滤出根节点
        List<T> roots = list.stream()
                .filter(item -> parentId.equals(getParentId.apply(item)))
                .collect(Collectors.toList());

        // 递归设置子节点
        roots.forEach(root -> setChildren.accept(root, findChildren(root, childrenMap, getId, setChildren)));

        return roots;
    }

    /**
     * 递归查找子节点
     */
    private static <T> List<T> findChildren(T parent,
                                            Map<Integer, List<T>> childrenMap,
                                            Function<T, Integer> getId,
                                            BiConsumer<T, List<T>> setChildren) {
        Integer parentId = getId.apply(parent);
        List<T> children = childrenMap.getOrDefault(parentId, new ArrayList<>());
        children.forEach(child -> setChildren.accept(child, findChildren(child, childrenMap, getId, setChildren)));
        return children;
    }

    /**
     * 构建树形结构并按 seq 排序（根节点 parentId == 0）
     *
     * @param list        数据列表
     * @param getId       获取ID的方法
     * @param getParentId 获取父ID的方法
     * @param setChildren 设置子节点的方法
     * @param getSeq      获取排序字段的方法
     * @param <T>         节点类型
     * @return 树形结构列表
     */
    public static <T> List<T> buildTreeSorted(List<T> list,
                                               Function<T, Integer> getId,
                                               Function<T, Integer> getParentId,
                                               BiConsumer<T, List<T>> setChildren,
                                               Function<T, Integer> getSeq) {
        return buildTreeSorted(list, getId, getParentId, setChildren, getSeq, 0);
    }

    /**
     * 构建树形结构并按 seq 排序（指定根节点 parentId）
     *
     * @param list        数据列表
     * @param getId       获取ID的方法
     * @param getParentId 获取父ID的方法
     * @param setChildren 设置子节点的方法
     * @param getSeq      获取排序字段的方法
     * @param parentId    根节点父ID
     * @param <T>         节点类型
     * @return 树形结构列表
     */
    public static <T> List<T> buildTreeSorted(List<T> list,
                                               Function<T, Integer> getId,
                                               Function<T, Integer> getParentId,
                                               BiConsumer<T, List<T>> setChildren,
                                               Function<T, Integer> getSeq,
                                               Integer parentId) {
        // 按 parentId 分组
        Map<Integer, List<T>> childrenMap = list.stream()
                .collect(Collectors.groupingBy(getParentId));

        // 过滤出根节点并按 seq 排序
        List<T> roots = list.stream()
                .filter(item -> parentId.equals(getParentId.apply(item)))
                .sorted(Comparator.comparingInt(item -> getSeq.apply(item) == null ? 0 : getSeq.apply(item)))
                .collect(Collectors.toList());

        // 递归设置子节点
        roots.forEach(root -> setChildren.accept(root, findChildrenSorted(root, childrenMap, getId, setChildren, getSeq)));

        return roots;
    }

    /**
     * 递归查找子节点并按 seq 排序
     */
    private static <T> List<T> findChildrenSorted(T parent,
                                                   Map<Integer, List<T>> childrenMap,
                                                   Function<T, Integer> getId,
                                                   BiConsumer<T, List<T>> setChildren,
                                                   Function<T, Integer> getSeq) {
        Integer parentId = getId.apply(parent);
        List<T> children = childrenMap.getOrDefault(parentId, new ArrayList<>())
                .stream()
                .sorted(Comparator.comparingInt(item -> getSeq.apply(item) == null ? 0 : getSeq.apply(item)))
                .collect(Collectors.toList());
        children.forEach(child -> setChildren.accept(child, findChildrenSorted(child, childrenMap, getId, setChildren, getSeq)));
        return children;
    }

}

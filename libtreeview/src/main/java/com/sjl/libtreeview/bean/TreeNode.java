package com.sjl.libtreeview.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点对象
 *
 * @author 林zero
 * @date 2018/1/14
 */

public class TreeNode<T extends LayoutItem> {
    /**
     * 当前节点值
     */
    private T value;
    /**
     * 父节点
     */
    private TreeNode parentNode;
    /**
     * 孩子节点列表
     */
    private List<TreeNode> childNodes = new ArrayList<>();
    /**
     * 是否已展开
     */
    private boolean expanded;
    /**
     * 是否被选中
     */
    private boolean checked;
    /**
     * 层级
     */
    private int level = 0;

    public TreeNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public TreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(TreeNode parentNode) {
        this.parentNode = parentNode;
    }

    public List<TreeNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<TreeNode> childNodes) {
        if (childNodes == null) {
            childNodes = new ArrayList<>();
        }
        for (int i = 0; i < childNodes.size(); i++) {
            childNodes.get(i).setParentNode(this);
        }
        initLevel(childNodes);
        this.childNodes = childNodes;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean toggle() {
        expanded = !expanded;
        return expanded;
    }

    public boolean open() {
        expanded = true;
        return expanded;
    }

    public boolean close() {
        expanded = false;
        return expanded;
    }

    public boolean isRoot() {
        return parentNode == null;
    }

    public boolean isLeaf() {
        return childNodes == null || childNodes.isEmpty();
    }

    public void addChild(TreeNode treeNode) {
        if (childNodes == null) {
            childNodes = new ArrayList<>();
        }
        treeNode.setParentNode(this);
        childNodes.add(treeNode);
        initLevel(childNodes);
    }

    private void initLevel(List<TreeNode> childNodes) {
        for (TreeNode item : childNodes) {
            item.setLevel(item.getParentNode().getLevel() + 1);
            if (!item.getChildNodes().isEmpty()) {
                initLevel(item.childNodes);
            }
        }

    }

    @Override
    public boolean equals(Object obj) {
        TreeNode treeNode = (TreeNode) obj;
        return value.equals(treeNode.getValue())
                && ((parentNode != null && parentNode.equals(treeNode.getParentNode())) || (parentNode == null && treeNode.getParentNode() == null))
                && childNodes.equals(treeNode.getChildNodes())
                && expanded == treeNode.isExpanded()
                && checked == treeNode.isChecked();
    }
}

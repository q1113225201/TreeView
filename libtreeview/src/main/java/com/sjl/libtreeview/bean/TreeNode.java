package com.sjl.libtreeview.bean;

import java.util.List;

/**
 * TreeNode
 *
 * @author 林zero
 * @date 2018/1/14
 */

public class TreeNode<T> {
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
    private List<TreeNode> childNodes;
    /**
     * 是否已展开
     */
    private boolean isExpanded;
    /**
     * 是否被选中
     */
    private boolean isSelected;

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
        for (int i=0;childNodes!=null&&i<childNodes.size();i++){
            childNodes.get(i).setParentNode(this);
        }
        this.childNodes = childNodes;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

package com.sjl.treeview.bean;

import com.sjl.libtreeview.bean.LayoutItem;
import com.sjl.treeview.R;

/**
 * BranchNode
 *
 * @author 林zero
 * @date 2018/1/14
 */

public class BranchNode implements LayoutItem {
    private String name;

    public BranchNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_branch;
    }

    @Override
    public int getToggleId() {
        return R.id.ivNode;
    }

    @Override
    public int getCheckedId() {
        return R.id.tvName;
    }
}

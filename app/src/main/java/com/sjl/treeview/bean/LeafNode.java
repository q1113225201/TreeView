package com.sjl.treeview.bean;

import com.sjl.libtreeview.bean.LayoutItem;
import com.sjl.treeview.R;

/**
 * LeafNode
 *
 * @author 林zero
 * @date 2018/1/14
 */

public class LeafNode implements LayoutItem {
    private String name;

    public LeafNode(String name) {
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
        return R.layout.item_leaf;
    }
}

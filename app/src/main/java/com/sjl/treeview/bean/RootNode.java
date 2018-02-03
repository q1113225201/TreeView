package com.sjl.treeview.bean;

import com.sjl.libtreeview.bean.LayoutItem;
import com.sjl.treeview.R;

/**
 * RootNode
 *
 * @author 林zero
 * @date 2018/1/14
 */

public class RootNode implements LayoutItem {
    private String name;

    public RootNode(String name) {
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
        return R.layout.item_root;
    }

    @Override
    public int getToggleId() {
        return R.id.ivNode;
    }

    @Override
    public int getCheckedId() {
        return R.id.ivCheck;
    }

    @Override
    public int getClickId() {
        return R.id.tvName;
    }
}

package com.sjl.treeview.bean;

import android.view.View;
import android.widget.TextView;

import com.sjl.libtreeview.bean.TreeNode;
import com.sjl.libtreeview.bean.TreeViewBinder;
import com.sjl.treeview.R;

/**
 * LeafViewBinder
 *
 * @author 林zero
 * @date 2018/1/17
 */

public class LeafViewBinder extends TreeViewBinder<LeafViewBinder.ViewHolder> {
    @Override
    public int getLayoutId() {
        return R.layout.item_leaf;
    }

    @Override
    public ViewHolder creatViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position, TreeNode treeNode) {
        ((TextView)holder.findViewById(R.id.tvName)).setText(((LeafNode)treeNode.getValue()).getName());
    }

    class ViewHolder extends TreeViewBinder.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}

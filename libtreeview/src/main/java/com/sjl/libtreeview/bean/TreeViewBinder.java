package com.sjl.libtreeview.bean;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * TreeViewBinder
 *
 * @author 林zero
 * @date 2018/1/16
 */
public abstract class TreeViewBinder<VH extends RecyclerView.ViewHolder> implements LayoutItem {
    public abstract VH creatViewHolder(View itemView);

    public abstract void bindViewHolder(VH holder, int position, TreeNode treeNode);

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public <T extends View> T findViewById(int id) {
            return itemView.findViewById(id);
        }
    }
}

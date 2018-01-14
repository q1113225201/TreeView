package com.sjl.treeview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sjl.libtreeview.BaseTreeViewAdapter;
import com.sjl.libtreeview.bean.TreeNode;
import com.sjl.treeview.R;
import com.sjl.treeview.bean.BranchNode;
import com.sjl.treeview.bean.LeafNode;
import com.sjl.treeview.bean.RootNode;

import java.util.List;

/**
 * TreeViewAdapter
 *
 * @author 林zero
 * @date 2018/1/14
 */

public class TreeViewAdapter extends BaseTreeViewAdapter<TreeViewAdapter.ViewHolder> {
    public TreeViewAdapter(Context mContext, List<TreeNode> list) {
        super(mContext, list);
    }

    @Override
    public ViewHolder onCreateTreeViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_root, parent, false));
    }

    @Override
    public void onBindTreeViewHolder(ViewHolder holder, int position) {
        TreeNode treeNode = expandedList.get(position);
        Object value = ((TreeNode<RootNode>) treeNode).getValue();
        String name = null;
        if (value instanceof RootNode) {
            name = ((RootNode) value).getName();
        } else if (value instanceof BranchNode) {
            name = ((BranchNode) value).getName();
        } else if (value instanceof LeafNode) {
            name = ((LeafNode) value).getName();
        }
        ((TextView) holder.findViewById(R.id.tvName)).setText(name);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> viewList;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            viewList = new SparseArray<>();
            this.itemView = itemView;
        }

        public View findViewById(int id) {
            View view = viewList.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                viewList.put(id, view);
            }
            return view;
        }
    }
}

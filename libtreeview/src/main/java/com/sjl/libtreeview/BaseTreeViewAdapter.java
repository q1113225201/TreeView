package com.sjl.libtreeview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.sjl.libtreeview.bean.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseTreeViewAdapter
 *
 * @author 林zero
 * @date 2018/1/14
 */

public abstract class BaseTreeViewAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    protected Context mContext;

    protected List<TreeNode> list;

    protected List<TreeNode> expandedList = new ArrayList<>();

    public BaseTreeViewAdapter(Context mContext, List<TreeNode> list) {
        this.mContext = mContext;
        this.list = list;
        buildExpandedList();
    }

    private void buildExpandedList() {
        expandedList.clear();
        insertNode(expandedList, list);
        Log.i("buildExpandedList", expandedList.size() + "");
    }

    private void insertNode(List<TreeNode> expandedList, List<TreeNode> list) {
        if (list == null) {
            return;
        }
        for (TreeNode item : list) {
            expandedList.add(item);
            if (item.isExpanded()) {
                //insertNode(expandedList, item.getChildNodes());
            }
            insertNode(expandedList, item.getChildNodes());
        }
    }

    /**
     * 同步数据
     */
    public void notifyData() {
        buildExpandedList();
        notifyDataSetChanged();
    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateTreeViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        onBindTreeViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return expandedList.size();
    }

    public abstract V onCreateTreeViewHolder(ViewGroup parent, int viewType);

    public abstract void onBindTreeViewHolder(V holder, int position);
}

package com.sjl.libtreeview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjl.libtreeview.bean.TreeNode;
import com.sjl.libtreeview.bean.TreeViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * TreeViewAdapter
 *
 * @author 林zero
 * @date 2018/1/14
 */
public class TreeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<TreeNode> expandedList = new ArrayList<>();
    private List<? extends TreeViewBinder> viewBinders = new ArrayList<>();
    private int padding = 30;

    public TreeViewAdapter(List<? extends TreeViewBinder> viewBinders) {
        this(null, viewBinders);
    }

    public TreeViewAdapter(List<TreeNode> list, List<? extends TreeViewBinder> viewBinders) {
        this.viewBinders = viewBinders;
        if (list != null) {
            buildExpandedList(list);
        }
    }

    /**
     * 构建显示的展开列表
     * @param list
     */
    private void buildExpandedList(List<TreeNode> list) {
        expandedList.clear();
        buildNodes(expandedList, list);
        Log.i("buildExpandedList", expandedList.size() + "");
    }

    /**
     * 构建节点
     * @param expandedList
     * @param list
     */
    private void buildNodes(List<TreeNode> expandedList, List<TreeNode> list) {
        if (list == null) {
            return;
        }
        for (TreeNode item : list) {
            expandedList.add(item);
            if (item.isExpanded()) {
                buildNodes(expandedList, item.getChildNodes());
            }
        }
    }

    /**
     * 同步数据
     */
    public void notifyData(List<TreeNode> list) {
        buildExpandedList(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return expandedList.get(position).getValue().getLayoutId();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        RecyclerView.ViewHolder viewHolder = viewBinders.get(0).creatViewHolder(view);
        for (TreeViewBinder item : viewBinders) {
            if (item.getLayoutId() == viewType) {
                viewHolder = item.creatViewHolder(view);
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setPadding(expandedList.get(position).getLevel() * padding, 0, 0, 0);
        ((TreeViewBinder.ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(holder.getLayoutPosition());
            }
        });
        for (TreeViewBinder item : viewBinders) {
            if (item.getLayoutId() == expandedList.get(position).getValue().getLayoutId()) {
                item.bindViewHolder(holder, position, expandedList.get(position));
            }
        }
    }

    /**
     * 节点展开收拢
     * @param position
     */
    public void toggle(int position) {
        TreeNode currentNode = expandedList.get(position);
        boolean isExpanded = currentNode.isExpanded();
        int startPosition = expandedList.indexOf(currentNode) + 1;
        if (isExpanded) {
            notifyItemRangeRemoved(startPosition, removeNodes(currentNode, true));
        } else {
            notifyItemRangeInserted(startPosition, insertNodes(currentNode, startPosition));
        }
    }

    /**
     * 收拢时移除节点
     * @param treeNode
     * @param toggle
     * @return
     */
    private int removeNodes(TreeNode treeNode, boolean toggle) {
        int count = 0;
        if (!treeNode.isLeaf()) {
            List<TreeNode> list = treeNode.getChildNodes();
            count += list.size();
            expandedList.removeAll(list);
            for (TreeNode item : list) {
                if (item.isExpanded()) {
                    item.toggle();
                }
                count += removeNodes(item, false);
            }
        }
        if (toggle) {
            treeNode.toggle();
        }
        return count;
    }

    /**
     * 展开时插入节点
     * @param treeNode
     * @param startPosition
     * @return
     */
    private int insertNodes(TreeNode treeNode, int startPosition) {
        List<TreeNode> list = treeNode.getChildNodes();
        int count = 0;
        for (TreeNode item : list) {
            expandedList.add(startPosition + count, item);
            count++;
            if (item.isExpanded()) {
                count += insertNodes(item, startPosition + count);
            }
        }
        if (!treeNode.isExpanded()) {
            treeNode.setExpanded(!treeNode.isExpanded());
        }
        return count;
    }

    @Override
    public int getItemCount() {
        return expandedList.size();
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}

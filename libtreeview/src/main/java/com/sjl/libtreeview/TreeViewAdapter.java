package com.sjl.libtreeview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjl.libtreeview.bean.TreeNode;
import com.sjl.libtreeview.bean.TreeViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形适配器
 *
 * @author 林zero
 * @date 2018/1/14
 */
public abstract class TreeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 比较时不同点的关键字
     */
    private static final String KEY_EXPAND = "expand";
    private static final String KEY_CHECK = "check";
    /**
     * 显示的展开列表
     */
    protected List<TreeNode> expandedList = new ArrayList<>();
    /**
     * 布局绑定列表
     */
    private List<? extends TreeViewBinder> viewBinders = new ArrayList<>();
    /**
     * 根据层级设置左边padding
     */
    private int padding = 30;
    /**
     * 改变子节点选中是否改变父节点选中状态
     */
    private boolean changeParentCheck = false;

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
     *
     * @param list
     */
    private void buildExpandedList(List<TreeNode> list) {
        expandedList.clear();
        buildNodes(expandedList, list);
    }

    /**
     * 构建节点
     *
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setPadding(expandedList.get(position).getLevel() * padding, 0, 0, 0);
        for (TreeViewBinder item : viewBinders) {
            if (item.getLayoutId() == expandedList.get(position).getValue().getLayoutId()) {
                item.bindViewHolder(holder, position, expandedList.get(position));
                if (item.getToggleId() != 0) {
                    //展开收拢状态切换
                    ((TreeViewBinder.ViewHolder) holder).findViewById(item.getToggleId()).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isOpen = toggle(expandedList.get(holder.getLayoutPosition()));
                            toggle(v, isOpen, expandedList.get(holder.getLayoutPosition()));
                        }
                    });
                }
                if (item.getCheckedId() != 0) {
                    //选中事件
                    ((TreeViewBinder.ViewHolder) holder).findViewById(item.getCheckedId()).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean checked = checked(expandedList.get(holder.getLayoutPosition()));
                            checked(v, checked, expandedList.get(holder.getLayoutPosition()));
                        }
                    });
                }
                if (item.getClickId() != 0) {
                    //单项点击事件
                    ((TreeViewBinder.ViewHolder) holder).findViewById(item.getClickId()).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            itemClick(v, expandedList.get(holder.getLayoutPosition()));
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && !payloads.isEmpty()) {
            Bundle bundle = (Bundle) payloads.get(0);
            TreeNode currentNode = expandedList.get(position);
            for (String key : bundle.keySet()) {
                if (KEY_EXPAND.equals(key) && currentNode.getValue().getToggleId() != 0) {
                    toggle(((TreeViewBinder.ViewHolder) holder).findViewById(currentNode.getValue().getToggleId()), bundle.getBoolean(key), currentNode);
                }
                if (KEY_CHECK.equals(key) && currentNode.getValue().getCheckedId() != 0) {
                    checked(((TreeViewBinder.ViewHolder) holder).findViewById(currentNode.getValue().getCheckedId()), bundle.getBoolean(key), currentNode);
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }

    /**
     * 改变当前节点、父节点和子节点的选中状态
     *
     * @param currentNode
     * @return
     */
    private boolean checked(TreeNode currentNode) {
        boolean isChecked = !currentNode.isChecked();
        int startPosition = expandedList.indexOf(currentNode);
        //选中子节点
        notifyItemRangeChanged(startPosition, checkChildren(currentNode, isChecked) + 1);
        //选中父节点
        TreeNode parentNode = currentNode.getParentNode();
        while (parentNode != null && changeParentCheck) {
            int index = expandedList.indexOf(parentNode);
            parentNode.setChecked(isChecked);
            notifyItemChanged(index);
            parentNode = parentNode.getParentNode();
        }
        return isChecked;
    }

    /**
     * 改变所有子节点选中状态
     *
     * @param treeNode
     * @param isChecked
     * @return
     */
    private int checkChildren(TreeNode treeNode, boolean isChecked) {
        treeNode.setChecked(isChecked);
        List<TreeNode> list = treeNode.getChildNodes();
        int count = treeNode.isExpanded() ? list.size() : 0;
        for (TreeNode item : list) {
            count += checkChildren(item, isChecked);
        }
        return count;
    }

    /**
     * 节点展开收拢
     *
     * @param currentNode
     */
    public boolean toggle(TreeNode currentNode) {
        boolean isExpanded = currentNode.isExpanded();
        int startPosition = expandedList.indexOf(currentNode) + 1;
        if (isExpanded) {
            notifyItemRangeRemoved(startPosition, removeNodes(currentNode, true, false));
        } else {
            notifyItemRangeInserted(startPosition, insertNodes(currentNode, startPosition, false));
        }
        return !isExpanded;
    }

    /**
     * 收拢时移除节点
     *
     * @param treeNode
     * @param toggle     需要改变当前结点展开收拢
     * @param isCloseAll 是否收拢全部
     * @return
     */
    private int removeNodes(TreeNode treeNode, boolean toggle, boolean isCloseAll) {
        int count = 0;
        if (!treeNode.isLeaf()) {
            List<TreeNode> list = treeNode.getChildNodes();
            count += list.size();
            expandedList.removeAll(list);
            for (TreeNode item : list) {
                if (item.isExpanded() && isCloseAll) {
                    //已展开并且是展开全部时，关闭
                    item.toggle();
                }
                if (item.isExpanded() || isCloseAll) {
                    //已展开或者是展开全部的时候，移除子节点
                    count += removeNodes(item, false, isCloseAll);
                }
            }
        }
        if (toggle) {
            treeNode.toggle();
        }
        return count;
    }

    /**
     * 展开时插入节点
     *
     * @param treeNode
     * @param startPosition
     * @param isOpenAll     是否展开全部
     * @return
     */
    private int insertNodes(TreeNode treeNode, int startPosition, boolean isOpenAll) {
        List<TreeNode> list = treeNode.getChildNodes();
        int count = 0;
        for (TreeNode item : list) {
            if (expandedList.indexOf(item) < 0) {
                //添加时先查看是否已添加，防止展开全部时重复添加
                expandedList.add(startPosition + count, item);
            }
            count++;
            if (item.isExpanded() || isOpenAll) {
                //已经展开或展开全部的时候
                count += insertNodes(item, startPosition + count, isOpenAll);
            }
        }
        if (!treeNode.isExpanded()) {
            treeNode.toggle();
        }
        return count;
    }

    /**
     * 关闭所有节点
     */
    public void closeAll() {
        List<TreeNode> cloneList = cloneList(expandedList);
        List<TreeNode> rootList = getRootList();
        for (TreeNode treeNode : rootList) {
            removeNodes(treeNode, treeNode.isExpanded(), true);
        }
//        notifyDataSetChanged();
        notifyDiff(cloneList);
    }

    /**
     * 展开所有节点
     */
    public void openAll() {
        List<TreeNode> cloneList = cloneList(expandedList);
        List<TreeNode> rootList = getRootList();
        for (TreeNode treeNode : rootList) {
            insertNodes(treeNode, expandedList.indexOf(treeNode) + 1, true);
        }
//        notifyDataSetChanged();
        notifyDiff(cloneList);
    }

    /**
     * 列表克隆
     * @param list
     * @return
     */
    private List<TreeNode> cloneList(List<TreeNode> list) {
        List<TreeNode> result = new ArrayList<>();
        for (TreeNode treeNode : list) {
            try {
                result.add(treeNode.clone());
            } catch (CloneNotSupportedException e) {
                result.add(treeNode);
            }
        }
        return result;
    }

    /**
     * 更新列表
     * @param oldList
     */
    private void notifyDiff(final List<TreeNode> oldList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return expandedList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                TreeNode oldTreeNode = oldList.get(oldItemPosition);
                TreeNode newTreeNode = expandedList.get(newItemPosition);
                return oldTreeNode.getValue() != null && oldTreeNode.getValue().equals(newTreeNode.getValue());
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                TreeNode oldTreeNode = oldList.get(oldItemPosition);
                TreeNode newTreeNode = expandedList.get(newItemPosition);
                return oldTreeNode.getValue() != null && oldTreeNode.getValue().equals(newTreeNode.getValue()) &&
                        oldTreeNode.isExpanded() == newTreeNode.isExpanded() &&
                        oldTreeNode.isChecked() == newTreeNode.isChecked();
            }

            @Nullable
            @Override
            public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                TreeNode oldTreeNode = oldList.get(oldItemPosition);
                TreeNode newTreeNode = expandedList.get(newItemPosition);
                Bundle bundle = new Bundle();
                if (oldTreeNode.isExpanded() != newTreeNode.isExpanded()) {
                    bundle.putBoolean(KEY_EXPAND, newTreeNode.isExpanded());
                }
                if (oldTreeNode.isChecked() != newTreeNode.isChecked()) {
                    bundle.putBoolean(KEY_CHECK, newTreeNode.isChecked());
                }
                if (bundle.size() == 0) {
                    bundle = null;
                }
                return bundle;
            }
        });
        diffResult.dispatchUpdatesTo(this);
    }

    /**
     * 获取跟节点列表
     *
     * @return
     */
    private List<TreeNode> getRootList() {
        List<TreeNode> rootList = new ArrayList<>();
        for (TreeNode treeNode : expandedList) {
            if (treeNode.isRoot()) {
                rootList.add(treeNode);
            }
        }
        return rootList;
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

    public boolean isChangeParentCheck() {
        return changeParentCheck;
    }

    public void setChangeParentCheck(boolean changeParentCheck) {
        this.changeParentCheck = changeParentCheck;
    }

    public abstract void toggle(View view, boolean isOpen, TreeNode treeNode);

    public abstract void checked(View view, boolean checked, TreeNode treeNode);

    public abstract void itemClick(View view, TreeNode treeNode);

}

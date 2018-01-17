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

    public TreeViewAdapter(List<? extends TreeViewBinder> viewBinders) {
        this(null,viewBinders);
    }

    public TreeViewAdapter(List<TreeNode> list, List<? extends TreeViewBinder> viewBinders) {
        this.viewBinders = viewBinders;
        if(list!=null) {
            buildExpandedList(list);
        }
    }

    private void buildExpandedList(List<TreeNode> list) {
        expandedList.clear();
        buildNodes(expandedList, list);
        Log.i("buildExpandedList", expandedList.size() + "");
    }

    private void buildNodes(List<TreeNode> expandedList, List<TreeNode> list) {
        if (list == null) {
            return;
        }
        for (TreeNode item : list) {
            expandedList.add(item);
            if (item.isExpanded()) {
//                buildNodes(expandedList, item.getChildNodes());
            }
            buildNodes(expandedList, item.getChildNodes());
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
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType,parent,false);
        RecyclerView.ViewHolder viewHolder = viewBinders.get(0).creatViewHolder(view);
        for (TreeViewBinder item:viewBinders){
            if(item.getLayoutId()==viewType){
                viewHolder = item.creatViewHolder(view);
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        for (TreeViewBinder item:viewBinders){
            if(item.getLayoutId()==expandedList.get(position).getValue().getLayoutId()){
                item.bindViewHolder(holder,position,expandedList.get(position));
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExpanded = expandedList.get(position).isExpanded();
                expandedList.get(position).setExpanded(!isExpanded);
                if(isExpanded){
                    notifyItemRangeRemoved(position,removeNodes(expandedList.get(position),position+1));
                }else{
                    notifyItemRangeInserted(position,insertNodes(expandedList.get(position),position+1));
                }
            }
        });
    }

    private int removeNodes(TreeNode treeNode, int i) {
        return 0;
    }

    private int insertNodes(TreeNode treeNode, int startPosition) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return expandedList.size();
    }

}

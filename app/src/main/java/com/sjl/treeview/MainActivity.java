package com.sjl.treeview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sjl.libtreeview.TreeViewAdapter;
import com.sjl.libtreeview.bean.LayoutItem;
import com.sjl.libtreeview.bean.TreeNode;
import com.sjl.treeview.bean.BranchNode;
import com.sjl.treeview.bean.BranchViewBinder;
import com.sjl.treeview.bean.LeafNode;
import com.sjl.treeview.bean.LeafViewBinder;
import com.sjl.treeview.bean.RootNode;
import com.sjl.treeview.bean.RootViewBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private RecyclerView rv;
    private List<TreeNode> list = new ArrayList<>();
    private TreeViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        rv = findViewById(R.id.rv);
        findViewById(R.id.btnLoad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                list.addAll(initRoot());
                adapter.notifyData(list);
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        adapter = new TreeViewAdapter(list, Arrays.asList(new RootViewBinder(), new BranchViewBinder(), new LeafViewBinder())) {
            @Override
            public void toggle(View view, TreeNode treeNode) {
                if(!(treeNode.getValue() instanceof LeafNode)) {
                    view.setRotation(view.getRotation() > 0 ? 0 : 90);
                }
            }

            @Override
            public void checked(View view, boolean checked, TreeNode treeNode) {
                //((TextView)view).setTextColor(view.getContext().getResources().getColor(checked?R.color.colorPrimary:R.color.colorAccent));
            }

            @Override
            public void itemClick(View view, TreeNode treeNode) {
                String name = null;
                LayoutItem item = treeNode.getValue();
                if(item instanceof RootNode){
                    name = ((RootNode) item).getName();
                }else if(item instanceof BranchNode){
                    name = ((BranchNode) item).getName();
                }else if(item instanceof LeafNode){
                    name = ((LeafNode) item).getName();
                }
                Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();
            }
        };
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 初始化跟
     * @return
     */
    private List<TreeNode> initRoot() {
        List<TreeNode> rootList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TreeNode<RootNode> node = new TreeNode<>(new RootNode("根" + i));
            if (i % 2 == 0) {
                node.setChildNodes(initBranchs(node.getValue().getName()));
            } else {
                node.setChildNodes(initLeaves(node.getValue().getName()));
            }
            rootList.add(node);
        }
        return rootList;
    }

    int count = 5;

    /**
     * 初始化枝
     * @param name
     * @return
     */
    private List<TreeNode> initBranchs(String name) {
        List<TreeNode> branchList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TreeNode<BranchNode> node = new TreeNode<>(new BranchNode(name + "枝" + i));
            if (i % 2 == 0) {
                node.setChildNodes(initLeaves(node.getValue().getName()));
            } else {
                if (count > 0) {
                    count--;
                    node.setChildNodes(initBranchs(node.getValue().getName()));
                } else {
                    node.setChildNodes(initLeaves(node.getValue().getName()));
                }
            }
            branchList.add(node);
        }
        return branchList;
    }

    /**
     * 初始化叶
     * @param name
     * @return
     */
    private List<TreeNode> initLeaves(String name) {
        List<TreeNode> leafList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TreeNode<LeafNode> node = new TreeNode<>(new LeafNode(name + "叶" + i));
            leafList.add(node);
        }
        return leafList;
    }
}

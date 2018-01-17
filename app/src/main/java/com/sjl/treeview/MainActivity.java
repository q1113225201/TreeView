package com.sjl.treeview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sjl.libtreeview.TreeViewAdapter;
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
                list.addAll(initTree());
                adapter.notifyData(list);
            }
        });
        initAdapter();
    }

    private void initAdapter() {
        Log.i(TAG, list.size() + "");
        adapter = new TreeViewAdapter(list,Arrays.asList(new RootViewBinder(),new BranchViewBinder(),new LeafViewBinder()));
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<TreeNode> initTree() {
        List<TreeNode> rootList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TreeNode<RootNode> node = new TreeNode<>(new RootNode("根" + i));
            node.setChildNodes(initBranchs(node, node.getValue().getName()));
            rootList.add(node);
            Log.i(TAG, "Tree" + i + i);
        }
        return rootList;
    }

    private List<TreeNode> initBranchs(TreeNode treeNode, String name) {
        List<TreeNode> branchList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TreeNode<BranchNode> node = new TreeNode<>(new BranchNode("枝" + i));
            node.setChildNodes(initLeaves(node, node.getValue().getName()));
            branchList.add(node);
            Log.i(TAG, name + i);
        }
        return branchList;
    }

    private List<TreeNode> initLeaves(TreeNode treeNode, String name) {
        List<TreeNode> leafList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TreeNode<LeafNode> node = new TreeNode<>(new LeafNode("叶" + i));
            leafList.add(node);
            Log.i(TAG, name + i);
        }
        return leafList;
    }
}

package com.sjl.treeview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

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
        findViewById(R.id.btnOpenAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.openAll();
            }
        });
        findViewById(R.id.btnCloseAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.closeAll();
            }
        });
        ((ToggleButton)findViewById(R.id.tbChoose)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.setChangeParentCheck(isChecked);
            }
        });

        TreeNode<RootNode> rootNode = new TreeNode<>(new RootNode("根节点"));
        TreeNode<BranchNode> branchNode1 = new TreeNode<>(new BranchNode("枝节点1"));
        TreeNode<BranchNode> branchNode2 = new TreeNode<>(new BranchNode("枝节点2"));
        TreeNode<LeafNode> leafNode1 = new TreeNode<>(new LeafNode("叶节点1"));
        TreeNode<LeafNode> leafNode2 = new TreeNode<>(new LeafNode("叶节点2"));
        TreeNode<LeafNode> leafNode3 = new TreeNode<>(new LeafNode("叶节点3"));

        rootNode.addChild(branchNode1);
        rootNode.addChild(branchNode2);
        branchNode1.addChild(leafNode1);
        branchNode2.addChild(leafNode2);
        branchNode2.addChild(leafNode3);
        list.add(rootNode);
        initAdapter();
    }

    private void initAdapter() {
        adapter = new TreeViewAdapter(list, Arrays.asList(new RootViewBinder(), new BranchViewBinder(), new LeafViewBinder())) {
            @Override
            public void toggle(View view, TreeNode treeNode) {
                if(!(treeNode.getValue() instanceof LeafNode)) {
                    //非叶节点时，旋转动画，具体效果自行添加
                    view.setRotation(view.getRotation() > 0 ? 0 : 90);
                }
            }

            @Override
            public void checked(View view, boolean checked, TreeNode treeNode) {

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

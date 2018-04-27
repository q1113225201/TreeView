# TreeView
这是一个功能多样的自定义树形列表，在使用RecyclerView实现树形列表，并在其基础上附加了一些额外功能。

**基本功能：**
- 展开收拢，收拢后保持子节点展开收拢状态
- 响应点击事件

**附加功能：**
- 自定义每个节点的布局，自定义每个节点所保存的数据
- 选中功能，响应选中事件，可设置选中子节点后是否选中父节点
- 全部展开（展开列表中数据，不响应节点展开事件）和全部收拢功能
- 将展开收拢细分成：
    - 点击展开收拢后事件响应toggleClick（可在此请求该节点下孩子节点数据，并添加到当前结点下）
    - 列表展开或收拢toggle
    - 展开后事件响应toggled（节点箭头旋转等）

## 添加依赖
仓库
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
([最新版本](https://github.com/q1113225201/TreeView/releases/latest))

```
compile 'com.github.q1113225201:TreeView:1.2.0'
```

具体使用可参考[博客](http://blog.csdn.net/q1113225201/article/details/79328247)或[项目中MainActivity.java](https://github.com/q1113225201/TreeView/blob/master/app/src/main/java/com/sjl/treeview/MainActivity.java)

## 效果展示
![url](show.gif)

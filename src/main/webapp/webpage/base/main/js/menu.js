/**
 * 
 */


function initComplete() {
	$.fn.zTree.init($("#treeDemo"), setting);
}

//菜单树懒加载配置
var setting = {
	async: {
        enable: true,
        // dataType: 'JSON',
        //返回的JSON数据的名字
        url: path + "app/functionController/menuTree",
        autoParam: ["id", "type", "rootId"],
        dataFilter: filter
    },
	callback: {
		onClick: onClick
	}
};

// 点击功能显示面包屑
function onClick(e, treeId, treeNode) {
	
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	//单击展开
	zTree.expandNode(treeNode);
	//出现进度条
	if (treeNode.url != null && treeNode.showProgess != false) {
		//showProgressBar();
		top.positionContent = "当前位置：" + treeNode.getParentNode().name + ">>" + treeNode.name;
		top.positionType = "normal";
	}

}

// 缓存应用的id与url前缀的对应关系，并缓存应用本身的应用前缀url，这样对于本工程应用，可以不指定前缀url（为null或空串）
var appUrlCache = {};
appUrlCache["self"] = path;

// 懒加载的树节点处理
function filter(treeId, parentNode, childNodes) {
	
	$.each(childNodes.obj, function(index, element) {
		// 对于应用节点，缓存url（前缀），并将节点本身的url与target重置为null，避免错误打开
		if (element.type == "app") {
			// 缓存
			appUrlCache[element.id] = element.url;
			element["target"] = null;
			element.url = null;
			
			return true;
		} else if (element.type == "func") {
			var appUrlPrefix = appUrlCache[element.rootId];
			if (appUrlPrefix == null || appUrlPrefix == "" || appUrlPrefix == undefined) {
				appUrlPrefix = appUrlCache["self"];
			}
			element.url = appUrlPrefix + element.url;
		}
		
		// 处理“初始化功能”的url，因为这个节点没有上级节点，也就无法通过应用节点获取路径前缀，只能 使用path变量
		if (parentNode == null && element.type == null) {
			element.url = path + element.url;
		}
		
		// 设置所有功能的弹出指定位置
		element["target"] = "frmright";
		
	});
	
	return childNodes.obj;
}

function customHeightSet(contentHeight){
	var windowWidth=document.documentElement.clientWidth;
	$("#scrollContent").width(windowWidth-4);
	$("#scrollContent").height(contentHeight-30);
}

function homeHandler(){
	var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
	treeObj.expandAll(false);
	top.positionType="none";
	jQuery.jCookie('leftTreeNodeId',"false");
}
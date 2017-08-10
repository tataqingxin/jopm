/**
 * html中有一棵隐藏的菜单树（tempTree），将这颗树的内容数据转换为首页菜单的数据需求格式使用
 * 这棵树是异步加载树，所以只能通过特殊的方法获取其中所有数据
 */

// tempTree 以及相关属性
var tempTreeObj;
var curStatus = "init", curAsyncCount = 0, asyncForAll = false, goAsync = false;

// 一级菜单
var firstLevelMenu = new Array();

//菜单树懒加载配置
var setting = {
	async: {
        enable: true,
        url: path + "app/functionController/menuTree",
        autoParam: ["id", "type", "rootId"],
        dataFilter: filter
    },
	callback: {
		beforeAsync: beforeAsync,
		onAsyncSuccess: zTreeOnAsyncSuccess
	}
};

// 首页加载framework.js报错，则使用这种方法
$(function() {
	tempTreeObj = $.fn.zTree.init($("#tempTree"), setting);
});

// 检查是否有节点正在加载
function check() {
	if (curAsyncCount > 0) {
		return false;
	}
	return true;
}

// 计数器：判断当前是否有节点正在加载子节点
function beforeAsync() {
	curAsyncCount++;
}

// 加载所有子节点
function asyncAll() {
	var zTree = $.fn.zTree.getZTreeObj("tempTree");
	if (asyncForAll) {
		// 已经加载所有子节点，不执行任何操作
	} else {
		asyncNodes(zTree.getNodes());
		if (!goAsync) {
			// 开始执行，重置当前状态（进行中）
			curStatus = "";
		}
	}
}

/**
 * 自递归方法
 * 遍历参数节点集合，如果发现父节点，则取出父节点中的所有子节点递归
 */
function asyncNodes(nodes) {
	if (!nodes) return;
	curStatus = "async";
	var zTree = $.fn.zTree.getZTreeObj("tempTree");
	for (var i=0, l=nodes.length; i<l; i++) {
		if (nodes[i].isParent && nodes[i].zAsync) {
			asyncNodes(nodes[i].children);
		} else {
			goAsync = true;
			zTree.reAsyncChildNodes(nodes[i], "refresh", true);
		}
	}
}

// 初始化加载完成后，延迟加载asyncAll方法
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	
	setTimeout(function() {
		asyncAll();
	}, 100);
	
	curAsyncCount--;
	if (curStatus == "async") {
		asyncNodes(treeNode.children);
	}

	if (curAsyncCount <= 0) {
		if (curStatus != "init" && curStatus != "") {
			transformMenuData();
			asyncForAll = true;
		}
		curStatus = "";
	}
	
}

function transformMenuData() {
	
	// 展开所有节点
	// tempTreeObj.expandNode(tempTreeObj.getNodesByParam("id", firstLevelMenu[0].id)[0], true, true, true, true);
	
	var nodeList = tempTreeObj.transformToArray(tempTreeObj.getNodes());
	
	$.each(nodeList, function(index, element) {
		if (element.rootId == null || element.rootId == "") {
			// root , awa application, drop it.
		} else {
			$.each(firstLevelMenu, function(index, obj) {
				if (obj.id == element.rootId) {
					
					// 针对菜单层级，对菜单样式进行特殊处理
					if (element.level == 1) {
						element["iconSkin"] = "diy01";
						element["icon"] = "skin/menuIcons/06.png";
					} else {
						element["iconOpen"] = path + "libs/icons/tree_close.gif";
						element["iconClose"] = path + "libs/icons/tree_open.gif";
						element["icon"] = "skin/titlebar_arrow.gif";
					}
					
					// 将菜单放到对应的以及菜单treeNodes集合下
					obj["treeNodes"].push(element);
				}
			});
		}
	});
	
	createTabH({"list": firstLevelMenu});
	changeLeftMenu(firstLevelMenu[0].id);
}

function getListData(firstLevelMenuId){
	var result = {};
	$.each(firstLevelMenu, function(index, obj) {
		if (obj.id == firstLevelMenuId) {
			result["treeNodes"] = obj["treeNodes"];
		}
	});
	return result;
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
			
			// construction of first level menu
			var node = {"id" : element.id, "name" : element.name, "treeNodes": []};
			firstLevelMenu.push(node);
			
			return true;
		} else if (element.type == "func") {
			var appUrlPrefix = appUrlCache[element.rootId];
			if (element.url.indexOf("http://")!=-1){
				appUrlPrefix = "";
			} else {
				if (appUrlPrefix == null || appUrlPrefix == "" || appUrlPrefix == undefined) {
					appUrlPrefix = appUrlCache["self"];
				}
			}
			
			element.url = appUrlPrefix + element.url;
		}
		
		// 处理“初始化功能”的url，因为这个节点没有上级节点，也就无法通过应用节点获取路径前缀，只能 使用path变量
		// TODO: 这个菜单需要被放到合适的位置上
		if (parentNode == null && element.type == null) {
			element.url = path + element.url;
		}
		
		element.showProgress = false;
		
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


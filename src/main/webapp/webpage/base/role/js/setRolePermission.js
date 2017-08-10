/**
 * 由于页面内容是懒加载的，所以后台向前台传递了角色已有的权限信息（功能、操作的id集合），
 * 在选中节点时，将会修改已有的权限数据集合，保存权限时，提交集合内容。
 */

/**
 * 角色绑定的功能ID集合
 */
var checkedFunctionIdArray = new Array();

/**
 * 角色绑定的操作ID集合
 */
var checkedOperationIdArray = new Array();


/**
 * 初始化执行方法
 */
function initComplete() {

}

/**
 * 渲染页面方法
 * 父页面调用该方法，向本页面赋值
 * @param roleId 角色ID
 * @param data 数据集合
 */
function wrapData(roleId, data,orgId) {
	
	if (roleId == null || roleId == "") {
		quiAlertDialog("非法操作，请刷新后重试", function(){});
		return;
	}
	
	$("#roleId").val(roleId);
	$("#orgId").val(orgId);
	
	checkedFunctionIdArray = data.attributes.checkedFunctionIds;
	checkedOperationIdArray = data.attributes.checkedOperationIds;
	
	// 设置懒加载、复选框及其回调
	var setting = {
		check: {
			enable: true,
			chkboxType:{"Y": "p", "N": "s"}	
		},
	    async: {
	        enable: true,
	        // dataType: 'JSON',
	        //返回的JSON数据的名字
	        url: path+"roleController/permissionTree",
	        autoParam: ["id", "type", "rootId"],
	        otherParam:{
	        	"roleId": $("#roleId").val(),
	        	"orgId": orgId
	        },
	        dataFilter: filter
	    },
	    callback: {
	    	onCheck: zTreeOnCheck
	    }
	};
	$.fn.zTree.init($("#rolePermissionTree"), setting, data.obj);
}

/**
 * 节点选中时执行的方法
 * 
 * @param event
 * @param treeId
 * @param treeNode选中的节点
 */
function zTreeOnCheck(event, treeId, treeNode) {
	
	if (treeNode.checked == true) {
		if (treeNode.type == "func") {
			if ($.inArray(treeNode.id, checkedFunctionIdArray) == -1) {
				checkedFunctionIdArray.push(treeNode.id);
			}
		} else if (treeNode.type == "oper") {
			if ($.inArray(treeNode.id, checkedOperationIdArray) == -1) {
				checkedOperationIdArray.push(treeNode.id);
			}
		}
	} else {
		if (treeNode.type == "func") {
			var idx = $.inArray(treeNode.id, checkedFunctionIdArray);
			if (idx != -1) {
				checkedFunctionIdArray.splice(idx, 1);
			}
		} else if (treeNode.type == "oper") {
			var idx = $.inArray(treeNode.id, checkedOperationIdArray);
			if (idx != -1) {
				checkedOperationIdArray.splice(idx, 1);
			}
		}
	}
	
}

/**
 * 懒加载展开节点时，对返回的数据进行分析，并设置其选中属性
 * @param treeId
 * @param parentNode
 * @param childNodes 懒加载调用返回的数据
 * @returns 子节点集合
 */
function filter(treeId, parentNode, childNodes) {
	var list = childNodes.obj;
	$.each(list, function(index, element) {
		if (element.type == "func") {
			if ($.inArray(element.id, checkedFunctionIdArray) != -1) {
				element["checked"] = true;
				return true;
			}
		} else if (element.type == "oper") {
			if ($.inArray(element.id, checkedOperationIdArray) != -1) {
				element["checked"] = true;
				return true;
			}
		}
	});
    return list; 
}

/**
 * 保存权限方法
 */
function save() {
	
	
	$.ajax({
		url : path+"roleController/setRolePermission",
		type: "POST",
		data: {
			id: $("#roleId").val(),
			functionIds: checkedFunctionIdArray.join(","),
			operationIds: checkedOperationIdArray.join(",")
		},
		success : function(data) {
			if (data == null || data == "") {
				quiAlertDialog("保存失败，请刷新后重试", function(){});
				return;
			}
			if (typeof(data) == "string") data = eval("(" + data + ")");
			var result = data.success;
			if (result == true) {
				quiAlertDialog("保存成功", function(){
					parent.closeMyDialog();
				});
				return;
			} else {
				quiAlertDialog("保存失败，" + data.message);
			}
		},
		error: function(err) {
			quiAlertDialog("err");
		}
	});
	
}
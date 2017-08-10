/**
 * 
 */

function initComplete() {

}

function go() {

}

function wrapData(tagId, data) {
	
	if (tagId == null || tagId == "") {
		quiAlertDialog("非法操作，请刷新后重试", function(){});
		return;
	}
	
	$("#tagId").val(tagId);
	
	var settings = {
		check: {
			enable: true
		}
	};
	
	$.fn.zTree.init($("#roleTagTree"), settings, data);
}

function addUser(treeId, treeNode) {
	
}

function save() {
	
	var roleIds = new Array();
	
	// var $checks = $("input:checkbox[name='checkUser']:checked");

	var treeObj = $.fn.zTree.getZTreeObj("roleTagTree");
	var roleTagTree = treeObj.getCheckedNodes();

	
	$.each(roleTagTree, function(index, element) {
		roleIds.push(element.id);
	});
	
	var roleIdStr = roleIds.join(",");
	
	$.ajax({
		url : path+"tagController/setRoleTag",
		type: "POST",
		data: {
			tagId: $("#tagId").val(),
			roleIds: roleIdStr
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
			alert("err");
		}
	});
	
}
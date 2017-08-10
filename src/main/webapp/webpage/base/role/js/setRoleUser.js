/**
 * 
 */

function initComplete() {

}

function go() {
	parent.closeMyDialog();
	return;
}

function wrapData(roleId, data) {
	
	if (roleId == null || roleId == "") {
		quiAlertDialog("非法操作，请刷新后重试", function(){});
		return;
	}
	
	$("#roleId").val(roleId);
	
	var settings = {
		check: {
			enable: true
		}
	};
	
	$.fn.zTree.init($("#roleUserTree"), settings, data);
}

function addUser(treeId, treeNode) {
	var aObj = $("#" + treeNode.tId + "_a");
	if ($("#diyBtn_" + treeNode.id).length > 0) return;
	
	var editStr = "[";
	var userList = treeNode.userList;
	try {
		$.each(userList, function(index, user) {
			editStr += "<input type='checkbox' name='checkUser' id='" + user.userId + "'";
			if (user.checked == true) {
				editStr += " checked ";
			}
			editStr += "/>" + user.userRealName;
			if (index != userList.length - 1) {
				editStr += "; ";
			}
		});
		editStr = editStr.subStr(0, editStr.length - 2);
	} catch (e) {
		// do nothing.
	}
	editStr += "]";
	aObj.append(editStr);
}

function save() {
	
	var userIds = new Array();
	
	// var $checks = $("input:checkbox[name='checkUser']:checked");

	var treeObj = $.fn.zTree.getZTreeObj("roleUserTree");
	var roleUserTree = treeObj.getCheckedNodes();

	
	$.each(roleUserTree, function(index, element) {
		userIds.push(element.id);
	});
	
	var userIdStr = userIds.join(",");
	
	$.ajax({
		url : path+"roleController/setRoleUser",
		type: "POST",
		data: {
			id: $("#roleId").val(),
			userIds: userIdStr
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
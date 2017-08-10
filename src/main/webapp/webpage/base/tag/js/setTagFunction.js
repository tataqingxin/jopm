/**
 * 
 */

function initComplete() {

}

function go() {

}

function wrapData(functionId, data) {
	
	if (functionId == null || functionId == "") {
		quiAlertDialog("非法操作，请刷新后重试", function(){});
		return;
	}
	
	$("#functionId").val(functionId);
	
	var settings = {
		check: {
			enable: true
		}
	};
	
	$.fn.zTree.init($("#tagFunctionTree"), settings, data);
}

function addUser(treeId, treeNode) {
	
}

function save() {
	var tagIds = new Array();
	
	// var $checks = $("input:checkbox[name='checkUser']:checked");

	var treeObj = $.fn.zTree.getZTreeObj("tagFunctionTree");
	var tagFunctionTree = treeObj.getCheckedNodes();

	
	$.each(tagFunctionTree, function(index, element) {
		tagIds.push(element.id);
	});
	
	var tagIdStr = tagIds.join(",");
	
	$.ajax({
		url : path+"tagController/setTagFunction",
		type: "POST",
		data: {
			functionId: $("#functionId").val(),
			tagIds: tagIdStr
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
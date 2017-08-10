//初始化grid，selectTree，tree
function initComplete(){
	getUserInfo();
}

//修改用户
function getUserInfo(){
	var result = getUserById();
	var userMsg = result.obj;
	var userOrgId = result.attributes.organizationId;
	var userDepartIdArray = result.attributes.departIds;
	userDepartIdArray.push(userOrgId);
	
	var treeData = getOrgDepartTreeData(userOrgId, userDepartIdArray);
	// 渲染机构部门树
	$("#department").data("data", treeData);
	$("#department").render();
	var x = "";
	for (var i=0;i<userMsg.userName.length-2;i++)
	{
		x = x + "*";
	}
	// 树渲染完成后，为其设置默认选择的值
	$("#department").setValue(userDepartIdArray.join(","));
	
	// 填充值
	$("#userName").text(userMsg.userName.replace(userMsg.userName.substring(1,userMsg.userName.length-1),x));
	$("#realName").text(userMsg.realName);
	if(0==userMsg.sex){
		$("#sex").text("女性");
	}else{
		$("#sex").text("男性");
	}
	$("#phone").text(userMsg.phone);
	$("#address").text(userMsg.address);
	$("#description").text(userMsg.description);
				
}

function getUserById() {
	
	var msg = null;
	
	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		type: "POST",
		url: path + "webosController/getUserInfo",
		// dataType: "json",  // 在前端独立阶段有这句话时执行不正常
		success: function(data) {
			if (data == null || data == "") {
				quiAlertDialog("非法操作，请刷新后重试", function(){});
				return;
			}
			if (typeof(data) == "string") data = eval("(" + data + ")");
			msg = data;
		},
		error: function(err) {
			alert("err" + err);
		}
	});
	return msg;
}

/**
 * 机构与部门树方法，通过传递第二个参数，获得选中部门的效果
 * @param orgId 组织机构ID
 * @param departIdArray 部门ID集合
 * @returns ztree 数据集合，可以直接用来渲染一棵树
 */
function getOrgDepartTreeData(orgId, departIdArray) {
	var msg = null;
	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		type: "POST",
		url: path + "userController/departTree",
		data: {
			"organizationId" : orgId
		},
		// dataType: "json",  // 在前端独立阶段有这句话时执行不正常
		success: function(data) {
			if (data == null || data == "") {
				quiAlertDialog("非法操作，请刷新后重试", function(){});
				return;
			}
			if (typeof(data) == "string") data = eval("(" + data + ")");
			$.each(data.obj, function(index, element){
				// 设置根节点没有选择框（业务需要）
				if (element.parentId == null) {
					element.nocheck = true;
				} else {
					element.nocheck = false;
				}
				
				// 设置普通节点（部门）的选择状态
				if ($.inArray(element.id, departIdArray) != -1) {
					element.checked = true;
				} else {
					element.checked = false;
				}
			});
			msg = data.obj;
		},
		error: function(err) {
			alert("err" + err);
		}
	});
	
	return msg;
}
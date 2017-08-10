/**
 * 
 */

var datagrid = null;
var myDialog = null;

function initComplete() {
	// 下面datagrid需要添加param
	datagrid = $("#datagrid").quiGrid({

		columns : [
			{
				display : '角色名称',
				name : 'id',
				align : 'center',
				hide: true
			},
			{
				display : '角色名称',
				name : 'name',
				align : 'center',
				width : "20%"
			},
			{
				display : '角色编码',
				name : 'code',
				align : 'center',
				width : "20%"
			},
			{
				display : '角色类型',
				name : 'typeName',
				align : 'center',
				width : "20%"
			},
			{
				display : '操作',
				isAllowHide : false,
				align : 'center',
				width : "40%",
				render : function (rowdata, rowindex, value, column) {
					return '<div class="padding_top4 padding_left5">'
					 + '<span class="img_list hand" title="人员设置" onclick="setUser(\'' + rowdata.id + '\')"></span>'
					 + '<span class="img_edit hand" title="权限设置" onclick="setPermission(\'' + rowdata.id + '\')"></span>'
					 + '</div>';
				}
			}
		],
		rownumbers : true,
		checkbox : true,
		height : '100%',
		width : "100%",
		pageSize : 10,
		percentWidthMode : true,
		toolbar : {
			items : [
				{
					text : '新建',
					click : addRole,
					iconClass : 'icon_add'
				},
				{
					line : true
				},
				{
					text : '修改',
					click : editRole,
					iconClass : 'icon_edit'
				},
				{
					line : true
				},
				{
					text : '删除',
					click : deleteRole,
					iconClass : 'icon_delete'
				}
			]

		}

	});

}

$(function(){
	//处理组织机构树的选中
    $("#orgTree").bind("change",function(){
        if(!$(this).attr("relValue")){
            quiAlertDialog("请选择需要操作的组织机构");
            return;
        }else{
        	var orgId = $(this).attr("relValue");
        	$("#organizationId").val(orgId);
        	search();
        }
    }) 
    setTimeout(getSelectData,100); 
});

//默认选中根节点
function getSelectData(){
	var selectTree = $.fn.zTree.getZTreeObj("selectTree2_tree");
	var nodes = selectTree.getNodes();
	$("#orgTree").setValue(nodes[1].id);
	$("#selectTree3_input").val(nodes[1].name);
	$("#organizationId").val(nodes[1].id);
	search();
}

/**
 * 增加角色
 */
function addRole() {
	
	if ($("#organizationId").val() == "") {
		quiAlertDialog("请选择组织机构");
		return;
	}
	
	myDialog = quiDialog(
			{
				URL:path+"webpage/base/role/roleAddOrEdit.html",
				Title:"新建角色", 
				Height: 300
			},
			function() {
				getChildWin(myDialog).$("#organizationId").val($("#organizationId").val());
			}
		);
}

function closeMyDialog(){
	myDialog.close();
}

function closeAndRefreshMyDialog(){
	onDialogClose(myDialog);
}

/**
 * 编辑角色
 */
function editRole() {
	var rows = datagrid.getSelectedRows();
	if (rows.length != 1) {
		quiAlertDialog("请选择一条数据",function(){});
		return;
	}
	var id = rows[0].id;
	
	var roleData = getRoleById(id);
	
	/*var dialog = new Dialog();
	dialog.Title = "编辑角色";
	dialog.URL = "roleAddOrEdit.jsp";
	dialog.Height = 170;
	dialog.Onload = function() {
		// 这个方法没有被触发，可能是个问题
		dialog.innerFrame.contentWindow.wrapData(roleData);
	};
	dialog.CancelEvent = function() {
		dialog.close();
	};
	
	dialog.show();*/
	
	myDialog = quiDialog(
			{
				URL:path+"webpage/base/role/roleAddOrEdit.html",
				Title:"编辑角色", 
				Height: 300
			},
			function() {
				getChildWin(myDialog).wrapData(roleData);
			}
		);
}

function onDialogClose(dialog) {
    datagrid.loadData();
    myDialog.close();
}

function deleteRole() {
	var rows = datagrid.getSelectedRows();
	if (rows.length != 1) {
		quiAlertDialog("请选择一条数据",function(){});
		return;
	}
	var id = rows[0].id;
	quiConfirmDialog(
		"确认删除吗？",
		function(){
			// 确认按钮回调
			deleteRoleById(id);
		},
		function(){
			// 取消按钮回调
		}
	);
}

/**
 * 根据角色主键删除
 * @param id
 */
function deleteRoleById(id) {
	
	if (id == null || id == "") {
		quiAlertDialog("非法操作，请刷新后重试", function(){});
		return;
	}
	
	// 获取后台数据
	$.ajax({
		url: path + "roleController/deleteRole",
		type: "POST",
		data: {
			"id" : id
		},
		// dataType: "json",  // 在前端独立阶段有这句话时执行不正常
		success: function(data) {
			if (data == null || data == "") {
				quiAlertDialog("非法操作，请刷新后重试", function(){});
				return;
			}
			if (typeof(data) == "string") data = eval("(" + data + ")");
			var result = data.success;
			if (result == true) {
				quiAlertDialog("删除成功", function(){});
			} else {
				quiAlertDialog("操作失败 ，" + data.message, function(){});
			}
			datagrid.loadData();
		},
		error: function(err) {
			alert("err" + err);
		}
	});
}

/**
 * 根据ID获取角色基本信息
 * @TODO 修正方法的实现
 * @param id
 */
function getRoleById(id) {
	
	var msg = null;
	
	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		type: "POST",
		url: path + "roleController/detailRole",
		data: {
			"id" : id
		},
		// dataType: "json",  // 在前端独立阶段有这句话时执行不正常
		success: function(data) {
			if (data == null || data == "") {
				quiAlertDialog("非法操作，请刷新后重试", function(){});
				return;
			}
			if (typeof(data) == "string") data = eval("(" + data + ")");
			msg = data.obj;
		},
		error: function(err) {
			alert("err" + err);
		}
	});
	
	return msg;
}

/**
 * 列表查询
 */
function search() {
	var query = $("#queryForm").formToArray(); 
    datagrid.setOptions({
    	params : query,
    	url: path + "roleController/roleList"
    });
    

    //页号重置为1
    datagrid.setNewPage(1);
    
	//加载数据
    datagrid.loadData();
}

/**
 * 重置查询条件并查询
 */
function resetSearch() {
	$("#queryForm")[0].reset();
	search();
}

function setUser(roleId) {
	var data = getRoleUser(roleId);
	if(data){
		myDialog = quiDialog(
				{
					URL:path+"webpage/base/role/setRoleUser.html",
					Title:"人员设置",Width:500,Height:350
				},
				function() {
					getChildWin(myDialog).wrapData(roleId, data);
				}
			);
	}
}

function getRoleUser(roleId) {
	
	var msg = null;
	
	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		url: path + "roleController/userTree",
		type: "POST",
		data: {
			"roleId" : roleId,
			"organizationId": $("#organizationId").val()
		},
		dataType: "json",
		success: function(data) {
			if (data == null || data == "") {
				quiAlertDialog("获取角色用户数据出错，请刷新后重试");
				return;
			}
			if (!data.success) {
				quiAlertDialog(data.message);
				return;
			}
			msg = data.obj;
		},
		error: function(err) {
			alert("err" + err);
		}
	});
	
	return msg;
}

function setPermission(roleId) {
	
	var orgId = $("#organizationId").val();
	var data = getRolePermission(roleId);
	if (data) {
		
		myDialog = quiDialog(
				{
					URL:path+"webpage/base/role/setRolePermission.html",
					Title:"编辑角色权限"
				},
				function() {
					getChildWin(myDialog).wrapData(roleId, data, orgId);
				}
			);
	}
}

function getRolePermission(roleId) {
	var orgId = $("#organizationId").val();
	var result = null;
	
	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		url: path + "roleController/permissionTree",
		type: "POST",
		data: {
			"roleId" : roleId,
			"orgId" :orgId
		},
		dataType: "json",
		success: function(data) {
			if (data == null || data == "") {
				quiAlertDialog("获取角色权限数据出错，请刷新后重试");
				return;
			}
			if (!data.success) {
				quiAlertDialog("操作失败 ，" + data.message);
				return;
			}
			result = data;
		},
		error: function(err) {
			quiAlertDialog("err" + err);
		}
	});
	
	return result;
}


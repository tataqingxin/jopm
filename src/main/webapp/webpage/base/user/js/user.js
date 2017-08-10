var grid = null;
var g = null;
var userAddDialog = null; //新增用户窗口
var updPwdDialog = null;
var emailDialog = null;
//初始化grid，selectTree，tree
function initComplete(){
    $("#layout1").layout({ leftWidth: 200,topHeight: 100, allowLeftResize: false, allowRightResize: false}); 
    
    grid = $("#maingrid").quiGrid({
        columns:[
            { display: '用户名', name: 'userName',     align: 'left', width: "15%"},
            { display: '真实姓名', name: 'realName', align: 'left', width: "15%"},
            { display: '性别', name: 'sexName', align: 'left', width: "15%"},
            { display: '手机', name: 'phone', align: 'left', width: "15%"},
            { display: '状态', name: 'statusName', align: 'left', width:"15%"},
            { display: '操作', isAllowHide: false, align: 'left', width:"15%",
                render: function (rowdata, rowindex, value, column){
                    return '<div class="padding_top4 padding_left5">'
                        + '<span style="margin-left:20px;cursor:pointer" title="修改密码" onclick="updatePasswWord(\'' + rowdata.id+'\')">修改密码</span>'
                        + '</div>';
                }
            }
        ],
        url :path + "userController/getUserList",rownumbers:true,checkbox:true,
        height: '100%', width:"100%",pageSize:20,percentWidthMode:true,
        toolbar: {
            items: [
                {text: '新建', click: saveOrUpdateUser, iconClass: 'icon_export'},
                {line: true},
                {text: '编辑', click: editUser, iconClass: 'icon_export'},
                {line: true},
                {text: '删除', click: deleteUser, iconClass: 'icon_export'},
                {line: true},
                {text: '启用/停用', click: enableUser, iconClass: 'icon_export'},
                {line: true},
                {text: '重置密码', click: changePassword, iconClass: 'icon_export'},
                {line: true} 
            ]
        }
    });
}
//初始化机构选择树
$(function(){
	//绑定change事件
	$("#selectTree1").bind("change",function(){
        if(!$(this).attr("relValue")){
            quiAlertDialog("没有选择机构！");
            $("#orgId").val("");
            $("#departId").val("");
            $.fn.zTree.init($("#tree-1"), {});
            refreshUser($(this).attr("relValue"));
        }else{
        	refreshTree($(this).attr("relValue"));
        	refreshUser($(this).attr("relValue"));
        	$("#orgId").val($(this).attr("relValue"));
        	
        }
    });
	setTimeout(getSelectData,100); 
});

//默认选择第一条数据
function getSelectData(){
	var selectTree = $.fn.zTree.getZTreeObj("selectTree2_tree");
	var nodes = selectTree.getNodes();
	$("#selectTree1").setValue(nodes[1].id);
	$("#selectTree3_input").val(nodes[1].name);
	$("#orgId").val(nodes[1].id);
	refreshTree(nodes[1].id);
	refreshUser(nodes[1].id);
}

//点击选择机构或者初始化部门树，刷新部门树
function refreshTree(orgId){
	//部门树重新赋值
	$.fn.zTree.init($("#tree-1"), {
		async: {
        enable: true,
        dataType: 'JSON',
        //返回的JSON数据的名字
        dataName: '',
        url: path + "departmentController/departmentTree",
        autoParam: ["id", "name"],
        otherParam:{
        	"orgId": orgId
        },
        dataFilter: filter
    },
	callback: {
		onClick: zTreeSelect,
		onNodeCreated: onNodeCreated
	}})
	
}

//懒加载，点击“+”显示数据
function filter(treeId, parentNode, childNodes) {
	  childNodes=childNodes.obj;
  return childNodes; 
}

//选择机构时，刷新左边机构树，并默认选中第一个根节点
function onNodeCreated(event, treeId, treeNode){
	var treeObj = $.fn.zTree.getZTreeObj("tree-1");
	var nodes = treeObj.getNodes();
	treeObj.selectNode(nodes[0]);
}

//点击部门树时刷新数据
function zTreeSelect(event,treeId,treeNode){
	//刷新用户列表
	$("#departId").val(treeNode.id);
	refreshUser(treeNode.id);
}

//刷新用户列表
function refreshUser(departId){
	var query = null; 
	$("#departId").val(departId);
	var query = [{name:"orgId",value:departId}];
	grid.setOptions({params:query});
	//页号重置为1
	grid.setNewPage(1);
	//刷新表格数据 
	grid.loadData();
}


//新增用户
function saveOrUpdateUser() {
	
	// 获取当前选择的组织机构
	var orgId = $("#selectTree1").attr("relValue");
	
	// 获取选择的部门
	var departTreeObj = $.fn.zTree.getZTreeObj("tree-1");
	if (departTreeObj == null) {
		quiAlertDialog("请选择组织机构");
		return;
	}
	
	// 左侧树选中的机构或部门
	var checkedDepartNodes = departTreeObj.getSelectedNodes();
	
	if(checkedDepartNodes == null || checkedDepartNodes.length != 1){
		quiAlertDialog("请在左侧选择部门或机构");
		return;
	}else{
		
		// 获取左侧树选中的第一个节点（单选，仅有一个）
		var depart = checkedDepartNodes[0];
		var departId = depart.id;
		var departName = depart.name;
		
		// 创建部门ID集合，方便渲染弹出页面的部门树
		var departIdArray = new Array();
		
		// 左侧部门树上选中的节点如果是根节点，则节点ID为机构ID
		// 如果为机构ID，则不设置部门ID集合
//		if (departId != orgId) {
			departIdArray.push(departId);
//		}
		
		// 获取机构下的整棵部门树
		var treeData = getOrgDepartTreeData(orgId, departIdArray);
		
		userAddDialog = quiDialog({
				URL:path + "webpage/base/user/userInput.html",
				Title:"新建用户",
				Width:600,
				Height:400
			},
			function() {
				var curWin = getChildWin(userAddDialog);
				
				// 设置新增用户所在的组织机构
				curWin.$("#orgId").val(orgId);
				
				// 对于新增页面，需要通过这个隐藏域为用户选择默认所在的部门
				// this.innerFrame.contentWindow.$("#pageCheckedDepartId").val(departId);
				
				// 渲染机构部门树
				curWin.$("#department").data("data", treeData);
				curWin.$("#department").render();
				
				// 树渲染完成后，为其设置默认选择的值
				curWin.$("#department").setValue(departIdArray.join(","));
			}
		);
	}
}


//关闭新增窗口
function closeUserAddDialog(){
	userAddDialog.close();
	grid.loadData();
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

//重置密码
function changePassword(){
	var rowDatas = grid.getSelectedRows();
	if(rowDatas.length==0){
		quiAlertDialog("请选择一个用户修改密码！");
	}else{
		var ids=getSelectId(grid);
		$.ajax({
			url:path+"userController/resetPassWrod",
			datatype:"json",
			data:{"ids":ids},
			type:"post",
			success:function(data){
				var d = $.parseJSON(data);
				var result = d.success;
				if (result == true) {
					quiAlertDialog("重置成功");
				} else {
					quiAlertDialog("重置失败，"+result.message);
				};
			}
		});
	}
}
//用户列表刷新
function refresh(isUpdate){
    if(!isUpdate){
	    //重置排序
	    grid.options.sortName='usernName';
	    grid.options.sortOrder="desc";
	    //页号重置为1
        grid.setNewPage(1);
    }
    grid.loadData();
}

//查询用户
function searchHandler(){
	var orgId = $("#selectTree1").attr("relValue");
	var treeObj = $.fn.zTree.getZTreeObj("tree-1");
	if(treeObj==null){
		quiAlertDialog("请按机构进行筛选！",function(){});
	}else{
		var node = treeObj.getSelectedNodes();
		if(node.length==0||node.length>1){
			quiAlertDialog("请选择左侧单位树进行查询！",function(){});
		}else{
//			var query = [{name:"orgId",value:departmentId}];
//			grid.setOptions({params:query});
		    var query = $("#queryForm").formToArray(); 
			grid.setOptions({ params : query});
			//页号重置为1
			grid.setNewPage(1);
			//刷新表格数据 
			grid.loadData();
		}
	}
}

//重置查询条件
function resetSearch(){
	$("#queryForm")[0].reset();
	searchHandler();
}

//查看
function onView(rowid){
    top.Dialog.open({URL:"../../sample_html/layout/user-management-content2.jsp",Title:"查看",Width:500,Height:330});
}

//修改用户
function editUser(){
	var rows = grid.getSelectedRows();
	if (rows.length != 1) {
		quiAlertDialog("请选择一条数据",function(){});
		return;
	}
	var id = rows[0].id;
	
	var result = getUserById(id);
	var userMsg = result.obj;
	var userOrgId = result.attributes.organizationId;
	var userDepartIdArray = result.attributes.departIds;
	userDepartIdArray.push(userOrgId);
	
	var treeData = getOrgDepartTreeData(userOrgId, userDepartIdArray);
	
	userAddDialog = quiDialog(
			{
				URL:path + "webpage/base/user/userInput.html",
				Title:"编辑用户",Width:600,Height:400
			},
			function() {
				var curWin = getChildWin(userAddDialog);
				// 渲染机构部门树
				curWin.$("#department").data("data", treeData);
				curWin.$("#department").render();
				
				// 树渲染完成后，为其设置默认选择的值
				curWin.$("#department").setValue(userDepartIdArray.join(","));
				
				// 填充值
				
				curWin.$("#id").val(id);
				if(id!=""){
					curWin.$("#passwordTr").html("");
					curWin.$("#cpasswordTr").html("");
				}
				curWin.$("#orgId").val(userOrgId);
				curWin.$("#userName").val(userMsg.userName);
				curWin.$("#realName").val(userMsg.realName);
				curWin.$("#sex").setValue(userMsg.sex);
				curWin.$("#phone").val(userMsg.phone);
				curWin.$("#address").val(userMsg.address);
				curWin.$("#description").val(userMsg.description);
				
			},
			function() {
				grid.loadData();
				userAddDialog.close();
			}
		);
}

function getUserById(userId) {
	
	var msg = null;
	
	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		type: "POST",
		url: path + "userController/user",
		data: {
			"id" : userId
		},
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

//删除用户
function deleteUser(){
	var rowDatas = grid.getCheckedRows()
	if(rowDatas.length==0 || rowDatas.length>1){
		quiAlertDialog("请选择一个用户删除！");
	}else{
		var ids=getSelectId(grid);
		quiConfirmDialog("确定要删除该记录吗？",function(){
			//删除记录
			$.post(path+"userController/deleteUser",
					{"ids":ids},
				function(result){
						quiAlertDialog(result.message);	
					refresh(true);
			},"json");
		});
	}
}

//获取选中行的id 格式为 id=1&id=2
function getSelectId(grid){
	var selectedRows = grid.getSelectedRows();
    var ids = "";
    for (i = 0; i < selectedRows.length; i++) {
        ids +=selectedRows[i].id+',';
    }
	if(ids!=""){
		ids = ids.substring(0, ids.lastIndexOf(",")); // 获取所有id
	}
    return ids;
}

//启用或停用用户
function enableUser(){
	var rowDatas = grid.getSelectedRows();
	if(rowDatas.length==0){
		quiAlertDialog("请选择用户进行启用或停用！");
	}else{
		var ids=getSelectId(grid);
		var message = "确定操作此功能吗！";
		quiConfirmDialog(message,function(){
    	    //启用或停用
	        $.post(path+"userController/updateStatus",
	            {"ids":ids},
	            function(result){
	            	if(result.success){
	            		quiAlertDialog(result.message);
	            		refresh(true);
	            	}else{
	            		quiAlertDialog(result.message);
	            		return;
	            	}
	            },"json");
	        //刷新表格数据
	    });
	}
}

function customHeightSet(contentHeight){
    $(".cusBoxContent").height(contentHeight-55);
    $(".orgTreeContainer").height(contentHeight-30);
}


function updatePasswWord(rowid){
	
	updPwdDialog = quiDialog(
			{
				 URL:path + "webpage/base/user/updatePasswWord.html",
				 Title:"修改密码",Width:400,Height:250,
			},
			function() {
				var curWin = getChildWin(updPwdDialog);
				curWin.document.getElementById('id').value=rowid;
				curWin.document.getElementById('passwordOld').value="";
			}
		);
}

function updPwdDialogClose(){
	updPwdDialog.close();
}


//新增用户
function saveUserForOfficePlatForm() {
	// 获取当前选择的组织机构
	var orgId = $("#selectTree1").attr("relValue");
	// 获取选择的部门
	var departTreeObj = $.fn.zTree.getZTreeObj("tree-1");
	if (departTreeObj == null) {
		quiAlertDialog("请选择组织机构",function(){});
		return;
	}
	
	// 左侧树选中的机构或部门
	var checkedDepartNodes = departTreeObj.getSelectedNodes();
	
	if(checkedDepartNodes == null || checkedDepartNodes.length != 1){
		quiAlertDialog("请在左侧选择部门或机构",function(){});
		return;
	}else{
		
		// 获取左侧树选中的第一个节点（单选，仅有一个）
		var depart = checkedDepartNodes[0];
		var departId = depart.id;
		var departName = depart.name;
		
		// 创建部门ID集合，方便渲染弹出页面的部门树
		var departIdArray = new Array();
		
		// 左侧部门树上选中的节点如果是根节点，则节点ID为机构ID
		// 如果为机构ID，则不设置部门ID集合
		if (departId != orgId) {
			departIdArray.push(departId);
		}
		
		// 获取机构下的整棵部门树
		var treeData = getOrgDepartTreeData(orgId, departIdArray);
		
		emailDialog = quiDialog(
				{
					URL:path + "webpage/base/user/userForEmail.html",
					Title:"新建用户",Width:600,Height:400,
				},
				function() {
					var curWin = getChildWin(emailDialog);

					
					// 设置新增用户所在的组织机构
					curWin.$("#orgId").val(orgId);
					
					// 对于新增页面，需要通过这个隐藏域为用户选择默认所在的部门
					// curWin.$("#pageCheckedDepartId").val(departId);
					
					// 渲染机构部门树
					curWin.$("#department").data("data", treeData);
					curWin.$("#department").render();
					
					// 树渲染完成后，为其设置默认选择的值
					curWin.$("#department").setValue(departIdArray.join(","));
				
				}
			);
	}
} 

function closeEmailDialog(){
	emailDialog.close();
}

function closeEmailAndRefreshDialog(){
	grid.loadData();
	emailDialog.close();
}

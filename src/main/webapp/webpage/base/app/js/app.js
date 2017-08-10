/**
 * Created by suenpeng on 2016/5/7.
 */
var grid=null;
var addDialog = null;
function initComplete(){
    //当提交表单刷新本页面时关闭弹窗
    grid = $("#dataBasic").quiGrid({
        columns:[
            { display: '主键', name: 'id',     align: 'center',hide:true},
            { display: '名称', name: 'name',     align: 'center', width: "10%"},
            { display: '注册码', name: 'instanceCode',    align: 'center', width: "15%"},
            { display: '图标', name: 'iconEntity.iconPath',    align: 'center', width: "10%",
            	render: function (rowdata, rowindex, value, column){
            		return "" == value ? value : '<img  height="26" width="26" src="'+path+''+value+'" /></img>';
                }},
                
            { display: '所属机构', align: 'center', width: "15%", 
                	render: function (rowdata, rowindex, value, column){
                		if (rowdata.organizationEntity == null) {
                			return "无";
                		} else {
                			return rowdata.organizationEntity.name;
                		}
                	}
            },
            { display: '分类', name: 'typeName',    align: 'center', width: "10%"},
            /*{ display: '状态', name: 'statusName',    align: 'center', width: "10%"},*/
            { display: '操作', isAllowHide: false, align: 'left', width:"30%",
                render: function (rowdata, rowindex, value, column){
                	var rowFunction = '<div class="padding_left5">'
                        + '<span style="margin-left:20px;cursor:pointer" title="服务接口" onclick="onServer(\'' + rowdata.id+'\','+rowindex + ')">服务接口</span>'
                        + '<span style="margin-left:20px;cursor:pointer" title="功能管理" onclick="onManage(\'' + rowdata.id+'\','+rowindex + ')">功能管理</span>';
                	if(null != rowdata.organizationEntity){
                		rowFunction += '<span style="margin-left:20px;cursor:pointer" title="机构开通" onclick="assortApp(\'' + rowdata.id+'\','+rowindex + ',\'' + rowdata.organizationEntity.id+'\')">机构开通</span>';
                	}else {
                		rowFunction += '<span style="margin-left:20px;cursor:pointer" title="机构开通" onclick="assortApp(\'' + rowdata.id+'\','+rowindex + ')">机构开通</span>';
                	}
//                	rowFunction += '<span style="margin-left:20px;cursor:pointer" title="开通策略" onclick="chooseStrategy(\'' + rowdata.id+'\','+rowindex + ')">开通策略</span>';
//                	rowFunction += '<span style="margin-left:20px;cursor:pointer" title="开通历史" onclick="getHistory(\'' + rowdata.id+'\','+rowindex + ')">开通策略</span>';
                	rowFunction += '<span style="margin-left:20px;cursor:pointer" title="注销" onclick="appCancel(\'' + rowdata.id+'\','+rowindex + ')">注销</span>';
                	rowFunction += '</div>';
                    return rowFunction;
                }
            }
        ],
        url:path+'app/applicationController/applicationList',rownumbers:true,checkbox:true,
        height: '100%', width:"100%",pageSize:10,percentWidthMode:true,
        toolbar:{
            items:[
                {text: '新建', click:appInput, iconClass: 'icon_add'},
                { line : true },
                {text: '编辑', click: appEdit, iconClass: 'icon_edit'},
                { line : true }
                /*
               ,{text: '删除', click: appDelete, iconClass: 'icon_delete'},
                { line : true },
                {text: '启用/禁用', click: appEnable, iconClass: 'icon_export'},
                { line : true }*/
            ]
        }
    });
}

$(function(){
	//机构选择绑定事件
//	$("#organazationTree").bind("change",function(){
//		if(!$(this).attr("relValue")){
//			$("#organizationId").val($(this).attr("relValue"));
//			quiAlertDialog("没有选择机构！");
//		}else{
//			$("#organizationId").val($(this).attr("relValue"));
//			queryData($(this).attr("relValue"));
//		}
//	});  
//	setTimeout(getSelectData,200); 
});
	
////默认选中根节点
//function getSelectData(){
//	var selectTree = $.fn.zTree.getZTreeObj("selectTree2_tree");
//	var nodes = selectTree.getNodes();
//	$("#organazationTree").setValue(nodes[1].id);
//	$("#selectTree3_input").val(nodes[1].name);
//	queryData(nodes[1].id);
//}

//查询应用数据
//function queryData(organizationId){
//	$("#organizationId").val(organizationId);
//	var query = null;
//	var query = [{name:"organizationId",value: organizationId}];
//	grid.setOptions({ params : query});
//	//页号重置为1
//	grid.setNewPage(1);
//	//刷新表格数据 
//	grid.loadData();
//}

//function chooseStrategy(rowid,rowidx){
//	addDialog = quiDialog(
//			{
//			  URL:path+"webpage/base/app/appStrategy.html",
//		      Title:"服务接口",Width:800,Height:600
//			},
//			function() {
//				var curWin = getChildWin(addDialog);
//				curWin.document.getElementById('applicationId').value=rowid;
//			}
//		);
//}

function getHistory(rowid,rowidx){
	addDialog = quiDialog(
			{
			  URL:path+"webpage/base/app/appHistory.html",
		      Title:"服务接口",Width:800,Height:600
			},
			function() {
				var curWin = getChildWin(addDialog);
				curWin.document.getElementById('applicationId').value=rowid;
			}
		);
}

//查询
function searchHandler(){
    var query = $("#queryForm").formToArray();
    grid.setOptions({ params : query});
    //页号重置为1
    grid.setNewPage(1);
    grid.loadData();//加载数据

}

//启用/禁用应用
function appEnable(){
	var rowDatas = grid.getCheckedRows();
    if(rowDatas.length==0||rowDatas.length>1){
        quiAlertDialog("请选择一条应用项进行操作！")
    }else{
    	var message =null;
    	if(rowDatas[0].status==0){
    		message = "确定要启用该应用吗？";
    	}else{
    		message = "确定要禁用该应用吗？";
    	}
    	quiConfirmDialog(message,function(){
            //删除记录
            $.post(path+"app/applicationController/enableApplicationStatus",
                {"id":rowDatas[0].id},
                function(result){
                    refresh();
                },"json");
            //刷新表格数据
        });
    }
}

//重置查询

function resetSearch(){
    $("#queryForm")[0].reset();
    $('#search').click();
}

//录入
function appInput(){
//	var organizationId = $("#organazationTree").attr("relValue");
//	if(organizationId!==""&&organizationId!=null){
//		
	addDialog = quiDialog(
			{
				URL:path+"webpage/base/app/appInput.html",
				Title:"新建",Width:500,Height:350
			},
			function() {
				var curWin = getChildWin(addDialog);
//				curWin.document.getElementById('organizationId').value=organizationId;
			},
			function() {
				searchHandler();
			}
		);
//	}else{
//		quiAlertDialog("请选择机构！");
//	}
}

function closeAddDialog(){
	addDialog.close();
}
//编辑
function  appEdit(){
	var organizationId = $("#organazationTree").attr("relValue");
    var rowDatas = grid.getCheckedRows();
    if(rowDatas.length==0||rowDatas.length>1){
        quiAlertDialog("请选择一条应用进行编辑！");
    }else{
    	
    	addDialog = quiDialog(
				{
					URL:path+"webpage/base/app/appInput.html",
		            Title:"编辑",Width:500,Height:350
				},
				function() {
					var curWin = getChildWin(addDialog);
					curWin.document.getElementById('organizationId').value=organizationId;
					curWin.document.getElementById('id').value=rowDatas[0].id;
					curWin.getData(rowDatas[0].id);
				},
				function() {
					searchHandler();
				}
			);
    }
}
//删除
function appDelete(){
	var rowDatas = grid.getCheckedRows();
    if(rowDatas.length==0||rowDatas.length>1){
        quiAlertDialog("请选择一条应用进行删除！");
    }else{
    	if(rowDatas[0].type==1){
    		quiAlertDialog("您无法删除系统应用！");
    	}else{
    		quiConfirmDialog("确定要删除该应用吗？",function(){
    			//删除记录
    			$.post(path+"app/applicationController/deleteApplication",
    					{"id":rowDatas[0].id},
    					function(result){
    						if(result.success){
    							quiAlertDialog(result.message);
    							refresh();
    						}else{
    							quiAlertDialog(result.message);
    						}
    					},"json");
    			//刷新表格数据
    		});
    	}
    }
    
}
//服务接口
function onServer(rowid,rowidx){
	addDialog = quiDialog(
			{
				URL:path+"webpage/base/app/appServer.html",
		      Title:"服务接口",Width:800,Height:600
			},
			function() {
				var curWin = getChildWin(addDialog);
				curWin.document.getElementById('applicationId').value=rowid;
				curWin.onloadData(rowid);
			}
		);
}

//功能管理
function onManage(rowid,rowidx){
	var data = getApplication(rowid);
    addDialog = quiDialog(
			{
				URL:path+"webpage/base/app/appFunction.html",
		      Title:"功能管理",Width:800,Height:600
			},
			function() {
				var curWin = getChildWin(addDialog);
				curWin.document.getElementById('applicationId').value=rowid;
				curWin.onloadData(rowid,data);
			}
		);
}

//开通机构
function assortApp(rowid,rowidx,orgId){
	var data = getOrgList(rowid);
	addDialog = quiDialog(
			{
				URL:path+"webpage/base/app/appMirror.html",
				Title:"机构开通",Width:600,Height:450
			},
			function() {
				var curWin = getChildWin(addDialog);
				curWin.wrapData(rowid, data,orgId);
			},
			function() {
				searchHandler();
			}
		);
}

//注销应用
function appCancel(rowid,rowidx){
	quiConfirmDialog("应用注销后，所有关联本应用的数据[功能、操作、资源、游客桌面、机构开通、角色权限]都将被删除，是否继续？",function(){
        //删除记录
        $.post(path+"appMirrorController/appMirrorCancel",
            {"appId":rowid},
            function(result){
                refresh();
            },"json");
        //刷新表格数据
    });
}

//获取已经被分配的机构列表
function getOrgList(appId){
	var result = null;
	
	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		url: path + "appMirrorController/getOrgList",
		type: "POST",
		data: {
			"appId" : appId
		},
		dataType: "json",
		success: function(data) {
			if (data == null || data == "") {
				quiAlertDialog("获取组织机构数据出错，请刷新后重试");
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

function getApplication(applicationId){
	var result = null;
	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		url: path + "app/functionController/getApplicationTreeNode",
		type: "POST",
		data: {
			"applicationId" : applicationId
		},
		success: function(data) {
			var d = $.parseJSON(data)
			result = d.obj;
		},
		error: function(err) {
			quiAlertDialog("err" + err);
		}
	});
	return result;
}

//刷新页面
function refresh(){
	grid.options.sortName='name';
	grid.options.sortOrder="desc";
	//页号重置为1
	grid.setNewPage(1);
	//重新加载数据
	grid.loadData();
}
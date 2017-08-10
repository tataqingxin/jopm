/**
 * Created by suenpeng on 2016/5/17.
 */
var grid = null;
var myDialog = null;
//初始化grid，selectTree，tree
function initComplete(){
    $("#layout1").layout({ leftWidth: 150,topHeight: 100,onEndResize:function(){
    }});
    //本地数据源
    grid = $("#dataBasic").quiGrid({
        columns:[
            { display: '主键', name: 'id',  align: 'center', width: "0%",hide:true},
            { display: '名称', name: 'name',     align: 'center', width: "15%"},
            { display: '编码', name: 'code', align: 'center', width: "15%"},
            { display: '标签', name: 'tagName', align: 'center', width: "15%"},
            { display: '图标', name: 'iconEntity.iconPath', align: 'center', width: "15%",
            	render: function (rowdata, rowindex, value, column){
            		return "" == value ? value : '<img  height="18" width="18" src="'+path+''+value+'" /></img>';
                }
            },
            /*{ display: '状态', name: 'statusName', align: 'center', width: "15%"},*/
            { display: '操作', isAllowHide: false, align: 'center', width:"40%",
                render: function (rowdata, rowindex, value, column){
                    return '<div class="padding_top4 padding_left5">'
                        + '<span style="margin-left:20px;cursor:pointer" title="资源维护" onclick="onRes(\'' + rowdata.id+'\','+rowindex + ')">资源维护</span>'
                        + '<span style="margin-left:20px;cursor:pointer" title="操作维护" onclick="onFunctionOperation(\'' + rowdata.id+'\','+rowindex + ')">操作维护</span>'
                        + '<span style="margin-left:20px;cursor:pointer" title="标签维护" onclick="onTag(\'' + rowdata.id+'\','+rowindex + ')">标签维护</span>'
                        + '</div>';
                }
            }
        ],
        
        url:path+"app/functionController/functionList",rownumbers:true,checkbox:true,
        height: '100%', width:"100%",pageSize:10,percentWidthMode:true,
        toolbar: {
            items: [
                {text: '新建', click: appManInput, iconClass: 'icon_add'},
                {line: true},
                {text: '编辑', click: appManEdit, iconClass: 'icon_edit'},
                {line: true},
                {text: '删除', click: appManDelete, iconClass: 'icon_delete'},
                {line: true}
                /*
                ,{text: '启用/停用', click: appManEnable, iconClass: 'icon_export'},
                {line: true}*/
            ]
        }
    });
}

//设置应用的全局变量，保存功能刷新树使用
var appData = null;
//更新树
function initFunctionTree(applicationId,data){
	appData = data;
	//部门树重新赋值
	$.fn.zTree.init($("#tree-1"), {async: {
	    enable: true,
	    dataType: 'JSON',
	    dataName: '',
	    //返回的JSON数据的名字
	    url: path+"app/functionController/functionTree",
	    autoParam: ["id","type"],
	    dataFilter: filter,
	    otherParam:{
	    	"applicationId":applicationId,
	    }
	},
	callback: {
		onClick: zTreeSelect
	}},data);
}

function filter(treeId, parentNode, childNodes) {
	  childNodes=childNodes.obj;
    return childNodes; 
}



//页面加载数据（供上一个列表页操作）
function onloadData(applicationId,data){
	initFunctionTree(applicationId,data);
	var treeObj = $.fn.zTree.getZTreeObj("tree-1");
	var nodes = treeObj.getNodes();
	treeObj.selectNode(nodes[0]);
	//设置查询参数
	var query = [{name:"applicationId",value: applicationId}];
	grid.setOptions({ params : query});
	//页号重置为1
	grid.setNewPage(1);
	//刷新表格数据 
	grid.loadData();
}
//录入
function appManInput(){
	var treeObj = $.fn.zTree.getZTreeObj("tree-1");
	var nodes = treeObj.getSelectedNodes();
	
	if(nodes.length!=1){
		quiAlertDialog("请选择功能操作！");
	}else{
		var applicationId = $("#applicationId").val();
		myDialog = quiDialog(
				{
					URL:path+"webpage/base/app/appFunctionInput.html",
					Title:"添加",Width:500,Height:350
				},
				function() {
					var curWin = getChildWin(myDialog);
					if(applicationId!=nodes[0].id){
						//父功能ID
						curWin.document.getElementById('functionId').value=nodes[0].id;
					}
					//所在应用ID
					curWin.document.getElementById('applicationId').value=applicationId;
					// 渲染资源树
				}
			);
	}
}



function closeMyDialog(){
	myDialog.close();
}

function closeAndRefreshMyDialog(){
	searchHandler();
	initFunctionTree(applicationId,appData);
	myDialog.close();
}


//编辑
function appManEdit(){
    var rowDatas = grid.getCheckedRows();
    var treeObj = $.fn.zTree.getZTreeObj("tree-1");
    if(rowDatas.length==0||rowDatas.length>1){
        quiAlertDialog("请选择一条功能操作项进行编辑！");
    }else{
    	var applicationId = $("#applicationId").val();
    	myDialog = quiDialog(
				{
					URL:path+"webpage/base/app/appFunctionInput.html",
		         Title:"编辑",Width:500,Height:350
				},
				function() {
					var curWin = getChildWin(myDialog);
					//功能ID
					curWin.document.getElementById('id').value=rowDatas[0].id;
	                //所在应用ID
					curWin.document.getElementById('applicationId').value=applicationId;
	                //调用编辑页面的getData方法
					curWin.getData(rowDatas[0].id);
				}
			);
    }
}
//删除
function appManDelete(){
	var rowDatas = grid.getCheckedRows();
	var applicationId = $("#applicationId").val();
	if(rowDatas.length==0||rowDatas.length>1){
		quiAlertDialog("请选择一条功能删除！",function(){});
	}else{
		quiConfirmDialog("确定要删除该记录吗？",function(){
			//删除记录
			$.post(path+"app/functionController/deleteFunction",
				{"id":rowDatas[0].id},
				function(result){
					if(result.success){
						quiAlertDialog(result.message);
						refresh();
						initFunctionTree(applicationId,appData);
					}else{
						quiAlertDialog(result.message);
						return;
					}
				},"json");
			//刷新表格数据
		});
	}
}
//启用停用
function appManEnable(){
	var rowDatas = grid.getCheckedRows();
    if(rowDatas.length==0||rowDatas.length>1){
        quiConfirmDialog("请选择一条功能操作项进行启用或禁用！");
    }else{
    	var message = null;
    	if(rowDatas[0].status==0){
    		message = "确定要启用该记录吗?"
    	}else{
    		message = "确定要禁用该记录吗?"
    	}
    	 quiConfirmDialog(message,function(){
    	    //启用或停用
	        $.post(path+"app/functionController/enableFunctionStatus",
	            {"id":rowDatas[0].id},
	            function(result){
	            	if(result.success){
	            		quiAlertDialog(result.message);
	            		refresh();
	            	}else{
	            		quiAlertDialog(result.message);
	            		return;
	            	}
	            },"json");
	        //刷新表格数据
	    });
    }
}
//资源维护
function onRes(rowid,rowidx){
	var applicationId = $("#applicationId").val();
	myDialog = quiDialog(
			{
				URL:path+"webpage/base/app/appFunctionRes.html",
		      Title:"新建",Width:600,Height:350
			},
			function() {
				var curWin = getChildWin(myDialog);
				curWin.document.getElementById('applicationId').value=applicationId;
				curWin.document.getElementById('functionId').value=rowid;
				curWin.onloadData(rowid,applicationId);
			}
		);
}

function closeAndRefreshResDialog(){
	searchHandler();
}

function closeResDialog(){
	myDialog.close();
}

//操作维护
function  onFunctionOperation(rowid,rowidx){
	var applicationId = $("#applicationId").val();
	var resRows = null;
	$.ajax({
		async:false,
    	data:{
    		"applicationId":applicationId,
    		"functionId":rowid
    	},
    	url: path+"app/resourceController/resourceList",
    	type:"post",
    	dataType:"json",
        //表单提交成功后的回调
        success: function(data){
        	resRows = data.rows;
        }
	});
	if(resRows.length>0){
		myDialog = quiDialog(
				{
					URL:path+"webpage/base/app/appFunctionOperation.html",
					Title:"添加",Width:600,Height:350
				},
				function() {
					var curWin = getChildWin(myDialog);
					curWin.document.getElementById('applicationId').value=applicationId;
					curWin.document.getElementById('functionId').value=rowid;
					curWin.onloadData(rowid,applicationId);
				}
			);
	}else{
		quiAlertDialog("请先添加该应用的资源！");
	}
}

//tree点击事件
function zTreeSelect(event,treeId,treeNode){
	var query = null;
	var applicationId = $("#applicationId").val();
	var query =null;
	if(treeNode.id==applicationId){
		query = [{name:"applicationId",value: applicationId}];
		$("#functionId").val("");
	}else{
		query = [{name:"applicationId",value: applicationId},{name:"functionId",value: treeNode.id}];
		$("#functionId").val(treeNode.id);
	}
	var url = path + "app/functionController/functionList";
	grid.setOptions({ params : query,url:url});
	//页号重置为1
	grid.setNewPage(1);
	//刷新表格数据 
	grid.loadData();
}

//查询
function searchHandler(){
	var query = $("#queryForm").formToArray();
    grid.setOptions({ params : query});
    //页号重置为1
    grid.setNewPage(1);
    grid.loadData();//加载数据
}

//重置查询条件
function resetSearch(){
	 $("#queryForm")[0].reset();
	 $('#searchBtn').click();
}

//刷新页面
function refresh(){
	//页号重置为1
	grid.setNewPage(1);
	//重新加载数据
	grid.loadData();
}

var functionId = "";

function onTag(rowid,rowidx){
	data = getTagFunction(rowid);
	functionId = rowid;
	var settings = {
			URL: "../tag/setTagFunction.html",
			Title: "",
			Width:500,
			Height:350
	};
	var dialog = quiDialog(settings, onloadCallBack, closeCallBack);
}

function getTagFunction(functionId) {
	var msg = null;

	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		url: path + "/tagController/tagTree",
		type: "POST",
		data: {
			"functionId" : functionId,
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

function onloadCallBack(dialog) {
	// 不要调用initComplete
	dialog.innerFrame.contentWindow.wrapData(functionId,data);
}

function closeCallBack(dialog) {
	grid.loadData();
	dialog.close();
}

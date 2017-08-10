/**
 * Created by suenpeng on 2016/5/7.
 */
var grid=null;
var myDialog = null;
function initComplete(){
    //当提交表单刷新本页面时关闭弹窗

    grid = $("#dataBasic").quiGrid({
        columns:[
            { display: '主键', name: 'id',     align: 'center', width: "25%",hide:true},
            { display: '名称', name: 'name',     align: 'center', width: "25%"},
            { display: '编码', name: 'code',    align: 'center', width: "25%"},
            { display: '图标', name: 'iconEntity.iconPath',    align: 'center', width: "25%",
            	render: function (rowdata, rowindex, value, column){
            		return "" == value ? value : '<img  height="18" width="18" src="'+path+''+value+'" /></img>';
            }},
            { display: '描述', name: 'description',    align: 'center', width: "25%"}
        ],
        url:path+"/app/operationController/operationList",rownumbers:true,checkbox:true,
        height: '100%', width:"100%",pageSize:10,percentWidthMode:true,
        toolbar:{
            items:[
                {text: '新建', click:orerationInput, iconClass: 'icon_add'},
                { line : true },
                {text: '编辑', click: orerationEdit, iconClass: 'icon_edit'},
                { line : true },
                {text: '删除', click: orerationDelete, iconClass: 'icon_delete'},
                { line : true }
            ]
        }
    });
}

function onloadData(functionId,applicationId){
	var query = null;
	query = [{name:"applicationId",value: applicationId},{name:"functionId",value: functionId}];
	grid.setOptions({ params : query});
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

//重置查询
function resetSearch(){
    $("#queryForm")[0].reset();
    $('#search').click();
}

//录入
function orerationInput(){
	var functionId = $("#functionId").val();
    var applicationId = $("#applicationId").val();
    var treeData = getResourceTree(functionId);
    myDialog = quiDialog(
    		{
    			URL:path+"webpage/base/app/appFunctionOperationInput.html",
    	        Title:"新建",Width:500,Height:350
    		},
    		function() {
    			var curWin = getChildWin(myDialog);
    			curWin.document.getElementById('applicationId').value=applicationId;
    			curWin.document.getElementById('functionId').value=functionId;
    			// 渲染资源树
    			curWin.$("#resource").data("data", treeData);
    			curWin.$("#resource").render();
    		}
    	);
}

function closeMyDialog(){
	myDialog.close();
}

function closeAndRefreshMyDialog(){
	searchHandler();
	myDialog.close();
}
//根据功能获取资源树
function getResourceTree(functionId){
	var msg = null;
	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		type: "POST",
		url: path + "app/resourceController/getResourceList",
		data: {
			"functionId" : functionId
		},
		// dataType: "json",  // 在前端独立阶段有这句话时执行不正常
		success: function(data) {
			if (data == null || data == "") {
				quiAlertDialog("非法操作，请刷新后重试");
				return;
			}
			if (typeof(data) == "string") data = eval("(" + data + ")");
			$.each(data.obj, function(index, element){
			// 设置根节点没有选择框（业务需要）
				element.name = element.url;
				element.url = null;
				// 设置普通节点（部门）的选择状态
			});
			msg = data.obj;
		},
		error: function(err) {
			alert("err" + err);
		}
	});
	return msg;
}
//编辑
function  orerationEdit(){
	var functionId = $("#functionId").val();
    var applicationId = $("#applicationId").val();
    var rowDatas = grid.getCheckedRows();
    var treeData = getResourceTree(functionId);
    if(rowDatas.length==0||rowDatas.length>1){
        quiAlertDialog("请选择一条操作进行编辑");
    }else{
    	 myDialog = quiDialog(
    	    		{
    	    			 URL:path+"webpage/base/app/appFunctionOperationInput.html",
    	    	            Title:"编辑",Width:500,Height:350
    	    		},
    	    		function() {
    	    			var curWin = getChildWin(myDialog);
    	    			curWin.document.getElementById('applicationId').value=applicationId;
    	    			curWin.document.getElementById('functionId').value=functionId;
    	                curWin.document.getElementById('id').value=rowDatas[0].id;
    	             // 渲染资源树
    	    			curWin.$("#resource").data("data", treeData);
    	    			curWin.$("#resource").render();
    	                //调用编辑页面的getData方法
    	            curWin.getData(rowDatas[0].id);
    	    		}
    	    	);
    }
}
//删除
function orerationDelete(){
    var rowDatas = grid.getCheckedRows();
    if(rowDatas.length==0||rowDatas.length>1){
        quiAlertDialog("请选择一条删除！");
    }else{
    	quiConfirmDialog("确定要删除该操作吗？",function(){
            //删除记录
            $.post(path+"app/operationController/deleteOperation",
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

function refresh(){
	//页号重置为1
    grid.setNewPage(1);
    //加载数据
    grid.loadData();
}
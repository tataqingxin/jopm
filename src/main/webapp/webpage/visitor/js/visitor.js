var grid=null;
var myDialog = null;
function initComplete(){
    //当提交表单刷新本页面时关闭弹窗
    grid = $("#dataBasic").quiGrid({
        columns:[
            { display: '主键', name: 'id',     align: 'left', width: "5%",hide:true},
            { display: '应用名称', name: 'applicationEntity.name',     align: 'center', width: "40%"},
            { display: '功能名称', name: 'functionEntity.name',    align: 'center', width: "40%"},
            { display: '操作', isAllowHide: false, align: 'center', width:"20%",
                render: function (rowdata, rowindex, value, column){
                	var rowFunction = '<div class="padding_left5">'
                        + '<span style="cursor:pointer" title="删除" onclick="visitorDelete(\'' + rowdata.id+'\')">删除</span>';
                	rowFunction += '</div>';
                    return rowFunction;
                }
            }
        ],
        url:path+"visitorController/visitorList",rownumbers:true,checkbox:false,
        height: '100%', width:"100%",pageSize:10,percentWidthMode:true,
        toolbar:{
            items:[
                {text: '新建', click:visitorAdd, iconClass: 'icon_add'},
                { line : true }
            ]
        }
    });
    
    getVisitorSwitch();
}

function getVisitorSwitch(){
	$.ajax({
		async: false, 
		url: path + "visitorController/getVisitorSwitch",
		type: "POST",
		dataType: "json",
		success: function(data) {
			$("#visitorSwitch").attr("relvalue",data.visitorSwitch);
			$("#visitorSwitch").render();
		}
	});
}
$(function(){
	$("#visitorSwitch").click(function(){
		var visitorSwitch = $("#visitorSwitch").attr("relValue");
//		var msg = "确认开启游客桌面么？";
//		alert(visitorSwitch);
//		if("1"==visitorSwitch){
//			msg = "确认关闭游客桌面么？";
//		}
//		quiConfirmDialog(msg,function(){
		$.ajax({
			async: false, 
			url: path + "visitorController/changeVisitorSwitch",
			type: "POST",
			data:{'visitorSwitch':visitorSwitch},
			dataType: "json",
			success: function(data) {
				$("#visitorSwitch").attr("relvalue",data.visitorSwitch);
				$("#visitorSwitch").render();
			}
		});
//		});
	});
})
//录入
function visitorAdd(){
	var data = getAppFunction();
	myDialog = quiDialog(
			{
				URL:path+"webpage/visitor/appFunction.html",
			    Title:"新建",Width:500,Height:350
			},function() {
				getChildWin(myDialog).wrapData(data);
			},
			function() {
				refresh();
			}
		);
}

//获取应用功能树
function getAppFunction(){
	var result = null;
	
	// 获取后台数据
	$.ajax({
		async: false, 
		url: path + "visitorController/appFunctionTree",
		type: "POST",
		dataType: "json",
		success: function(data) {
			if (data == null || data == "") {
				quiAlertDialog("获取游客功能数据出错，请刷新后重试");
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

function closeMyDialog(){
	myDialog.close();
	refresh();
}

//删除
function visitorDelete(rowId){
	quiConfirmDialog("确定要删除该游客功能吗？",function(){
        //删除记录
        $.post(path+"/visitorController/deleteVisitor",
            {"id":rowId},
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

function refresh(){
	//页号重置为1
	grid.setNewPage(1);
	//重新加载数据
	grid.loadData();
}
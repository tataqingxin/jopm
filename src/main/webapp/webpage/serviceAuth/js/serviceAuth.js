var grid = null;
var checkedAppArray = new Array();
function initComplete(){
	
	//应用分配表格
	grid = $("#dataBasic").quiGrid({
		columns:[
		         { display: '主键', name: 'id',     align: 'left', width: "30%",hide:true},
		         { display: '应用名称', name: 'name',     align: 'center', width: "50%"},
		         { display: '应用注册码', name: 'instanceCode',    align: 'center', width: "50%"}
		         ],
		         url: path+'serviceAuthController/applicationList', rownumbers:true,checkbox:true,
		         height: 'auto', width:"100%",usePager:false,percentWidthMode:true,
		         isChecked:checkedHandler
	});
}

$(function(){
	//应用服务树
	var setting = {
		async: {
			enable: true,
			dataType: 'JSON',
			//返回的JSON数据的名字
			url: path+"serviceAuthController/getAppServiceTree",
			autoParam: ["id", "type", "rootId"],
			dataFilter: filter
		},
		callback: {
			onClick: onNodeClick
		}
	};
	$.fn.zTree.init($("#appServiceTree"),setting);
});

function onNodeClick(event, treeId, treeNode){
	if(!treeNode.isParent){
		onSet(treeNode.id);
	}else{
		$("#serviceId").val("");
		searchHandler();
	}
}
//查询
function searchHandler(){
	 grid.setNewPage(1);
	//刷新表格数据 
	 grid.loadData();
}

//树的懒加载
function filter(treeId, parentNode, childNodes) {
	var list = childNodes.obj;
    return list; 
}

function checkedHandler(rowdata){
	var serviceId = $("#serviceId").val();
	if(null!=serviceId&&""!=serviceId){
		if ($.inArray(rowdata.id, checkedAppArray) == -1) {
			return false;
		}else{
			return true;
		}
	}else{
		return false;
	}
}

//设置参数
function onSet(serviceId){
	//刷新表格数据 
	$("#serviceId").val(serviceId);
	$.ajax({
		url:path+"serviceAuthController/getServiceAuthList",
		dataType:"json",
		type:"post",
		data:{"serviceId":serviceId},
		success:function(data){
			if(null!=data.attributes.checkedAppIds){
				checkedAppArray = data.attributes.checkedAppIds; 
			}
			
			//页号重置为1
			grid.setNewPage(1);
			//刷新表格数据 
			grid.loadData();
		}
	})
	
	
}

function saveServiceAuth(){
	var serviceId = $("#serviceId").val();
	var rowDatas = grid.getCheckedRows();
	var appIds = "";
	if(rowDatas.length==0){
		// quiAlertDialog("请选择应用下的服务进行操作！");
	}else{
		for(var i=0,j = rowDatas.length;i<j;i++){
			appIds+= rowDatas[i].id+",";
		}
		appIds = appIds.substring(0,appIds.lastIndexOf(","));
	}
	$.ajax({
		url:path+"serviceAuthController/insertServiceAuth",
		dataType:"json",
		type:"post",
		data:{"serviceId":serviceId,"applicationIds":appIds},
		success:function(data){
			if(data.success){
				onSet(serviceId);
			}
			quiAlertDialog(data.message);
		}
	})
	
}


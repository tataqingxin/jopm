var setting1 = {
    async: {
        enable: true,
        dataType: 'JSON',
        dataName: '',
        //返回的JSON数据的名字
        url: path+"organizationController/organizationTree",
        autoParam : [ "id", "name"],
        dataFilter: filter
    },
        callback: {
			onClick: zTreeSelect,
			onNodeCreated: onNodeCreated
		}
};

function filter(treeId, parentNode, childNodes) {
	  childNodes=childNodes.obj;
      return childNodes; 
}
function onNodeCreated(event, treeId, treeNode){
	var id = null;
	var treeObj = $.fn.zTree.getZTreeObj("tree-1");
	var nodes = treeObj.getNodes();
	var Selectednodes = treeObj.getSelectedNodes();
	if(Selectednodes[0]==null){
		treeObj.selectNode(nodes[0]);
		id = nodes[0].id;
	}else if(Selectednodes[0].id==nodes[0].id){
			treeObj.selectNode(nodes[0]);
			id = nodes[0].id;
	}else{
			id = Selectednodes[0].id;
		}
	$("#isEdit").hide();
	$(".l-layout-header:eq(1)").html("机构编辑");
	$.post(path+"organizationController/organization",
		{"id":id},
		function(result){
			$('#id').val(result.obj.id);
			$('#name').val(result.obj.name);
			$('#code').val(result.obj.code);
			$('#range').val(result.obj.range);
			$('#description').val(result.obj.description);
		},"json");
	//刷新右边儿树
	var url= path+"departmentController/departmentTree";

	$.post(url,
		{"orgId":id},
		function(result){
			$.fn.zTree.init($("#tree-2"), {},result.obj);
		},"json")
	
}



var g = null;
//初始化grid，selectTree，tree
function initComplete(){
    $("#layout1").layout({ leftWidth: 260,rightWidth: 200, allowLeftResize: false, allowRightResize: false}); 
    
    /*$("#layout1").layout({ leftWidth: 260,rightWidth: 200,onEndResize:function(){
        g.resetWidth();
    }});*/

    //本地数据源
    $.fn.zTree.init($("#tree-1"), setting1);
}


function saveData() {
	var valid = $('#myFormId').validationEngine({returnIsValid: true});
	if(valid){
    	$.ajax({
    		async: false,
    		url: path + "organizationController/saveOrUpdate",
    		type: "POST",
    		data: {
    			"id" : $("#id").val(),
    			"orgarizationId" : $("#orgarizationId").val(),
    			"name" : $("#name").val(),
    			"code" : $("#code").val(),
    			"range" : $("#range").val(),
    			"description" : $("#description").val()
    		},
    		// dataType: "json",  // 在前端独立阶段有这句话时执行不正常
    		success: function(data) {
    			if (data == null || data == "") {
    				quiAlertDialog("非法操作，请刷新后重试");
    				return;
    			}
    			if (typeof(data) == "string") data = eval("(" + data + ")");
    			var result = data.success;
    			if (result == true) {
    				quiAlertDialog("操作成功", function(){
    					refreshTree($("#id").val(),$("#name").val());
//        				clearDepartTree();
    				}); 
    			} else {
    				quiAlertDialog("操作失败，失败原因   " + data.message);
    			}
    		},
    		error: function(err) {
    			quiAlertDialog("err" + err.message);
    		}
    	});
	}
}



//点击机构，刷新部门树
function refreshTree(id,name){
	var treeObj = $.fn.zTree.getZTreeObj("tree-1");
	var nodes = treeObj.getSelectedNodes();
	if(id){
		nodes[0].name=name;
		treeObj.updateNode(nodes[0]);
	}else{
		if (nodes.length>0) {
			if(nodes[0].isParent==false){
				nodes[0].isParent=true;
				treeObj.updateNode(nodes[0]);
			}
			treeObj.reAsyncChildNodes(nodes[0], "refresh");
		}
	}
}

//点击部门或者机构树时刷新数据(编辑)
function zTreeSelect(event,treeId,treeNode){
	//机构编辑
	$("#isEdit").hide();
	$(".l-layout-header:eq(1)").html("机构编辑");
	$.post(path+"organizationController/organization",
		{"id":treeNode.id},
		function(result){
			$('#id').val(result.obj.id);
			$('#name').val(result.obj.name);
			$('#code').val(result.obj.code);
			$('#range').val(result.obj.range);
			$('#description').val(result.obj.description);
		},"json");
	//刷新右边儿树
	var url= path+"departmentController/departmentTree";

	$.post(url,
		{"orgId":treeNode.id},
		function(result){
			$.fn.zTree.init($("#tree-2"), {},result.obj);
		},"json");
	
}


//新增机构
function addOrganization(){
	clearForm();
	var node = $.fn.zTree.getZTreeObj("tree-1").getSelectedNodes();
	if(node.length==1){
		$("#orgarizationId").val(node[0].id)
		$("#orgarizationName").val(node[0].name)
	}else if(node.length==0){
		quiAlertDialog("请选择所在机构！");
	}
}
//清空form表单
function clearForm(){
	$("#isEdit").show();
	$("#isEdit td:eq(0)").css("width","45%");
	$(".l-layout-header:eq(1)").html("新建机构");
	$("#description").val("");
	$("#myFormId input").val("");
//	$("input[type='submit']").val("保存");
}

//删除机构
function delOrganization(){
	var treeObj = $.fn.zTree.getZTreeObj("tree-1");
	var node = treeObj.getSelectedNodes();
	
	/*treeObj.reAsyncChildNodes(node[0],"refresh");
	
	var childNodes = treeObj.getNodesByParam("parentId", node[0].id, node[0]);
	if (childNodes.length == 0) {
		node[0].isParent = false;
		treeObj.updateNode(node[0]);
	}*/
	
	if(node.length==0||node.length>1){
		quiAlertDialog("请选择要删除的部门！");
	}else if(node[0].isParent){
		quiAlertDialog("请先删除子机构！");
	}else{
		quiConfirmDialog("确定要删除该机构吗？",function(){
			//删除记录
		  	$.post(path+"organizationController/delOrganizationEntity",
	  			{"id":node[0].id},
	  			function(result){
	  				if(result.success){
	  					handleResult(result);
	  				}else{
	  					quiAlertDialog("删除失败，失败原因  [" + result.message+"]");
	  				}
				},"json");
		});
	}
}

function handleResult(data){
	var result = data.success;
	var parentNode = null;
	var childrenNodes = null
	if (result == true) {
		quiAlertDialog("删除成功", function(){
			var treeObj = $.fn.zTree.getZTreeObj("tree-1");
			var nodes = treeObj.getSelectedNodes();
			var parent = nodes[0].getParentNode();
			treeObj.removeNode(nodes[0]);
			treeObj.selectNode(parent);
			zTreeSelect("","",parent)
//			if (nodes.length>0) {
//				parentNode = nodes[0].getParentNode();
//				treeObj.reAsyncChildNodes(parentNode,"refreshnot");
//				childrenNodes = treeObj.getNodesByParam("parentId",parentNode.id,parentNode)
//				if(childrenNodes.length==0){
//					parentNode.isParent = false;
//					treeObj.updateNode(parentNode);
//					alert(parentNode.isParent);
//				}
//			}
		});
	} else {
		quiAlertDialog("删除失败，失败原因  [" + data.message+"]");
	}
}

function customHeightSet(contentHeight){
    $(".cusBoxContent").height(contentHeight-55)
    $(".orgTreeContainer").height(contentHeight-30);
}
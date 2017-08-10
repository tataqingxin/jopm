var g = null;
//初始化grid，selectTree，tree
var organizationId;
var departmentId;
function initComplete(){
    $("#layout1").layout({ leftWidth: 270,topHeight: 100, allowLeftResize: false});
    
    /*$("#layout1").layout({ leftWidth: 270,topHeight: 100,onEndResize:function(){
        g.resetWidth();
    }});*/
    initOrganizationTree();
}



//初始化机构树
function initOrganizationTree(){
	//绑定change事件
	$("#selectTree1").bind("change",function(){
        if(!$(this).attr("relValue")){
            quiAlertDialog("没有选择机构！");
            $("#orgId").val("");
            $("#departId").val("");
            $.fn.zTree.init($("#tree-1"), {});
        }else{
        	organizationId=$(this).attr("relValue");
        	refreshTree($(this).attr("relValue"));
        	queryData($(this).attr("relValue"));
        	$("#orgId").val($(this).attr("relValue"));
        }
    });
	//选中根节点事件
	setTimeout(getSelectData,200); 
}

//默认选中根节点
function getSelectData(){
	var selectTree = $.fn.zTree.getZTreeObj("selectTree2_tree");
	var nodes = selectTree.getNodes();
	$("#selectTree1").setValue(nodes[1].id);
	organizationId = nodes[1].id;
	$("#selectTree3_input").val(nodes[1].name);
	queryData(nodes[1].id);
	refreshTree(nodes[1].id);
}

function queryData(orgId){
	$.post(path+"organizationController/organization",
		{"id":orgId},
		function(result){
			$(".l-layout-header:eq(1)").html("");
			$("#isEdit").hide();
			$('#name').val(result.obj.name).attr("disabled","disabled");
			$('#code').val(result.obj.code).attr("disabled","disabled");
			$('#range').val(result.obj.range).attr("disabled","disabled");
			$("#submitBtn").hide();
			$('#description').val(result.obj.description).attr("disabled","disabled");
		},"json");
}

//点击机构，刷新部门树
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
		onNodeCreated: onNodeCreated,
		onAsyncSuccess: zTreeOnAsyncSuccess
	}});
}

function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	treeObj.expandNode(treeObj.getNodes()[0],true,false,true);
}

//懒加载展示子节点
function filter(treeId, parentNode, childNodes) {
	  childNodes=childNodes.obj;
    return childNodes; 
}

//左边部门树选中第一个根节点（机构）
function onNodeCreated(event, treeId, treeNode){
	var treeObj = $.fn.zTree.getZTreeObj("tree-1");
	var nodes = treeObj.getNodes();
	var selectedNodes = treeObj.getSelectedNodes();
	if(selectedNodes.length==0){
		treeObj.selectNode(nodes[0]);
	}else{
		treeObj.selectNode(selectedNodes[0]);
	}
}

//点击部门或者机构树时刷新数据(编辑)
function zTreeSelect(event,treeId,treeNode){
	if(treeNode.id!=organizationId){
		var parentNode = treeNode.getParentNode();
		$("#isEdit").hide();
		departmentId=treeNode.id;
		$(".l-layout-header:eq(1)").html("编辑");
		$.post(path+"departmentController/department",
			{"id":treeNode.id},
			function(result){  
				$('#id').val(result.obj.id);
				$('#departId').val(parentNode.id).removeAttr("disabled");
				$('#name').val(result.obj.name).removeAttr("disabled");
				$('#code').val(result.obj.code).removeAttr("disabled");
				$('#range').val(result.obj.range).removeAttr("disabled");
				$('#description').val(result.obj.description).removeAttr("disabled");
				$("#submitBtn").show();
			},"json");
	}else{
		queryData(treeNode.id);
	}
}

//保存部门以后，刷新部门树
function refreshTreeNode(id,name){
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
			if(nodes[0].id==organizationId){
				queryData(nodes[0].id);
				// treeObj.reAsyncChildNodes(null, "refresh");
				refreshTree($("#orgId").val());
				treeObj.expandNode(nodes[0],true, true, true);
			}else{
				treeObj.reAsyncChildNodes(nodes[0], "refresh");
			}
		}
	}
}

//保存部门
function saveData() {
	var valid = $('#myFormId').validationEngine({returnIsValid: true});
	if(valid){
    	$.ajax({
    		async: false,
    		url: path + "departmentController/saveOrUpdate",
    		type: "POST",
    		data: {
    			"orgId" : organizationId,
    			"parentName" : $("#parentName").val(),
    			"id" : $("#id").val(),
    			"departId" : $("#departId").val(),
    			"name" : $("#name").val(),
    			"code" : $("#code").val(),
    			"range" : $("#range").val(),
    			"description" : $("#description").val()
    		},
    		// dataType: "json",  // 在前端独立阶段有这句话时执行不正常
    		success: function(data) {
    			if (typeof(data) == "string") data = eval("(" + data + ")");
    			var result = data.success;
    			if (result == true) {
    				quiAlertDialog("操作成功", function(){
    					refreshTreeNode($("#id").val(),$("#name").val());
    				}); 
    			} else {
    				quiAlertDialog("操作失败 ，" + data.message);
    			}
    		},
    		error: function(err) {
    			quiAlertDialog("操作失败 ，" + err.message);
    		}
    	});
	}
}


//新增部门
function addDepartment(){
	clearForm();
	var orgId = $("#orgId").val();
	if(orgId!=null&&orgId!=""){
		var node = $.fn.zTree.getZTreeObj("tree-1").getSelectedNodes();
		if(node.length==1){
			if(node==null){
				var orgName = $("#selectTree1").attr("relText");
				$("#isEdit td:eq(0)").html("所属机构");
				$("#parentName").val(orgName);
			}else{
				$("#isEdit td:eq(0)").html("上级部门");	
				$("#departId").val(node[0].id);
				$("#parentName").val(node[0].name);
			}
		}else if(node.length==0){
			quiAlertDialog("请选择所在部门！");
		}
	}else{
		quiAlertDialog("没有选择机构");
	}
}


//清空form表单
function clearForm(){
	$("#isEdit").show();
	$("#isEdit td:eq(0)").css("width","45%");
	$(".l-layout-header:eq(1)").html("新建部门");
	$("#myFormId input").val("").removeAttr("disabled");
	$("#description").val("").removeAttr("disabled");
	$("#submitBtn").show();
//	$("input[type='submit']").val("保存");
	$("#orgId").val($("#selectTree1").attr("relValue"));
}

//删除部门
function delDepartment(){
	var treeObj = $.fn.zTree.getZTreeObj("tree-1");
	var node = treeObj.getSelectedNodes();
	if(node.length==0||node.length>1){
		quiAlertDialog("请选择要删除的部门！");
	}else if(node[0].isParent){
		quiAlertDialog("请先删除子部门！");
	}else{
		quiConfirmDialog("确定要删除该部门吗？",function(){
			//删除记录
		  	$.post(path+"departmentController/delDepartmentEntity",
	  			{"id":node[0].id},
	  			function(result){
	  			//刷新左边树
	  			handleResult(result);
				},"json");
		  		clearForm();
		});
	}
}

function handleResult(data){
	var result = data.success;
	if (result == true) {
		quiAlertDialog("删除成功", function(){
			var treeObj = $.fn.zTree.getZTreeObj("tree-1");
			var nodes = treeObj.getSelectedNodes();
			var parent = nodes[0].getParentNode();
			treeObj.removeNode(nodes[0]);
			treeObj.selectNode(parent);
			zTreeSelect("","",parent);
		});
	} else {
		quiAlertDialog("删除失败，失败原因  [" + data.message+"]");
	}
}

function customHeightSet(contentHeight){
    $(".cusBoxContent").height(contentHeight-55);
    $(".orgTreeContainer").height(contentHeight-30);
}
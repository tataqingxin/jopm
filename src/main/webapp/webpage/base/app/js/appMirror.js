/**
 * 已经分配的组织机构ID集合
 */
var checkedOrgIdArray = new Array();

function filter(treeId, parentNode, childNodes) {
	  var list = childNodes.obj;
	$.each(list, function(index, element) {
		if ($.inArray(element.id, checkedOrgIdArray) != -1) {
			element["checked"] = true;
			return true;
		}
	});
      return list; 
}

function initComplete(){

}

function subAppMirror(){
	
	quiConfirmDialog("如果取消应用在机构的开通关系，对应机构已经分配的功能权限将一并被撤销，是否继续？", function() {
		
		/*var treeObj = $.fn.zTree.getZTreeObj("orgTree");
		var nodes = treeObj.getCheckedNodes();
		if(nodes.length==0){
			quiAlertDialog("请选择机构分配");
			return;
		}
		var ids ="";
		for(var i = 0,k=nodes.length;i<k;i++){
			if(i!=k-1){
				ids += nodes[i].id + ",";
			}else{
				ids += nodes[i].id;
			}
		}*/
		
		var ids = checkedOrgIdArray.join(",");
		
		$.ajax({
			async: false,
			url: path + "appMirrorController/insertAppMirror",
			type: "POST",
			data: {
				"orgIds" : ids,
				"appId":$("#applicationId").val()
			},
			dataType: "json",  // 在前端独立阶段有这句话时执行不正常
			success: function(data) {
				if (data == null || data == "") {
					quiAlertDialog("非法操作，请刷新后重试");
					return;
				}
				if (data.success) {
					quiAlertDialog(data.message,function(){
	        			closeWin();
	        		});
				} else {
					quiAlertDialog("操作失败");
				}
			}
		});
		
	}, function() {
		
		return false;
		
	});
	
}
function closeWin(){
	parent.closeAddDialog();
}
/**
 * 渲染页面方法
 * 父页面调用该方法，向本页面赋值
 * @param appId 应用ID
 * @param data 数据集合
 */
function wrapData(appId, data, orgId) {
	
	if (appId == null || appId == "") {
		quiAlertDialog("非法操作，请刷新后重试", function(){});
		return;
	}
	
	$("#applicationId").val(appId);
	
	checkedOrgIdArray = data.attributes.checkedOrgIds;
	
	// 设置懒加载、复选框及其回调
	var setting = {
		check: {
		    chkStyle: "checkbox",
			enable: true,
			chkboxType:{"Y": "", "N": ""}	
		},
	    async: {
	        enable: true,
	        dataType: 'JSON',
	        //返回的JSON数据的名字
	        url: path+"appMirrorController/organizationTree",
	        autoParam : [ "id", "name"],
	        otherParam: { "orgId":orgId},
	        dataFilter: filter
	    },
	    callback: {
	    	onCheck: zTreeOnCheck
	    }
	};
	$.fn.zTree.init($("#orgTree"), setting);
}
/**
 * 节点选中时执行的方法
 * 
 * @param event
 * @param treeId
 * @param treeNode选中的节点
 */
function zTreeOnCheck(event, treeId, treeNode) {
	
	if (treeNode.checked == true) {
		checkedOrgIdArray.push(treeNode.id);
	} else {
		var idx = $.inArray(treeNode.id, checkedOrgIdArray);
		if (idx != -1) {
			checkedOrgIdArray.splice(idx, 1);
		}
	}
}
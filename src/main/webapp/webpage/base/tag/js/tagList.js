var datagrid = null;
var tagId = null;
var data = null;
function initComplete(){
	datagrid = $("#datagrid").quiGrid({
		columns : [
	           {
	        	   display : 'id',
	        	   name : 'id',
	        	   align : 'center',
	        	   hide: true
	           },
	           {
	        	   display : '标签名称',
	        	   name : 'name',
	        	   align : 'center',
	        	   width : "20%"
	           },
	           {
	        	   display : '标签编码',
	        	   name : 'code',
	        	   align : 'center',
	        	   width : "20%"
	           },
	           {
	        	   display : '角色',
	        	   name : 'roleName',
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
						 + '<span class="img_list hand" title="角色设置" onclick="setRole(\'' + rowdata.id + '\')"></span>'						 
						 + '</div>';
					}
	           }
	           ],
	           
	           url : path + '/tagController/getTagForm',
	           rownumbers : true,
	           checkbox : true,
	           height : '100%',
	           width : "100%",
	           pageSize : 10,
	           percentWidthMode : true
	});
}

function setRole(id) {
	data = getRoleTag(id);
	tagId = id;
	var settings = {
			URL: "setRoleTag.html",
			Title: "",
			Width:500,
			Height:350
	};
	var dialog = quiDialog(settings, onloadCallBack, closeCallBack);
}



function getRoleTag(tagId) {
	var msg = null;

	// 获取后台数据
	$.ajax({
		async: false, // 此处的返回值将影响弹出框，所以这个ajax是同步的
		url: path + "/tagController/roleTree",
		type: "POST",
		data: {
			"tagId" : tagId,
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
	dialog.innerFrame.contentWindow.wrapData(tagId,data);
}

function closeCallBack(dialog) {
	datagrid.loadData();
	dialog.close();
}



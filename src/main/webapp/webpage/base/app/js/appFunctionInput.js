/**
 * Created by suenpeng on 2016/5/18.
 */

function initComplete(){
	initIconSelectTree();
}

//初始化图标选择树
function initIconSelectTree(){
	$.post(path+"iconController/getIconTreeList",
			{},
			function(result){
				var iconArr = eval(result.obj);
				for(var i=0;i<iconArr.length;i++){
					iconArr[i].icon = path + iconArr[i].iconPath;
				}
				$("#selectTree1").data("data",iconArr);
				$("#selectTree1").render();
	},"json");
	//图标选择绑定事件
	$("#selectTree1").bind("change",function(){
        if(!$(this).attr("relValue")){
            $("#iconId").val("");
        }else{
        	$("#iconId").val($(this).attr("relValue"));
        }
    });  
}
//提交
function subManage(){
	var id = $("#id").val();
	var url = null;
	if(id!= null&&id!=""){
		url = path + "app/functionController/updateFunction";
	}else{
		url = path + "app/functionController/insertFunction";
	}
    //判断表单的客户端验证是否通过
    var valid = $('#myFormId').validationEngine({returnIsValid: true});
    if(valid){
        $.ajax({
        	async:false,
        	data:{
        		"id":id,
        		"name":$("#name").val(),
        		"code":$("#code").val(),
        		"applicationId":$("#applicationId").val(),
        		"functionId":$("#functionId").val(),
        		"url":$("#url").val(),
        		"iconId":$("#iconId").val(),
        		"status":$("#status").val()
        	},
        	url: url,
        	type:"post",
        	dataType:"json",
            //表单提交成功后的回调
            success: function(data){
            	if(data.success){
            		quiAlertDialog(data.message,function(){
            			parent.closeAndRefreshMyDialog();
            		});
            	}else{
            		quiAlertDialog(data.message);
            	}
            }
        });
    }
}

function closeWin(){
    //刷新数据
	parent.closeMyDialog();
    //关闭窗口
}
function getData(id){
    if(id!=null&&id!=""){
        $.ajax({
            url:path+"app/functionController/detailFunction",
            datatype:"json",
            data:{"id":id},
            type:"post",
            success:function(data){
                var d = $.parseJSON(data);
                if(d.success){
                	$("#name").val(d.obj.name);
                	$("#code").val(d.obj.code);
                	$("#url").val(d.obj.url);
                	$("#status").setValue(d.obj.status);
                	$("#selectTree1").setValue(d.obj.iconEntity.id);
                	$("#iconId").val(d.obj.iconEntity.id);
                }
            }
        });
    }
}
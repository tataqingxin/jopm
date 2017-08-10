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

function subOpe(){
    //表单提交
	var id = $("#id").val();
	var url = null;
	if(id!= null&&id!=""){
		url = path + "app/operationController/updateOperation";
	}else{
		url = path + "app/operationController/insertOperation";
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
        		"iconId":$("#iconId").val(),
        		"resourceIds":$("input[name=resourceIds").val(),
        		"description":$("#description").val()
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
    //关闭窗口
    parent.closeMyDialog();
}
function getData(id){
    if(id!=null&&id!=""){
        $.ajax({
            url:path+"app/operationController/detailOperation",
            datatype:"json",
            data:{"id":id},
            type:"post",
            success:function(data){
                var d = $.parseJSON(data);
                if(d.success){
                	$("#name").val(d.obj.name);
                	$("#code").val(d.obj.code);
                	$("#url").val(d.obj.url);
                	$("#selectTree1").setValue(d.obj.iconEntity.id);
                	$("#iconId").val(d.obj.iconEntity.id);
                	var  resources = d.obj.resourceEntityList;
                	var rIds = new Array();
                	for(var i = 0;i<resources.length;i++){
                		rIds.push(resources[i].id);
                	}
                	$("#resource").setValue(rIds.join(","));
                	$("#description").text(d.obj.description);
                }
            }
        });
    }
}
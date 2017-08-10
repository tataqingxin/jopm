/**
 * Created by suenpeng on 2016/5/18.
 */
function initComplete(){
	initIconSelectTree();
	$("#organazationTree").bind("change",function(){
        if(!$(this).attr("relValue")){
            $("#organizationId").val("");
        }else{
        	$("#organizationId").val($(this).attr("relValue"));
        }
    }); 
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

//提交表单
function subApp(){
	var id = $("#id").val();
	var url = null;
	if(id!= null&&id!=""){
		url = path + "app/applicationController/updateApplication";
	}else{
		url = path + "app/applicationController/insertApplication";
	}
    //判断表单的客户端验证是否通过
    var valid = $('#myFormId').validationEngine({returnIsValid: true});
    if(valid){
        $.ajax({
        	async:false,
        	data:{
        		"id":id,
        		"name":$("#name").val(),
        		"companyCode":$("#companyCode").val(),
        		"appSerial":$("#appSerial").val(),
        		"code":$("#code").val(),
        		"organizationId": $("#organizationId").val(),
        		"url":$("#url").val(),
        		"type":$("#type").val(),
        		"status":$("#status").val(),
        		"iconId":$("#iconId").val(),
        		"description":$("#description").val(),
        		"strategy":$("#strategy").val()
        	},
        	url: url,
        	type:"post",
        	dataType:"json",
            //表单提交成功后的回调
            success: function(data){
            	if(data.success){
            		quiAlertDialog(data.message,function(){
            			closeWin();
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
	 parent.searchHandler();
    parent.closeAddDialog();
}
function getData(id){
    if(id!=null&&id!=""){
        $.ajax({
            url:path+"app/applicationController/detailApplication",
            datatype:"json",
            data:{"id":id},
            type:"post",
            success:function(data){
                var d = $.parseJSON(data);
            	$("#name").val(d.obj.name);
            	$("#code").val(d.obj.code);
            	$("#instanceCode").val(d.obj.instanceCode);
            	$("#companyCode").val(d.obj.companyCode);
            	$("#appSerial").val(d.obj.appSerial);
            	$("#insCode").text(d.obj.instanceCode);
            	$("#codeTr").show();
            	$("#type").setValue(d.obj.type);
            	$("#status").setValue(d.obj.status);
            	$("#strategy").setValue(d.obj.strategy);
            	if (d.obj.organizationEntity != null) {
            		$("#organizationId").val(d.obj.organizationEntity.id);
                	$("#organazationTree").attr("relValue",d.obj.organizationEntity.id);
                	$("#organazationTree").attr("relText",d.obj.organizationEntity.name);
                	$("#organazationTree").find("input:text").val(d.obj.organizationEntity.name);
            	}
            	
            	$("#selectTree1").setValue(d.obj.iconEntity.id);
            	$("#iconId").val(d.obj.iconEntity.id);
            	$("#url").val(d.obj.url);
            	$("#description").val(d.obj.description);
            }
        });
    }
}
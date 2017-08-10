/**
 * Created by suenpeng on 2016/5/18.
 */
function subServer(){
	var id = $("#id").val();
	if(id!= null&&id!=""){
		url = path + "app/serviceInterfaceController/updateServiceInterface";
	}else{
		url = path + "app/serviceInterfaceController/insertServiceInterface";
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
        		"url":$("#url").val(),
        		"description":$("#description").val()
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
	parent.closeAndRefreshMyDialog();
}
function getData(serverId){
    if(serverId!=null&&serverId!=""){
        $.ajax({
            url:path+"/app/serviceInterfaceController/detailServiceInterface",
            datatype:"json",
            data:{"id":serverId},
            type:"post",
            success:function(data){
                var d = $.parseJSON(data);
                if(d.success){
                	$("#name").val(d.obj.name);
                	$("#code").val(d.obj.code);
                	$("#url").val(d.obj.url);
                	$("#description").val(d.obj.description);
                }
            }
        });
    }
}
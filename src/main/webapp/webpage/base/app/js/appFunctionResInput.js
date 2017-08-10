/**
 * Created by suenpeng on 2016/5/18.
 */
function subRes(){
	var id = $("#id").val();
	var url = null;
	if(id!= null&&id!=""){
		url = path + "app/resourceController/updateResource";
	}else{
		url = path + "app/resourceController/insertResource";
	}
    //判断表单的客户端验证是否通过
    var valid = $('#myFormId').validationEngine({returnIsValid: true});
    if(valid){
        $.ajax({
        	async:false,
        	data:{
        		"id":id,
        		"applicationId":$("#applicationId").val(),
        		"functionId":$("#functionId").val(),
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
            url:path+"app/resourceController/detailResource",
            datatype:"json",
            data:{"id":id},
            type:"post",
            success:function(data){
                var d = $.parseJSON(data);
                if(d.success){
                	$("#url").val(d.obj.url);
                	$("#description").val(d.obj.description);
                }
            }
        });
    }
}
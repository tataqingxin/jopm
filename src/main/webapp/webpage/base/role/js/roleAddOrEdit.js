/**
 * 
 */

function initComplete() {
	
}

function saveOrUpdate() {
	
	var urlSuffix = $("#id").val() == "" ? "roleController/insertRole" : "roleController/updateRole";
	
    //判断表单的客户端验证是否
    var valid = $('#roleForm').validationEngine({returnIsValid: true});
    if(valid){
    	$.ajax({
    		async:false,
        	data:{
        		"id":$("#id").val(),
        		"organizationId":$("#organizationId").val(),
        		"code":$("#code").val(),
        		"name":$("#name").val(),
        		"type":$("#type").val(),
        		"description":$("#description").val()
        	},
    		type: "POST",
        	url: path + urlSuffix,
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

function wrapData(data) {
	if (data == null || data.id == null) {
		quiAlertDialog("非法操作，请刷新后重试");
		return;
	}
	$("#id").val(data.id);
	$("#organizationId").val(data.organizationEntity.id);
	$("#code").val(data.code);
	$("#name").val(data.name);
	$("#description").val(data.description);
	$("#type").setValue(data.type);
	// $("#type").render();
}

function resetSearch() {
	$("#roleForm")[0].reset();
}
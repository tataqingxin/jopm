$(function(){  
//	var emailId=UrlParm.parm("emailId");
//	$.ajax({
//    	async:false,
//    	data:{
//    		"emailId":emailId
//    	},
//    	url: path + "emailLoseController/validateEmailLose",
//    	type: "POST",
//    	dataType:"json",
//        //表单提交成功后的回调
//        success: function(data){
//        	if(!data.success){
//        		window.location.href=path+"webpage/base/emailLose/emailLose.html";
//        	}
//        }
//    });  
});
function saveUser(){
    var valid = $('#myFormId').validationEngine({returnIsValid: true});
    var tPassWord = '';
    if($("#password").val()){
    	tPassWord = MD5($("#password").val());
    }
    if(valid){
        $.ajax({
        	async:false,
        	data:{
        		"id":$("#id").val(),
        		"userName":$("#userName").val(),
        		"realName":$("#realName").val(),
        		"orgId":$("#orgId").val(),
        		"pageCheckedDepartId":$("#pageCheckedDepartId").val(),
        		"sex":$("#sex").val(),
        		"password":tPassWord,
        		"departmentId":$("input[name=departmentId]").val(),
        		"phone":$("#phone").val(),
        		"description":$("#description").val(),
        		"address":$("#address").val()
        	},
        	url: path + "userController/saveOrUpdateUser",
        	type: "POST",
        	dataType:"json",
            //表单提交成功后的回调
            success: function(data){
            	if(data.success){
            		quiAlertDialog(data.message,function(){
            			parent.closeUserAddDialog();
            		});
            	}else{
            		quiAlertDialog(data.message);
            	}
            }
        });
    }
}

function updateForgetPasswWord(){
	
	var userid=UrlParm.parm("user");  
    var valid = $('#myFormId').validationEngine({returnIsValid: true});
    if(valid){
        $.ajax({
        	async:false,
        	data:{
        		"id":userid,
        		"passwordOld":$("#passwordOld").val(),
        		"passwordNew":$("#passwordNew").val()
        	},
        	url: path + "userController/updatePasswWord",
        	type: "POST",
        	dataType:"json",
            //表单提交成功后的回调
            success: function(data){
            	if(data.success){
            		quiAlertDialog(data.message,function(){
            			window.location=casPath;
            		});
            	}else{
            		quiAlertDialog(data.message);
            	}
            }
        });
    }  
}
/**
 * 修改密码
 */
function updatePasswWord(){
	
    var valid = $('#myFormId').validationEngine({returnIsValid: true});
    if(valid){
        $.ajax({
        	async:false,
        	data:{
        		"id":$("#id").val(),
        		"passwordOld":MD5($("#passwordOld").val()),
        		"passwordNew":MD5($("#passwordNew").val())
        	},
        	url: path + "userController/updatePasswWord",
        	type: "POST",
        	dataType:"json",
            //表单提交成功后的回调
            success: function(data){
            	if(data.success){
            		quiAlertDialog(data.message,function(){
            			parent.updPwdDialogClose();
            		});
            	}else{
            		quiAlertDialog(data.message);
            	}
            }
        });
    }
}


function saveUserForOfficePlatForm(){
    var valid = $('#myFormId2').validationEngine({returnIsValid: true});
    if(valid){
        $.ajax({
        	async:false,
        	data:{
        		"id":$("#id").val(),
        		"userName":$("#userName").val(),
        		"realName":$("#realName").val(),
        		"orgId":$("#orgId").val(),
        		"pageCheckedDepartId":$("#pageCheckedDepartId").val(),
        		"sex":$("#sex").val(),
        		"departmentId":$("input[name=departmentId]").val(),
        		"phone":$("#phone").val(),
        		"email":$("#email").val(),
        		"description":$("#description").val(),
        		"address":$("#address").val()
        	},
        	url: path + "userController/saveOrUpdateUserForEamil",
        	type: "POST",
        	dataType:"json",
            //表单提交成功后的回调
            success: function(data){
            	if(data.success){
            		quiAlertDialog(data.message,function(){
            			parent.closeEmailAndRefreshDialog();
            		});
            	}else{
            		quiAlertDialog(data.message);
            	}
            }
        });
    }
}


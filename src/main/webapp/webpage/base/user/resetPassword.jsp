<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>重置密码</title>
	
	<%
		String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	%>

	<!--框架必需start-->
    <script type="text/javascript" src="<%= path %>ui-framework/qui/libs/js/jquery.js"></script>
    <script type="text/javascript" src="<%= path %>ui-framework/qui/libs/js/language/cn.js"></script>
    <script type="text/javascript" src="<%= path %>ui-framework/qui/libs/js/framework.js"></script>
    <link href="<%=path%>ui-framework/qui/libs/css/import_basic.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" id="skin" prePath="<%=path%>/" splitMode="true" href="<%=path%>ui-framework/qui/libs/skins/blue/style.css"/>
	<link rel="stylesheet" type="text/css" id="customSkin" href="<%=path%>ui-framework/qui/system/layout/skin/style.css"/>
    <!--框架必需end-->
    
    <!-- 表单验证start -->
	<script src="<%= path %>ui-framework/qui/libs/js/form/validationRule.js" type="text/javascript"></script>
	<script src="<%= path %>ui-framework/qui/libs/js/form/validation.js" type="text/javascript"></script>
	<!-- 表单验证end -->
    <!--表单异步提交start-->
    <script src="<%= path %>ui-framework/qui/libs/js/form/form.js" type="text/javascript"></script>
    <!--表单异步提交end-->
    <!--引入弹窗组件start--> 
    <script type="text/javascript" src="<%= path %>ui-framework/qui/libs/js/popup/drag.js"></script>
	<script type="text/javascript" src="<%= path %>ui-framework/qui/libs/js/popup/dialog.js"></script>
	<script type="text/javascript" src="<%= path %>ui-framework/qui/quiDialogWrapper.js"></script>
	<!--引入弹窗组件end--> 
	
	<script type="text/javascript" src="<%= path %>webpage/base/user/js/md5.js"></script>
    
    <script type="text/javascript">
    
		//禁止backSpace后退页面事件
		function banBackSpace(e){
			 var ev = e || window.event;//获取event对象  
		        var obj = ev.target || ev.srcElement;//获取事件源  
		        var t = obj.type || obj.getAttribute('type');//获取事件源类型  
		        //获取作为判断条件的事件类型  
		        var vReadOnly = obj.readOnly;  
		        var vDisabled = obj.disabled;  
		        //处理undefined值情况  
		        vReadOnly = (vReadOnly == undefined) ? false : vReadOnly;  
		        vDisabled = (vDisabled == undefined) ? true : vDisabled;  
		        //当敲Backspace键时，事件源类型为密码或单行、多行文本的，  
		        //并且readOnly属性为true或disabled属性为true的，则退格键失效  
		        var flag1 = ev.keyCode == 8 && (t == "password" || t == "text" || t == "textarea") && (vReadOnly == true || vDisabled == true);  
		        //当敲Backspace键时，事件源类型非密码或单行、多行文本的，则退格键失效  
		        var flag2 = ev.keyCode == 8 && t != "password" && t != "text" && t != "textarea";  
		        //判断  
		        if (flag2 || flag1) return false  
		}
		window.onload=function(){
			 //禁止退格键 作用于Firefox、Opera  
		    document.onkeypress = banBackSpace;  
		    //禁止退格键 作用于IE、Chrome  
		    document.onkeydown = banBackSpace; 
		}
		
		function resetPassword() {
			var valid = $('#myFormId').validationEngine({returnIsValid: true});
		    if(valid){
		    	$.ajax({
		        	async:false,
		        	data:{
		        		"passwordOld":MD5($("#passwordOld").val()),
		        		"passwordNew":MD5($("#passwordNew").val())
		        	},
		        	url: "<%=path%>userPasswordController/reset",
		        	type: "POST",
		        	dataType:"json",
		            //表单提交成功后的回调
		            success: function(data){
		            	if(data.success){
		            		alert("初始密码修改成功，请使用新密码重新登录系统使用。");
		            		gotoLogout();
		            	}else{
		            		quiAlertDialog(data.message);
		            	}
		            },
		            error : function(e) {
		            	alert(e);
		            }
		        });
		    	
		    }
		}
		
		function cancelReset() {
			var result = confirm("如果不修改密码，您将被注销，是否继续？");
			if (result == true) {
				gotoLogout();
			}
		}
		
		function gotoLogout() {
			window.location.href = "<%=path%>userPasswordController/logout";
		}
		
    </script>
    
</head>
<body>

<%-- 这个页面仅用于用户重置初始密码使用，请联系彭德获取更多信息 --%>

<form id="myFormId">
    <div class="box1" id="formContent" whiteBg="true">
         <table class="tableStyle" formMode="transparent">
            <tr id="passwordTr">
                <td width="150">旧密码：</td>
                <td><input type="password" name="passwordOld" id="passwordOld" class="validate[required]" /></td>
            </tr>
            <tr id="passwordTr">
                <td width="150">新密码：</td>
                <td><input type="password" name="passwordNew" id="passwordNew" class="validate[required,length[6,18],custom[SpecialCaracters]]" /></td>
            </tr>
            <tr id="cpasswordTr">
                <td width="150">再次输入新密码：</td>
                <td><input type="password" id="rePassword"class="validate[required,confirm[passwordNew]]" /></td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="button" value="确定" onclick="resetPassword()"/>
                    <input type="button" value="取消" onclick="cancelReset()"/>
                </td>
            </tr>
        </table>
    </div>
</form>


</body>
</html>
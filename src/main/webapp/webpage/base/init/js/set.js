/**
 * Created by suenpeng on 2016/5/18.
 */
function subMit(){
    $('#myFormId').submit(function(){
        //判断表单的客户端验证是否
        var valid = $('#myFormId').validationEngine({returnIsValid: true});
        if(valid){
            $(this).ajaxSubmit({
                async:"false",
                url: path+"initController/InitDataInfo",
                type:"post",
                dataType:"json",
                data:{
                	"orgName":$("#applicationId").val(),
                	"orgCode":$("#orgCode").val(),
                	"userName":$("#userName").val(),
                	"password":$("#password").val()
                },
                //表单提交成功后的回调
                success: function(responseText, statusText, xhr, $form){
                        var diag = new Dialog();
                        diag.Title = "数据初始完成";
                        diag.ShowMaxButton=true;
                        diag.ShowMinButton=true;
                        diag.OKEvent = function(){
                        	 parent.document.location.reload();   
                            diag.close();
                            };
                        diag.URL = path+"webpage/base/init/data.html";
                        diag.OnLoad=function() {
                			this.innerFrame.contentWindow.wrapData(responseText.obj);
                		};
                        diag.ShowButtonRow=true;
                        diag.ShowCancelButton=false;
                        diag.show();
                }
            });
        }
        //阻止表单默认提交事件
        return false;
    });
}
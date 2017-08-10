/**
 * Created by suenpeng on 2016/4/5.
 */
function subMit(){
    $('#myFormId').submit(function(){
        //判断表单的客户端验证是否
        var valid = $('#myFormId').validationEngine({returnIsValid: true});
        if(valid){
            $(this).ajaxSubmit({
            	async:"false",
            	url: path+"dictionaryGroupController/saveOrUpdateDctionaryGroup",
            	type:"post",
            	dataType:"json",
                //表单提交成功后的回调
                success: function(responseText, statusText, xhr, $form){
                	quiAlertDialog(responseText.message,function(){
                		parent.closedicEditDialog(true);
                    });
                }
            });
        }
        //阻止表单默认提交事件
        return false;
    });
}

function getData(id){
	if(id!=null&&id!=""){
		$.ajax({
			url:path+"dictionaryGroupController/dictionaryGroup",
			datatype:"json",
			data:{"id":id},
			type:"post",
			success:function(data){
				var d = $.parseJSON(data);
				$("#name").val(d.name);
				$("#code").val(d.code);
				$("#code").attr("disabled","disabled");
			}
		});
	}
}

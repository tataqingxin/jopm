function saveDict() {
	$('#myFormId').submit(function(){
		// 这里应当有校验：groupid是否有值/code/value是否有值
		var valid = $('#myFormId').validationEngine({returnIsValid: true});
		if(valid){
		 	$(this).ajaxSubmit({
		    	async:"false",
		    	url: path+"dictionaryController/saveOrUpdateDctionary",
		    	type:"post",
		    	dataType:"json",
		        //表单提交成功后的回调
		        success: function(responseText, statusText, xhr, $form){
		        	quiAlertDialog(responseText.message,function(){
		            	closeWin2();
		            });
		        }
		    });
		}
	//阻止表单默认提交事件
    return false;
    });
}
function closeWin2(){
	var update = false;
	var isupdate = $('#id').val();
	if(isupdate != ''){
		update = true;
	}else{
		update = false;
	}
    //刷新数据
	parent.closeParamDialog(update);
}

function getData2(id){
	if(id!=null&&id!=""){
		$.ajax({
			url:path+"dictionaryController/dictionary",
			datatype:"json",
			data:{"id":id},
			type:"post",
			success:function(data){
				var d = $.parseJSON(data);
				$("#name").val(d.name);
				$("#code").val(d.code);
			}
		})
	}
}

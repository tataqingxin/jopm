
function initComplete(){

}

function chooseStrategy(){	
	$.ajax({
		url: path + "app/applicationController/updateStrategy",
		data: {
			"applicationId" : $("#applicationId").val(),
			"strategy" : $("#strategy").val()
	
		},
		dataType: "json",  
	    type: "post", 
		success:function(data){
			if (data.success) {
				quiAlertDialog(data.message, function() {
					parent.closeAddDialog();
				});
			} else {
				quiAlertDialog(data.message);
			}	
		}
	});
}
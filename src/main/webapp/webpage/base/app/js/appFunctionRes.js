/**
 * Created by suenpeng on 2016/5/7.
 */
var grid=null;
var myDialog = null;
function initComplete(){
    grid = $("#dataBasic").quiGrid({
        columns:[
            { display: '主键', name: 'id',     align: 'left', width: "25%",hide:true},
            { display: '地址', name: 'url',    align: 'left', width: "45%"},
            { display: '描述', name: 'description',    align: 'left', width: "45%"}
        ],
        url:path+"app/resourceController/resourceList",rownumbers:true,checkbox:true,
        height: '100%', width:"100%",pageSize:10,percentWidthMode:true,
        toolbar:{
            items:[
                {text: '新建', click:resInput, iconClass: 'icon_add'},
                { line : true },
                {text: '编辑', click: resEdit, iconClass: 'icon_edit'},
                { line : true },
                {text: '删除', click: resDelete, iconClass: 'icon_delete'},
                { line : true }
            ]
        }
    });
}

function onloadData(functionId,applicationId){
	var query = null;
	query = [{name:"applicationId",value: applicationId},{name:"functionId",value: functionId}];
	grid.setOptions({ params : query});
	//页号重置为1
	grid.setNewPage(1);
	//刷新表格数据 
	grid.loadData();
}

//查询
function searchHandler(){
    var query = $("#queryForm").formToArray();
    grid.setOptions({ params : query});
    //页号重置为1
    grid.setNewPage(1);
    grid.loadData();//加载数据
}

//重置查询
function resetSearch(){
    $("#queryForm")[0].reset();
    $('#search').click();
}

//录入
function resInput(){
	var functionId = $("#functionId").val();
    var applicationId = $("#applicationId").val();
    
    myDialog = quiDialog(
    		{
    			URL:path+"webpage/base/app/appFunctionResInput.html",
    	        Title:"新建",Width:500,Height:350
    		},
    		function() {
    			var curWin = getChildWin(myDialog);
    			curWin.document.getElementById('applicationId').value=applicationId;
    			curWin.document.getElementById('functionId').value=functionId;
    		}
    	);
}

function closeMyDialog(){
	myDialog.close();
}

function closeAndRefreshMyDialog(){
	searchHandler();
	myDialog.close();
}
//编辑
function  resEdit(){
    var rowDatas = grid.getCheckedRows();
    var functionId = $("#functionId").val();
    var applicationId = $("#applicationId").val();
    if(rowDatas.length==0||rowDatas.length>1){
        quiAlertDialog("请选择一条资源进行编辑！");
    }else{
    	 myDialog = quiDialog(
    	    		{
    	    			 URL:path+"webpage/base/app/appFunctionResInput.html",
    	    	            Title:"编辑",Width:500,Height:350
    	    		},
    	    		function() {
    	    			var curWin = getChildWin(myDialog);
    	    			curWin.document.getElementById('id').value=rowDatas[0].id;
                  curWin.document.getElementById('applicationId').value=applicationId;
                  curWin.document.getElementById('functionId').value=functionId;
                        //调用编辑页面的getData方法
                  curWin.getData(rowDatas[0].id);
    	    		}
    	    	);
    }
}
//删除
function resDelete(){
	var functionId = $("#functionId").val();
	var rowDatas = grid.getCheckedRows();
    if(rowDatas.length==0||rowDatas.length>1){
        quiAlertDialog("请选择一条资源删除！");
    }else{
    	quiConfirmDialog("确定要删除该记录吗？",function(){
            //删除记录
            $.post(path+"app/resourceController/deleteResource",
                {"id":rowDatas[0].id,"functionId":functionId},
                function(result){
                	if(result.success){
                		quiAlertDialog(result.message);
                		refresh();
                	}else{
                		quiAlertDialog(result.message);
                	}
                },"json");
            //刷新表格数据
        });
    }
}

function refresh(){
	//页号重置为1
    grid.setNewPage(1);
    //加载数据
    grid.loadData();
}

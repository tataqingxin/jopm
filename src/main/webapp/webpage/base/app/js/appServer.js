/**
 * Created by suenpeng on 2016/5/7.
 */
var grid=null;
var myDialog = null;
function initComplete(){
    
}

//页面加载数据（供上一个列表页操作）
function onloadData(applicationId){
	
	//当提交表单刷新本页面时关闭弹窗
    grid = $("#dataBasic").quiGrid({
        columns:[
            { display: '主键', name: 'id',     align: 'center', width: "25%",hide:true},
            { display: '服务名称', name: 'name',     align: 'center', width: "20%"},
            { display: '服务类地址', name: 'url',    align: 'center', width: "40%"},
            { display: '描述', name: 'description',    align: 'center', width: "25%"},
            { display: '操作', isAllowHide: false, align: 'center', width:"15%",
                render: function (rowdata, rowindex, value, column){
                    return '<div class="padding_top4 padding_left5">'
                        + '<span style="margin-left:20px;cursor:pointer" title="编辑" onclick="serverEdit(\'' + rowdata.id+'\')">编辑</span>'
                        + '<span style="margin-left:20px;cursor:pointer" title="删除" onclick="serverDelete(\'' + rowdata.id+'\')">删除</span>'
                        + '</div>';
                }
            }
        ],
        url:path+"/app/serviceInterfaceController/serviceInterfaceList?applicationId=" + applicationId,
        rownumbers:true,
        checkbox:true,
        height: '100%', width:"100%",pageSize:10,percentWidthMode:true,
        toolbar:{
            items:[
                {text: '新建', click:serverInput, iconClass: 'icon_add'},
                { line : true }
            ]
        }
    });
    
	/*var query = [{name:"applicationId",value: applicationId}];
	grid.setOptions({ params : query});
	//页号重置为1
	grid.setNewPage(1);
	//刷新表格数据 
	grid.loadData();*/
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
function serverInput(){
	var applicationId = $("#applicationId").val();
	
	myDialog = quiDialog(
			{
				 URL:path+"webpage/base/app/appServerInput.html",
			    Title:"新建",Width:500,Height:350
			},
			function() {
				getChildWin(myDialog).document.getElementById('applicationId').value=applicationId;
			},
			function() {
				searchHandler();
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
function  serverEdit(rowId){
	var applicationId = $("#applicationId").val();
	myDialog = quiDialog(
		{
			 URL:path+"webpage/base/app/appServerInput.html",
	       Title:"编辑",Width:500,Height:350
		},
		function() {
			 var curWin = getChildWin(myDialog);
			 curWin.document.getElementById('id').value=rowId;
			 curWin.document.getElementById('applicationId').value=applicationId;
              //调用编辑页面的getData方法
			 curWin.getData(rowId);
		},
		function() {
			searchHandler();
		}
	);
}
//删除
function serverDelete(rowId){
	quiConfirmDialog("确定要删除该服务接口吗？",function(){
        //删除记录
        $.post(path+"/app/serviceInterfaceController/deleteServiceInterface",
            {"id":rowId},
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

function refresh(){
	//页号重置为1
	grid.setNewPage(1);
	//重新加载数据
	grid.loadData();
}
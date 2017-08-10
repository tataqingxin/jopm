//grid
var grid = null;
var grid2=null;
var dicEditDialog = null;//字典项录入
var paramDialog = null;//参数录入
function initComplete(){
    //当提交表单刷新本页面时关闭弹窗

    grid = $("#dataBasic").quiGrid({
        columns:[
            { display: '主键', name: 'id',     align: 'left', width: "30%",hide:true},
            { display: '字典项名称', name: 'name',     align: 'center', width: "30%"},
            { display: '字典项编码', name: 'code',    align: 'center', width: "30%"},
            { display: '操作', isAllowHide: false, align: 'left', width:"30%",
                render: function (rowdata, rowindex, value, column){
                    return '<div class="padding_top4 padding_left5">'
                        + '<span style="margin-left:20px;cursor:pointer" title="删除" onclick="onDelete(\'' + rowdata.id+'\','+rowindex + ')">删除</span>'
                        + '<span style="margin-left:20px;cursor:pointer" title="设置参数" onclick="onSet(\'' + rowdata.id+'\','+rowindex + ')">设置参数</span>'
                        + '</div>';
                }
            }
        ],
        url: path+'dictionaryGroupController/getDctionaryGroupList', rownumbers:true,checkbox:true,
        height: '100%', width:"100%",pageSize:10,percentWidthMode:true,
        toolbar:{
            items:[
                {text: '新建', click: dctionaryGroupSaveView, iconClass: 'icon_export'},
                { line : true },
                {text: '编辑', click: dctionaryGroupView, iconClass: 'icon_export'},
                { line : true }
            ]
        }
    });


    grid2= $("#dataBasic2").quiGrid({
        columns:[
            { display: '主键', name: 'id', align: 'left', width: "30%",hide:true},
            { display: '参数名称', name: 'name',     align: 'center', width: "18%"},
            { display: '参数编码', name: 'code',    align: 'center', width: "20%"},
            { display: '操作', isAllowHide: false, align: 'center', width:"12%",
                render: function (rowdata, rowindex, value, column){
                    return '<div class="padding_top4 padding_left5">'
                        + '<span style="align:center;cursor:pointer" title="删除" onclick="onDelete2(\'' + rowdata.id+'\','+rowindex + ')">删除</span>'
                        + '</div>';
                }
            }
        ],
        url:path+'dictionaryController/getDctionaryList',rownumbers:true,checkbox:true,
        height: '100%', width:"100%",pageSize:10,percentWidthMode:true,
        toolbar:{
            items:[
                {text: '新建', click: dctionary, iconClass: 'icon_export'},
                { line : true },
                {text: '编辑', click: dctionaryView, iconClass: 'icon_export'},
                { line : true }
            ]
        }
    });
}
//查询字典项
function searchHandler(){
	 var query = $("#queryForm").formToArray(); 
	 grid.setOptions({ params : query});
	 //页号重置为1
	 grid.setNewPage(1);
	//刷新表格数据 
	 grid.loadData();
}
//查询参数
function searchHandler2(){
	 var query = $("#queryForm2").formToArray(); 
	 grid2.setOptions({ params : query});
	 //页号重置为1
	 grid2.setNewPage(1);
	//刷新表格数据 
	 grid2.loadData();
}

//重置字典项查询
function resetSearch(){
	$("#queryForm")[0].reset();
	searchHandler();
}
//重置参数查询
function resetSearch2(){
	$("#queryForm2")[0].reset();
	searchHandler2();
}

//字典项录入
function dctionaryGroupSaveView(groupId){
    dicEditDialog = quiDialog({
        URL:path+"webpage/base/dictionary/dctionaryGroupSaveView.html",
        Title:"新建",Width:500,Height:350}
    	);
}

function closedicEditDialog(isUpdate){
	refresh(isUpdate);
    //关闭窗口
	dicEditDialog.close();
}

function closedicEditDialogOnly(){
	dicEditDialog.close();
}
//字典项编辑
function dctionaryGroupView(){
	var count = grid.getCheckedRows();
	if(count.length==0||count.length>1){
		quiAlertDialog("请选择一条字典项进行编辑！");
	}else{
		var rowdata = grid.getSelectedRow();
		dicEditDialog = quiDialog(
				{
					URL:path+"webpage/base/dictionary/dctionaryGroupSaveView.html",
					Title:"编辑字典项",Width:500,Height:350
				},
				function() {
					var curWin = getChildWin(dicEditDialog);
					curWin.document.getElementById('id').value=rowdata.id;
					//调用编辑页面的getData方法
					curWin.getData(rowdata.id);
				}
			);
	}
}

//参数录入
function dctionary(){
	var groupId = $("#groupId").val();
	if(groupId!=null&&groupId!=""){
		
		paramDialog = quiDialog(
				{
					URL:path+"webpage/base/dictionary/dctionaryView.html",
					Title:"新建参数",Width:500,Height:350
				},
				function() {
					var curWin = getChildWin(paramDialog);
					curWin.document.getElementById('groupId').value=groupId;
				}
			);
	}else{
		quiAlertDialog("请选择字典项后再录入参数！");
	}
}

function closeParamDialogOnly(){
	paramDialog.close();
}

function closeParamDialog(isUpdate){
	refresh2(isUpdate);
	paramDialog.close();
}

//参数编辑
function  dctionaryView(){
	var groupId = $("#groupId").val();
	var count = grid2.getCheckedRows();
	if(count.length==0||count.length>1){
		quiAlertDialog("请选择一条参数进行编辑！");
	}else{
		var rowdata = grid2.getSelectedRow();
		
		paramDialog = quiDialog(
				{
					URL:path+"webpage/base/dictionary/dctionaryView.html",
					Title:"编辑参数",Width:500,Height:350
				},
				function() {
					var curWin = getChildWin(paramDialog);
					curWin.document.getElementById('id').value=rowdata.id;
					curWin.document.getElementById('groupId').value=groupId;
					curWin.getData2(rowdata.id);
				}
			);
	}
}

//字典项刷新
function refresh(isUpdate){
    if(!isUpdate){
    //重置排序
    grid.options.sortName='name';
    grid.options.sortOrder="desc";
    //页号重置为1
        grid.setNewPage(1);
    }
    grid.loadData();

}
//参数刷新
function refresh2(isUpdate){
    if(!isUpdate){
    //重置排序
    grid2.options.sortName='name';
    grid2.options.sortOrder="desc";
    //页号重置为1
        grid2.setNewPage(1);
    }
    grid2.loadData();

}
//删除字典项	
function onDelete(rowid,rowidx){
	quiConfirmDialog("确定要删除该记录吗？",function(){
  	//删除记录
  	$.post(path+"dictionaryGroupController/delDctionaryGroup",
  			{"id":rowid},
  			function(result){
	  				handleResult(result);
  				refresh(true);
			},"json");
			//刷新表格数据 
	});
}
//删除指点数值
function onDelete2(rowid,rowidx){
	quiConfirmDialog("确定要删除该记录吗？",function(){
  	//删除记录
  	$.post(path+"dictionaryController/delDctionary",
  			{"id":rowid},
  			function(result){
	  				handleResult(result);
  				refresh2(true);
			},"json");
			//刷新表格数据 
	});
}

function handleResult(data){
	var result = data.success;
	if (result == true) {
		quiAlertDialog("删除成功");
	} else {
		quiAlertDialog("操作失败 ，" + data.message);
	}
}

//设置参数
function onSet(rowid,rowidx){
	var groupId="";
	groupId=rowid;
	//刷新表格数据 
	$("#groupId").val(groupId);
	var query = [{name:"groupId",value: groupId}];
	grid2.setOptions({ params : query});
	//页号重置为1
	grid2.setNewPage(1);
	//刷新表格数据 
	grid2.loadData();
}


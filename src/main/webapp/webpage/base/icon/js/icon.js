/**
 * Created by suenpeng on 2016/5/7.
 */
var grid = null;
var meDialog = null;
function initComplete(){

    grid = $("#dataBasic").quiGrid({
        columns:[
            { display: '主键', name: 'id',   hide:true,  align: 'center', width: "20%"},
            { display: '图标名称', name: 'name',     align: 'center', width: "15%"},
            { display: '图标CODE', name: 'type',   hide:true,  align: 'center', width: "20%"},
            { display: '图标类型', name: 'typeName', align: 'center', width: "17%"},
            { display: '小图标', name: 'iconPath',     align: 'center',  width:"17%",  
            	render : function(rowdata, rowindex, value, column){
            		return "" == value ? value : '<img  height="30" width="30" src="'+path+''+value+'" /></img>';
            	}
            },
            { display: '中图标', name: 'mediumIconPath',     align: 'center',  width:"17%",  
            	render : function(rowdata, rowindex, value, column){
            		return "" == value ? value : '<img  height="50" width="50" src="'+path+''+value+'" /></img>';
            	}
            },
            { display: '大图标', name: 'bigIconPath',     align: 'center',  width:"17%",  
            	render : function(rowdata, rowindex, value, column){
            		return "" == value ? value : '<img  height="80" width="80" src="'+path+''+value+'" /></img>';
            	}
            },
            { display: '操作', isAllowHide: false, align: 'center', width:"17%",
                render: function (rowdata, rowindex, value, column){
                    return '<div class="padding_left5">'
                        + '<span class="" title="删除" onclick="onDelete(\'' + rowdata.id+'\','+rowindex + ')"><a>[删除]</a></span>'
                        + '</div>';
                }
            }
        ],
        url: path+'iconController/iconList', rownumbers:false,checkbox:false,fixedCellHeight:false,

        height: '100%', width:"100%",pageSize:10,percentWidthMode:true,
        toolbar: {
            items: [
                {text: '新建', click: iconInput, iconClass: 'icon_export'},
                {line: true},
                {text: '编辑', click: iconEdit, iconClass: 'icon_export'},
                {line: true}
            ]
        }
    });
    

}
// 查询

function searchHandler(){
    var query = $("#queryForm").formToArray();
    grid.setOptions({ params : query});

    // 页号重置为1

    grid.setNewPage(1);

    grid.loadData();// 加载数据

}



// 重置查询

function resetSearch(){

    $("#queryForm")[0].reset();

    searchHandler();

}

// 录入
function iconInput(){
	meDialog = quiDialog(
			{
				 URL:path+"webpage/base/icon/iconEdit.html",
			    Title:"新建",Width:500,Height:350
			},
			function() {
				
			},
			function() {
				searchHandler();
			}
		);
}
// 修改
function iconEdit(){
	var count = grid.getCheckedRows();
	if(count.length==0||count.length>1){
		quiAlertDialog("请选中一条记录进行编辑！");
	}else{
		var rowdata = grid.getSelectedRow();
		
		meDialog = quiDialog(
				{
					URL:path+"webpage/base/icon/iconEdit.html",
			      Title:"编辑",Width:500,Height:350
				},
				function() {
					var curWin = getChildWin(meDialog);
					curWin.document.getElementById('id').value=rowdata.id;
					//调用编辑页面的getData方法
					curWin.getData(rowdata.id);
				},
				function() {
					searchHandler();
				}
			);
	}
}

function closeMeDialog(){
	meDialog.close();
	searchHandler();
}
// 删除
function onDelete(rowid,rowidx){
	quiConfirmDialog("确定要删除该记录吗？",function(){
	  	// 删除记录
	  	$.post(path+"iconController/deleteIcon",
	  			{"id":rowid},
	  			function(result){
	  				handleResult(result);
				},"json");
				// 刷新表格
				grid.loadData();
		});
}


function handleResult(result){
	
	if(result.success){
		quiAlertDialog(result.message);
		grid.loadData();
	}else{
		quiAlertDialog(result.message);
	}
}


// 刷新表格数据并重置排序和页数
function refresh(isUpdate){
	if(!isUpdate){
		// 重置排序
    	grid.options.sortName='id';
    	grid.options.sortOrder="desc";
    	// 页号重置为1
		grid.setNewPage(1);
	}
	
	grid.loadData();
}
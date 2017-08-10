/**
 * Created by suenpeng on 2016/5/7.
 */
var grid=null;

function initComplete(){
    grid = $("#dataBasic").quiGrid({
        columns:[
            { display: '日志等级', name: 'logLevel',     align: 'center', width: "18%"},
            { display: '操作人', name: 'userEntity.userName',    align: 'center', width: "20%"},
            { display: '操作内容', name: 'content', align: 'center', width: "10%"},
            { display: '操作时间', name: 'operateTime',     align: 'center',  width:"20%",render: function (rowdata, rowindex, value, column){
            	if(value.indexOf(".") > -1) return value.substr(0, 19); else return value;
            }} ,
            { display: '浏览器', name: 'broswer',     align: 'center',  width:"20%"}
        ],
        url: path+'logController/logList', 
        rownumbers:true,
        checkbox:false,
        height: '100%', 
        width:"100%",
        pageSize:10
    });
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

    searchHandler();

}

//导出
function exportTotalData(){

}
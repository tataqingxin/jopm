function initComplete() {
	 var id = $("#applicationId").val();
	 datagrid = $("#datagrid").quiGrid({
			columns : [
				{
					display : 'id',
					name : 'id',
					align : 'center',
					hide: true
				},
				{
					display : '',
					name : '',
					align : 'center',
					width : "20%"
				},
				{
					display : '',
					name : '',
					align : 'center',
					width : "25%"
				},
				{
					display : '',
					name : '',
					align : 'center',
					width : "25%"
				}
			],
			url : path + '',
			rownumbers : true,
			checkbox : false,
			params : [{"name":"id",value:id}],
			height : '100%',
			width : "100%",
			pageSize : 10,
			percentWidthMode : true,
			rowHeight : 35,
			headerRowHeight : 35
		});	 
}
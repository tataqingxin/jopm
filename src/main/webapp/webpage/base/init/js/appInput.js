/**
 * Created by suenpeng on 2016/5/18.
 */
function initComplete(){
    //表单提交
    $('#myFormId').submit(function(){
        //判断表单的客户端验证是否通过
        var valid = $('#myFormId').validationEngine({returnIsValid: true});
        //$("#file").remove();
        if(valid){
            $(this).ajaxSubmit({
                //表单提交成功后的回调
                success: function(responseText, statusText, xhr, $form){
                    top.Dialog.alert("保存成功",function(){
                        closeWin();
                    })
                }
            });
        }
        //阻止表单默认提交事件
        return false;
    });
}

function closeWin(){
    var update = false;
    var isupdate = $('#id').val();
    if(isupdate != ''){
        update = true;
    }else{
        update = false;
    }
    //刷新数据
    window.parent.refresh(update);
    //top.frmright.refresh(update);
    //关闭窗口
    top.Dialog.close();
}
function getData(){
    var id = $("#id").val();
    if(id!=null&&id!=""){
        $("#image").css("display","block");
        $.ajax({
            url:path+"iconController/viewIcon",
            datatype:"json",
            data:{"id":id},
            type:"post",
            success:function(data){
                var d = $.parseJSON(data);
                $("#name").val(d.name);
                $("#image").attr("src",path+d.iconPath);
                $("#select").attr("selectedValue",d.type);
                $("#select").render();
                $("#description").val(d.description);
            }
        })
    }
}
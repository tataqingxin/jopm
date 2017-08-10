/**
 * Created by suenpeng on 2016/3/31.
 */
var url='';//项目地址
var project='';//项目名称
/**
 * 访问url拼接
 * 当url与project 有值的情况下 走后台代码
 * 相反走前台的模拟数据
 * @param controllerName  controller层
 * @param methodName 方法名称
 * @param sourceName 目录 当url与project 没有值得时候 才能使用
 * @param field 参数
 * @returns {string|*}
 */
function pathUrl(controllerName,methodName,sourceName){
    var newPath='';
    if(url!='' || project!=''){
        newPath=url+project;
    }else{
        newPath=path+"webpage/common/"+sourceName+"/";
    }
    newPath+=controllerName+"/"+methodName;
    
    if(url=='' || project==''){
        newPath+=".json";
    }

    return newPath;
}

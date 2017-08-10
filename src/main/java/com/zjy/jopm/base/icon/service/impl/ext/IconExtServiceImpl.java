/** 
 * @Description:[对外图标接口实现类]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.icon.service.impl.ext.IconExtServiceImpl.java
 * @ClassName:IconExtServiceImpl
 * @Author:Lu Guoqiang 
 * @CreateDate:2016年5月9日 下午3:25:50
 * @UpdateUser:Lu Guoqiang   
 * @UpdateDate:2016年5月9日 下午3:25:50  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.icon.service.impl.ext;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.model.AjaxJson;
import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.zjy.jopm.base.icon.entity.IconEntity;
import com.zjy.jopm.base.icon.service.ext.IconExtService;

/**
 * @ClassName: IconExtServiceImpl 
 * @Description: [对外图标接口实现类] 
 * @author Lu Guoqiang 
 * @date 2016年5月9日 下午3:25:50 
 * @since JDK 1.6 
 */
@Service("iconExtService")
@Transactional
public class IconExtServiceImpl extends BaseServiceimpl implements IconExtService {

	/**
	 * 
	 * @Title: getIconTreeList
	 * @Description: [外部获取图片列表]
	 * @return AjaxJson
	 * @see com.zjy.jopm.base.icon.service.ext.IconExtService#getIconTreeList()
	 */
	@Override
	public AjaxJson getIconTreeList() {
		AjaxJson j = new AjaxJson();
		List<IconEntity> iconList = this.queryListByHql("from IconEntity");
		if(iconList.size()!=0){
			j.setObj(iconList);
			return j;
		}
		return j;
	}

}

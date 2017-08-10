package com.zjy.jopm.service.open.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unistc.core.common.service.impl.BaseServiceimpl;
import com.zjy.jopm.base.dict.entity.DictionaryEntity;
import com.zjy.jopm.base.dict.entity.DictionaryGroupEntity;
import com.zjy.jopm.sdk.entityVO.Dictionary;
import com.zjy.jopm.sdk.entityVO.DictionaryChild;
import com.zjy.jopm.sdk.service.OpenDictService;

@Service("openDictService")
@Transactional
public class OpenDictServiceImpl extends BaseServiceimpl implements
		OpenDictService {

	@Override
	public List<Dictionary> getAllDicList() {

		List<Dictionary> list = new ArrayList<Dictionary>();
		List<DictionaryGroupEntity> typeGroups = this
				.queryListByClass(DictionaryGroupEntity.class);
		for (DictionaryGroupEntity dictionaryGroupEntity : typeGroups) {
			Dictionary dic = new Dictionary();
			dic.setCode(dictionaryGroupEntity.getCode());
			dic.setName(dictionaryGroupEntity.getName());
			
			
			List<DictionaryEntity> types = this.queryListByProperty(
					DictionaryEntity.class, "dictionaryGroupEntity.id",
					dictionaryGroupEntity.getId());
			List<DictionaryChild> listChild = new ArrayList<DictionaryChild>();
			
			for(DictionaryEntity groupChild : types){
				DictionaryChild child = new DictionaryChild();
				child.setCode(groupChild.getCode());
				child.setName(groupChild.getName());
				listChild.add(child);
			}
			dic.setChildList(listChild);
			
			list.add(dic);
		}
		
		return list;

	}

}

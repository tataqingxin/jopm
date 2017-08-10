package com.zjy.jopm.base.common;
/**
 * 
 * @ClassName: TreeNode 
 * @Description: 树所需的属性 可根据业务需要自行往类里面加属性
 * @author xuanx
 * @date 2016年5月12日 上午9:56:08 
 * @since JDK 1.6
 */
public class TreeNode {
	private String id = null;//节点的唯一标识
	private String parentId = null;//节点的父节点，用来标识层级。
	private Boolean isParent = null;//是否为父节点
	private String name = null;//节点名称
	private Boolean open = Boolean.valueOf(false);//记录 treeNode 节点的展开 / 折叠状态
	private String url = null;//节点链接的目标 URL
	private String target = null;//设置点击节点后在何处打开 url
	private String iconClose = null;//图标关闭
	private String iconOpen = null;//图标打开
	private String icon = null;//图标
	private Boolean checked;//节点的 checkBox 
	private Boolean nocheck=true; 
	private Boolean chkDisabled;//设置节点的 checkbox / radio 是否禁用
	private Boolean drag;//用于树形双选器的数据源中，双选器的父节点都要设置该属性值为false
	private Boolean clickExpand;//用于树形下拉框的数据源中，当某些节点的该属性设置为true时，则无法被选中。
	private String click;//最简单的 click 事件操作
	
	private String type;// 节点类型
	private String rootId;// 根节点

	public Boolean getNocheck() {
		return nocheck;
	}

	public void setNocheck(Boolean nocheck) {
		this.nocheck = nocheck;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public Boolean getIsParent() {
		return this.isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getOpen() {
		return this.open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getIconClose() {
		return this.iconClose;
	}

	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}

	public String getIconOpen() {
		return this.iconOpen;
	}

	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}


	public Boolean getChecked() {
		return this.checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}


	public Boolean getChkDisabled() {
		return this.chkDisabled;
	}

	public void setChkDisabled(Boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public Boolean getDrag() {
		return this.drag;
	}

	public void setDrag(Boolean drag) {
		this.drag = drag;
	}


	public Boolean getClickExpand() {
		return this.clickExpand;
	}

	public void setClickExpand(Boolean clickExpand) {
		this.clickExpand = clickExpand;
	}

	public String getClick() {
		return this.click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the rootId
	 */
	public String getRootId() {
		return rootId;
	}

	/**
	 * @param rootId the rootId to set
	 */
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}
	
	
}

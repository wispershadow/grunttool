package org.wispersd.common.tools.jsutilties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompressGroups {
	private String version;
	private final Map<String, String> commonPaths;
	private final List<Object> itemList;

	public CompressGroups() {
		this.commonPaths = new HashMap<String, String>();
		this.itemList = new ArrayList<Object>();
	}

	public void addItem(Object nextItem) {
		if (nextItem instanceof CompressGroup) {
			CompressGroup compressGroup = (CompressGroup) nextItem;
			if (compressGroup.hasItems())
				this.itemList.add(nextItem);
		} else {
			this.itemList.add(nextItem);
		}
	}

	public List<Object> getItemList() {
		return this.itemList;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public void addCommonPath(String pathName, String pathValue) {
		this.commonPaths.put(pathName, pathValue);
	}

	public String getPathValue(String pathName) {
		return ((String) this.commonPaths.get(pathName));
	}

	public List<CompressGroup> getCompressGroups() {
		List<CompressGroup> resList = new ArrayList<CompressGroup>();
		for (Object nextObj : itemList) {
			if (nextObj instanceof CompressGroup) {
				resList.add((CompressGroup) nextObj);
			}
		}
		return resList;
	}
}

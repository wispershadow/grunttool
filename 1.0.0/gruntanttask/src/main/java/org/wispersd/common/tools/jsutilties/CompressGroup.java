package org.wispersd.common.tools.jsutilties;

import java.util.ArrayList;
import java.util.List;

public class CompressGroup {
	private final String sourceFileName;
	private final String groupName;
	private final List<String> itemList;
	private final List<String> originalList;

	public CompressGroup(String sourceFileName, String groupName) {
		this.sourceFileName = sourceFileName;
		this.groupName = groupName;
		this.itemList = new ArrayList<String>();
		this.originalList = new ArrayList<String>();
	}

	public void addPathToCompressGroup(String path, String original) {
		this.itemList.add(path);
		this.originalList.add(original);
	}

	public void addPathsToCompressGroup(List<String> paths, String original) {
		this.itemList.addAll(paths);
		this.originalList.add(original);
	}

	public void addOriginalToGroup(String original) {
		this.originalList.add(original);
	}
	
	public String getSourceFileName() {
		return sourceFileName;
	}

	public boolean hasItems() {
		return (!(this.itemList.isEmpty()));
	}

	public List<String> getItemList() {
		return this.itemList;
	}

	public List<String> getOriginalList() {
		return this.originalList;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void moveAll(CompressGroups groups) {
		for (int i = 0; i < this.itemList.size(); ++i) {
			String originalLine = (String) this.originalList.get(i);
			groups.addItem(originalLine);
		}
		this.itemList.clear();
	}

	public void clear() {
		this.itemList.clear();
	}

	public String toString() {
		return "CompressGroup [groupName=" + this.groupName + ", itemList="
				+ this.itemList + "]";
	}
}

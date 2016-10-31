package org.wispersd.common.tools.jsutilties;

import java.util.Map;

public class OutputFileUtils {
	
	public static String getOutputFileActualPath(CompressGroup curCompressGroup, Map<String, String> groupOutputPathMapping, String suffix) {
		String groupName = curCompressGroup.getGroupName();
		String prefix = groupOutputPathMapping.get(groupName);
		if (prefix != null) {
			return prefix + groupName + suffix; 
		}
		return null;
	}

	public static String getOutputFileDispPath(CompressGroup curCompressGroup, Map<String, String> groupOutputPathMapping) {
		String groupName = curCompressGroup.getGroupName();
		String prefix = groupOutputPathMapping.get(groupName);
		if (prefix != null) {
			if (prefix.endsWith("\\") || prefix.endsWith("/")) {
				return prefix.substring(0, prefix.length()-1);
			}
			else {
				return prefix;
			}
		}
		return curCompressGroup.getOriginalList().get(0);
	}
}

package org.wispersd.common.tools.jsutilties.processors;

import org.wispersd.common.tools.CommonConstants;
import org.wispersd.common.tools.jsutilties.CompressGroup;
import org.wispersd.common.tools.jsutilties.CompressGroups;
import org.wispersd.common.tools.jsutilties.CompressSourceLineProcessor;
import org.wispersd.common.tools.jsutilties.ParseUtils;
import org.wispersd.common.tools.jsutilties.SourceLineProcessContext;

public class GroupStartProcessor implements CompressSourceLineProcessor{

	public CompressGroup processSourceLine(String line, CompressGroup curGroup,
			CompressGroups allGroups, SourceLineProcessContext context) {
		if (curGroup != null) {
			curGroup.moveAll(allGroups);
		}
		String tagName = (String) context.getGlobalProps().get(CommonConstants.ATTR_COMBINESTARTTAG);
		String sourcePath = (String)context.getLocalVars().get(CommonConstants.ATTR_SOURCEPATH);
		return new CompressGroup(sourcePath, ParseUtils.getCompressGroupName(line, tagName));
	}

}

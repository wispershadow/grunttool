package org.wispersd.common.tools.jsutilties.processors;

import org.wispersd.common.tools.CommonConstants;
import org.wispersd.common.tools.jsutilties.CompressGroup;
import org.wispersd.common.tools.jsutilties.CompressGroups;
import org.wispersd.common.tools.jsutilties.CompressSourceLineProcessor;
import org.wispersd.common.tools.jsutilties.SourceLineProcessContext;

public class CommentLineStartProcessor implements CompressSourceLineProcessor{

	public CompressGroup processSourceLine(String line, CompressGroup curGroup,
			CompressGroups allGroups, SourceLineProcessContext context) {
		context.addLocalVariable(CommonConstants.ATTR_WITHINCOMMENT, Boolean.valueOf(true));
		allGroups.addItem(line);
		return curGroup;
	}

}

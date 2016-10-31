package org.wispersd.common.tools.jsutilties.processors;

import org.wispersd.common.tools.jsutilties.CompressGroup;
import org.wispersd.common.tools.jsutilties.CompressGroups;
import org.wispersd.common.tools.jsutilties.CompressSourceLineProcessor;
import org.wispersd.common.tools.jsutilties.SourceLineProcessContext;

public class GroupEndProcessor implements CompressSourceLineProcessor{

	public CompressGroup processSourceLine(String line, CompressGroup curGroup, CompressGroups allGroups, SourceLineProcessContext context) {
		allGroups.addItem(curGroup);
        return null;
	}

}

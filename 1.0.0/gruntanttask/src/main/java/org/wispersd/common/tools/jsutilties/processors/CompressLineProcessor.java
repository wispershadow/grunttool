package org.wispersd.common.tools.jsutilties.processors;

import org.wispersd.common.tools.CommonConstants;
import org.wispersd.common.tools.jsutilties.CompressGroup;
import org.wispersd.common.tools.jsutilties.CompressGroups;
import org.wispersd.common.tools.jsutilties.CompressSourceLineProcessor;
import org.wispersd.common.tools.jsutilties.ParseUtils;
import org.wispersd.common.tools.jsutilties.SourceLineProcessContext;

public class CompressLineProcessor implements CompressSourceLineProcessor{

	public CompressGroup processSourceLine(String line, CompressGroup curGroup,
			CompressGroups allGroups, SourceLineProcessContext context) {
		 String elementToParse = (String)context.getLocalVars().get(CommonConstants.ATTR_ELEMENTTOPARSE);
		 String sourceAttribute = (String)context.getLocalVars().get(CommonConstants.ATTR_SOURCEATTRIB);
		 String sourcePath = ParseUtils.extractSourceFromLine(line, allGroups,  elementToParse, sourceAttribute);
         if (sourcePath != null) {
			curGroup.addPathToCompressGroup(sourcePath, line);
         }
         else {
           allGroups.addItem(line);
         }
         return curGroup;
	}

}

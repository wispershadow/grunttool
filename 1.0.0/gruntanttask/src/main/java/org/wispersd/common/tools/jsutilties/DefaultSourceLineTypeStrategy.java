package org.wispersd.common.tools.jsutilties;

import org.wispersd.common.tools.CommonConstants;

public class DefaultSourceLineTypeStrategy implements SourceLineTypeStrategy{

	public LineTypes getLineType(String trimmedLine, CompressGroup curGroup,
			CompressGroups allGroups, SourceLineProcessContext context) {
		String elementToParse = (String)context.getLocalVars().get(CommonConstants.ATTR_ELEMENTTOPARSE);
	    String sourceAttribute = (String)context.getLocalVars().get(CommonConstants.ATTR_SOURCEATTRIB);
	    Boolean withinComment = (Boolean)context.getLocalVars().get(CommonConstants.ATTR_WITHINCOMMENT);
	    if (ParseUtils.isHtmlCommentStart(trimmedLine)) {
	    	if (ParseUtils.isSingleLineHtmlComment(trimmedLine)) {
	    		if (ParseUtils.isGroupStart(trimmedLine, context.getGlobalProps())) {
	    			return LineTypes.COMPRESSGROUP_START;
	    		}
	    		else if (ParseUtils.isGroupEnd(trimmedLine, context.getGlobalProps(), curGroup)) {
	    			return LineTypes.COMPRESSGROUP_END;
	    		}
	    		return LineTypes.NORMAL_LINE;
	    	}
	    	else {
	    		return LineTypes.COMMENTLINE_START;
	    	}
	    }
	    else if (ParseUtils.isJspCommentStart(trimmedLine)) {
	    	if (ParseUtils.isSingleLineJspComment(trimmedLine)) {
	    		return LineTypes.NORMAL_LINE;
	    	}
	    	else {
	    		return LineTypes.COMMENTLINE_START;
	    	}
	    }
	    else if (ParseUtils.isCommentEnd(trimmedLine)) {
	    	return LineTypes.COMMENTLINE_END;
	    }
	    else {
	    	if (ParseUtils.isComposeGroupLine(trimmedLine, elementToParse, sourceAttribute)) { 
	    		if (withinComment != null && withinComment.booleanValue()) {
	    			return LineTypes.NORMAL_LINE;
	    		}
	    		else if (curGroup == null) {
	    			return LineTypes.NORMAL_LINE;
	    		}
	    		return LineTypes.COMPRESSGROUP_LINE;
	    	}
	    	else {
	    		return LineTypes.NORMAL_LINE;
	    	}
	    }
	}

}

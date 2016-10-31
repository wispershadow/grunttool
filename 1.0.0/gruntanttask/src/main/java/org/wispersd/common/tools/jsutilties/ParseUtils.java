package org.wispersd.common.tools.jsutilties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.wispersd.common.tools.CommonConstants;

public class ParseUtils {
	private static final String HTML_COMMENTSTART = "<!--";
	private static final String HTML_COMMENTEND = "-->";
	private static final String JSP_COMMENTSTART = "<%--";
	private static final String JSP_COMMENTEND = "--%>";
	private static final String PLACEHOLDER_PATTERN = "\\$\\{[^\\}]*\\}";
	private static final Pattern p = Pattern.compile(PLACEHOLDER_PATTERN);
	
	public static boolean isComposeGroupLine(String curLine, String elementToParse, String sourceAttribute) {
		return curLine.startsWith(elementToParse) && curLine.indexOf(sourceAttribute) >= 0;
	}
	
	public static boolean isHtmlCommentStart(String curLine) {
		return curLine.startsWith(HTML_COMMENTSTART);
	}
	
	public static boolean isJspCommentStart(String curLine) {
		return curLine.startsWith(JSP_COMMENTSTART);
	}
	
	public static boolean isSingleLineHtmlComment(String curLine) {
		return curLine.startsWith(HTML_COMMENTSTART) && curLine.endsWith(HTML_COMMENTEND);
	}
	
	public static boolean isSingleLineJspComment(String curLine) {
		return curLine.startsWith(JSP_COMMENTSTART) && curLine.endsWith(JSP_COMMENTEND);
	}
	
	
	public static boolean isCommentEnd(String curLine) {
		return curLine.startsWith(HTML_COMMENTEND) || curLine.startsWith(JSP_COMMENTEND);
	}
	
	public static boolean isGroupStart(String curLine, Properties p) {
		String startPattern = p.getProperty(CommonConstants.ATTR_COMBINESTARTTAG);
		return curLine.startsWith(HTML_COMMENTSTART) && curLine.indexOf(startPattern) >= 0 && curLine.endsWith(HTML_COMMENTEND);
	}
	
	public static boolean isGroupEnd(String curLine, Properties p, CompressGroup curGroup) {
		String endPattern = p.getProperty(CommonConstants.ATTR_COMBINEENDTAG);
		boolean patternMatch = curLine.startsWith(HTML_COMMENTSTART) && curLine.indexOf(endPattern) >= 0 && curLine.endsWith(HTML_COMMENTEND); 
		if (patternMatch) {
			String curGroupName = getCompressGroupName(curLine, endPattern);
			return curGroup.getGroupName().equals(curGroupName);
		}
		else {
			return false;
		}
	}
	
	public static String getCompressGroupName(String curLine, String tagName) {
		int startInd = curLine.indexOf(tagName) + tagName.length();
		int endInd = curLine.length() - HTML_COMMENTEND.length();
		return StringUtils.trim(curLine.substring(startInd, endInd));
	}
	
	public static String extractSourceFromLine(String line,
			CompressGroups allGroups, String elementToParse,
			String sourceAttribute) {
		if (line.startsWith(elementToParse)) {
			StringBuilder result = new StringBuilder();
			int srcIndex = line.indexOf(sourceAttribute);

			if (srcIndex >= 0) {
				int state = 0;
				for (int i = srcIndex + sourceAttribute.length(); i < line.length(); ++i) {
					char c = line.charAt(i);
					if ((state == 0) && (c == '=')) {
						state = 1;
					} else if ((state == 1) && (c == '\'')) {
						state = 2;
					} else if ((state == 1) && (c == '"')) {
						state = 2;
					} else if (state == 2) {
						if ((c == '\'') || (c == '"'))
							break;
						result.append(c);
					}

				}

				//replace all ${} placeholders in js/css path with actual path value
				String jsPath = result.toString();
				Matcher m = p.matcher(jsPath);
				List<String> allPlaceholderNames = new ArrayList<String>();
				List<String> allPlaceholderValues = new ArrayList<String>();
				while(m.find()) {
					String nextPlaceholderName = m.group();
					String nextPlaceholderValue = allGroups.getPathValue(nextPlaceholderName.substring(2, nextPlaceholderName.length()-1));
					if (nextPlaceholderValue != null) {
						allPlaceholderNames.add(nextPlaceholderName);
						allPlaceholderValues.add(nextPlaceholderValue);
					}
				}
				String res= StringUtils.replaceEach(jsPath, allPlaceholderNames.toArray(new String[allPlaceholderNames.size()]), allPlaceholderValues.toArray(new String[allPlaceholderValues.size()]));
				return StringUtils.replaceChars(res, '\\', '/');
			}
		}
		return null;

	}
	
}

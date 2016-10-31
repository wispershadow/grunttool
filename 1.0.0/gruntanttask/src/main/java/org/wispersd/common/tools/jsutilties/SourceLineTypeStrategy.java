package org.wispersd.common.tools.jsutilties;

public interface SourceLineTypeStrategy {
	public LineTypes getLineType(String line, CompressGroup curGroup, CompressGroups allGroups, SourceLineProcessContext context);
}

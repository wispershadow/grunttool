package org.wispersd.common.tools.jsutilties;


public interface CompressSourceLineProcessor {
	
	public CompressGroup processSourceLine(String line, CompressGroup curGroup, CompressGroups allGroups, SourceLineProcessContext context);

}

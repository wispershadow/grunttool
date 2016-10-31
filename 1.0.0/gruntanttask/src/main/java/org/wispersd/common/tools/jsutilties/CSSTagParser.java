package org.wispersd.common.tools.jsutilties;

import java.util.Map;
import java.util.Properties;

public class CSSTagParser extends AbstractCompressSourceParser{
	private static final String CSS_START = "<link";
	private static final String ATTR_HREF = "href";
	
	public CSSTagParser(Properties props,
			SourceLineTypeStrategy sourceLineTypeStrategy,
			Map<LineTypes, CompressSourceLineProcessor> lineProcessors) {
		super(props, sourceLineTypeStrategy, lineProcessors);
	}

	@Override
	protected String getElementToParse() {
		return CSS_START;
	}

	@Override
	protected String getSourceAttribute() {
		return ATTR_HREF;
	}

}

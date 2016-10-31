package org.wispersd.common.tools.jsutilties;

import java.util.Map;
import java.util.Properties;

public class JsTagParser extends AbstractCompressSourceParser{

	private static final String SCRIPT_START = "<script";
	private static final String ATTR_SRC = "src";
	
	public JsTagParser(Properties props,
			SourceLineTypeStrategy sourceLineTypeStrategy,
			Map<LineTypes, CompressSourceLineProcessor> lineProcessors) {
		super(props, sourceLineTypeStrategy, lineProcessors);
	}

	@Override
	protected String getElementToParse() {
		return SCRIPT_START;
	}

	@Override
	protected String getSourceAttribute() {
		return ATTR_SRC;
	}

}

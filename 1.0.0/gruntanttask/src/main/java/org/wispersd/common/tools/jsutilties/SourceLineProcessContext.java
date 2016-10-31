package org.wispersd.common.tools.jsutilties;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SourceLineProcessContext {
	private final Properties globalProps;
	private final Map<String, Object> params;
	private final Map<String, Object> localVars;

	public SourceLineProcessContext(Properties globalProps,
			Map<String, Object> params) {
		this.globalProps = globalProps;
		this.params = params;
		this.localVars = new HashMap<String, Object>();
	}

	public void addLocalVariable(String varName, Object varValue) {
		localVars.put(varName, varValue);
	}
	
	public void clearLocalVariable(String varName) {
		localVars.remove(varName);
	}

	public Properties getGlobalProps() {
		return globalProps;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public Map<String, Object> getLocalVars() {
		return localVars;
	}
}

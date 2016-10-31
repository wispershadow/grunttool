package org.wispersd.common.tools.jsutilties;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.wispersd.common.tools.CommonConstants;

public abstract class AbstractCompressSourceParser {
	private Properties props;
	private SourceLineTypeStrategy sourceLineTypeStrategy;
	private Map<LineTypes, CompressSourceLineProcessor> lineProcessors;
	
	public AbstractCompressSourceParser(Properties props, SourceLineTypeStrategy sourceLineTypeStrategy, Map<LineTypes, CompressSourceLineProcessor> lineProcessors) {
		this.props = props;
		this.sourceLineTypeStrategy = sourceLineTypeStrategy;
		this.lineProcessors = lineProcessors;
	}

	public CompressGroups parseItemSource(String sourceFile,
			Map<String, Object> additionalAttribs) throws Exception {
		SourceLineProcessContext context = createSourceLineProcessContext(sourceFile, additionalAttribs);
		BufferedReader br = null;
		try {
			CompressGroup curGroup = null;
			CompressGroups allCompressGroups = new CompressGroups();
			addCommonPath(context, allCompressGroups);
			br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile)));
			String nextLine = br.readLine();
			 while (nextLine != null) {
				 String trimmedLine = StringUtils.trim(nextLine);
				 LineTypes curLineType = sourceLineTypeStrategy.getLineType(trimmedLine, curGroup, allCompressGroups, context);
				 CompressSourceLineProcessor lineProcessor = lineProcessors.get(curLineType);
				 if (lineProcessor != null) {
					 curGroup = lineProcessor.processSourceLine(trimmedLine, curGroup, allCompressGroups, context);
				 }
				 nextLine = br.readLine();
			 }
			 if (curGroup != null)
		      {
		        curGroup.moveAll(allCompressGroups);
		      }
		      allCompressGroups.setVersion(generateCompressVersion());
		      return allCompressGroups;

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					System.out.println("Error closing source file " + e.getMessage());
				}
		}
		return null;
	}
	
	
	protected abstract String getElementToParse();

	protected abstract String getSourceAttribute();
	
	protected SourceLineProcessContext createSourceLineProcessContext(String sourceFile, Map<String, Object> additionalAttribs) { 
		SourceLineProcessContext context = new SourceLineProcessContext(props, additionalAttribs);
		context.addLocalVariable(CommonConstants.ATTR_SOURCEPATH, sourceFile);
		context.addLocalVariable(CommonConstants.ATTR_ELEMENTTOPARSE, getElementToParse());
		context.addLocalVariable(CommonConstants.ATTR_SOURCEATTRIB, getSourceAttribute());
		return context;
	}
	
	protected void addCommonPath(SourceLineProcessContext context, CompressGroups allCompressGroups) {
		Map<String, Object> params = context.getParams();
		for(String nextKeyStr: params.keySet()) {
			if (nextKeyStr.startsWith(CommonConstants.PROPPREFIX_COMMONPATH)) {
				String pathName = nextKeyStr.substring(CommonConstants.PROPPREFIX_COMMONPATH.length());
				allCompressGroups.addCommonPath(pathName, (String)params.get(nextKeyStr));
			}
		}
	}
	
	private static String generateCompressVersion()
	  {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	    return sdf.format(new Date());
	  }

}

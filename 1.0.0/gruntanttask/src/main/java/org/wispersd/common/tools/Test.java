package org.wispersd.common.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.wispersd.common.tools.jsutilties.CompressGroup;
import org.wispersd.common.tools.jsutilties.CompressSourceLineProcessor;
import org.wispersd.common.tools.jsutilties.DefaultSourceLineTypeStrategy;
import org.wispersd.common.tools.jsutilties.JsTagParser;
import org.wispersd.common.tools.jsutilties.LineTypes;
import org.wispersd.common.tools.jsutilties.SourceLineTypeStrategy;
import org.wispersd.common.tools.jsutilties.processors.CommentLineEndProcessor;
import org.wispersd.common.tools.jsutilties.processors.CommentLineStartProcessor;
import org.wispersd.common.tools.jsutilties.processors.CompressLineProcessor;
import org.wispersd.common.tools.jsutilties.processors.GroupEndProcessor;
import org.wispersd.common.tools.jsutilties.processors.GroupStartProcessor;
import org.wispersd.common.tools.jsutilties.processors.NormalLineProcessor;

public class Test {
	public static void main(String[] args) {
		try {
			InputStream is = Test.class.getClassLoader().getResourceAsStream("minifytask.properties");
			Properties p = new Properties();
			p.load(is);
			SourceLineTypeStrategy sourceLineTypeStrategy = new DefaultSourceLineTypeStrategy();
			Map<LineTypes, CompressSourceLineProcessor> lineProcessors = new HashMap<LineTypes, CompressSourceLineProcessor>();
			lineProcessors.put(LineTypes.NORMAL_LINE, new NormalLineProcessor());
			lineProcessors.put(LineTypes.COMPRESSGROUP_START, new GroupStartProcessor());
			lineProcessors.put(LineTypes.COMPRESSGROUP_END, new GroupEndProcessor());
			lineProcessors.put(LineTypes.COMMENTLINE_START, new CommentLineStartProcessor());
			lineProcessors.put(LineTypes.COMMENTLINE_END, new CommentLineEndProcessor());
			lineProcessors.put(LineTypes.COMPRESSGROUP_LINE, new CompressLineProcessor());
			JsTagParser parser = new JsTagParser(p, sourceLineTypeStrategy, lineProcessors);
			Map<String, Object> additionalAttribs = new HashMap<String, Object>();
			additionalAttribs.put("commonpath.commonResourcePath", "C:\\hybris\\57\\hybris\\bin\\ext-template\\yacceleratorstorefront\\web\\webroot\\_ui\\desktop\\common");
			additionalAttribs.put("commonpath.themeResourcePath", "C:\\hybris\\57\\hybris\\bin\\ext-template\\yacceleratorstorefront\\web\\webroot\\_ui\\desktop\\theme-black");
			String sourceFile = "C:\\hybris\\57\\hybris\\bin\\ext-template\\yacceleratorstorefront\\web\\webroot\\WEB-INF\\tags\\desktop\\template\\compressible\\js.tag";
			List<CompressGroup> groups = parser.parseItemSource(sourceFile, additionalAttribs).getCompressGroups();
			for(CompressGroup nextGrp: groups) {
				System.out.println(nextGrp.getGroupName() + "  " + nextGrp.getSourceFileName());
				System.out.println(nextGrp.getOriginalList());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

package org.wispersd.common.tools.jsutilties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.wispersd.common.tools.CommonConstants;

public class GruntTemplateGenerator {
	public void generateTemplate(List<CompressGroups> cssCompressGroups, List<CompressGroups> jsCompressGroups, String templateFullPath, String outputFileName) {
		VelocityEngine ve = new VelocityEngine();
		File f = new File(templateFullPath);
		String templateName = f.getName();

		Properties p = new Properties();
		p.put("resource.loader", "file");
		p.put("file.resource.loader.class","org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		p.put("file.resource.loader.path", f.getParent());
		ve.init(p);

		VelocityContext context = new VelocityContext();
		if (cssCompressGroups != null) {
			CompressGroups combinedCssGrp = new CompressGroups();
			for(CompressGroups nextCssGrps: cssCompressGroups) {
				List<CompressGroup> compressGrpList = nextCssGrps.getCompressGroups();
				for(CompressGroup nextCompressGrp: compressGrpList) {
					combinedCssGrp.addItem(nextCompressGrp);
				}
			}
			context.put(CommonConstants.ATTR_CSSCOMPRESSGROUP,combinedCssGrp);
		}
		
		if (jsCompressGroups != null) {
			CompressGroups combinedJsGrp = new CompressGroups();
			for(CompressGroups nextJsGrps: jsCompressGroups) {
				List<CompressGroup> compressGrpList = nextJsGrps.getCompressGroups();
				for(CompressGroup nextCompressGrp: compressGrpList) {
					combinedJsGrp.addItem(nextCompressGrp);
				}
			}
			context.put(CommonConstants.ATTR_JSCOMPRESSGROUP, combinedJsGrp);
		}
		
		PrintWriter pr = null;
		Template template = null;
		try {
			template = ve.getTemplate(templateName);
			pr = new PrintWriter(new FileOutputStream(outputFileName));
			template.merge(context, pr);
			pr.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (pr != null)
				pr.close();
		}
	}
}

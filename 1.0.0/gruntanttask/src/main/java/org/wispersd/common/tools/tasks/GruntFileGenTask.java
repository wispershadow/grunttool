package org.wispersd.common.tools.tasks;

import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.wispersd.common.tools.jsutilties.CompressGroups;
import org.wispersd.common.tools.jsutilties.GruntTemplateGenerator;

public class GruntFileGenTask extends Task implements TaskConstants {
	private static final String PROPNAME_GRUNTHOME = "grunt.home";
	private static final String GRUNTTEMPLATE_NAME = "Gruntfile.js.template";
	private static final String GRUNTFILE_NAME = "Gruntfile.js";

	public void execute() throws BuildException {
		String gruntfileTemplate = getProject().getProperty(PROPNAME_GRUNTHOME) + "/" + GRUNTTEMPLATE_NAME;
		String outputFile = getProject().getProperty(PROPNAME_GRUNTHOME) + "/" + GRUNTFILE_NAME;
		List<CompressGroups> cssresult = (List<CompressGroups>) getProject().getReference(CSSCOMPRESSGROUP);
		List<CompressGroups> jsresult = (List<CompressGroups>) getProject().getReference(JSCOMPRESSGROUP);

		GruntTemplateGenerator generator = new GruntTemplateGenerator();
		getProject().log("------------------------ start generating grunt js file from template: " + gruntfileTemplate);
		generator.generateTemplate(cssresult, jsresult, gruntfileTemplate, outputFile);
		getProject().log("------------------------ end generating grunt js file");
	}
}

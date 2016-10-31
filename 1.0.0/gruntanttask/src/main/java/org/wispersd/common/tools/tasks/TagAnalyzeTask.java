package org.wispersd.common.tools.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;
import org.wispersd.common.tools.jsutilties.AbstractCompressSourceParser;
import org.wispersd.common.tools.jsutilties.CSSTagParser;
import org.wispersd.common.tools.jsutilties.CompressGroup;
import org.wispersd.common.tools.jsutilties.CompressGroups;
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

public class TagAnalyzeTask extends Task implements TaskConstants {
	protected Map<String, ResourceCollection> filesets = new HashMap<String, ResourceCollection>();
	protected List<PlaceholderRef> placeholderRefs = new ArrayList<PlaceholderRef>();
	private static final String SUFFIX_BAK = "bak";
	private static final String FILESET_CSS = "css";
	private static final String FILESET_JS = "js";
	public static final Map<LineTypes, CompressSourceLineProcessor> lineProcessors = new HashMap<LineTypes, CompressSourceLineProcessor>();
	
	static {
		lineProcessors.put(LineTypes.NORMAL_LINE, new NormalLineProcessor());
		lineProcessors.put(LineTypes.COMPRESSGROUP_START, new GroupStartProcessor());
		lineProcessors.put(LineTypes.COMPRESSGROUP_END, new GroupEndProcessor());
		lineProcessors.put(LineTypes.COMMENTLINE_START, new CommentLineStartProcessor());
		lineProcessors.put(LineTypes.COMMENTLINE_END, new CommentLineEndProcessor());
		lineProcessors.put(LineTypes.COMPRESSGROUP_LINE, new CompressLineProcessor());
	}

	public void addCssFiles(FileSet set) {
		this.filesets.put(FILESET_CSS, set);
	}

	public void addJsFiles(FileSet set) {
		this.filesets.put(FILESET_JS, set);
	}
	
	public void addPlaceholderRef(PlaceholderRef placeholderRef) {
		this.placeholderRefs.add(placeholderRef);
	}
	
	public void execute() throws BuildException {
		SourceLineTypeStrategy sourceLineTypeStrategy = new DefaultSourceLineTypeStrategy();
		File baseDir = null;
		FileSet cssFiles = (FileSet) this.filesets.get(FILESET_CSS);
		FileSet jsFiles = (FileSet) this.filesets.get(FILESET_JS);
		Properties p = this.buildProperties();
		Map<String, Object> additionalAttribs = new HashMap<String, Object>();
		for(PlaceholderRef nextPlaceholderRef: placeholderRefs) {
			additionalAttribs.put(nextPlaceholderRef.getName(), nextPlaceholderRef.getRef());
		}
		
		DirectoryScanner ds;
		if (cssFiles != null) {
			baseDir = cssFiles.getDir();
			ds = cssFiles.getDirectoryScanner(getProject());
			List<CompressGroups> cssresult = process(baseDir, ds, new CSSTagParser(p, sourceLineTypeStrategy, lineProcessors), additionalAttribs);
			getProject().addReference(CSSCOMPRESSGROUP, cssresult);
			logCompressGroup("css", cssresult, this.getProject());
		}
		if (jsFiles != null) {
			baseDir = jsFiles.getDir();
			ds = jsFiles.getDirectoryScanner(getProject());
			List<CompressGroups> jsresult = process(baseDir, ds, new JsTagParser(p, sourceLineTypeStrategy, lineProcessors), additionalAttribs);
			getProject().addReference(JSCOMPRESSGROUP, jsresult);
			logCompressGroup("js", jsresult, this.getProject());
		}
	}
	
	protected Properties buildProperties() {
		Hashtable<String, Object> projProps = this.getProject().getProperties();
		Properties p = new Properties();
		for(Entry<String, Object> nextEntry: projProps.entrySet()) {
			if (nextEntry.getValue() instanceof String) {
				p.put(nextEntry.getKey(), (String)nextEntry.getValue());
			}
		}
		return p;
	}
	
	private static void logCompressGroup(String title, List<CompressGroups> compressGroups, Project project) {
		for(CompressGroups nextGrps: compressGroups) {
			List<CompressGroup> allCompressGroups = nextGrps.getCompressGroups();
			for(CompressGroup nextGrp: allCompressGroups) {
				project.log("Next " + title + " groups: " + nextGrp, Project.MSG_DEBUG);
			}	
		}
	}

	protected List<CompressGroups> process(File baseDir, DirectoryScanner ds,AbstractCompressSourceParser parser, Map<String, Object> additionalAttribs) {
		List<CompressGroups> finalGroups = new ArrayList<CompressGroups>();
	    String[] includedFiles = ds.getIncludedFiles();
	    for (String nextIncludeFile : includedFiles) {
	    	String bakFile = nextIncludeFile + "." + SUFFIX_BAK;
	        File fileToProcess = new File(baseDir, bakFile);
	        if (!(fileToProcess.exists())) {
	          fileToProcess = new File(baseDir, nextIncludeFile);
	        }
	        if (fileToProcess.exists()) {
	          try {
	            String filePath = fileToProcess.getAbsolutePath();
	            getProject().log("-------------------------starting parsing file: " + filePath);
	            CompressGroups curGroups = parser.parseItemSource(filePath, additionalAttribs);
	            finalGroups.add(curGroups);
	            getProject().log("-------------------------successfully parsed css tag file");
	          }
	          catch (Exception e) {
	            e.printStackTrace();
	          }
	        }
	        else {
	          getProject().log("Error! file to process does not exist: " + fileToProcess.getAbsolutePath());
	        }
	    }
	    return finalGroups;
	}
}

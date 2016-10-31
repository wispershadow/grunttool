package org.wispersd.common.tools.tasks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.ResourceCollection;
import org.wispersd.common.tools.jsutilties.CompressGroup;
import org.wispersd.common.tools.jsutilties.CompressGroups;
import org.wispersd.common.tools.jsutilties.OutputFileUtils;
import org.wispersd.common.tools.jsutilties.TagFileDecorator;

public class TagFileModifyTask extends Task implements TaskConstants {
	protected Map<String, ResourceCollection> filesets = new HashMap<String, ResourceCollection>();
	protected List<OutputPathMapping> outputPathMappings = new ArrayList<OutputPathMapping>();
	protected String sourcePath;
	
	private static final String SUFFIX_BAK = "bak";
	private static final String FILESET_CSS = "css";
	private static final String FILESET_JS = "js";
	
	public void addCssFiles(FileSet set) {
		this.filesets.put(FILESET_CSS, set);
	}

	public void addJsFiles(FileSet set) {
		this.filesets.put(FILESET_JS, set);
	}
	
	public void addOutputPathMapping(OutputPathMapping outputPathMapping) {
		this.outputPathMappings.add(outputPathMapping);
	}
	
	
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
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
	
	protected Map<String, String> buildMappingForDisplay() {
		Map<String, String> result = new HashMap<String, String>();
		for(OutputPathMapping nextPathMapping: outputPathMappings) {
			result.put(nextPathMapping.getGroupName(), nextPathMapping.getDisplayPrefix());
		}
		return result;
	}
	
	
	protected Map<String, String> buildMappingForOutput() {
		Map<String, String> result = new HashMap<String, String>();
		for(OutputPathMapping nextPathMapping: outputPathMappings) {
			result.put(nextPathMapping.getGroupName(), nextPathMapping.getOutputPath());
		}
		return result;
	}

	public void execute() throws BuildException {
		File baseDir = null;
		FileSet cssFiles = (FileSet) this.filesets.get(FILESET_CSS);
		FileSet jsFiles = (FileSet) this.filesets.get(FILESET_JS);
		Properties p = this.buildProperties();
		Map<String, String> mappingFileDisplay = buildMappingForDisplay();
		Map<String, String> mappingFileOutput = buildMappingForOutput();
		
		DirectoryScanner ds;
		if (cssFiles != null) {
			baseDir = cssFiles.getDir();
			ds = cssFiles.getDirectoryScanner(getProject());
			List<CompressGroups> cssresult = (List<CompressGroups>) getProject().getReference(CSSCOMPRESSGROUP);
			process(baseDir, ds, cssresult, 1, mappingFileDisplay, mappingFileOutput, p);
		}
		if (jsFiles != null) {
			baseDir = jsFiles.getDir();
			ds = jsFiles.getDirectoryScanner(getProject());
			List<CompressGroups> jsresult = (List<CompressGroups>) getProject().getReference(JSCOMPRESSGROUP);
			process(baseDir, ds, jsresult, 0, mappingFileDisplay, mappingFileOutput, p);
		}
	}

	protected void process(File baseDir, DirectoryScanner ds, List<CompressGroups> curGroupList, int type, 
			Map<String, String> mappingFileDisplay, Map<String, String> mappingFileOutput, Properties p) {
		String[] includedFiles = ds.getIncludedFiles();
		int i = 0;
		
		for (String nextIncludeFile : includedFiles) {
			CompressGroups curCompressGroups = curGroupList.get(i);
			String bakFileName = nextIncludeFile + "." + SUFFIX_BAK;
			File fileToProcess = new File(baseDir, nextIncludeFile);
			File bakFile = new File(baseDir, bakFileName);
			boolean genBackup = false;
			if (!(bakFile.exists())) {
				genBackup = true;
			}
			String filepath = fileToProcess.getAbsolutePath();
			TagFileDecorator decorator = new TagFileDecorator();
			getProject().log("------ generate file path: " + filepath + " gen backup: " + genBackup);
			decorator.generateOutputFile(filepath, curCompressGroups, mappingFileDisplay, genBackup, type, p);
			
			String suffix = null;
			if (type == 0) {
				suffix = ".min.js";
			}
			else if (type == 1) {
				suffix = ".min.css";
			}
			List<CompressGroup> lst = curCompressGroups.getCompressGroups();
			for(CompressGroup nextCompGrp: lst) {
				String fromPath = sourcePath + nextCompGrp.getGroupName() + suffix; 
				String targetPath = OutputFileUtils.getOutputFileActualPath(nextCompGrp, mappingFileOutput, suffix);
				try {
					Files.copy(Paths.get(fromPath), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			i++;
		}
		
	}

}

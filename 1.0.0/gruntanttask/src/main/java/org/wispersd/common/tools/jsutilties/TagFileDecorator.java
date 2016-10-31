package org.wispersd.common.tools.jsutilties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.wispersd.common.tools.CommonConstants;

public class TagFileDecorator {

	public void generateOutputFile(String tagPath,CompressGroups compressGroups, Map<String, String> groupOutputPathMapping, 
			boolean genBackup, int type, Properties p) {
		File originalTagFile = new File(tagPath);
		String originalTagFileName = originalTagFile.getName();
		String originalTagFilePath = originalTagFile.getParent();
		if (genBackup) {
			String backupFileName = genBackupFileName(originalTagFileName);
			File backupFile = new File(originalTagFilePath, backupFileName);
			originalTagFile.renameTo(backupFile);
		}

		PrintWriter pr = null;
		try {
			pr = new PrintWriter(new FileOutputStream(tagPath));
		    List<Object> itemList = compressGroups.getItemList();
			for (Object nextItem : itemList) {
				if (nextItem instanceof String) {
					pr.println((String) nextItem);
				} else if (nextItem instanceof CompressGroup) {
					CompressGroup compressGroup = (CompressGroup) nextItem;
					pr.println(p.getProperty(CommonConstants.ATTR_CHOOSETAGSTART));
					String outputFileName = null;
					String messageTemplate = null;
					String suffix = null;
					if (type == 0) {
						messageTemplate = p.getProperty(CommonConstants.ATTR_OUTPUTJSTPLT);
						suffix = ".min.js";
					}
					else if (type == 1) {
						messageTemplate = p.getProperty(CommonConstants.ATTR_OUTPUTCSSTPLT);
						suffix = ".min.css";
					}
					
					String templatePath = OutputFileUtils.getOutputFileDispPath(compressGroup, groupOutputPathMapping);
					outputFileName = compressGroup.getGroupName() + suffix;
					pr.println(MessageFormat.format(messageTemplate, templatePath, outputFileName));
					
					pr.println(p.getProperty(CommonConstants.ATTR_CHOOSETAGMIDDLE));
					for (String nextLine : compressGroup.getOriginalList()) {
						pr.println(nextLine);
					}
					pr.println(p.getProperty(CommonConstants.ATTR_CHOOSETAGEND));
				}
				pr.flush();
			}
			pr.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pr != null)
				try {
					pr.close();
				} catch (Exception localException2) {
				}
		}
	}
	
	
	
	
	private static final String genBackupFileName(String originalTagFileName) {
		StringBuilder res = new StringBuilder(originalTagFileName).append(".bak");
		return res.toString();
	}

}

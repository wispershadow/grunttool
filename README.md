# grunttool
A ant based grunt tool that compress css/js


How to use:
1. Download and install node js engine from https://nodejs.org/en/download/    npm install -g grunt-cli

2. Define how you want to combine and compress your css and js files in html/jsp/tag declaration file
  Eg. 
   <!-- combinestart:commonjs -->
   <script type="text/javascript" src="${commonResourcePath}/js/jquery.query-2.1.7.js"></script>
   <script type="text/javascript" src="${commonResourcePath}/js/jquery-ui-1.11.2.min.js"></script>
   ....
   <!-- combineend:commonjs -->
will combine all js files within the pair of html comments, and generate a single compressed js file named commonjs.min.js. The html comments must start with combinestart: and combineend: and follow by the combine group name, which is also the prefix of the output file name

3. Go to "dist" folder, change ant configuration. most often, you will change the "dir" and included files for the two fileset "cssfileset" and "jsfileset", those should usually be html/jsp/tag file that contains declaration of css/js files to combine. 
In addition "outputPathMapping" will need to be changed. 
The "name" attribute should match each compress group's name declared. 
The "displayPrefix" attribute defines a new path prefix for the combined css/js in the modified htm/jsp/tag declaration file  
The "outputPath" attribute defines where the combined/compressed css/js file should be written into, usually it should be the same folder as the source js/css file, otherwise, embedded image path in css file may break.
4. make sure that apache ant is installed and execute "ant -buildfile compressionant.xml storefrontcompression"

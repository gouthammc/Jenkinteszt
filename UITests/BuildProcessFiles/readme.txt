This file contents the instructions to run the build automation process.

Prerequsites :- 
1. Eclipse should be zipped with SWTBOT installed. For this you can take a fresh eclipse, 
unzip that and install SWTBOT.
2. Go to eclipse plugin folder and then search for folder org.eclipse.swtbot.eclipse.junit4.headless_**** where **** will be swtbot version and open that folder. 
3. Open library.xml and change its timeout property from 7200000 to 21600000 and save the file.(This property can be change to some more higher value if your JVM is exiting unexpectedly)
4. Then zip the eclipse again (zip should be done of eclipse folder that means under eclipse folder all eclipse files should be there, so zip the root folder i.e. eclipse)
5. There should not be any space in path location in build.properties.
6. Latest code has been taken in TFS.
7. Ant is installed.

Instructions to run the automation  : 
1. Go to git repo. cd <<gitrepo>>/UITests/BuildProcessFiles. 
2. Open build.properties and change the values as per your environment.   
 
    or 
	
	go to setEnv.bat and change the values as per your environment. Instead of using this file you can set 
	environment variables from command prompt as well.
       
3.Go to <<gitrepo>>/wap4ej/PluginsAndFeatures from command prompt , execute maven task 
   "mvn clean compile install" to generate binaries   

4 To run automation test cases , go to <<gitrepo>>/UITests/BuildProcessFiles from command prompt
	and execute the command 
                      "ant" if you are using build.properties
					  "ant -f build-env.xml" if you are setting environment variables uisng bat file or from command prompt.

4. This runs automation and results will be stored inside eclipse home "results" folder.

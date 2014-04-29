@rem This is the location where build process will take place.It can be any path on your machine.
set ECLIPSE_BUILDLOC=C:/automationtestsws/build

@rem his is the location of eclipse project git repository.
set GIT_REPO_LOC=C:/git/wap4ej

@rem The location and name of zip, where the eclipse zip is already placed.
set ECLIPSE_ZIP_LOC=C:/eclipsesoftware/eclipse.zip

@rem Library file location for swtbot. This is the file which is used by swtbot to run the test cases. This file can be found at
@rem your eclipse installation folder/plugins/org.eclipse.swtbot.junit4.headlessXXX/library.xml
set SWTLIB_FILELOC=eclipse/plugins/org.eclipse.swtbot.eclipse.junit4.headless_2.0.5.20111003_1754-3676ac8-dev-e36/library.xml

@rem Machine architecture
@rem For 32 bit - value should be x86 ; for 64 bit - value should be x86_64
set MACHINE_ARCH=x86_64
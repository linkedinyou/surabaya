Those are the settings and files that differ from a plain JBoss Installation.

In "bin" you will find some Config files and startup scripts.

	If JBoss should be profiled with JProfiler 7.x the "standalone_jprofiler.sh" script has to be used in order to start JBoss. The script expects the binaries to be in "/home/opensim/jprofiler7"
	If JBoss should be profiled with AppDynamics the "standalone_appdyn.sh" script expects the "standalone_appdyn.conf" to be in the same directory and the Serverside AppDynamics binaries to be in /opt/jboss/appdyn 
 
In "modules" you will find the necessary module to run JBoss and Infinispan with a Berkeley Database to store the cached objects.

In "standalone" you will find the properties and configurations I use  in my installation.
	"standalone.xml" is configured for Infinispan with "Berkeley Database"	



package eu.openminted.omtdshareontology;

import java.io.File;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import eu.openminted.workflows.galaxy.GalaxySectionGenerator;
import eu.openminted.workflows.galaxy.GalaxyWrapperGeneratorMain;

public class Main implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(GalaxyWrapperGeneratorMain.class);

	String ontologyPath;
	String toolsFolder;
	String rootFolderAtGalaxyTools;
	String toolBoxXML;
	String parentClassInTheOntology;

	private void setTest(){
		ontologyPath = "/home/ilsp/Desktop/classificationComponentCalssification/ontology.xml";
		parentClassInTheOntology = "http://w3id.org/meta-share/omtd-share/ComponentType";
		
		toolsFolder = "/home/ilsp/Desktop/classificationComponentCalssification/";
		rootFolderAtGalaxyTools = "omtdTools";
		toolBoxXML = "/home/ilsp/Desktop/classificationComponentCalssification/classification.xml";
	}
	
	public static void main(String args[]){
		
		log.info("...");
		SpringApplication app = new SpringApplication(Main.class);
		app.setWebEnvironment(false);
		// app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		log.info("DONE!");
	}
	
	@Override
	public void run(String... args) throws Exception {
		ontologyPath = args[0];
		parentClassInTheOntology = args[1];
		
		parentClassInTheOntology = args[2]; 
		rootFolderAtGalaxyTools = args[3];
		toolBoxXML = args[4];
		
		//setTest();
		
		OWLReader reader = new OWLReader();
		reader.load(ontologyPath);
		ArrayList<String> listOfFolders = reader.getClassificationItems(parentClassInTheOntology);
		
		GalaxySectionGenerator galaxySectionGenerator = new GalaxySectionGenerator();
		for(int i = 0; i < listOfFolders.size(); i++){
			String folderName = listOfFolders.get(i);
			String sectionID = "omtd" + folderName;
			String sectionName = folderName;
			
			galaxySectionGenerator.addSection(sectionID, sectionName, GalaxySectionGenerator.Dir_Type);
			galaxySectionGenerator.addDir(rootFolderAtGalaxyTools + "/" + folderName);		
			
			String finalDirName = toolsFolder + "/" + rootFolderAtGalaxyTools + "/" + folderName;
			File dir = new File(finalDirName);
			dir.mkdirs();
		}	

		galaxySectionGenerator.write(toolBoxXML);		
	}
}

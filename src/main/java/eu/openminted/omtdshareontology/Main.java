package eu.openminted.omtdshareontology;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import eu.openminted.workflows.galaxy.GalaxySectionGenerator;
import eu.openminted.workflows.galaxy.GalaxyWrapperGeneratorMain;

public class Main implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(GalaxyWrapperGeneratorMain.class);

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
		String ontologyPath = "/home/ilsp/Desktop/classificationComponentCalssification/ontology.xml";
		//String rootFolderAtGalaxyTools = args[1];
		String rootFolderAtGalaxyTools = "omtdTools";
		String toolBoxXML = "/home/ilsp/Desktop/classificationComponentCalssification/classification.xml";
		String parent = "http://w3id.org/meta-share/omtd-share/ComponentType";
		
		OWLReader reader = new OWLReader();
		reader.load(ontologyPath);
		ArrayList<String> listOfFolders = reader.getClassificationItems(parent);
		
		GalaxySectionGenerator galaxySectionGenerator = new GalaxySectionGenerator();
		for(int i = 0; i < listOfFolders.size(); i++){
			String folderName = listOfFolders.get(i);
			galaxySectionGenerator.addSection("omtd" + folderName, folderName, GalaxySectionGenerator.Dir_Type);
			galaxySectionGenerator.addDir(rootFolderAtGalaxyTools + "/" + folderName);			
		}	

		galaxySectionGenerator.write(toolBoxXML);		
	}
}

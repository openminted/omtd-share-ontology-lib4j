package eu.openminted.omtdshareontology;

import java.io.File;
import java.util.ArrayList;

import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import eu.openminted.workflows.galaxy.GalaxySectionGenerator;
import eu.openminted.workflows.galaxy.GalaxyWrapperGeneratorMain;

public class Main implements CommandLineRunner{

	private static final Logger log = LoggerFactory.getLogger(GalaxyWrapperGeneratorMain.class);

	String ontologyPath;
	String outputFolder;
	String toolBoxXML;
	String parentClassInTheOntology;

	private void setTest(){
		ontologyPath = "/home/ilsp/Desktop/classificationComponentCalssification/ontology.xml";
		parentClassInTheOntology = "http://w3id.org/meta-share/omtd-share/ComponentType";
		
		outputFolder = "/home/ilsp/Desktop/classificationComponentCalssification/";
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

		setTest();
		
		OWLOntManager ontMan = new OWLOntManager();
		//ontMan.load(ontologyPath);
		ontMan.load();

		SectionGen gen = new SectionGen(ontMan);
		
		ArrayList<OWLClass> listOfFolders = ontMan.getClassificationItems(parentClassInTheOntology);
		
		GalaxySectionGenerator galaxySectionGenerator = new GalaxySectionGenerator();
		for(int i = 0; i < listOfFolders.size(); i++){
			OWLClass owlClass = listOfFolders.get(i);
			Section gs = gen.generate(owlClass);

			galaxySectionGenerator.addSection(gs.getSectionID(), gs.getSectionName(), GalaxySectionGenerator.Dir_Type);
			galaxySectionGenerator.addDir(gs.getFolderPath());		
			
			String finalDirName = outputFolder + "/" + gs.getFolderPath();
			File dir = new File(finalDirName);
			dir.mkdirs();
		}	

		galaxySectionGenerator.write(toolBoxXML);		
	}
	

}

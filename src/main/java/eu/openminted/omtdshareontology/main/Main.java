package eu.openminted.omtdshareontology.main;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import org.apache.log4j.LogManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import eu.openminted.omtdshareontology.OWLOntManager;
import eu.openminted.omtdshareontology.Section;
import eu.openminted.omtdshareontology.SectionGen;
import eu.openminted.registry.domain.Component;
import eu.openminted.workflows.galaxy.GalaxySectionGenerator;
import eu.openminted.workflows.galaxy.GalaxyWrapperGeneratorMain;
import eu.openminted.workflows.galaxywrappers.OMTDSHAREParser;

public class Main implements CommandLineRunner {

	private static final org.apache.log4j.Logger log = LogManager.getLogger(Main.class);

	String ontologyPath;
	String outputFolder;
	String toolBoxXML;
	//String parentClassInTheOntology;
	String oldFolders;

	OWLOntManager ontMan;
	GalaxySectionGenerator galaxySectionGenerator;
	SectionGen gen;
	OMTDSHAREParser omtdShareParser;

	private void setTest() {
		ontologyPath = "/home/ilsp/Desktop/OMTDTemp/TDMCompClassification/ontology.xml";
		//parentClassInTheOntology = "http://w3id.org/meta-share/omtd-share/ComponentType";

		outputFolder = "/home/ilsp/Desktop/OMTDTemp/TDMCompClassification";
		toolBoxXML = "/home/ilsp/Desktop/OMTDTemp/TDMCompClassification/classification.xml";

		oldFolders = "/home/ilsp/Desktop/OMTDTemp/TDMCompClassification/omtdToolsClfnByDS/";
	}

	public static void main(String args[]) {

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

		init();
		generateClassification();
		//assignAll(new File(oldFolders));
	}

	private void init() {
		ontMan = new OWLOntManager();
		// ontMan.load(ontologyPath);
		ontMan.load();
		gen = new SectionGen(ontMan);
		galaxySectionGenerator = new GalaxySectionGenerator();
		
		omtdShareParser = new OMTDSHAREParser();
	}

	private void generateClassification() {
		ArrayList<OWLClass> listOfFolders = ontMan.getComponentTypeClassificationItems();
		System.out.println("size:" + listOfFolders.size());
		for (int i = 0; i < listOfFolders.size(); i++) {
			OWLClass owlClass = listOfFolders.get(i);
			Section gs = gen.generate(owlClass);

			galaxySectionGenerator.addSection(gs.getSectionID(), gs.getSectionName(), GalaxySectionGenerator.Dir_Type);
			galaxySectionGenerator.addDir(gs.getFolderPath());

			String finalDirName = outputFolder + "/" + gs.getFolderPath();
			//log.info("Creating dir:" + finalDirName);
			System.out.println("Creating dir:" + finalDirName);
			File dir = new File(finalDirName);
			dir.mkdirs();
		}

		galaxySectionGenerator.write(toolBoxXML);
	}

	public void assignAll(File dir) {
		File[] files = dir.listFiles();

		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					assignAll(file);
				} else {
					assign(file);
				}
			}
		}
	}

	public void assign(File omtdShareFile) {
		System.out.println(omtdShareFile.getAbsolutePath());
		Component comp = omtdShareParser.parse(omtdShareFile);
		String iri = comp.getComponentInfo().getFunctionInfo().getFunction().value().toString();
		System.out.println("iri:" + iri);
		Section sec = gen.generate(iri);
		
		String nFolder = outputFolder + sec.getFolderPath() + "/";
		System.out.println("new folder path:" + nFolder);
		
		try{
			// TO-DO: Regenerate 
            Files.copy(Paths.get(omtdShareFile.getAbsolutePath()), Paths.get(nFolder + omtdShareFile.getName()), StandardCopyOption.REPLACE_EXISTING);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}

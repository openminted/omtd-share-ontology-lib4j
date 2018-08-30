package eu.openminted.omtdshareontology;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ilsp
 *
 */
public class OWLReader {

	public final static Logger logger = LoggerFactory.getLogger(OWLReader.class.getName());

	private OWLOntologyManager manager = null;
	private OWLOntology ontology = null;
	private OWLDataFactory owlDataFactory = null;
	private OWLReasoner reasoner = null;
	private OWLReasonerFactory factory;
	private ConsoleProgressMonitor progressMonitor;
	private OWLReasonerConfiguration config;
	
	/**
	 * Constructor
	 */
	public OWLReader() {
		manager = OWLManager.createOWLOntologyManager();
		owlDataFactory = manager.getOWLDataFactory();
		factory = new StructuralReasonerFactory();
		
		progressMonitor = new ConsoleProgressMonitor();
		config = new SimpleConfiguration(progressMonitor);
	}

	/**
	 * Load an ontology from file.
	 * @param fileName
	 */
	public void load(String fileName) {
		try {
			File owlfileHandler = new File(fileName);

			if (owlfileHandler.exists()) {
				System.out.println("Loading:" + owlfileHandler.getAbsolutePath());
				ontology = manager.loadOntologyFromOntologyDocument(owlfileHandler);
				reasoner =  factory.createReasoner(ontology, config);
			} else {
				ontology = null;
				System.out.println("Does not exist:" + owlfileHandler.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<IRI> getSubClasses(IRI parentIRI){
		currentLevel++;
		OWLClass parent = owlDataFactory.getOWLClass(parentIRI);
		ArrayList<IRI> subclassesList = new ArrayList<IRI>();
		
		// Get subclasses
		NodeSet<OWLClass> subClasses = reasoner.getSubClasses(parent, true);
		Set<OWLClass> subClassesFlattened = subClasses.getFlattened();
		for (OWLClass subcl : subClassesFlattened) {
			System.out.println(" subclass" + subcl + " " + subcl.getIRI().getShortForm());
			subclassesList.add(subcl.getIRI());
			
			if(currentLevel + 1 <= maxLevel){
				subclassesList.addAll(getSubClasses(subcl.getIRI()));
			}
		}
		
		currentLevel--;
		return subclassesList;
	}
	
	private int currentLevel;
	private int maxLevel;
	
	public ArrayList<String> getClassificationItems(String str){
		ArrayList<String> foldersList = new ArrayList<String>();
		// Root class.
		IRI parentIRI = IRI.create(str);
		
		currentLevel = 0;
		maxLevel = 2;
		ArrayList<IRI> allSubclasses = getSubClasses(parentIRI);
		
		for(int i = 0; i < allSubclasses.size(); i++){
			foldersList.add(allSubclasses.get(i).getShortForm());
		}
		
		return foldersList;
	}
}

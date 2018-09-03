package eu.openminted.omtdshareontology;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
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
public class OWLOntManager {

	public final static Logger logger = LoggerFactory.getLogger(OWLOntManager.class.getName());

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
	public OWLOntManager() {
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
			logger.info("error:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void load(){
		try{
			InputStream inputStream = OWLOntManager.class.getResourceAsStream("/ontology.xml");
			ontology = manager.loadOntologyFromOntologyDocument(inputStream);
			reasoner =  factory.createReasoner(ontology, config);
		}catch(Exception e){
			logger.info("error:" + e.getMessage());
		}
	}
	
	public OWLClass getOWLClass(String IRI){
		OWLClass owlIRI = owlDataFactory.getOWLClass(IRI);
		return owlIRI;
	}
	
	private ArrayList<OWLClass> getSubClasses(IRI parentIRI){
		currentLevel++;
		OWLClass parent = owlDataFactory.getOWLClass(parentIRI);
		ArrayList<OWLClass> subclassesList = new ArrayList<OWLClass>();
		
		// Get subclasses
		NodeSet<OWLClass> subClasses = reasoner.getSubClasses(parent, true);
		Set<OWLClass> subClassesFlattened = subClasses.getFlattened();
		for (OWLClass subcl : subClassesFlattened) {
			System.out.println(" subclass" + subcl + " " + subcl.getIRI().getShortForm());
			subclassesList.add(subcl);
			
			if(currentLevel + 1 <= maxLevel){
				subclassesList.addAll(getSubClasses(subcl.getIRI()));
			}
		}
		
		currentLevel--;
		return subclassesList;
	}
	
	private int currentLevel;
	private int maxLevel;
	
	public ArrayList<OWLClass> getClassificationItems(String str){
		ArrayList<OWLClass> foldersList = new ArrayList<OWLClass>();
		// Root class.
		IRI parentIRI = IRI.create(str);
		
		currentLevel = 0;
		maxLevel = 2;
		ArrayList<OWLClass> allSubclasses = getSubClasses(parentIRI);
		
		for(int i = 0; i < allSubclasses.size(); i++){
			foldersList.add(allSubclasses.get(i));
		}
		
		return foldersList;
	}
	
	public String getLabelOfOWLClass(OWLClass owlClass){
		String ret = "";
		
		for(OWLAnnotationAssertionAxiom a : ontology.getAnnotationAssertionAxioms(owlClass.getIRI())) {
		    if(a.getProperty().isLabel()) {
		        if(a.getValue() instanceof OWLLiteral) {
		            OWLLiteral val = (OWLLiteral) a.getValue();
		            ret = val.getLiteral().toString();
		        }
		    }
		}
		
		return ret;
	}
	

}

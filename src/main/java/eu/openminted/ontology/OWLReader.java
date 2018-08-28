package eu.openminted.ontology;

import java.io.File;
import java.util.logging.LogManager;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ilsp
 *
 */
public class OWLReader {

	public final static Logger logger = LoggerFactory.getLogger(OWLReader.class.getName());
	
	private OWLOntologyManager manager;
	
	public OWLReader(){
		manager = OWLManager.createConcurrentOWLOntologyManager();
	}
	
	public void load(String fileName){
		try{
			File owlfileHandler = new File(fileName);
			
			if(owlfileHandler.exists()){
				System.out.println("Loading" + owlfileHandler.getAbsolutePath());
				manager.loadOntologyFromOntologyDocument(owlfileHandler);
			}else{
				System.out.println("Does not exist" + owlfileHandler.getAbsolutePath());
			}
		}catch(Exception e){
			
		}
	}
	
	
}

package eu.openminted.omtdshareontology;

import org.semanticweb.owlapi.model.OWLClass;

/**
 * @author ilsp
 *
 */
public class SectionGen {

	private OWLOntManager ontMan;
	
	public final static String rootFolderAtGalaxyTools = "omtdTools";
	public final static String idPrefix = "omtd";
	
	public SectionGen(OWLOntManager ontMan){
		this.ontMan = ontMan;
	}
	
	public Section generate(String classIRI){
		return generate(ontMan.getOWLClass(classIRI));
	}
	
	public Section generate(OWLClass owlClass){
		String folder = owlClass.getIRI().getShortForm();
		String sectionID = idPrefix + folder;
		String sectionName = ontMan.getLabelOfOWLClass(owlClass);
		
		Section sec = new Section();
		sec.setFolderPath(rootFolderAtGalaxyTools + "/" + folder);
		sec.setSectionID(sectionID);
		sec.setSectionName(sectionName);
		
		return sec;
	}

}

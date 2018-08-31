package eu.openminted.omtdshareontology;

import org.semanticweb.owlapi.model.OWLClass;

public class SectionGen {

	private OWLOntManager ontMan;
	
	public final static String rootFolderAtGalaxyTools = "omtdTools";
	public final static String idPrefix = "omtd";
	
	public SectionGen(OWLOntManager ontMan){
		this.ontMan = ontMan;
	}
	
	public Section generate(OWLClass owlClass){
		String folderPath = owlClass.getIRI().getShortForm();
		String sectionID = idPrefix + folderPath;
		String sectionName = ontMan.getLabelOfOWLClass(owlClass);
		
		Section sec = new Section();
		sec.setFolderPath(rootFolderAtGalaxyTools + "/" + folderPath);
		sec.setSectionID(sectionID);
		sec.setSectionName(sectionName);
		
		return sec;
	}

}

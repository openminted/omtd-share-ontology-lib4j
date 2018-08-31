package eu.openminted.omtdshareontology;

public class Section {

	public final static String rootFolderAtGalaxyTools = "omtdTools";

	private String folderPath;
	private String sectionID; 
	private String sectionName; 
	
	public String getFolderPath() {
		return folderPath;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	public String getSectionID() {
		return sectionID;
	}
	public void setSectionID(String sectionID) {
		this.sectionID = sectionID;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
		
}

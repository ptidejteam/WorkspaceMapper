package wsmapper;

import java.util.ArrayList;
import java.util.List;

public class Project {
	
	String name;
	List<Project> dependsOfList;
	
	
	public Project(String project) {
		this.name = project;
		this.dependsOfList = new ArrayList<Project>();
	}

	public void addDependency(Project node) {
		this.dependsOfList.add(node);
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<Project> getDependencies() {
		return this.dependsOfList;
	}
}


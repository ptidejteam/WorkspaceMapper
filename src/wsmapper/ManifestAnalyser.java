package wsmapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public class ManifestAnalyser {
	
	private static final String MANIFEST_FILE = "MANIFEST.MF";
	private static final String WORKSPACE = "/home/petrillo/eclipse-workspace/v5.2/";
	private static final int POSITION_PROJECT_NAME_IN_PATH = 5;
	
	static Map<String, Project> projects = new HashMap<String, Project>(); 

	public static void main(String[] args) {
		try {
			Files.walk(Paths.get(WORKSPACE))
	        .filter(Files::isRegularFile).filter(p -> p.toString().contains(MANIFEST_FILE))
	        .forEach((p) -> ManifestAnalyser.createProject(p));
			
			Files.walk(Paths.get(WORKSPACE))
	        .filter(Files::isRegularFile).filter(p -> p.toString().contains(MANIFEST_FILE))
	        .forEach((p) -> ManifestAnalyser.processManifest(p));
			
			generateGraph();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void generateGraph() {
		StringBuffer graph = new StringBuffer("digraph G {");
		projects.forEach((key, project)  -> addGraphNode(project, graph));
		graph.append("}");

		System.out.println(graph);
	}

	private static void addGraphNode(Project project, StringBuffer graph) {
		//graph.append(project.name + "\n");
		for (Project dep : project.dependsOfList) {
			graph.append("<" + project.name + "> -> <" + dep.name + ">\n");
		} 
	}

	private static Project createProject(Path path) {
		String projectName = path.toString().split("/")[POSITION_PROJECT_NAME_IN_PATH];
		Project project = new Project(projectName);
		projects.put(projectName, project);
		return project;
	}

	private static void processManifest(Path path) {
		Manifest manifest;
		try {
			String projectName = path.toString().split("/")[POSITION_PROJECT_NAME_IN_PATH];
			Project project = projects.get(projectName);
			
			manifest = new Manifest(new FileInputStream(path.toFile()));
			Attributes att = manifest.getMainAttributes();
			
			if(att.getValue("Require-Bundle") != null) {
				String[] depProjects = att.getValue("Require-Bundle").split(",");
				
				for (String depProjectName : depProjects) {
					depProjectName = depProjectName.split(";")[0];
					if (projects.containsKey(depProjectName)) {
						Project depProject = projects.get(depProjectName);
						project.addDependency(depProject);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
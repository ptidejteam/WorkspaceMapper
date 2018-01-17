package wsmapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DotProjectAnalyser {

	public static void main(String[] args) {
		try {
			Files.walk(Paths.get("/home/petrillo/eclipse-workspace/v5.2/"))
	        .filter(Files::isRegularFile).filter(p -> p.toString().contains("MANIFEST.MF"))
	        .forEach((p) -> DotProjectAnalyser.processManifest(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Files.walk(Paths.get("/home/petrillo/eclipse-workspace/v5.2/"))
	        .filter(Files::isRegularFile).filter(p -> p.toString().contains(".project"))
	        .forEach((p) -> DotProjectAnalyser.processDotProject(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void processDotProject(Path path) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(new FileInputStream(path.toFile()));
			document.getDocumentElement().normalize();
			
			NodeList nodes = document.getElementsByTagName("project");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
			    System.out.println("\nCurrent Element :" + node.getNodeName());
			}

			
			System.out.println(document.getChildNodes().item(5));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private static void processManifest(Path path) {
		Manifest manifest;
		try {
			manifest = new Manifest(new FileInputStream(path.toFile()));
			Attributes att = manifest.getMainAttributes();
			System.out.println(att.getValue("Require-Bundle"));	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

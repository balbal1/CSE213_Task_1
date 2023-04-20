import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

class NotVaildAutosarFileException extends Exception {
    public NotVaildAutosarFileException() {
        super("the file extention is not .arxml");
    }
}
class EmptyAutosarFileException extends RuntimeException {
    public EmptyAutosarFileException() {
        super("the arxml file is empty");
    }
}

public class Task {
    public static void main(String[] args)
    throws IOException, ParserConfigurationException, SAXException, TransformerConfigurationException, TransformerException, NotVaildAutosarFileException, EmptyAutosarFileException {
        
        String fileName = args[0];
        if (!fileName.substring(fileName.length()-6, fileName.length()).equals(".arxml")) {
            throw new NotVaildAutosarFileException();
        }

        if (new File(fileName).length() == 0) {
            throw new EmptyAutosarFileException();
        }

        fileName = fileName.substring(0, fileName.length()-6);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(fileName + ".arxml"));

        NodeList nodeList = document.getElementsByTagName("CONTAINER");
        List<String> unOrderedNames = new ArrayList<String>();
        List<String> orderedNames = new ArrayList<String>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            String shortName = nodeList.item(i).getChildNodes().item(1).getTextContent();
            unOrderedNames.add(shortName);
            orderedNames.add(shortName);
        }

        Collections.sort(orderedNames);
        Node unOrderedRoot = document.getDocumentElement();
        Node orderedRoot = document.createElement("AUTOSAR");
        
        for (int i = 0; i < nodeList.getLength(); i++) {
            orderedRoot.appendChild(nodeList.item(unOrderedNames.indexOf(orderedNames.get(i))).cloneNode(true));
        }

        document.removeChild(unOrderedRoot);
        document.appendChild(orderedRoot);

        FileOutputStream output = new FileOutputStream(fileName + "_mod.arxml");

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }
}
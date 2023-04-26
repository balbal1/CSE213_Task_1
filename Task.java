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
        
        // take input file name
        String fileName = args[0];
        // check if file extention is .arxml
        if (!fileName.substring(fileName.length()-6, fileName.length()).equals(".arxml")) {
            throw new NotVaildAutosarFileException();
        }
        // check if file is empty
        if (new File(fileName).length() == 0) {
            throw new EmptyAutosarFileException();
        }
        
        // remove the extention from the file name
        fileName = fileName.substring(0, fileName.length()-6);
        
        // take input xml file using w3c package
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(fileName + ".arxml"));
        
        // create a nodeList containing all CONTAINER tags
        NodeList nodeList = document.getElementsByTagName("CONTAINER");
        // create two String arrays for SHORT-NAME tags, one will be sorted while the other remains unsorted
        List<String> unOrderedNames = new ArrayList<String>();
        List<String> orderedNames = new ArrayList<String>();

        // iterate over all CONTAINER tags and put thier SHORT-NAME tags in the two arrays
        for (int i = 0; i < nodeList.getLength(); i++) {
            String shortName = nodeList.item(i).getChildNodes().item(1).getTextContent();
            unOrderedNames.add(shortName);
            orderedNames.add(shortName);
        }
        
        // sorts the first SHORT-NAME tags array
        Collections.sort(orderedNames);
        // get the AUTOSAR node from the current file
        Node unOrderedRoot = document.getDocumentElement();
        // create a new AUTOSAR node to append the CONTAINER tags in it after getting sorted
        Node orderedRoot = document.createElement("AUTOSAR");
        
        // iterate over the CONTAINER tags and append them in the new AUTOSAR tag in the correct order
        for (int i = 0; i < nodeList.getLength(); i++) {
            // we take i-th element in orderedNames array and get the index of this element in unOrderedNames array
            // so it takes the unsorted index of the CONTAINER tag to be able to fetch it from the original xml file.
            orderedRoot.appendChild(nodeList.item(unOrderedNames.indexOf(orderedNames.get(i))).cloneNode(true));
        }
        
        // remove the unsorted AUTOSAR tag and put the sorted one
        document.removeChild(unOrderedRoot);
        document.appendChild(orderedRoot);

        // name the output file with the proper name
        FileOutputStream output = new FileOutputStream(fileName + "_mod.arxml");
        // write the output arxml document in the output file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(output);
        transformer.transform(source, result);

    }
}

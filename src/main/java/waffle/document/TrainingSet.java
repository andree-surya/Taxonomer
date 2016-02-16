package waffle.document;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TrainingSet {

    private File file;

    public TrainingSet(File file) {
        this.file = file;
    }

    public List<Document> readDocuments() throws IOException {

        try {
            List<Document> documents = new LinkedList<Document>();

            DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = xmlDocumentBuilder.parse(file);

            NodeList categoryElements = xmlDocument.getElementsByTagName("category");

            // Traverse and parse list of categories.
            for (int i = 0; i < categoryElements.getLength(); i++) {

                Element categoryElement = (Element) categoryElements.item(i);
                String category = categoryElement.getAttribute("name");

                NodeList documentElements = categoryElement.getElementsByTagName("document");

                // Traverse and parse list of documents.
                for (int j = 0; j < documentElements.getLength(); j++) {

                    Element documentElement = (Element) documentElements.item(j);
                    URL documentUrl = new URL(documentElement.getTextContent());

                    documents.add(new HTMLDocument(documentUrl, category));
                }
            }

            return Collections.unmodifiableList(new ArrayList<Document>(documents));

        } catch (SAXException exception) {

            String errorMessage = "Invalid XML file: " + file;
            throw new RuntimeException(errorMessage, exception);

        } catch (ParserConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }
}

package waffle.classifier;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import waffle.document.Document;

public class NaiveBayesClassifier extends Classifier {

    private Map<String, Float> overallTokenChances;
    private Map<String, Map<String, Float>> categoricalTokenChances;

    public NaiveBayesClassifier() {
        overallTokenChances = new HashMap<String, Float>();
        categoricalTokenChances = new HashMap<String, Map<String, Float>>();
    }

    @Override
    public String classify(Document document) {
        // TODO Auto-generated method stub
        return null;
    }

    public void addFeatureToken(String token, float overallChance) {
        overallTokenChances.put(token, overallChance);
    }

    public void addFeatureToken(String token, String category, float categoricalChance) {

        Map<String, Float> tokenChances = categoricalTokenChances.get(category);

        if (tokenChances == null) {
            tokenChances = new HashMap<String, Float>();

            categoricalTokenChances.put(category, tokenChances);
        }

        tokenChances.put(token, categoricalChance);
    }

    public void saveModelToFile(File file) {

        try {
            DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = xmlDocumentBuilder.newDocument();

            Element rootElement = xmlDocument.createElement("occurrence-chances");
            Element overallElement = xmlDocument.createElement("overall");

            rootElement.appendChild(overallElement);

            for (Map.Entry<String, Float> entry : overallTokenChances.entrySet()) {
                Element tokenElement = xmlDocument.createElement("token");

                tokenElement.setAttribute("name", entry.getKey());
                tokenElement.setTextContent(entry.getValue().toString());

                overallElement.appendChild(tokenElement);
            }

            for (Map.Entry<String, Map<String, Float>> categoryEntry : categoricalTokenChances.entrySet()) {

                Element categoryElement = xmlDocument.createElement("category");
                categoryElement.setAttribute("name", categoryEntry.getKey());

                Map<String, Float> tokenChances = categoryEntry.getValue();

                for (Map.Entry<String, Float> entry : tokenChances.entrySet()) {
                    Element tokenElement = xmlDocument.createElement("token");

                    tokenElement.setAttribute("name", entry.getKey());
                    tokenElement.setTextContent(entry.getValue().toString());

                    categoryElement.appendChild(tokenElement);
                }

                rootElement.appendChild(categoryElement);
            }

            xmlDocument.appendChild(rootElement);

            Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer();
            xmlTransformer.transform(new DOMSource(xmlDocument), new StreamResult(file));

        } catch (ParserConfigurationException exception) {
            throw new RuntimeException(exception);

        } catch (TransformerException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void loadModelFromFile(File file) throws IOException {

        overallTokenChances.clear();
        categoricalTokenChances.clear();

        try {
            DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = xmlDocumentBuilder.parse(file);
            
            Element overallElement = (Element) xmlDocument.getElementsByTagName("overall").item(0);
            NodeList overallTokens = overallElement.getElementsByTagName("token");
            
            for (int i = 0; i < overallTokens.getLength(); i++) {
                Element tokenElement = (Element) overallTokens.item(i);
                
                String tokenName = tokenElement.getAttribute("name");
                Float tokenChance = Float.valueOf(tokenElement.getTextContent());
                
                overallTokenChances.put(tokenName, tokenChance);
            }
            
            NodeList categoryElements = xmlDocument.getElementsByTagName("category");
            
            for (int i = 0; i < categoryElements.getLength(); i++) {
                
                Element categoryElement = (Element) categoryElements.item(i);
                String categoryName = categoryElement.getAttribute("name");
                
                Map<String, Float> tokenChances = categoricalTokenChances.get(categoryName);
                
                if (tokenChances == null) {
                    tokenChances = new HashMap<String, Float>();
                    
                    categoricalTokenChances.put(categoryName, tokenChances);
                }
                
                NodeList categoryTokens = categoryElement.getElementsByTagName("token");
                
                for (int j = 0; j < categoryTokens.getLength(); j++) {
                    Element tokenElement = (Element) categoryTokens.item(j);
                    
                    String tokenName = tokenElement.getAttribute("name");
                    Float tokenChance = Float.valueOf(tokenElement.getTextContent());
                    
                    tokenChances.put(tokenName, tokenChance);
                }
            }
            
        } catch (SAXException exception) {
            
           String errorMessage = "Invalid XML file: " + file;
           throw new RuntimeException(errorMessage, exception);
            
        } catch (ParserConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }
}

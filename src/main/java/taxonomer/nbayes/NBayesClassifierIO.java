package taxonomer.nbayes;

import java.io.File;
import java.io.IOException;
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

public class NBayesClassifierIO {
    public static final File DEFAULT_FILE = new File("output/classifier-model.xml");

    public static NBayesClassifier readFromDefaultFile() throws IOException {
        return read(DEFAULT_FILE);
    }
    
    public static void writeToDefaultFile(NBayesClassifier classifier) {
        write(DEFAULT_FILE, classifier);
    }
    
    public static NBayesClassifier read(File inputFile) throws IOException {
        
        try {
            DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = xmlDocumentBuilder.parse(inputFile);
            
            NodeList categoryElements = xmlDocument.getElementsByTagName("category");
            NBayesClassifier classifier = new NBayesClassifier();
            
            for (int i = 0; i < categoryElements.getLength(); i++) {
                Element categoryElement = (Element) categoryElements.item(i);
                
                String categoryName = categoryElement.getAttribute("name");
                String defaultScore = categoryElement.getAttribute("default");
                
                IndexedScore tokenScores = new IndexedScore();
                tokenScores.setDefaultScore(Float.parseFloat(defaultScore));
                
                NodeList tokenElements = categoryElement.getElementsByTagName("token");
                
                for (int j = 0; j < tokenElements.getLength(); j++) {
                    Element tokenElement = (Element) tokenElements.item(j);
                    
                    String tokenName = tokenElement.getAttribute("name");
                    Float tokenScore = Float.valueOf(tokenElement.getTextContent());
                    
                    tokenScores.setScoreForKey(tokenName, tokenScore);
                }
                
                classifier.setTokenScoresForCategory(categoryName, tokenScores);
            }
            
            return classifier;
            
        } catch (SAXException exception) {
            
           String errorMessage = "Invalid XML file: " + inputFile;
           throw new RuntimeException(errorMessage, exception);
            
        } catch (ParserConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public static void write(File outputFile, NBayesClassifier classifier) {

        try {
            outputFile.mkdirs();
            
            DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = xmlDocumentBuilder.newDocument();

            Element rootElement = xmlDocument.createElement("classifier-model");
            
            Map<String, IndexedScore> tokenScoresByCategories = classifier.getClassifierModel();
            for (Map.Entry<String, IndexedScore> tokenScoresEntry : tokenScoresByCategories.entrySet()) {
                
                String category = tokenScoresEntry.getKey();
                Element categoryElement = xmlDocument.createElement("category");
                
                IndexedScore tokenScores = tokenScoresEntry.getValue();
                double defaultScore = tokenScores.getDefaultScore();
                
                categoryElement.setAttribute("name", category);
                categoryElement.setAttribute("default", Double.toString(defaultScore));
                
                for (String token : tokenScores.getKeySet()) {
                    
                    Double tokenScore = tokenScores.getScoreForKey(token);
                    Element tokenElement = xmlDocument.createElement("token");
                    
                    tokenElement.setAttribute("name", token);
                    tokenElement.setTextContent(Double.toString(tokenScore));
                    
                    categoryElement.appendChild(tokenElement);
                }
                
                rootElement.appendChild(categoryElement);
            }

            xmlDocument.appendChild(rootElement);

            Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer();
            xmlTransformer.transform(new DOMSource(xmlDocument), new StreamResult(outputFile));

        } catch (ParserConfigurationException exception) {
            throw new RuntimeException(exception);

        } catch (TransformerException exception) {
            throw new RuntimeException(exception);
        }
    }
}

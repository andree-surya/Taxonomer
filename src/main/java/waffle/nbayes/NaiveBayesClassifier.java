package waffle.nbayes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

import waffle.core.Classifier;
import waffle.core.Document;

public class NaiveBayesClassifier extends Classifier {

    private Map<String, IndexedScore> tokenScoresByCategories;
    
    public NaiveBayesClassifier() {
        tokenScoresByCategories = new HashMap<String, IndexedScore>();
    }
    
    @Override
    public String classify(Document document) {
        
        Set<String> uniqueTokens = new HashSet<String>(document.extractTokens());
        Map<String, Float> overallScoresByCategories = new HashMap<String, Float>();
        
        for (Map.Entry<String, IndexedScore> tokenScoresEntry : tokenScoresByCategories.entrySet()) {
            
            String category = tokenScoresEntry.getKey();
            IndexedScore tokenScores = tokenScoresEntry.getValue();
            
            float overallScore = 1;
            
            for (String token : uniqueTokens) {
                overallScore *= tokenScores.getScoreForKey(token);
            }
            
            overallScoresByCategories.put(category, overallScore);
        }
        
        Map.Entry<String, Float> bestScoreEntry = null;
        
        for (Map.Entry<String, Float> scoreEntry : overallScoresByCategories.entrySet()) {
            
            if (bestScoreEntry == null || bestScoreEntry.getValue() < scoreEntry.getValue() ) {
                bestScoreEntry = scoreEntry;
            }
        }
        
        return (bestScoreEntry != null) ? bestScoreEntry.getKey() : null; 
    }
    
    public void setTokenScoresForCategory(String category, IndexedScore tokenScores) {
        tokenScoresByCategories.put(category, tokenScores);
    }
    
    public void saveModelToFile(File file) {

        try {
            DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = xmlDocumentBuilder.newDocument();

            Element rootElement = xmlDocument.createElement("classifier-model");
            
            for (Map.Entry<String, IndexedScore> tokenScoresEntry : tokenScoresByCategories.entrySet()) {
                
                String category = tokenScoresEntry.getKey();
                Element categoryElement = xmlDocument.createElement("category");
                
                IndexedScore tokenScores = tokenScoresEntry.getValue();
                float defaultScore = tokenScores.getDefaultScore();
                
                categoryElement.setAttribute("name", category);
                categoryElement.setAttribute("default", Float.toString(defaultScore));
                
                for (String token : tokenScores.getKeySet()) {
                    
                    Float tokenScore = tokenScores.getScoreForKey(token);
                    Element tokenElement = xmlDocument.createElement("token");
                    
                    tokenElement.setAttribute("name", token);
                    tokenElement.setTextContent(Float.toString(tokenScore));
                    
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

        tokenScoresByCategories.clear();

        try {
            DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document xmlDocument = xmlDocumentBuilder.parse(file);
            
            NodeList categoryElements = xmlDocument.getElementsByTagName("category");
            
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
                
                tokenScoresByCategories.put(categoryName, tokenScores);
            }
            
        } catch (SAXException exception) {
            
           String errorMessage = "Invalid XML file: " + file;
           throw new RuntimeException(errorMessage, exception);
            
        } catch (ParserConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }
}

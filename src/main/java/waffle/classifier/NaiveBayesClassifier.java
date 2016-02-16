package waffle.classifier;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

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

            xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xmlTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            xmlTransformer.transform(new DOMSource(xmlDocument), new StreamResult(file));

        } catch (ParserConfigurationException exception) {
            throw new RuntimeException(exception);

        } catch (TransformerException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void loadModelFromFile(File file) {

        overallTokenChances.clear();
        categoricalTokenChances.clear();

    }
}

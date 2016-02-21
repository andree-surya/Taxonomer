package waffle.nbayes;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import waffle.core.ClassifierResult;
import waffle.core.Document;
import waffle.core.DocumentIO;

public class NBayesClassifierTest {

    @Test
    public void test() throws Exception {
        
        URL trainingSetUrl = getClass().getResource("/training-set.xml");
        URL testingSetUrl = getClass().getResource("/testing-set.xml");
        
        File trainingSetFile = new File(trainingSetUrl.toURI());
        File testingSetFile = new File(testingSetUrl.toURI());
        
        List<Document> trainingDocuments = DocumentIO.read(trainingSetFile);
        List<Document> testingDocuments = DocumentIO.read(testingSetFile);
        
        NBayesClassifierBuilder classifierBuilder = new NBayesClassifierBuilder();

        for (Document document : trainingDocuments) {
            System.out.printf("Processing %s ...\n", document.getUrl());
            classifierBuilder.addTokensFromDocument(document);
        }

        NBayesClassifier classifier = classifierBuilder.build();
        
        for (Document document : testingDocuments) {
            System.out.printf("Processing %s ...\n", document.getUrl());
            ClassifierResult result = classifier.classify(document);
            
            assertEquals(document.getCategory(), result.getMatchedCategory());
        }
    }

}

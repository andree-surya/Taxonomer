package waffle.classifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import waffle.document.Document;

public class NaiveBayesClassifierBuilder {

    private int overallDocumentCount;
    private Map<String, Integer> overallTokenCounts;

    private Map<String, Integer> categoricalDocumentCounts;
    private Map<String, Map<String, Integer>> categoricalTokenCounts;

    public NaiveBayesClassifierBuilder() {
        overallDocumentCount = 0;
        overallTokenCounts = new HashMap<String, Integer>();

        categoricalDocumentCounts = new HashMap<String, Integer>();
        categoricalTokenCounts = new HashMap<String, Map<String, Integer>>();
    }

    public void addTokensFromDocument(Document document) {

        String category = document.getCategory();

        Map<String, Integer> tokenCounts = categoricalTokenCounts.get(category);
        Set<String> uniqueTokens = new HashSet<String>(document.extractTokens());

        if (tokenCounts == null) {
            tokenCounts = new HashMap<String, Integer>();

            categoricalTokenCounts.put(category, tokenCounts);
        }

        // Increment token counters.
        for (String token : uniqueTokens) {

            Integer tokenCount = tokenCounts.get(token);
            Integer overallTokenCount = overallTokenCounts.get(token);

            if (tokenCount == null) {
                tokenCount = 0;
            }

            if (overallTokenCount == null) {
                overallTokenCount = 0;
            }

            tokenCounts.put(token, tokenCount + 1);
            overallTokenCounts.put(token, overallTokenCount + 1);
        }

        // Increment document counters.
        Integer categoricalDocumentCount = categoricalDocumentCounts.get(category);

        if (categoricalDocumentCount == null) {
            categoricalDocumentCount = 0;
        }

        categoricalDocumentCounts.put(category, categoricalDocumentCount + 1);
        overallDocumentCount += 1;
    }

    public NaiveBayesClassifier build() {
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();

        // Calculate occurrence chances of each token in the entire training
        // set.
        for (Map.Entry<String, Integer> entry : overallTokenCounts.entrySet()) {

            String token = entry.getKey();
            float tokenCount = entry.getValue();
            float overallChance = tokenCount / overallDocumentCount;

            classifier.addFeatureToken(token, overallChance);
        }

        // Calculate occurrence chances of each token in each category.
        for (Map.Entry<String, Map<String, Integer>> categoryEntry : categoricalTokenCounts.entrySet()) {

            String category = categoryEntry.getKey();

            int documentCount = categoricalDocumentCounts.get(category);
            Map<String, Integer> tokenCounts = categoryEntry.getValue();

            for (Map.Entry<String, Integer> entry : tokenCounts.entrySet()) {

                String token = entry.getKey();
                float tokenCount = entry.getValue();
                float categoricalChance = tokenCount / documentCount;

                classifier.addFeatureToken(token, category, categoricalChance);
            }
        }

        return classifier;
    }
}

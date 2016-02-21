package waffle.nbayes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import waffle.core.Classifier;
import waffle.core.Document;

public class NBayesClassifier implements Classifier {

    private Map<String, IndexedScore> tokenScoresByCategories;
    
    public NBayesClassifier() {
        tokenScoresByCategories = new HashMap<String, IndexedScore>();
    }
    
    public NBayesClassifierResult classify(Document document) {
        
        Set<String> uniqueTokens = new HashSet<String>(document.extractTokens());
        Map<String, Float> overallScoresByCategories = new HashMap<String, Float>();
        
        for (Map.Entry<String, IndexedScore> tokenScoresEntry : tokenScoresByCategories.entrySet()) {
            
            String category = tokenScoresEntry.getKey();
            IndexedScore tokenScores = tokenScoresEntry.getValue();
            
            float overallScore = 0;
            
            for (String token : uniqueTokens) {
                overallScore += tokenScores.getScoreForKey(token);
            }
            
            overallScoresByCategories.put(category, overallScore);
        }
        
        return new NBayesClassifierResult(overallScoresByCategories); 
    }
    
    public void setTokenScoresForCategory(String category, IndexedScore tokenScores) {
        tokenScoresByCategories.put(category, tokenScores);
    }
    
    public Map<String, IndexedScore> getClassifierModel() {
        return tokenScoresByCategories;
    }
}

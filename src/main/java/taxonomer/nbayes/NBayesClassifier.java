package taxonomer.nbayes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import taxonomer.core.Classifier;
import taxonomer.core.Document;

public class NBayesClassifier implements Classifier {

    private Map<String, IndexedScore> tokenScoresByCategories;
    
    public NBayesClassifier() {
        tokenScoresByCategories = new HashMap<String, IndexedScore>();
    }
    
    public NBayesClassifierResult classify(Document document) {
        
        Set<String> uniqueTokens = new HashSet<String>(document.extractTokens());
        Map<String, Double> overallScoresByCategories = new HashMap<String, Double>();
        
        for (Map.Entry<String, IndexedScore> tokenScoresEntry : tokenScoresByCategories.entrySet()) {
            
            String category = tokenScoresEntry.getKey();
            IndexedScore tokenScores = tokenScoresEntry.getValue();
            
            double overallScore = 0;
            
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

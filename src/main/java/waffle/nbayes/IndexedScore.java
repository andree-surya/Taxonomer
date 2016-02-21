package waffle.nbayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IndexedScore {
    
    private Map<String, Double> scoresByKeys;
    private double defaultScore = 0;
    
    public IndexedScore() {
        scoresByKeys = new HashMap<String, Double>();
    }
    
    public void setScoreForKey(String key, double score) {
        scoresByKeys.put(key, score);
    }
    
    public void setDefaultScore(double defaultScore) {
        this.defaultScore = defaultScore;
    }
    
    public double getScoreForKey(String key) {
        Double score = scoresByKeys.get(key);
        
        if (score == null) {
            return defaultScore;
        }
        
        return score;
    }
    
    public double getDefaultScore() {
        return defaultScore;
    }
    
    public Set<String> getKeySet() {
        return scoresByKeys.keySet();
    }
}

package waffle.nbayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IndexedScore {
    
    private Map<String, Float> scoresByKeys;
    private float defaultScore = 0;
    
    public IndexedScore() {
        scoresByKeys = new HashMap<String, Float>();
    }
    
    public void setScoreForKey(String key, float score) {
        scoresByKeys.put(key, score);
    }
    
    public void setDefaultScore(float defaultScore) {
        this.defaultScore = defaultScore;
    }
    
    public float getScoreForKey(String key) {
        Float score = scoresByKeys.get(key);
        
        if (score == null) {
            return defaultScore;
        }
        
        return score;
    }
    
    public float getDefaultScore() {
        return defaultScore;
    }
    
    public Set<String> getKeySet() {
        return scoresByKeys.keySet();
    }
}

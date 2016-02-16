package waffle.document;

import java.net.URL;
import java.util.List;

public abstract class Document {

    private URL url;
    private String category;

    public Document(URL url, String category) {
        this.url = url;
        this.category = category;
    }

    public URL getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    public abstract List<String> extractTokens();
}

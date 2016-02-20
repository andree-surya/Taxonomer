package waffle.core;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.atilika.kuromoji.Token;
import org.atilika.kuromoji.Tokenizer;
import org.jsoup.Jsoup;

public class HTMLDocument extends Document {

    private static Tokenizer tokenizer = Tokenizer.builder().build();

    public HTMLDocument(URL url, String category) {
        super(url, category);
    }

    @Override
    public List<String> extractTokens() {

        try {
            org.jsoup.nodes.Document document = Jsoup.connect(getUrl().toString()).get();
            String documentText = document.select("p").text();

            List<String> tokens = new LinkedList<String>();

            for (Token token : tokenizer.tokenize(documentText)) {

                if (token.getPartOfSpeech().startsWith("名詞")) {
                    tokens.add(token.getSurfaceForm());
                }
            }

            return tokens;

        } catch (IOException exception) {

            String errorMessage = "Failed to connect to " + getUrl();
            throw new RuntimeException(errorMessage, exception);
        }
    }
}

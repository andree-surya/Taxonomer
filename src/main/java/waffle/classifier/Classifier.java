package waffle.classifier;

import waffle.document.Document;

public abstract class Classifier {

    public abstract String classify(Document document);
}

**Taxonomer** demonstrates a simple Java-based supervised document classification system.

# Getting Started

You can confirm that the system works with `mvn test`.
This will run a [smoke test][1] for several minutes, building and validating a classifier by executing the following procedures.

#### 1. Documents Preparation

Read the [training][2] and [testing][3] sample files. Both files adopt a similar XML structure that describes a list of documents, each preassigned to a category.

#### 2. Feature Extraction

Load, parse, and tokenize each document in the training and testing set (see [HTMLDocument.java][4]).
To simplify this demo, a few assumptions were made regarding the format and language of the document.
The output of this phase is a list of unique tokens contained within the input document.

#### 3. Classification Model Building

Given tokens extracted from the previous phase, iteratively build a probabilistic classification model by calculating `P(y|x)` where `y ∈ {all categories}` and `x = token` (see [NBayesClassifierBuilder.java][5]).
Additive smoothings are included in model calculation to circumvent the zero-probability problem.

#### 4. Classification Model Testing

Extract unique tokens from each testing document.
Given these tokens and the probability model calculated from the previous phase, we can compute `P(y|X)` where `y ∈ {all categories}` and `X = vector of tokens` (see [NBayesClassifier.java][6]).
From this point on, the classification task can be simplified as a task of selecting category `y` which maximizes the function `P(y|X)` (see [NBayesClassifierResult.java][7]).

#### 5. Reporting

Report the overall accuracy of classifier against the given testing set.

# Limitations

This system is currently built for the sole sake of demonstration and a lot has to be taken care of before it can be considered for a production system.
The following is a non-exhaustive list of what needs to be done.

- Support for alternative document source specifications (e.g. local, remote, inline).
- Support for alternative document formats (e.g. plain text, HTML, PDF).
- Support for alternative document languages (e.g. English, non-Latin).
- Serializing, persisting, and deserializing the classification model.
- CLI or GUI support for ad-hoc training and classification task.

# License

This project is licensed under the [Apache License, Version 2.0](https://opensource.org/licenses/Apache-2.0).

[1]: src/test/java/taxonomer/nbayes/NBayesClassifierTest.java
[2]: src/test/resources/training-set.xml
[3]: src/test/resources/testing-set.xml
[4]: src/main/java/taxonomer/core/HTMLDocument.java
[5]: src/main/java/taxonomer/nbayes/NBayesClassifierBuilder.java
[6]: src/main/java/taxonomer/nbayes/NBayesClassifier.java
[7]: src/main/java/taxonomer/nbayes/NBayesClassifierResult.java

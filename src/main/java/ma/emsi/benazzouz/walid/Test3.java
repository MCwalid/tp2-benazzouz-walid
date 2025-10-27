package ma.emsi.benazzouz.walid;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;

import java.time.Duration;

public class Test3 {

    public static void main(String[] args) {

        // 1. Clé API
        String cle = System.getenv("GEMINI_KEY");

        // 2. Modèle embeddings (SANS dimensions)
        EmbeddingModel embeddingModel = GoogleAiEmbeddingModel.builder()
                .apiKey(cle)
                .modelName("text-embedding-004")   // modèle embeddings Gemini
                .timeout(Duration.ofSeconds(2))
                .build();

        // 3. Phrases
        String phrase1 = "J'adore le développement en Java";
        String phrase2 = "La programmation Java est vraiment agréable";
        String phrase3 = "Le soleil brille dans le ciel";
        String phrase4 = "J'aime manger des pizzas au fromage";

        // 4. Embeddings
        Embedding emb1 = embeddingModel.embed(phrase1).content();
        Embedding emb2 = embeddingModel.embed(phrase2).content();
        Embedding emb3 = embeddingModel.embed(phrase3).content();
        Embedding emb4 = embeddingModel.embed(phrase4).content();

        // 5. Similarité cosinus (manuelle)
        double sim12 = cosineSimilarity(emb1.vector(), emb2.vector());
        double sim34 = cosineSimilarity(emb3.vector(), emb4.vector());

        // 6. Résultat
        System.out.println("Similarité phrase1 / phrase2 (proches) : " + sim12);
        System.out.println("Similarité phrase3 / phrase4 (éloignées) : " + sim34);
    }

    // Méthode utilitaire cosine
    private static double cosineSimilarity(float[] v1, float[] v2) {
        double dot = 0, norm1 = 0, norm2 = 0;
        for (int i = 0; i < v1.length; i++) {
            dot += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}

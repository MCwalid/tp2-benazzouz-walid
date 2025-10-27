package ma.emsi.benazzouz.walid;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.util.Scanner;

public class Test5 {

    interface Assistant {
        String chat(String userMessage);
    }

    public static void main(String[] args) {

        // 1) Modèle Gemini
        ChatModel model = GoogleAiGeminiChatModel.builder()
                .apiKey(System.getenv("GEMINI_KEY"))
                .modelName("gemini-2.5-flash")
                .temperature(0.1)
                .build();

        // 2) Charger le PDF du support
        Document document = FileSystemDocumentLoader.loadDocument("ml.pdf");

        // 3) Vector store + embeddings
        EmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(document, store);

        // 4) Assistant avec RAG + Mémoire
        Assistant assistant =
                AiServices.builder(Assistant.class)
                        .chatModel(model)
                        .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                        .contentRetriever(EmbeddingStoreContentRetriever.from(store))
                        .build();

        // 5) Démarrer la conversation multi-tour
        conversationAvec(assistant);
    }

    private static void conversationAvec(Assistant assistant) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("==================================================");
                System.out.println("Posez votre question : ");
                String question = scanner.nextLine();

                if (question.isBlank()) {
                    continue;
                }
                if ("fin".equalsIgnoreCase(question)) {
                    System.out.println("Assistant : Au revoir !");
                    break;
                }

                System.out.println("==================================================");
                String reponse = assistant.chat(question);
                System.out.println("Assistant : " + reponse);
            }
        }
    }
}

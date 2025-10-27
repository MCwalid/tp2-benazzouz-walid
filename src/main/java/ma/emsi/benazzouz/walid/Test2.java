package ma.emsi.benazzouz.walid;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;

import java.util.Map;

public class Test2 {

    public static void main(String[] args) {

        // 1. Récupération de la clé API
        String cle = System.getenv("GEMINI_KEY");

        // 2. Création du modèle
        ChatModel modele = GoogleAiGeminiChatModel.builder()
                .apiKey(cle)
                .modelName("gemini-2.5-flash")
                .temperature(0.7)
                .build();

        // 3. Phrase à traduire
        String texteAFaireTraduire = "Bonjour, je suis étudiant en informatique et j’aime Java.";

        // 4. Création d'un PromptTemplate
        PromptTemplate template = PromptTemplate.from(
                "Traduis le texte suivant en anglais : {{texte}}"
        );

        // 5. Remplacement de la variable via une Map
        Map<String, Object> variables = Map.of("texte", texteAFaireTraduire);
        Prompt prompt = template.apply(variables);

        // 6. Envoi du prompt au modèle
        String reponse = modele.chat(prompt.text());

        // 7. Affichage
        System.out.println("Texte original : " + texteAFaireTraduire);
        System.out.println("Traduction : " + reponse);
    }
}

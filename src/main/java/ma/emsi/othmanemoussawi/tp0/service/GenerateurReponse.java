package ma.emsi.othmanemoussawi.tp0.service;

import jakarta.enterprise.context.Dependent;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Classe de service qui génère la réponse à une question.
 * Traitement personnalisé : mini-traducteur français vers darija (écrit en arabizi).
 */
@Dependent
public class GenerateurReponse implements Serializable {

    /** Dictionnaire français -> darija (arabizi). Les expressions de plusieurs mots sont acceptées. */
    private static final Map<String, String> DICTIONNAIRE = new LinkedHashMap<>();

    static {
        DICTIONNAIRE.put("au revoir", "bslama");
        DICTIONNAIRE.put("d'accord", "wakha");
        DICTIONNAIRE.put("ça va", "labas");
        DICTIONNAIRE.put("bonjour", "salam");
        DICTIONNAIRE.put("salut", "salam");
        DICTIONNAIRE.put("merci", "shukran");
        DICTIONNAIRE.put("oui", "iyeh");
        DICTIONNAIRE.put("non", "la");
        DICTIONNAIRE.put("beaucoup", "bzaf");
        DICTIONNAIRE.put("bien", "mzyan");
        DICTIONNAIRE.put("comment", "kifach");
        DICTIONNAIRE.put("pourquoi", "3lach");
        DICTIONNAIRE.put("où", "fin");
        DICTIONNAIRE.put("quoi", "chno");
        DICTIONNAIRE.put("qui", "chkoun");
        DICTIONNAIRE.put("maintenant", "daba");
        DICTIONNAIRE.put("demain", "ghedda");
        DICTIONNAIRE.put("aujourd'hui", "lyoum");
        DICTIONNAIRE.put("ami", "sahbi");
        DICTIONNAIRE.put("maison", "dar");
        DICTIONNAIRE.put("eau", "ma");
        DICTIONNAIRE.put("pain", "khobz");
        DICTIONNAIRE.put("thé", "atay");
        DICTIONNAIRE.put("argent", "flous");
        DICTIONNAIRE.put("travail", "khedma");
        DICTIONNAIRE.put("je", "ana");
        DICTIONNAIRE.put("tu", "nta");
        DICTIONNAIRE.put("avec", "m3a");
        DICTIONNAIRE.put("et", "w");
        DICTIONNAIRE.put("ici", "hna");
    }

    /** Motif qui reconnaît les entrées du dictionnaire, les plus longues d'abord. */
    private static final Pattern MOTIF = Pattern.compile(
            "(?<!\\p{L})(" + DICTIONNAIRE.keySet().stream()
                    .sorted((a, b) -> b.length() - a.length())
                    .map(Pattern::quote)
                    .collect(Collectors.joining("|")) + ")(?!\\p{L})",
            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    /** Rôle système qui active la traduction en darija (une seule ligne, sans saut de ligne). */
    public static final String ROLE_TRADUCTEUR_DARIJA =
            "You are a translator from French to Darija (Moroccan Arabic written in Latin letters).";

    private boolean roleSystemeAffiche = false;

    /**
     * Génère une réponse à une question : traduction mot à mot en darija (arabizi).
     * Le rôle système, s'il n'est pas null, est ajouté en majuscules au début de la première réponse.
     *
     * @param question La question à traduire.
     * @param roleSysteme Le rôle système à afficher au début de la première réponse.
     * @return La question traduite, suivie du nombre de mots traduits.
     */
    public String genererReponse(String question, String roleSysteme) {
        boolean darija = roleSysteme != null && roleSysteme.contains("French to Darija");
        StringBuilder resultat = new StringBuilder(darija ? "" : "||");
        if (!this.roleSystemeAffiche && roleSysteme != null) {
            resultat.append(roleSysteme.toUpperCase(Locale.FRENCH)).append("\n");
            this.roleSystemeAffiche = true;
        }

        if (!darija) {
            // Traitement d'origine du TP : question en minuscules entourée de "||".
            return resultat.append(question.toLowerCase(Locale.FRENCH)).append("||").toString();
        }

        Matcher m = MOTIF.matcher(question);
        StringBuilder traduction = new StringBuilder();
        int nbTraduits = 0;
        while (m.find()) {
            String mot = DICTIONNAIRE.get(m.group(1).toLowerCase(Locale.FRENCH));
            m.appendReplacement(traduction, Matcher.quoteReplacement(mot));
            nbTraduits++;
        }
        m.appendTail(traduction);

        resultat.append("Darija : ").append(traduction)
                .append("\n(").append(nbTraduits).append(" mot(s) traduit(s))");
        return resultat.toString();
    }
}

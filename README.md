# TP 0 - Introduction à Jakarta EE

**Auteur :** Moussawi Othmane
**Groupe / numéro :** CASA-IA-G1, n° 16
**Projet :** `tp0MoussawiOthmane`

Application Web Jakarta EE (JSF + CDI + PrimeFaces) qui simule un chat avec une API. Le serveur traite la question saisie et affiche la réponse, ainsi que l'historique de la conversation.

## Technologies

- Java 25, Maven (wrapper `mvnw` fourni)
- Jakarta EE (Web Profile) : Jakarta Faces (JSF) et CDI
- PrimeFaces 16.0.0 (liste déroulante du rôle de l'API)
- Serveur d'applications : Payara Server 6.2025.11

## Structure du projet

| Élément | Rôle |
| --- | --- |
| `src/main/webapp/index.xhtml` | Page JSF : question, réponse, conversation, choix du rôle |
| `src/main/webapp/resources/` | CSS et JavaScript de la page |
| `src/main/webapp/WEB-INF/web.xml` | Page d'accueil `index.xhtml` et encodage UTF-8 |
| `ma.emsi.othmanemoussawi.tp0.jsf.Bb` | Backing bean (`@Named`, `@ViewScoped`) : état de la conversation |
| `ma.emsi.othmanemoussawi.tp0.service.GenerateurReponse` | Service CDI (`@Dependent`) : génère la réponse |
| `HelloServlet`, `index.jsp` | Fichiers générés par l'IDE, conservés pour le correcteur |

## Lancer l'application

Avec IntelliJ : configuration Run avec Payara Server 6 (Local) et l'artifact `tp0MoussawiOthmane:war exploded`.

Sans IntelliJ, depuis le dossier du projet :

```
.\mvnw.cmd clean package
asadmin deploy --force target\tp0MoussawiOthmane-1.0-SNAPSHOT.war
```

L'application est alors disponible à l'adresse :
`http://localhost:8080/tp0MoussawiOthmane-1.0-SNAPSHOT/`

Après un redéploiement, recharger la page dans le navigateur avant de soumettre le formulaire (sinon `ViewExpiredException`).

## Fonctionnement

1. L'utilisateur choisit un rôle de l'API (une seule fois par chat), écrit une question et clique sur « Envoyer la question ».
2. `Bb.envoyer()` appelle `GenerateurReponse.genererReponse(question, role)`.
3. La réponse est affichée dans la zone « Réponse », et l'échange est ajouté à la conversation.
4. « Nouveau chat » retourne `"index"` : JSF change de vue, une nouvelle instance de `Bb` est créée et tout est réinitialisé.

## Exercice bonus : traducteur français vers darija

Un nouveau rôle de l'API, **« Traducteur Français-Darija »**, a été ajouté dans la liste déroulante.

- Avec ce rôle, `GenerateurReponse` traduit la question mot à mot en darija écrit en arabizi (lettres latines, avec des chiffres comme `3` ou `7`).
- Un dictionnaire d'environ 30 mots et expressions courantes est stocké dans une `Map`.
- Une expression régulière reconnaît les entrées du dictionnaire, les plus longues d'abord (par exemple « au revoir » avant « au »). La casse est ignorée et les mots inconnus restent en français.
- La réponse indique le nombre de mots traduits.
- Avec les autres rôles (Assistant, Traducteur Anglais-Français, Guide touristique), le traitement d'origine du TP est conservé : question en minuscules entourée de `||`.

Exemple (rôle « Traducteur Français-Darija ») :

```
Question : Bonjour, ça va ? Merci beaucoup et au revoir !
Réponse  : Darija : salam, labas ? shukran bzaf w bslama !
           (6 mot(s) traduit(s))
```


# Programmation mobile - Projet Android Studio

Ce projet a été réalisé dans le cadre du cours de Programmation mobile par Jeanne Bisson et Zoé Le Barillec.  
Le but de ce projet est de créer une application mobile sous AndroidStudio permettant de gérer une base de données de jeux vidéo.

## Séance 1 - Prise en main d'Android studio
Durant cette première séance, l'objectif était de réaliser la page d'accueil de notre application. Celle-ci contient :
- Une App Bar contant le titre de l'application : My Games List.
- Le corps de la page contenant une liste déroulante de jeux sous la forme de cartes (Game Cards).

Les Game Cards, elles, affichent :
- La couverture du jeu
- Le titre du jeu
- Les genres du jeu

Toutes ces fonctionnalités sont bien opérationnelles.

## Séance 2 - Navigation
Le but de cette séance était de pouvoir naviguer entre différentes pages de notre application. Les fonctionnalités attendues sont les suivantes :
- Au toucher d'une Game Card, on est dirigés vers un second écran
- Le retour à la page d'accueil fonctionne par :  
  ○​ Appui sur le bouton “back” OU swipe depuis les côtés (dépendant du mode d’action activé sur le smartphone)  
  ○​ Appui sur la flèche de l’App Bar
- Le titre de l'App Bar sur l’écran d’un jeu est le titre du jeu
- Après le retour depuis les détails d’un jeu vers la liste des jeux, le titre est à nouveau “My Games List”
- Lors d’un retour réalisé sur la liste des jeux, on quitte l’application
  Toutes ces fonctions ont bien été implémentées dans notre application. Par souci d'esthétique, nous avons choisi de ne pas garder la flèche de retour sur la Home Page.

## Séance 3 - Configuration du GameScreen
Cette séance avait pour but de gérer l'affichage du détail d'un jeu. Cette page doit contenir :
- Une App Bar qui affiche le nom du jeu
- Sous l'App Bar sont affichées les informations du jeu :  
  ○​ Le nom en gras souligné  
  ○​ L'image de couverture
  ○​ Le(s) genre(s)  
  ○​ Le(s) logo(s) des plateformes qui supportent le jeu  
  ○​ Un résumé du jeu  
  Tous ces critères sont respectés.

## Séance 4 - Recherche
Cette séance avait pour but de paramétrer la fonction de recherche de notre application.
- Notre barre de recherche est dans un premier temps cachée. Elle est révélée par un appui sur l'icone loupe, placée à droite dans l'App Bar
- L'icône de recherche dans l'App Bar (croix ou loupe) change si on est respectivement en train de faire une recherche ou non
- Si la recherche est vide, tous les jeux sont affichés
- Dès la première lettre tapée dans la recherche, la liste est filtrée sur :  
  ○​ Le nom
  ○​ Le genre
  ○​ La plate-forme
- S’il n’y a aucun jeu correspondant à la recherche, on affiche un écran “No match :(”
- Lorsque l'on touche un jeu, on va à l’écran de détails correspondant et l’App Bar sur cet écran :  
  ○​ Ne permet pas de recherche  
  ○​ Affiche la flèche de retour  
  ○​ Affiche le nom du jeu
- Lorsqu'on retourne en arrière, le filtre est toujours présent et la valeur du filtre est toujours dans la recherche​.

Ces fonctionnalités sont toutes implémentées, nous n'avons pas de bugs.

## Séance 5 - Favoris
Le but de cette séance était d'implémenter une fonction de mise en favori des jeux.
- Chaque cellule de la liste des jeux a une icône “favori”
- Cette icône change d’apparence selon le fait qu’un jeu est favori ou non (respectivement un coeur plein ou vide)
- Cette icône réagit au toucher et change l’état “favori” du jeu.
- Les Game Screens contiennent aussi une icône “favori” dans l'App Bar.
- Celui-ci a la bonne apparence suivant si le jeu est déjà “favori” ou non.
- Un toucher sur cete icône change l’état “favori” du jeu.
- L’état des jeux “favoris” est cohérent entre la liste des jeux et les détails de celui-ci (i.e. changer l’état “favori” d’un jeu dans le détail de celui-ci se reflète dans la liste de la Home Page).

Toutes ces conditions sont respectées.

## Séance 6 - TP Facultatif - Passage à une API en ligne

Après configuration de notre compte Twitch, nous avons récupérer le Client-ID et le Client-secret, puis l'access Token (avec une comande curl).
```
curl -X POST "https://id.twitch.tv/oauth2/token?
    client_id=*client_id*&
    client_secret=*client_secret*&
    grant_type=client_credentials" | jq
```
Nous arrivons aussi à récupérer la liste des jeux avec une requète.
```
curl -X POST \
    'https://api.igdb.com/v4/games' \
    -H 'Client-ID: *client_id*' \
    -H 'Authorization: Bearer *token* ' \
    -d 'fields name, url; limit 500;' | jq | bat -l json
```
Nous ne sommes pas allées plus loin dans l'intégration de l'API.
Nous avons préféré améliorer notre code et ajouter des fonctionnalités supplémentaires au vu du temps qu'il nous restait.

## Bonus et améliorations

### Amélioration de la fonction favori
Nous voulions pouvoir faire un filtre qui afficherait tous les jeux marqués comme favoris.
Nous avons donc intégré une icône coeur sur l'App Bar du Home Screen, qui affiche une fois que l'on a cliqué dessus tous les favoris.
Si la fonction recherche est activée, ainsi que le filtre favoris, la recherche se concentre donc uniquement sur ces derniers.
Si aucun jeu n'est favori, un clic sur le coeur affiche un écran “No match :(”.

### Swipe entre les jeux
Nous avons intégré le swipe entre les affichages de GameScreen.  
Lorsque l'on clique sur un jeu dans la liste affichée dans la Home Page, on arrive sur le GameScreen du jeu en question.
En swipant vers la gauche ou la droite, on arrive sur le GameScreen du jeu respectivement au dessus ou en dessous du jeu initial dans la liste.
Ainsi, on peut par de simples swipes se déplacer dans les GameScreens de la liste de jeu.  
L'App Bar est bien mise à jour à chaque changement de GameScreen, avec le bon titre et la mention de favori ou non.  
Cette fonctionnalité respecte les filtres éventuels et/ou la recherche en cours.  
Pour revenir à la liste de jeux, il suffit de cliquer sur la flèche de retour de l'application ou du téléphone.
Une fois en bout de liste (début ou fin), on ne peut plus aller plus loin (respectivement vers la gauche ou la droite).

### Thème sombre
Depuis le début du projet, nous travaillons uniquement sur la version "thème clair" de l'application.
Nous avons donc décidé de nous attaquer également à sa version en "thème sombre".
Nous nous sommes aperçues que la version actuelle avec le thème dynamique n'était pas très esthétique.
Nous l'avons donc retiré et remplacé par des thèmes clairs et sombres faits par nos soins.
Maintenant, les deux thèmes sont à notre goût et toutes les couleurs en dur sont stockées au même endroit.

### Architecture
Pour finir, nous avons décidé de faire une meilleure organisation de notre code.
Au départ, toutes les fonctions et composables étaient dans le même fichier.
Maintenant, ils sont tous dans des fichiers différents, rangés dans des packages leur correspondant.

# Projet : Créer une application mobile sous AndroidStudio permettant de gérer une base de données de jeux vidéo.

## Séance 1 (HomeScreen) :
  - Création de "game cards" qui stockent les premières informations sur les jeux
  - Création d'une liste déroulante de ces game cards, avec une game card par jeu

## Séance 2 (Navigation) :
  - Au toucher sur une game card, on va vers un second écran
  - Le retour fonctionne par :

    ○​ Appui sur le bouton “back” OU swipe depuis les côtés (dépendant du mode d’action activé sur le smartphone)

    ○​ Appui sur la flèche de l’AppBar
    
  -​ Le titre de la AppBar sur l’écran d’un jeu est le titre du jeu
  - Après le retour depuis les détails d’un jeu vers la liste des jeux, le titre est à nouveau “MyGamesList”
  - Lors d’un retour réalisé sur la liste des jeux, on quitte l’application

## Séance 3 (GameScreen) :

  Sur l'écran le second écran : 
  - La App Bar affiche le nom du jeu 
  - Sous la AppBar sont affichées toutes les informations sur le jeu :
    
    ○​ Nom
    
    ○​ Image
    
    ○​ Genres

    ○​ Logos des plateformes
    
    ○​ Résumé du jeu

## Séance 4 (Recherche) : 
  - Icône de recherche dans la AppBar (loupe ou croix, dépend de si on est en recherche ou non) 
  - Si la recherche est vide, tous les jeux sont affichés
​  - Dès la première lettre tapée dans la recherche, la liste est filtrée sur :

    ○​ Nom

    ○​ Genre

    ○​ Plateforme

  - S’il n’y a aucun jeu correspondant à la recherche, on affiche un écran “No match :(”
  - Lorsque on touche un jeu, on va à l’écran de détails correspondant et l’AppBar sur cet écran :
    
​      ○​ Ne permet pas de recherche

​      ○​ Affiche la flèche de retour

​      ○​ Affiche le nom du jeu

​  - Lorsque on retourne en arrière, le filtre est toujours présent et la valeur du filtre est toujours dans la recherche​

## Séance 5 (Favoris) :
  - Chaque cellule de la liste des jeux a une icône “favori”
  - Cette icône change d’apparence selon le fait qu’un jeu est favori ou non
​  - Cette icône réagit au toucher et change l’état “favori” du jeu
​  - Le détail d’un jeu possède une icône “favori” dans la AppBar
​  - Celui-ci a la bonne apparence suivant si le jeu est “favori” ou non
  - Un toucher sur cete icône change l’état “favori” du jeu
  - L’état des jeux “favoris” est cohérent entre la liste des jeux et les détails de celui-ci (i.e. changer l’état “favori” d’un jeu dans le détail de celui-ci se reflète dans la liste)

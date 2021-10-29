L'application affiche une liste de bandes dessinées en utilisant l'API Marvel. En cliquant sur n'importe quel élément de la liste, l'écran des détails s'ouvre
(qui peut être réduit).
La vue de liste a un EditText à l'intérieur d'un TextInputLayout de la conception matérielle qui le fait rester en haut pendant que la liste défile.
Ce EditText est utilisé pour rechercher les bandes dessinées par nom de titre.


## Le projet a été développé et testé avec Android studio.  
Il utilise des bibliothèques de génération de code comme Dagger2, Databinding, Room 

## Les fonctionnalités du projet:
- [x] Kotlin
- [x] Rxjava2
- [x] RxRelay
- [x] Dagger2 (Injection de dépendance)
- [x] Coroutines (extension)
- [x] Glide (Image library)
- [x] Navigation (Composant d'architecture Android de jetpak)
- [x] DataBinding (Composant d'architecture Android de jetpak)
- [x] ViewModel (Composant d'architecture Android de jetpak)
- [x] LiveDate (Composant d'architecture Android de jetpak)
- [x] PagedList (Composant d'architecture Android de jetpak)
- [x] Room (Base de données basée sur SQLite pour la persistance et prise en charge automatique de la source de données PagedList à l'aide de BoundaryCallback de la bibliothèque de pagination)
- [x] Retrofit2 (Pour les appels de service)
- [x] okHttp (Pour la couche réseau, l'interception des journaux http et l'interception pour ajouter apiKey aux paramètres de requête pour chaque appel de service, nous faisons)
- [x] Espresso (AndroidJUnit4ClassRunner pour les UITests)
- [x] JUnit4 (pour les unit tests)
- [x] Tests d'instrumentation pour les classes RoomDB Dao
- [x] Commutation automatique de la disposition maître-détail pour les tablettes et les appareils à grand écran



## Le projet utilise l'architecture CLEAN du célèbre oncle Bob.

## Le projet a été divisé en 9 modules, répertoriés ci-dessous de haut en bas en fonction du flux de d'application:

1. app  -  Module d'application Android qui a une SplashActivity et les tests UI (expresso) et le nécessaire,
   C'est le module qui a la classe d'application et fournit l'injection de dépendance pour tous les autres modules utilisant Dagger2.
   Le module de test Android referencie les  "Dagger-Modules": remote, local et domain et remplace le service de mise à niveau pour fournir les données de
    "FakeComicsService". Il implémente également un NetworkStateIdlingResource pour déclencher le test uniquement après que l'état du réseau est CHARGÉ et que le
   L'interface utilisateur est dans un état testable inactif.

2. feature-home - Android Library module. Ceci est le module de fonctionnalité et contient toute la logique de l'interface utilisateur + les mises en page pour les deux écrans (list et detail).

   Il  écoute les différents états du réseau que le module données émet et y réagit en mettant à jour l'interface utilisateur de manière appropriée.
   Il écoute la LiveData<PagedList> construite à partir de la DataSourceFactory émise par la couche Room DB locale. La liste paginée est ensuite utilisée pour mettre à jour l'adaptateur supportant la
   vue recycler. Il existe deux vues et deux liaisons sont générées à l'aide de la bibliothèque de liaison de données pour lier les dispositions d'interface utilisateur avec les fragments. De plus, parce qu'une vue recycler
   est utilisé, il y a un ItemViewBinding généré pour les vues d'élément dans la vue recycleur. Vous pouvez trouver ces classes dans les packages nommés de manière appropriée dans ce module.
   En fait, la DataSourcefactory est générée par la requête sql Room basée sur le texte de recherche saisi à partir de la couche UI et passe par plusieurs couches avant d'atteindre
   le modèle de vue où il est converti en LiveData<PagedList<ComicsEntity>.
   Chaque couche a sa propre classe de modèle pour réduire le couplage fort entre les couches et prendre en charge des transformations supplémentaires dans chaque couche.
   Exemple, dans le module 'localdata' appelle le modèle en tant que ComicsLocal qui a des champs annotés pour fournir des méta-informations pour le RoomDB,
   alors que la couche "domain" ou "fonctionnalité" n'a pas besoin de ces annotations, elle utilise donc son propre format.
   La couche réseau a beaucoup d'autres modèles pour mapper la réponse reçue de l'appel api et se transforme en une version simplifiée
   juste approprié pour la couche suivante ci-dessus donnée à consommer.
   Si vous pouvez le remarquer attentivement, il existe deux liaisons différentes bigImageUrl et imageUrl utilisées pour les vues d'image dans l'écran de détails et l'écran de vue de liste respectivement.
   Cela nous permet de télécharger et de conserver deux tailles d'image différentes pour les deux écrans afin que la vue en liste utilise une taille d'icône plus petite pour les images tandis que le plein écran
   l'écran de détails utilise une image plus grande.
   La vue détaillée est conçue à l'aide de CoordinatorLayout, NestedScrollview (body) et CollapsableToolBarLayout (header), afin qu'elle puisse être défilée vers le haut pour s'estomper et se réduire.
   Le module 'feature-home' utilise également le composant de navigation pour la navigation des écrans.
   Il utilise la liaison de données pour lier les données qu'il a obtenues du modèle de vue à la vue réelle, réduisant ainsi une grande partie du code.
   Il gère également les dispositions spécifiques à la tablette et au téléphone.

3. presentation - Android library module.
   Ce module a le composant ViewModel  et fournit la couche d'interface utilisateur (module 'feature-home')
   avec la nouvelle PagedList pour les éléments via LiveData à lier à l'interface utilisateur. Il utilise des coroutines pour effectuer les opérations dans bgscope. Il maintient également l'état pour la
   disposition de la barre de progression que la couche d'interface utilisateur utilise pour lier à l'aide du composant de liaison de données. Lorsque l'action de recherche est déclenchée par l'interface utilisateur, le modèle de vue utilise le cas d'utilisation de la couche domaine pour récupérer la nouvelle DataSourceFactory pour la recherche
   chaîne de caractères. Avec la source de données, il récupère également l'implémentation BoundaryCallback du module 'data' dans le même appel au cas d'utilisation 'domain'. La source de données ensemble
   avec le rappel de limite ensemble est ensuite utilisé pour créer une LivedData pour une liste paginée qui sera observée par la couche 'feature-home'.

4. domain - Kotlin library module.
   C'est le cœur de l'application contenant les cas d'utilisation de l'application. . Il n'utilise aucune API spécifique à Android, à l'exception de
   DataSourceFactory utilisé comme type de retour de la bibliothèque de pagination. Il utilise uniquement RxJava, il est donc éloigné de toute dépendance de plate-forme et suit donc le principe de l'architecture CLEAN.
   Il dispose de tests Junit pour tous les cas d'utilisation fournis dans cette couche.

5. data - Kotlin library module.
   Il s'agit d'une abstraction suivant le modèle de référentiel. Il masque la source réelle des données. Il encapsule les sources de données locales et distantes des couches ci-dessus. Il définit le contrat pour les référentiels locaux et distants.
   Il n'utilise aucune API Android. C'est un module kotlin pur. Cette couche fournit l'implémentation de la classe BoundaryCallback requise pour déclencher la récupération des nouvelles données à partir du
   PagedListAdapter. Mais il n'a aucune référence aux API spécifiques à Android et fait son travail en utilisant les abstractions de source de données locales et distantes définies comme interfaces pour la couche respective à
   respecter et mettre en œuvre. Il dispose de tests unitaires couvrant toutes les méthodes exposées par cette couche.

6. localdata - Android library module.
   Cette couche implémente le contrat d'interface LocalRepository défini dans la couche du module 'data'. Il utilise l'API RoomDB fournie par Android pour la persistance.
   Il fournit les opérations CRUD via les définitions Dao et Entity que le compilateur Room comprend. Il dispose des tests d'instrumentation utilisant la base de données en mémoire et les tests unitaires requis pour le
   API exposée dans cette couche. Il prend en charge l'application pour fonctionner hors ligne.

7. remotedata - Kotlin module.
   Cette couche implémente l'interface RemoteRepository de la couche module 'data'. Il utilise Retrofit api et okHttp comme client pour les appels d'API.
   Le point final de la bande dessinée est utilisé pour servir la liste des bandes dessinées. Les clés API (publiques et privées) sont fournies dans le fichier gradle.properties et sont disponibles en tant que BuildConfig
   défini dans le fichier gradle du module 'app'. Parce qu'il s'agit d'un module kotlin pur non Android, les propriétés requises pour la modernisation sont fournies via Dagger à partir du module 'app'
   via l'injection de dépendances. Ce module a deux versions pour récupérer la liste car l'API Marvel ne permet pas de rechercher avec une clé vide. Si une clé de recherche non vide est disponible, elle
   utilise le paramètre de requête "titleStartsWith" du point de terminaison de la bande dessinée pour récupérer les résultats, sinon il effectue un appel sans le paramètre de requête "titleStartsWith".
   Il a des tests couvrant toutes les API que ce module expose.

8. utils - Kotlin library module.
   Il s'agit d'un petit module pour les classes d'aide. Il fournit le singleton wrapper BehaviorRelay pour suivre l'état du réseau et les erreurs dans le réseau à travers les modules
   (Android et non Android).

   Le projet a été structuré dans un souci d'évolutivité. La structure peut être agrandie. Le module feature-home a ses propres mises en page dans le dossier res et cette abstraction est bonne pour
   la séparation des préoccupations. Nous pouvons même utiliser la fonctionnalité de dynamic-feature-modules pour fournir le code à la demande et pour réduire la taille initiale de l'apk.
   Nous pouvons avoir plusieurs de ces modules de présentation, de fonctionnalité et de domaine pour séparer les préoccupations et même être déplacés vers des dépôts séparés et consommés en tant que fichier de bibliothèque par d'autres équipes,
   de sorte que le temps de construction peut être considérablement réduit. Il est même simple de tester des fonctionnalités AB et ensuite permettre à un module d'être disponible pour le public.
   
9. android-utils - Android library module.
   Il fournit la surveillance de l'état de la connectivité Livedata qui surveille l'évolution de la disponibilité du réseau. Ces données en direct sont utilisées dans le
   module 'feature-home' pour détecter le changement d'état du réseau et agir en conséquence. Le ConnectivityMonitorLiveData est un singleton injecté via Dagger à l'activité
   dans les modules 'feature-home'.


Le NetworkStateRelay dans la couche domaine est une abstraction au niveau du domaine des états du réseau que l'application doit gérer.
Il est implémenté à l'aide du RxRelay.
Il est injecté via Dagger. Le ComicsListFeatureFragment dans le module 'feature' utilise l'état pour modifier l'interface utilisateur.
Le relais est essentiellement poussé à partir de 2 endroit:
1. ComicsListFeatureActivity - définit l'état initial sur VIDE, puis pousse l'état CONNECTÉ/DÉCONNECTÉ en fonction de la disponibilité de la connexion.
2. ComicsListBoundaryCallback - définit l'état LOADING/LOADED/ERROR en fonction de l'état de l'appel de l'API de service.

## Voici les modules Android :
1. app  
2. feature-home 
3. presentation
4. android-utils
5. localdata 

## Voici les modules de la bibliothèque kotlin:
1. domain 
2. data 
3. remotedata
4. utils 

J'ai utilisé le RXJava et le RxRelay pour communiquer entre les modules Android et non Android.

Les versions de toutes les bibliothèques externes utilisées sont conservées dans le fichier versions.gradle à la racine du projet.
Ainsi, nous pouvons facilement manipuler les différentes versions de la bibliothèque, ainsi que les versions minSdk, targetSdk et compileSdk.


## Le flux de code de l'application:
L'application démarre avec une activité de démarrage dans le module application et, après un délai, lance ComicsListFeatureActivity dans le module feature.
ComicsListFeatureActivity définit l'état du réseau au niveau du domaine sur EMPTY via le NetworkStateRelay qui a été injecté via Dagger à partir du module 'domain'.
L'activité définit ensuite les écouteurs sur les changements de connectivité pour communiquer avec les autres systèmes via le NetworkStateRelay.
Il utilise ensuite la valeur booléenne res isTablet pour choisir entre la mise en page paysage (style maître-détail) ou le portrait (un seul fragment à la fois).
Il utilise le modèle de vue de la couche « présentation » pour voir s'il existe une bande dessinée sélectionnée dans la liste pour remplir le fragment de détails dans la mise en page maître-détail.
si l'appareil est une tablette.
Il utilise le navHostFragment du composant de navigation pour traiter ces transactions de fragments.

Le ComicsListFeatureFragment écoute les changements d'état du réseau et lorsqu'il reçoit le changement d'état EMPTY déclenché par ComicsListFeatureActivity,
il charge la recherche avec l'ensemble de requêtes actuel. Si aucune requête n'est définie, il utilise la requête par défaut qui est "Avengers".

La requête est envoyée à viewmodel dans le module 'présentation', le viewmodel utilise le cas d'utilisation GetComicsListAction du module 'domain'.
Le GetComicsListAction utilise le ComicsRepository défini dans le module 'data' pour obtenir le DatasourceFactory et le BoundaryCallback nécessaires à la génération
les LiveData de Pagelist de ComicsEntity à afficher dans la vue de liste. Le BoundaryCallback est défini dans le module 'data' lui-même,
où en tant que DatasourceFactory de la base de données de la salle définie dans le module 'localdata'. Chaque requête génère une nouvelle DatasourceFactory.

Une fois que le LiveData<PageList<ComicsEntity> est reçu via le comicListSource dans le viewmodel, le ComicsListFeatureFragment essaie de remplir 
L'adaptateur de Recyclerview pour rendre la vue. Maintenant, il y a deux cas, soit il n'y a pas de données immédiatement, soit la fin des données est atteinte.
Le BoundaryCallback gère ces 2 cas via le onZeroitemLoaded et le onItemAtEndLoaded. Les deux cas déclenchent une action d'appel d'API qui est effectuée via
ComicsService défini dans la couche 'remotedata'. Le BoundaryCallback se trouve dans le module 'data' qui est une couche d'abstraction du référentiel.
Il définit le NetworkStateRelay sur l'état LOADING afin que ComicsListFeatureFragment dans le module 'feature' puisse afficher la barre de progression.
Le ComicsListFeatureFragment gère cela en définissant l'isLoading Observable dans le modèle de vue qui est lié à la vue de la barre de progression dans la mise en page
via la liaison de données.
Une fois l'appel de service terminé, le contrôle revient au module 'data' qui met à jour le résultat dans la base de données de la salle dans le module 'localdb'.
Et puis l'état du réseau est défini sur l'état CHARGÉ afin que la couche d'interface utilisateur (module « fonctionnalité ») puisse arrêter la barre de progression. Parallèlement, la mise à jour du résultat dans le
room db déclenche un événement dans la Datasource écouté par le viewmodel via les livedata et communiqué à l'observateur dans le ComicsListFeatureFragment
avec la nouvelle liste paginée. L'interface utilisateur met à jour l'adaptateur de vue du recycleur et la liste s'affiche.
Lorsque l'utilisateur clique sur un élément quelconque, l'écouteur onClick du spectateur approprié est déclenché. La liaison de données appelle la méthode onCicked définie dans le
Classe ComicsDataBindingModel qui est la liaison responsable du chargement des données et des événements de gestion pour le viewholder particulier.
L'écouteur onClick met d'abord à jour les données en direct actuelles de Comics dans le modèle de vue.
Le onClick utilise le composant de navigation pour effectuer la navigation dans la mise en page à fragment unique. S'il s'agit d'une tablette, aucune navigation n'est effectuée.
Le ComicsDetailFragment observe les modifications apportées aux données en direct actuelles de Comics à partir du modèle de vue. Il met donc à jour sa vue à l'aide de la liaison de données.

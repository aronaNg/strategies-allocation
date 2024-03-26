import java.util.Random;

public class PageFit {
    private static final int TAILLE_MEMOIRE = 2 * 1024 * 1024; // 2 Mo
    private static final int TAILLE_PAGE = 4 * 1024; // 4 Ko
    private static final int TOTALES_PAGES = TAILLE_MEMOIRE / TAILLE_PAGE;
    private static final int OS_PAGES = (int) (TOTALES_PAGES * 0.45);
    private static final int NB_REQUETE = 100;
    private static final int MIN_REQUETE = 20 * 1024; // 20 Ko
    private static final int MAX_REQUETE = 50 * 1024; // 50 Ko

    public static void main(String[] args) {
        // Initialiser l'espace mémoire
        int[] memoire = new int[TOTALES_PAGES];

        // Réservation des pages pour le SE
        Random random = new Random();
        for (int i = 0; i < OS_PAGES; i++) {
            int pageIndex;
            do {
                pageIndex = random.nextInt(TOTALES_PAGES);
            } while (memoire[pageIndex] != 0);
            memoire[pageIndex] = 1;
        }

        // Générer les requêtes d'allocation
        int[] requetes = new int[NB_REQUETE];
        for (int i = 0; i < NB_REQUETE; i++) {
            int tailleReq = MIN_REQUETE + random.nextInt(MAX_REQUETE - MIN_REQUETE + 1);
            requetes[i] = tailleReq / TAILLE_PAGE;
        }
        // https://www.geeksforgeeks.org/clone-method-in-java-2/
        
        executeFirstFit(memoire.clone(), requetes);
        executeNextFit(memoire.clone(), requetes);
        executeBestFit(memoire.clone(), requetes);
        executeWorstFit(memoire.clone(), requetes);
        }

        private static void executeFirstFit(int[] memoire, int[] requetes) {
            int reqServies = 0;
            int pageFragmentees = 0;
        
            for (int requete : requetes) {
                for (int i = 0; i < memoire.length; i++) {
                    // Chercher un bloc de mémoire libre suffisamment grand
                    int j = i;
                    while (j < memoire.length && memoire[j] == 0 && j - i < requete) {
                        j++;
                    }
        
                    // Si un bloc de mémoire libre suffisamment grand a été trouvé
                    if (j - i == requete) {
                        // Allouer le bloc de mémoire
                        for (int k = i; k < j; k++) {
                            memoire[k] = 1;
                        }
        
                        reqServies++;
                        break;
                    }
                }
            }
        
            // Calculer le nombre de pages fragmentées
            for (int i = 0; i < memoire.length; i++) {
                if (memoire[i] == 0) {
                    pageFragmentees++;
                }
            }
        
            System.out.println("First Fit:");
            System.out.println("Nombre de requêtes servies: " + reqServies);
            System.out.println("Nombre total de pages fragmentées: " + pageFragmentees);
        }

        private static void executeNextFit(int[] memoire, int[] requetes) {
            int reqServies = 0;
            int pageFragmentees = 0;
            int dernierAlloc = 0;
        
            for (int requete : requetes) {
                int i = dernierAlloc;
                do {
                    // Chercher un bloc de mémoire libre suffisamment grand
                    int j = i;
                    while (j < memoire.length && memoire[j] == 0 && j - i < requete) {
                        j++;
                    }
        
                    // Si un bloc de mémoire libre suffisamment grand a été trouvé
                    if (j - i == requete) {
                        // Allouer le bloc de mémoire
                        for (int k = i; k < j; k++) {
                            memoire[k] = 1;
                        }
        
                        reqServies++;
                        dernierAlloc = j;
                        break;
                    }
        
                    i = (i + 1) % memoire.length;
                } while (i != dernierAlloc);
            }
        
            // Calculer le nombre de pages fragmentées
            for (int i = 0; i < memoire.length; i++) {
                if (memoire[i] == 0) {
                    pageFragmentees++;
                }
            }
        
            System.out.println("Next Fit:");
            System.out.println("Nombre de requêtes servies: " + reqServies);
            System.out.println("Nombre total de pages fragmentées: " + pageFragmentees);
        }

        private static void executeBestFit(int[] memoire, int[] requetes) {
            int reqServies = 0;
            int pageFragmentees = 0;
        
            for (int requete : requetes) {
                int bestIndex = -1;
                int bestSize = Integer.MAX_VALUE;
        
                for (int i = 0; i < memoire.length; i++) {
                    // Chercher un bloc de mémoire libre suffisamment grand
                    int j = i;
                    while (j < memoire.length && memoire[j] == 0 && j - i < requete) {
                        j++;
                    }
        
                    // Si un bloc de mémoire libre suffisamment grand a été trouvé
                    if (j - i == requete && j - i < bestSize) {
                        bestIndex = i;
                        bestSize = j - i;
                    }
                }
        
                // Si un bloc de mémoire a été trouvé
                if (bestIndex != -1) {
                    // Allouer le bloc de mémoire
                    for (int k = bestIndex; k < bestIndex + bestSize; k++) {
                        memoire[k] = 1;
                    }
        
                    reqServies++;
                }
            }
        
            // Calculer le nombre de pages fragmentées
            for (int i = 0; i < memoire.length; i++) {
                if (memoire[i] == 0) {
                    pageFragmentees++;
                }
            }
        
            System.out.println("Best Fit:");
            System.out.println("Nombre de requêtes servies: " + reqServies);
            System.out.println("Nombre total de pages fragmentées: " + pageFragmentees);
        }

        private static void executeWorstFit(int[] memoire, int[] requetes) {
            int reqServies = 0;
            int pageFragmentees = 0;
        
            for (int requete : requetes) {
                int worstIndex = -1;
                int worstSize = -1;
        
                for (int i = 0; i < memoire.length; i++) {
                    // Chercher un bloc de mémoire libre suffisamment grand
                    int j = i;
                    while (j < memoire.length && memoire[j] == 0) {
                        j++;
                    }
        
                    // Si un bloc de mémoire libre plus grand a été trouvé
                    if (j - i >= requete && j - i > worstSize) {
                        worstIndex = i;
                        worstSize = j - i;
                    }
                }
        
                // Si un bloc de mémoire a été trouvé
                if (worstIndex != -1) {
                    // Allouer le bloc de mémoire
                    for (int k = worstIndex; k < worstIndex + requete; k++) {
                        memoire[k] = 1;
                    }
        
                    reqServies++;
                }
            }
        
            // Calculer le nombre de pages fragmentées
            for (int i = 0; i < memoire.length; i++) {
                if (memoire[i] == 0) {
                    pageFragmentees++;
                }
            }
        
            System.out.println("Worst Fit:");
            System.out.println("Nombre de requêtes servies: " + reqServies);
            System.out.println("Nombre total de pages fragmentées: " + pageFragmentees);
        }

}
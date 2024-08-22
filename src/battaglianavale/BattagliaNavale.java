/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battaglianavale;

/**
 *
 * @author IlDivinoPase
 */
import java.util.Scanner;
import todoubledemo.toInt;
import java.io.IOException;

//O se la cella non é stata colpita, X se la cella e stata colpita ma la nave non c'é, | se la nave é stata colpita ma non interamente e carattereRappresentativo se la nave é stata affondata
public class BattagliaNavale 
{
/*PER ACCEDERE ALLA VARIABLE DI ISTANZA infoNave[] SI POTREBBE CREARE UN ARRAY BIDIMENSIONALE UGUALE A TAVOLA, E OGNI BLOCCO DI NAVE É UNA VARIABILE A SE STANTE (OVVIAMENTE) CHE PERÓ SI COLLEGA ALLO STESSO OGGETTO DI TIPO NAVE, PER ACCEDERE AL CAMPO RIFERITO PRECISAMENTE AL BLOCCO COLPITO RICORDIAMO DI AVERE A DISPOSIZIONE LA X E LA Y INSERITA DALL'UTENTE*/
/*PER INSERIRE LE NAVI CAMBIANDO ORIENTAMENTO SI PUÓ DIVIDERE OGNI DIMENSIONE[][] DI OGNI NAVE E TRASFORMARLO IN ARRAY UNIDIMENSIONALI, E POI METTERE GLI ARRAY UNIDIMENSIONALI (CHE RAPPRESENTANO CIASCUNO OGNI RIGA)come colonne*/

    private static final char tavolaGiocatore1[][] = new char[10][10];//UTILIZZO: tavolaGiocatore[][] é la tavola che verrá stampata e su cui il giocatore opererá per inserire le navi
    private static final Nave tavolaNaviGiocatore1[][] = new Nave[10][10];//UTILIZZO: tavolaNaviGiocatore[][] é la tavola che servirá per collegarsi agli oggetti Nave, utilizando x e y inseriti dal giocatore
    private static final char tavolaDaMostrareGiocatore1[][] = new char[10][10];//UTILIZZO: é la tavola che si mostra mentre si gioca
    private static Output.statusPulsante[][] tavolaDaMostrareG1 = new Output.statusPulsante[10][10];
    
    private static final char tavolaGiocatore2[][] = new char[10][10];
    private static final Nave tavolaNaviGiocatore2[][] = new Nave[10][10];
    private static final char tavolaDaMostrareGiocatore2[][] = new char[10][10];
    
    private static final int quanteNavi = 6;
    private static final PacchettoNavi pacchettoNavi = new PacchettoNavi(quanteNavi);
    private static final PacchettoNavi pacchettoNaviG1 = new PacchettoNavi(quanteNavi);
    private static final PacchettoNavi pacchettoNaviG2 = new PacchettoNavi(quanteNavi);
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Output frame = new Output("Modern");
        frame.setVisible(true);
    }
    
    public static void inizializzaOgniStatus(Output.statusPulsante[][] status)
    {
        for(int i = 0; i < status.length; i++)
        {
            for(int j = 0; j < status[i].length; j++)
            {
                status[i][j] = Output.statusPulsante.NON_COLPITO;
            }
        }
    }
    
    private static int turno = 0;//variabile che conta a che turno siamo arrivati, e serve per far capire al computer dove colpire
    
    public static int[] mossaComputer(int modalita, int colpoPrecedente)//chiede in ingresso il modo in cui si muove il computer, restituisce 0 se acqua, 1 se colpito, 2 se vinto (non funziona, edit del 04/08/2019)
    {
        int[] coordinate = new int[2];//coordinate[0] -> y, coordinate[1] -> x

        switch(modalita)
        {
            case 0:
                if((turno < 4) && (colpoPrecedente == 0))
                {
                    coordinate = coordinateDaColpireComputerRandom();
                }
                else
                    coordinate = coordinateDaColpireComputerNave();//vengono restituite Y e poi X
                break;
            case 1://strat. 1
                break;
            case 2://strat. 2
                break;
            default:
                System.out.println("ERRORE IN strategiaComputer");
                System.exit(0);
        }
        //alla fine
        turno++;

        return coordinate;
    }
    
    /*public static int[] coordinateDaColpireComputerRandom(statusPulsante[][] tavolaDaMostrareGx, int variabile)//Da correggere. Restituisce un array di coordinate, funzione stupida ma funziona lol (variabile serve per decidere la differenza del quadrato nuovo da quello precedente)
    {
        int[] coordinate = new int[2];
        if((tavolaDaMostrareGx.length == 2) && (tavolaDaMostrareGx[0].length == 2))//caso base
        {
            do//continuare a generare due coordinate finché non si trova un blocco non ancora colpito
            {
               coordinate[0] = (int) (Math.random() * 2);
               coordinate[1] = (int) (Math.random() * 2);
            }while(tavolaDaMostrareGx[coordinate[0]][coordinate[1]] != statusPulsante.NON_COLPITO);
        }
        else if((tavolaDaMostrareGx.length == 10) && (tavolaDaMostrareGx[0].length == 10))//divide il quadratone da 10 in 4 quadrtati da 5
        {
            int[] quadrati = {0, 0, 0, 0};
            statusPulsante[][] nuovaTavolaDaMostrare = new statusPulsante[5][5];
            for(int i = 0; i < 10; i++)//calcola quanti spazi vuoti ha ogni quadrato
            {
                for(int j = 0; j < 10; j++)
                {
                    if((i < 5) && (j < 5) && (tavolaDaMostrareGx[i][j] == statusPulsante.NON_COLPITO))
                        quadrati[0]++;
                    else if((i < 5) && (j >= 5) && (tavolaDaMostrareGx[i][j] == statusPulsante.NON_COLPITO))
                        quadrati[1]++;
                    else if((i >= 5) && (j < 5) && (tavolaDaMostrareGx[i][j] == statusPulsante.NON_COLPITO))
                        quadrati[2]++;
                    else if((i >= 5) && (j >= 5) && (tavolaDaMostrareGx[i][j] == statusPulsante.NON_COLPITO))
                        quadrati[3]++;
                }
            }
            
            //calcola quale é il quadrato con il maggior numero di 'O' //sbagliato, il quadrato 4 ha piu possibilitá di essere scelto
            int currentMax = 0;
            
            int[] probabilitaQuadrati = {(int) (Math.random() *100000), (int) (Math.random() *100000), (int) (Math.random() *100000), (int) (Math.random() *100000)};//per risolvere il problema della probabilitá, non lo risolve in modo assoluto, ma la probabilitá che probabilitaQuadrati abbia generato anche solo 2 numeri uguali é trascurabile
            for(int i = 1; i < 4; i++)
            {
                 if(quadrati[currentMax] < quadrati[i])
                     currentMax = i;
                 else if(quadrati[currentMax] == quadrati[i])
                 {
                     if(probabilitaQuadrati[currentMax] > probabilitaQuadrati[i])
                         ;
                     else if(probabilitaQuadrati[currentMax] == probabilitaQuadrati[i])
                     {
                         if((int) (Math.random()*2) == 0)
                             currentMax = i;
                         else
                             ;
                     }
                     else
                         currentMax = i;
                 }
            }

            //copia il quadrato scelto dentro nuovaTavolaDaMostrare[][]
            int contatoreY = 0, contatoreX = 0;
            for(int i = 0; i < 10; i++)//calcola quanti spazi vuoti ha ogni quadrato
            {
                for(int j = 0; j < 10; j++)
                {
                    if((i < 5) && (j < 5) && (currentMax == 0))//se currentMax == 0 allora il quadrato scelto é il quadrato1 e cosí via
                    {
                        nuovaTavolaDaMostrare[contatoreY][contatoreX] = tavolaDaMostrareGx[i][j];
                        contatoreX++;
                    }
                    else if((i < 5) && (j >= 5) && (currentMax == 1))
                    {
                        nuovaTavolaDaMostrare[contatoreY][contatoreX] = tavolaDaMostrareGx[i][j];
                        contatoreX++;
                    }
                    else if((i >= 5) && (j < 5) && (currentMax == 2))
                    {
                        nuovaTavolaDaMostrare[contatoreY][contatoreX] = tavolaDaMostrareGx[i][j];
                        contatoreX++;
                    }
                    else if((i >= 5) && (j >= 5) && (currentMax == 3))
                    {
                        nuovaTavolaDaMostrare[contatoreY][contatoreX] = tavolaDaMostrareGx[i][j];
                        contatoreX++;
                    }
                }
                contatoreY++;
                contatoreX = 0;
                if(contatoreY == 5)
                    contatoreY = 0;
            }
            
            coordinate = coordinateDaColpireComputerRandom(tavolaDaMostrareGx, 2);//parte ricorsiva
            
            //aggiusta le coordinate, perché quelle che returna coordinateDaColpireComputerRandom() sono riferite al quadrato scelto
            switch(currentMax)
            {
                case 0://il quadrato scelto e il primo in alto a sinistra, quindi le coordinate attuali sono corrette
                    break;
                case 1://essendo che ci si riferisce al quadrato in alto a destra bisogna aumentare la x di 5
                    coordinate[1] += 5;
                    break;
                case 2://ci si riferisce al qiadrato in basso a sinistra, quindi si aumentano le y di 5
                    coordinate[0] += 5;
                    break;
                case 3://ci si riferisce al quadrato in basso a destra quindi bisogna aumentare sa la y sia la x di 5
                    coordinate[0] += 5;
                    coordinate[1] += 5;
                    break;
                default:
                    System.out.println("FATAL ERROR");
                    System.exit(0);
            }
        }
        else
        {
            //molto simile a quello dei quattro quadrati da 5
            int lunghezzaQuadrato = tavolaDaMostrareGx.length - variabile;//un quadrato da 10 (MAX) avrá 4 quadrati da 9 all'interno, un quadrato da 9 avrá 4 quadrati da 8 e cosí via
            statusPulsante[][] nuovaTavolaDaMostrare = new statusPulsante[lunghezzaQuadrato][lunghezzaQuadrato];
            int[] quadrati = {0, 0, 0, 0};
            
            //conta quanti spazi non colpiti nel quadrato 5x5 scelto ci sono, a differenza del ciclo dove si sceglie il 5x5 questo non mette gli else if, ma solo degli if, questo perché i quadrati si sovrappongono
            for(int i = 0; i < tavolaDaMostrareGx.length; i++)
            {
                for(int j = 0; j < tavolaDaMostrareGx[i].length; j++)
                {
                    if((i < tavolaDaMostrareGx.length - variabile) && (j < tavolaDaMostrareGx[i].length - variabile) && (tavolaDaMostrareGx[i][j] == statusPulsante.NON_COLPITO))
                        quadrati[0]++;
                    if((i < tavolaDaMostrareGx.length - variabile) && (j > variabile - 1) && (tavolaDaMostrareGx[i][j] == statusPulsante.NON_COLPITO))
                        quadrati[1]++;
                    if((i > variabile - 1) && (j < tavolaDaMostrareGx[i].length - variabile) && (tavolaDaMostrareGx[i][j] == statusPulsante.NON_COLPITO))
                        quadrati[2]++;
                    if((i > variabile - 1) && (j > variabile - 1) && (tavolaDaMostrareGx[i][j] == statusPulsante.NON_COLPITO))
                        quadrati[3]++;
                }
            }
            
            //quadrato con piú 'O'
            int currentMax = 0;
            int[] probabilitaQuadrati = {(int) (Math.random() *100000), (int) (Math.random() *100000), (int) (Math.random() *100000), (int) (Math.random() *100000)};//per risolvere il problema della probabilitá, non lo risolve in modo assoluto, ma la probabilitá che probabilitaQuadrati abbia generato anche solo 2 numeri uguali é trascurabile
            for(int i = 1; i < 4; i++)
            {
                 if(quadrati[currentMax] < quadrati[i])
                     currentMax = i;
                 else if(quadrati[currentMax] == quadrati[i])
                 {
                     if(probabilitaQuadrati[currentMax] > probabilitaQuadrati[i])
                         ;
                     else if(probabilitaQuadrati[currentMax] == probabilitaQuadrati[i])
                     {
                         if((int) (Math.random()*2) == 0)
                             currentMax = i;
                         else
                             ;
                     }
                     else
                         currentMax = i;
                 }
            }
            
            //copia il quadrato scelto dentro nuovaTavolaDaMostrare[][]
            int contatoreX = 0, contatoreY = 0;
            for(int i = 0; i < tavolaDaMostrareGx.length; i++)
            {
                for(int j = 0; j < tavolaDaMostrareGx[i].length; j++)
                {
                    if((i < tavolaDaMostrareGx.length - variabile) && (j < tavolaDaMostrareGx[i].length - variabile) && (currentMax == 0))
                    {
                        nuovaTavolaDaMostrare[contatoreY][contatoreX] = tavolaDaMostrareGx[i][j];
                        contatoreX++;
                    }
                    if((i < tavolaDaMostrareGx.length - variabile) && (j > variabile - 1) && (currentMax == 1))
                    {
                        nuovaTavolaDaMostrare[contatoreY][contatoreX] = tavolaDaMostrareGx[i][j];
                        contatoreX++;
                    }
                    if((i > variabile - 1) && (j < tavolaDaMostrareGx[i].length - variabile) && (currentMax == 2))
                    {
                        nuovaTavolaDaMostrare[contatoreY][contatoreX] = tavolaDaMostrareGx[i][j];
                        contatoreX++;
                    }
                    if((i > variabile - 1) && (j > variabile - 1) && (currentMax == 3))
                    {
                        nuovaTavolaDaMostrare[contatoreY][contatoreX] = tavolaDaMostrareGx[i][j];
                        contatoreX++;
                    }
                }
                contatoreY++;
                contatoreX = 0;
                if(contatoreY == nuovaTavolaDaMostrare.length)
                    contatoreY = 0;
            }
            
            coordinate = coordinateDaColpireComputerRandom(nuovaTavolaDaMostrare, 1);//parte ricorsiva
            
            //aggiusta le coordinate, perché quelle che returna coordinateDaColpireComputerRandom() sono riferite al quadrato scelto
            switch(currentMax)
            {
                case 0://il quadrato scelto e il primo in alto a sinistra, quindi le coordinate attuali sono corrette
                    break;
                case 1://essendo che ci si riferisce al quadrato in alto a destra bisogna aumentare la x
                    coordinate[1] += 1;
                    break;
                case 2://ci si riferisce al qiadrato in basso a sinistra, quindi si aumentano le y
                    coordinate[0] += 1;
                    break;
                case 3://ci si riferisce al quadrato in basso a destra quindi bisogna aumentare sia la y sia la x 
                    coordinate[0] += 1;
                    coordinate[1] += 1;
                    break;
                default:
                    System.out.println("FATAL ERROR");
                    System.exit(0);
            }
        }
        return coordinate;
    }*/
    
    private static boolean[] quadranteColpito = new boolean[4];
    public static int[] coordinateDaColpireComputerRandom()
    {
        int[] coordinate = new int[2];
        int quadrante = -1;
        
        do
        {
            coordinate = coordinateRandom();

            if((coordinate[0] < 5) && (coordinate[1] < 5))
            {
                quadrante = 0;
            }
            else if((coordinate[0] < 5) && (coordinate[1] >= 5))
            {
                quadrante = 1;
            }
            else if((coordinate[0] >= 5) && (coordinate[1] < 5))
            {
                quadrante = 2;
            }
            else if((coordinate[0] >= 5) && (coordinate[1] >= 5))
            {
                quadrante = 3;
            }
        }while(quadranteColpito[quadrante] == true);
        
        quadranteColpito[quadrante] = true;
        return coordinate;
    }
    
    public static int[] coordinateRandom()
    {
        int[] coordinate = new int[2];
        coordinate[0] = (int) (Math.random() * 10);//y
        coordinate[1] = (int) (Math.random() * 10);//x
        return coordinate;
    }
    
    public static int[] coordinateDaColpireComputerNave()//inserire che se ci fossero piú punti con gli stessi valori si sceglie randomicamente chi colpire
    {
        //prima di tutto capire che dimensione hanno i blocchi colpiti
        //boolean[][] dimensioneBlocchiColpiti = new boolean[10][10];
        int[] naviPossibili = new int[quanteNavi];//array che contiene per ogni casella un numero, l'indice di ogni casella rappresenta la nave nel pacchetto di navi utilizzato, se per esempio naviPossibili[0] == 1 allora vuol dire che la nave pacchettoNavi.navi[i].dimensione puo contenere il dimensioneBlochiColpiti
        int[][] tavolaValori = new int[10][10];//contiene la possibilitá per ogni cella di essere quella contenente la nave
        inizializzaTavolaValori(tavolaValori);
        int[][] matriceCoordinate = new int[1][2];
        
        calcoloNaviPossibili(naviPossibili, tavolaValori);
        
        int xMaggiore = 0, yMaggiore = 0;
        
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                if((tavolaValori[i][j] > tavolaValori[yMaggiore][xMaggiore]) && (tavolaDaMostrareG1[i][j] == Output.statusPulsante.NON_COLPITO))
                {
                    yMaggiore = i;
                    xMaggiore = j;
                    matriceCoordinate = azzeraMatriceDinamica(j, i);
                }
                else if((tavolaValori[i][j] == tavolaValori[yMaggiore][xMaggiore]) && (tavolaDaMostrareG1[i][j] == Output.statusPulsante.NON_COLPITO))
                {
                    matriceCoordinate = aggiungiElementoMatrice(j, i, matriceCoordinate);
                }
            }
        }
        //visualizzaTavolaValori(tavolaValori);
        return matriceCoordinate[(int) (Math.random() * (matriceCoordinate.length))];
    }
    
    private static int[][] azzeraMatriceDinamica(int x, int y)
    {
        int nuovaMatrice[][] = new int[1][2];
        nuovaMatrice[0][0] = y;
        nuovaMatrice[0][1] = x;
        
        return nuovaMatrice;
    }
    
    private static int[][] aggiungiElementoMatrice(int x, int y, int[][] matrice)
    {
        int[][] nuovaMatrice = new int[matrice.length + 1][2];
        
        for(int i = 0; i < matrice.length; i++)
        {
            for(int j = 0; j < matrice[0].length; j++)
            {
                nuovaMatrice[i][j] = matrice[i][j];
            }
        }
        
        nuovaMatrice[nuovaMatrice.length - 1][0] = y;
        nuovaMatrice[nuovaMatrice.length - 1][1] = x;
        
        return nuovaMatrice;
    }
    
    private static void calcoloNaviPossibili(int[] naviPossibili, int[][] tavolaValori)
    {
        Nave nave;

        naviPossibili = new int[quanteNavi];
        
        for(int i = 0; i < quanteNavi; i++)//per ogni nave, se la nave é stata affondata allora la nave non é possbile, altrimenti si
        {
            if(pacchettoNaviG1.getNave(i).affondata() == true)
                naviPossibili[i] = 0;
            else
                naviPossibili[i] = 1;
        }
        
        boolean[][] dimensioneNave;
        for(int numeroNave = 0; numeroNave < quanteNavi; numeroNave++)
        {
            if(naviPossibili[numeroNave] == 1)
            {
                for(int orientamento = 1; orientamento < 5; orientamento++)
                {
                    nave = pacchettoNavi.getNave(numeroNave);
                    if((nave.getGradoDiSimmetria() == 0) || ((nave.getGradoDiSimmetria() == 1) && (orientamento < 3)) || ((nave.getGradoDiSimmetria() == 2) && (orientamento == 1)))
                    {
                        nave.ruotaNave(orientamento);

                        dimensioneNave = nave.getDimensione();
                        for(int y = 0; y < 10; y++)
                        {
                            for(int x = 0; x < 10; x++)
                            {
                                if(controllaPosizione(dimensioneNave, x, y) == true)
                                {
                                    modificaTavolaValori(nave, x, y, tavolaValori);
                                }
                            }
                        }
                    }
                }
            }
            else;
        }
    }
    
    private static void inizializzaTavolaValori(int[][] tavolaValori)
    {
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                if(tavolaDaMostrareG1[i][j] != Output.statusPulsante.NON_COLPITO)
                    tavolaValori[i][j] = -1000;//cosí per togliere la possibilita che colpisca un blocco giá colpito dove c'é una nave
                else
                    tavolaValori[i][j] = 0;
            }
        }
    }
    
    private static boolean controllaPosizione(boolean[][] dimensioneNave, int x, int y)//controlla che la nave messa in una posizione con il blocco in alto a destra che ha coordinate x y é disponibile, da riscrivere
    {
        //controllo out of bounds
        if((dimensioneNave[0].length + x > 10) || (dimensioneNave.length + y > 10))
        {
            return false;
        }
        else
            ;
        
        //controllo di presenza blocchi giá colpiti
        for(int i = 0; i < 10 ; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                if((j >= x) && (j < x + dimensioneNave[0].length) && (i >= y) && (i < y + dimensioneNave.length))//(da rivedere) Se i blocchi di nave sono compresi
                {
                    if(dimensioneNave[i - y][j - x] == true)//tutto ció solo se il blocco in questione e true, se no é indifferente
                    {
                        if((tavolaDaMostrareG1[i][j] == Output.statusPulsante.NON_COLPITO) || (tavolaDaMostrareG1[i][j] == Output.statusPulsante.NAVE));
                        else
                            return false;
                    }
                }
            }
        }
        return true;
    }
    
    private static void modificaTavolaValori(Nave nave, int x, int y, int[][] tavolaValori)//da riscrivere. Se una posizione per una nave é disponibile aumenta di n le celle immaginarie occupate dalla nave nella tavolaValori dove n é il numero di blocchiColpiti che la nave in quella posizione contiene, quindi se la nave ha una posizione disponibile ma non contiene alcun blocco, le celle vengono aumentate di 0
    {
        int iDiX, jDiY;//indicano i contatori della nave
        int quantiBlocchiColpiti = 0;
        int quantiBlocchiInclusi = 0;//variabile che memorizza la quantitá di blocchi che include la nave
        //calcola innanzitutto quanti blocchi colpiti include la nave in quella posizione
        for(jDiY = 0; jDiY < nave.getDimensione().length; jDiY++)
        {
            for(iDiX= 0; iDiX < nave.getDimensione()[0].length; iDiX++)
            {
                if((nave.getDimensione()[jDiY][iDiX] == true) && (tavolaDaMostrareG1[jDiY + y][iDiX + x] == Output.statusPulsante.NAVE))//se il blocco della nave é true, e quella posizione é un blocco colpito, allora aumenta il counter
                {
                    quantiBlocchiInclusi++;
                }
            }
        }
        
        for(int i = 0; i < 10; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                if(tavolaDaMostrareG1[i][j] == Output.statusPulsante.NAVE)
                {
                    quantiBlocchiColpiti++;
                }
            }
        }
        //aggiungere alla tavolaValori[][] in ogni posto dove "c'é" la nave, tranne nei blocchi colpiti, il valore di quantiBlocchiInclusi
        for(jDiY= 0; jDiY < nave.getDimensione().length; jDiY++)
        {   
            for(iDiX = 0; iDiX < nave.getDimensione()[0].length; iDiX++)
            {
                if(nave.getDimensione()[jDiY][iDiX] == true)
                {
                    if(quantiBlocchiColpiti != quantiBlocchiInclusi)
                    {
                        if(quantiBlocchiColpiti == 0)
                        {
                            tavolaValori[jDiY + y][iDiX + x] += 1;
                        }
                        else
                            tavolaValori[jDiY + y][iDiX + x] += (quantiBlocchiInclusi);
                    }
                    else if(quantiBlocchiColpiti == quantiBlocchiInclusi)
                    {
                        if(quantiBlocchiColpiti == 0)
                        {
                            tavolaValori[jDiY + y][iDiX + x] += 1;
                        }
                        else
                            tavolaValori[jDiY + y][iDiX + x] += ((quantiBlocchiInclusi * 5));
                    }
                }
            }
        } 
    }
    
    public static boolean controllaStrutturaNave(boolean[][] strutturaNave /*non lo modifica, lo legge solo*/)//controlla che non sia una nave completamente vuota ove tutti i blocchi sono false, e controlla che non ci siano blocchi separati, un blocco si considera attaccato se sopra o sotto o a destra od a sinistra od in diagonale a lui c'é un true (si  sottoinende che ci devono essere almeno due blocchi true)
    {
        int nTrue = 0;
        int i, j;
        boolean[][] strutturaNaveGrossa = new boolean[strutturaNave.length + 2][strutturaNave[0].length + 2];//serve per creare una sorta di cornice intorno a stutturaNave[][] cosí da non aver problemi di ArrayIndexOutOfBoundsException mentre controllo i blocchi
        
        for(i = 0; i < strutturaNave.length; i++)
        {
            for(j = 0; j < strutturaNave[i].length; j++)
            {
                if(strutturaNave[i][j] == true)
                    nTrue++;
            }
        }
        if(nTrue < 2)
            return false;
        
        //copia in strutturaNaveGrossa[][] strutturaNave[][]
        for(i = 0; i < strutturaNave.length; i++)
        {
            for(j = 0; j < strutturaNave[i].length; j++)
            {
                strutturaNaveGrossa[i + 1][j + 1] = strutturaNave[i][j];
            }
        }
        
        //controlla ogni blocco true di strutturaNave[][] (simulato all'interno di strutturaGrossa[][])
        for(i = 0; i < strutturaNave.length; i++)
        {
            for(j = 0; j < strutturaNave[i].length; j++)
            {
                if(strutturaNaveGrossa[i + 1][j + 1] == true)
                {
                    if((strutturaNaveGrossa[i][j] == true) || (strutturaNaveGrossa[i][j + 1] == true) || (strutturaNaveGrossa[i][j + 2] == true) || (strutturaNaveGrossa[i + 1][j] == true) || (strutturaNaveGrossa[i + 1][j + 2] == true) || (strutturaNaveGrossa[i + 2][j] == true) || (strutturaNaveGrossa[i + 2][j + 1] == true) || (strutturaNaveGrossa[i + 2][j + 2] == true))
                        ;
                    else
                        return false;
                        //se il blocco non e attaccato a nessun altro blocco, neanche diagonalmente, allora la nave non é accettabile
                }
            }
        }
        return (true && controllaDimensione(strutturaNave));//la nave é accettabile
    }
    
    private static boolean controllaDimensione(boolean[][] dimensione)
    {
        int primax = 11, primay = 11, ultimax = -1, ultimay = -1;
        
        for(int j = 0; (j < 10); j++)
        {
            for(int i = 0; (i < 10); i++)
            {
                if((dimensione[i][j]))
                {
                    if(j < primax)
                        primax = j;
                    if(i < primay)
                        primay = i;
                    
                    if(j > ultimax)
                        ultimax = j;
                    if(i > ultimay)
                        ultimay = i;
                }
            }
        }
        
        boolean continuaI, continuaJ;
        
        //controlla se ci fossero righe vuote dopo primax e prima di ultimax
        for(int i = primay; (i <= ultimay); i++)
        {
            continuaJ = true;
            for(int j = primax; (j <= ultimax) && (continuaJ); j++)
            {
                if(dimensione[i][j])
                {
                    continuaJ = false;
                }
            }
            if(continuaJ)
                return false;
        }
        
        //controlla se ci fossero colonne vuote dopo primay e prima di ultimay
        for(int j = primax; (j <= ultimax); j++)
        {
            continuaI = true;
            for(int i = primay; (i <= ultimay) && (continuaI); i++)
            {
                if(dimensione[i][j])
                {
                    continuaI = false;
                }
            }
            if(continuaI)
                return false;
        }
        
        ultimax++; ultimay++;
        return ((Math.abs(primax - ultimax)) * Math.abs(primay - ultimay)) <= 16;
    }
    
    public static void visualizzaTavolaValori(int[][] tavolaValori)
    {
        for(int i = 0; i < tavolaValori.length; i++)
        {
            for(int j = 0; j < tavolaValori[i].length; j++)
            {
                System.out.print(tavolaValori[i][j] + "\t");
            }
            System.out.println();
        }
    }
    
    public static void inizializzaTavolaDaMostrareGx()
    {
        for(int i = 0; i < tavolaDaMostrareG1.length; i++)
        {
            for(int j = 0; j < tavolaDaMostrareG1[i].length; j++)
            {
                tavolaDaMostrareG1[i][j] = Output.statusPulsante.NON_COLPITO;
            }
        }
    }
    
    public static Output.statusPulsante[][] getTavolaDaMostrare()
    {
        return tavolaDaMostrareG1;
    }
    
    public static void updateTavolaDaMostrare(Output.statusPulsante[][] status)
    {
        for(int i = 0; i < status.length; i++)
        {
            for(int j = 0; j < status[i].length; j++)
            {
                tavolaDaMostrareG1[i][j] = status[i][j];
            }
        }
    }
    
    public static void inizializzaPacchettoNave(PacchettoNavi pacchettoG1, PacchettoNavi pacchettoNavi1)
    {
        pacchettoNavi1.copy(pacchettoNavi);
        pacchettoG1.copy(pacchettoNaviG1);
    }
}

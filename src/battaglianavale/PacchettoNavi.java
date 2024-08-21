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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.EOFException;
import java.awt.Color;

public class PacchettoNavi {
    
    private int quanteNavi;
    private int naviInserite = 0;
    private final Nave navi[];
    
    public PacchettoNavi(int quanteNavi)
    {
        this.quanteNavi = quanteNavi;
        navi = new Nave[quanteNavi];
    }
    
    /*public int creaNaveLineare(int lunghezzaBlocco, String nome)//returna 0 se la nave é stata aggiuta con successo, e negativo se ci sono stati errori, funziona
    {        
        if(this.naviInserite == this.navi.length)
        {
            return -1;//non c'e piú spazio
        }
        else
        {
            boolean dimensione[][] = new boolean[lunghezzaBlocco][1];//1 perché é una nave lineare
            for(int i = 0; i < lunghezzaBlocco; i++)
            {
                dimensione[i][0] = true;
            }
            Nave nuovaNaveLineare = new Nave(dimensione, nome, 1);//l'1 é il grado di simmetria, il quale, in una nave lineare é sempre 1
            this.navi[naviInserite] = nuovaNaveLineare;
            this.naviInserite++;
            return 0;
        }
    }*/
    public void inizializzaPacchetto()
    {
        for(int i = 0; i < quanteNavi; i++)
        {
            navi[i] = new Nave();
        }
        
        naviInserite = 0;
    }
    
    public int creaNaveNonLineare(boolean dimensione[][], String nome)//funziona
    {        

        if(naviInserite == navi.length)
        {
            return -1;//non c'e piú spazio
        }
        else
        {
            Color coloreNave = null;
            switch(naviInserite)
            {
                case 0:
                    coloreNave = new Color(0, 0, 0);
                    break;
                case 1:
                    coloreNave = new Color(237, 28, 36);
                    break;
                case 2:
                    coloreNave = new Color(22, 203, 7);
                    break;
                case 3:
                    coloreNave = new Color(255, 127, 39);
                    break;
                case 4:
                    coloreNave = new Color(255, 242, 0);
                    break;
                case 5:
                    coloreNave = new Color(163, 73, 164);
                    break;
            }
            Nave naveNonLineare = new Nave(dimensione, nome, 0, coloreNave);
            //deve trimmare sia righe che colonne, quindi l'operazione si esegue due volte
            naveNonLineare.trimmaDimensione();
            naveNonLineare.calcolaGradoDiSimmetria();
            
            navi[naviInserite] = naveNonLineare;
            naviInserite++;
            return 0;
        }
    }
    
    public void visualizzaOgniInfoNave()
    {
        for(int i = 0; i < quanteNavi; i++)
        {
            navi[i].visualizzaInfoNave();
        }
    }
    
    public void inizializzaOgniInfoNave()
    {
        for(int i = 0; i < quanteNavi; i++)
        {
            navi[i].inizializzaInfoNave();
        }
    }
    
    public String toStringFake()
    {
        String stringaFake = "";
        for(int i = 0; i < quanteNavi; i++)
        {
            stringaFake += i + "^ nave:\nNome: " + navi[i].getNome() + "\nCarattere rappresentativo: " + navi[i].getCarattereRappresentativo() + "\n\n\n";
        }
        return stringaFake;
    }
    
    public boolean controllaPacchettoNavi() throws NumeroInsufficienteDiNaviException//controlla tutte le navi di un pacchetto di navi per capire se questo pacchetto é utilizzabile, returna false se una nave é uguale a null
    {
        /*int areaTotale = 0;*/
        for(int i = 0; i < this.navi.length; i++)
        {
            if(this.navi[i] == null)
                throw new NumeroInsufficienteDiNaviException();
        }
        
        if(naviInserite < quanteNavi)
        {
            return false;
        }
        
        return true;
    }
    
    public int getQuanteNavi()
    {
        return quanteNavi;
    }
    
    public boolean[][] getDimensioneNave(int i)
    {
        return navi[i].getDimensione();
    }
    
    public Nave getNave(int i)
    {
        Nave copiaNave = new Nave();
        navi[i].copy(copiaNave);
        return copiaNave;
    }
    
    public void ruotaNave(int nave, int orientamento)
    {
        navi[nave].ruotaNave(orientamento);
    }
    
    @Override
    public String toString()
    {
        String pacchettoNaviInStringa = "";
        for(int i = 0; i < this.naviInserite; i++)
        {
            pacchettoNaviInStringa += this.navi[i].toString();
            pacchettoNaviInStringa += "\n\n\n";
        }
        
        return pacchettoNaviInStringa;
    }
    
    public void copy(PacchettoNavi altroPacchetto)//copia nel pacchetto altroPacchetto ció che é contenuto nel pacchetto chiamante (this), é il contrario di un costruttore
    {
       altroPacchetto.quanteNavi = this.quanteNavi;
       for(int i = 0; i < quanteNavi; i++)
       {
           Nave nave = new Nave();
           altroPacchetto.navi[i] = nave;
           this.navi[i].copy(altroPacchetto.navi[i]);
           altroPacchetto.naviInserite++;
       }
    }
    
    public int areaTotalePacchetto()
    {
        int areaNavi = 0;
        for(int i = 0; i < this.navi.length; i++)
        {
            areaNavi += navi[i].getDimensione().length * navi[i].getDimensione()[0].length;
        }
        return areaNavi;
    }
    
    public boolean inserimentoManuale(int x, int y, int orientamento, Nave[][] tavolaGiocatoreX, int naveI)//returna false se la posizione con quell'orientamento non funziona o se naveI non é una nave in quel pacchetto
    {
        this.navi[naveI].ruotaNave(orientamento);
        if((naveI < 0) || (naveI > quanteNavi - 1 ))
            return false;
        if(controllaPosizione(tavolaGiocatoreX, x, y, naveI) == false)
            return false;
        
        int contatoreX;
        int contatoreY = y;
        boolean[][] copiaDimensione = navi[naveI].getDimensione();
        int nBlocco = 0;
        
        //inserimento navi
        for(int j = 0; j < copiaDimensione.length; j++)
        {
            contatoreX = x;
            for(int k = 0; k < copiaDimensione[j].length; k++)
            {
                if(copiaDimensione[j][k] == true)
                {
                    tavolaGiocatoreX[contatoreY][contatoreX] = navi[naveI];//assegnare ad ogni blocco della nave il collegamento alla nave intera stessa... é la mia unica idea per accedere ad infoNave[]
                    navi[naveI].setTreCasellePerVoltaInfoNave(contatoreX, contatoreY, 0, nBlocco);
                    nBlocco++;
                }
                contatoreX++;
            }
            contatoreY++;
        }
        
        return true;
    }
    
    public int inserimentoRandom(Nave[][] tavolaGiocatoreX)//returna -1 se non é avvenuto l'inserimento
    {
        int x, y;//casuali, serviranno per la posizione, da 1 a 10, indicano il blocco in alto a destra
        int orientamento;//valore da 1 a 4
        int contatoreX;
        int contatoreY;
        int contatoreAnnoi = 0;
        int nBlocco = 0;//conta a quale blocco della nave siamo arrivati
        
        for(int i = 0; i < tavolaGiocatoreX.length; i++)
        {
            for(int j = 0; j < tavolaGiocatoreX[i].length; j++)
            {
                tavolaGiocatoreX[i][j] = null;
            }
        }
        
        for(int i = 0; i < this.quanteNavi; i++)
        {
            nBlocco = 0;
            contatoreAnnoi = 0;
            //controlla la posizione
            do
            {
                if(contatoreAnnoi >= 1000)
                {
                    return -1;
                }
                x = (int) (Math.random() *9);
                y = (int) (Math.random() *9);
                orientamento = (int) (Math.random() *4) + 1;
                contatoreAnnoi++;
            }while(!controllaPosizione(tavolaGiocatoreX, x, y, i));
            boolean[][] copiaDimensione = navi[i].getDimensione();
            
            contatoreX = x;
            contatoreY = y;
            navi[i].ruotaNave(orientamento);
            //inserimento navi
            for(int j = 0; j < copiaDimensione.length; j++)
            {
                contatoreX = x;
                for(int k = 0; k < copiaDimensione[j].length; k++)
                {
                    if(copiaDimensione[j][k] == true)
                    {
                        tavolaGiocatoreX[contatoreY][contatoreX] = navi[i];//assegnare ad ogni blocco della nave il collegamento alla nave intera stessa... é la mia unica idea per accedere ad infoNave[]
                        navi[i].setTreCasellePerVoltaInfoNave(contatoreX, contatoreY, 0, nBlocco);
                        nBlocco++;
                    }
                    contatoreX++;
                }
                contatoreY++;
            }
            
        }
        return 0;
    }
    
    private boolean controllaPosizione(Nave[][] tavolaGiocatoreX, int x, int y, int i)//l'ultimo parametro serve per cancellare e non considerare la nave da cancellare
    {
        boolean[][] copiaDimensione = navi[i].getDimensione();
        int altezzaNave = copiaDimensione.length;
        int lunghezzaNave = copiaDimensione[0].length;
        
        //controllo outOfBounds
        if(altezzaNave + y > 10)
            return false;//la posizione non é valida
        if(lunghezzaNave + x > 10)
            return false;
        
        int jDiy = y;//si legge "La j (contatore) di y"
        int kDix;
        
        for(int j = 0; j < altezzaNave; j++)
        {
            kDix = x;
            for(int k = 0; k < lunghezzaNave; k++)
            {
                if(copiaDimensione[j][k] == true)//diamo la possibilitá di mettere le navi attaccate
                {
                    if((tavolaGiocatoreX[jDiy][kDix] != navi[i]) && (tavolaGiocatoreX[jDiy][kDix] != null))//se c'é una nave diversa da nave e diverso da null
                        return false;
                }
                kDix++;
            }
            jDiy++;
        }
        
        for(int j = 0; j < tavolaGiocatoreX.length; j++)
        {
            for(int k = 0; k < tavolaGiocatoreX[j].length; k++)
            {
                if(tavolaGiocatoreX[j][k] == navi[i])
                    tavolaGiocatoreX[j][k] = null;
            }
        }
        return true;
    }
    
    public boolean controllaVittora()
    {
        int[] infoNave;
        for(int i = 0; i < quanteNavi; i++)
        {
            infoNave = navi[i].getInfoNave();
            for(int j = 2; j < infoNave.length; j += 3)
            {
                if(infoNave[j] != 1)
                    return false;
            }
        }
        return true;
    }
    
    public int salvaFileBinario(String nomeCartella)//Salva in diversi file binari ogni nave del pacchetto, funziona
    {
        File checkEsistenza = new File("PacchettiNave/" + nomeCartella);
        
        int contatoreCartelle = 1;
        
        while(checkEsistenza.exists())
        {
            checkEsistenza = new File("PacchettiNave/" + nomeCartella + "(" + contatoreCartelle + ")");
            contatoreCartelle++;
        }
        
        if(contatoreCartelle == 1)
            ;
        else
            nomeCartella += "(" + contatoreCartelle + ")";
        
        ObjectOutputStream output;
        new File("PacchettiNave/" + nomeCartella).mkdir();
        try
        {
            for(int i = 0; i < this.quanteNavi; i++)
            {
                String nomeFile = "Nave" + i +".dat";
                output = new ObjectOutputStream(new FileOutputStream("PacchettiNave/" + nomeCartella + "/" + nomeFile));
                output.writeObject(this.navi[i]);
                output.close();
            }
        }
        catch(FileNotFoundException e)
        {
            return -1;
        }
        catch(IOException e)
        {
            return -1;
        }
        return 0;//procedura avvenuta correttamente
    }
    
    public int caricaFileBinario(String nomeCartella)//funziona
    {
        ObjectInputStream input;
        try
        {
            naviInserite = 0;
            for(int i = 0; i < this.quanteNavi; i++)
            {
                String nomeFile = "Nave" + i + ".dat";
                input = new ObjectInputStream(new FileInputStream("PacchettiNave/" + nomeCartella + "/" + nomeFile));
                this.navi[i] = (Nave) input.readObject();
                this.naviInserite++;
                input.close();
            }
            try
            {
                if(this.controllaPacchettoNavi() == false)
                    return -2;
            }
            catch(NumeroInsufficienteDiNaviException e)
            {
                return -1;
            }
        }
        catch(FileNotFoundException e)
        {
            return -2;
        }
        catch(IOException e)
        {
            return -3;
        }
        catch(ClassNotFoundException e)
        {
            return -1;
        }
        return 0;
    }
    
    public static String[] nomiPacchettiNave()
    {
        File pacchettiNaveDirctory = new File("PacchettiNave");
        File[] pacchettiNave = pacchettiNaveDirctory.listFiles();
        String[] nomiPacchettiNave = new String[pacchettiNave.length];
        
        for(int i = 0; i < pacchettiNave.length; i++)
        {
            nomiPacchettiNave[i] = pacchettiNave[i].getName();
        }
        
        return nomiPacchettiNave;
    }
    
    /*public int salvaFileDiTesto(String nomeCartella)//salva il pacchetto navi su di un file di testo, e returna 0 se il salvataggio avviene correttamente, funziona
    {        
        PrintWriter file;
        try
        {
            for(int i = 0; i < quanteNavi; i++)
            {
                boolean[][] cloneDimensione = this.navi[i].getDimensione();//prende l'array dimensione[][] che deve scrivere
                String nomeFile = "Nave" + i + ".txt";
                new File(nomeCartella).mkdir();
                file = new PrintWriter(nomeCartella + "/" + nomeFile);//scrive ogni nave in un file diverso numerati nella cartella pacchetto navi, ognuno sará un file di testo
                
                for(int j = 0; j < cloneDimensione.length; j++)//scrive l'arrayDimensione
                {
                    for(int k = 0; k < cloneDimensione[j].length; k++)
                    {
                        file.print(booleanToInt(cloneDimensione[j][k]) + " ");//scrive i valori con degli 0 e 1
                    }
                    file.println();
                }
                //due righe vuote
                file.println();
                file.println();
                
                file.print(navi[i].getCarattereRappresentativo());//scrive il carattere rappresentativo
                
                file.close();
            }
        }
        catch(FileNotFoundException e)
        {
            return -1;
        }
        
        return 0;
    }
    
    public int caricaFileDiTEsto(String nomeCartella)//non solo dovra leggere i dati, ma anche ricostruire degli oggetti di tipo Nave dato che leggerá prima l'array dimensione e poi il carattere rappresentativo
    {        
        Scanner inputFile;
        
        try
        {
            for(int i = 0; i < quanteNavi; i++)
            {
                inputFile = new Scanner(new File(nomeCartella + "/Nave" + i + ".txt"));
                boolean[][] dimensione = new boolean[quanteLineeDimensione(nomeCartella, i)][];
                char carattereRappresentativo;
                
                String stringaLetta;
                int[] stringaLettaBooleana;
                //leggere dimensione[][]
                for(int j = 0; j < dimensione.length; j++)
                {
                    stringaLetta = inputFile.nextLine();
                    stringaLettaBooleana = traduciLinea(stringaLetta);
                    
                    for(int k = 0; k < dimensione[j].length; k++)
                    {
                        dimensione[j][k] = intToBoolean(stringaLettaBooleana[k]);
                    }
                }
                //in teoria dovrebbe essere sotto di due linee
                inputFile.nextLine();
                inputFile.nextLine();
                
                carattereRappresentativo = inputFile.next().charAt(1);
                inputFile.close();//chiude il file
                
                Nave nuovaNave = new Nave(carattereRappresentativo, dimensione);//crea la nave
                navi[i] = nuovaNave;
            }
        }
        catch(FileNotFoundException e)
        {
            return -1;
        }
        
        return 0;
    }
    
    private static int quanteOccorrenze(String stringa, char carattereDaContare)//conta quante sono le occorrenze di carattereDaContare in una stringa
    {
        int occorrenzeCarattere = 0;
        
        for(int i = 0; i < stringa.length(); i++)
        {
            if(stringa.charAt(i) == carattereDaContare)
                occorrenzeCarattere++;
        }
        return occorrenzeCarattere;
    }
    
    private static boolean intToBoolean(int valoreIntero)
    {
        if(valoreIntero == 1)
            return true;
        else
            return false;
    }
    
    private static int booleanToInt(boolean valoreBooleano)
    {
        if(valoreBooleano == true)
            return 1;
        else
            return 0;
    }
    
    private static int quanteLineeDimensione(String nomeCartella, int i)
    {
        Scanner inputFile;
        int linee = 0;
        try
        {
                inputFile = new Scanner(new File(nomeCartella + "/Nave" + i + ".txt"));
                while(inputFile.hasNextLine() && quanteOccorrenze(inputFile.nextLine(), ' ') > 0)
                {
                    linee++;
                }
                inputFile.close();
        }
        catch(FileNotFoundException e)
        {
            return -1;
        }
        
        return linee;
    }
    
    private static int[] traduciLinea(String linea)//prende una stringa e trasforma in un array di interi binari
    {
        int[] lineaTradotta = new int[linea.length() - quanteOccorrenze(linea, ' ')];//il nostro array con i numeri 0 e 1 sará lungo linea.length() - il numero di spazi nella linea
        for(int i = 0; i < linea.length(); i++)
        {
            if(linea.charAt(i) == '0')
                lineaTradotta[i] = 0;
            else if(linea.charAt(i) == '1')
                lineaTradotta[i] = 1;
        }
        
        return lineaTradotta;
    }*/
   
}

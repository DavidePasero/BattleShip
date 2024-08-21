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
import java.io.Serializable;
import java.io.IOException;
import java.awt.Color;

public class Nave implements Serializable{
    
    private int infoNave[];//é un array dove il primo elemento é la posizione X del primo blocco, il secondo elemento é la posizione Y del primo blocco, il terzo é lo status del blocco (se é stato colpito o meno), il quarto la posizione X del secondo blocco ecc... Viene settato dopo, nel programma
    private char carattereRappresentativo;
    private Color coloreRappresentativo;
    private boolean dimensione[][];//idea: array che rappresenta esattamente la dimensione della nave
    private String nome;
    private boolean dimensioneIniziale[][];
    private int gradoDiSimmetria;//0 se tutti gli orientamenti sono diversi, 1 se ce ne sono due uguali, 2 se tutti e 4 sono uguali
    
    public Nave(boolean dimensione[][], String nome, int gradoDiSimmetria, Color colore)
    {
        this.dimensione = new boolean[dimensione.length][dimensione[0].length];
        this.dimensioneIniziale = new boolean[dimensione.length][dimensione[0].length];
        this.carattereRappresentativo = '*';
        for(int i = 0; i < dimensione.length; i++)
        {
            for(int j = 0; j < dimensione[i].length; j++)
            {
                this.dimensione[i][j] = dimensione[i][j];
                this.dimensioneIniziale[i][j] = dimensione[i][j];
            }
        }
        this.nome = nome;
        this.gradoDiSimmetria = gradoDiSimmetria;
        this.coloreRappresentativo = colore;
    }
    
    public Nave(boolean[][] dimensione)
    {
        this.dimensione = new boolean[dimensione.length][dimensione[0].length];
        this.dimensioneIniziale = new boolean[dimensione.length][dimensione[0].length];
        for(int i = 0; i < dimensione.length; i++)
        {
            for(int j = 0; j < dimensione[i].length; j++)
            {
                this.dimensione[i][j] = dimensione[i][j]; 
                this.dimensioneIniziale[i][j] = dimensione[i][j];
            }
        }
        this.carattereRappresentativo = '_';
        this.nome = "_";
    }
    
    public Nave()
    {
        this.nome = "*";
        boolean[][] dimensioneDefault = new boolean[4][4];
        this.dimensione = dimensioneDefault;
        this.dimensioneIniziale = dimensioneDefault;
        carattereRappresentativo = '_';
    }
    
    public void copy(Nave altraNave)
    {
        altraNave.dimensione = new boolean[this.dimensione.length][this.dimensione[0].length];
        altraNave.dimensioneIniziale = new boolean[this.dimensioneIniziale.length][this.dimensioneIniziale[0].length];
        altraNave.carattereRappresentativo = this.carattereRappresentativo;
        for(int i = 0; i < dimensione.length; i++)
        {
            for(int j = 0; j < this.dimensione[i].length; j++)
            {
                altraNave.dimensione[i][j] = this.dimensione[i][j]; 
            }
        }
        
        for(int i = 0; i < this.dimensioneIniziale.length; i++)
        {
            for(int j = 0; j < this.dimensioneIniziale[0].length; j++)
            {
                altraNave.dimensioneIniziale[i][j] = this.dimensioneIniziale[i][j];
            }
        }
        
        altraNave.nome = this.nome;
        
        if(this.infoNave != null)
        {
            altraNave.infoNave = new int[this.infoNave.length];

            for(int i = 0; i < infoNave.length; i++)
            {
                altraNave.infoNave[i] = this.infoNave[i];
            }
        }
        
        altraNave.coloreRappresentativo = coloreRappresentativo;
    }
    
    public void setInfoNave(int infoNave[])
    {
        for(int i = 0; i < this.infoNave.length; i++)
        {
            this.infoNave[i] = infoNave[i];
        }
    }
    
    public String getNome()
    {
        String stringaClone = "" + this.nome;
        return stringaClone;
    }
    
    public int[] getInfoNave()
    {
        int[] cloneInfoNave = new int[infoNave.length];
        for(int i = 0; i < infoNave.length; i++)
        {
            cloneInfoNave[i] = infoNave[i];
        }
        return cloneInfoNave;
    }
    
    public Color getColoreNave()
    {
        return coloreRappresentativo;
    }
    
    public int getGradoDiSimmetria()
    {
        return gradoDiSimmetria;
    }
    
    public void inizializzaInfoNave()
    {
        int numeroDiBlocchi = 0;
        for(int i = 0; i < this.dimensione.length; i++)
        {
            for(int j = 0; j < this.dimensione[i].length; j++)
            {
                if(dimensione[i][j] == true)
                {
                    numeroDiBlocchi++;
                }
            }
        }
        
        this.infoNave = new int[numeroDiBlocchi * 3];
    }
    
    public int setTreCasellePerVoltaInfoNave(int x, int y, int status, int bloccoI)//returna -1 se non é avvenuto il set
    {
        /*int numeroDiBlocchi = 0;
        for(int i = 0; i < this.dimensione.length; i++)
        {
            for(int j = 0; j < this.dimensione[i].length; j++)
            {
                if(dimensione[i][j] == true)
                {
                    numeroDiBlocchi++;
                }
            }
        }
        
        if(bloccoI > numeroDiBlocchi)
            return -1;*/
        
        this.infoNave[bloccoI * 3] = x;
        this.infoNave[bloccoI * 3 + 1] = y;
        this.infoNave[bloccoI * 3 + 2] = status;//variabile binaria: 0 - non colpito, 1 - colpito
        return 0;
    }
    
    public void visualizzaInfoNave()
    {
        for(int i = 0; i < infoNave.length; i++)
        {
            System.out.println(infoNave[i] + " ");
        }
    }
    
    public boolean setStatusBlocco(int x, int y)//returna true se il set e avvenuto correttamente
    {
        for(int i = 0; i < infoNave.length - 2; i += 3)
        {
            if((x == infoNave[i]) && (y == infoNave[i + 1]))
            {
                infoNave[i + 2] = 1;
                return true;
            }
        }
        return false;
    }
    
    public boolean[][] getDimensione()
    {
        boolean[][] cloneDimensione = new boolean[this.dimensione.length][this.dimensione[0].length];
        for(int i = 0; i < this.dimensione.length; i++)
        {
            for(int j = 0; j < this.dimensione[i].length; j++)
            {
                cloneDimensione[i][j] = this.dimensione[i][j];
            }
        }
        return cloneDimensione;
    }
    
    public boolean[][] getDimensioneIniziale()
    {
        boolean[][] cloneDimensione = new boolean[this.dimensioneIniziale.length][this.dimensioneIniziale[0].length];
        for(int i = 0; i < this.dimensioneIniziale.length; i++)
        {
            for(int j = 0; j < this.dimensioneIniziale[i].length; j++)
            {
                cloneDimensione[i][j] = this.dimensioneIniziale[i][j];
            }
        }
        return cloneDimensione;
    }
    
    public char getCarattereRappresentativo()
    {
        return carattereRappresentativo;
    }
    
    public boolean affondata()
    {
        for(int i = 2; i < this.infoNave.length; i += 3)
        {
            if(this.infoNave[i] == 0)
                return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        String naveInStringa = "";
        naveInStringa += "Nome della nave: " + nome + "\n\n";
        naveInStringa += "Forma nave:\n";
        for(int i = 0; i < dimensione.length; i++)
        {
            for(int j = 0; j < dimensione[i].length; j++)
            {
                if(dimensione[i][j] == true)
                {
                        naveInStringa += this.carattereRappresentativo;
                }
                else
                    naveInStringa += " ";
            }
            naveInStringa += "\n";
        }
        naveInStringa += "\n";
        naveInStringa += "Carattere rappresentativo:\n";
        naveInStringa += "" + carattereRappresentativo + "\n\n";
        naveInStringa += "Grado di simmetria:\n" + getGradoDiSimmetria() + "\n\n\n";
        
        return naveInStringa;
    }
    
    public void ruotaNave(int orientamento)
    {
        switch(orientamento)
        {
            case 1://la nave viene lasciata cosí com'é
                dimensione = new boolean[this.dimensioneIniziale.length][this.dimensioneIniziale[0].length];
                for(int i = 0; i < this.dimensioneIniziale.length; i++)
                {
                    for(int j = 0; j < this.dimensioneIniziale[0].length; j++)
                    {
                        dimensione[i][j] = this.dimensioneIniziale[i][j];
                    }
                }
                break;
            case 2:
                dimensione = new boolean[dimensioneIniziale[0].length][dimensioneIniziale.length];
                for(int i = 0; i < dimensioneIniziale.length; i++)
                {
                    for(int j = 0; j < dimensioneIniziale[0].length; j++)
                    {
                        dimensione[j][dimensioneIniziale.length - 1 - i] = dimensioneIniziale[i][j];
                    }
                }
                break;
            case 3:
                dimensione = new boolean[dimensioneIniziale.length][dimensioneIniziale[0].length];
                for(int i = 0; i < dimensioneIniziale.length; i++)
                {
                    for(int j = 0; j < dimensioneIniziale[0].length; j++)
                    {
                        dimensione[dimensioneIniziale.length - 1 - i][dimensioneIniziale[0].length - 1 - j] = dimensioneIniziale[i][j];
                    }
                }
                break;
            case 4:
                dimensione = new boolean[dimensioneIniziale[0].length][dimensioneIniziale.length];
                for(int i = 0; i < dimensioneIniziale.length; i++)
                {
                    for(int j = 0; j < dimensioneIniziale[0].length; j++)
                    {
                        dimensione[dimensioneIniziale[0].length - 1 - j][i] = dimensioneIniziale[i][j];
                    }
                }
                break;
            default:
                System.out.println("ERRORE in ruotaNave()");
                System.exit(0);
        }
    }
    
    public void trimmaDimensione()
    {
        boolean[][] nuovaDimensione;
        int primaRiga = 0, ultimaRiga = 0, primaColonna = 0, ultimaColonna = 0;
        
        //capisce quale sia l'ultima riga da non trimmare
        for(int i = 0; i < dimensione.length; i++)
        {
            for(int j = 0; j < dimensione[0].length; j++)
            {
                if(this.dimensione[i][j] == true)
                {
                    ultimaRiga = i;
                    break;
                }
            }
        }
        
        //capisce quale sia la prima riga da non trimmare
        for(int i = dimensione.length - 1; i >= 0; i--)
        {
            for(int j = 0; j < dimensione[0].length; j++)
            {
                if(this.dimensione[i][j] == true)
                {
                    primaRiga = i;
                    break;
                }
            }
        }
        
        //trimma colonne
        for(int j = 0; j < dimensione[0].length; j++)
        {
            for(int i = 0; i < dimensione.length; i++)
            {
                if(dimensione[i][j] == true)
                {
                    ultimaColonna = j;
                    break;
                }
            }
        }
        
        for(int j = dimensione[0].length - 1; j >= 0; j--)
        {
            for(int i = 0; i < dimensione.length; i++)
            {
                if(dimensione[i][j] == true)
                {
                    primaColonna = j;
                    break;
                }
            }
        }

        nuovaDimensione = new boolean[dimensione.length - ((primaRiga) + (dimensione.length - ultimaRiga)) + 1][this.dimensione[0].length - ((primaColonna) + (this.dimensione[0].length - ultimaColonna - 1))];

        //copia in nuovaDimensione solo la parte non da trimmare
        for(int i = 0; i < dimensione.length; i++)
        {
            for(int j = 0; j < dimensione[0].length; j++)
            {
                if((i >= primaRiga) && (i <= ultimaRiga) && (j >= primaColonna) && (j <= ultimaColonna))
                {
                    nuovaDimensione[i - (primaRiga)][j - (primaColonna)] = this.dimensione[i][j];
                }
            }
        }
        
        dimensione = nuovaDimensione;
        dimensioneIniziale = new boolean[dimensione.length][dimensione[0].length];
        for(int i = 0; i < dimensione.length; i++)
        {
            for(int j = 0; j < dimensione[0].length; j++)
            {
                dimensioneIniziale[i][j] = dimensione[i][j];
            }
        }
    }
    
    public void calcolaGradoDiSimmetria()
    {
        this.ruotaNave(1);
        boolean[][] orientamento1 = new boolean[dimensione.length][dimensione[0].length];
        
        for(int i = 0; i < dimensione.length; i++)
        {
            for(int j = 0; j < dimensione[0].length; j++)
            {
                orientamento1[i][j] = dimensione[i][j];
            }
        }
        
        
        this.ruotaNave(2);
        boolean[][] orientamento2 = new boolean[dimensione.length][dimensione[0].length];
        
        for(int i = 0; i < dimensione.length; i++)
        {
            for(int j = 0; j < dimensione[0].length; j++)
            {
                orientamento2[i][j] = dimensione[i][j];
            }
        }
        
        
        this.ruotaNave(3);
        boolean[][] orientamento3 = new boolean[dimensione.length][dimensione[0].length];
        
        for(int i = 0; i < dimensione.length; i++)
        {
            for(int j = 0; j < dimensione[0].length; j++)
            {
                orientamento3[i][j] = dimensione[i][j];
            }
        }
        
        //orientamento4 non serve dato che se una nave é simmetrica di tipo 1 allora basta controllare solo due orientamenti opposti (1 e 3 oppure 2 e 4), mentre per controllare se é simmetrica di tipo 2 allora bisogna fare cio che é stato detto prima ed in piú controllare anche un altro dei due orientamenti rimasti (2 o 4)
        
        boolean continua = true;
        for(int i = 0; (i < orientamento1.length) && (continua == true); i++)
        {
            for(int j = 0; (j < orientamento1[0].length) && (continua == true); j++)
            {
                if(orientamento1[i][j] != orientamento3[i][j])
                {
                    gradoDiSimmetria = 0;
                    continua = false;
                }
                else;
            }
        }
        
        if(continua == true)
        {
            gradoDiSimmetria = 1;
            if((orientamento1.length == orientamento2.length) && (orientamento1[0].length == orientamento2[0].length))
            {
                for(int i = 0; (i < orientamento1.length) && (continua == true); i++)
                {
                    for(int j = 0; (j < orientamento1[0].length) && (continua == true); j++)
                    {
                        if(orientamento1[i][j] != orientamento2[i][j])
                        {
                            gradoDiSimmetria = 1;
                            continua = false;
                        }
                        else;
                    }
                }
            }
            else
                continua = false;
            if(continua == true)
                gradoDiSimmetria = 2;
        }
    }
}
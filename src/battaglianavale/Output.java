package battaglianavale;

/**
 *
 * @author IlDivinoPase
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;

//l'idea è che Output sarà una finestra che conterrà diversi pannelli, e la finestra si riimensionerà in base a ciò che deve contenere
public class Output extends JFrame
{
    private static final int QUANTE_NAVI = 6;
    private static final PacchettoNavi PACCHETTO_NAVI = new PacchettoNavi(QUANTE_NAVI);
    private static final PacchettoNavi PACCHETTOG1 = new PacchettoNavi(QUANTE_NAVI);
    private static final PacchettoNavi PACCHETTOG2 = new PacchettoNavi(QUANTE_NAVI);
    
    private static final Nave tavolaNaviG1[][] = new Nave[10][10];//UTILIZZO: tavolaNaviGiocatore[][] é la tavola che servirá per collegarsi agli oggetti Nave, utilizando x e y inseriti dal giocatore
    private static final Nave tavolaNaviG2[][] = new Nave[10][10];//UTILIZZO: tavolaNaviGiocatore[][] é la tavola che servirá per collegarsi agli oggetti Nave, utilizando x e y inseriti dal giocatore
    
    private static enum qualePannello {PRIMO_PANNELLO, PANNELLO_CREDITS, PANNELLO_MENU, PANNELLO_IMPOSTAZIONIPL, PANNELLO_IMPOSTAZIONIAI, PANNELLO_POSIZIONA_NAVI1, PANNELLO_POSIZIONA_NAVI2, PANNELLO_PARTITA, PANNELLO_EDITOR, PANNELLO_VITTORIA, RESIZA};//messaggio è una variabile che indica quale pannello vada visualizzato
    private static enum chiGiocaPrimo {VALORE_INIZIALE, G1, G2, CASUALE};
    public static enum statusPulsante {NON_COLPITO, ACQUA, NAVE, AFFONDATA};
    
    private qualePannello messaggio;
    
    private static String iconsPath;

    public Output(String iconPack)
    {
        iconsPath = "Immagini/" + iconPack + "/";
        this.setSize(1080, 720);
        this.setIconImage(new ImageIcon(iconsPath + "IconaFinestra.png").getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PrimoPannello menu = new PrimoPannello();
        this.add(menu);
        this.getContentPane().setBackground(Color.WHITE);
        this.setTitle("Up's new BattleShip");
    }

    public static JButton factoryPulsanteBase (String testo, String actionCommand, boolean active, boolean visible)
    {
        JButton pulsante = new JButton();
        JLabel etichetta = new JLabel(testo, JLabel.CENTER);
        // Set text color to white
        etichetta.setForeground(Color.WHITE);
        // Set background to transparent so that only the icon is visible
        etichetta.setOpaque(false);
        pulsante.setOpaque(false);

        pulsante.setLayout(new BorderLayout());
        pulsante.add(etichetta, BorderLayout.CENTER);
        pulsante.setIcon(new ImageIcon(iconsPath + "PulsanteBaseNormale.png"));
        pulsante.setActionCommand(actionCommand);
        pulsante.setPreferredSize(new Dimension(120, 55));
        pulsante.setMinimumSize(new Dimension(120, 55));
        pulsante.setBorderPainted(false);

        pulsante.setFocusable(active);
        pulsante.setEnabled(active);
        pulsante.setContentAreaFilled(visible);
        pulsante.setVisible(visible);

        pulsante.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent me)
            {
                pulsante.setIcon(new ImageIcon(iconsPath + "PulsanteBaseSelected.png"));
            }

            @Override
            public void mouseExited(MouseEvent me)
            {
                pulsante.setIcon(new ImageIcon(iconsPath + "PulsanteBaseNormale.png"));
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                pulsante.setIcon(new ImageIcon(iconsPath + "PulsanteBasePressed.png"));
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                pulsante.setIcon(new ImageIcon(iconsPath + "PulsanteBaseSelected.png"));
            }
        });
        return pulsante;
    }
    
    //Inizio del "primo form"***************************************************
    private class PrimoPannello extends JPanel implements ActionListener
    {        
        private JButton pulsanteImpostazioni;
        private JButton pulsanteCrediti;
        private JButton pulsanteInfo;
        
        public PrimoPannello()
        {
            this.setLayout(new BorderLayout());
            this.setBackground(Color.WHITE);
            
            JPanel pannelloTitolo = new JPanel();
            JPanel pannelloPlay = new JPanel();
            JPanel pannelloPulsanti = new JPanel();
            
            //title
            JLabel title = new JLabel("Up's new BattleShip");
            Font font = new Font("Serif", Font.PLAIN, 30);
            title.setForeground(Color.BLACK);
            title.setFont(font);
            Dimension dTitle = new Dimension(title.getPreferredSize().width, 100);
            title.setPreferredSize(dTitle);
            pannelloTitolo.add(title);
            pannelloTitolo.setBackground(Color.LIGHT_GRAY);
            title.setBackground(Color.LIGHT_GRAY);
            this.add(pannelloTitolo, BorderLayout.NORTH);
            
            //playButton
            PlayButton pb = new PlayButton();
            pannelloPlay.setLayout(new GridBagLayout());
            pannelloPlay.setBackground(Color.WHITE);
            JLabel etichettaImmagine = new JLabel();
            etichettaImmagine.setIcon(new ImageIcon(iconsPath + "NaveSfondo.jpg"));
            etichettaImmagine.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            pb.setActionCommand("play");
            pannelloPlay.add(pb, gbc);
            pb.addActionListener(this);
            this.add(pannelloPlay, BorderLayout.CENTER);
            
            //pulsante impostazioni e crediti
            pulsanteImpostazioni = factoryPulsanteBase("Impostazioni", "impostazioni", true, true);
            pulsanteCrediti = factoryPulsanteBase("Crediti", "crediti", true, true);
            pulsanteInfo = factoryPulsanteBase("Info", "info", true, true);
            
            pannelloPulsanti.setLayout(new FlowLayout());
            pulsanteImpostazioni.addActionListener(this);
            pulsanteCrediti.addActionListener(this);

            pannelloPulsanti.add(pulsanteImpostazioni);
            pannelloPulsanti.add(pulsanteCrediti);
            pannelloPulsanti.add(pulsanteInfo);
            pannelloPulsanti.setBackground(Color.LIGHT_GRAY);
            this.add(pannelloPulsanti, BorderLayout.SOUTH);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getActionCommand().equals("play"))
            {
                messaggio = qualePannello.PANNELLO_MENU;
                updateFrame(this);
            }
        }
        
        private class PlayButton extends JButton implements MouseListener
        {
            public PlayButton()
            {
                this.setFocusPainted(false);
                this.setBorderPainted(false);
                this.setContentAreaFilled(false);
                ImageIcon playbuttonSmall = new ImageIcon(iconsPath + "PlaybuttonSmall2.png");
                Dimension dimensione = new Dimension(49, 63);
                this.setPreferredSize(dimensione);
                this.setIcon(playbuttonSmall);
                this.setBackground(Color.white);
                this.addMouseListener(this);
            }

            @Override
            public void mouseClicked(MouseEvent me)//fa giá tutto actionPerformed di PrimoPannello
            {
            }

            @Override
            public void mousePressed(MouseEvent me)
            {
                Dimension dimensione = new Dimension(205, 284);
                this.setPreferredSize(dimensione);
                ImageIcon playButtonBig = new ImageIcon(iconsPath + "PlaybuttonBigPressed3.png");
                this.setIcon(playButtonBig);
            }

            @Override
            public void mouseReleased(MouseEvent me)
            {
                Dimension dimensione = new Dimension(205, 284);
                this.setPreferredSize(dimensione);
                ImageIcon playButtonBig = new ImageIcon(iconsPath + "PlaybuttonBigNormal3.png");
                this.setIcon(playButtonBig);
            }

            @Override
            public void mouseEntered(MouseEvent me)
            {
                Dimension dimensione = new Dimension(205, 284);
                this.setPreferredSize(dimensione);
                ImageIcon playButtonBig = new ImageIcon(iconsPath + "PlaybuttonBigNormal3.png");
                this.setIcon(playButtonBig);
            }

            @Override
            public void mouseExited(MouseEvent me)
            {
                Dimension dimensione = new Dimension(49, 63);
                this.setPreferredSize(dimensione);
                ImageIcon playbuttonSmall = new ImageIcon(iconsPath + "PlaybuttonSmall2.png");
                this.setIcon(playbuttonSmall);
            }
        }
    }
    //fine del "primo form" ****************************************************
    
    //inizio del "form menu" ***************************************************
    private class PannelloMenu extends JPanel implements ActionListener
    {
        JLabel vsComputer;
        JLabel vsPlayer;
        JLabel editor;
        PulsanteImmagine pComputer;
        PulsanteImmagine pGiocatore;
        PulsanteImmagine pEditor;
        
        //da inserire 3 JLabel cosí che quando il mouse passa sopra un pulsante / pannello appaia la JLabel con il titolo del pulsante, ad esempio se passa sopra pComputer appare la JLabel con scritto "vs Computer" o qualcosa del genere
        public PannelloMenu()
        {
            this.setBackground(Color.WHITE);
            this.setLayout(new BorderLayout());
            JPanel pannelloComponenti = new JPanel();
            pannelloComponenti.setLayout(new GridBagLayout());
            pannelloComponenti.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            
            JPanel pannelloComputer = new JPanel();
            pannelloComputer.setBackground(Color.WHITE);
            JPanel pannelloGiocatore = new JPanel();
            pannelloGiocatore.setBackground(Color.WHITE);
            JPanel pannelloEditor = new JPanel();
            pannelloEditor.setBackground(Color.WHITE);
            
            JPanel pannelloEtichettaComputer = new JPanel();
            pannelloEtichettaComputer.setBackground(Color.WHITE);
            JPanel pannelloEtichettaGiocatore = new JPanel();
            pannelloEtichettaGiocatore.setBackground(Color.WHITE);
            JPanel pannelloEtichettaEditor = new JPanel();
            pannelloEtichettaEditor.setBackground(Color.WHITE);
            
            pannelloEtichettaComputer.setLayout(new FlowLayout());
            pannelloEtichettaGiocatore.setLayout(new FlowLayout());
            pannelloEtichettaEditor.setLayout(new FlowLayout());
            
            pComputer = new PulsanteImmagine(1);
            pGiocatore = new PulsanteImmagine(2);
            pEditor = new PulsanteImmagine(3);
            
            vsComputer = new JLabel("");
            vsPlayer = new JLabel("");
            editor = new JLabel("Editor navi", JLabel.CENTER);
            Font font = new Font("Serif", Font.PLAIN, 30);
            Font font2 = new Font("Serif", Font.PLAIN, 28);
            
            vsComputer.setForeground(Color.BLACK);
            vsComputer.setFont(font);
            vsPlayer.setForeground(Color.BLACK);
            vsPlayer.setFont(font);
            editor.setForeground(Color.BLACK);
            editor.setFont(font2);
            
            vsComputer.setMinimumSize(editor.getPreferredSize());
            vsPlayer.setMinimumSize(editor.getPreferredSize());
            editor.setMinimumSize(editor.getPreferredSize());
            
            pannelloEtichettaComputer.add(vsComputer);
            pannelloEtichettaGiocatore.add(vsPlayer);
            pannelloEtichettaEditor.add(editor);
            
            pannelloEtichettaComputer.setPreferredSize(pannelloEtichettaEditor.getPreferredSize());
            pannelloEtichettaComputer.setMinimumSize(pannelloEtichettaEditor.getPreferredSize());
            pannelloEtichettaGiocatore.setPreferredSize(pannelloEtichettaEditor.getPreferredSize());
            pannelloEtichettaGiocatore.setMinimumSize(pannelloEtichettaEditor.getPreferredSize());
            pannelloEtichettaEditor.setMinimumSize(pannelloEtichettaEditor.getPreferredSize());
            
            editor.setText("");
            
            //pComputer.setPreferredSize(dStandard);
            pComputer.setActionCommand("computer");
            pComputer.addActionListener(this);
            //pGiocatore.setPreferredSize(dStandard);
            pGiocatore.setActionCommand("1vs1");
            pGiocatore.addActionListener(this);
            //pEditor.setPreferredSize(dStandard);
            pEditor.setActionCommand("editor");
            pEditor.addActionListener(this);
            
            pannelloComputer.add(pComputer);
            pannelloGiocatore.add(pGiocatore);
            pannelloEditor.add(pEditor);
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            pannelloComponenti.add(pannelloEtichettaComputer, gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 0;
            pannelloComponenti.add(pannelloEtichettaGiocatore, gbc);
            
            gbc.gridx = 2;
            gbc.gridy = 0;
            pannelloComponenti.add(pannelloEtichettaEditor, gbc);
            
            gbc.gridx = 0;
            gbc.gridy = 1;
            pannelloComponenti.add(pannelloComputer, gbc);
            
            gbc.gridx = 1;
            gbc.gridy = 1;
            pannelloComponenti.add(pannelloGiocatore, gbc);
            
            gbc.gridx = 2;
            gbc.gridy = 1;
            pannelloComponenti.add(pannelloEditor, gbc);
            
            this.add(pannelloComponenti, BorderLayout.CENTER);
        }
        
        private class PulsanteImmagine extends JButton implements MouseListener
        {
            private int immagine;
            
            public PulsanteImmagine(int immagine)
            {
                this.immagine = immagine;
                this.setFocusPainted(false);
                this.setBorderPainted(false);
                this.setContentAreaFilled(false);
                Dimension dimensione = null;
                switch(immagine)
                {
                    case 1:
                        ImageIcon pulsanteComputer = new ImageIcon(iconsPath + "IconaComputer.png");
                        dimensione = new Dimension(313, 263);
                        this.setIcon(pulsanteComputer);
                        break;
                    case 2:
                        ImageIcon pulsanteGiocatore = new ImageIcon(iconsPath + "IconaPersona.png");
                        dimensione = new Dimension(200, 334);
                        this.setIcon(pulsanteGiocatore);
                        break;
                    case 3:
                        ImageIcon pulsanteEditor = new ImageIcon(iconsPath + "IconaEditor.png");
                        dimensione = new Dimension(302, 236);
                        this.setIcon(pulsanteEditor);
                        break;
                    default:
                        
                }
                
                this.setPreferredSize(dimensione);
                this.setMinimumSize(dimensione);
                this.addMouseListener(this);
            }

            @Override
            public void mouseClicked(MouseEvent me)
            {
                //fa tutto actionperformed di PannelloMenu
            }

            @Override
            public void mousePressed(MouseEvent me)
            {
                Dimension dimensione;
                
                switch(immagine)
                {
                    case 1:
                        ImageIcon pulsanteComputer = new ImageIcon(iconsPath + "IconaComputerPressed.png");
                        dimensione = new Dimension(303, 253);
                        this.setIcon(pulsanteComputer);
                        break;
                    case 2:
                        ImageIcon pulsanteGiocatore = new ImageIcon(iconsPath + "IconaPersonaPressed.png");
                        dimensione = new Dimension(193, 322);
                        this.setIcon(pulsanteGiocatore);
                        break;
                    case 3:
                        ImageIcon pulsanteEditor = new ImageIcon(iconsPath + "IconaEditorPressed.png");
                        dimensione = new Dimension(289, 222);
                        this.setIcon(pulsanteEditor);
                        break;
                    default:
                        
                }
            }

            @Override
            public void mouseReleased(MouseEvent me)
            {
                Dimension dimensione;
                
                switch(immagine)
                {
                    case 1:
                        ImageIcon pulsanteComputer = new ImageIcon(iconsPath + "IconaComputer.png");
                        dimensione = new Dimension(313, 263);
                        this.setIcon(pulsanteComputer);
                        break;
                    case 2:
                        ImageIcon pulsanteGiocatore = new ImageIcon(iconsPath + "IconaPersona.png");
                        dimensione = new Dimension(200, 334);
                        this.setIcon(pulsanteGiocatore);
                        break;
                    case 3:
                        ImageIcon pulsanteEditor = new ImageIcon(iconsPath + "IconaEditor.png");
                        dimensione = new Dimension(302, 236);
                        this.setIcon(pulsanteEditor);
                        break;
                    default:
                }
            }

            @Override
            public void mouseEntered(MouseEvent me)
            {
                if(me.getComponent().equals(pComputer))
                {
                    vsComputer.setText("AI");
                }
                else if(me.getComponent().equals(pGiocatore))
                {
                    vsPlayer.setText("1 vs 1");
                }
                else if(me.getComponent().equals(pEditor))
                {
                    editor.setText("Editor navi");
                }
            }

            @Override
            public void mouseExited(MouseEvent me)
            {
                this.mouseReleased(me);
                if(me.getComponent().equals(pComputer))
                {
                    vsComputer.setText("");
                }
                else if(me.getComponent().equals(pGiocatore))
                {
                    vsPlayer.setText("");
                }
                else if(me.getComponent().equals(pEditor))
                {
                    editor.setText("");
                }
            }
            
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getActionCommand().equals("computer"))
            {
                messaggio = qualePannello.PANNELLO_IMPOSTAZIONIAI;
                updateFrame(this);
            }
            else if(e.getActionCommand().equals("1vs1"))
            {
                messaggio = qualePannello.PANNELLO_IMPOSTAZIONIPL;
                updateFrame(this);
            }
            else if(e.getActionCommand().equals("editor"))
            {
                messaggio = qualePannello.PANNELLO_EDITOR;
                updateFrame(this);
            }
        }
    }
    //fine del "form menu"******************************************************
    
    //inizio PannelloEditor*****************************************************
    private class PannelloEditor extends JPanel implements ActionListener, KeyListener
    {
        private JButton pOk;
        private JTextField fieldNomeNave, fieldNomePacchetto;
        private JFrame finestraInformazioniEditor;
        private JFrame finestraPacchetto;
        private JFrame frameSalva;
        private JButton pulsantiEditor[][];
        private JLabel naveOk;
        private JButton accettaNave;
        private JButton salvaPacchetto;
        private JButton confermaSalva;
        private JButton scarta;
        JButton visualizzaPacchetto;
        JButton pInformazioniEditor;
        boolean[][] dimensione = new boolean[10][10];
        private int contatoreNavi = 0;
        private PannelloNave[] pannelliNave;
        private JPanel disegnoNavi;
        
        public PannelloEditor()
        {
            //finestra del "regolamento del'editor"
            finestraInformazioniEditor = new JFrame("Informazioni sull'editor");
            JPanel pannelloInfo = new JPanel();
            finestraInformazioniEditor.setLayout(new BorderLayout());
            
            pannelloInfo.setLayout(new BorderLayout());
            pOk = factoryPulsanteBase("Ok", "ok", true, true);
            pOk.addActionListener(this);

            JTextArea info = new JTextArea(26, 50);
            info.setEditable(false);
            info.setLineWrap(true);
            info.setBorder(new EmptyBorder(10, 10, 10, 10));
            info.setText("Buongiorno utente, come ben sai, questa non è una battaglia navale come altre, per render " +
                            "il gioco più creativo e avvincente, si è pensato a una nuova meccanica: l'utente può " +
                            "disegnare la forma delle proprie navi. Ovviamente ci sono delle regole riguardo alla " +
                            "loro forma, alla quantità e all'uso di un pacchetto di navi. Un pacchetto di navi è composto da " +
                            "6 navi, le quali non possono avere un'area maggiore di 16 caselle, perché se no ci " +
                            "sarebbero più pezzi di nave che caselle di gioco. Per \"area\" della nave si intende " +
                            "lunghezza_massima * altezza_massima quindi anche delle caselle \"vuote\" vengono contate, perché la nave è come se fosse un rettangolo. " +
                            "Ogni pezzo della nave (con pezzo si intende " +
                            "ogni casella) ha una propria immagine rappresentativa, la feature della personalizzazione " +
                            "dell'immagine sarà disponibile in futuro, per ora c'è un'immagine di default. Ovviamente i " +
                            "giocatori utilizzeranno in ogni partita lo stesso pacchetto di navi personalizzato, e " +
                            "prima di iniziare, verrà mostrato a schermo. " +
                            "Se avete bisogno di rivedere questo messaggio in futuro, premete il pulsantino blu con " +
                            "la \"i\" in mezzo.");
            info.setForeground(Color.BLACK);
            Font font = new Font("Serif", Font.BOLD, 20);
            info.setFont(font);
            JScrollPane barra = new JScrollPane(info);
            pannelloInfo.add(barra);
            barra.setVisible(true);
            pannelloInfo.setPreferredSize(new Dimension(info.getPreferredSize().width + 5, info.getPreferredSize().height));
            finestraInformazioniEditor.add(pannelloInfo);
            finestraInformazioniEditor.setSize(1080, 720);
            JPanel pannelloPulsante = new JPanel();
            pannelloPulsante.add(pOk);
            finestraInformazioniEditor.add(pannelloPulsante, BorderLayout.SOUTH);
            finestraInformazioniEditor.setVisible(true);
            //fine finestra "regolamento dell'editor"
            
            //inizio finestra editor
            
            //conferma nave e etichetta dinamica
            this.setLayout(new GridBagLayout());
            this.setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.insets = new Insets(10, 10, 10, 10);
            gbc.insets = new Insets(10, 10, 10, 10);
                               
            naveOk = new JLabel("La nave non è accettabile", JLabel.CENTER);
            naveOk.setPreferredSize(new Dimension(160, 50));//serve per non far muovere tutti i componenti del pannello quando la scritta si cambia, ma solo quelli del pannelloNaveOk
            naveOk.setText("Disegnare una nave");
            JPanel pannelloNaveOk = new JPanel();
            pannelloNaveOk.setBackground(Color.WHITE);
            pannelloNaveOk.add(naveOk);
            accettaNave = factoryPulsanteBase("Conferma", "okNave", false, true);
            accettaNave.addActionListener(this);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.fill = GridBagConstraints.VERTICAL;
            gbc.gridheight = 1;
            pannelloNaveOk.setMinimumSize(new Dimension(pannelloNaveOk.getPreferredSize().width, 70));
            pannelloNaveOk.add(accettaNave, BorderLayout.SOUTH);
            this.add(pannelloNaveOk, gbc);
            
            gbc.gridheight = 1;
            
            //nome della nave
            JPanel pannelloNomeNave = new JPanel();
            pannelloNomeNave.setBackground(Color.WHITE);
            fieldNomeNave = new JTextField("Nome nave", 20);
            fieldNomeNave.setActionCommand("nome");
            fieldNomeNave.addKeyListener(this);
            pannelloNomeNave.add(fieldNomeNave, BorderLayout.CENTER);
            pannelloNomeNave.setVisible(true);
            pannelloNomeNave.setPreferredSize(fieldNomeNave.getPreferredSize());
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            this.add(pannelloNomeNave, gbc);
            
            gbc.gridwidth = 1;
            
            //pacchetto navi mostrato, idea lo si mostra in un'altra finestra apribile
            JPanel visualizzaSalva = new JPanel();
            JPanel panelVisualizzaPacchetto = new JPanel();
            JPanel panelInformazioniEditor = new JPanel();
            visualizzaSalva.setBackground(Color.WHITE);
            panelVisualizzaPacchetto.setBackground(Color.WHITE);
            panelInformazioniEditor.setBackground(Color.WHITE);
            
            visualizzaSalva.setLayout(new GridBagLayout());
            visualizzaPacchetto = factoryPulsanteBase("Visualizza", "visualizza", true, true);
            visualizzaPacchetto.addActionListener(this);
            panelVisualizzaPacchetto.add(visualizzaPacchetto);

            pInformazioniEditor = factoryPulsanteBase("Info", "i", true, true);
            pInformazioniEditor.addActionListener(this);
            panelInformazioniEditor.add(pInformazioniEditor);

            salvaPacchetto = factoryPulsanteBase("Salva", "salva", true, false);
            salvaPacchetto.addActionListener(this);
            
            gbc2.gridx = 0;
            gbc2.gridy = 0;
            visualizzaSalva.add(panelVisualizzaPacchetto, gbc2);
            gbc2.gridx = 0;
            gbc2.gridy = 1;
            visualizzaSalva.add(panelInformazioniEditor, gbc2);
            gbc2.gridx = 0;
            gbc2.gridy = 2;
            visualizzaSalva.add(salvaPacchetto, gbc2);
            gbc.gridx = 0;
            gbc.gridy = 2;
            this.add(visualizzaSalva, gbc);
            
            //pannello con il pacchetto navi renderizzato a run time
            finestraPacchetto = new JFrame("Visualizza pacchetto");
            finestraPacchetto.setSize(1080, 720);
           
            disegnoNavi = new JPanel();
            disegnoNavi.setLayout(new GridLayout(2, 3));
            pannelliNave = new PannelloNave[6];
            for(int i = 0; i < 6; i++)
            {
                pannelliNave[i] = new PannelloNave();
                pannelliNave[i].setPreferredSize(new Dimension(360, 360));
                pannelliNave[i].setLayout(new BorderLayout());
            }
            finestraPacchetto.add(disegnoNavi);
            
            //pannello tavola
            JPanel pannelloTavola = new JPanel();
            pannelloTavola.setBackground(Color.WHITE);
            BufferedImage tavolaDaMostrare = null;
            
            try
            {
                tavolaDaMostrare = ImageIO.read(new File(iconsPath + "tavolaDaMostrare.png"));
            }
            catch(IOException e)
            {
                System.exit(0);
            }
            
            JLabel picLabel = new JLabel(new ImageIcon(tavolaDaMostrare));
            picLabel.setLayout(null);
            pulsantiEditor = new JButton[10][10];
            
            for(int i = 0; i < 10; i++)
            {
                for(int j = 0; j < 10; j++)
                {
                    pulsantiEditor[i][j] = new JButton()
                    {
                        private boolean isSelected = false;
                        
                        public void setSelected(boolean valore)
                        {
                            isSelected = valore;
                        }
                        
                        public void paintComponent(Graphics g)
                        {
                            if(this.isSelected)
                            {
                                super.paintComponent(g);
                                g.fillOval(15, 15, 25, 25);
                            }
                        }
                    };
                    pulsantiEditor[i][j].setActionCommand(i + " " + j);
                    pulsantiEditor[i][j].setBackground(new Color(106 - 10 * i, 192 - 12 * i, 255 - 13 * i));
                    pulsantiEditor[i][j].setBounds(39 + 61 * j, 39 + (61 * i) + 1, 55, 54);
                    pulsantiEditor[i][j].addActionListener(this);
                    pulsantiEditor[i][j].setContentAreaFilled(false);
                    picLabel.add(pulsantiEditor[i][j]);
                }
            }
            
            pannelloTavola.add(picLabel);
            
            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.gridheight = 4;
            gbc.gridwidth = 3;
            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.LINE_END;
            this.add(pannelloTavola, gbc);
        }
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            if(e.getActionCommand().equals("ok"))
                finestraInformazioniEditor.dispose();
            else if(e.getActionCommand().equals("okNave"))
            {
                for(int i = 0; i < 10; i++)
                {
                    for(int j = 0; j < 10; j++)
                    {
                        pulsantiEditor[i][j].setSelected(false);
                        pulsantiEditor[i][j].repaint();
                    }
                }
                
                PACCHETTO_NAVI.creaNaveNonLineare(dimensione, fieldNomeNave.getText());
                pannelliNave[contatoreNavi].addNave(contatoreNavi, 1, 1);
                pannelliNave[contatoreNavi].setBackground(Color.WHITE);
                disegnoNavi.add(pannelliNave[contatoreNavi]);
                contatoreNavi++;
                
                if(contatoreNavi == 6)
                {
                    accettaNave.setVisible(false);
                    naveOk.setVisible(false);
                    fieldNomeNave.setVisible(false);
                    salvaPacchetto.setVisible(true);
                }
                
                dimensione = new boolean[10][10];
                naveOk.setText("Disegnare una nave");
                fieldNomeNave.setText("Nome nuova nave");
                accettaNave.setEnabled(false);
            }
            else if(e.getActionCommand().equals("visualizza"))
            {
                finestraPacchetto.setVisible(true);
            }
            else if(e.getActionCommand().equals("salva"))
            {
                frameSalva = new JFrame("Salva pacchetto");
                frameSalva.setSize(450, 300);
                frameSalva.setBackground(Color.WHITE);
                frameSalva.setLayout(new BorderLayout());
                JPanel pannelloTesto = new JPanel();
                pannelloTesto.setBackground(Color.WHITE);
                pannelloTesto.setLayout(new FlowLayout());
                fieldNomePacchetto = new JTextField("Inserire nome cartella", 32);
                fieldNomePacchetto.addKeyListener(this);
                pannelloTesto.add(fieldNomePacchetto);
                JPanel pannelloPulsanti = new JPanel();
                pannelloPulsanti.setBackground(Color.WHITE);
                pannelloPulsanti.setLayout(new FlowLayout());
                
                confermaSalva = factoryPulsanteBase("Conferma", "salva2", true, true);
                confermaSalva.addActionListener(this);

                scarta = factoryPulsanteBase("Scarta", "scarta", true, true);
                scarta.addActionListener(this);

                pannelloPulsanti.add(confermaSalva);
                pannelloPulsanti.add(scarta);
                frameSalva.add(pannelloTesto, BorderLayout.CENTER);
                frameSalva.add(pannelloPulsanti, BorderLayout.SOUTH);
                frameSalva.setVisible(true);
            }
            else if(e.getActionCommand().equals("salva2"))
            {
                PACCHETTO_NAVI.salvaFileBinario(fieldNomePacchetto.getText());
                frameSalva.dispose();
                for(int i = 0; i < QUANTE_NAVI; i++)
                {
                    pannelliNave[i] = new PannelloNave();
                }
                PACCHETTO_NAVI.inizializzaPacchetto();
                
                messaggio = qualePannello.PANNELLO_MENU;
                updateFrame(this);
            }
            else if(e.getActionCommand().equals("scarta"))
            {
                frameSalva.dispose();
                for(int i = 0; i < QUANTE_NAVI; i++)
                {
                    pannelliNave[i] = new PannelloNave();
                }
                PACCHETTO_NAVI.inizializzaPacchetto();
                
                messaggio = qualePannello.PANNELLO_MENU;
                updateFrame(this);
            }
            else if(e.getActionCommand().equals("i"))
            {
                finestraInformazioniEditor.setVisible(true);
            }
            else
            {
                for(int i = 0; i < 10; i++)
                {
                    for(int j = 0; j < 10; j++)
                    {
                        if(e.getActionCommand().equals(pulsantiEditor[i][j].getActionCommand()))
                        {
                            dimensione[i][j] = !dimensione[i][j];
                            
                            if(BattagliaNavale.controllaStrutturaNave(dimensione))
                            {
                                naveOk.setText("La nave è accettabile");
                                accettaNave.setEnabled(true);
                            }
                            else
                            {
                                naveOk.setText("La nave non è accettabile");
                                accettaNave.setEnabled(false);
                            }
                            
                            if(dimensione[i][j])
                            {
                                pulsantiEditor[i][j].setSelected(true);
                                pulsantiEditor[i][j].repaint();
                            }
                            else
                            {
                                pulsantiEditor[i][j].setSelected(false);
                                pulsantiEditor[i][j].repaint();
                            }
                            break;
                        }
                    }
                }
            }            
        }
        
        @Override
            public void keyTyped(KeyEvent e)
            {
                char carattereInput = e.getKeyChar();
                if((carattereInput == '/') || (carattereInput == '\\') || (carattereInput == ':') || (carattereInput == '*') || (carattereInput == '?') || (carattereInput == '"') || (carattereInput == '<') || (carattereInput == '>') || (carattereInput == '|'))
                    e.consume();
            }

            @Override
            public void keyPressed(KeyEvent e){}

            @Override
            public void keyReleased(KeyEvent e){}
    }
    
    private class PannelloNave extends JPanel//La classe PannelloNave è un JPanel su cui sopra viene printata la forma ed il nome di una nave, nella costruttore di PannelloEditor si è costruito un array di 6 elementi PannelloNave, uno per ogni nave
    {
        private boolean[][] dimensione;
        private Color colore;
        
        public PannelloNave()
        {}

        public void addNave(int nave, int qualePacchetto, int orientamento)//qualePacchetto = 1 -> PACCHETTO_NAVI, qualePacchetto = 2 -> PACCHETTOG1, qualePacchetto = 3 -> PACCHETTOG2
        {
            PacchettoNavi[] pacchetti = {PACCHETTO_NAVI, PACCHETTOG1, PACCHETTOG2};

            //this.setPreferredSize(new Dimension(360, 240));
            switch(nave)
            {
                case 0:
                    Nave naveDimensione = pacchetti[qualePacchetto - 1].getNave(0);
                    naveDimensione.ruotaNave(orientamento);
                    dimensione = naveDimensione.getDimensione();     
                    break;
                case 1:
                    Nave naveDimensione1 = pacchetti[qualePacchetto - 1].getNave(1);
                    naveDimensione1.ruotaNave(orientamento);
                    dimensione = naveDimensione1.getDimensione(); 
                    break;
                case 2:
                    Nave naveDimensione2 = pacchetti[qualePacchetto - 1].getNave(2);
                    naveDimensione2.ruotaNave(orientamento);
                    dimensione = naveDimensione2.getDimensione(); 
                    break;
                case 3:
                    Nave naveDimensione3 = pacchetti[qualePacchetto - 1].getNave(3);
                    naveDimensione3.ruotaNave(orientamento);
                    dimensione = naveDimensione3.getDimensione(); 
                    break;
                case 4:
                    Nave naveDimensione4 = pacchetti[qualePacchetto - 1].getNave(4);
                    naveDimensione4.ruotaNave(orientamento);
                    dimensione = naveDimensione4.getDimensione(); 
                    break;
                case 5:
                    Nave naveDimensione5 = pacchetti[qualePacchetto - 1].getNave(5);
                    naveDimensione5.ruotaNave(orientamento);
                    dimensione = naveDimensione5.getDimensione(); 
                    break;
            }
            this.add(new JLabel(pacchetti[qualePacchetto - 1].getNave(nave).getNome()), BorderLayout.NORTH);
            colore = pacchetti[qualePacchetto - 1].getNave(nave).getColoreNave();
            this.repaint();
    }

        public void paintComponent(Graphics g)
        {
            g.setColor(colore);
            super.paintComponent(g);
            int dimensioneCerchio = this.getPreferredSize().width / 15;
                    
            for(int i = 0; i < dimensione.length; i++)
            {
                for(int j = 0; j < dimensione[i].length; j++)
                {
                    if(dimensione[i][j])
                        g.fillOval(dimensioneCerchio * j + 10, dimensioneCerchio * i + 50, dimensioneCerchio, dimensioneCerchio);
                }
            }
        }

        public void setDimensione(boolean[][] dimensione)
        {
            this.dimensione = dimensione;
        }
    }
    //fine PannelloEditor*******************************************************
    
    private class PannelloGioco extends JPanel
    {
        private boolean aiPlayer;
        private int qualeGiocatore;
        private int difficoltaAI = - 1;
        private int qualePacchettoSelezionato = -1;//indica quale dei pacchetti, sottoforma di numero é stato selezionato, rappresenta l'indice dell'array nomiPacchettiNave
        private chiGiocaPrimo primo;//é una enum, come per difficoltaAI e qualePacchettoSelezionato esiste un valore iniziale, ovvero VALORE_INIZIALE
        private String nomeGiocatore1;
        private String nomeGiocatore2;
        private int vincitore;
        private Partita partitaG1;
        private Partita partitaG2;
        
        public PannelloGioco()
        {
            this.setLayout(new BorderLayout());
            
            switch(messaggio)
            {
                case PANNELLO_IMPOSTAZIONIAI:
                    aiPlayer = true;
                    PannelloImpostazioni impostazioniAI = new PannelloImpostazioni();
                    this.add(impostazioniAI, BorderLayout.CENTER);
                    break;
                case PANNELLO_IMPOSTAZIONIPL:
                    aiPlayer = false;
                    PannelloImpostazioni impostazioniPL = new PannelloImpostazioni();
                    this.add(impostazioniPL, BorderLayout.CENTER);
                    break;
            }
        }
        
        public void updatePannelloGioco(JPanel pannello)
        {
            pannello.setVisible(false);
            if(messaggio == qualePannello.PANNELLO_POSIZIONA_NAVI1)
            {
                if(!aiPlayer)//se si stesse giocando contro il computer comunque deve posizionare le navi prima il giocatore
                {
                    switch(primo)
                    {
                        case G1:
                            qualeGiocatore = 1;
                            break;
                        case G2:
                            qualeGiocatore = 2;
                            break;
                        case CASUALE:
                            qualeGiocatore = (int)(Math.random() * 2) + 1;
                            if(qualeGiocatore == 1)
                                primo = chiGiocaPrimo.G1;
                            else
                                primo = chiGiocaPrimo.G2;
                            break;
                    }
                }
                else
                {
                    qualeGiocatore = 1;
                }
                PannelloPosizionaNavi posizionaNavi1 = new PannelloPosizionaNavi();
                this.add(posizionaNavi1, BorderLayout.CENTER);
            }
            else if(messaggio == qualePannello.PANNELLO_POSIZIONA_NAVI2)
            {
                if(qualeGiocatore == 1)
                    qualeGiocatore = 2;
                else
                    qualeGiocatore = 1;
                
                PannelloPosizionaNavi posizionaNavi2 = new PannelloPosizionaNavi();
                this.add(posizionaNavi2, BorderLayout.CENTER);
            }
            else if(messaggio == qualePannello.PANNELLO_PARTITA)
            {
                if(qualeGiocatore == 1)
                {
                    qualeGiocatore = 2;//l'ultimo a giocare é stato il G1 quindi colpisce il G2
                    if(partitaG2 == null)
                    {
                        if(aiPlayer)
                        {
                            partitaG2 = new Partita(true);
                            partitaG2.AIcolpisci();
                        }
                        else
                        {
                            partitaG2 = new Partita(false);
                        }
                        this.add(partitaG2, BorderLayout.CENTER);
                    }
                    else
                    {
                        partitaG2.cambiaStatoPulsanti(true);
                        if(aiPlayer)
                            partitaG2.AIcolpisci();
                        partitaG2.setVisible(true);
                    }
                }
                else
                {
                    qualeGiocatore = 1;
                    if(partitaG1 == null)
                    {
                        partitaG1 = new Partita(false);//il G1 non sará mai un AI
                        this.add(partitaG1, BorderLayout.CENTER);
                    }
                    else
                    {
                        partitaG1.cambiaStatoPulsanti(true);
                        partitaG1.setVisible(true);
                    }
                }
                
            }
            else if(messaggio == qualePannello.PANNELLO_VITTORIA)
            {
                PannelloVittoria vittoria = new PannelloVittoria();
                this.add(vittoria, BorderLayout.CENTER);
            }
            else if(messaggio == qualePannello.PRIMO_PANNELLO)
            {
                updateFrame(this);
            }
        }
        
        private void cambiaQualeGiocatore()
        {
            if(qualeGiocatore == 1)
                qualeGiocatore = 2;
            else
                qualeGiocatore = 1;
        }
        
        //inizio PannelloImpostazioni***********************************************
        private class PannelloImpostazioni extends JPanel implements ActionListener, MouseListener
        {
            Timer tm = new Timer(250, this);

            private int contatorePulsante = 0;
            private JButton dif1;
            private JButton dif2;
            private JButton dif3;
            private JPanel difficolta;
            private JButton pulsantePrimo;
            private JButton pulsanteSecondo;
            private JButton pulsanteCasuale;
            private JButton browsePacchetto;
            private JFrame frameBrowsePacchetto;
            private String[] nomiPacchettiNave;
            private JPanel[] pannelloNomePacchetti;
            private LabelCartella[] etichettaNomePacchetti;
            private JButton accettaPacchetto;
            private JButton pulsanteConfermaImpostazioni;

            public PannelloImpostazioni()
            {
                this.setBackground(Color.WHITE);
                nomeGiocatore1 = "G1";
                nomeGiocatore2 = "G2";
                JPanel pannelloComponenti = new JPanel();//è il pannello che contiene tutti i componenti, cosicchè essi non si spargano nel nulla quando la finestra si resiza
                pannelloComponenti.setBackground(Color.WHITE);
                pannelloComponenti.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);

                //inizio titolo
                JLabel titolo = new JLabel("Impostazioni");
                Font fontTitolo = new Font("NSimSun", Font.PLAIN, 22);
                titolo.setFont(fontTitolo);
                gbc.gridx = 0;
                gbc.gridy = 0;
                pannelloComponenti.add(titolo, gbc);
                //fine titolo

                //Inizio sel difficolta
                if(aiPlayer)
                {
                    difficolta = new JPanel();
                    difficolta.setBackground(Color.WHITE);
                    difficolta.setLayout(new GridBagLayout());
                    GridBagConstraints gbc2 = new GridBagConstraints();

                    JLabel pannelloSelDifficolta = new JLabel();
                    pannelloSelDifficolta.setLayout(new BorderLayout());
                    pannelloSelDifficolta.setIcon(new ImageIcon(iconsPath + "PannelloPulsantiPrimo.png"));
                    JLabel selDifficolta = new JLabel("Seleziona difficoltà", JLabel.CENTER);
                    selDifficolta.setForeground(Color.WHITE);
                    pannelloSelDifficolta.add(selDifficolta, BorderLayout.CENTER);
                    difficolta.addMouseListener(this);
                    dif1 = factoryPulsanteBase("Liv. 1", "1", true, false);
                    dif1.addMouseListener(this);
                    dif1.addActionListener(this);

                    dif2 = factoryPulsanteBase("Liv. 2", "2", true, false);
                    dif2.addMouseListener(this);
                    dif2.addActionListener(this);

                    dif3 = factoryPulsanteBase("Liv. 3", "3", true, false);
                    dif3.addMouseListener(this);
                    dif3.addActionListener(this);

                    gbc2.insets = new Insets(5, 5, 5, 5);

                    gbc2.gridx = 0;
                    gbc2.gridy = 0;
                    difficolta.add(pannelloSelDifficolta, gbc2);

                    gbc2.gridx = 0;
                    gbc2.gridy = 1;
                    difficolta.add(dif1, gbc2);

                    gbc2.gridx = 0;
                    gbc2.gridy = 2;
                    difficolta.add(dif2, gbc2);

                    gbc2.gridx = 0;
                    gbc2.gridy = 3;
                    difficolta.add(dif3, gbc2);

                    gbc.gridx = 0;
                    gbc.gridy = 1;
                    pannelloComponenti.add(difficolta, gbc);
                    //fine sel difficolta

                    //inizio seleziona chi gioca per primo
                    JPanel pannelloPrimo = new JPanel();
                    pannelloPrimo.setBackground(Color.WHITE);
                    pannelloPrimo.setLayout(new FlowLayout());
                    JLabel contenitorePulsantiPrimo = new JLabel();
                    contenitorePulsantiPrimo.setLayout(new GridBagLayout());
                    contenitorePulsantiPrimo.setIcon(new ImageIcon(iconsPath + "PannelloPulsantiPrimo.png"));
                    contenitorePulsantiPrimo.setMaximumSize(new Dimension(268, 83));

                    pulsantePrimo = new JButton();
                    pulsantePrimo.setContentAreaFilled(false);
                    pulsantePrimo.setBorderPainted(false);
                    pulsantePrimo.setFocusable(false);

// Change the layout to GridBagLayout for proper centering
                    pulsantePrimo.setLayout(new GridBagLayout());

                    pulsantePrimo.setIcon(new ImageIcon(iconsPath + "PulsantePrimoNormale.png"));
                    pulsantePrimo.setPreferredSize(new Dimension(66, 66));

                    JLabel etichettaPrimo = new JLabel("1°", JLabel.CENTER);
                    etichettaPrimo.setForeground(Color.WHITE);

// Add the label to the button, GridBagLayout centers by default
                    pulsantePrimo.add(etichettaPrimo, new GridBagConstraints());

                    pulsantePrimo.addActionListener(this);
                    pulsantePrimo.addMouseListener(this);
                    pulsantePrimo.setActionCommand("primo");

                    pulsanteSecondo = new JButton();
                    pulsanteSecondo.setContentAreaFilled(false);
                    pulsanteSecondo.setBorderPainted(false);
                    pulsanteSecondo.setFocusable(false);

                    pulsanteSecondo.setLayout(new GridBagLayout());

                    pulsanteSecondo.setIcon(new ImageIcon(iconsPath + "PulsantePrimoNormale.png"));
                    pulsanteSecondo.setPreferredSize(new Dimension(66, 66));

                    JLabel etichettaSecondo = new JLabel("2°", JLabel.CENTER);
                    etichettaSecondo.setForeground(Color.WHITE);

                    pulsanteSecondo.add(etichettaSecondo, new GridBagConstraints());

                    pulsanteSecondo.addActionListener(this);
                    pulsanteSecondo.addMouseListener(this);
                    pulsanteSecondo.setActionCommand("secondo");

                    pulsanteCasuale = new JButton();
                    pulsanteCasuale.setContentAreaFilled(false);
                    pulsanteCasuale.setBorderPainted(false);
                    pulsanteCasuale.setFocusable(false);
                    pulsanteCasuale.setLayout(new FlowLayout());
                    pulsanteCasuale.setIcon(new ImageIcon(iconsPath + "PulsanteDadoNormale.png"));
                    pulsanteCasuale.setPreferredSize(new Dimension(42, 35));
                    pulsanteCasuale.setMinimumSize(pulsanteCasuale.getPreferredSize());
                    pulsanteCasuale.addActionListener(this);
                    pulsanteCasuale.addMouseListener(this);
                    pulsanteCasuale.setActionCommand("casuale");

                    primo = chiGiocaPrimo.VALORE_INIZIALE;

                    GridBagConstraints gbc_pulsanti_primo = new GridBagConstraints();
                    gbc_pulsanti_primo.gridx = 0;
                    gbc_pulsanti_primo.gridy = 0;

                    contenitorePulsantiPrimo.add(pulsantePrimo, new GridBagConstraints());
                    gbc_pulsanti_primo.gridx = 1;
                    contenitorePulsantiPrimo.add(pulsanteSecondo, gbc_pulsanti_primo);
                    gbc_pulsanti_primo.gridx = 2;
                    contenitorePulsantiPrimo.add(pulsanteCasuale,new GridBagConstraints());

                    pannelloPrimo.add(contenitorePulsantiPrimo, BorderLayout.CENTER);
                    gbc.gridx = 0;
                    gbc.gridy = 2;
                    pannelloComponenti.add(pannelloPrimo, gbc);
                    //fine selezione chi gioca per primo
                }

                //inizio browse pachetto navi
                JPanel pannelloBrowsePacchetto = new JPanel();
                pannelloBrowsePacchetto.setBackground(Color.WHITE);
                JLabel pannelloPulsantiBello = new JLabel();
                pannelloPulsantiBello.setIcon(new ImageIcon(iconsPath + "PannelloPulsantiPrimo.png"));
                pannelloPulsantiBello.setLayout(new GridBagLayout());
                browsePacchetto = factoryPulsanteBase("Pacchetto...", "seleziona", true, true);
                browsePacchetto.addActionListener(this);
                browsePacchetto.addMouseListener(this);
                pannelloPulsantiBello.add(browsePacchetto);
                pannelloBrowsePacchetto.add(pannelloPulsantiBello);

                if(aiPlayer)
                {
                    gbc.gridx = 0;
                    gbc.gridy = 3;
                }
                else
                {
                    gbc.gridx = 0;
                    gbc.gridy = 1;
                }
                pannelloComponenti.add(pannelloBrowsePacchetto, gbc);

                //qua inzia la finestra
                nomiPacchettiNave = PacchettoNavi.nomiPacchettiNave();
                frameBrowsePacchetto = new JFrame("Sel. PacchettoNavi");
                frameBrowsePacchetto.setVisible(false);
                frameBrowsePacchetto.setSize(350, 500);
                frameBrowsePacchetto.setLayout(new BorderLayout());
                JPanel pannelloPacchettiNave = new JPanel();
                pannelloPacchettiNave.setLayout(new GridLayout(nomiPacchettiNave.length, 1));
                pannelloNomePacchetti = new JPanel[nomiPacchettiNave.length];//************************************************************************************************
                etichettaNomePacchetti = new LabelCartella[nomiPacchettiNave.length];

                for(int i = 0; i < nomiPacchettiNave.length; i++)
                {
                    pannelloNomePacchetti[i] = new JPanel();
                    etichettaNomePacchetti[i] = new LabelCartella(nomiPacchettiNave[i], i);
                    pannelloNomePacchetti[i].setBackground(new Color(200, 200, 200));
                    etichettaNomePacchetti[i].setBackground(new Color(200, 200, 200));
                    etichettaNomePacchetti[i].addMouseListener(etichettaNomePacchetti[i]);

                    pannelloNomePacchetti[i].add(etichettaNomePacchetti[i]);
                    pannelloPacchettiNave.add(pannelloNomePacchetti[i]);
                }

                JScrollPane barra = new JScrollPane(pannelloPacchettiNave);
                frameBrowsePacchetto.add(barra, BorderLayout.CENTER);

                JPanel pannelloPulsanti = new JPanel();
                accettaPacchetto = factoryPulsanteBase("Conferma", "confermaPacchetto", false, true);
                accettaPacchetto.addMouseListener(this);
                accettaPacchetto.addActionListener(this);
                pannelloPulsanti.add(accettaPacchetto);
                frameBrowsePacchetto.add(pannelloPulsanti, BorderLayout.SOUTH);


                //inizio pulsanteConfermaImpostazioni
                JPanel pannelloConferma = new JPanel();
                pannelloConferma.setBackground(Color.WHITE);
                pulsanteConfermaImpostazioni = factoryPulsanteBase("Conferma", "confermaImp", false, true);
                pulsanteConfermaImpostazioni.addActionListener(this);
                pulsanteConfermaImpostazioni.addMouseListener(this);
                pannelloConferma.add(pulsanteConfermaImpostazioni);
                if(aiPlayer)
                {
                    gbc.gridx = 0;
                    gbc.gridy = 4;
                }
                else
                {
                    gbc.gridx = 0;
                    gbc.gridy = 2;
                }
                pannelloComponenti.add(pannelloConferma, gbc);

                this.add(pannelloComponenti);
            }

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(aiPlayer)
                {
                    if((qualePacchettoSelezionato != -1) && (difficoltaAI != -1) && (primo != chiGiocaPrimo.VALORE_INIZIALE))
                        pulsanteConfermaImpostazioni.setEnabled(true);
                    else
                        pulsanteConfermaImpostazioni.setEnabled(false);
                }
                else
                {
                    if(qualePacchettoSelezionato != -1)
                        pulsanteConfermaImpostazioni.setEnabled(true);
                }

                if(e.getActionCommand().equals("tm1"))
                {
                    if(contatorePulsante == 0)
                    {
                        dif1.setVisible(true);
                        contatorePulsante++;
                    }
                    else if(contatorePulsante == 1)
                    {
                        dif2.setVisible(true);
                        contatorePulsante++;
                    }
                    else if(contatorePulsante == 2)
                    {
                        dif3.setVisible(true);
                        tm.stop();
                    }
                }
                else if(e.getActionCommand().equals("tm2"))
                {
                    if(contatorePulsante == 0)
                    {
                        dif1.setVisible(false);
                        tm.stop();
                    }
                    else if(contatorePulsante == 1)
                    {
                        dif2.setVisible(false);
                        contatorePulsante--;
                    }
                    else if(contatorePulsante == 2)
                    {
                        dif3.setVisible(false);
                        contatorePulsante--;
                    }
                }
                else if(e.getActionCommand().equals("1"))
                {
                    difficoltaAI = 1;
                }
                else if(e.getActionCommand().equals("2"))
                {
                    difficoltaAI = 2;
                }
                else if(e.getActionCommand().equals("3"))
                {
                    difficoltaAI = 3;
                }
                else if(e.getActionCommand().equals("primo"))
                {
                    primo = chiGiocaPrimo.G1;
                }
                else if(e.getActionCommand().equals("secondo"))
                {
                    primo = chiGiocaPrimo.G2;
                }
                else if(e.getActionCommand().equals("casuale"))
                {
                    primo = chiGiocaPrimo.CASUALE;
                }
                else if(e.getActionCommand().equals("seleziona"))
                {
                    frameBrowsePacchetto.setVisible(true);
                }
                else if(e.getActionCommand().equals("confermaPacchetto"))
                {
                    if(qualePacchettoSelezionato == -1)
                        ;
                    else
                    {
                        PacchettoNavi pacchettoProvaSicurezza = new PacchettoNavi(QUANTE_NAVI);
                        if(pacchettoProvaSicurezza.caricaFileBinario(nomiPacchettiNave[qualePacchettoSelezionato]) != 0)
                        {
                            JFrame frameErrore = new JFrame("Errore");
                            frameErrore.setSize(300, 200);
                            JLabel etichettaErrore = new JLabel("Errore, riprovare con un altro pacchetto");
                            frameErrore.add(etichettaErrore, BorderLayout.CENTER);
                        }
                        else
                        {
                            PACCHETTO_NAVI.caricaFileBinario(nomiPacchettiNave[qualePacchettoSelezionato]);
                        }
                    }
                    frameBrowsePacchetto.dispose();
                }
                else if(e.getActionCommand().equals("confermaImp"))
                {
                    if(!aiPlayer)
                        primo = chiGiocaPrimo.G1;
                    messaggio = qualePannello.RESIZA;
                    updateFrame(this);
                    messaggio = qualePannello.PANNELLO_POSIZIONA_NAVI1;
                    updatePannelloGioco(this);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
                if((e.getSource().equals(pulsantePrimo)) || (e.getSource().equals(pulsanteSecondo)))
                {
                    ((JButton)e.getSource()).setIcon(new ImageIcon(iconsPath + "PulsantePrimoPressed.png"));
                }
                else if(e.getSource().equals(pulsanteCasuale))
                {
                    pulsanteCasuale.setIcon(new ImageIcon(iconsPath + "PulsanteDadoPressed.png"));
                    pulsanteCasuale.setMinimumSize(new Dimension(35, 35));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if((e.getSource().equals(pulsantePrimo)) || (e.getSource().equals(pulsanteSecondo)))
                {
                    ((JButton)e.getSource()).setIcon(new ImageIcon(iconsPath + "PulsantePrimoSelected.png"));
                }
                else if(e.getSource().equals(pulsanteCasuale))
                {
                    pulsanteCasuale.setIcon(new ImageIcon(iconsPath + "PulsanteDadoSelected.png"));
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if(e.getSource().equals(difficolta) || (e.getSource().equals(dif1)) || (e.getSource().equals(dif2)) || (e.getSource().equals(dif3)))
                {
                    tm.stop();
                    tm.setActionCommand("tm1");
                    tm.start();
                }
                else if((e.getSource().equals(pulsantePrimo)) || (e.getSource().equals(pulsanteSecondo)))
                {
                    ((JButton)e.getSource()).setIcon(new ImageIcon(iconsPath + "PulsantePrimoSelected.png"));
                }
                else if(e.getSource().equals(pulsanteCasuale))
                {
                    pulsanteCasuale.setIcon(new ImageIcon(iconsPath + "PulsanteDadoSelected.png"));
                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if(e.getSource().equals(difficolta))
                {
                    tm.stop();
                    tm.setActionCommand("tm2");
                    tm.start(); 
                }
                else if((e.getSource().equals(pulsantePrimo)) || (e.getSource().equals(pulsanteSecondo)))
                {
                    ((JButton)e.getSource()).setIcon(new ImageIcon(iconsPath + "PulsantePrimoNormale.png"));
                }
                else if(e.getSource().equals(pulsanteCasuale))
                {
                    pulsanteCasuale.setIcon(new ImageIcon(iconsPath + "PulsanteDadoNormale.png"));
                }
            }

            private class LabelCartella extends JLabel implements MouseListener
            {
                public boolean selected = false;//indica se questa é la cartella selezionata
                private int qualePacchettoRappresenta;

                public LabelCartella(String nomePacchetto, int qualePacchetto)
                {
                    super("                              " + nomePacchetto);
                    this.setPreferredSize(new Dimension(300, 40));
                    //this.setBackground(new Color(200, 200, 200));
                    this.setOpaque(true);
                    qualePacchettoRappresenta = qualePacchetto;
                }

                public void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    Image cartellaChiusa = null;
                    Image cartellaAperta = null;
                    try
                    {
                        if(selected)
                        {
                            cartellaAperta = ImageIO.read(new File(iconsPath + "CartellaAperta.png"));
                        }
                        else
                            cartellaChiusa = ImageIO.read(new File(iconsPath + "CartellaChiusa.png"));
                    }
                    catch(IOException e)
                    {
                        System.out.println(e.getMessage());
                    }
                    if(selected)
                        g.drawImage(cartellaAperta, 10, 0, null);
                    else
                        g.drawImage(cartellaChiusa, 20, 0, null);
                }

                @Override
                public void mouseClicked(MouseEvent me)
                {
                    selected = !selected;
                    /*
                    */
                    if(qualePacchettoSelezionato == -1)
                    {
                        qualePacchettoSelezionato = qualePacchettoRappresenta;
                        accettaPacchetto.setEnabled(true);
                        this.repaint();
                    }
                    else
                    {
                        if(selected)
                        {
                            etichettaNomePacchetti[qualePacchettoSelezionato].selected = false;
                            etichettaNomePacchetti[qualePacchettoSelezionato].repaint();
                            qualePacchettoSelezionato = qualePacchettoRappresenta;
                            accettaPacchetto.setEnabled(true);
                            this.repaint();
                        }
                        else
                        {
                            qualePacchettoSelezionato = -1;
                            accettaPacchetto.setEnabled(false);
                            this.repaint();
                        }
                    }
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent me)
                {}

                @Override
                public void mouseReleased(MouseEvent me)
                {}

                @Override
                public void mouseEntered(MouseEvent me)
                {
                    this.setBackground(new Color(150, 150, 150));
                }

                @Override
                public void mouseExited(MouseEvent me)
                {
                    this.setBackground(new Color(200, 200, 200));
                }
            }
        }
        //fine PannelloImpostazioni*************************************************
        
        //inizio PannelloGioco******************************************************
        private class PannelloPosizionaNavi extends JPanel implements ActionListener, MouseListener//PannelloGioco si dividerá in due pannelli: PannelloPosizionaNavi e PannelloGioca
        {
            private JButton confermaPosizione;
            private JButton pulsanteRandom;
            private JLabel etichettaDinamica;
            private PulsanteDipinto[][] pulsantiEditor;
            private PannelloNave[] pannelliNave;
            private int[] orientamentoNave;
            private JButton ruotaNave;
            private boolean[] naveInserita;//array di valori booleani che indicano quali navi sono giá state inserite nella griglia, serve per rendere attivo confermaPosizione
            private int naveSelezionata = -1;

            public PannelloPosizionaNavi()
            {
                this.naveInserita = new boolean[QUANTE_NAVI];
                if(qualeGiocatore == 1)
                {
                    PACCHETTO_NAVI.copy(PACCHETTOG1);
                    PACCHETTOG1.inizializzaOgniInfoNave();
                }
                else
                {
                    PACCHETTO_NAVI.copy(PACCHETTOG2);
                    PACCHETTOG2.inizializzaOgniInfoNave();
                }

                orientamentoNave = new int[QUANTE_NAVI];

                for(int i = 0; i < QUANTE_NAVI; i++)
                {
                    orientamentoNave[i] = 1;
                }

                this.setBackground(Color.WHITE);

                JPanel pannelloComandi = new JPanel();
                pannelloComandi.setLayout(new GridBagLayout());
                GridBagConstraints gbc2 = new GridBagConstraints();
                gbc2.insets = new Insets(5, 5, 5, 5);
                
                JLabel etichettaGiocatore;
                if(qualeGiocatore == 1)
                {
                   etichettaGiocatore  = new JLabel(nomeGiocatore1 + " posiziona le navi!");
                }
                else
                {
                    etichettaGiocatore  = new JLabel(nomeGiocatore2 + " posiziona le navi!");
                }
                
                gbc2.gridx = 0;
                gbc2.gridy = 0;
                pannelloComandi.add(etichettaGiocatore);
                
                etichettaDinamica = new JLabel("Scegliere una nave ed inserirla nella griglia");
                etichettaDinamica.setMinimumSize(etichettaDinamica.getPreferredSize());
                gbc2.gridx = 0;
                gbc2.gridy = 1;
                pannelloComandi.add(etichettaDinamica, gbc2);

                confermaPosizione = factoryPulsanteBase("Conferma", "conferma", false, true);
                confermaPosizione.addMouseListener(this);
                confermaPosizione.addActionListener(this);
                pannelloComandi.setBackground(Color.WHITE);
                gbc2.gridx = 0;
                gbc2.gridy = 2;
                pannelloComandi.add(confermaPosizione, gbc2);

                JPanel pannelloPosizionaNavi = new JPanel();
                pannelloPosizionaNavi.setBackground(Color.WHITE);
                pannelloPosizionaNavi.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();

                JPanel pannelloPacchettoMostrato = new JPanel();
                pannelloPacchettoMostrato.setBackground(Color.WHITE);
                pannelloPacchettoMostrato.setLayout(new GridLayout(3, 2));
                pannelliNave = new PannelloNave[6];
                for(int i = 0; i < 6; i++)
                {
                    pannelliNave[i] = new PannelloNave();
                    pannelliNave[i].setPreferredSize(new Dimension(200, 150));
                    pannelliNave[i].setLayout(new BorderLayout());
                    pannelliNave[i].setBackground(Color.WHITE);
                    pannelliNave[i].addNave(i, qualeGiocatore + 1, 1);//il "+1" é messo perché nel metodo addNave viene preso il pacchetto qualePacchetto - 1, e nel caso partisse il giocatore 2 il pacchettoG1 non sarebbe stato ancora inizializzato, e dato che 2 - 1 fa 1 in addNave() si sarebbe voluta agiungere la nave da PacchettoG1
                    pannelliNave[i].addMouseListener(this);
                    pannelloPacchettoMostrato.add(pannelliNave[i]);
                }

                JPanel pannelloPulsanteRuotaNave = new JPanel();
                pannelloPulsanteRuotaNave.setBackground(Color.WHITE);
                ruotaNave = factoryPulsanteBase("Ruota", "ruota", true, true);
                ruotaNave.addMouseListener(this);
                ruotaNave.addActionListener(this);
                pannelloPulsanteRuotaNave.add(ruotaNave);
                
                pulsanteRandom = factoryPulsanteBase("Random", "random", true, true);
                pulsanteRandom.addMouseListener(this);
                pulsanteRandom.addActionListener(this);
                pannelloPulsanteRuotaNave.add(pulsanteRandom);

                JPanel pannelloTavola = new JPanel();
                pannelloTavola.setBackground(Color.WHITE);
                BufferedImage tavolaDaMostrare = null;

                try
                {
                    tavolaDaMostrare = ImageIO.read(new File(iconsPath + "tavolaDaMostrare.png"));
                }
                catch(IOException e)
                {
                    System.exit(0);
                }

                JLabel picLabel = new JLabel(new ImageIcon(tavolaDaMostrare));
                picLabel.setLayout(null);
                pulsantiEditor = new PulsanteDipinto[10][10];

                for(int i = 0; i < 10; i++)
                {
                    for(int j = 0; j < 10; j++)
                    {
                        pulsantiEditor[i][j] = new PulsanteDipinto();

                        pulsantiEditor[i][j].setActionCommand(i + " " + j);
                        pulsantiEditor[i][j].setBackground(new Color(106 - 10 * i, 192 - 12 * i, 255 - 13 * i));
                        pulsantiEditor[i][j].setBounds(39 + 61 * j, 39 + (61 * i) + 1, 55, 54);
                        pulsantiEditor[i][j].addActionListener(this);
                        pulsantiEditor[i][j].setContentAreaFilled(false);
                        pulsantiEditor[i][j].setBorderPainted(false);
                        pulsantiEditor[i][j].setPosizionaNaviPartita(true);
                        picLabel.add(pulsantiEditor[i][j]);
                    }
                }

                pannelloTavola.add(picLabel);

                gbc.gridx = 0;
                gbc.gridy = 0;
                pannelloPosizionaNavi.add(pannelloComandi, gbc);

                gbc.gridx = 0;
                gbc.gridy = 1;
                pannelloPosizionaNavi.add(pannelloPacchettoMostrato, gbc);

                gbc.gridx = 0;
                gbc.gridy = 2;
                pannelloPosizionaNavi.add(pannelloPulsanteRuotaNave, gbc);

                pannelloTavola.setMinimumSize(new Dimension(650, 650));
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridheight = 3;
                gbc.fill = GridBagConstraints.VERTICAL;
                pannelloPosizionaNavi.add(pannelloTavola, gbc);
                this.add(pannelloPosizionaNavi);
            }

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(e.getActionCommand().equals("ruota"))
                {
                    if(naveSelezionata != -1)
                    {
                        if(orientamentoNave[naveSelezionata] == 4)
                            orientamentoNave[naveSelezionata] = 1;
                        else
                            orientamentoNave[naveSelezionata]++;

                        if(qualeGiocatore == 1)
                            PACCHETTOG1.ruotaNave(naveSelezionata, orientamentoNave[naveSelezionata]);
                        else
                            PACCHETTOG2.ruotaNave(naveSelezionata, orientamentoNave[naveSelezionata]);
                        
                        if(qualeGiocatore == 1)
                            pannelliNave[naveSelezionata].addNave(naveSelezionata, 2, orientamentoNave[naveSelezionata]);//il 2 é perché indica il PACCHETTOG1
                        else
                            pannelliNave[naveSelezionata].addNave(naveSelezionata, 3, orientamentoNave[naveSelezionata]);//il 2 é perché indica il PACCHETTOG1  
                        pannelliNave[naveSelezionata].repaint();
                        etichettaDinamica.setText("Inserire nella griglia la nave ruotata di 90°");
                    }
                }
                else if(e.getActionCommand().equals("conferma"))
                {
                    if(aiPlayer)
                    {
                        PACCHETTO_NAVI.copy(PACCHETTOG2);//ripeto questa cosa perché se si stesse giocando contro l'AI (e quindi il G2 é l'AI) non si ritornerebbe mai nel costruttore di PosizionaNavi, ragion per cui PACCHETTOG2 sarebbe una variabile che punta a nessun oggetto, e quindi ci sarebbero delle bellissime NullPointerException yeeee
                        PACCHETTOG2.inizializzaOgniInfoNave();
                        PACCHETTOG2.inserimentoRandom(tavolaNaviG2);
                        cambiaQualeGiocatore();
                        messaggio = qualePannello.PANNELLO_PARTITA;
                        updatePannelloGioco(this);
                    }
                    else
                    {
                        if(messaggio == qualePannello.PANNELLO_POSIZIONA_NAVI1)
                        {
                            messaggio = qualePannello.PANNELLO_POSIZIONA_NAVI2;
                            updatePannelloGioco(this);
                        }
                        else
                        {
                            messaggio = qualePannello.PANNELLO_PARTITA;
                            updatePannelloGioco(this);
                        }
                        
                    }
                }
                else if(e.getActionCommand().equals("random"))
                {
                    for(int i = 0; i < pulsantiEditor.length; i++)//per spostare la nave selezionata rende tutti i blocchi di quella nave null nella posizione precedente, per poi reinserirli nella nuova posizione
                    {
                        for(int j = 0; j < pulsantiEditor[i].length; j++)
                        {
                            if(qualeGiocatore == 1)
                            {
                                tavolaNaviG1[i][j] = null;
                            }
                            else
                            {
                                tavolaNaviG2[i][j] = null;
                            }
                        }
                    }
                    
                    if(qualeGiocatore == 1)
                    {
                        while(PACCHETTOG1.inserimentoRandom(tavolaNaviG1) == -1)
                        {;}
                    }
                    else
                    {
                        while(PACCHETTOG2.inserimentoRandom(tavolaNaviG2) == -1)
                        {;}
                    }
                    
                    for(int i = 0; i < pulsantiEditor.length; i++)
                    {
                           for(int j = 0; j < pulsantiEditor[i].length; j++)
                           {
                               if(qualeGiocatore == 1)
                               {
                                   if(tavolaNaviG1[i][j] != null)
                                   {
                                       pulsantiEditor[i][j].setSelected(true);
                                       pulsantiEditor[i][j].setColore(tavolaNaviG1[i][j].getColoreNave());
                                       repaint();
                                   }
                                   else
                                   {
                                       pulsantiEditor[i][j].setSelected(false);
                                       repaint();
                                   }
                               }
                               else
                               {
                                   if(tavolaNaviG2[i][j] != null)
                                   {
                                       pulsantiEditor[i][j].setSelected(true);
                                       pulsantiEditor[i][j].setColore(tavolaNaviG2[i][j].getColoreNave());
                                       repaint();
                                   }
                                   else
                                   {
                                       pulsantiEditor[i][j].setSelected(false);
                                       repaint();
                                   }
                               }
                           }
                    }
                    confermaPosizione.setEnabled(true);
                }
                else
                {
                    if(naveSelezionata != -1)
                    {
                        for(int i = 0; i < pulsantiEditor.length; i++)//per spostare la nave selezionata rende tutti i blocchi di quella nave null nella posizione precedente, per poi reinserirli nella nuova posizione
                        {
                            for(int j = 0; j < pulsantiEditor[i].length; j++)
                            {
                                if(qualeGiocatore == 1)
                                {
                                    if(tavolaNaviG1[i][j] != null)
                                    {
                                        if(tavolaNaviG1[i][j] == PACCHETTO_NAVI.getNave(naveSelezionata))
                                            tavolaNaviG1[i][j] = null;
                                    }
                                }
                                else
                                {
                                    if(tavolaNaviG2[i][j] != null)
                                    {
                                        if(tavolaNaviG2[i][j] == PACCHETTO_NAVI.getNave(naveSelezionata))
                                            tavolaNaviG2[i][j] = null;
                                    }
                                }
                            }
                        }

                        boolean continua = true;
                        //inserisce la nave in PACCHETTOG1
                        for(int i = 0; (i < pulsantiEditor.length) && continua; i++)
                        {
                            for(int j = 0; (j < pulsantiEditor[i].length) && continua; j++)
                            {
                                if(e.getActionCommand().equals("" + i + " " + j))
                                {
                                    if(qualeGiocatore == 1)
                                    {
                                        if(PACCHETTOG1.inserimentoManuale(j, i, orientamentoNave[naveSelezionata], tavolaNaviG1, naveSelezionata))
                                        {
                                            continua = false;
                                            etichettaDinamica.setText("Posizione perfetta, grande capitano!");
                                            naveInserita[naveSelezionata] = true;
                                            if(checkNaveInserita())
                                                confermaPosizione.setEnabled(true);
                                        }
                                        else
                                        {
                                            etichettaDinamica.setText("La posizione non é accettabile");
                                        }
                                    }
                                    else
                                    {
                                        if(PACCHETTOG2.inserimentoManuale(j, i, orientamentoNave[naveSelezionata], tavolaNaviG2, naveSelezionata))
                                        {
                                            continua = false;
                                            etichettaDinamica.setText("Posizione perfetta, grande capitano!");
                                            naveInserita[naveSelezionata] = true;
                                            if(checkNaveInserita())
                                                confermaPosizione.setEnabled(true);
                                        }
                                        else
                                        {
                                            etichettaDinamica.setText("La posizione non é accettabile");
                                        }
                                    }
                                }
                            }
                        }

                        for(int i = 0; i < pulsantiEditor.length; i++)
                        {
                            for(int j = 0; j < pulsantiEditor[i].length; j++)
                            {
                                if(qualeGiocatore == 1)
                                {
                                    if(tavolaNaviG1[i][j] != null)
                                    {
                                        pulsantiEditor[i][j].setSelected(true);
                                        pulsantiEditor[i][j].setColore(tavolaNaviG1[i][j].getColoreNave());
                                        repaint();
                                    }
                                    else
                                    {
                                        pulsantiEditor[i][j].setSelected(false);
                                        repaint();
                                    }
                                }
                                else
                                {
                                    if(tavolaNaviG2[i][j] != null)
                                    {
                                        pulsantiEditor[i][j].setSelected(true);
                                        pulsantiEditor[i][j].setColore(tavolaNaviG2[i][j].getColoreNave());
                                        repaint();
                                    }
                                    else
                                    {
                                        pulsantiEditor[i][j].setSelected(false);
                                        repaint();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            private boolean checkNaveInserita()
            {
                for(int i = 0; i < QUANTE_NAVI; i++)
                {
                    if(!naveInserita[i])
                        return false;
                }

                return true;
            }

            @Override
            public void mouseClicked(MouseEvent me) 
            {
                //controlla che la fonte sia un pannello
                for(int i = 0; i < QUANTE_NAVI; i++)
                {
                    if(me.getSource().equals(pannelliNave[i]))
                    {
                        if(naveSelezionata != -1)
                            pannelliNave[naveSelezionata].setBackground(Color.WHITE);

                        pannelliNave[i].setBackground(new Color(220, 220, 220));
                        naveSelezionata = i;
                        etichettaDinamica.setText("Inserire la nave scelta nella griglia");
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent me) 
            {}

            @Override
            public void mouseReleased(MouseEvent me)
            {}

            @Override
            public void mouseEntered(MouseEvent me) 
            {}

            @Override
            public void mouseExited(MouseEvent me)
            {}
        }

        private class Partita extends JPanel implements Serializable, ActionListener
        {
            private Timer tm = new Timer(1000, this);
            private PulsanteDipinto[][] pulsantiEditor;
            private statusPulsante[][] statusDelPulsante;
            private PannelloNave[] pannelliNave;
            private JLabel etichettaDiComando;
            private Timer eventoAI = new Timer(1, this);
            private boolean sonoUnaAI;
            private int colpo = 0;
            //Classe Partita: le istanze della classe Partita sono la partita in sè, come variabili di istanza contiene tutte le matrici del caso per ogni giocatore, inclusi i loro pacchetti nave. La partita si potrà salvare
            public Partita(boolean ai)
            {
                this.sonoUnaAI = ai;
                if(sonoUnaAI)
                {
                    BattagliaNavale.inizializzaPacchettoNave(PACCHETTOG1, PACCHETTO_NAVI);
                    BattagliaNavale.inizializzaTavolaDaMostrareGx();
                }
                this.tm.setActionCommand("tm");
                this.setLayout(new BorderLayout());
                this.setBackground(Color.WHITE);
                this.eventoAI.setActionCommand("ai");
                eventoAI.setActionCommand("ai");
                
                JPanel pannelloComponenti = new JPanel();
                pannelloComponenti.setBackground(Color.WHITE);
                pannelloComponenti.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                
                JPanel pannelloTavola = new JPanel();
                pannelloTavola.setBackground(Color.WHITE);
                BufferedImage tavolaDaMostrare = null;

                try
                {
                    tavolaDaMostrare = ImageIO.read(new File(iconsPath + "tavolaDaMostrare.png"));
                }
                catch(IOException e)
                {
                    System.exit(0);
                }

                JLabel picLabel = new JLabel(new ImageIcon(tavolaDaMostrare));
                picLabel.setLayout(null);
                pulsantiEditor = new PulsanteDipinto[10][10];
                statusDelPulsante = new statusPulsante[10][10];
                
                for(int i = 0; i < 10; i++)
                {
                    for(int j = 0; j < 10; j++)
                    {
                        pulsantiEditor[i][j] = new PulsanteDipinto();

                        pulsantiEditor[i][j].setActionCommand("" + i + " " + j);
                        pulsantiEditor[i][j].setBackground(new Color(106 - 10 * i, 192 - 12 * i, 255 - 13 * i));
                        pulsantiEditor[i][j].setBounds(39 + 61 * j, 39 + (61 * i) + 1, 55, 54);
                        pulsantiEditor[i][j].addActionListener(this);
                        pulsantiEditor[i][j].setContentAreaFilled(false);
                        pulsantiEditor[i][j].setBorderPainted(true);
                        pulsantiEditor[i][j].setPosizionaNaviPartita(false);
                        pulsantiEditor[i][j].setStatusPulsante(statusPulsante.NON_COLPITO);
                        statusDelPulsante[i][j] = statusPulsante.NON_COLPITO;
                        
                        picLabel.add(pulsantiEditor[i][j]);
                        
                    }
                }

                pannelloTavola.add(picLabel);
                
                JPanel pannelloEtichetta = new JPanel();
                pannelloEtichetta.setBackground(Color.WHITE);
                Font fontEtichetta = new Font("NSimSum", Font.PLAIN, 15);
                if(qualeGiocatore == 1)
                    etichettaDiComando = new JLabel(nomeGiocatore1);
                else
                    etichettaDiComando = new JLabel(nomeGiocatore2);
                
                etichettaDiComando.setFont(fontEtichetta);
                pannelloEtichetta.setPreferredSize(etichettaDiComando.getPreferredSize());
                pannelloEtichetta.add(etichettaDiComando);
                
                JPanel pannelloPacchettoMostrato = new JPanel();
                pannelloPacchettoMostrato.setBackground(Color.WHITE);
                pannelloPacchettoMostrato.setLayout(new GridLayout(3, 2));
                pannelliNave = new PannelloNave[6];
                for(int i = 0; i < 6; i++)
                {
                    pannelliNave[i] = new PannelloNave();
                    pannelliNave[i].setPreferredSize(new Dimension(200, 200));
                    pannelliNave[i].setMinimumSize(pannelliNave[i].getPreferredSize());
                    pannelliNave[i].setLayout(new BorderLayout());
                    pannelliNave[i].setBackground(Color.WHITE);
                    pannelliNave[i].addNave(i, 2, 1);//il "+1" é messo perché nel metodo addNave viene preso il pacchetto qualePacchetto - 1, e nel caso partisse il giocatore 2 il pacchettoG1 non sarebbe stato ancora inizializzato, e dato che 2 - 1 fa 1 in addNave() si sarebbe voluta agiungere la nave da PacchettoG1
                    pannelloPacchettoMostrato.add(pannelliNave[i]);
                }
                
                gbc.gridx = 0;
                gbc.gridy = 0;
                pannelloComponenti.add(pannelloEtichetta, gbc);
                
                gbc.gridx = 0;
                gbc.gridy = 1;
                pannelloComponenti.add(pannelloPacchettoMostrato, gbc);
                
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridheight = 2;
                gbc.fill = GridBagConstraints.VERTICAL;
                pannelloComponenti.add(pannelloTavola, gbc);
                
                this.add(pannelloComponenti, BorderLayout.CENTER);
            }

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(e.getActionCommand().equals("tm"))
                {
                    this.tm.stop();
                    messaggio = qualePannello.PANNELLO_PARTITA;
                    updatePannelloGioco(this);
                }
                else if(e.getActionCommand().equals("ai"))
                {
                    eventoAI.stop();
                    BattagliaNavale.updateTavolaDaMostrare(statusDelPulsante);
                    int coordinate[] = BattagliaNavale.mossaComputer(0, colpo);
                    cambiaStatoPulsanti(true);
                    pulsantiEditor[coordinate[0]][coordinate[1]].doClick();
                    cambiaStatoPulsanti(false);
                }
                else
                {
                    cambiaStatoPulsanti(false);
                    boolean continuaCiclo = true;
                    boolean continuaColpire = false;
                    boolean vittoria = false;
                    for(int i = 0; (i < pulsantiEditor.length) && continuaCiclo; i++)
                    {
                        for(int j = 0; (j < pulsantiEditor[i].length) && continuaCiclo; j++)
                        {
                            if(e.getActionCommand().equals(i + " " + j))
                            {
                                if(qualeGiocatore == 1)
                                {
                                    if(tavolaNaviG2[i][j] == null)
                                    {
                                        statusDelPulsante[i][j] = statusPulsante.ACQUA;
                                        continuaColpire = false;
                                        etichettaDiComando.setText(nomeGiocatore1 + " hai colpito un pesce :/");
                                    }
                                    else
                                    {
                                        tavolaNaviG2[i][j].setStatusBlocco(j, i);
                                        statusDelPulsante[i][j] = statusPulsante.NAVE;
                                        etichettaDiComando.setText(nomeGiocatore1 + " complimenti, hai colpito una nave");

                                        if(tavolaNaviG2[i][j].affondata())
                                        {
                                            int[] infoNave = tavolaNaviG2[i][j].getInfoNave();
                                            for(int k = 0; k < infoNave.length; k += 3)
                                            {
                                                statusDelPulsante[infoNave[k + 1]][infoNave[k]] = statusPulsante.AFFONDATA;
                                                pulsantiEditor[infoNave[k + 1]][infoNave[k]].setColore(tavolaNaviG2[infoNave[k + 1]][infoNave[k]].getColoreNave());
                                                pulsantiEditor[infoNave[k + 1]][infoNave[k]].setStatusPulsante(statusDelPulsante[infoNave[k + 1]][infoNave[k]]);
                                                pulsantiEditor[infoNave[k + 1]][infoNave[k]].repaint();
                                            }
                                            statusDelPulsante[i][j] = statusPulsante.AFFONDATA;
                                            etichettaDiComando.setText(nomeGiocatore1 + " complimenti, hai colpito ed affondato una nave");
                                            vittoria = PACCHETTOG2.controllaVittora();
                                            if(vittoria)
                                            {
                                                vincitore = 1;
                                                messaggio = qualePannello.PANNELLO_VITTORIA;
                                                updatePannelloGioco(this);
                                            }
                                        }
                                        continuaColpire = true;
                                    }
                                }
                                else
                                {
                                    if(tavolaNaviG1[i][j] == null)
                                    {
                                        statusDelPulsante[i][j] = statusPulsante.ACQUA;
                                        continuaColpire = false;
                                        etichettaDiComando.setText(nomeGiocatore2 + " hai colpito un pesce :/");
                                        if(sonoUnaAI)
                                            colpo = 0;
                                    }
                                    else
                                    {
                                        tavolaNaviG1[i][j].setStatusBlocco(j, i);
                                        statusDelPulsante[i][j] = statusPulsante.NAVE;
                                        etichettaDiComando.setText(nomeGiocatore2 + " complimenti, hai colpito una nave");
                                        if(sonoUnaAI)
                                            colpo = 1;

                                        if(tavolaNaviG1[i][j].affondata())
                                        {
                                            int[] infoNave = tavolaNaviG1[i][j].getInfoNave();
                                            for(int k = 0; k < infoNave.length; k += 3)
                                            {
                                                statusDelPulsante[infoNave[k + 1]][infoNave[k]] = statusPulsante.AFFONDATA;
                                                pulsantiEditor[infoNave[k + 1]][infoNave[k]].setColore(tavolaNaviG1[infoNave[k + 1]][infoNave[k]].getColoreNave());
                                                pulsantiEditor[infoNave[k + 1]][infoNave[k]].setStatusPulsante(statusDelPulsante[infoNave[k + 1]][infoNave[k]]);
                                                pulsantiEditor[infoNave[k + 1]][infoNave[k]].repaint();
                                            }
                                            statusDelPulsante[i][j] = statusPulsante.AFFONDATA;
                                            etichettaDiComando.setText(nomeGiocatore2 + " complimenti, hai colpito ed affondato una nave");
                                            vittoria = PACCHETTOG1.controllaVittora();
                                            if(vittoria)
                                            {
                                                if(aiPlayer)
                                                    vincitore = 3;
                                                else
                                                    vincitore = 2;
                                                messaggio = qualePannello.PANNELLO_VITTORIA;
                                                updatePannelloGioco(this);
                                            }
                                            if(sonoUnaAI)
                                                colpo = 2;
                                        }
                                        continuaColpire = true;
                                    }

                                    pulsantiEditor[i][j].setStatusPulsante(statusDelPulsante[i][j]);
                                    pulsantiEditor[i][j].repaint();
                                    continuaCiclo = false;  
                                }

                                pulsantiEditor[i][j].setStatusPulsante(statusDelPulsante[i][j]);
                                pulsantiEditor[i][j].repaint();
                                continuaCiclo = false;
                            }
                        }
                    }
                    if(!vittoria)
                    {
                        if(continuaColpire)
                        {
                            if(sonoUnaAI)
                            {
                                eventoAI = new Timer(500, this);
                                eventoAI.setActionCommand("ai");
                                eventoAI.start();
                            }
                            else
                                cambiaStatoPulsanti(true);
                        }
                        else
                        {
                            tm.start();
                        }
                    }
                }
            }
            
            public void cambiaStatoPulsanti(boolean valore)
            {
                 for(int i = 0; i < pulsantiEditor.length; i++)
                 {
                     for(int j = 0; j < pulsantiEditor[i].length; j++)
                     {
                         if(valore == false)
                            pulsantiEditor[i][j].setEnabled(false);
                         else
                         {
                            pulsantiEditor[i][j].setEnabled(true);
                         }
                     }
                 }
            }
            
            public void AIcolpisci()
            {
                if(sonoUnaAI)
                {
                    eventoAI = new Timer(1, this);
                    eventoAI.setActionCommand("ai");
                    eventoAI.start();
                }
                else
                {
                    System.out.println("Errore in AIcolpisci");
                    System.exit(0);
                }
            }        
        }
        
        private class PannelloVittoria extends JPanel implements ActionListener
        {
            private JButton pulsanteMenu;
            public PannelloVittoria()
            {
                this.setLayout(new BorderLayout());
                this.setBackground(Color.WHITE);
                JPanel pannelloEtichette = new JPanel();
                pannelloEtichette.setLayout(new GridBagLayout());
                pannelloEtichette.setBackground(Color.WHITE);
                GridBagConstraints gbc = new GridBagConstraints();
                ImageIcon iconaVittoria = null;
                String stringaVittoria = null;
                switch(vincitore)
                {
                    case 1:
                        stringaVittoria = nomeGiocatore1 + ", complimenti, hai sconfitto la flotta di " + nomeGiocatore2;
                        iconaVittoria = new ImageIcon(iconsPath + "CoppaG1.png");
                        break;
                    case 2:
                        stringaVittoria = nomeGiocatore2 + ", complimenti, hai sconfitto la flotta di " + nomeGiocatore1;
                        iconaVittoria = new ImageIcon(iconsPath + "CoppaG2.png");
                        break;
                    case 3:
                        stringaVittoria = nomeGiocatore1 + ", mi dispiace, ma sei stato sconfitto dall'AI";
                        iconaVittoria = new ImageIcon(iconsPath + "CoppaAI.png");
                        break;
                        
                }
                JLabel etichettaVincitore = new JLabel(stringaVittoria);
                Font font = new Font("Serif", Font.PLAIN, 20);
                etichettaVincitore.setFont(font);
                JLabel coppa = new JLabel();
                coppa.setIcon(iconaVittoria);
                
                gbc.insets = new Insets(20, 20, 20, 20);
                
                gbc.gridx = 0;
                gbc.gridy = 0;
                pannelloEtichette.add(etichettaVincitore, gbc);
                
                gbc.gridx = 0;
                gbc.gridy = 1;
                pannelloEtichette.add(coppa, gbc);
                
                JPanel pannelloPulsante = new JPanel();
                pulsanteMenu = factoryPulsanteBase("Menu", "menu", true, false);
                pulsanteMenu.addActionListener(this);
                pannelloPulsante.add(pulsanteMenu);
                pannelloPulsante.setBackground(Color.LIGHT_GRAY);
                
                this.add(pannelloEtichette, BorderLayout.CENTER);
                this.add(pannelloPulsante, BorderLayout.SOUTH);
            }

            @Override
            public void actionPerformed(ActionEvent ae)
            {
                messaggio = qualePannello.PRIMO_PANNELLO;
                updatePannelloGioco(this);
            }
        }
        
        private class PulsanteDipinto extends JButton
        {
            private statusPulsante status;
            private boolean posizionaNaviPartita;//indica se il pulsante in questione é in PosizionaNavi o Partita, se é true vuol dire che siamo in PosizionaNavi
            private boolean isSelected = false;//isSelected vale solo per posizionaNaviPartita = true
            private Color colore;

            public void setPosizionaNaviPartita(boolean posizionaNaviPartita)
            {
                this.posizionaNaviPartita = posizionaNaviPartita;
            }
            
            public void setColore(Color colore)
            {
                this.colore = colore;
            }
            
            public void setStatusPulsante(statusPulsante status)
            {
                this.status = status;
            }
            
            public statusPulsante getStatus()
            {
                return this.status;
            }

            @Override
            public void setSelected(boolean valore)
            {
                isSelected = valore;
            }

            @Override
            public void paintComponent(Graphics g)
            {
                if(posizionaNaviPartita)
                {
                    if(this.isSelected)
                    {
                        g.setColor(colore);
                        super.paintComponent(g);
                        g.fillOval(15, 15, 25, 25);
                    }
                }
                else
                {
                    switch(status)
                    {
                        case NON_COLPITO:
                            break;
                        case ACQUA:
                            BufferedImage imageAcqua = null;
                            
                            try
                            {
                                imageAcqua = ImageIO.read(new File(iconsPath + "Acqua.png"));
                            }
                            catch(IOException e)
                            {
                                System.out.println(e.getMessage());
                                System.exit(0);
                            }
                            
                            g.drawImage(imageAcqua, 2, 2, null);
                            this.setEnabled(false);
                            break;
                        case NAVE:
                            BufferedImage imageNaveColpita = null;
                            
                            try
                            {
                                imageNaveColpita = ImageIO.read(new File(iconsPath + "NaveColpita.png"));
                            }
                            catch(IOException e)
                            {
                                System.out.println(e.getMessage());
                                System.exit(0);
                            }
                            
                            g.drawImage(imageNaveColpita, 2, 2, null);
                            
                            this.setEnabled(false);
                            break;
                        case AFFONDATA:
                            this.setContentAreaFilled(false);
                            g.setColor(colore);
                            super.paintComponent(g);
                            g.fillOval(15, 15, 25, 25);
                            this.setEnabled(false);
                            break;
                    }
                }
            }
        } 
    }
    
    public void updateFrame(JPanel pannello)
    {
        pannello.setVisible(false);
        //pannello = null;
        //this.setResizable(false);
        if(messaggio == qualePannello.PRIMO_PANNELLO)
        {
            this.setSize(1080, 720);
            PrimoPannello pannelloPrincipale = new PrimoPannello();
            pannelloPrincipale.setVisible(true);
            this.add(pannelloPrincipale);
        }
        else if(messaggio == qualePannello.PANNELLO_MENU)
        {
            this.setSize(1080, 720);
            PannelloMenu pm = new PannelloMenu();
            pm.setVisible(true);
            this.add(pm);
        }
        else if(messaggio == qualePannello.PANNELLO_EDITOR)
        {
            this.setSize(1080, 720);
            PannelloEditor pe = new PannelloEditor();
            pe.setVisible(true);
            this.add(pe);
        }
        else if(messaggio == qualePannello.PANNELLO_IMPOSTAZIONIAI)
        {
            this.setSize(400, 550);
            PannelloGioco impostazioniAI = new PannelloGioco();
            impostazioniAI.setVisible(true);
            this.add(impostazioniAI);
        }
        else if(messaggio == qualePannello.PANNELLO_IMPOSTAZIONIPL)
        {
            this.setSize(400, 550);
            PannelloGioco impostazioni1vs1 = new PannelloGioco();
            impostazioni1vs1.setVisible(true);
            this.add(impostazioni1vs1);
        }
        else if(messaggio == qualePannello.RESIZA)
        {
            this.setSize(1080, 720);
        }
    }
}

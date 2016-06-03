/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piekarnik_2;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.*;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JFrame;

public class Piekarnik_2 extends JFrame{
    
    private Symulacja piekarnik;
    final private Color kolorRamki = Color.gray; 
    final private Color kolorTla   = Color.white; 
    //final private Color kolorLiter = Color.black;
    final private Color kolorWykresu = Color.blue;
    //static private Dimension sizeOkno;
    static private int lengOY, lengOX;
    private Vector<Double> out;
  
    Piekarnik_2()
    {
        super("Wykres");
        //stworz symulacje
        piekarnik = new Symulacja();
        
        setBounds(50,50,1080,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
        createBufferStrategy(2);
        
        //ustawienia minimalnego rozmiaru okna
        Dimension dimMinimum = this.getMinimumSize();
        dimMinimum.width = 300;
        dimMinimum.height = 200;
        setMinimumSize( dimMinimum );
        
        //sizeOkno = getSize();
        //lengOX = sizeOkno.width-80;
        //lengOY = sizeOkno.height-100;  
        repaint();
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Piekarnik_2 okno = new Piekarnik_2();
    }
    
    @Override
    public void paint(Graphics g) 
    { 
        
        if(piekarnik != null)  //rysowanie rozpoczyna sie dopiero po utworzeniu symulcji
        {
        // Pobiera aktualne rozmiary panela 
        Dimension d = getSize();
        
        // Rozmiary osi
        lengOX = d.width-80;
        lengOY = d.height-100;

        // Wypelnia panel kolorem tla 
        g.setColor(kolorTla); 
        g.fillRect(0, 0, d.width - 1, d.height - 1); 

        // Rysuje ramke dookola panela 
        g.setColor(kolorRamki); 
        g.drawRect(9, 40, d.width - 19, d.height - 50); 

        //wyśrodkowuje punkt
        Insets b = getInsets(); 
        g.translate (b.left, b.bottom); 
        
        out = (Vector<Double>)piekarnik.getOutput().clone(); //wczytuje kopie wektora wyjscia
        
        // osie 
        g.setColor(Color.black); 
        skalowanieOY(g, d);
        rysujOX(g, (int)piekarnik.getSimulationTime());
        
        //rysuje odpowiedz ukladu
        rysujOdp(g);
        }
    } 
    
    public void skalowanieOY(Graphics g, Dimension d)
    {        
        double max = Collections.max(out);
        double min = Collections.min(out);
        int maxR = (int)Math.ceil(max);
        int minR = (int)Math.floor(min);
        int n = maxR + Math.abs(minR);
        
        int minOy = minR*lengOY/n;
        int maxOy = maxR*lengOY/n;
        
        if(min < 0)    //spradza czy trzeba rysowac os ujemna
        {
            g.translate (35,d.height+minOy-50);  //zmienia punkt srodka wzgledem ktorego jest rysowany wykres
            rysujOY(g, minOy, maxOy, n*2);
        }
        else
        {
            g.translate (35,d.height-50);  //zmienia punkt srodka wzgledem ktorego jest rysowany wykres
            rysujOY(g, 0, lengOY, n*2);
        }
        
        for(int i = 0; i < out.size(); i++)  // przeskalowanie osi Y - proporcja
        {
            double scaleY = (lengOY*out.elementAt(i))/n;
            out.set(i, scaleY);
        }
    }
    
    public void rysujOY(Graphics g, int min, int max, int n) //rysowanie osi Y
    {
        double N = (double)lengOY/(double)n;
        int minN = min/(int)N;
        g.drawLine( 0, -min, 0, -max );  //rysowanie glownej lini osi
        for(int i = 0; i <= n; i++)   //rysowanie podzailki na osi wraz z liczbami
        {
            int poz = (int)Math.round((double)i*N);
            g.drawLine(3,-min-poz,-3,-min-poz);
            g.drawString(Double.toString((double)(i+minN)/2), -22, -min-poz);
        }
    }
    
    public void rysujOX(Graphics g, int n)   //rysowanie osi X
    {
        double N = (double)lengOX/(double)n;
        g.drawLine( 0, 0, lengOX, 0);  //rysowanie glownej lini osi
        for(int i = 0; i <= n; i++)   //rysowanie podzailki na osi wraz z liczbami
        {
            int poz = (int)Math.round((double)i*N);
            g.drawLine(poz,-3,poz,3);
            g.drawString(Double.toString((double)(i)), poz, 15);
        }
    }
    
    public void rysujOdp(Graphics g)  //rysuje odpowiedz ukladu
    {
        g.setColor(kolorWykresu); // ustawienie koloru wykresu
                       
        //rysowanie wykresu
        for(int i = 1; i < out.size(); i++)
        {
            int a1 = (int)Math.round(out.elementAt(i-1));
            int a2 = (int)Math.round(out.elementAt(i));
            //skalowanie w osi X
            double j1 = Math.round(((double)i-1)*lengOX/piekarnik.getIterations());
            double j2 = Math.round(((double)i)*lengOX/piekarnik.getIterations());
            //rysowanie lini laczacej dwa punkty
            g.drawLine((int)j1, -a1, (int)j2, -a2);
        }
    }
}

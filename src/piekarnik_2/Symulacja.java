/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package piekarnik_2;

import java.util.Vector;


public class Symulacja {
       
    private Vector<Double> input;    //wektor z wartosciami dla wybranego wejscia
    private Vector<Double> output;   //wektor z wartosciami wyjscia
    private double calka;     //caleczka
    private double podstawa = 0.005;  //podstawa czasu dt
    private double czasSymulacji = 10; //czas trwania symulacji
    private int iteracje;
    private double T = 1;  //stala czasowa
    private double K = 1;  //wzmocnienie
    private double warPocz = 0; //warunki poczatkowe
    private double b = .8;  //wartosc wyjścia histerezy
    private double B = b; //wyjście histerezy
    private double H = 0.05;   //szerokosc petli histerezy
    private double przesHister = 0.35;  //przesuniecie histerezy
    private Input wej;
    
    /**
     * 
     */
    public enum Input {
        STEP, RAMP, SIN
    }
    
    
    private double calk(double i0, double i1, double dt) //całkowanie numeryczne - metoda trapezów
    {
     calka += ((i0+i1)/2)*dt;
     return calka;
    }
    private double spUL(double u, double y, double a, double b) //sprzeżenie zwrotne układu liniowego
    {
        return b*u - a*y;
    }
    private double spUN(double r, double y) //sprzeżenie zwrotne dla całego ukladu z członem nieliniowym
    {
        return r - y;
    }
    
    private double histereza(double in, double a) //czlon symulujacy dzialanie histerezy
    {       
        if (in > a+przesHister)
            B = b;
        if (in < -a+przesHister)
            B = -b;

        return B;
    }
    
    private void liczWejscie(Vector<Double> in, int iter) //liczy wartości dla wybranego wejścia i zapisuje do wektora
    {
        in.clear();
        for(int i = 0; i < iter; i++)
        {
            switch(wej){
                 case STEP:{
                      in.add(1.0);
                      break;
                 }    
                 case RAMP:{
                      double n = i*podstawa;
                      in.add(n);
                      break;
                 }            
                 case SIN:{   
                      double n = Math.sin(i*podstawa);
                      in.add(n);
                      break;
                 }
            }
        }
    }
       
    Symulacja()
    {
        input = new Vector<Double>();
        output = new Vector<Double>();
        iteracje = (int) (czasSymulacji/podstawa);
        
        stratSymulacja(Input.STEP);     
    }
    
    
    public void stratSymulacja(Input in) //symuluje dzialanie ukladu
    {
        wej = in;
        liczWejscie(input, iteracje);
        output.clear();
        calka = 0;
        
        double a = 1/T;     //wspolczynik a
        double b = K/T;     //wspolczynik b
        double wejCalk, wyj;     //wyjscie Y i wejscie U na czlon calkujacy 1/s
        double wejCalkTmp = input.firstElement();          //poprzednia watosc wejscia calki
        
        wyj = warPocz;  // przypisanie wartosci warunkow poczatkowych do wyjscia Y
        
        //System.out.println("iter = "+ iteracje);
        
        for(int i =0; i<iteracje; i++)
        {
            double u0 = spUN(input.elementAt(i), wyj);
            double u1 = histereza(u0, H/2);   // wyjscie z histerezy
            wejCalk = spUL(u1, wyj, a, b);
            
            wyj = calk(wejCalkTmp, wejCalk, podstawa);
            wejCalkTmp = wejCalk;
            
            output.add(wyj);
            System.out.println(""+ wyj);
            //System.out.println("u = "+ u0);
            //System.out.println("u = "+ u1);
        }
  
    }
    
    //jekies tam funkcje z dupy bo to Java
    //get wyjscie
    public Vector<Double> getOutput(){
        return output;
    }
    //get wejscie
    public Vector<Double> getInput(){
        return input;
    }
    //---
    
    //set i get stala czasowa
    public double getTimeConstant(){
        return T;
    }
    
    public void setTimeConstant(double T){
        this.T = T;
    }
    //---
    
    //set i get wzmocnienie
    public double getGain(){
        return K;
    }
    
    public void setGain(double K){
        this.K = K;
    }
    //---
    
    //set i get podstawa czasu dla calkowania
    public double getTimeBase(){
        return podstawa;
    }
    
    public void setTimeBase(double podstawa) {
        this.podstawa = podstawa;
    }
    //---
    
    //set i get czas symulacji
    public double getSimulationTime() {
        return czasSymulacji;
    }

    public void setSimulationTime(double czasSymulacji) {
        this.czasSymulacji = czasSymulacji;
    }
    //---
    
    //get itaracje
    public int getIterations(){
        return iteracje;
    }
    //---
    
    //set i get warunki poczatkowe
    public double getInitCondition() {
        return warPocz;
    }

    public void setInitCondition(double y0) {
        this.warPocz = y0;
    }
    //---
    
    //set i get wyjscie histerezy
    public double getHystOutput() {
        return b;
    }

    public void setHystOutput(double b) {
        this.b = b;
    }
    //---
    
    //set i get szerokosc petli histerezy
    public double getHystWidth() {
        return H;
    }

    public void setHystWidth(double H) {
        this.H = H;
    }
    //---
    
    //set i get przesuniecie histerezy
    public double getOffsetHyst() {
        return przesHister;
    }

    public void setOffsetHyst(double przesHister) {
        this.przesHister = przesHister;
    }
    //---
}

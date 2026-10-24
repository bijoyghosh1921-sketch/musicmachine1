//Java 21
//My atempt at making a progressive rock song
import javax.sound.midi.*;
public class intro{
    private Synthesizer synth;
    private MidiChannel gch;
    private MidiChannel ch;
    private MidiChannel dch;
    public intro(){
        try{
            this.synth= MidiSystem.getSynthesizer();
            this.synth.open();
            MidiChannel[] ac= this.synth.getChannels();
            this.gch= ac[0];
            this.ch=ac[1];
            this.dch=ac[9];
            
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    public void shutdown(){
        if(this.synth.isOpen() && this.synth!=null){
            this.synth.close();
        }
    }
    public static void main(String[] args){
        intro a= new intro();//phrygian: hwwwhww
        try{
        a.gch.programChange(25);//{48,50,52,53,55,57,59,59,57,55,59,59,57,55,57,55,53,55,57,59,55,59,57,55,65,64,62,60,59};
        a.ch.programChange(0);//B Phrygian:59,60,50,52,53,55,57,59 
        int cphrygian[]= {49,39,29,31,32,22,24};
        int k=cphrygian.length*4;
        int drums[]= {48,47,35,41,43,35,41};
        int drums2[]= {35,35,47,47,35,35,40};
        //ac bass drum=35 ac snare=38 hi/high mid/low mid/low tom/lft/hft=50/48/47/45/43/41  cymbal=49 
        for(int i=0; i<k; i++){
        if(i<=14){
         a.ch.noteOn(cphrygian[i%7],140);
        
         Thread.sleep(750);
         
         a.ch.noteOff(cphrygian[i%7],140);
        }else{
         a.ch.noteOn(cphrygian[i%7],140);
         a.dch.noteOn(drums[i%7],100);
         Thread.sleep(625);
         a.dch.noteOn(drums[i%7],100);
         a.ch.noteOff(cphrygian[i%7],140);
        }}
        for(int i=0; i<k; i++){
        
         a.ch.noteOn(cphrygian[i%7],140);
         a.dch.noteOn(drums2[i%7],100);
         Thread.sleep(500);
         a.dch.noteOn(drums2[i%7],100);
         a.ch.noteOff(cphrygian[i%7],140);
         
        
        }
        Thread melody= new Thread(()->{try{
        for(int i=0; i<k; i++){
        
         a.ch.noteOn(cphrygian[i%7],140);
         
         Thread.sleep(500);
         
         a.ch.noteOff(cphrygian[i%7],140);
         
        
        }
        
        
        }catch(InterruptedException e){System.out.println("Error");}});
        Thread rhythm= new Thread(()->{try{
        for(int i=0; i<k*4; i++){
        
         a.dch.noteOn(drums[i%7],140);
         
         Thread.sleep(125);
         
         a.dch.noteOff(drums[i%7],140);
         
        
        }
        
        
        }catch(InterruptedException e){System.out.println("Error");}});
        rhythm.start();
        melody.start();
        rhythm.join();
        melody.join();
        for(int i=0; i<k*4; i++){
        
         a.dch.noteOn(drums[i%7],140);
         
         Thread.sleep(125);
         
         a.dch.noteOff(drums[i%7],140);
         if(i==k*4-1){
         a.dch.noteOn(49,160);
         Thread.sleep(1000);
         a.dch.noteOff(49,160);
         
        }
        }
        for(int i=0; i<k; i++){
        
         a.gch.noteOn(cphrygian[i%7],140);
         a.ch.noteOn(cphrygian[i%7],140);
         Thread.sleep(750);
         a.ch.noteOn(cphrygian[i%7],140);
         a.gch.noteOff(cphrygian[i%7],140);        
        
        }
        for(int i=0; i<k; i++){
        
         a.gch.noteOn(cphrygian[i%7],100);
         a.ch.noteOn(cphrygian[i%7],100);
         Thread.sleep(750);
         a.ch.noteOn(cphrygian[i%7],100);
         a.gch.noteOff(cphrygian[i%7],100);              
        }
        int drums3[]={49,48,47,45,47,43,41};
        //c phrygian: 49,39,29,31,32,22,24 
        
        a.ch.programChange(81);
        int synthsolop1[]={25,27,29,27,39,41,44};
        int synthsolop2[]={44,44,46,48,46,44,41};
        int synthsolop3[]={49,50,51,49,51,41,43,41,39,29,31,32,34,36,41,43,44,56,53,51,44,56,53,51,49,46,51,48};
         for(int i=0; i<k; i++){
             a.dch.noteOn(drums3[i%7],120);
             Thread.sleep(500);
             a.dch.noteOff(drums3[i%7],120);
           }
        
        Thread msr= new Thread(() ->{
            try{
             for(int i=0; i<k*4; i++){
             a.dch.noteOn(drums3[i%7],120);
             Thread.sleep(500);
             a.dch.noteOff(drums3[i%7],120);
           }
            
            }catch(InterruptedException e){System.out.println("Error");}
        });
        
        Thread msm= new Thread(() -> {
            try{
           for(int i=0; i<k*4; i++){
             a.ch.noteOn(cphrygian[i%7],120);
             Thread.sleep((i!=k*4-1)?125:500);
             a.ch.noteOff(cphrygian[i%7],120);
           }
           for(int i=0; i<k*4; i++){
             a.ch.noteOn(synthsolop1[i%7],120);
             Thread.sleep((i!=k*4-1)?125:500);
             a.ch.noteOff(synthsolop1[i%7],120);
           }
           for(int i=0; i<k*4; i++){
             a.ch.noteOn(synthsolop2[i%7],120);
             Thread.sleep((i!=k*4-1)?125:500);
             a.ch.noteOff(synthsolop2[i%7],120);
           }
           int volneg=1;
           for(int i=0; i<k*4; i++){
             int v= (i!=84)?120:120-volneg;
             volneg++;
             a.ch.noteOn(synthsolop3[i%28],v);
             Thread.sleep((i!=k*4-1)?125:2000);
             a.ch.noteOff(synthsolop3[i%28],v);
           }
           
           
        }catch(InterruptedException e){System.out.println("Error");}});
        msr.start();
        msm.start();        
        msr.join();
        msm.join();
        for(int i=0; i<k; i++){
             a.dch.noteOn(drums3[i%7],120);
             Thread.sleep(500);
             a.dch.noteOff(drums3[i%7],120);
           }
        Thread m2sr= new Thread(() ->{
            try{
             for(int i=0; i<k*12; i++){
             a.dch.noteOn(drums3[i%7],120);
             Thread.sleep(250);
             a.dch.noteOff(drums3[i%7],120);
           }
            
            }catch(InterruptedException e){System.out.println("Error");}
        });
        Thread m2sm= new Thread(() -> {
            try{
             for(int i=0; i<k*8; i++){
             a.dch.noteOn(drums[i%7],100);
             Thread.sleep(500);
             a.dch.noteOff(drums[i%7],100);
           }
           
        }catch(InterruptedException e){System.out.println("Error");}});
        
        Thread m2Sm=new Thread(() -> {
            try{
             for(int i=0; i<k*16; i++){
             a.dch.noteOn(drums2[i%7],160);
             Thread.sleep(125);
             a.dch.noteOff(drums2[i%7],160);
           }
           
        }catch(InterruptedException e){System.out.println("Error");}});
        m2sr.start();
        m2sm.start();
        m2Sm.start();
        m2sr.join();
        m2sm.join();
        m2Sm.join();
        
        
        
        
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }finally{
            a.shutdown();
        }
        
    }
    
}

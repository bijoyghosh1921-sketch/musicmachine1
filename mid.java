//Java 21
//Prog rock song continuation, this is the mid section
import javax.sound.midi.*;

public class mid {
    private Synthesizer synth;
    private MidiChannel gch;
    private MidiChannel ch;
    private MidiChannel bch;
    private MidiChannel dch;

    public mid() {
        try {
            this.synth = MidiSystem.getSynthesizer();
            this.synth.open();
            MidiChannel[] ac = this.synth.getChannels();
            this.ch = ac[0];  
            this.gch = ac[1]; 
            this.bch = ac[2];  
            this.dch = ac[9]; 
        } catch (Exception e) {
            System.out.println("Error initializing MIDI: " + e.getMessage());
        }
    }

    public void shutdown() {
        if (this.synth != null && this.synth.isOpen()) {
            this.synth.close();
        }
    }

    public static void main(String[] args) {
        mid a = new mid();//E aeolian:52,54,55,57,59,60,62,64
        //ac bass drum=35 ac snare=38 hi/high mid/low mid/low tom/lft/hft=50/48/47/45/43/41  cymbal=49 
        try {
            a.ch.programChange(33);//Fingered electric bass
            a.gch.programChange(79);//Ocarina
            a.bch.programChange(0);//Grand Piano
            int drums[]={35,38,41,43,47,48,41};
            int basslead1[]={40,42,43,45,47,48,50,48,36,38,40,42,47,48,50,51,52};
            int basslead2[]= {42,43,45,47,42,43,45,47,45,46,47,48,50,52};
            int basslead3[]={40,41,40,42,43,45,47,45,47,45,47,45,47,49,45,47,49,45,47,49,52,50};//resolution melody:45,47,49,45,47,49,45,47,49,52,50
            int k=drums.length*4;
            for(int i=0; i<k*4; i++){
            a.dch.noteOn(drums[i%7],120);
            Thread.sleep(250);
            a.dch.noteOff(drums[i%7]);
            }
            //Drum rhythm
            Thread d=new Thread(() -> {
            try{
                for(int i=0; i<k*8; i++){
                  a.dch.noteOn(drums[i%7],120);
                  Thread.sleep(250);
                  a.dch.noteOff(drums[i%7]);
            }
           
           
             }catch(InterruptedException e){System.out.println("Error");}});
            //Bass Lead
            Thread b=new Thread(() -> {
            try{
                for(int i=0; i<(k*4+7)*2; i++){
                  a.ch.noteOn(basslead1[i%17],120);
                  Thread.sleep(125);
                  a.ch.noteOff(basslead1[i%17]);
            }
            for(int i=0; i<k*6; i++){
                  a.ch.noteOn(basslead2[i%14],120);
                  Thread.sleep(125);
                  a.ch.noteOff(basslead2[i%14]);
            }
            for(int i=0; i<k*4+7; i++){
                  a.ch.noteOn(basslead3[i%22],120);
                  Thread.sleep(125);
                  a.ch.noteOff(basslead3[i%22]);
            }
            
            
           
           
             }catch(InterruptedException e){System.out.println("Error");}});
            b.start();
            d.start();
            b.join();//E aeolian:52,54,55,57,59,60,62,64
            d.join();//ac bass drum=35 ac snare=38 hi/high mid/low mid/low tom/lft/hft=50/48/47/45/43/41  cymbal=49
            int brh[]={45,42,43,45,47,48,46,45,47,48,42,43,47,48,50,52};
            int gl1[]={38,40,43,45,47,48,50,52,47,48,50,52,47,48,50,52};
            for(int i=0; i<k*4; i++){
            a.ch.noteOn(brh[i%16], 120);
            Thread.sleep(200);
            a.ch.noteOff(brh[i%16]);           
            }
            //Bass Rhythm
            Thread br=new Thread(() -> {
            try{
              for(int i=0; i<k*6; i++){
            a.ch.noteOn(brh[i%16], 100);
            Thread.sleep(200);
            a.ch.noteOff(brh[i%16]);           
            }
           
             }catch(InterruptedException e){System.out.println("Error");}});
            //Ocarina Lead
            Thread gl=new Thread(() -> {
            try{
              for(int i=0; i<k*2; i++){
            a.gch.noteOn(brh[i%16]+12, 120);
            Thread.sleep(200);
            a.gch.noteOff(brh[i%16]+12);           
            }
            
              for(int i=0; i<k*4; i++){
            a.gch.noteOn(gl1[i%16]+24, 120);
            Thread.sleep(200);
            a.gch.noteOff(gl1[i%16]+24);           
            }
           
             }catch(InterruptedException e){System.out.println("Error");}});
            //Grand piano rhythm
            Thread pr=new Thread(() -> {
            try{
              for(int i=0; i<k*6; i++){
            a.bch.noteOn(brh[i%16], 100);
            Thread.sleep(200);
            a.bch.noteOff(brh[i%16]);           
            }
           
             }catch(InterruptedException e){System.out.println("Error");}});
            gl.start();
            br.start();
            pr.start();
            pr.join();
            gl.join();
            br.join();
            //Ocarina resolution
            for(int i=0; i<k*4-2; i++){
                  a.gch.noteOn(basslead3[i%22]+36,120);
                  Thread.sleep(200);
                  a.gch.noteOff(basslead3[i%22]+36);
            }
             
        } catch (Exception e) {
            System.out.println("Error playing sequence: " + e.getMessage());
            a.shutdown();
        }
    }
}

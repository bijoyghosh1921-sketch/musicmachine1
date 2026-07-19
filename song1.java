import javax.sound.midi.*;
public class song1{
    private Synthesizer synth;
    private MidiChannel gchannel;
    private MidiChannel pchannel;
    private MidiChannel bchannel;
    private MidiChannel dchannel;
    public song1(){
        try{
            this.synth= MidiSystem.getSynthesizer();
            this.synth.open();
            MidiChannel[] ac=this.synth.getChannels();
            this.pchannel=ac[0];
            this.gchannel=ac[1];
            this.bchannel=ac[2];
            this.dchannel=ac[9];
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    public void shutdown(){
        if(this.synth!=null && this.synth.isOpen())
        this.synth.close();
    }
    public static void main(String[] args){
        song1 eng=new song1();
        try{
          eng.pchannel.programChange(6);
          eng.gchannel.programChange(27);
          int[] pnotes={38, 41, 45, 43};//38, 41, 45, 43
          int[] gnotes={50, 53, 57, 56, 55, 53, 52, 50};
          int[] bnotes={38, 41, 45, 43,38, 41, 45, 43};
          int step=0;
          for(int i=0; i<88; i++){
              if(i<=20){
              eng.dchannel.noteOn(36, 127); 
              eng.dchannel.noteOn(42, 100); 
              
              eng.dchannel.noteOn(38, 127); 
              eng.dchannel.noteOn(42, 100); 
              
              int ppitch=pnotes[step/2];
              int bpitch=bnotes[step];
              int gpitch=gnotes[step];
              if(step%2==0){
                  eng.pchannel.noteOn(ppitch,120);
                  eng.bchannel.noteOn(bpitch,220);
              }
              eng.gchannel.noteOn(gpitch,200);
              Thread.sleep(100);
              eng.gchannel.noteOff(gpitch);
              eng.bchannel.noteOff(bpitch);
              if(step%2==1){
                  eng.pchannel.noteOff(ppitch);
              }
              if(i%8==0){
                  step=0;
              }else
              step++;
          }else{
              eng.dchannel.noteOn(36, 127); 
              eng.dchannel.noteOn(42, 100); 
              Thread.sleep(100);
              eng.dchannel.noteOn(38, 127); 
              eng.dchannel.noteOn(42, 100); 
              
              int ppitch=pnotes[step/2];
              int bpitch=bnotes[step];
              int gpitch=gnotes[step];
              if(step%2==0){
                  eng.pchannel.noteOn(ppitch,120);
                  eng.bchannel.noteOn(bpitch,220);
              }
              eng.gchannel.noteOn(gpitch,200);
              Thread.sleep(100);
              eng.gchannel.noteOff(gpitch);
              eng.bchannel.noteOff(bpitch);
              if(step%2==1){
                  eng.pchannel.noteOff(ppitch);
              }
              if(i%8==0){
                  step=0;
              }else
              step++;
            }}
          
          Thread.sleep(500);
          eng.gchannel.programChange(30);
          int[] tornadoSoloNotes = {34, 35, 38, 40, 41, 43, 46, 47, 50, 52, 53, 55, 57, 59, 62, 65, 67, 71};
          
        
          int totalSoloSteps = 108; 
          
          for(int i = 0; i < totalSoloSteps; i++) {
              eng.dchannel.noteOn(36, 127); 
              eng.dchannel.noteOn(42, 100); 
              Thread.sleep(100);
              eng.dchannel.noteOn(38, 127); 
              eng.dchannel.noteOn(42, 100); 
              
              int ppitch = tornadoSoloNotes[step];
              int bpitch = bnotes[step/4];
              
              
              if(step % 2 == 0) {
                eng.pchannel.noteOn(ppitch, 200);
                  eng.bchannel.noteOn(bpitch, 220);
              }
              
              
              Thread.sleep(100);
              
              eng.bchannel.noteOff(bpitch);
              eng.pchannel.noteOff(ppitch);
              
              
              
              if(i % 8 == 0) {
                  step = 0;
              } else {
                  step++;
              }
              
              
          }
          
          
          
        
                 
        }
        catch(InterruptedException e){
        System.out.println("Playback op failed");
        }finally{
        eng.shutdown();
        }
    }
}

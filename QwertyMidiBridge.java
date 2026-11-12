import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class QwertyMidiBridge {
    private final Map<Integer, Integer> keyMap = new HashMap<>();
    private final Set<Integer> pressedKeys = new HashSet<>();

    private Synthesizer synthesizer;
    private MidiChannel channel;
    private MidiChannel dch;

    public QwertyMidiBridge() throws Exception {
        initializeKeyMap();

        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();

        MidiChannel[] channels = synthesizer.getChannels();
        if (channels == null || channels.length == 0 || channels[0] == null) {
            synthesizer.close();
            throw new IllegalStateException("No MIDI channel is available.");
        }
        channel = channels[0];
        dch= channels[9];
        channel.programChange(105);
        JFrame frame = new JFrame("QWERTY MIDI Keyboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(
                "<html><center>Click here, then play:<br>"
                        + "Q 2 W 3 E R 5 T 6 Y 7<br>"
                        +"C4 through B4 <br>"
                        +"Z S X D C V G B H N J M K<BR>"
                        + "C5 through C6</center></html>",
                SwingConstants.CENTER
        ), BorderLayout.CENTER);

        installKeyBindings(panel);

        frame.add(panel);
        frame.setSize(400, 180);
        frame.setLocationRelativeTo(null);

        frame.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                releaseAllNotes();
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                releaseAllNotes();
                synthesizer.close();
            }
        });

        frame.setVisible(true);
        panel.requestFocusInWindow();
    }

    private void initializeKeyMap() {
        int o=0;
        keyMap.put(java.awt.event.KeyEvent.VK_Q, 60-o); // C4
        keyMap.put(java.awt.event.KeyEvent.VK_2, 61-o); // C#4
        keyMap.put(java.awt.event.KeyEvent.VK_W, 62-o); // D4
        keyMap.put(java.awt.event.KeyEvent.VK_3, 63-o); // D#4
        keyMap.put(java.awt.event.KeyEvent.VK_E, 64-o); // E4
        keyMap.put(java.awt.event.KeyEvent.VK_R, 65-o); // F4
        keyMap.put(java.awt.event.KeyEvent.VK_5, 66-o); // F#4
        keyMap.put(java.awt.event.KeyEvent.VK_T, 67-o); // G4
        keyMap.put(java.awt.event.KeyEvent.VK_6, 68-o); // G#4
        keyMap.put(java.awt.event.KeyEvent.VK_Y, 69-o); // A4
        keyMap.put(java.awt.event.KeyEvent.VK_7, 70-o); // A#4
        keyMap.put(java.awt.event.KeyEvent.VK_U, 71-o); // B4
        keyMap.put(java.awt.event.KeyEvent.VK_Z, 72-o); // C5
        keyMap.put(java.awt.event.KeyEvent.VK_S, 73-o); // C#5
        keyMap.put(java.awt.event.KeyEvent.VK_X, 74-o); // D5
        keyMap.put(java.awt.event.KeyEvent.VK_D, 75-o); // D#5
        keyMap.put(java.awt.event.KeyEvent.VK_C, 76-o); // E5
        keyMap.put(java.awt.event.KeyEvent.VK_V, 77-o); // F5
        keyMap.put(java.awt.event.KeyEvent.VK_G, 78-o); // F#5
        keyMap.put(java.awt.event.KeyEvent.VK_B, 79-o); // G5
        keyMap.put(java.awt.event.KeyEvent.VK_H, 80-o); // G#5
        keyMap.put(java.awt.event.KeyEvent.VK_N, 81-o); // A5
        keyMap.put(java.awt.event.KeyEvent.VK_J, 82-o); // A#5
        keyMap.put(java.awt.event.KeyEvent.VK_M, 83-o); // B5
        keyMap.put(java.awt.event.KeyEvent.VK_K, 84-o); // C6
        
    }

    private void installKeyBindings(JPanel panel) {
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();

        for (Map.Entry<Integer, Integer> entry : keyMap.entrySet()) {
            int keyCode = entry.getKey();
            int note = entry.getValue();

            String pressAction = "noteOn-" + keyCode;
            String releaseAction = "noteOff-" + keyCode;

            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, false), pressAction);
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, true), releaseAction);

            actionMap.put(pressAction, new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (pressedKeys.add(keyCode)) {
                        channel.noteOn(note, 120);
                    }
                }
            });

            actionMap.put(releaseAction, new AbstractAction() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (pressedKeys.remove(keyCode)) {
                        channel.noteOff(note);
                    }
                }
            });
        }
    }

    private void releaseAllNotes() {
        for (int keyCode : pressedKeys) {
            Integer note = keyMap.get(keyCode);
            if (note != null && channel != null) {
                channel.noteOff(note);
            }
        }
        pressedKeys.clear();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new QwertyMidiBridge();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Could not start the MIDI synthesizer:\n" + e.getMessage(),
                        "MIDI Error",
                        JOptionPane.ERROR_MESSAGE
                );
                e.printStackTrace();
            }
        });
        int oct=-12;
        int bass[]={40-oct, 40-oct, 40-oct, 40-oct, 40-oct, 40-oct, 40-oct,40-oct,40-oct, 43-oct, 36-oct, 38-oct, 40-oct, 40-oct,40-oct,40-oct,41-oct, 41-oct, 41-oct,  41-oct,40-oct, 40-oct, 40-oct, 40-oct,38-oct, 38-oct, 38-oct,40-oct, 38-oct, 38-oct, 43-oct,43-oct};
            
        rhythm a= new rhythm();
        //ac bass drum=35 ac snare=38 hi/high mid/low mid/low tom/lft/hft=50/48/47/45/43/41  cymbal=49 closed/open hi hat= 42/46
        Thread d= new Thread(() ->{
            int du1[]= {35,35,38,42,38,42,38,42 ,38,35,38};
            int tom=48;
            int du2[]={35,35,38, 35,38,35,38 ,35,35, tom,38,tom,tom,49, 38, 42,38,42,38,42, 38, 35,38};
            int du3[]={35,35,38,42,38,42,38,42 ,38,35,38, /*10*/   /*to be continued du4*/35,35,38, 35,38,35,38, 35,35, 38,35,38,38,    35,35,38, 35,38,35,38 ,35,35, tom,38,tom,tom,49};
            int h=du1.length*3;// 11-17 300, 12-13 150,
            int h1=du2.length;
            int h2=du3.length;
            a.ch.programChange(0);
            try{
            for( ; ;){
            for(int i=0;i<h  ;i++){
                a.gch.noteOn(du1[i%du1.length],100);
                
                if(i%du1.length!=8){
                Thread.sleep((i%du1.length<=2 || i%du1.length==7)?300:150);}
                else 
                Thread.sleep(300);
                
                a.gch.noteOff(du1[i%du1.length]);
            }
            for(int i=0;i<h1 ;i++){
                a.gch.noteOn(du2[i%du2.length],100);
                
                if(i%du2.length==13)
                Thread.sleep(600);
                else if(i%du2.length==14 || i%du2.length==19 || i%du2.length==20)
                Thread.sleep(300);
                else if(i%du2.length!=8 || i%du2.length!=7)
                Thread.sleep((i%du2.length<3)?300:150);               
                else
                Thread.sleep(75);
                 
                a.gch.noteOff(du2[i%du2.length]);
            }
            for(int i=0; i<h2; i++){
             a.gch.noteOn(du3[i%du3.length],100);
             
             if(i%du3.length!=8 && i%du3.length<=10){
                Thread.sleep((i%du3.length<=2 || i%du3.length==7)?300:150);}
                else if(i%du3.length>10 && i%du3.length<=13){
                Thread.sleep(300);                
                }else if(i%du3.length>13 && i%du3.length<=17 ){
                Thread.sleep(150);
                }else if(i%du3.length>=18 && i%du3.length<=19){
                Thread.sleep(75);
                }else if(i%du3.length>19 && i%du3.length<=23){
                Thread.sleep(150);
                }else if(i%du3.length>=24 && i%du3.length<=26){
                Thread.sleep(300);
                }else if(i%du3.length>=27 && i%du3.length<=30){
                    Thread.sleep(150);
                }else if(i%du3.length>=31 && i%du3.length<=32){
                    Thread.sleep(75);
                }else 
                Thread.sleep(150);

             
            
            a.gch.noteOff(du3[i%du3.length]);
        }
        }}catch(InterruptedException e){
              System.out.println("Error");
            }
        });
        Thread b= new Thread(() ->{try{
        for(int i=0; ;i++){
        a.ch.noteOn(bass[i%bass.length],100);
        Thread.sleep(300);
        a.ch.noteOff(bass[i%bass.length]);           
        
        }}catch(Exception e){System.out.println("Error");}});
       b.start();
       d.start();
        
        
    }}

class rhythm{
    private Synthesizer synth;
    public MidiChannel gch;
    public MidiChannel ch;
    public rhythm(){
        try{
            this.synth= MidiSystem.getSynthesizer();
            this.synth.open();
            MidiChannel[] ac= this.synth.getChannels();
            this.gch= ac[9];
            this.ch=ac[1];
            
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    public void shutdown(){
        if(this.synth.isOpen() && this.synth!=null){
            this.synth.close();
        }
    }
}

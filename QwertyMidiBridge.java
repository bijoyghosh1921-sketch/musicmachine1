import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class QwertyMidiBridge {
    
    private final Map<Integer, Integer> keyMap = new HashMap<>();

    public QwertyMidiBridge() {
        // 1. Initialize your key mappings here using KeyEvent constants
        // Example: Home row mapping starting at Middle C (MIDI 60)
        keyMap.put(KeyEvent.VK_A, 60); // C4
        keyMap.put(KeyEvent.VK_W, 61); // C#4
        keyMap.put(KeyEvent.VK_S, 62); // D4
        keyMap.put(KeyEvent.VK_E, 63); // D#4
        keyMap.put(KeyEvent.VK_D, 64); // E4
        keyMap.put(KeyEvent.VK_F, 65); // F4
        keyMap.put(KeyEvent.VK_T, 66); // F#4
        keyMap.put(KeyEvent.VK_G, 67); // G4
        keyMap.put(KeyEvent.VK_Y, 68); // G#4
        keyMap.put(KeyEvent.VK_H, 69); // A4
        keyMap.put(KeyEvent.VK_U, 70); // A#4
        keyMap.put(KeyEvent.VK_J, 71); // B4
        keyMap.put(KeyEvent.VK_K, 72); // C5

        // Setup a basic window to catch keyboard focus
        JFrame frame = new JFrame("QWERTY Synth Bridge");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 2. Add the KeyListener logic
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                
                // Check if the pressed key is in our map
                if (keyMap.containsKey(keyCode)) {
                    // !e.isAutoRepeat() stops the OS from spamming triggers when holding a key
                    if (!e.isAutoRepeat()) {
                        int midiNote = keyMap.get(keyCode);
                        triggerNoteOn(midiNote);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                
                if (keyMap.containsKey(keyCode)) {
                    int midiNote = keyMap.get(keyCode);
                    triggerNoteOff(midiNote);
                }
            }
        });

        frame.setFocusable(true);
        frame.setVisible(true);
    }

    private void triggerNoteOn(int note) {
        System.out.println("NOTE ON: " + note);
        // TODO: Pass this to your MonophonicVoiceController stack
    }

    private void triggerNoteOff(int note) {
        System.out.println("NOTE OFF: " + note);
        // TODO: Pass this to your MonophonicVoiceController stack
    }

    public static void main(String[] args) {
        // Run on the Swing Event Dispatch Thread
        SwingUtilities.invokeLater(QwertyMidiBridge::new);
    }
}

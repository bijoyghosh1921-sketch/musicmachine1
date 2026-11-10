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
        channel.programChange(40);
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
    }
}

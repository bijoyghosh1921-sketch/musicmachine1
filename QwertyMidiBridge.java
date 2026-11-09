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
                        + "A W S E D F T G Y H U J K<br>"
                        + "C4 through C5</center></html>",
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
        keyMap.put(java.awt.event.KeyEvent.VK_A, 60); // C4
        keyMap.put(java.awt.event.KeyEvent.VK_W, 61); // C#4
        keyMap.put(java.awt.event.KeyEvent.VK_S, 62); // D4
        keyMap.put(java.awt.event.KeyEvent.VK_E, 63); // D#4
        keyMap.put(java.awt.event.KeyEvent.VK_D, 64); // E4
        keyMap.put(java.awt.event.KeyEvent.VK_F, 65); // F4
        keyMap.put(java.awt.event.KeyEvent.VK_T, 66); // F#4
        keyMap.put(java.awt.event.KeyEvent.VK_G, 67); // G4
        keyMap.put(java.awt.event.KeyEvent.VK_Y, 68); // G#4
        keyMap.put(java.awt.event.KeyEvent.VK_H, 69); // A4
        keyMap.put(java.awt.event.KeyEvent.VK_U, 70); // A#4
        keyMap.put(java.awt.event.KeyEvent.VK_J, 71); // B4
        keyMap.put(java.awt.event.KeyEvent.VK_K, 72); // C5
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

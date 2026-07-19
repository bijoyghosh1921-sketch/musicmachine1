# Java MIDI Music Player

A simple Java program that plays a custom song using the built-in `javax.sound.midi` library. I built this to figure out how to sequence different instruments (drums, bass, synth, and guitar) and make them play together in sync using loops and arrays.

## What it does
- Plays a backing track loop with drums, bass, and synth.
- Switches over to a faster guitar solo section midway through.
- Uses math (`step / 2`, `step / 4`, and modulos) to map out note lengths and keep the instruments locked to the beat.
- Safe from array boundary crashes during long loops.

## How to run it
1. Open the project in BlueJ (or any Java IDE).
2. Compile `song1.java`.
3. Right-click the class and run the `main` method. 
4. Make sure your computer volume is up!

## Future plans
Thinking about adding custom tempos, better drum patterns, or making it easier to change the notes without rewriting the arrays.

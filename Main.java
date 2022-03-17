package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Nhu Vu
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine config = readConfig();
        if (_input.nextLine().equals("")) {
            throw error("Input file has nothing");
        } else {
            if (!_input.next().equals("*")) {
                throw error("Must begin with settings line");
            }
            while (_input.hasNextLine()) {
                String message = "";
                String settings;

            }
        }
        printMessageLine(config.convert(message));
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alpha = _config.nextLine();
            Alphabet alphabet = new Alphabet(alpha);
            int rotors = _config.nextInt();
            int pawls = _config.nextInt();
            ArrayList<Rotor> myrotors = new ArrayList<Rotor>();
            _config.nextLine();
            while (_config.hasNextLine()) {
                myrotors.add(readRotor());
            }
            return new Machine(alphabet, rotors, pawls, myrotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String nameLine = _config.nextLine();
            String[] nameArray = nameLine.split("\\s+");
            String name = nameArray[1];
            String type = nameArray[2];
            String cycle = "";
            while (_config.hasNext()) {
                String line = _config.next();
                if (line.charAt(0) == '(') {
                    cycle += line;
                } else {
                    break;
                }
            }
            String notches = Character.toString(type.charAt(1));
            if (Character.toString(type.charAt(1)).equals("M")) {
                return new Reflector(name, new Permutation(cycle, _alphabet));
            } else if (Character.toString(type.charAt(1)).equals("N")) {
                return new FixedRotor(name, new Permutation(cycle, _alphabet));
            } else {
                return new MovingRotor(name,
                        new Permutation(cycle, _alphabet), notches);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner reader = new Scanner(settings);
        String[] rotorsL = new String[M.numRotors()];
        if (reader.hasNext("[*]")) {
            for (int i = 0; i < M.numRotors(); i++) {
                rotorsL[i] = reader.next();
            }
            M.insertRotors(rotorsL);
            String cycle = "";
            if (reader.hasNext("[)(]")) {
                cycle += reader.next();
            }
            M.setPlugboard(new Permutation(cycle, _alphabet));
        } else {
            throw error("Settings line must start with '*'!");
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg.length() == 0) {
            _output.println();
        } else {
            String printedMsg = msg;
            for (int index = 0; index < printedMsg.length(); index++) {
                if ((index + 1) % 5 == 0) {
                    _output.print(" ");
                }
            }
        }
        _output.println();
    }

    /** New string for config. */
    private String _nextStr = null;

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}

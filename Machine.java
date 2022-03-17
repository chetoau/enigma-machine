package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Nhu Vu
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _rotors = new Rotor[numRotors()];
        _allRotors = new ArrayList<Rotor>();
        _allRotors.addAll(0, allRotors);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (rotors.length != numRotors()) {
            throw error("Wrong number of rotors.");
        }
        for (int i = 0; i < rotors.length; i++) {
            for (int j = 0; j < _allRotors.size(); j++) {
                if (rotors[i].equals(_allRotors.get(j).name())) {
                    _rotors[i] = _allRotors.get(j);
                } else {
                    continue;
                }
            }
        }

    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int index = 1; index < _rotors.length; index++) {
            if (!_alphabet.contains(setting.charAt(index - 1))) {
                throw error("Invalid setting!");
            }
            _rotors[index].set(setting.charAt(index - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard =  plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int permuted = _plugboard.permute(c);
        boolean moveable = _rotors[numRotors() - 1].atNotch();
        boolean moved = true;
        _rotors[numRotors() - 1].advance();
        for (int i = numRotors() - 2; i >= 0; i--) {
            if (moveable && moved) {
                moveable = _rotors[i].atNotch();
                _rotors[i].advance();
            } else {
                moved = false;
            }
        }
        for (int index = numRotors() - 1; index >= 0; index--) {
            permuted = _rotors[index].convertForward(permuted);
        }
        for (int index = 1; index < numRotors(); index++) {
            permuted = _rotors[index].convertBackward(permuted);
        }
        permuted = _plugboard.permute(permuted);
        return permuted;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        int conversion;
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            conversion = convert(_alphabet.toInt(msg.charAt(i)));
            char convertStr = _alphabet.toChar(conversion);
            result += Character.toString(convertStr);
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotor slots. */
    private final int _numRotors;

    /** Collection of all rotors. */
    private ArrayList<Rotor> _allRotors;

    /** Number of pawls. */
    private final int _pawls;

    /** My plugboard. */
    private Permutation _plugboard;

    /** Arraylist containing rotors that will be used. */
    private Rotor[] _rotors;

}

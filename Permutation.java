package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Nhu Vu
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;

        if (_cycles.equals("()")) {
            throw error("Cycles are invalid!");
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return this._alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int i = this.wrap(p);
        int j = _cycles.indexOf(_alphabet.toChar(i));
        if (_cycles.isBlank()) {
            return i;
        } else if (!_cycles.contains(Character.toString(_alphabet.toChar(i)))) {
            return i;
        } else if (_cycles.charAt(j - 1) == '('
                && _cycles.charAt(j + 1) == ')') {
            return i;
        } else if (_cycles.charAt(j - 1) == '('
                && _alphabet.contains(_cycles.charAt(j + 1))) {
            return _alphabet.toInt(_cycles.charAt(j + 1));
        } else if (_cycles.charAt(j + 1) == ')') {
            while (_cycles.charAt(j - 1) != '(') {
                j--;
            }
            return _alphabet.toInt(_cycles.charAt(j));
        } else {
            return _alphabet.toInt(_cycles.charAt(j + 1));
        }
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int i = this.wrap(c);
        int j = _cycles.indexOf(_alphabet.toChar(i));
        if (_cycles.isBlank()) {
            return i;
        } else if (!_cycles.contains(Character.toString(_alphabet.toChar(i)))) {
            return i;
        } else if (_cycles.charAt(j - 1) == '('
                && _cycles.charAt(j + 1) == ')') {
            return i;
        } else if (_cycles.charAt(j + 1) == ')'
                && _alphabet.contains(_cycles.charAt(j - 1))) {
            return _alphabet.toInt(_cycles.charAt(j - 1));
        } else if (_cycles.charAt(j - 1) == '(') {
            while (_cycles.charAt(j + 1) != ')') {
                j++;
            }
            return _alphabet.toInt(_cycles.charAt(j));
        } else {
            return _alphabet.toInt(_cycles.charAt(j - 1));
        }
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int index = this._alphabet.toInt(p);
        int result = this.permute(index);
        return this._alphabet.toChar(result);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int index = this._alphabet.toInt(c);
        int result = this.invert(index);
        return this._alphabet.toChar(result);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return this._alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < this._alphabet.size(); i++) {
            char c = this._alphabet.toChar(i);
            if (this._cycles.indexOf(c) == -1) {
                return false;
            } else if (this.permute(c) == c) {
                return false;
            }
        }
        return true;
    }


    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** String containing cycles of this permutation. */
    private String _cycles;

}


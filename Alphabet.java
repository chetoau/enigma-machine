package enigma;
import static enigma.EnigmaException.*;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Nhu Vu
 */
class Alphabet {

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _size = chars.length();
        _chars = chars;

        if (_size == 0) {
            throw error("Alphabet cannot be empty!");
        }
        if (this.contains(' ')) {
            throw error("Cannot contain whitespace!");
        }
        if (this.duplicates()) {
            throw error("Cannot contain duplicate chars!");
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _size;
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _chars.indexOf(ch) != -1;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _chars.charAt(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (this.contains(ch)) {
            return _chars.indexOf(ch);
        } else {
            throw error("Character must be in Alphabet!");
        }
    }

    /** Checks for duplicates.
     * @return true for duplicates in Alphabet.
     */
    boolean duplicates() {
        for (int index = 0; index < this.size(); index++) {
            for (int next = index + 1; next < this.size(); next++) {
                if (this.toChar(index) == this.toChar(next)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** String containing all chars of Alphabet. */
    private String _chars;

    /** Size of Alphabet. */
    private int _size;

}


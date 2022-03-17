package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Nhu Vu
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    /** Makes sure Alphabet does not contain duplicates. */
    @Test(expected = EnigmaException.class)
    public void testDuplicates() {
        Alphabet alphabet = new Alphabet("ABCLDFL");
        Permutation perm1 = new Permutation("(ABCLDFL)", alphabet);
        perm1.size();
        perm1.permute('L');
    }

    /** Makes sure that ALphabet used to permute through
     * does not contain whitespace. */
    @Test(expected = EnigmaException.class)
    public void testWhiteSpace() {
        Alphabet alphabet = new Alphabet("$ 89AF G");
        Permutation permu = new Permutation("($) (89AF) (G)", alphabet);
        permu.size();
        permu.permute(1);
    }

    /** Test cases for size() method. */
    @Test
    public void testSize() {
        Permutation newP = new Permutation("(AB$2OL)", new Alphabet("AB$2OL"));
        assertEquals(6, newP.size());

        Permutation permu = new Permutation("($)", new Alphabet("$"));
        assertEquals(1, permu.size());

    }

    /** Test cases for derangements. */
    @Test
    public void testDerangement() {
        Alphabet alphabet = new Alphabet("AJUF");
        Permutation perm1 = new Permutation("(AJUF)", alphabet);
        assertTrue(perm1.derangement());

        Alphabet alpha1 = new Alphabet("342Sj9T");
        Permutation perm2 = new Permutation("(342Sj9) (T)", alpha1);
        assertFalse(perm2.derangement());

        Alphabet alpha2 = new Alphabet("ABCDEFGH");
        Permutation perm3 = new Permutation("(ABCD) (EFG)", alpha2);
        assertFalse(perm3.derangement());

        Alphabet alpha3 = new Alphabet("4532SAF");
        Permutation perm4 = new Permutation("(45) (32) (AF)", alpha3);
        assertFalse(perm4.derangement());
    }

    /** Test cases for both permute methods. */
    @Test
    public void testPermute() {
        Alphabet alphabet = new Alphabet("12HJQWST4R");
        Permutation perm1 = new Permutation("(12HJQW) (S) (T4R)", alphabet);
        assertEquals(6, perm1.permute(6));
        assertEquals(0, perm1.permute(5));
        assertEquals(1, perm1.permute(0));
        assertEquals('S', perm1.permute('S'));
        assertEquals('J', perm1.permute('H'));
        assertEquals('1', perm1.permute('W'));
        assertEquals('T', perm1.permute('R'));

        Alphabet alpha1 = new Alphabet("1SNA$");
        Permutation perm2 = new Permutation("(1) (S) (N) (A) ($)", alpha1);
        assertEquals(0, perm2.permute(0));
        assertEquals(3, perm2.permute(3));
        assertEquals('N', perm2.permute('N'));
        assertEquals('$', perm2.permute('$'));

        Alphabet alpha2 = new Alphabet("ABFKJ123^4*&%");
        Permutation perm3 = new Permutation("(ABFJ) (1234) (*&%)", alpha2);
        assertEquals(3, perm3.permute(3));
        assertEquals(8, perm3.permute(8));
        assertEquals(4, perm3.permute(2));

        Alphabet alpha3 = new Alphabet("HCDABE");
        Permutation perm4 = new Permutation("(ABCDE)", alpha3);
        assertEquals(3, perm4.permute(5));

    }

    /** Test cases for both invert methods. */
    @Test
    public void testInvert() {
        Alphabet alphabet = new Alphabet("12HJQWST4R");
        Permutation perm1 = new Permutation("(12HJQW) (S) (T4R)", alphabet);
        assertEquals(6, perm1.invert(6));
        assertEquals(4, perm1.invert(5));
        assertEquals(5, perm1.invert(0));
        assertEquals('R', perm1.invert('T'));
        assertEquals('Q', perm1.invert('W'));

        Alphabet alpha1 = new Alphabet("1SNA$");
        Permutation perm2 = new Permutation("(1) (S) (N) (A) ($)", alpha1);
        assertEquals(0, perm2.invert(0));
        assertEquals(3, perm2.invert(3));
        assertEquals('N', perm2.invert('N'));
        assertEquals('$', perm2.invert('$'));


    }

}

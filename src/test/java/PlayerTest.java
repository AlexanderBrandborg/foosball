import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testDefaultHandicapIs10() {
        Player player = new Player("Test", "T");
        assertEquals(10, player.getHandicap());
    }

    // TODO: Could add way more string test cases
    @Test
    void testCantSetEmptyName() {
        assertThrows(IllegalArgumentException.class, () ->  new Player("  ", "T"));
    }
    @Test
    void testCantSetEmptyInitials() {
        assertThrows(IllegalArgumentException.class, () ->  new Player("Test", "  "));
    }

    @Test
    void testInitialsMustBeContainedInName() {
        assertThrows(IllegalArgumentException.class, () ->  new Player("Alex", "BB"));
    }

    @Test
    void testInitialsCantBeLongerThanName() {
        assertThrows(IllegalArgumentException.class, () ->  new Player("Alex", "Alexa"));
    }

    @Test
    void testNamesCanContainSpaces() {
        assertDoesNotThrow(() ->  new Player("Alexander Brandborg", "Alexa"));
    }

    @Test
    void testInitialsCanContainSpaces() {
        assertDoesNotThrow(() ->  new Player("Alexander", "A X"));
    }
}
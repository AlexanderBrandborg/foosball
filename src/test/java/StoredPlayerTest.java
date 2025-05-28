import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StoredPlayerTest {
    @Test
    void testIncreaseHandicap() {
        StoredPlayer player = new StoredPlayer("1", "Test", "T", 10);
        player.increaseHandicap();
        assertEquals(11, player.getHandicap());
    }

    @Test
    void testDecreaseHandicap() {
        StoredPlayer player = new StoredPlayer("1", "Test", "T", 10);
        player.decreaseHandicap();
        assertEquals(9, player.getHandicap());
    }
}
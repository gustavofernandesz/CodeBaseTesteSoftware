package st.project;

import st.project.game.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MissionTest {
    private Room salaCalice;
    private Mission mission;
    private Player player;

    @BeforeEach
    public void setUp() {
        salaCalice = new Room("Sagrado", 2, 2);
        mission = new Mission(salaCalice);
        player = new Player(new Room("Inicio", 0, 0));
    }

    @Test
    public void testVerificarProgressoSemCalice() {
        mission.verificarProgresso(player);
        assertFalse(mission.isCaliceColetado());
        assertFalse(mission.isMissaoConcluida());
    }

    @Test
    public void testVerificarProgressoComCalice() {
        Item calice = new Item("Cálice", Item.Type.CALICE, "Objeto sagrado");
        player.adicionarItem(calice);
        mission.verificarProgresso(player);
        assertTrue(mission.isCaliceColetado());
        assertTrue(mission.isMissaoConcluida());
    }

    @Test
    public void testVerificarProgressoJaColetado() {
        // Simula já ter coletado antes
        mission.verificarProgresso(player); // sem calice
        assertFalse(mission.isCaliceColetado());
        Item calice = new Item("Cálice", Item.Type.CALICE, "Objeto sagrado");
        player.adicionarItem(calice);
        mission.verificarProgresso(player);
        assertTrue(mission.isCaliceColetado());
        assertTrue(mission.isMissaoConcluida());

        // Chama novamente, deve permanecer true
        mission.verificarProgresso(player);
        assertTrue(mission.isCaliceColetado());
        assertTrue(mission.isMissaoConcluida());
    }
}
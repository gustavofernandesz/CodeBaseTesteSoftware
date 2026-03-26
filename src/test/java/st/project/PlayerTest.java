package st.project;

import st.project.game.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Room salaA, salaB;
    private Player player;

    @BeforeEach
    public void setUp() {
        salaA = new Room("Sala A", 0, 0);
        salaB = new Room("Sala B", 0, 1);
        player = new Player(salaA);
    }

    @Test
    public void testMoverParaDestinoNull() {
        assertFalse(player.moverPara(null));
        assertEquals(salaA, player.getPosicaoAtual());
        assertEquals(1, player.getHistorico().size());
    }

    @Test
    public void testMoverParaDestinoValido() {
        assertTrue(player.moverPara(salaB));
        assertEquals(salaB, player.getPosicaoAtual());
        assertEquals(2, player.getHistorico().size());
        assertEquals(salaA, player.getHistorico().get(0));
        assertEquals(salaB, player.getHistorico().get(1));
    }

    @Test
    public void testMoverParaSalaBloqueadaSemChave() {
        salaB.setBloqueada(true);
        assertFalse(player.moverPara(salaB));
        assertEquals(salaA, player.getPosicaoAtual());
    }

    @Test
    public void testMoverParaSalaBloqueadaComChave() {
        salaB.setBloqueada(true);
        Item chave = new Item("Chave", Item.Type.CHAVE, "Chave mágica");
        player.adicionarItem(chave);
        assertTrue(player.moverPara(salaB));
        assertEquals(salaB, player.getPosicaoAtual());
    }

    @Test
    public void testPossuiItemVerdadeiro() {
        Item pocao = new Item("Poção", Item.Type.POCAO_VELOCIDADE, "Aumenta tempo");
        player.adicionarItem(pocao);
        assertTrue(player.possuiItem(Item.Type.POCAO_VELOCIDADE));
    }

    @Test
    public void testPossuiItemFalso() {
        assertFalse(player.possuiItem(Item.Type.CALICE));
    }

    @Test
    public void testUsarItemRemove() {
        Item pocao = new Item("Poção", Item.Type.POCAO_VELOCIDADE, "Aumenta tempo");
        player.adicionarItem(pocao);
        player.usarItem(pocao);
        assertFalse(player.possuiItem(Item.Type.POCAO_VELOCIDADE));
    }
}
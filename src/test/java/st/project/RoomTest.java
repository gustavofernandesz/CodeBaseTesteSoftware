package st.project;

import st.project.game.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {
    private Room room;

    @BeforeEach
    public void setUp() {
        room = new Room("Teste", 1, 1);
    }

    @Test
    public void testAdicionarERemoverItem() {
        Item item = new Item("Teste", Item.Type.CALICE, "Desc");
        room.adicionarItem(item);
        assertTrue(room.contemItem(Item.Type.CALICE));
        Item removido = room.removerItem(item);
        assertSame(item, removido);
        assertFalse(room.contemItem(Item.Type.CALICE));
    }

    @Test
    public void testGetItemPorTipo() {
        Item pocao = new Item("Poção", Item.Type.POCAO_VELOCIDADE, "Veloz");
        room.adicionarItem(pocao);
        assertEquals(pocao, room.getItemPorTipo(Item.Type.POCAO_VELOCIDADE));
        assertNull(room.getItemPorTipo(Item.Type.CHAVE));
    }

    @Test
    public void testBloqueada() {
        assertFalse(room.isBloqueada());
        room.setBloqueada(true);
        assertTrue(room.isBloqueada());
    }

    @Test
    public void testVizinhos() {
        Room viz = new Room("Viz", 0, 0);
        room.setVizinho("norte", viz);
        assertEquals(viz, room.getVizinho("norte"));
        assertNull(room.getVizinho("sul"));
    }
}
package st.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import st.project.game.Room;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomEstruturaTest {
    Room room = new Room("Sala de Teste", 0, 0);

    // isBloqueada

    @Test
    @DisplayName("Teste de Estrutura: Retorna False depois de ser bloqueada e desbloqueada")
    void testeEstruturaSalaBloqueadaDepoisDesbloqueada() {
        room.setBloqueada(true);
        room.setBloqueada(false);
        assertThat(room.isBloqueada()).isFalse();
    }
    ///////////////////////
}

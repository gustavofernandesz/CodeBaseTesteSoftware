package st.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import st.project.game.Item;
import st.project.game.Mission;
import st.project.game.Player;
import st.project.game.Room;
import static org.assertj.core.api.Assertions.assertThat;

public class MissionDominioTest {
    Room salaDoCalice = new Room("Sala Do Calice", 0, 1);
    Mission missao = new Mission(salaDoCalice);

    // verificarProgresso

    @Test
    @DisplayName("Teste de Dominio: Missao concluida é True se o jogador possui calice")
    void testeDominioJogadorPossuiCalice() {
        Item calice = new Item("Cálice Mágico", Item.Type.CALICE, "O objeto da missão");
        Player jog = new Player(salaDoCalice);
        jog.adicionarItem(calice);

        missao.verificarProgresso(jog);
        assertThat(missao.isMissaoConcluida()).isTrue();
    }

    @Test
    @DisplayName("Teste de Dominio: Missao não concluida se o jogador não possui calice")
    void testeDominioJogadorNaoPossuiCalice() {
        Player jog = new Player(salaDoCalice);

        missao.verificarProgresso(jog);
        assertThat(missao.isMissaoConcluida()).isFalse();
    }

    @Test
    @DisplayName("Teste de Dominio: Se o jogador perder o calice depois de concluir a missao a missao continua concluida")
    void testeDominioCaliceJaFoiColetado() {
        Item calice = new Item("Cálice Mágico", Item.Type.CALICE, "O objeto da missão");
        Player jog = new Player(salaDoCalice);
        jog.adicionarItem(calice);

        missao.verificarProgresso(jog);
        assertThat(missao.isMissaoConcluida()).isTrue();

        jog.removerItem(calice);

        missao.verificarProgresso(jog);
        assertThat(missao.isMissaoConcluida()).isTrue();
    }

    // isCaliceColetado

    @Test
    @DisplayName("Teste de Dominio: Se o jogador coletou o calice deve retornar True")
    void testeDominioCaliceColetado() {
        Item calice = new Item("Cálice Mágico", Item.Type.CALICE, "O objeto da missão");
        Player jog = new Player(salaDoCalice);
        jog.adicionarItem(calice);

        missao.verificarProgresso(jog);
        assertThat(missao.isCaliceColetado()).isTrue();

    }

    @Test
    @DisplayName("Teste de Dominio: Se o jogador não coletou o calice deve retornar False")
    void testeDominioCaliceNaoColetado() {
        Player jog = new Player(salaDoCalice);

        missao.verificarProgresso(jog);
        assertThat(missao.isCaliceColetado()).isFalse();
    }

    // getSalaCalice

    @Test
    @DisplayName("Teste de Dominio: Retorna a sala que guarda o calice")
    void testeDominioRetornaSalaCalice() {
        assertThat(missao.getSalaCalice()).isEqualTo(salaDoCalice);
    }
    ///////////////////////
}

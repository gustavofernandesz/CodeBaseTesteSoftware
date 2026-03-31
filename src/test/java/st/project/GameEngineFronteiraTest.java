package st.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import st.project.game.GameEngine;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GameEngineFronteiraTest {

    GameEngine gameEngine = new GameEngine(null);

//    // Verifica limite de movimentos antes de tentar mover
//        if (movimentosRestantes <= 0) {
//        encerrarJogo(false);
//        return false;
//    }

    @Test
    @DisplayName("Teste de Fronteira: deve retornar false se movimentos == 0")
    void TestFronteiraNaoMoverSeMovimentosZero(){
        gameEngine.setJogoAtivo(true);
        gameEngine.setMovimentosRestantes(0);
        boolean moveu = gameEngine.moverJogador("sul"); // direção válida

        assertThat(moveu).isFalse();
    }
    @Test
    @DisplayName("Teste de Fronteira: deve retornar false se movimentos == 1")
    void TestFronteiraMoverSeMovimentosUm(){
        gameEngine.setJogoAtivo(true);
        gameEngine.setMovimentosRestantes(1);
        boolean moveu = gameEngine.moverJogador("sul"); // direção válida

        assertThat(moveu).isTrue();
    }
    @Test
    @DisplayName("Teste de Fronteira: deve retornar false se movimentos == 1")
    void TestFronteiraMoverSeMovimentosMenorZero(){
        gameEngine.setJogoAtivo(true);
        gameEngine.setMovimentosRestantes(-1);
        boolean moveu = gameEngine.moverJogador("sul"); // direção válida

        assertThat(moveu).isFalse();
    }
}

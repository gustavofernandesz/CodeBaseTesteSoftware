package st.project;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import st.project.game.GameEngine;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GameEngineDominioTest {

GameEngine gameEngine = new GameEngine(null);


    @Test
    @DisplayName("Teste de domínio: deve retornar falso caso não possa mover")
    void testeDominioNaoMover() {
        gameEngine.setJogoAtivo(false);
        assertThat(gameEngine.moverJogador("sul")).isFalse();
    }

    @Test
    @DisplayName("Teste de domínio: deve retornar verdadeiro caso respeite as regras e possa mover")
    void testeDominioDeveMover() {

        boolean moveu = gameEngine.moverJogador("sul"); // direção válida

        assertThat(moveu).isTrue();
    }





    @Test
    @DisplayName("Teste de domínio: deve retornar verdadeiro caso respeite as regras e possa mover")
    void testeDominioNaoDeveMoverSemMovimentosRestantes() {
        gameEngine.setJogoAtivo(true);
        for (int i = 0; i < 10; i++) {
            gameEngine.moverJogador("sul");
        }

        boolean moveu = gameEngine.moverJogador("sul"); // direção válida

        assertThat(moveu).isFalse();
    }
    @Test
    @DisplayName("Teste de domínio: deve decrementar movimentos ao mover para um lugar válido")
    void testeDominioDeveDecrementarMovimentos() {
        gameEngine.setMovimentosRestantes(3);
        boolean moveu = gameEngine.moverJogador("leste"); // direção válida

        assertThat(gameEngine.getMovimentosRestantes()).isEqualTo(2);
    }

    @Test
    @DisplayName("Teste de domínio: deve decrementar movimentos ao mover para um lugar válido")
    void deveEncerrarQuandoMovimentosZeram() {
       gameEngine.setMovimentosRestantes(1);

        gameEngine.moverJogador("sul");

        assertThat(gameEngine.isJogoAtivo()).isFalse();
    }
    }







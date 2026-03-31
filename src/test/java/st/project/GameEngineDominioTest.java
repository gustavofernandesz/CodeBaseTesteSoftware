package st.project;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import st.project.game.GameEngine;
import st.project.game.Player;
import st.project.game.Room;

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
    void testeDominioNãoDeveMoverSemMovimentosRestantes() {
        gameEngine.setJogoAtivo(true);
        for (int i = 0; i < 10; i++) {
            gameEngine.moverJogador("sul");
        }

        boolean moveu = gameEngine.moverJogador("sul"); // direção válida

        assertThat(moveu).isFalse();
    }
    }







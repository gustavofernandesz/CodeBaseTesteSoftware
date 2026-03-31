package st.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import st.project.game.GameEngine;
import st.project.game.Item;
import st.project.game.Room;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineNullListenerTest {

    private GameEngine engine;

    @BeforeEach
    void setUp() {
        engine = new GameEngine(null); // listener nulo
    }

    @Test
    void moverJogador_ComListenerNull_DeveExecutarSemErro() {
        // Tenta mover para uma direção válida
        Room entrada = engine.getSalas().get("entrada");
        Room vizinhoLeste = entrada.getVizinho("leste");
        assertNotNull(vizinhoLeste);

        boolean moveu = engine.moverJogador("leste");

        assertTrue(moveu);
        assertEquals(vizinhoLeste, engine.getJogador().getPosicaoAtual());
        // Não deve lançar exceção por causa do listener nulo
    }

    @Test
    void moverJogador_DirecaoInvalida_ComListenerNull_DeveRetornarFalse() {
        Room entrada = engine.getSalas().get("entrada");
        boolean moveu = engine.moverJogador("norte"); // pode ser nulo
        if (entrada.getVizinho("norte") == null) {
            assertFalse(moveu);
        } else {
            // Se existir, testa uma direção inexistente
            assertFalse(engine.moverJogador("invalida"));
        }
        assertEquals(entrada, engine.getJogador().getPosicaoAtual());
    }

    @Test
    void moverJogador_SemMovimentosRestantes_ComListenerNull_DeveEncerrarJogo() {
        engine.setMovimentosRestantes(0);
        boolean moveu = engine.moverJogador("leste");

        assertFalse(moveu);
        assertFalse(engine.isJogoAtivo());
    }

    @Test
    void moverJogador_ColetaItemComEfeito_ComListenerNull_DeveAplicarEfeito() {
        // Coloca um item de poção na sala de destino
        Room entrada = engine.getSalas().get("entrada");
        Room destino = entrada.getVizinho("leste");
        assertNotNull(destino);
        Item pocao = new Item("Poção", Item.Type.POCAO_VELOCIDADE, "Dobra tempo");
        destino.adicionarItem(pocao);

        int tempoAntes = engine.getTempoRestante();

        engine.moverJogador("leste");

        assertEquals(tempoAntes * 2, engine.getTempoRestante());
        // Não verifica listener, apenas a aplicação do efeito
    }

    @Test
    void moverJogador_ConcluiMissao_ComListenerNull_DeveEncerrarJogo() {
        // Força conclusão da missão (coloca jogador na sala sagrado com chave)
        Room sagrado = engine.getSalas().get("sagrado");
        Room vizinha = obterVizinhaDe(sagrado);
        engine.getJogador().moverPara(vizinha);

        // Dá a chave ao jogador
        engine.getJogador().adicionarItem(new Item("Chave", Item.Type.CHAVE, ""));

        String direcao = obterDirecaoPara(vizinha, sagrado);
        boolean moveu = engine.moverJogador(direcao);

        assertTrue(moveu);
        assertTrue(engine.getMissao().isMissaoConcluida());
        assertFalse(engine.isJogoAtivo());
        // Não deve lançar exceção
    }

    // Métodos auxiliares (copiados de outros testes)
    private Room obterVizinhaDe(Room room) {
        for (String dir : new String[]{"norte", "sul", "leste", "oeste"}) {
            Room viz = room.getVizinho(dir);
            if (viz != null) return viz;
        }
        throw new IllegalStateException("Sala sem vizinhos");
    }

    private String obterDirecaoPara(Room origem, Room destino) {
        for (String dir : new String[]{"norte", "sul", "leste", "oeste"}) {
            if (origem.getVizinho(dir) == destino) return dir;
        }
        throw new IllegalArgumentException("Destino não é vizinho");
    }
}
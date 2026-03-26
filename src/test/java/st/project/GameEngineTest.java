package st.project;

import st.project.game.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GameEngineTest {
    private GameEngine engine;
    private boolean jogoTerminado;
    private boolean vitoria;
    private int ultimoTempo;

    @BeforeEach
    public void setUp() {
        engine = new GameEngine(new GameEngine.TimerListener() {
            @Override
            public void onTempoAtualizado(int segundosRestantes) {
                ultimoTempo = segundosRestantes;
            }
            @Override
            public void onJogoTerminado(boolean vitoria) {
                jogoTerminado = true;
                GameEngineTest.this.vitoria = vitoria;
            }
        });
        jogoTerminado = false;
        vitoria = false;
        ultimoTempo = 60;
    }

    // 1. Jogo inativo
    @Test
    public void testMoverJogadorJogoInativo() {
        setCampoPrivado(engine, "jogoAtivo", false);
        assertFalse(engine.moverJogador("norte"));
    }

    // 2. Direção sem vizinho
    @Test
    public void testMoverJogadorDirecaoSemVizinho() {
        Room atual = engine.getJogador().getPosicaoAtual();
        atual.setVizinho("norte", null);
        assertFalse(engine.moverJogador("norte"));
    }

    // 3. Movimento válido
    @Test
    public void testMoverJogadorValido() {
        Room atual = engine.getJogador().getPosicaoAtual();
        // Tenta encontrar uma direção válida (leste, sul, norte, oeste)
        String[] direcoes = {"leste", "sul", "norte", "oeste"};
        String direcao = null;
        Room vizinho = null;
        for (String d : direcoes) {
            vizinho = atual.getVizinho(d);
            if (vizinho != null) {
                direcao = d;
                break;
            }
        }
        assertNotNull(direcao, "Não há vizinho para testar movimento");
        assertTrue(engine.moverJogador(direcao));
        assertEquals(vizinho, engine.getJogador().getPosicaoAtual());
        assertEquals(2, engine.getJogador().getHistorico().size());
    }

    // 4. Sala bloqueada sem chave
    @Test
    public void testMoverParaSalaBloqueadaSemChave() {
        Room salaAtual = engine.getSalas().get("torre");
        Room salaBloqueada = engine.getSalas().get("sagrado");
        salaAtual.setVizinho("leste", salaBloqueada);
        salaBloqueada.setBloqueada(true);
        setPosicaoJogador(salaAtual);
        assertFalse(engine.moverJogador("leste"));
        assertEquals(salaAtual, engine.getJogador().getPosicaoAtual());
    }

    // 5. Sala bloqueada com chave
    @Test
    public void testMoverParaSalaBloqueadaComChave() {
        Room salaAtual = engine.getSalas().get("torre");
        Room salaBloqueada = engine.getSalas().get("sagrado");
        salaAtual.setVizinho("leste", salaBloqueada);
        salaBloqueada.setBloqueada(true);
        setPosicaoJogador(salaAtual);
        Item chave = new Item("Chave", Item.Type.CHAVE, "Chave mágica");
        engine.getJogador().adicionarItem(chave);
        assertTrue(engine.moverJogador("leste"));
        assertEquals(salaBloqueada, engine.getJogador().getPosicaoAtual());
        assertTrue(engine.getJogador().possuiItem(Item.Type.CHAVE));
    }

    // 6. Coleta de poção de velocidade
    @Test
    public void testColetaItemPocaoVelocidade() {
        Room salaPocao = engine.getSalas().get("cozinha");
        setPosicaoJogador(salaPocao);
        int tempoAntes = engine.getTempoRestante();
        // Forçar coleta: mover de uma adjacente
        Room adj = salaPocao.getVizinho("norte");
        if (adj == null) {
            adj = new Room("Adj", salaPocao.getX(), salaPocao.getY() - 1);
            salaPocao.setVizinho("norte", adj);
        }
        setPosicaoJogador(adj);
        engine.moverJogador("sul");
        assertEquals(tempoAntes * 2, engine.getTempoRestante());
    }

    // 7. Coleta de amuleto
    @Test
    public void testColetaAmuletoVisao() {
        Room salaAmuleto = engine.getSalas().get("jardim");
        assertFalse(engine.isAmuletoAtivo());
        Room adj = salaAmuleto.getVizinho("norte");
        if (adj == null) {
            adj = new Room("Adj", salaAmuleto.getX(), salaAmuleto.getY() - 1);
            salaAmuleto.setVizinho("norte", adj);
        }
        setPosicaoJogador(adj);
        engine.moverJogador("sul");
        assertTrue(engine.isAmuletoAtivo());
    }

    // 8. Conclusão da missão
    @Test
    public void testMissaoConcluida() {
        Room salaCalice = engine.getSalas().get("sagrado");
        Item chave = new Item("Chave", Item.Type.CHAVE, "");
        engine.getJogador().adicionarItem(chave);
        Room adj = salaCalice.getVizinho("norte");
        if (adj == null) {
            adj = new Room("Adj", salaCalice.getX(), salaCalice.getY() - 1);
            salaCalice.setVizinho("norte", adj);
        }
        setPosicaoJogador(adj);
        engine.moverJogador("sul");
        assertTrue(jogoTerminado);
        assertTrue(vitoria);
    }

    // 9. Tempo esgotado
    @Test
    public void testTempoEsgotado() {
        setCampoPrivado(engine, "tempoRestante", 1);
        Timer timer = getCampoPrivado(engine);
        for (ActionListener al : timer.getActionListeners()) {
            al.actionPerformed(new ActionEvent(engine, 0, ""));
        }
        assertTrue(jogoTerminado);
        assertFalse(vitoria);
    }

    // Métodos auxiliares
    private void setCampoPrivado(Object obj, String nomeCampo, Object valor) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(nomeCampo);
            field.setAccessible(true);
            field.set(obj, valor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getCampoPrivado(Object obj) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField("timer");
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPosicaoJogador(Room room) {
        try {
            java.lang.reflect.Field field = Player.class.getDeclaredField("posicaoAtual");
            field.setAccessible(true);
            field.set(engine.getJogador(), room);
            engine.getJogador().getHistorico().push(room);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
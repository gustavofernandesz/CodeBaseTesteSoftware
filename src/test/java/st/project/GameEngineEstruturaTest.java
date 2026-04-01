package st.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import st.project.game.GameEngine;
import st.project.game.Item;
import st.project.game.Room;

import javax.swing.Timer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameEngineEstruturaTest {

    @Mock
    private GameEngine.TimerListener listenerMock;

    private GameEngine engine;

    @BeforeEach
    void setUp() {
        engine = new GameEngine(listenerMock);
    }

    @Test
    @DisplayName("Teste de Estrutura: Inicialização deve criar mapa com 25 salas")
    void testeEstruturaInicializacaoMapa25Salas() {
        Map<String, Room> salas = engine.getSalas();
        assertEquals(25, salas.size());
    }

    @Test
    @DisplayName("Teste de Estrutura: Sala sagrado deve iniciar bloqueada")
    void testeEstruturaInicializacaoSalaSagradoBloqueada() {
        Room sagrado = engine.getSalas().get("sagrado");
        assertTrue(sagrado.isBloqueada());
    }

    @Test
    @DisplayName("Teste de Estrutura: Adjacências iniciais do mapa devem estar corretas")
    void testeEstruturaInicializacaoAdjacenciasCorretas() {
        // Verifica itens nas salas específicas
        assertTrue(engine.getSalas().get("biblioteca").getItems().stream()
                .anyMatch(i -> i.getTipo() == Item.Type.CHAVE));
        assertTrue(engine.getSalas().get("cozinha").getItems().stream()
                .anyMatch(i -> i.getTipo() == Item.Type.POCAO_VELOCIDADE));
        assertTrue(engine.getSalas().get("jardim").getItems().stream()
                .anyMatch(i -> i.getTipo() == Item.Type.AMULETO_VISAO));
        assertTrue(engine.getSalas().get("sagrado").getItems().stream()
                .anyMatch(i -> i.getTipo() == Item.Type.CALICE));
    }

    @Test
    void inicializacao_AdjacenciasDevemEstarCorretas() {
        // Testa algumas adjacências esperadas no grid 5x5
        Room entrada = engine.getSalas().get("entrada");
        // Entrada está na posição (0,0) -> só vizinhos leste e sul
        assertNotNull(entrada.getVizinho("leste"));
        assertNotNull(entrada.getVizinho("sul"));
        assertNull(entrada.getVizinho("norte"));
        assertNull(entrada.getVizinho("oeste"));
    }

    @Test
    @DisplayName("Teste de Estrutura: Listener deve ser notificado ao movimentar")
    void testeEstruturaListenerNotificaMovimento() {
        engine.moverJogador("leste");
        verify(listenerMock).onMovimentoRealizado(6);
    }

    @Test
    @DisplayName("Teste de Estrutura: Listener deve ser notificado a cada tick do timer")
    void testeEstruturaListenerNotificaTempoAoDispararTimer() throws Exception {
        Timer timer = getTimerFromEngine(engine);
        // Simula um tick do timer
        fireTimerAction(timer);
        verify(listenerMock).onTempoAtualizado(59);
    }

    @Test
    @DisplayName("Teste de Estrutura: Listener deve ser notificado ao esgotar o tempo")
    void testeEstruturaListenerNotificaFimDeJogoQuandoTempoZera() throws Exception {
        Timer timer = getTimerFromEngine(engine);
        // Dispara 60 vezes
        for (int i = 0; i < 60; i++) {
            fireTimerAction(timer);
        }
        verify(listenerMock).onJogoTerminado(false);
    }

    @Test
    @DisplayName("Teste de Estrutura: Após jogo encerrado, movimentos devem ser ignorados")
    void testeEstruturaAposEncerrarJogoMovimentosIgnorados() {
        engine.setJogoAtivo(false);
        boolean moveu = engine.moverJogador("leste");
        assertFalse(moveu);
        verify(listenerMock, never()).onMovimentoRealizado(anyInt());
    }

    @Test
    @DisplayName("Teste de Estrutura: Ao encerrar jogo, timer deve parar de atualizar")
    void testeEstruturaAoEncerrarJogoTimerPara() throws Exception {
        Timer timer = getTimerFromEngine(engine);
        // Encerra o jogo
        engine.setJogoAtivo(false);
        // Simula um tick do timer (não deve afetar)
        fireTimerAction(timer);
        // Como jogo não está ativo, tempo não deve diminuir
        assertEquals(60, engine.getTempoRestante());
        verify(listenerMock, never()).onTempoAtualizado(anyInt());
    }

    @Test
    @DisplayName("Teste de Estrutura: Chave não deve ser consumida ao entrar no sagrado")
    void testeEstruturaChaveNaoConsumidaAoEntrarNoSagrado() {
        // Dá a chave ao jogador
        engine.getJogador().adicionarItem(new Item("Chave", Item.Type.CHAVE, ""));
        Room sagrado = engine.getSalas().get("sagrado");
        Room vizinha = obterVizinhaDe(sagrado);
        engine.getJogador().moverPara(vizinha);
        String direcao = obterDirecaoPara(vizinha, sagrado);

        engine.moverJogador(direcao);

        // A chave ainda deve estar no inventário
        assertTrue(engine.getJogador().possuiItem(Item.Type.CHAVE));
    }

    // Métodos auxiliares para navegação
    private Room obterVizinhaDe(Room room) {
        for (String dir : new String[]{"norte", "sul", "leste", "oeste"}) {
            Room viz = room.getVizinho(dir);
            if (viz != null) return viz;
        }
        throw new IllegalStateException("Sala não tem vizinhos?");
    }

    private String obterDirecaoPara(Room origem, Room destino) {
        for (String dir : new String[]{"norte", "sul", "leste", "oeste"}) {
            if (origem.getVizinho(dir) == destino) return dir;
        }
        throw new IllegalArgumentException("Destino não é vizinho");
    }

    // --- Utilitários para manipular o Timer interno ---
    private Timer getTimerFromEngine(GameEngine engine) throws Exception {
        Field field = GameEngine.class.getDeclaredField("timer");
        field.setAccessible(true);
        return (Timer) field.get(engine);
    }

    private void fireTimerAction(Timer timer) throws Exception {
        Class<?> timerClass = timer.getClass();
        Method getListeners = timerClass.getDeclaredMethod("getListeners", Class.class);
        getListeners.setAccessible(true);
        Object[] listeners = (Object[]) getListeners.invoke(timer, Class.forName("java.awt.event.ActionListener"));
        if (listeners.length > 0) {
            java.awt.event.ActionListener al = (java.awt.event.ActionListener) listeners[0];
            al.actionPerformed(new java.awt.event.ActionEvent(timer, 0, null));
        }
    }
}
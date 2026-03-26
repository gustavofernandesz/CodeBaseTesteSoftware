package st.project.game;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GameEngine {
    private Map<String, Room> salas;          // mapeamento nome -> Room
    private final Player jogador;
    private final Mission missao;
    private int tempoRestante;                 // segundos
    private Timer timer;
    private boolean jogoAtivo;
    private final TimerListener timerListener;

    public interface TimerListener {
        void onTempoAtualizado(int segundosRestantes);
        void onJogoTerminado(boolean vitoria);
    }

    public GameEngine(TimerListener listener) {
        this.timerListener = listener;
        this.jogoAtivo = true;
        inicializarMapa();
        inicializarItens();
        this.jogador = new Player(salas.get("entrada"));
        this.missao = new Mission(salas.get("sagrado"));
        this.tempoRestante = 60;
        iniciarTimer();
    }

    private void inicializarMapa() {
        salas = new HashMap<>();
        // 25 salas para grid 5x5
        String[] nomes = {
                "entrada", "sala1", "sala2", "sala3", "sala4",
                "corredor", "biblioteca", "sala5", "sala6", "sala7",
                "jardim", "cozinha", "sala8", "sala9", "sala10",
                "torre", "sala11", "sala12", "sala13", "sala14",
                "sala15", "sala16", "sala17", "sala18", "sagrado"
        };
        // Criar todas as salas
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int idx = i * 5 + j;
                String nome = nomes[idx];
                Room r = new Room(nome, j, i); // x=j, y=i
                salas.put(nome, r);
            }
        }
        // Configurar adjacências (4 direções)
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int idx = i * 5 + j;
                Room r = salas.get(nomes[idx]);
                // norte
                if (i > 0) r.setVizinho("norte", salas.get(nomes[(i-1)*5 + j]));
                // sul
                if (i < 4) r.setVizinho("sul", salas.get(nomes[(i+1)*5 + j]));
                // oeste
                if (j > 0) r.setVizinho("oeste", salas.get(nomes[i*5 + (j-1)]));
                // leste
                if (j < 4) r.setVizinho("leste", salas.get(nomes[i*5 + (j+1)]));
            }
        }
        // Bloquear sala do cálice
        salas.get("sagrado").setBloqueada(true);
    }

    private void inicializarItens() {
        // Colocar itens em salas específicas
        salas.get("biblioteca").adicionarItem(new Item("Chave Encantada", Item.Type.CHAVE, "Abre a sala do cálice"));
        salas.get("cozinha").adicionarItem(new Item("Poção de Velocidade", Item.Type.POCAO_VELOCIDADE, "Dobra o tempo restante"));
        salas.get("jardim").adicionarItem(new Item("Amuleto de Visão", Item.Type.AMULETO_VISAO, "Revela localização do cálice"));
        salas.get("sagrado").adicionarItem(new Item("Cálice Mágico", Item.Type.CALICE, "O objeto da missão"));
    }

    private void iniciarTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jogoAtivo) {
                    tempoRestante--;
                    if (timerListener != null)
                        timerListener.onTempoAtualizado(tempoRestante);
                    if (tempoRestante <= 0) {
                        jogoAtivo = false;
                        timer.stop();
                        if (timerListener != null)
                            timerListener.onJogoTerminado(false);
                    }
                }
            }
        });
        timer.start();
    }

    public boolean moverJogador(String direcao) {
        if (!jogoAtivo) return false;
        Room atual = jogador.getPosicaoAtual();
        Room destino = atual.getVizinho(direcao);
        if (destino == null) return false;
        boolean moveu = jogador.moverPara(destino);
        if (moveu) {
            // Coletar itens da nova sala automaticamente
            coletarItensSala();
            // Verificar missão
            missao.verificarProgresso(jogador);
            if (missao.isMissaoConcluida()) {
                jogoAtivo = false;
                timer.stop();
                if (timerListener != null)
                    timerListener.onJogoTerminado(true);
            }
        }
        return moveu;
    }

    private void coletarItensSala() {
        Room atual = jogador.getPosicaoAtual();
        List<Item> itens = new ArrayList<>(atual.getItems());
        for (Item item : itens) {
            // Coleta automática
            atual.removerItem(item);
            jogador.adicionarItem(item);
            // Aplicar efeitos imediatos
            aplicarEfeitoItem(item);
        }
    }

    private void aplicarEfeitoItem(Item item) {
        switch (item.getTipo()) {
            case POCAO_VELOCIDADE:
                tempoRestante *= 2;
                if (timerListener != null)
                    timerListener.onTempoAtualizado(tempoRestante);
                break;
            case AMULETO_VISAO:
                // Revelar localização do cálice: pode ser tratado na GUI via listener
                // Por simplicidade, apenas ativamos um flag que a GUI pode consultar
                // Mas aqui não temos acesso direto à GUI. Deixamos para a GUI verificar
                // se o jogador possui o amuleto e mostrar o destino.
                break;
            case CHAVE:
                // A chave é usada automaticamente para destrancar a sala do cálice
                // quando o jogador tentar entrar
                // Como a sala está bloqueada, a chave só é verificada no métod moverPara
                break;
            case CALICE:
                // Missão será concluída na verificação
                break;
        }
    }

    public Player getJogador() { return jogador; }
    public Mission getMissao() { return missao; }
    public Map<String, Room> getSalas() { return salas; }
    public int getTempoRestante() { return tempoRestante; }
    public boolean isJogoAtivo() { return jogoAtivo; }
    public boolean isAmuletoAtivo() { return jogador.possuiItem(Item.Type.AMULETO_VISAO); }
}
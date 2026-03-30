package st.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import st.project.game.Item;
import st.project.game.Player;
import st.project.game.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerDominioTest {
    Room salaInicial = new Room("Sala Inicial", 0, 0);
    Player player = new Player(salaInicial);

    // getPosicaoAtual

    @Test
    @DisplayName("Teste de Dominio: Retorna a sala em que o player esta atualmente")
    void testeDominioGetPosicaoAtual() {
        assertThat(player.getPosicaoAtual()).isEqualTo(salaInicial);
    }

    // getInventario

    @Test
    @DisplayName("Teste de Dominio: Retorna os itens do inventario do player")
    void testeDominioGetInventario() {
        Item chave = new Item("Chave Encantada", Item.Type.CHAVE, "Abre a sala do cálice");
        List<Item> inv = new ArrayList<>();
        inv.add(chave);

        player.adicionarItem(chave);

        assertThat(player.getInventario()).isEqualTo(inv);
    }

    // getHistorico

    @Test
    @DisplayName("Teste de Dominio: Retorna uma stack das salas que o player passou")
    void testeDominioGetHistorico() {
        Stack<Room> hist = new Stack<>();
        hist.push(salaInicial);

        assertThat(player.getHistorico()).isEqualTo(hist);
    }

    // moverPara

    @Test
    @DisplayName("Teste de Dominio: Retorna True se player mover para outra sala")
    void testeDominioMoverPara() {
        Room segundaSala = new Room("Segunda Sala", 0, 1);
        assertThat(player.moverPara(segundaSala)).isTrue();
    }

    // adicionarItem

    @Test
    @DisplayName("Teste de Dominio: Item adicionado ao inventario do player")
    void testeDominioAdicionarItem() {
        Item amuleto = new Item("Amuleto de Visão", Item.Type.AMULETO_VISAO, "Revela localização do cálice");

        player.adicionarItem(amuleto);
        assertThat(player.getInventario().contains(amuleto)).isTrue();
    }

    // removerItem

    @Test
    @DisplayName("Teste de Dominio: Retorna o item removido do inventario")
    void testeDominioRemoverItem() {
        Item amuleto = new Item("Amuleto de Visão", Item.Type.AMULETO_VISAO, "Revela localização do cálice");

        player.adicionarItem(amuleto);
        assertThat(player.getInventario().contains(amuleto)).isTrue();

        assertThat(player.removerItem(amuleto)).isEqualTo(amuleto);

        assertThat(player.getInventario().isEmpty()).isTrue();
    }

    // possuiItem

    @Test
    @DisplayName("Teste de Dominio: Retorna True se o jogador posuir o item procurado")
    void testeDominioPossuiItem() {
        Item pocao = new Item("Poção de Velocidade", Item.Type.POCAO_VELOCIDADE, "Dobra o tempo restante");
        player.adicionarItem(pocao);
        assertThat(player.possuiItem(pocao.getTipo())).isTrue();
    }

    // usarItem

    @Test
    @DisplayName("Teste de Dominio: Item consumivel é usado e removido do inventario")
    void testeDominioUsarItem() {
        Item pocao = new Item("Poção de Velocidade", Item.Type.POCAO_VELOCIDADE, "Dobra o tempo restante");
        player.adicionarItem(pocao);
        player.usarItem(pocao);
        assertThat(player.getInventario().isEmpty()).isTrue();
    }
}
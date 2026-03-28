package st.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import st.project.game.Item;
import st.project.game.Room;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomFronteiraTest {
    Room room = new Room("Sala de Teste", 0, 0);

    // isBloqueada

    @Test
    @DisplayName("Teste de Fronteira: isBloqueada retorna false")
    void testeFronteiraSalaDesbloqueada() {
        assertThat(room.isBloqueada()).isFalse();
    }

    @Test
    @DisplayName("Teste de Fronteira: isBloqueada retorna true")
    void testeFronteiraSalaBloqueada() {
        room.setBloqueada(true);
        assertThat(room.isBloqueada()).isTrue();
    }

    // setBloqueada

    @Test
    @DisplayName("Teste de Fronteira: Bloquear sala faz isBloqueada retornar True")
    void testeFronteiraBloquearSala() {
        assertThat(room.isBloqueada()).isFalse();
        room.setBloqueada(true);
        assertThat(room.isBloqueada()).isTrue();
    }

    @Test
    @DisplayName("Teste de Fronteira: Desbloquear sala faz isBloqueada retornar False")
    void testeFronteiraDesbloquearSala() {
        room.setBloqueada(true);
        assertThat(room.isBloqueada()).isTrue();
        room.setBloqueada(false);
        assertThat(room.isBloqueada()).isFalse();
    }

    // setVizinho / getVizinho

    @Test
    @DisplayName("Teste de Fronteira: Adicionar sala vizinha e retorna a sala vizinha quando buscado")
    void testeFronteiraAdicionarSalaVizinha() {
        Room salaVizinha = new Room("Sala Vizinha", 0, 1);
        room.setVizinho("leste", salaVizinha);
        assertThat(room.getVizinho("leste")).isEqualTo(salaVizinha);
    }

    @Test
    @DisplayName("Teste de Fronteira: Retorna null se nao possuir vizinho para direcao")
    void testeFronteiraNaoPossuiVizinhaParaDirecaoBuscada() {
        Room salaVizinha = new Room("Sala Vizinha", 0, 1);
        room.setVizinho("leste", salaVizinha);
        assertThat(room.getVizinho("sul")).isEqualTo(null);
    }

    @Test
    @DisplayName("Teste de Fronteira: Retorna null se nao possuir nenhum vizinho ")
    void testeFronteiraNaoPossuiNenhumVizinho() {
        assertThat(room.getVizinho("leste")).isEqualTo(null);
    }

    // adicionarItem

    @Test
    @DisplayName("Teste de Fronteira: Adição de item no vetor de items da sala")
    void testeFronteiraAdicaoDeItems() {
        assertThat(room.getItems().size()).isEqualTo(0);
        room.adicionarItem(new Item("Poção de Velocidade", Item.Type.POCAO_VELOCIDADE, "Dobra o tempo restante"));
        assertThat(room.getItems().size()).isEqualTo(1);
    }

    // removerItem

    @Test
    @DisplayName("Teste de Fronteira: Remoção de item no vetor da sala")
    void testeFronteiraRemocaoDeItems() {
        Item pocao = new Item("Poção de Velocidade", Item.Type.POCAO_VELOCIDADE, "Dobra o tempo restante");
        room.adicionarItem(pocao);
        assertThat(room.removerItem(pocao)).isEqualTo(pocao);
    }

    @Test
    @DisplayName("Teste de Fronteira: Remoção de item no vetor vazio")
    void testeFronteiraRemocaoEmVetorVazio() {
        Item pocao = new Item("Poção de Velocidade", Item.Type.POCAO_VELOCIDADE, "Dobra o tempo restante");
        assertThat(room.removerItem(pocao)).isEqualTo(null);
    }

    // contemItem

    @Test
    @DisplayName("Teste de Fronteira: Verficar se vetor contêm item retorna True")
    void testeFronteiraVetorContemItemBuscado() {
        Item amuleto = new Item("Amuleto de Visão", Item.Type.AMULETO_VISAO, "Revela localização do cálice");
        room.adicionarItem(amuleto);
        assertThat(room.contemItem(Item.Type.AMULETO_VISAO)).isTrue();
    }

    @Test
    @DisplayName("Teste de Fronteira: Verficar se vetor contêm item retorna False se tipo de item não estiver no vetor")
    void testeFronteiraVetorNaoContemItemBuscado() {
        Item amuleto = new Item("Amuleto de Visão", Item.Type.AMULETO_VISAO, "Revela localização do cálice");
        room.adicionarItem(amuleto);
        assertThat(room.contemItem(Item.Type.POCAO_VELOCIDADE)).isFalse();
    }

    @Test
    @DisplayName("Teste de Fronteira: Verficar se vetor contêm item retorna False com vetor vazio")
    void testeFronteiraVetorNaoContemNenhumItem() {
        assertThat(room.contemItem(Item.Type.POCAO_VELOCIDADE)).isFalse();
    }

    // getItemPorTipo

    @Test
    @DisplayName("Teste de Fronteira: Verificar item buscado retorna o item caso possuir o tipo de item buscado no vetor")
    void testeFronteiraGetItemPorTipoPossuiItemBuscado() {
        Item chave = new Item("Chave Encantada", Item.Type.CHAVE, "Abre a sala do cálice");
        room.adicionarItem(chave);
        assertThat(room.getItemPorTipo(Item.Type.CHAVE)).isEqualTo(chave);
    }

    @Test
    @DisplayName("Teste de Fronteira: Verificar item buscado retorna null se não possuir o tipo de item buscado no vetor")
    void testeFronteiraGetItemPorTipoNaoPossuiItemBuscado() {
        Item chave = new Item("Chave Encantada", Item.Type.CHAVE, "Abre a sala do cálice");
        room.adicionarItem(chave);
        assertThat(room.getItemPorTipo(Item.Type.POCAO_VELOCIDADE)).isEqualTo(null);
    }

    @Test
    @DisplayName("Teste de Fronteira: Verificar item buscado retorna null o vetor estiver vazio")
    void testeFronteiraGetItemPorTipoNaoPossuiNenhumItem() {
        assertThat(room.getItemPorTipo(Item.Type.POCAO_VELOCIDADE)).isEqualTo(null);
    }
    ///////////////////////
}

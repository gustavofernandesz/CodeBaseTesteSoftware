package st.project;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import st.project.game.Item;
import st.project.game.Room;
import static org.assertj.core.api.Assertions.assertThat;

class RoomDominioTest {
    Room room = new Room("Sala de Teste", 0, 0);

    // getters e setter padrões

    @Test
    @DisplayName("Teste de Dominio: getNome retorna String com nome")
    void testeDominioGetNome() {
        assertThat(room.getNome()).isEqualTo("Sala de Teste");
    }

    @Test
    @DisplayName("Teste de Dominio: getX retorna a posicao X da sala")
    void testeDominioGetX() {
        assertThat(room.getX()).isEqualTo(0);
    }


    @Test
    @DisplayName("Teste de Dominio: getY retorna a posicao Y da sala")
    void testeDominioGetY() {
        assertThat(room.getY()).isEqualTo(0);
    }

    // isBloqueada

    @Test
    @DisplayName("Teste de Dominio: isBloqueada retorna false")
    void testeDominioSalaDesbloqueada() {
        assertThat(room.isBloqueada()).isFalse();
    }

    @Test
    @DisplayName("Teste de Dominio: isBloqueada retorna true")
    void testeDominioSalaBloqueada() {
        room.setBloqueada(true);
        assertThat(room.isBloqueada()).isTrue();
    }

    // setBloqueada

    @Test
    @DisplayName("Teste de Dominio: Bloquear sala faz isBloqueada retornar True")
    void testeDominioBloquearSala() {
        assertThat(room.isBloqueada()).isFalse();
        room.setBloqueada(true);
        assertThat(room.isBloqueada()).isTrue();
    }

    @Test
    @DisplayName("Teste de Dominio: Desbloquear sala faz isBloqueada retornar False")
    void testeDominioDesbloquearSala() {
        room.setBloqueada(true);
        assertThat(room.isBloqueada()).isTrue();
        room.setBloqueada(false);
        assertThat(room.isBloqueada()).isFalse();
    }

    // setVizinho / getVizinho

    @Test
    @DisplayName("Teste de Dominio: Adicionar sala vizinha e retorna a sala vizinha quando buscado")
    void testeDominioAdicionarSalaVizinha() {
        Room salaVizinha = new Room("Sala Vizinha", 0, 1);
        room.setVizinho("leste", salaVizinha);
        assertThat(room.getVizinho("leste")).isEqualTo(salaVizinha);
    }

    @Test
    @DisplayName("Teste de Dominio: Retorna null se nao possuir vizinho para direcao")
    void testeDominioNaoPossuiVizinhaParaDirecaoBuscada() {
        Room salaVizinha = new Room("Sala Vizinha", 0, 1);
        room.setVizinho("leste", salaVizinha);
        assertThat(room.getVizinho("sul")).isEqualTo(null);
    }

    @Test
    @DisplayName("Teste de Dominio: Retorna null se nao possuir nenhum vizinho ")
    void testeDominioNaoPossuiNenhumVizinho() {
        assertThat(room.getVizinho("leste")).isEqualTo(null);
    }

    // adicionarItem

    @Test
    @DisplayName("Teste de Dominio: Adição de item no vetor de items da sala")
    void testeDominioAdicaoDeItems() {
        assertThat(room.getItems().size()).isEqualTo(0);
        room.adicionarItem(new Item("Poção de Velocidade", Item.Type.POCAO_VELOCIDADE, "Dobra o tempo restante"));
        assertThat(room.getItems().size()).isEqualTo(1);
    }

    // removerItem

    @Test
    @DisplayName("Teste de Dominio: Remoção de item no vetor da sala")
    void testeDominioRemocaoDeItems() {
        Item pocao = new Item("Poção de Velocidade", Item.Type.POCAO_VELOCIDADE, "Dobra o tempo restante");
        room.adicionarItem(pocao);
        assertThat(room.removerItem(pocao)).isEqualTo(pocao);
    }

    @Test
    @DisplayName("Teste de Dominio: Remoção de item no vetor vazio")
    void testeDominioRemocaoEmVetorVazio() {
        Item pocao = new Item("Poção de Velocidade", Item.Type.POCAO_VELOCIDADE, "Dobra o tempo restante");
        assertThat(room.removerItem(pocao)).isEqualTo(null);
    }

    // contemItem

    @Test
    @DisplayName("Teste de Dominio: Verficar se vetor contêm item retorna True")
    void testeDominioVetorContemItemBuscado() {
        Item amuleto = new Item("Amuleto de Visão", Item.Type.AMULETO_VISAO, "Revela localização do cálice");
        room.adicionarItem(amuleto);
        assertThat(room.contemItem(Item.Type.AMULETO_VISAO)).isTrue();
    }

    @Test
    @DisplayName("Teste de Dominio: Verficar se vetor contêm item retorna False se tipo de item não estiver no vetor")
    void testeDominioVetorNaoContemItemBuscado() {
        Item amuleto = new Item("Amuleto de Visão", Item.Type.AMULETO_VISAO, "Revela localização do cálice");
        room.adicionarItem(amuleto);
        assertThat(room.contemItem(Item.Type.POCAO_VELOCIDADE)).isFalse();
    }

    @Test
    @DisplayName("Teste de Dominio: Verficar se vetor contêm item retorna False com vetor vazio")
    void testeDominioVetorNaoContemNenhumItem() {
        assertThat(room.contemItem(Item.Type.POCAO_VELOCIDADE)).isFalse();
    }

    // getItemPorTipo

    @Test
    @DisplayName("Teste de Dominio: Verificar item buscado retorna o item caso possuir o tipo de item buscado no vetor")
    void testeDominioGetItemPorTipoPossuiItemBuscado() {
        Item chave = new Item("Chave Encantada", Item.Type.CHAVE, "Abre a sala do cálice");
        room.adicionarItem(chave);
        assertThat(room.getItemPorTipo(Item.Type.CHAVE)).isEqualTo(chave);
    }

    @Test
    @DisplayName("Teste de Dominio: Verificar item buscado retorna null se não possuir o tipo de item buscado no vetor")
    void testeDominioGetItemPorTipoNaoPossuiItemBuscado() {
        Item chave = new Item("Chave Encantada", Item.Type.CHAVE, "Abre a sala do cálice");
        room.adicionarItem(chave);
        assertThat(room.getItemPorTipo(Item.Type.POCAO_VELOCIDADE)).isEqualTo(null);
    }

    @Test
    @DisplayName("Teste de Dominio: Verificar item buscado retorna null o vetor estiver vazio")
    void testeDominioGetItemPorTipoNaoPossuiNenhumItem() {
        assertThat(room.getItemPorTipo(Item.Type.POCAO_VELOCIDADE)).isEqualTo(null);
    }
    ///////////////////////
}

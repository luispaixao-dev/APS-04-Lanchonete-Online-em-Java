import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.example.lanchonete.dao.DaoLanche;
import org.example.lanchonete.model.Lanche;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LancheDaoTestUnitario {

    @Mock
    private DaoLanche daoLanche;

    @BeforeEach
    void setUp() {
        // Configuração do mock antes de cada teste, se necessário
    }

    @Test
    void testListarTodosLanchesAtivos() {
        // Dados de exemplo
        Lanche lancheAtivo = new Lanche();
        lancheAtivo.setId_lanche(1);
        lancheAtivo.setNome("X-Burger");
        lancheAtivo.setDescricao("Hamburguer com queijo, alface e tomate.");
        lancheAtivo.setValor_venda(15.0);
        lancheAtivo.setFg_ativo(1);

        List<Lanche> lanchesAtivos = new ArrayList<>();
        lanchesAtivos.add(lancheAtivo);

        // Configuração do mock para o método listarTodos()
        when(daoLanche.listarTodos()).thenReturn(lanchesAtivos);

        // Chamada ao método do DaoLanche que lista todos os lanches
        List<Lanche> result = daoLanche.listarTodos();

        // Verificações
        assertNotNull(result);
        assertFalse(result.isEmpty(), "A lista de lanches ativos não deveria estar vazia");

        // Verifica se todos os lanches na lista estão ativos
        result.forEach(lanche -> assertEquals(1, lanche.getFg_ativo(), "Um lanche na lista não está ativo"));
    }

    @Test
    void testPesquisaPorNome() {
        // Dados de exemplo
        String nomeLanche = "X-Burger";
        Lanche lancheEncontrado = new Lanche();
        lancheEncontrado.setId_lanche(1);
        lancheEncontrado.setNome(nomeLanche);
        lancheEncontrado.setDescricao("Hamburguer com queijo, alface e tomate.");
        lancheEncontrado.setValor_venda(15.0);
        lancheEncontrado.setFg_ativo(1);

        // Configuração do mock para o método pesquisaPorNome()
        when(daoLanche.pesquisaPorNome(nomeLanche)).thenReturn(lancheEncontrado);

        // Chamada ao método do DaoLanche que pesquisa por nome
        Lanche result = daoLanche.pesquisaPorNome(nomeLanche);

        // Verificações
        assertNotNull(result);
        assertEquals(nomeLanche, result.getNome(), "O nome do lanche encontrado não corresponde");
    }
}

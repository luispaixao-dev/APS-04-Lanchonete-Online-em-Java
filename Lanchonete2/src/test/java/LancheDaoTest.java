import org.example.lanchonete.dao.DaoLanche;
import org.example.lanchonete.model.Lanche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LancheDaoTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    private DaoLanche daoLanche;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this); // Inicializa os mocks

        daoLanche = new DaoLanche();

        // Configurações de mocks para a conexão e o statement
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockStatement.execute()).thenReturn(true);

        // Configuração padrão para qualquer operação de fechamento
        doNothing().when(mockStatement).close();
        doNothing().when(mockResultSet).close();

        // Injeta a conexão mockada no DaoLanche usando reflection (não recomendado em produção)
        try {
            Field field = daoLanche.getClass().getDeclaredField("conecta");
            field.setAccessible(true);
            field.set(daoLanche, mockConnection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    void testSalvar() throws SQLException {
        // Criar um objeto de teste
        Lanche lanche = new Lanche();
        lanche.setNome("X-Burger");
        lanche.setDescricao("Delicioso lanche com hambúrguer, alface, molho especial no pão australiano.");
        lanche.setValor_venda(15.0);

        // Configurar o mock para lançar SQLException ao executar o statement
        when(mockStatement.executeUpdate()).thenThrow(new SQLException("SQL Exception"));

        // Executar o método salvar
        try {
            daoLanche.salvar(lanche);
        } catch (RuntimeException e) {
            // Verificar se a RuntimeException foi lançada devido à SQLException
            assertEquals(SQLException.class, e.getCause().getClass());
        }

        // Verificar se o método correto (PreparedStatement.setXXX) foi chamado com os parâmetros esperados
        verify(mockStatement).setString(1, lanche.getNome());
        verify(mockStatement).setString(2, lanche.getDescricao());
        verify(mockStatement).setDouble(3, lanche.getValor_venda());
        verify(mockStatement).setInt(4, 1); // Verificar se o quarto parâmetro foi 1 para fg_ativo

        // Verificar se o statement foi fechado após o uso
        verify(mockStatement).close();
    }

    @Test
    void testListarTodos() throws SQLException {
        // Criar um mock de ResultSet com dados fictícios
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Simular um resultado no ResultSet
        when(mockResultSet.getInt("id_lanche")).thenReturn(1);
        when(mockResultSet.getString("nm_lanche")).thenReturn("X-Burger");
        when(mockResultSet.getString("descricao")).thenReturn("Delicioso lanche com hambúrguer, alface, molho especial no pão australiano.");
        when(mockResultSet.getDouble("valor_venda")).thenReturn(15.0);

        // Configurar o mock para lançar SQLException ao executar a query
        when(mockStatement.executeQuery()).thenThrow(new SQLException("SQL Exception"));

        // Executar o método listarTodos
        try {
            List<Lanche> result = daoLanche.listarTodos();

            // Verificar se os métodos getters de Lanche foram chamados corretamente
            assertEquals(1, result.size());
            assertEquals("X-Burger", result.get(0).getNome());
            assertEquals("Delicioso lanche com hambúrguer, alface, molho especial no pão australiano.", result.get(0).getDescricao());
            assertEquals(15.0, result.get(0).getValor_venda());

            // Verificar se o statement e o resultSet foram fechados após o uso
            verify(mockStatement).close();
            verify(mockResultSet).close();
        } catch (RuntimeException e) {
            // Verificar se a RuntimeException foi lançada devido à SQLException
            assertEquals(SQLException.class, e.getCause().getClass());
        }
    }
}
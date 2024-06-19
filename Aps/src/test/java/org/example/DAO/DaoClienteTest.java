package org.example.DAO;

import org.example.Model.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DaoClienteTest {
    @BeforeAll
    static void setUp() {
        Cliente cliente = new Cliente();
        cliente.setUsuario("luis");
        cliente.setSenha("123");
        cliente.setFg_ativo('1');
    }

    @Test
    void login() {
        Cliente cliente = new Cliente();
        cliente.setUsuario("luis");
        cliente.setSenha("123");
        cliente.setFg_ativo('1');
        DaoCliente daoCliente = new DaoCliente();
        boolean isSuccessLogin = daoCliente.login(cliente);
        Assertions.assertTrue(isSuccessLogin);


    }
}
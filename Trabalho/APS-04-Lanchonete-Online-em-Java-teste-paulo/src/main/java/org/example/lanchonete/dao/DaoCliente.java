/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.lanchonete.dao;

import org.example.lanchonete.helper.EncryptadorMD5;
import org.example.lanchonete.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kener_000
 */
public class DaoCliente {

    private final Connection conecta;

    public DaoCliente(){
        this.conecta = new DaoUtil().conecta();
    }

    public void salvar(Cliente cliente){
        DaoClient.Save(cliente, conecta);
    }

    public List<Cliente> listarTodos(){
        return getClientes(conecta);
    }

    static List<Cliente> getClientes(Connection conecta) {
        String sql = "SELECT * FROM tb_clientes WHERE fg_Ativo='1' ORDER BY id_cliente";
        ResultSet rs;
        List<Cliente> clientes = new ArrayList<>();

        try{
            PreparedStatement stmt = conecta.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()){
                Cliente cliente = new Cliente();
                result(rs, cliente);
                clientes.add(cliente);
            }
            rs.close();
            stmt.close();
            return clientes;

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    static void result(ResultSet rs, Cliente cliente) throws SQLException {
        cliente.setId_cliente(rs.getInt("id_cliente"));
        cliente.setNome(rs.getString("nome"));
        cliente.setSobrenome(rs.getString("sobrenome"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setUsuario(rs.getString("usuario"));
        cliente.setSenha(rs.getString("senha"));
        cliente.setFg_ativo(1);
    }

    public Cliente pesquisaPorUsuario(Cliente cliente){
        String sql = "SELECT * FROM tb_clientes WHERE usuario=?";
        ResultSet rs;
        Cliente clienteResultado = new Cliente();

        try{
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setString(1, cliente.getUsuario());
            rs = stmt.executeQuery();

            if (rs.next()){
                result(rs, clienteResultado);
            }
            rs.close();
            stmt.close();
            return clienteResultado;

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Cliente pesquisaPorID(String ID){
        return getCliente(ID, conecta);
    }

    static Cliente getCliente(String ID, Connection conecta) {
        String sql = "SELECT * FROM tb_clientes WHERE id_cliente=?";
        ResultSet rs;
        Cliente clienteResultado = new Cliente();

        try{
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setString(1, ID);
            rs = stmt.executeQuery();

            if (rs.next()){
                clienteResultado.setId_cliente(rs.getInt("id_cliente"));
                clienteResultado.setNome(rs.getString("nome"));
                clienteResultado.setSobrenome(rs.getString("sobrenome"));
                clienteResultado.setTelefone(rs.getString("telefone"));
                clienteResultado.setFg_ativo(1);
            }
            rs.close();
            stmt.close();
            return clienteResultado;

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public boolean login(Cliente cliente){
        String sql = "SELECT usuario, senha, fg_ativo FROM tb_clientes WHERE usuario=?";

        try{
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setString(1, cliente.getUsuario());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                String storedPassword = rs.getString("senha");
                int fg_ativo = rs.getInt("fg_ativo");

                if(EncryptadorMD5.encryptar(cliente.getSenha()).equals(storedPassword) && fg_ativo == 1){
                    return true;
                }
            }

            rs.close();
            stmt.close();

        } catch(SQLException e){
            throw new RuntimeException(e);
        }

        return false;
    }
}
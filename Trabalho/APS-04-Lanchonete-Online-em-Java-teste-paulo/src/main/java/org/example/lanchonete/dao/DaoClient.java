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
import java.util.List;
import java.util.Objects;

import static org.example.lanchonete.dao.DaoCliente.getClientes;

/**
 *
 * @author kener_000
 */
public class DaoClient {

    private final Connection conecta;

    public DaoClient(){
        this.conecta = new DaoUtil().conecta();
    }

    public void salvar(Cliente cliente){
        Save(cliente, conecta);
    }

    static void Save(Cliente cliente, Connection conecta) {
        String sql = "INSERT INTO tb_clientes(nome, sobrenome, telefone, usuario, senha, fg_ativo, id_endereco) "
                + "VALUES(?,?,?,?,?,?,?)";

        try{
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getUsuario());
            stmt.setString(5, EncryptadorMD5.encryptar(cliente.getSenha()));
            stmt.setInt(6, cliente.getFg_ativo());
            DaoEndereco dend = new DaoEndereco();
            int enderecoId = dend.validaEndereco(cliente.getEndereco());
            if(enderecoId == 0){
                dend.salvar(cliente.getEndereco());
                enderecoId = dend.validaEndereco(cliente.getEndereco());
            }
            stmt.setInt(7, enderecoId);
            stmt.execute();
            stmt.close();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Cliente> listarTodos(){
        return getClientes(conecta);
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
                DaoCliente.result(rs, clienteResultado);
            }
            rs.close();
            stmt.close();
            return clienteResultado;

        } catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public Cliente pesquisaPorID(String ID){
        return DaoCliente.getCliente(ID, conecta);
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

                if(Objects.equals(EncryptadorMD5.encryptar(cliente.getSenha()), storedPassword) && fg_ativo == 1){
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
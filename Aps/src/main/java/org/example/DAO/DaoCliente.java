/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.DAO;

import org.example.Model.Cliente;
import  org.example.Helpers.EncryptadorMD5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
/**
 *
 * @author kener_000
 */
public class DaoCliente {

    Connection conecta;
    
    public DaoCliente(){
        this.conecta = new DaoUtil().conecta();
    }

    public void salvar(Cliente cliente){
        String sql = "INSERT INTO tb_clientes(nome, sobrenome, telefone, usuario, senha, fg_ativo, id_endereco) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try{
            EncryptadorMD5 md5 = new EncryptadorMD5();
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getUsuario());
            stmt.setString(5, md5.encryptar(cliente.getSenha())); // Usa a função MD5 para criptografar a senha
            stmt.setInt(6, cliente.getFg_ativo());

            DaoEndereco dend = new DaoEndereco();
            if(dend.validaEndereco(cliente.getEndereco()) == 0){
                dend.salvar(cliente.getEndereco());
                stmt.setInt(7, dend.validaEndereco(cliente.getEndereco()));
            } else{
                stmt.setInt(7, dend.validaEndereco(cliente.getEndereco()));
            }

            stmt.execute();
            stmt.close();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    public List<Cliente> listarTodos(){
        String sql = "SELECT * FROM tb_clientes WHERE fg_Ativo='1' ORDER BY id_cliente";
        ResultSet rs;
        List<Cliente> clientes = new ArrayList<Cliente>();
        
        try{
            
            PreparedStatement stmt = conecta.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()){
            
                Cliente cliente = new Cliente();
                cliente.setId_cliente(rs.getInt("id_cliente"));
                cliente.setNome(rs.getString("nome"));
                cliente.setSobrenome(rs.getString("sobrenome"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setUsuario(rs.getString("usuario"));
                cliente.setSenha(rs.getString("senha"));
                cliente.setFg_ativo(1);
                clientes.add(cliente);
            }
            rs.close();
            stmt.close();
            return clientes;
        
            
        } catch(SQLException e){
            
             throw new RuntimeException(e);
        }
        
    }
    
    public Cliente pesquisaPorUsuario(Cliente cliente){
        String sql = "SELECT * FROM tb_clientes WHERE usuario='"+cliente.getUsuario()+"'";
        ResultSet rs;
        Cliente clienteResultado = new Cliente();
        
        try{
            
            PreparedStatement stmt = conecta.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()){
            
                clienteResultado.setId_cliente(rs.getInt("id_cliente"));
                clienteResultado.setNome(rs.getString("nome"));
                clienteResultado.setSobrenome(rs.getString("sobrenome"));
                clienteResultado.setTelefone(rs.getString("telefone"));
                clienteResultado.setUsuario(rs.getString("usuario"));
                clienteResultado.setSenha(rs.getString("senha"));
                clienteResultado.setFg_ativo(1);

            }
            rs.close();
            stmt.close();
            return clienteResultado;
        
            
        } catch(SQLException e){
            
             throw new RuntimeException(e);
        }
        
    }
    
    public Cliente pesquisaPorID(String ID){
        String sql = "SELECT * FROM tb_clientes WHERE id_cliente='"+ID+"'";
        ResultSet rs;
        Cliente clienteResultado = new Cliente();
        
        try{
            
            PreparedStatement stmt = conecta.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()){
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
        String sql = "SELECT usuario, senha, fg_ativo FROM tb_clientes WHERE usuario = ?";
        try {
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setString(1, cliente.getUsuario());

            ResultSet rs = stmt.executeQuery();
            Cliente validCliente = null;

            if (rs.next()) {
                validCliente = new Cliente();
                validCliente.setUsuario(rs.getString("usuario"));
                validCliente.setSenha(rs.getString("senha"));
                validCliente.setFg_ativo(rs.getInt("fg_ativo"));
            }

            rs.close();
            stmt.close();

            String encryptedPassword = new EncryptadorMD5().encryptar(cliente.getSenha());
            boolean validPassword = validCliente != null && encryptedPassword.equals(validCliente.getSenha());
            boolean isActive = validCliente != null && validCliente.getFg_ativo() == 1;
            return validPassword && isActive;


        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return false;
        } catch (EncryptadorMD5.EncryptionException e) {
            throw new RuntimeException(e);
        }
    }

}



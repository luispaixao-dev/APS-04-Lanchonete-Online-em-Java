/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.lanchonete.dao;


import org.example.lanchonete.model.Ingrediente;
import org.example.lanchonete.model.Lanche;

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
public class DaoLanche {
    private final Connection conecta;

    public DaoLanche() {
        this.conecta = new DaoUtil().conecta();
    }

    public void salvar(Lanche lanche){
        String sql = "INSERT INTO tb_lanches(nm_lanche, descricao, valor_venda, fg_ativo) "
                + "VALUES(?,?,?,?)";

        try{
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setString(1, lanche.getNome());
            stmt.setString(2, lanche.getDescricao());
            stmt.setDouble(3, lanche.getValor_venda());
            stmt.setInt(4, 1);


            stmt.execute();
            stmt.close();


        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void salvarCliente(Lanche lanche){
        String sql = "INSERT INTO tb_lanches(nm_lanche, descricao, valor_venda, fg_ativo) "
                + "VALUES(?,?,?,?)";

        try{
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setString(1, lanche.getNome());
            stmt.setString(2, lanche.getDescricao());
            stmt.setDouble(3, lanche.getValor_venda());
            stmt.setInt(4, 0);


            stmt.execute();
            stmt.close();


        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Lanche> listarTodos(){
        String sql;
        sql = "SELECT * FROM tb_lanches WHERE fg_Ativo='1' ORDER BY id_lanche";
        ResultSet rs;
        List<Lanche> lanches = new ArrayList<Lanche>();

        return getLanches(sql, lanches);

    }

    public Lanche pesquisaPorNome(Lanche lanche){
        String sql = "SELECT * FROM tb_lanches WHERE nm_lanche='"+lanche.getNome()+"'";
        return (Lanche) getLanche(sql);

    }

    public Lanche pesquisaPorNome(String nome){
        String sql = "SELECT * FROM tb_lanches WHERE nm_lanche='"+nome+"'";
        return (Lanche) getLanche(sql);

    }

    private List<Lanche> getLanche(String sql) {
        ResultSet rs;
        List<Lanche> listaDeLanches = new ArrayList<>();

        return getLanches(sql, listaDeLanches);
    }

    private List<Lanche> getLanches(String sql, List<Lanche> listaDeLanches) {
        ResultSet rs;
        try {
            PreparedStatement stmt = conecta.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Lanche lanche = new Lanche();
                lanche.setId_lanche(rs.getInt("id_lanche"));
                lanche.setNome(rs.getString("nm_lanche"));
                lanche.setDescricao(rs.getString("descricao"));
                lanche.setValor_venda(rs.getDouble("valor_venda"));
                lanche.setFg_ativo(1);
                listaDeLanches.add(lanche);
            }

            rs.close();
            stmt.close();
            return listaDeLanches;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void vincularIngrediente(Lanche lanche, Ingrediente ingrediente){

        String sql = "INSERT INTO tb_ingredientes_lanche(id_lanche, id_ingrediente, quantidade)"
                + "VALUES(?,?,?)";
        try{
            PreparedStatement stmt = conecta.prepareStatement(sql);
            stmt.setInt(1, lanche.getId_lanche());
            stmt.setInt(2, ingrediente.getId_ingrediente());
            stmt.setInt(3, ingrediente.getQuantidade());


            stmt.execute();
            stmt.close();

        } catch(SQLException e){

            throw new RuntimeException(e);
        }
    }
}
    
    
    
    
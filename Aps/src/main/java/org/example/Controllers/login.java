/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.Controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.DaoCliente;
import org.example.DAO.DaoToken;
import org.example.Model.Cliente;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.Instant;

/**
 *
 * @author kener_000
 */
@WebServlet (name = "login", urlPatterns = "/login")
public class login extends HttpServlet {

    public void init() {
        String message = "Hello World!";
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DaoCliente listaClientes = new DaoCliente();
        response.setContentType("text/html");
        for (Cliente listarTodo : listaClientes.listarTodos()) {
            // Hello
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + listarTodo.getNome() + "</h1>");
            out.println("<h1>" + listarTodo.getSenha() + "</h1>");
            out.println("</body></html>");

        }
    }
    //processRequest(request, response);


    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        //Para receber JSONs, é necessário utilizar esse Buffer para receber os dados,
        //Então tem que ser Feito assim:
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json;
        boolean resultado;


        //Aqui ele checa se os Dados não tão vazios, por motivos de vai que
        //Converte os dados do JSON para um Formato de Objeto que o Java consiga lidar
        json = br.readLine();
        JSONObject dados = new JSONObject(json);

        //Aqui, ele Instancia um objeto do Model Cliente, e Popula ele com os dados do JSON
        Cliente cliente = new Cliente();
        cliente.setUsuario(dados.getString("usuario"));
        cliente.setSenha(dados.getString("senha"));

        /////////////////////////
        //E Para finalizar, salva no Banco usando o DAO dele

        DaoCliente clienteDAO = new DaoCliente();

        DaoToken tokenDAO = new DaoToken();
        resultado = clienteDAO.login(cliente);

        if(resultado){
            Cliente clienteCompleto = clienteDAO.pesquisaPorUsuario(cliente);

            Cookie cookie = new Cookie("token", clienteCompleto.getId_cliente()+"-"+Instant.now().toString());
            tokenDAO.salvar(cookie.getValue());
            cookie.setMaxAge(30*60);
            response.addCookie(cookie);
        }

        try (PrintWriter out = response.getWriter()) {

            //Aqui é onde a Resposta é mandada para o Cliente, dando um Feedback de que tudo deu certo.

            if (resultado) {
                out.println("../carrinho/carrinho.html");

            } else {
                out.println("erro");
            }


        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
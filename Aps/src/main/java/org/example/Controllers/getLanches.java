/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.Controllers;

import Model.Lanche;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.DAO.DaoLanche;
import org.example.Helpers.ValidadorCookie;
import jakarta.servlet.http.Cookie;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * @author kener_000
 */
public class getLanches extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
//            PrintWriter out = response.getWriter();
//            out.println("<html>");
//            out.println("<body>");
//            out.println("Hello World");
//            out.println("</body>");
//            out.println("</html>");


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

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
        //processRequest(request, response);
        response.setContentType("application/json");
        ////////Validar Cookie
        boolean resultado = false;

        try {
            Cookie[] cookies = request.getCookies();
            ValidadorCookie validar = new ValidadorCookie();

            resultado = validar.validarFuncionario(cookies);
        } catch (java.lang.NullPointerException e) {
            System.out.println(e);
        }
        //////////////

        if (resultado) {

            DaoLanche lancheDAO = new DaoLanche();

            List<Lanche> lanches = lancheDAO.listarTodos();

            Gson gson = new Gson();
            String json = gson.toJson(lanches);
            try (PrintWriter out = response.getWriter()) {
                out.print(json);
                out.flush();
            }
        } else {
            try (PrintWriter out = response.getWriter()) {
                out.println("erro");
            }
        }
    }

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
        processRequest(request, response);
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

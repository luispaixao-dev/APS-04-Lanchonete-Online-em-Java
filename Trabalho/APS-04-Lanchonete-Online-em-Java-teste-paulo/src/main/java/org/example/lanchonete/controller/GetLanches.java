package org.example.lanchonete.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lanchonete.dao.DaoLanche;
import org.example.lanchonete.model.Lanche;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class GetLanches extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Validar Cookie
        boolean resultado = validarCookie(request);

        if (resultado) {
            sendLanchesList(response);
        } else {
            try (PrintWriter out = response.getWriter()) {
                out.println("erro");
            }
        }
    }

    private boolean validarCookie(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("usuario") && cookie.getValue().equals("valido")) {
                    return true;
                }
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }
        return false;
    }

    private static final Gson gson = new Gson();

    private void sendLanchesList(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            DaoLanche lancheDAO = new DaoLanche();
            List<Lanche> lanches = lancheDAO.listarTodos();

            String json = gson.toJson(lanches);
            response.getWriter().write(json);
            response.getWriter().flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao enviar a lista de lanches: " + e.getMessage());
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
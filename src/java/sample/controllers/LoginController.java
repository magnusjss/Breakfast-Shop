/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sample.foods.FoodDAO;
import sample.foods.FoodDTO;
import sample.users.UserDAO;
import sample.users.UserDTO;
/**
 *
 * @author MagnusJS
 */
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController"})
public class LoginController extends HttpServlet {

    private static final String ERROR = "login.jsp";
    
    private static final String ADMIN = "ADMIN";
    private static final String SEARCH_PAGE = "index.jsp";
    
    private static final String USER = "USER";
    private static final String USER_PAGE = "index.jsp";
    
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        
        try {
            String userID = request.getParameter("userID");
            String password = request.getParameter("password");
            UserDAO dao = new UserDAO();
            UserDTO checkLogin = dao.checkLogin(userID, password);
            
            if (checkLogin != null) {
                HttpSession session = request.getSession();
                session.setAttribute("LOGIN_USER", checkLogin);
                String roleID = checkLogin.getRoleID();
                
                //Set data to shop page
                FoodDAO foodDao = new FoodDAO();
                List<FoodDTO> list = foodDao.getAllProduct();
                
                session.setAttribute("LIST", list);
                
                if (ADMIN.equals(roleID)) {
                    url = SEARCH_PAGE;
                }else if(USER.equals(roleID)) {
                    url = USER_PAGE;
                }
            } else {
                request.setAttribute("ERROR", "Incorrect userID or password!");
            }
            
            
        } catch (Exception e) {
            log("error at LoginController: " + e.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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

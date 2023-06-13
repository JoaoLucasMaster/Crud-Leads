package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelException;
import model.Lead;
import model.User;
import model.dao.DAOFactory;
import model.dao.LeadDAO;
import model.dao.UserDAO;

import java.sql.SQLIntegrityConstraintViolationException;

@WebServlet(urlPatterns = {"/leads", "/lead/form", "/lead/delete", "/lead/insert", "/lead/update"})
public class LeadsController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI();

        switch (action) {
            case "/crud-manager/lead/form": {
                listLeads(req);
                loadUsers(req);
                req.setAttribute("action", "insert");
                ControllerUtil.forward(req, resp, "/form-lead.jsp");
                break;
            }
            case "/crud-manager/lead/update": {
                listLeads(req);
                Lead lead = loadLead(req);
                req.setAttribute("lead", lead);
                loadUsers(req);
                req.setAttribute("action", "update");
                ControllerUtil.forward(req, resp, "/form-lead.jsp");
                break;
            }
            default:
                listLeads(req);

                ControllerUtil.transferSessionMessagesToRequest(req);

                ControllerUtil.forward(req, resp, "/leads.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getRequestURI();

        if (action == null || action.equals("")) {
            ControllerUtil.forward(req, resp, "/index.jsp");
            return;
        }

        switch (action) {
            case "/crud-manager/lead/delete":
                deleteLead(req, resp);
                break;
            case "/crud-manager/lead/insert": {
                insertLead(req, resp);
                break;
            }
            case "/crud-manager/lead/update": {
                updateLead(req, resp);
                break;
            }
            default:
                System.out.println("URL inválida " + action);
                break;
        }

        ControllerUtil.redirect(resp, req.getContextPath() + "/leads");
    }

    private Lead loadLead(HttpServletRequest req) {
        String leadIdParameter = req.getParameter("leadId");

        int leadId = Integer.parseInt(leadIdParameter);

        LeadDAO dao = DAOFactory.createDAO(LeadDAO.class);

        try {
            Lead lead = dao.findById(leadId);

            if (lead == null)
                throw new ModelException("Lead não encontrado para alteração");

            return lead;
        } catch (ModelException e) {
            // log no servidor
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }

        return null;
    }

    private void listLeads(HttpServletRequest req) {
        LeadDAO dao = DAOFactory.createDAO(LeadDAO.class);

        List<Lead> leads = null;
        try {
            leads = dao.listAll();
        } catch (ModelException e) {
            // Log no servidor
            e.printStackTrace();
        }

        if (leads != null)
            req.setAttribute("leads", leads);
    }
    
    private void loadUsers(HttpServletRequest req) {
        UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
        List<User> users = null;
        try {
            users = userDAO.listAll();
        } catch (ModelException e) {
            // Log no servidor
            e.printStackTrace();
        }
        if (users != null) {
            req.setAttribute("users", users);
        }
    }

    private void insertLead(HttpServletRequest req, HttpServletResponse resp) {
        String leadName = req.getParameter("name");
        String leadEmail = req.getParameter("email");
        String leadPhone = req.getParameter("phone");
        String leadStatus = req.getParameter("status");
        String leadUserId = req.getParameter("userId");

        Lead lead = new Lead();
        lead.setName(leadName);
        lead.setEmail(leadEmail);
        lead.setPhone(leadPhone);
        lead.setStatus(leadStatus);
        lead.setUserId(Integer.parseInt(leadUserId));

        LeadDAO dao = DAOFactory.createDAO(LeadDAO.class);

        try {
            if (dao.save(lead)) {
                ControllerUtil.sucessMessage(req, "Lead '" + lead.getName() + "' salvo com sucesso.");
            } else {
                ControllerUtil.errorMessage(req, "Lead '" + lead.getName() + "' não pode ser salvo.");
            }

        } catch (ModelException e) {
            // log no servidor
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }

    private void updateLead(HttpServletRequest req, HttpServletResponse resp) {
        String leadName = req.getParameter("name");
        String leadEmail = req.getParameter("email");
        String leadPhone = req.getParameter("phone");
        String leadStatus = req.getParameter("status");
        String leadUserId = req.getParameter("userId");

        Lead lead = loadLead(req);
        lead.setName(leadName);
        lead.setEmail(leadEmail);
        lead.setPhone(leadPhone);
        lead.setStatus(leadStatus);
        lead.setUserId(Integer.parseInt(leadUserId));

        LeadDAO dao = DAOFactory.createDAO(LeadDAO.class);

        try {
            if (dao.update(lead)) {
                ControllerUtil.sucessMessage(req, "Lead '" + lead.getName() + "' atualizado com sucesso.");
            } else {
                ControllerUtil.errorMessage(req, "Lead '" + lead.getName() + "' não pode ser atualizado.");
            }

        } catch (ModelException e) {
            // log no servidor
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }

    private void deleteLead(HttpServletRequest req, HttpServletResponse resp) {
        String leadIdParameter = req.getParameter("id");

        int leadId = Integer.parseInt(leadIdParameter);

        LeadDAO dao = DAOFactory.createDAO(LeadDAO.class);

        try {
            Lead lead = dao.findById(leadId);

            if (lead == null)
                throw new ModelException("Lead não encontrado para deleção.");

            if (dao.delete(lead)) {
                ControllerUtil.sucessMessage(req, "Lead '" + lead.getName() + "' deletado com sucesso.");
            } else {
                ControllerUtil.errorMessage(req, "Lead '" + lead.getName() + "' não pode ser deletado. Há dados relacionados ao lead.");
            }
        } catch (ModelException e) {
            // log no servidor
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                ControllerUtil.errorMessage(req, e.getMessage());
            }
            e.printStackTrace();
            ControllerUtil.errorMessage(req, e.getMessage());
        }
    }
}
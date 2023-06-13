package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Lead;
import model.ModelException;
import model.User;

public class MySQLLeadDAO implements LeadDAO {

    @Override
    public boolean save(Lead lead) throws ModelException {
        DBHandler db = new DBHandler();

        String sqlInsert = "INSERT INTO leads VALUES (DEFAULT, ?, ?, ?, ?, ?);";

        db.prepareStatement(sqlInsert);

        db.setString(1, lead.getName());
        db.setString(2, lead.getEmail());
        db.setString(3, lead.getPhone());
        db.setString(4, lead.getStatus());
        db.setInt(5, lead.getUserId());

        return db.executeUpdate() > 0;
    }

    @Override
    public boolean update(Lead lead) throws ModelException {
        DBHandler db = new DBHandler();

        String sqlUpdate = "UPDATE leads "
                + "SET name = ?, "
                + "email = ?, "
                + "phone = ?, "
                + "status = ?, "
                + "user_id = ? "
                + "WHERE id = ?; ";

        db.prepareStatement(sqlUpdate);

        db.setString(1, lead.getName());
        db.setString(2, lead.getEmail());
        db.setString(3, lead.getPhone());
        db.setString(4, lead.getStatus());
        db.setInt(5, lead.getUserId());
        db.setInt(6, lead.getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public boolean delete(Lead lead) throws ModelException {
        DBHandler db = new DBHandler();

        String sqlDelete = "DELETE FROM leads "
                + "WHERE id = ?;";

        db.prepareStatement(sqlDelete);
        db.setInt(1, lead.getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public List<Lead> listAll() throws ModelException {
        DBHandler db = new DBHandler();

        List<Lead> leads = new ArrayList<Lead>();

        String sqlQuery = "SELECT l.id AS 'lead_id', l.*, u.* "
                + "FROM leads l "
                + "INNER JOIN users u "
                + "ON l.user_id = u.id;";

        db.createStatement();

        db.executeQuery(sqlQuery);

        while (db.next()) {
            User user = new User(db.getInt("user_id"));
            user.setName(db.getString("name"));
            user.setEmail(db.getString("email"));

            Lead lead = new Lead(db.getInt("lead_id"));
            lead.setName(db.getString("name"));
            lead.setEmail(db.getString("email"));
            lead.setPhone(db.getString("phone"));
            lead.setStatus(db.getString("status"));
            lead.setUserId(user.getId());

            leads.add(lead);
        }

        return leads;
    }

    @Override
    public Lead findById(int id) throws ModelException {
        DBHandler db = new DBHandler();

        String sql = "SELECT * FROM leads WHERE id = ?;";

        db.prepareStatement(sql);
        db.setInt(1, id);
        db.executeQuery();

        Lead lead = null;
        while (db.next()) {
            lead = new Lead(id);
            lead.setName(db.getString("name"));
            lead.setEmail(db.getString("email"));
            lead.setPhone(db.getString("phone"));
            lead.setStatus(db.getString("status"));

            UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
            User user = userDAO.findById(db.getInt("user_id"));
            lead.setUserId(user.getId());

            break;
        }

        return lead;
    }
}

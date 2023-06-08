package model.dao;

import java.util.List;

import model.ModelException;
import model.Company;
import model.Lead;

public interface LeadDAO {
	boolean save(Lead lead) throws ModelException;
	boolean update(Lead lead) throws ModelException;
	boolean delete(Lead lead) throws ModelException;
	List<Lead> listAll() throws ModelException;
	Lead findById(int id) throws ModelException;
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.cp.sa.dao;

import edu.utfpr.cp.sa.entity.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caah_
 */
public class CustomerDAO {
    
    public void create(Customer c , int idCountry) {
        Connection con = ConnectionFactory.getConnection();
	PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO country (nameCustomer,phoneCustomer,ageCustomer,creditLimitCustomer,country_idCountry)VALUES(?,?,?,?,?)");
            stmt.setString(1, c.getName());
            stmt.setString(2, c.getPhone());
            stmt.setInt(3,c.getAge());
            stmt.setDouble(4, c.getCreditLimit());
            stmt.setInt(5, idCountry);
                        
            stmt.executeUpdate();
                        
	} catch (SQLException e) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE,null,e);
	}finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
     public List<Customer> read(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Customer> c = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELCT * FROM customer");
            rs = stmt.executeQuery();
        
            while(rs.next()){
                Customer customer = new Customer();
                customer.setName(rs.getString("nameCustomer"));
                customer.setPhone(rs.getString("phoneCustomer"));
                customer.setAge(rs.getInt("ageCustomer"));
                customer.setCreditLimit(rs.getDouble("creditLimitCustomer"));
                
                c.add(customer);
                return c;
            }
        } catch (SQLException ex ) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt,rs);
        }
        return c;
    }
}

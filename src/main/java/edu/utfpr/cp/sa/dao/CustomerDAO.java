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

public class CustomerDAO {

    private final CountryDAO DAO_COUNTRY = new CountryDAO();
    
    public boolean create(Customer c) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO customer (nameCustomer,phoneCustomer,ageCustomer,creditLimitCustomer,country_idCountry)VALUES(?,?,?,?,?)");
            stmt.setString(1, c.getName());
            stmt.setString(2, c.getPhone());
            stmt.setInt(3, c.getAge());
            stmt.setDouble(4, c.getCreditLimit());
            stmt.setInt(5, c.getCountry().getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
        return true;
    }

    public List<Customer> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Customer> c = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELECT * FROM customer");
            rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCountry(DAO_COUNTRY.findCountryById(rs.getInt("country_idCountry")));
                customer.setName(rs.getString("nameCustomer"));
                customer.setPhone(rs.getString("phoneCustomer"));
                customer.setAge(rs.getInt("ageCustomer"));
                customer.setCreditLimit(rs.getDouble("creditLimitCustomer"));
                c.add(customer);
            }
            return c;
        } catch (SQLException ex) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return null;
    }

    public boolean update(Customer c) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("UPDATE customer SET nameCustomer = ? , phoneCustomer = ? , ageCustomer = ? , creditLimitCustomer = ? , country_idCountry = ? WHERE idCustomer = ? ");
            stmt.setString(1, c.getName());
            stmt.setString(2, c.getPhone());
            stmt.setInt(3, c.getAge());
            stmt.setDouble(4, c.getCreditLimit());
            stmt.setDouble(5, c.getCountry().getId());
            stmt.setInt(6, c.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
        return true;
    }

    public boolean delete(Customer c) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("DELETE FROM customer WHERE idCustomer = ? ");
            stmt.setInt(1, c.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } finally {
            ConnectionFactory.closeConnection(con, stmt);
        }
        return true;
    }

    public Customer findCustomerByName(String name) throws Exception {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM customer WHERE nameCustomer = ?");
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            while(rs.next()){
                Customer c = new Customer();
                c.setId(rs.getInt("idCustomer"));
                c.setName(rs.getString("nameCustomer"));
                c.setAge(rs.getInt("ageCustomer"));
                c.setCountry(DAO_COUNTRY.findCountryById(rs.getInt("country_idCountry")));
                c.setPhone(rs.getString("phoneCustomer"));
                return c;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return null;
    }
    
    public int findIdCustomerByName(String name) throws Exception {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM customer WHERE nameCustomer = ?");
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            while(rs.next()){
                return rs.getInt("idCustomer");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return 0;
    }
}

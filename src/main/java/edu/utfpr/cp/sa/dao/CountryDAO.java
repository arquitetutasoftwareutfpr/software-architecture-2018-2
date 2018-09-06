/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.cp.sa.dao;

import edu.utfpr.cp.sa.entity.Country;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caah_
 */
public class CountryDAO {
    public void create(Country c) {
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
		
		try {
			stmt = con.prepareStatement("INSERT INTO country (nameCountry,phonedigtsCountry,acronymCountry)VALUES(?,?,?)");
                        stmt.setString(1, c.getName());
                        stmt.setInt(2, c.getPhoneDigits());
                        stmt.setString(3 , c.getAcronym());
                        
                        stmt.executeUpdate();
                        
		} catch (SQLException e) {
			Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE,null,e);
		}finally{
                    ConnectionFactory.closeConnection(con, stmt);
                }
    }
    public List<Country> read(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Country> c = new ArrayList<>();
        try {
            stmt = con.prepareStatement("SELCT * FROM country");
            rs = stmt.executeQuery();
        
            while(rs.next()){
                Country country = new Country();
                country.setName(rs.getString("nameCountry"));
                country.setPhoneDigits(rs.getInt("phonedigitsCountry"));
                country.setAcronym(rs.getString("acronymCountry"));
                
                c.add(country);
                return c;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            ConnectionFactory.closeConnection(con, stmt,rs);
        }
        return c;
    }   
    public void update(Country c) {
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
                int idCountry = findByCountryName(c.getName());
		
		try {
                        stmt = con.prepareStatement("UPDATE country SET nameCountry = ? , phonedigtsCountry = ? , acronymCountry = ? WHERE idCountry = ? ");
                        stmt.setString(1, c.getName());
                        stmt.setInt(2, c.getPhoneDigits());
                        stmt.setString(3 , c.getAcronym());
                        stmt.setInt(4 , idCountry);
                        
                        stmt.executeUpdate();
                        
		} catch (SQLException e) {
			Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE,null,e);
		}finally{
                    ConnectionFactory.closeConnection(con, stmt);
                }
    }
    public void delete(Country c) {
		Connection con = ConnectionFactory.getConnection();
		PreparedStatement stmt = null;
                int idCountry = findByCountryName(c.getName());
		
		try {
                        stmt = con.prepareStatement("DELETE FROM country WHERE idCountry = ? ");
                        stmt.setInt(1 , idCountry);
                        
                        stmt.executeUpdate();
                        
		} catch (SQLException e) {
			Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE,null,e);
		}finally{
                    ConnectionFactory.closeConnection(con, stmt);
                }
    }
    public int findByCountryName(String name){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idCountry = 0;
         try {
            stmt = con.prepareStatement("SELCT idCountry FROM country WHERE nameCountry = ?");
            stmt.setString(1,name);
            rs = stmt.executeQuery();
            idCountry = rs.getInt("idCountry");
            
        } catch (SQLException ex) {
            Logger.getLogger(CountryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt,rs);
        }
         return idCountry;
    }
}

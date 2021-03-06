package edu.utfpr.cp.sa.gui;

import edu.utfpr.cp.sa.dao.CountryDAO;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import edu.utfpr.cp.sa.entity.Country;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;

class CountryTableModel extends AbstractTableModel {

    private ArrayList<Country> countries;
    private String columnNames[] = {"Name", "Acronym", "Phone Digits"};

    public CountryTableModel(Set<Country> countries) {
        this.countries = new ArrayList<>(countries);
    }

    @Override
    public int getRowCount() {
        return countries.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        switch (columnIndex) {
            case 0:
                return this.countries.get(rowIndex).getName();

            case 1:
                return this.countries.get(rowIndex).getAcronym();

            case 2:
                return this.countries.get(rowIndex).getPhoneDigits();

            default:
                break;
        }

        return null;
    }

}

public class CountryWindow extends JFrame {

    private JPanel contentPane;
    private JTextField name;
    private JTextField acronym;
    private JTextField phoneDigits;
    private JTable table;
    private Set<Country> countries;
    private final CountryDAO DAO = new CountryDAO();

    private void create() {
        Country c = new Country();
        c.setName(name.getText());
        c.setAcronym(acronym.getText());
        c.setPhoneDigits(new Integer(phoneDigits.getText()));

        //Inserindo no banco
        if (DAO.create(c)) {
            c.setId(DAO.findIdCountryByName(c.getName()));
            if (this.countries.add(c)) {
                JOptionPane.showMessageDialog(this, "Country successfully added!");
                this.table.setModel(new CountryTableModel(countries));

            } else {
                JOptionPane.showMessageDialog(this, "Sorry, country already exists");
            }
        }
    }

    private void setRowsTable(){
        List<Country> listCountries = new ArrayList<>();
        listCountries = DAO.read();
        if(!listCountries.isEmpty()){
            listCountries.forEach(e -> {
                countries.add(e);
            });
            this.table.setModel(new CountryTableModel(countries));
        }
    }
    
    public CountryWindow(Set<Country> countries) {
        this.countries = countries;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowOpened(WindowEvent eve){
                setRowsTable();
            }
        });
        
        JScrollPane panelTable = new JScrollPane();
        contentPane.add(panelTable, BorderLayout.CENTER);

        table = new JTable();
        table.setModel(new CountryTableModel(countries));
        panelTable.setViewportView(table);

        JPanel panelInclusion = new JPanel();
        contentPane.add(panelInclusion, BorderLayout.NORTH);
        panelInclusion.setLayout(new GridLayout(4, 2, 0, 0));

        JLabel lblName = new JLabel("Name");
        panelInclusion.add(lblName);

        name = new JTextField();
        panelInclusion.add(name);
        name.setColumns(10);

        JLabel lblAcronym = new JLabel("Acronym");
        panelInclusion.add(lblAcronym);

        acronym = new JTextField();
        panelInclusion.add(acronym);
        acronym.setColumns(10);

        JLabel lblPhoneDigits = new JLabel("Phone Digits");
        panelInclusion.add(lblPhoneDigits);

        phoneDigits = new JTextField();
        panelInclusion.add(phoneDigits);
        phoneDigits.setColumns(10);

        JButton btnCreate = new JButton("Create");
        panelInclusion.add(btnCreate);
        btnCreate.addActionListener(e -> this.create());

        JButton btnClose = new JButton("Close");
        panelInclusion.add(btnClose);
        btnClose.addActionListener(e -> this.dispose());

        this.pack();
        this.setVisible(true);
    }

}

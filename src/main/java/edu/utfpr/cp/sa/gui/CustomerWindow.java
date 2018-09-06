package edu.utfpr.cp.sa.gui;

import edu.utfpr.cp.sa.dao.CountryDAO;
import edu.utfpr.cp.sa.dao.CustomerDAO;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import edu.utfpr.cp.sa.entity.Country;
import edu.utfpr.cp.sa.entity.Customer;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;

class CustomerTableModel extends AbstractTableModel {

    private ArrayList<Customer> customers;
    private String columnNames[] = {"Name", "Phone", "Credit Limit", "Age", "Country"};

    public CustomerTableModel(Set<Customer> customers) {
        this.customers = new ArrayList<>(customers);
    }

    @Override
    public int getRowCount() {
        return customers.size();
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
                return this.customers.get(rowIndex).getName();

            case 1:
                return this.customers.get(rowIndex).getPhone();

            case 2:
                return this.customers.get(rowIndex).getCreditLimit();

            case 3:
                return this.customers.get(rowIndex).getAge();

            case 4:
                return this.customers.get(rowIndex).getCountry().getName();

            default:
                break;
        }

        return null;
    }

}

public class CustomerWindow extends JFrame {

    private JPanel contentPane;
    private JTextField name;
    private JTextField phone;
    private JTextField age;
    private JComboBox<String> country;
    private JTable table;
    private JButton btnCreate;

    private Set<Customer> customers;
    private Set<Country> countries;

    private final CountryDAO DAO_COUNTRY = new CountryDAO();
    private final CustomerDAO DAO_CUSTOMER = new CustomerDAO();

    private void persist() {
        Customer c = new Customer();
        Country selected = countries
                .stream()
                .filter(
                        e -> e.getName()
                                .equalsIgnoreCase(
                                        (String) country.getSelectedItem()))
                .findFirst()
                .get();

        try {
            c.setCountry(selected);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;

        }

        c.setAge(new Integer(age.getText()));

        try {
            c.setName(name.getText());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;

        }

        try {
            c.setPhone(phone.getText());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            return;

        }

        //Inserindo no banco
        if (btnCreate.getText().equalsIgnoreCase("Create")) {
            if (DAO_CUSTOMER.create(c)) {
                if (this.customers.add(c)) {
                    JOptionPane.showMessageDialog(this, "Customer successfully added!");
                    this.table.setModel(new CustomerTableModel(customers));
                    this.pack();
                } else {
                    JOptionPane.showMessageDialog(this, "Sorry, customer already exists");
                }
            }
        } else {
            try {
                c.setId(DAO_CUSTOMER.findIdCustomerByName(c.getName()));
            } catch (Exception ex) {
                Logger.getLogger(CustomerWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (DAO_CUSTOMER.update(c)) {
                JOptionPane.showMessageDialog(this, "Customer successfully updated!");
                setRowsTable();
                this.table.setModel(new CustomerTableModel(customers));
                btnCreate.setText("Create");
                this.pack();
            }
        }

    }

    private void setCountriesFromDB() {
        List<Country> listCountries = new ArrayList<>();
        listCountries = DAO_COUNTRY.read();
        if (!listCountries.isEmpty()) {
            listCountries.forEach(e -> countries.add(e));
        }
    }

    private void setRowsTable() {
        customers.clear();
        List<Customer> listCustomers = new ArrayList<>();
        listCustomers = DAO_CUSTOMER.read();
        if (!listCustomers.isEmpty()) {
            listCustomers.forEach(e -> {
                customers.add(e);
            });
            this.table.setModel(new CustomerTableModel(customers));
        }
    }

    private void setFormToUpdateCustomer(Customer c) {
        name.setText(c.getName());
        age.setText(String.valueOf(c.getAge()));
        phone.setText(c.getPhone());
        country.setSelectedItem(c.getCountry().getName());
        btnCreate.setText("Save");
    }

    public CustomerWindow(Set<Customer> customers, Set<Country> countries) {
        this.customers = customers;
        this.countries = countries;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent eve) {
                setRowsTable();
            }
        });

        JScrollPane panelTable = new JScrollPane();
        contentPane.add(panelTable, BorderLayout.CENTER);

        table = new JTable();
        table.setModel(new CustomerTableModel(customers));
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int countClickes = evt.getClickCount();
                if (countClickes == 1) {
                    try {
                        Customer c = DAO_CUSTOMER.findCustomerByName((String) table.getValueAt(table.getSelectedRow(), 0));
                        setFormToUpdateCustomer(c);
                    } catch (Exception ex) {
                        Logger.getLogger(CustomerWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        panelTable.setViewportView(table);

        JPanel panelInclusion = new JPanel();
        contentPane.add(panelInclusion, BorderLayout.NORTH);
        panelInclusion.setLayout(new GridLayout(5, 2, 0, 0));

        JLabel lblName = new JLabel("Name");
        panelInclusion.add(lblName);

        name = new JTextField();
        panelInclusion.add(name);
        name.setColumns(10);

        JLabel lblPhone = new JLabel("Phone");
        panelInclusion.add(lblPhone);

        phone = new JTextField();
        panelInclusion.add(phone);
        phone.setColumns(10);

        JLabel lblAge = new JLabel("Age");
        panelInclusion.add(lblAge);

        age = new JTextField();
        panelInclusion.add(age);
        age.setColumns(10);

        JLabel lblCountry = new JLabel("Country");
        panelInclusion.add(lblCountry);

        setCountriesFromDB();
        country = new JComboBox<>(countries.stream().map(Country::getName).toArray(String[]::new));
        panelInclusion.add(country);

        btnCreate = new JButton("Create");
        panelInclusion.add(btnCreate);
        btnCreate.addActionListener(e -> this.persist());

        JButton btnClose = new JButton("Close");
        panelInclusion.add(btnClose);
        btnClose.addActionListener(e -> this.dispose());

        this.pack();
        this.setVisible(true);
    }

}

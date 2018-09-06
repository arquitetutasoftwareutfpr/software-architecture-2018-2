package edu.utfpr.cp.sa.entity;

public class Country {

    private int id;
    private String name;
    private String acronym;
    private int phoneDigits;

    public Country() {
    }
    
    public Country(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public int getPhoneDigits() {
        return phoneDigits;
    }

    public void setPhoneDigits(int phoneDigits) {
        this.phoneDigits = phoneDigits;
    }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        Country other = (Country) obj;

        return this.getName().equalsIgnoreCase(other.getName());
    }

}

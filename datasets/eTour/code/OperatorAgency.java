package beans;

import java.sql.Date;

public class OperatorAgency {
    private String name;
    private String surname;
    private String residenceCity;
    private String phone;
    private String cap;
    private String address;
    private String email;
    private String password;
    private String username;
    private Date dateOfBirth;

    public OperatorAgency() {
    }

    public OperatorAgency(String name, String surname, String city, String phone, String cap, String address,
            String email, String pass, String user, Date date) {
        this.name = name;
        this.surname = surname;
        this.residenceCity = city;
        this.phone = phone;
        this.cap = cap;
        this.address = address;
        this.email = email;
        this.password = pass;
        this.username = user;
        this.dateOfBirth = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }

    public void setResidenceCity(String par1) {
        this.residenceCity = par1;
    }

    public String getResidenceCity () {
        return residenceCity;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setCap(String par1) {
        this.cap = par1;
    }

    public String getCap() {
        return cap;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String GetUserName() {
        return username;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
}
package kz.greetgo.sandbox.controller.model;

import java.sql.Date;

public class ClientDetail {
    //client
    public int id;
    public String surname;
    public String name;
    public String patronymic;
    public String gender;
    public Date birthDate;
    public int actual;

    //charm
    public String charm;

    //client_addr
    public String factStreet;
    public String factNo;
    public String factFlat;
    public String regStreet;
    public String regNo;
    public String regFlat;

    //client_phone
    public String homePhoneNumber;
    public String workPhoneNumber;
    public String mobileNumber1;
    public String mobileNumber2;
    public String mobileNumber3;

    public ClientDetail(int id, String surname, String name, String gender, Date birthDate, int actual,
                        String charm, String regStreet, String regNo, String regFlat, String mobileNumber1) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.actual = actual;

        this.charm = charm;

        this.regStreet = regStreet;
        this.regNo = regNo;
        this.regFlat = regFlat;

        this.mobileNumber1 = mobileNumber1;
    }

    public ClientDetail() {
    }
}
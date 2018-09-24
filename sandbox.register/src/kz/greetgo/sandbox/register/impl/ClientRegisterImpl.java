package kz.greetgo.sandbox.register.impl;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.model.Charm;
import kz.greetgo.sandbox.controller.model.ClientDetail;
import kz.greetgo.sandbox.controller.model.ClientRecord;
import kz.greetgo.sandbox.controller.model.FilterParams;
import kz.greetgo.sandbox.controller.register.ClientRegister;
import kz.greetgo.sandbox.register.dao.ClientDao;
import kz.greetgo.sandbox.register.util.JdbcSandbox;
import kz.greetgo.util.RND;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Bean
public class ClientRegisterImpl implements ClientRegister {

    public BeanGetter<ClientDao> clientDao;
    public BeanGetter<JdbcSandbox> jdbc;

    @Override
    public List<ClientRecord> getClients(FilterParams params) {

        //JDBC
        return jdbc.get().execute(con -> {
            StringBuilder sql = new StringBuilder("select c.id, c.surname, c.name, patronymic, birth_date,\n" +
                    "(select name from charm where id=c.charm) as charm,\n" +
                    "(select sum(money) from client_account where client=c.id) as totalBalance,\n" +
                    "(select max(money) from client_account where client=c.id) as maxBalance,\n" +
                    "(select min(money) from client_account where client=c.id) as minBalance \n" +
                    "from client c left join client_account a on c.id = a.client\n" +
                    "where c.actual=1");

            if (params.filter != null && params.filterCol != null) {
                sql.append(" and " + params.filterCol + " like ?");
            }

            if (params.sortBy != null && params.sortDir != null) {
                sql.append(String.format(" ORDER BY %s %s ", params.sortBy, params.sortDir));
            }

            if (params.limit != 0) {
                sql.append(" limit " + params.limit);
            }

            if (params.offset != 0) {
                sql.append(" OFFSET " + params.offset);
            }

            List<ClientRecord> clientList = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
                if (params.filter != null && params.filterCol != null)
                    ps.setString(1, params.filter + "%");
//
//                if (params.filterCol != null && params.filterCol == "name" && params.filter != null)
//                    ps.setString(2, params.filter + "%");
//
//                if (params.filterCol != null && params.filterCol == "patronymic" && params.filter != null)
//                    ps.setString(3, params.filter + "%");

//                if (params.limit != 0)
//                    ps.setInt(4, params.limit);
//
//                if (params.offset != 0)
//                    ps.setInt(5, params.offset);

                System.out.println(ps);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ClientRecord record = new ClientRecord();

                        record.id = rs.getInt("id");
                        record.fio = rs.getString("surname") + " " + rs.getString("name");
                        if (rs.getString("patronymic") != null) {
                            record.fio += " " + rs.getString("patronymic");
                        }
                        record.surname = rs.getString("surname");
                        record.name = rs.getString("name");
                        record.patronymic = rs.getString("patronymic");
                        record.charm = rs.getString("charm");
                        record.age = computeAge(rs.getDate("birth_date"));
                        record.totalBalance = rs.getFloat("totalBalance");
                        record.maxBalance = rs.getFloat("maxBalance");
                        record.minBalance = rs.getFloat("minBalance");

                        clientList.add(record);
                    }
                    return clientList;
                }
            }
        });
    }

    private int computeAge(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int bYear = cal.get(Calendar.YEAR);
        int bMonth = cal.get(Calendar.MONTH) + 1;
        int bDay = cal.get(Calendar.DAY_OF_MONTH);

        Calendar calCurrent = Calendar.getInstance();
        int cYear = calCurrent.get(Calendar.YEAR);
        int cMonth = calCurrent.get(Calendar.MONTH) + 1;
        int cDay = calCurrent.get(Calendar.DAY_OF_MONTH);

        int age = cYear - bYear; // date = 21.03.1995     current = 23.09.2018

        if (bMonth > cMonth) {   // date = 17.10.1995     current = 23.09.2018
            age -= 1;
        }

        if (bMonth == cMonth) {
            if (bDay > cDay) {   // date = 28.09.1995     current = 23.09.2018
                age -= 1;
            }
        }
        return age;
    }

    @Override
    public void deleteClient(int id) {
        clientDao.get().deleteClientByID(id);
    }

    @Override
    public void editClient(ClientDetail cd) {
        if (cd.id == 0) {
            addNewClient(cd);
        } else {
            editExistedClient(cd);
        }
    }

    @Override
    public ClientDetail getDetails(int id) {
        return clientDao.get().selectClientByID(id);
    }

    @Override
    public List<Charm> getCharmsList() {
        return jdbc.get().execute(con -> {
            String sql = "select * from charm";

            List<Charm> charms = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Charm c = new Charm();
                        c.id = rs.getInt("id");
                        c.value = rs.getString("name");
                        c.label = rs.getString("name");
                        c.description = rs.getString("description");
                        c.energy = rs.getFloat("energy");

                        charms.add(c);
                    }
                    return charms;
                }
            }
        });
    }

    private void editExistedClient(ClientDetail cd) {
        clientDao.get().updateClientField(cd.id, "surname", cd.surname);
        clientDao.get().updateClientField(cd.id, "name", cd.name);

        if (cd.patronymic != null)
            clientDao.get().updateClientField(cd.id, "patronymic", cd.patronymic);

        clientDao.get().updateClientField(cd.id, "gender", cd.gender);

        clientDao.get().updateClientField(cd.id, "birth_date", cd.birthDate);

        int charm = clientDao.get().selectCharmIdByName(cd.charm);

        clientDao.get().updateClientField(cd.id, "charm", charm);

        updateFactAddress(cd);

        //reg
        clientDao.get().updateClientAddrREGField(cd.id, "street", cd.regStreet);

        clientDao.get().updateClientAddrREGField(cd.id, "house", cd.regNo);

        clientDao.get().updateClientAddrREGField(cd.id, "flat", cd.regFlat);

        //phones
        updatePhoneNumber(cd.id, cd.homePhoneNumber, "HOME");

        updatePhoneNumber(cd.id, cd.workPhoneNumber, "WORK");

        //mobile
        clientDao.get().updateClientPhoneNumber(cd.id, cd.mobileNumber1, "MOBILE1");

        checkAndUpdateMobileNumber(cd.id, cd.mobileNumber2, "MOBILE2");

        checkAndUpdateMobileNumber(cd.id, cd.mobileNumber3, "MOBILE3");

    }

    private void updatePhoneNumber(int id, String phoneNumber, String type) {
        if (phoneNumber != null) {
            if (clientDao.get().checkForExistPhoneNumberType(id, type) != null) {//update
                clientDao.get().updateClientPhoneNumber(id, phoneNumber, type);
            } else {//insert
                clientDao.get().insertIntoClientPhone(id, type, phoneNumber);
            }
        }
    }

    private void updateFactAddress(ClientDetail cd) {
        if (clientDao.get().checkIfFactAddressRecordExists(cd.id) == null) {
            clientDao.get().insertIntoClientAddrFACT(cd);
        } else {
            if (cd.factStreet != null)
                clientDao.get().updateClientAddrFACTField(cd.id, "street", cd.factStreet);
            if (cd.factNo != null)
                clientDao.get().updateClientAddrFACTField(cd.id, "house", cd.factNo);
            if (cd.factFlat != null)
                clientDao.get().updateClientAddrFACTField(cd.id, "flat", cd.factFlat);
        }
    }

    private void addNewClient(ClientDetail cd) {
        System.out.println("adding... ");
        int id = RND.plusInt(1000000);

        int charm = clientDao.get().selectCharmIdByName(cd.charm);
        clientDao.get().insertIntoClient(id, cd, charm);
        cd.id = id;

        if (cd.patronymic != null)
            clientDao.get().updateClientField(id, "patronymic", cd.patronymic);

        if (cd.factStreet != null)
            clientDao.get().insertIntoClientAddrFACT(cd);

        clientDao.get().insertIntoClientAddrREG(cd);

        if (cd.homePhoneNumber != null)
            clientDao.get().insertIntoClientPhone(id, "HOME", cd.homePhoneNumber);

        if (cd.workPhoneNumber != null)
            clientDao.get().insertIntoClientPhone(id, "WORK", cd.workPhoneNumber);

        clientDao.get().insertIntoClientPhone(id, "MOBILE1", cd.mobileNumber1);

        checkMobileNumber(id, cd.mobileNumber2, "MOBILE2");

        checkMobileNumber(id, cd.mobileNumber3, "MOBILE3");
    }

    void checkMobileNumber(int id, String mobileNumber, String type) {
        if (mobileNumber != null) {

            //1. check with M1, M2, M3 numbers
            if (clientDao.get().checkForDublicateMobileNumber(id, mobileNumber) == null) { //if such number does not exist insert
                clientDao.get().insertIntoClientPhone(id, type, mobileNumber);
            }
        }
    }

    void checkAndUpdateMobileNumber(int clientID, String number, String type) {
        if (number != null) {
            //check for dublicate
            //if not exist such mobile number for that client
            if (clientDao.get().checkForDublicateMobileNumber(clientID, number) == null) {
                //check for exiting mobile number for client for that type
                if (clientDao.get().checkForExistPhoneNumberType(clientID, type) == null) {//insert
                    clientDao.get().insertIntoClientPhone(clientID, type, number);
                } else {//update
                    clientDao.get().updateClientPhoneNumber(clientID, number, type);
                }
            }
        }
    }

}

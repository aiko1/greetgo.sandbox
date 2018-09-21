package kz.greetgo.sandbox.register.impl;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.model.ClientDetail;
import kz.greetgo.sandbox.controller.model.ClientRecord;
import kz.greetgo.sandbox.controller.model.FilterParams;
import kz.greetgo.sandbox.controller.register.ClientRegister;
import kz.greetgo.sandbox.register.dao.ClientDao;
import kz.greetgo.sandbox.register.util.JdbcSandbox;
import kz.greetgo.util.RND;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Bean
public class ClientRegisterImpl implements ClientRegister {

    public BeanGetter<ClientDao> clientDao;
    public BeanGetter<JdbcSandbox> jdbc;

    @Override
    public List<ClientRecord> getClients(FilterParams params) {

        //JDBC
        return jdbc.get().execute(con -> {
            StringBuilder sql = new StringBuilder("Select * from client where actual = 1");

            if (!params.filter.isEmpty() && !params.filterCol.isEmpty()) {
                sql.append(" and lower(" + params.filterCol + ") like '" + params.filter + "%'");
            }

            if (!params.sortBy.isEmpty() && !params.sortDir.isEmpty()) {
                sql.append(" ORDER BY " + params.sortBy + " " + params.sortDir);
            }
            List<ClientRecord> clientList = new ArrayList<>();
            try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        ClientRecord record = new ClientRecord();
                        record.fio = rs.getString("surname") + " " + rs.getString("name");
                        record.id = rs.getInt("id");
                        clientList.add(record);
                    }
                    return clientList;
                }
            }
        });
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

    private void editExistedClient(ClientDetail cd) {
        clientDao.get().updateClientField(cd.id, "surname", cd.surname);
        clientDao.get().updateClientField(cd.id, "name", cd.name);

        if (cd.patronymic != null)
            clientDao.get().updateClientField(cd.id, "patronymic", cd.patronymic);

        clientDao.get().updateClientField(cd.id, "gender", cd.gender);

        clientDao.get().updateClientField(cd.id, "birth_date", cd.birthDate);

        clientDao.get().updateClientField(cd.id, "charm", cd.charm);

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

        clientDao.get().insertIntoClient(id, cd);
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

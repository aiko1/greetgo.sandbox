package kz.greetgo.sandbox.register.test.beans.develop;

import kz.greetgo.depinject.core.Bean;
import kz.greetgo.depinject.core.BeanGetter;
import kz.greetgo.sandbox.controller.model.ClientDetail;
import kz.greetgo.sandbox.controller.model.UserCan;
import kz.greetgo.sandbox.register.beans.all.IdGenerator;
import kz.greetgo.sandbox.register.test.dao.AuthTestDao;
import kz.greetgo.sandbox.register.test.dao.ClientTestDao;
import kz.greetgo.security.password.PasswordEncoder;
import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Bean
public class DbLoader {
    final Logger logger = Logger.getLogger(getClass());


    public BeanGetter<AuthTestDao> authTestDao;
    public BeanGetter<ClientTestDao> clientTestDao;
    public BeanGetter<IdGenerator> idGenerator;
    public BeanGetter<PasswordEncoder> passwordEncoder;

    public void loadTestData() throws Exception {

        loadPersons();
        loadClients();

        logger.info("FINISH");
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void loadClients() throws ParseException {
        load_charm_list();
        load_client_list();
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void loadPersons() throws Exception {
        logger.info("Start loading persons...");

        user("Пушкин Александр Сергеевич", "1799-06-06", "pushkin");
        user("Сталин Иосиф Виссарионович", "1878-12-18", "stalin");
        user("Берия Лаврентий Павлович", "1899-03-17", "beria");
        user("Есенин Сергей Александрович", "1895-09-21", "esenin");
        user("Путин Владимир Владимирович", "1952-10-07", "putin");
        user("Назарбаев Нурсултан Абишевич", "1940-07-06", "papa");
        user("Менделеев Дмитрий Иванович", "1834-02-08", "mendeleev");
        user("Ломоносов Михаил Васильевич", "1711-11-19", "lomonosov");
        user("Бутлеров Александр Михайлович", "1828-09-15", "butlerov");

        add_can("pushkin", UserCan.VIEW_USERS);
        add_can("stalin", UserCan.VIEW_USERS);
        add_can("stalin", UserCan.VIEW_ABOUT);

        logger.info("Finish loading persons");
    }

    private void user(String fioStr, String birthDateStr, String accountName) throws Exception {
        String id = idGenerator.get().newId();
        String[] fio = fioStr.split("\\s+");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = sdf.parse(birthDateStr);
        String encryptPassword = passwordEncoder.get().encode("111");

        authTestDao.get().insertPerson(id, accountName, encryptPassword);
        authTestDao.get().updatePersonField(id, "birth_date", new Timestamp(birthDate.getTime()));
        authTestDao.get().updatePersonField(id, "surname", fio[0]);
        authTestDao.get().updatePersonField(id, "name", fio[1]);
        authTestDao.get().updatePersonField(id, "patronymic", fio[2]);
    }

    private void add_can(String username, UserCan... cans) {
        for (UserCan can : cans) {
            authTestDao.get().upsert(can.name());
            authTestDao.get().personCan(username, can.name());
        }
    }

    private void load_charm_list() {
        //load charm list
        clientTestDao.get().insertCharm(1, "sociable");
        clientTestDao.get().insertCharm(2, "polite");
        clientTestDao.get().insertCharm(3, "quiet");
        clientTestDao.get().insertCharm(4, "aggressive");
        clientTestDao.get().insertCharm(5, "ambitious");
        clientTestDao.get().insertCharm(6, "intelligent");
        clientTestDao.get().insertCharm(7, "honest");
        clientTestDao.get().insertCharm(8, "daring");
        clientTestDao.get().insertCharm(9, "reliable");
        clientTestDao.get().insertCharm(10, "artistic");
        clientTestDao.get().insertCharm(11, "patient");
        clientTestDao.get().insertCharm(12, "sensitive");
    }


    private void load_client_list() {
        //load client list
        loadTestClient(1, "Kim", "Igor", "MALE", "1993-11-12", 1, 2, "s1", "h1", "f1", "87758852542");
        loadTestClient(2, "Ivanov", "Alexey", "MALE", ("1995-11-05"), 1, 1, "s2", "h2", "f2", "85245626966");
        loadTestClient(3, "Coi", "Vika", "FEMALE", ("1992-11-02"), 1, 5, "s3", "h3", "f3", "87002006644");
        loadTestClient(4, "Li", "Andrey", "MALE", ("1995-10-12"), 1, 12, "s4", "f4", "h4", "87002003021");
        loadTestClient(5, "Mihailova", "Nadezhda", "FEMALE", ("1995-01-10"), 1, 9, "s5", "f5", "h5", "87002003022");
        loadTestClient(6, "Nikulin", "Yuriy", "MALE", ("1997-07-19"), 1, 5, "s6", "f6", "h6", "87002003023");
        loadTestClient(7, "Ahmetov", "Ahmet", "MALE", ("1982-09-05"), 1, 3, "s7", "f7", "h7", "87002003024");
        loadTestClient(8, "Igoreva", "Natazha", "FEMALE", ("1986-04-17"), 1, 5, "s84", "f84", "h84", "87002000025");
        loadTestClient(9, "Nikitin", "Alex", "MALE", ("1991-08-06"), 1, 2, "s49", "f94", "h94", "87002003026");
        loadTestClient(10, "Medvedeva", "Tanya", "FEMALE", ("1994-01-11"), 1, 4, "s41", "f41", "h14", "87002003027");
        loadTestClient(11, "Iureva", "Viktoria", "FEMALE", ("1998-06-18"), 1, 8, "s24", "f24", "h24", "87020030028");
        loadTestClient(12, "Li", "Dmitriy", "MALE", ("1994-08-25"), 1, 1, "s34", "f34", "h34", "87002003029");
        loadTestClient(13, "Kim", "Kristina", "FEMALE", ("1990-04-16"), 1, 10, "s54", "f54", "h54", "87002030020");
        loadTestClient(14, "Romanov", "Sasha", "MALE", ("1997-03-17"), 1, 11, "s64", "f64", "h46", "87002030051");
        loadTestClient(15, "Romanova", "Kim", "FEMALE", ("1991-12-11"), 1, 2, "s74", "f74", "h74", "87002003055");
    }

    void loadTestClient(int id, String surname, String name, String gender, String birthDate, int actual,
                   int charm, String regStreet, String regNo, String regFlat, String mobileNumber1) {
        ClientDetail cd = new ClientDetail(id, surname, name, gender, java.sql.Date.valueOf(birthDate), actual, charm, regStreet, regNo, regFlat, mobileNumber1);
        clientTestDao.get().insertTestClient(cd);
        clientTestDao.get().insertTestAddrREG(cd);
        clientTestDao.get().insertTestPhone(cd);
    }
}

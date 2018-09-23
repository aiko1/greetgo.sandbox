package kz.greetgo.sandbox.register.test.dao;

import kz.greetgo.sandbox.controller.model.ClientDetail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ClientTestDao {
    @Insert("insert into charm values (#{id}, #{name})")
    void insertCharm(@Param("id") int id,
                     @Param("name") String name);

    @Insert("insert into client_account (id, client) values (#{id}, #{client})")
    void insertDefaultAccount(@Param("id") int id,
                              @Param("client") int client);

    @Select("select id from charm where name=#{name}")
    int selectCharmIdByName(@Param("name") String name);

    @Insert("insert into client (id, surname, name, gender, birth_date, actual, charm) " +
            "values (#{cd.id}, #{cd.surname}, #{cd.name}, #{cd.gender}, " +
            "#{cd.birthDate}, #{cd.actual}, #{charm})")
    void insertTestClient(@Param("cd") ClientDetail cd,
                          @Param("charm") int charm);

    @Update("update client set ${fieldName} = #{fieldValue} where id = #{id}")
    void updateClientField(@Param("id") int id,
                           @Param("fieldName") String fieldName,
                           @Param("fieldValue") Object fieldValue);

    @Insert("insert into client_addr (client, type, street, house, flat) " +
            "values (#{cd.id}, 'REG', #{cd.regStreet}, #{cd.regNo}, #{cd.regFlat})")
    void insertTestAddrREG(@Param("cd") ClientDetail cd);

    @Insert("insert into client_phone (client, type, number) " +
            "values (#{cd.id}, 'MOBILE1', #{cd.mobileNumber1})")
    void insertTestPhone(@Param("cd") ClientDetail cd);

    @Insert("insert into client (id, surname, name, actual) " +
            "values (#{id}, #{surname}, #{name}, #{actual})")
    void insertNotFullClient(@Param("id") int id,
                             @Param("surname") String surname,
                             @Param("name") String name,
                             @Param("actual") int actual
    );

    @Update("update client set actual = 0")
    void deleteAllClients();

    @Select("select id, surname, name, patronymic, gender, birth_date as birthDate, actual,\n" +
            "(select name from charm where id=c.charm) as charm,\n" +
            "(select street from client_addr where client=a.client and type='FACT') as factStreet,\n" +
            "(select house from client_addr where client=a.client and type='FACT') as factNo,\n" +
            "(select flat from client_addr where client=a.client and type='FACT') as factFlat,\n" +
            "(select street from client_addr where client=a.client and type='REG') as regStreet,\n" +
            "(select house from client_addr where client=a.client and type='REG') as regNo,\n" +
            "(select flat from client_addr where client=a.client and type='REG') as regFlat,\n" +
            "(select number from client_phone where client=phone.client and type='HOME') as homePhoneNumber,\n" +
            "(select number from client_phone where client=phone.client and type='WORK') as workPhoneNumber,\n" +
            "(SELECT number FROM client_phone where type='MOBILE1' and client=phone.client) as mobileNumber1,\n" +
            "(SELECT number FROM client_phone where type='MOBILE2' and client=phone.client) as mobileNumber2,\n" +
            "(SELECT number FROM client_phone where type='MOBILE3' and client=phone.client) as mobileNumber3\n" +
            "from client c left join client_addr a on c.id = a.client left join client_phone phone on c.id = phone.client\n" +
            "where c.id=#{id} and c.actual=1 limit 1")
    ClientDetail selectClientByID(@Param("id") int id);

    @Select("select id, surname, name, patronymic, gender, birth_date as birthDate, actual,\n" +
            "(select name from charm where id=c.charm) as charm,\n" +
            "(select street from client_addr where client=a.client and type='FACT') as factStreet,\n" +
            "(select house from client_addr where client=a.client and type='FACT') as factNo,\n" +
            "(select flat from client_addr where client=a.client and type='FACT') as factFlat,\n" +
            "(select street from client_addr where client=a.client and type='REG') as regStreet,\n" +
            "(select house from client_addr where client=a.client and type='REG') as regNo,\n" +
            "(select flat from client_addr where client=a.client and type='REG') as regFlat,\n" +
            "(select number from client_phone where client=phone.client and type='HOME') as homePhoneNumber,\n" +
            "(select number from client_phone where client=phone.client and type='WORK') as workPhoneNumber,\n" +
            "(SELECT number FROM client_phone where type='MOBILE1' and client=phone.client) as mobileNumber1,\n" +
            "(SELECT number FROM client_phone where type='MOBILE2' and client=phone.client) as mobileNumber2,\n" +
            "(SELECT number FROM client_phone where type='MOBILE3' and client=phone.client) as mobileNumber3\n" +
            "from client c left join client_addr a on c.id = a.client left join client_phone phone on c.id = phone.client\n" +
            "where c.name=#{name} and c.actual=1 limit 1")
    ClientDetail selectClientByName(@Param("name") String name);
}

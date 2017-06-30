package prj.jersey.simonExp.example;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FileUtils;
import org.hibernate.Session;

import com.opencsv.CSVReader;

import enums.CurrencyType;
import util.HibernateUtil;

@Path("jerseyExample")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        List<Locale> localeList = Arrays.asList(Locale.getAvailableLocales());
        List<Locale> sortedLocale = localeList.stream().sorted(
            (L1, L2) -> L1.getCountry().compareTo(L2.getCountry()))
            .collect(Collectors.toList());
        for (Locale locale : sortedLocale) {
            System.out.println(
                locale.getCountry() + " " +
                    locale.getDisplayCountry() + " " +
                    locale.getLanguage());
        }

        System.out.println();
        System.out.println(CurrencyType.THB);

        return "Got it!";
    }

    @GET
    @Path("getfile")
    @Produces(MediaType.TEXT_PLAIN)
    public String getFileTest(
        @QueryParam("urlString") String urlString) {

        CSVReader reader = null;
        File file = new File("csvfile");
        try {
            URL url = new URL(urlString);
            FileUtils.copyURLToFile(url, file);
            reader = new CSVReader(new FileReader(file));
            String[] line;
            while ((line = reader.readNext()) != null) {
                System.out.println(Arrays.toString(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Got it!";
    }

    @Path("testHibernate")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void testHibernate() {
        // Session session = HibernateUtil.getSessionFactory().openSession();
        Session session = HibernateUtil.getHibernateSession();

        try {
            session.beginTransaction();

            Department department = new Department("java");
            session.save(department);

            session.save(new Employee("Jakab Gipsz", department));
            session.save(new Employee("Captain Nemo", department));

            session.getTransaction().commit();

            List<Employee> resultList = session.createQuery("From Employee ", Employee.class).list();

            System.out.println("num of employess:" + resultList.size());
            for (Employee next : resultList) {
                System.out.println("next employee: " + next);
            }
        } finally {
            session.close();
        }
    }

    @Path("testHibernateGet")
    @GET
    public void testHibernateGet() {
        System.out.println(HibernateUtil.basicRead(Employee.EntityName));

        HashMap<String, Object> params = new HashMap<>();
        params.put("name", "Jakab Gipsz");
        System.out.println(HibernateUtil.basicRead(Employee.EntityName, params));
    }

    @Path("testHibernateUpdate/{id}/{name}")
    @PUT
    public void testHibernateUpdate(
        @PathParam("id") Long id,
        @PathParam("name") String name) {
        System.out.println(HibernateUtil.basicRead(Employee.EntityName));

        System.out.println("find for id: " + id);

        Employee employee = (Employee) HibernateUtil.basicReadById(Employee.class, id);
        System.out.println(employee);

        employee.setName(name);
        HibernateUtil.basicUpdate(Employee.EntityName, employee);

        System.out.println(HibernateUtil.basicReadById(Employee.class, id));
    }

    @Path("testHibernateDelete/{id}")
    @DELETE
    public void testHibernateDelete(@PathParam("id") Long id) {
        System.out.println(HibernateUtil.basicRead(Employee.EntityName));
        System.out.println("find for id: " + id);
        Employee employee = (Employee) HibernateUtil.basicReadById(Employee.class, id);

        System.out.println("delete id: " + id);
        HibernateUtil.basicDelete(Employee.EntityName, employee);
    }
}

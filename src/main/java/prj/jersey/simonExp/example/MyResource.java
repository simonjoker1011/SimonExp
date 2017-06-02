package prj.jersey.simonExp.example;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.Session;

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
        return "Got it!";
    }

    @Path("testHibernate")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void getRecord() {
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
}

package utils.redshift.hibernate;

import entities.Log;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * Created by serhii on 06.04.15.
 */
public class HibernateRunner {
    public static void main(String args[]){
        SessionFactory sf= new AnnotationConfiguration().configure().buildSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        Log log =new Log();
        log.setId(Math.random()*100+"");
        log.setTime("time" + Math.random() * 100 + "");
        log.setKey("key" + Math.random() * 100 + "");
        log.setValue("value" + Math.random() * 100 + "");
        log.setUserId((long)Math.random()*100);
        session.save(log);
        session.getTransaction().commit();
        session.close();
        sf.close();

    }
}

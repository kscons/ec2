package utils.redshift.hibernate;

import configurations.servicesconfigurators.RedshiftConfigurator;
import entities.Log;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hibernate.Session;

import java.util.ArrayList;
@Deprecated
public class RedshiftHibernateUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RedshiftHibernateUtil.class);
    private  SessionFactory sf;
    private static RedshiftHibernateUtil instance=null;

    private RedshiftHibernateUtil() {
        Configuration configuration = new Configuration().configure();

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        sf = configuration.buildSessionFactory(builder.build());

    }



    public  void insertLog(final Log log){
        Session session=sf.openSession();
        session.beginTransaction();
        LOG.info("\t Redshift: " + log + " into Table " + RedshiftConfigurator.getLogsRedshiftOutputTableName() + " successfully inserted");
        session.save(log);
        session.getTransaction().commit();
        session.close();

    }
    public  void insertLogs(final ArrayList<Log> logs){
        Session session=sf.openSession();
        session.beginTransaction();
        for(Log log:logs){
        LOG.info("\t Redshift: " + log + " into Table " + RedshiftConfigurator.getLogsRedshiftOutputTableName() + " successfully inserted");
        session.save(log);}
        session.getTransaction().commit();
        session.close();

    }

    public static RedshiftHibernateUtil getInstance(){
        if (instance==null){
            instance=new RedshiftHibernateUtil();
        }
        return instance;
    }

}

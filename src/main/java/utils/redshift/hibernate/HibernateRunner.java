package utils.redshift.hibernate;

import entities.Log;

/**
 * Created by serhii on 06.04.15.
 */
public class HibernateRunner {
    public static void main(String args[]){


        Log log =new Log();
        log.setId(Math.random()*100+"");
        log.setTimestamp("time" + Math.random() * 100 + "");
        log.setKey("key" + Math.random() * 100 + "");
        log.setValue("value" + Math.random() * 100 + "");
        log.setUserId((long) Math.random() * 100);
       RedshiftHibernateUtil.getInstance().insertLog(log);

    }
}

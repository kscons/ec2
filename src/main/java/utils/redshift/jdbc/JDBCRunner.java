package utils.redshift.jdbc;


import entities.Log;

import java.util.ArrayList;

public class JDBCRunner {
    private static long longPause = 5000;
    private static String tableName = "logs";

    public static void main(String ars[]) {

        /**
         * Instruction
         *
         * If you want to run  this example of data processing first of all you should to configure Redshift:
         * 1) go to AWS Redshift console
         * 2) choose our cluster -logitech-analytics-ksolod (Ireland region)
         * 3) enter the Security in left side of screen
         * 4)click on "default" group
         * 5)add connection -type CIDR/IP - and choose your IP (current IP will be proposed)
         * You can also see some information and instructions in Redshift Documentation
         * @link- https://docs.google.com/a/logitech.com/document/d/10XhPmQftuo0H9p0yrJDJ35BtEskdjF2kX2bBtkJAsnM/edit}
         *
         */

        ArrayList<Log> listOfLogs;
        RedshiftJDBCUtil ru = new RedshiftJDBCUtil();
        /*
        try {
            ru.createConnection();
        } catch (NoConnectionException nce) {
            nce.printStackTrace();
        }*/
        //ru.deleteTable(tableName);
        //  ru.createTableForLogs(tableName);
        //  ru.insertLog(tableName, new Log());

        listOfLogs = ru.getAllLogsFromTable(tableName);
        System.out.println("\n\n\n\t\t  Getting All logs" + "\t\tCount of logs : " + listOfLogs.size());
        doPause(longPause);
        for (Log log : listOfLogs) {
            System.out.println(log);
        }


        System.out.println("\n\n\n\t\t  Start of processing");
        doPause(longPause);
        /**
         * Group by range query
         * any range from [0..100000]
         */
        listOfLogs = ru.getLogsFromTableByIDRange(tableName, 0, 10000);
        System.out.println("\n\n\n\t\t Getting logs by id  in range= [0,10000] \n" + "\t\tCount of logs : " + listOfLogs.size() + "\n\n");
        doPause(longPause);
        for (Log log : listOfLogs) {
            System.out.println(log);
        }

        /**
         * Group by value query
         * -1.2.3.4
         * -2.3.4.5
         * -5.6.7.8
         */

        String value = "2.3.4.5";
        listOfLogs = ru.getLogsFromTableByValue(tableName, value);
        System.out.println("\n\n\n\t\tGetting logs by value= [" + value + "]" + "\t\tCount of logs : " + listOfLogs.size() + "\n\n");
        doPause(longPause);
        for (Log log : listOfLogs) {
            System.out.println(log);
        }

        /**
         * Group by key query query
         * -fake_event
         * -push_event
         * -get_event
         */

        String key = "push_event";
        listOfLogs = ru.getLogsFromTableByKey(tableName, key);
        System.out.println("\n\n\t\t Getting logs by key= [" + key + "]\t\tCount of logs : " + listOfLogs.size() + "\n\n");
        doPause(longPause);
        for (Log log : listOfLogs) {
            System.out.println(log);
        }


        ru.closeConnection();

    }

    private static void doPause(long pause) {
        try {
            Thread.sleep(pause);
        } catch (InterruptedException ie) {
        }
    }
}

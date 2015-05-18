package autodeploy.entities.redshifttable;

import autodeploy.entities.Item;
import utils.redshift.jdbc.RedshiftJDBCUtil;
import utils.sqs.SQSUtil;

/**
 * Created by Logitech on 08.05.15.
 */
public class RedshiftTable implements Item {
    private String name;

    public RedshiftTable(String name) {
        this.name = name;
    }

    @Override
    public void create() {
        RedshiftJDBCUtil.createTableForLogs(name);
    }

    @Override
    public void delete() {
        RedshiftJDBCUtil.deleteTable(name);
    }

    @Override
    public boolean isCreated() {
        return true;
    }


    public String getName() {
        return name;
    }

}

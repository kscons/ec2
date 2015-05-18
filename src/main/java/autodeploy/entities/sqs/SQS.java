package autodeploy.entities.sqs;

import autodeploy.entities.Item;
import utils.sqs.SQSUtil;

/**
 * Created by Logitech on 05.05.15.
 */
public class SQS implements Item {
    private String name;
    public SQS(String name) {
        this.name=name;
    }

    @Override
    public void create() {
        SQSUtil.createQueue(name);
    }

    @Override
    public void delete() {
        SQSUtil.deleteQueue(name);
    }

    @Override
    public boolean isCreated() {
       return  SQSUtil.isExist(name);
    }


    public String getName() {
        return name;
    }

}

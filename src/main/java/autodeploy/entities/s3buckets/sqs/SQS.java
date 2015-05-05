package autodeploy.entities.s3buckets.sqs;

import autodeploy.entities.Item;
import utils.s3.S3Util;
import utils.sqs.SQSUtil;

/**
 * Created by Logitech on 05.05.15.
 */
public class SQS implements Item {
    private String name;
    private boolean isCreated;
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
    public void isCreated() {
        isCreated= SQSUtil.isExist(name);
    }


    public String getName() {
        return name;
    }

}

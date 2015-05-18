package autodeploy.entities.s3buckets;

import autodeploy.entities.Item;
import utils.s3.S3Util;

/**
 * Created by Logitech on 05.05.15.
 */
public abstract class Bucket implements Item {
    private String name;
    private boolean isCreated;

    public Bucket(String name) {
        // Output Bucket can't contains uppercase letters
        this.name = name.toLowerCase();
    }

    @Override
    public void create() {
        S3Util.createBucket(name);
    }

    @Override
    public void delete() {
        S3Util.deleteBucket(name);
    }

    @Override
    public boolean isCreated() {
        return isCreated = S3Util.isExist(name);
    }


    public String getName() {
        return name;
    }
}

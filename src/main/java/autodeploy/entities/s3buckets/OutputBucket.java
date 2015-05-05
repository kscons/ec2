package autodeploy.entities.s3buckets;

import utils.s3.S3Util;

/**
 * Created by Logitech on 05.05.15.
 */
public class OutputBucket implements Bucket {
    private String name;
    private boolean isCreated;
    public OutputBucket(String name) {
        // Output Bucket can't contains uppercase letters
       this.name=name.toLowerCase();
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
    public void isCreated() {
        isCreated= S3Util.isExist(name);
    }


    public String getName() {
        return name;
    }
}

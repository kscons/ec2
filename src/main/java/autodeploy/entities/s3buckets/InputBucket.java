package autodeploy.entities.s3buckets;


import utils.s3.S3Util;

/**
 * Created by Logitech on 05.05.15.
 */
public class InputBucket extends OutputBucket implements Bucket {
    public InputBucket(String name) {
        super(name);
    }

    @Override
    public void create() {
        super.create();

    }
    public void addPermissions(){
        S3Util.setPublicPermissions(super.getName());
    }
}

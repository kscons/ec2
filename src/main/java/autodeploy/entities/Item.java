package autodeploy.entities;

/**
 * Created by Logitech on 05.05.15.
 */
public interface Item {
    public void create();
    public void delete();
    public boolean isCreated();
}

package entities.datacollectionmap;

/**
 * Created by Logitech on 13.04.15.
 */
public class Source {
    private String name;
    private String version;

    public Source() {
    }

    public Source(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Source source = (Source) o;

        if (name != null ? !name.equals(source.name) : source.name != null) return false;
        return !(version != null ? !version.equals(source.version) : source.version != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Source{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}

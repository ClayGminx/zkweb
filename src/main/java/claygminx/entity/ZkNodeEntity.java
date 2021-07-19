package claygminx.entity;

import org.apache.zookeeper.data.Stat;

import java.io.Serializable;

public class ZkNodeEntity {

    private String connectionId;
    private String path;
    private String value;
    private Stat stat;

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }
}

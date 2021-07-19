package claygminx.service;

import claygminx.exception.ServiceException;
import org.apache.zookeeper.data.Stat;

import java.io.Serializable;
import java.util.List;

public interface ZkNodeService {

    List<String> getChildren(String connectionId, String path) throws ServiceException;

    byte[] getByteArrayValue(String connectionId, String path) throws ServiceException;

    String getStringValue(String connectionId, String path) throws ServiceException;

    Stat getStat(String connectionId, String path) throws ServiceException;

    void create(String connectionId, String path, Serializable data) throws ServiceException;

    void update(String connectionId, String path, Serializable data) throws ServiceException;

    void delete(String connectionId, String path) throws ServiceException;
}

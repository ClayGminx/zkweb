package claygminx.service;

import claygminx.entity.ZkConnectionEntity;
import claygminx.exception.ServiceException;
import org.apache.curator.framework.CuratorFramework;

import java.util.List;

public interface ZkConnectionService {

    void add(ZkConnectionEntity zkConnectionEntity) throws ServiceException;

    void delete(String id) throws ServiceException;

    List<ZkConnectionEntity> getAll();

    CuratorFramework getClient(String id);
}

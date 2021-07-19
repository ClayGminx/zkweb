package claygminx.service.impl;

import claygminx.exception.ServiceException;
import claygminx.service.ZkConnectionService;
import claygminx.service.ZkNodeService;
import cn.hutool.core.util.ObjectUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ZkNodeServiceImpl implements ZkNodeService {

    private final static Logger logger = LoggerFactory.getLogger(ZkNodeService.class);

    @Resource
    private ZkConnectionService zkConnectionService;

    @Override
    public List<String> getChildren(String connectionId, String path) throws ServiceException {
        CuratorFramework client = getClient(connectionId);

        existsPath(client, path);

        try {
            List<String> result = client.getChildren().forPath(path);
            return Optional.ofNullable(result).orElse(new ArrayList<>(0));
        } catch (Exception e) {
            logger.error("获取子节点时发生异常", e);
            throw new ServiceException("获取失败");
        }
    }

    @Override
    public byte[] getByteArrayValue(String connectionId, String path) throws ServiceException {
        CuratorFramework client = getClient(connectionId);

        existsPath(client, path);

        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            logger.error("获取节点值时发生异常", e);
            throw new ServiceException("获取失败");
        }
    }

    @Override
    public String getStringValue(String connectionId, String path) throws ServiceException {
        byte[] bytes = getByteArrayValue(connectionId, path);
        if (bytes == null) {
            return "";
        }
        return ObjectUtil.deserialize(bytes);
    }

    @Override
    public Stat getStat(String connectionId, String path) throws ServiceException {
        CuratorFramework client = getClient(connectionId);

        existsPath(client, path);

        try {
            return client.checkExists().forPath(path);
        } catch (Exception e) {
            logger.error("获取节点状态时发生异常", e);
            throw new ServiceException("获取失败");
        }
    }

    @Override
    public void create(String connectionId, String path, Serializable data) throws ServiceException {
        CuratorFramework client = getClient(connectionId);

        Stat checkResult = checkExists(client, path);
        if (checkResult != null) {
            throw new ServiceException("节点已存在");
        }

        try {
            if (data == null) {
                client.create().forPath(path);
            } else {
                byte[] bytes = ObjectUtil.serialize(data);
                client.create().forPath(path, bytes);
            }
        } catch (Exception e) {
            logger.error("创建失败", e);
            throw new ServiceException("创建失败");
        }
    }

    @Override
    public void update(String connectionId, String path, Serializable data) throws ServiceException {
        CuratorFramework client = getClient(connectionId);

        existsPath(client, path);

        try {
            if (data == null) {
                client.setData().forPath(path, null);
            } else {
                byte[] bytes = ObjectUtil.serialize(data);
                client.setData().forPath(path, bytes);
            }
        } catch (Exception e) {
            logger.error("更新失败", e);
            throw new ServiceException("更新失败");
        }
    }

    @Override
    public void delete(String connectionId, String path) throws ServiceException {
        CuratorFramework client = getClient(connectionId);

        Stat stat = checkExists(client, path);

        if (stat != null) {
            try {
                client.delete().forPath(path);
            } catch (Exception e) {
                logger.error("删除失败", e);
                throw new ServiceException("删除失败");
            }
        }
    }

    private CuratorFramework getClient(String connectionId) throws ServiceException {
        CuratorFramework client = zkConnectionService.getClient(connectionId);
        if (client == null) {
            throw new ServiceException("连接不存在");
        }
        return client;
    }

    private Stat checkExists(CuratorFramework client, String path) throws ServiceException {
        try {
            return client.checkExists().forPath(path);
        } catch (Exception e) {
            logger.error("与ZooKeeper交互时发生异常");
            throw new ServiceException("与ZooKeeper交互时发生异常");
        }
    }

    private void existsPath(CuratorFramework client, String path) throws ServiceException {
        Stat stat = checkExists(client, path);
        if (stat == null) {
            throw new ServiceException("节点不存在");
        }
    }
}

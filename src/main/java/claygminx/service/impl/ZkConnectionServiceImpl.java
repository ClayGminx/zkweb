package claygminx.service.impl;

import claygminx.entity.ZkConnectionEntity;
import claygminx.exception.ServiceException;
import claygminx.service.ZkConnectionService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ZkConnectionServiceImpl implements ZkConnectionService {

    private final static Logger logger = LoggerFactory.getLogger(ZkConnectionService.class);

    private final static Map<String, CuratorFramework> zkConnectionMap = new ConcurrentHashMap<>();

    @Value("${zk.max}")
    private int maxConnectionCount;

    @Override
    public void add(ZkConnectionEntity zkConnectionEntity) throws ServiceException {
        if (zkConnectionEntity.getId() == null || zkConnectionEntity.getId().isEmpty()) {
            throw new ServiceException("ID必填");
        }
        if (zkConnectionEntity.getConnectString() == null | zkConnectionEntity.getConnectString().isEmpty()) {
            throw new ServiceException("连接字符串必填");
        }
        if (zkConnectionMap.get(zkConnectionEntity.getId()) != null) {
            return;
        }
        if (maxConnectionCount == zkConnectionMap.size()) {
            throw new ServiceException("连接数已满");
        }

        CuratorFramework client;
        try {
            client = CuratorFrameworkFactory.builder()
                    .connectString(zkConnectionEntity.getConnectString())
                    .retryPolicy(new ExponentialBackoffRetry(10000, 29))
                    .build();
            client.start();
        } catch (Exception e) {
            logger.error("创建集群连接时发生异常", e);
            throw new ServiceException("创建集群连接时发生异常");
        }

        zkConnectionMap.put(zkConnectionEntity.getId(), client);
    }

    @Override
    public void delete(String id) throws ServiceException {
        CuratorFramework client = zkConnectionMap.remove(id);
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                logger.error("关闭集群连接时发生异常", e);
                throw new ServiceException("关闭集群连接时发生异常");
            }
        }
    }

    @Override
    public List<ZkConnectionEntity> getAll() {
        List<ZkConnectionEntity> result = new ArrayList<>(zkConnectionMap.size());
        Set<String> keys = zkConnectionMap.keySet();
        for (String k : keys) {
            CuratorFramework client = zkConnectionMap.get(k);
            String connectString = client.getZookeeperClient().getCurrentConnectionString();
            boolean connected = client.getZookeeperClient().isConnected();
            ZkConnectionEntity entity = new ZkConnectionEntity();
            entity.setId(k);
            entity.setConnectString(connectString);
            entity.setStatus(connected? "open": "close");
            result.add(entity);
        }
        return result;
    }

    @Override
    public CuratorFramework getClient(String id) {
        return zkConnectionMap.get(id);
    }

}

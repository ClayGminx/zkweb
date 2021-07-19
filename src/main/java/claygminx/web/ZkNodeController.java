package claygminx.web;

import claygminx.consts.ResultCode;
import claygminx.entity.Result;
import claygminx.entity.ZkNodeEntity;
import claygminx.exception.ServiceException;
import claygminx.service.ZkNodeService;
import org.apache.zookeeper.data.Stat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Zk节点
 */
@RestController
@RequestMapping("/zk/node")
public class ZkNodeController {

    @Resource
    private ZkNodeService zkNodeService;

    @GetMapping("/children")
    public Result getChildren(
            @RequestParam String connectionId,
            @RequestParam String nodePath) {
        try {
            List<String> data = zkNodeService.getChildren(connectionId, nodePath);
            return new Result(ResultCode.SUCCESS, null, data);
        } catch (ServiceException e) {
            return Result.failure(e.getMessage());
        }
    }

    @GetMapping("/value")
    public Result getNodeValue(
            @RequestParam String connectionId,
            @RequestParam String nodePath) {
        try {
            String data = zkNodeService.getStringValue(connectionId, nodePath);
            return new Result(ResultCode.SUCCESS, null, data);
        } catch (ServiceException e) {
            return Result.failure(e.getMessage());
        }
    }

    @GetMapping("/stat")
    public Result getNodeStat(
            @RequestParam String connectionId,
            @RequestParam String nodePath) {
        try {
            Stat data = zkNodeService.getStat(connectionId, nodePath);
            return new Result(ResultCode.SUCCESS, null, data);
        } catch (ServiceException e) {
            return Result.failure(e.getMessage());
        }
    }

    @PostMapping("/")
    public Result createNode(@RequestBody ZkNodeEntity zkNodeEntity) {
        try {
            zkNodeService.create(zkNodeEntity.getConnectionId(), zkNodeEntity.getPath(), zkNodeEntity.getValue());
            return Result.success();
        } catch (ServiceException e) {
            return Result.failure(e.getMessage());
        }
    }

    @DeleteMapping("/")
    public Result deleteNode(@RequestBody ZkNodeEntity zkNodeEntity) {
        try {
            zkNodeService.delete(zkNodeEntity.getConnectionId(), zkNodeEntity.getPath());
            return Result.success();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    @PutMapping("/")
    public Result updateNode(@RequestBody ZkNodeEntity zkNodeEntity) {
        try {
            zkNodeService.update(zkNodeEntity.getConnectionId(), zkNodeEntity.getPath(), zkNodeEntity.getValue());
            return Result.success();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
}

package claygminx.web;

import claygminx.consts.ResultCode;
import claygminx.entity.Result;
import claygminx.entity.ZkConnectionEntity;
import claygminx.exception.ServiceException;
import claygminx.service.ZkConnectionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Zk连接
 */
@RestController
@RequestMapping("/zk/conn")
public class ZkConnectionController {

    @Resource
    private ZkConnectionService zkConnectionService;

    /**
     * 创建并打开一个 zk 集群连接
     * @param zkConnectionEntity 集群信息
     * @return 返回值
     */
    @PostMapping("/")
    public Result createClusterConnection(@RequestBody ZkConnectionEntity zkConnectionEntity) {
        try {
            zkConnectionService.add(zkConnectionEntity);
            return Result.success();
        } catch (ServiceException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 关闭并删除一个 zk 集群连接
     * @param id 连接ID
     * @return 返回值
     */
    @DeleteMapping("/{id}")
    public Result deleteClusterConnection(@PathVariable String id) {
        try {
            zkConnectionService.delete(id);
            return Result.success();
        } catch (ServiceException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 获取所有 zk 集群信息
     * @return 返回值
     */
    @GetMapping("/all")
    public Result getAllClusters() {
        List<ZkConnectionEntity> list = zkConnectionService.getAll();
        return new Result(ResultCode.SUCCESS, null, list);
    }

}

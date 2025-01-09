package com.graduation.user;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.graduation.common.ObjectTransfer;
import com.graduation.common.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public Result<List<UserModel>> query(UserQuery query) {
        return new Result<>(userMapper.selectJoinList(UserDO.class, getQueryWrapper(query)).stream()
                .map(e -> ObjectTransfer.transfer(e, UserModel.class))
                .collect(Collectors.toList()));
    }

    public Result<IPage<UserModel>> page(UserQuery query) {
        IPage<UserDO> page = new Page<>(query.getCurrent(), 10);
        userMapper.selectJoinPage(page, UserDO.class, getQueryWrapper(query));
        IPage<UserModel> result = new Page<>();
        result.setPages((int) page.getPages());
        result.setCurrent((int) Math.min(page.getCurrent(), page.getPages()));
        result.setTotal((int) page.getTotal());
        result.setRecords(page.getRecords().stream().map(e -> {
            UserModel model = ObjectTransfer.transfer(e, UserModel.class);
            return model;
        }).collect(Collectors.toList()));
        return new Result<>(result);
    }

    public Result<?> add(UserModel model) {
        UserDO userDO = ObjectTransfer.transfer(model, UserDO.class);
        userDO.setCreateTime(LocalDateTime.now());
        userMapper.insert(userDO);
        return Result.success();
    }

    public Result<?> update(UserModel model) {
        UserDO userDO = ObjectTransfer.transfer(model, UserDO.class);
        userMapper.updateById(userDO);
        return Result.success();
    }

    public Result<?> delete(Integer id) {
        userMapper.deleteById(id);
        return Result.success();
    }

    private MPJLambdaWrapper<UserDO> getQueryWrapper(UserQuery query) {
        return new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .eq(Objects.nonNull(query.getId()), UserDO::getId, query.getId())
                .like(StrUtil.isNotBlank(query.getName()), UserDO::getName, query.getName())
                .like(StrUtil.isNotBlank(query.getSex()), UserDO::getSex, query.getSex())
                .like(StrUtil.isNotBlank(query.getPhone()), UserDO::getPhone, query.getPhone())
                .like(StrUtil.isNotBlank(query.getRole()), UserDO::getRole, query.getRole())
                .like(StrUtil.isNotBlank(query.getStatus()), UserDO::getStatus, query.getStatus())
                .like(StrUtil.isNotBlank(query.getFreezeReason()), UserDO::getFreezeReason, query.getFreezeReason())
                ;
    }
}

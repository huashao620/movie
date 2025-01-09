package com.graduation.common;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.graduation.user.UserDO;
import com.graduation.user.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
public class LoginController {

    @Resource
    private UserMapper userMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Loginer loginer) {
        UserDO user = userMapper.selectOne(new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getAccount, loginer.getAccount())
                .eq(UserDO::getPassword, loginer.getPassword()));
        if (Objects.isNull(user)) {
            return Result.failed("认证失败，请检查账户和密码");
        }
        if (user.getStatus().equals("禁用")) {
            return Result.failed("您的账号已被禁用，禁用原因：" + user.getFreezeReason());
        }
        loginer.setName(user.getName());
        loginer.setId(user.getId());
        String token = RandomUtil.randomString(32);
        LoginUtil.LOGIN_MAP.put(token, loginer);
        return new Result<>(Pair.of(token, user.getRole()));
    }

    @DeleteMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        LoginUtil.LOGIN_MAP.remove(token);
        return Result.success();
    }

    @GetMapping("/loginer")
    public Result<UserDO> getLoginer(HttpServletRequest request) {
        Loginer loginer = LoginUtil.loginer(request);
        if (Objects.isNull(loginer)) {
            return new Result<>(null);
        }
        return new Result<>(userMapper.selectById(loginer.getId()));
    }
}

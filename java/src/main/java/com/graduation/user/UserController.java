package com.graduation.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.graduation.common.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;
    @PostMapping("/query")
    public Result<List<UserModel>> query(@RequestBody UserQuery query) {
        return userService.query(query);
    }

    @PostMapping("/page")
    public Result<IPage<UserModel>> page(@RequestBody UserQuery query) {
        return userService.page(query);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody UserModel model) {
        try {
            model.setStatus("正常");
            return userService.add(model);
        } catch (DuplicateKeyException e) {
            return Result.failed("登录账户已存在，请修改");
        }
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody UserModel model) {
        return userService.update(model);
    }
}

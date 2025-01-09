package com.graduation.remark;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.graduation.common.LoginUtil;
import com.graduation.common.Result;
import com.graduation.user.UserModel;
import com.graduation.user.UserQuery;
import com.graduation.user.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movie-remark")
public class MovieRemarkController {

    @Resource
    private MovieRemarkService movieRemarkService;

    @Resource
    private MovieRemarkMapper movieRemarkMapper;

    @Resource
    private UserService userService;

    @ResponseBody
    @GetMapping("/tree")
    public Result<List<MovieRemarkModel>> getTree(Integer movieId) {
        Map<Integer, UserModel> userMap = userService.query(new UserQuery()).getData().stream()
                .collect(Collectors.toMap(UserModel::getId, Function.identity()));
        // 查询所有地区,构建树状结构
        List<MovieRemarkModel> allList = movieRemarkMapper.selectList(new LambdaQueryWrapper<MovieRemarkDO>()
                        .eq(MovieRemarkDO::getMovieId, movieId))
                .stream()
                .map(e -> {
                    MovieRemarkModel model = BeanUtil.copyProperties(e, MovieRemarkModel.class);
                    model.setCreateTime(DateUtil.formatLocalDateTime(e.getCreateTime()));
                    return model;
                })
                .collect(Collectors.toList());
        for (MovieRemarkModel child: allList) {
            child.setUser(userMap.get(child.getUserId()));
            child.setToUser(userMap.getOrDefault(child.getToUserId(), new UserModel()));
            for (MovieRemarkModel parent: allList) {
                if (Objects.equals(child.getParentId(), parent.getId())) {
                    parent.getChildren().add(child);
                }
            }
        }
        return new Result<>(allList.stream().filter(e -> Objects.equals(e.getParentId(), 0)).collect(Collectors.toList()));
    }
    @PostMapping("/query")
    public Result<List<MovieRemarkModel>> query(@RequestBody MovieRemarkQuery query) {
        return movieRemarkService.query(query);
    }

    @PostMapping("/page")
    public Result<IPage<MovieRemarkModel>> page(@RequestBody MovieRemarkQuery query) {
        return movieRemarkService.page(query);
    }

    @PostMapping("/add")
    public Result<?> add(HttpServletRequest request, @RequestBody MovieRemarkModel model) {
        model.setUserId(LoginUtil.loginer(request).getId());
        return movieRemarkService.add(model);
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody MovieRemarkModel model) {
        return movieRemarkService.update(model);
    }

    @DeleteMapping("/delete")
    public Result<?> delete(Integer id) {
        return movieRemarkService.delete(id);
    }
}

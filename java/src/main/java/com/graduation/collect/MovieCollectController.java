package com.graduation.collect;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.graduation.common.LoginUtil;
import com.graduation.common.Loginer;
import com.graduation.common.ObjectTransfer;
import com.graduation.common.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
@RequestMapping("/movie-collect")
public class MovieCollectController {

    @Resource
    private MovieCollectMapper movieCollectMapper;

    @ResponseBody
    @PostMapping("/add")
    public Result<?> add(HttpServletRequest request, @RequestBody MovieCollectModel model) {
        Loginer loginer = LoginUtil.loginer(request);
        if (Objects.isNull(loginer)) {
            return Result.failed("请先登录");
        }
        MovieCollectDO userCollectDO = ObjectTransfer.transfer(model, MovieCollectDO.class);
        userCollectDO.setUserId(loginer.getId());
        try {
            movieCollectMapper.insert(userCollectDO);
        } catch (DuplicateKeyException ignore) {}
        return Result.success();
    }

    @ResponseBody
    @DeleteMapping("/delete")
    public Result<?> delete(HttpServletRequest request, Integer movieId) {
        movieCollectMapper.delete(new LambdaQueryWrapper<MovieCollectDO>()
                .eq(MovieCollectDO::getUserId, LoginUtil.loginer(request).getId())
                .eq(MovieCollectDO::getMovieId, movieId));
        return Result.success();
    }
}

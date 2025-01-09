package com.graduation.remark;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.graduation.common.ObjectTransfer;
import com.graduation.common.Result;
import com.graduation.movie.MovieMapper;
import com.graduation.movie.MovieModel;
import com.graduation.user.UserMapper;
import com.graduation.user.UserModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MovieRemarkService {

    @Resource
    private MovieRemarkMapper movieRemarkMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MovieMapper movieMapper;

    public Result<List<MovieRemarkModel>> query(MovieRemarkQuery query) {
        return new Result<>(movieRemarkMapper.selectJoinList(MovieRemarkDO.class, getQueryWrapper(query)).stream()
                .map(e -> ObjectTransfer.transfer(e, MovieRemarkModel.class))
                .collect(Collectors.toList()));
    }

    public Result<IPage<MovieRemarkModel>> page(MovieRemarkQuery query) {
        IPage<MovieRemarkDO> page = new Page<>(query.getCurrent(), 10);
        movieRemarkMapper.selectJoinPage(page, MovieRemarkDO.class, getQueryWrapper(query));
        IPage<MovieRemarkModel> result = new Page<>();
        result.setPages((int) page.getPages());
        result.setCurrent((int) Math.min(page.getCurrent(), page.getPages()));
        result.setTotal((int) page.getTotal());
        Map<Integer, UserModel> userMap = userMapper.selectList(new LambdaQueryWrapper<>()).stream()
                .map(e -> ObjectTransfer.transfer(e, UserModel.class))
                .collect(Collectors.toMap(UserModel::getId, Function.identity()));
        Map<Integer, MovieModel> movieMap = movieMapper.selectList(new LambdaQueryWrapper<>()).stream()
                .map(e -> ObjectTransfer.transfer(e, MovieModel.class))
                .collect(Collectors.toMap(MovieModel::getId, Function.identity()));
        result.setRecords(page.getRecords().stream().map(e -> {
            MovieRemarkModel model = ObjectTransfer.transfer(e, MovieRemarkModel.class);
            model.setUser(userMap.get(e.getUserId()));
            model.setMovie(movieMap.get(e.getMovieId()));
            return model;
        }).collect(Collectors.toList()));
        return new Result<>(result);
    }

    public Result<?> add(MovieRemarkModel model) {
        MovieRemarkDO movieRemarkDO = ObjectTransfer.transfer(model, MovieRemarkDO.class);
        movieRemarkDO.setCreateTime(LocalDateTime.now());
        movieRemarkMapper.insert(movieRemarkDO);
        return Result.success();
    }

    public Result<?> update(MovieRemarkModel model) {
        MovieRemarkDO movieRemarkDO = ObjectTransfer.transfer(model, MovieRemarkDO.class);
        movieRemarkMapper.updateById(movieRemarkDO);
        return Result.success();
    }

    public Result<?> delete(Integer id) {
        movieRemarkMapper.deleteById(id);
        return Result.success();
    }

    private MPJLambdaWrapper<MovieRemarkDO> getQueryWrapper(MovieRemarkQuery query) {
        return new MPJLambdaWrapper<MovieRemarkDO>()
                .selectAll(MovieRemarkDO.class)
                .eq(Objects.nonNull(query.getId()), MovieRemarkDO::getId, query.getId())
                .like(StrUtil.isNotBlank(query.getContent()), MovieRemarkDO::getContent, query.getContent())
                .eq(Objects.nonNull(query.getUserId()), MovieRemarkDO::getUserId, query.getUserId())
                .eq(Objects.nonNull(query.getMovieId()), MovieRemarkDO::getMovieId, query.getMovieId())
                ;
    }
}

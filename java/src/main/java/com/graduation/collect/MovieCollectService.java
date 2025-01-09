package com.graduation.collect;

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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MovieCollectService {

    @Resource
    private MovieCollectMapper movieCollectMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MovieMapper movieMapper;

    public Result<List<MovieCollectModel>> query(MovieCollectQuery query) {
        return new Result<>(movieCollectMapper.selectJoinList(MovieCollectDO.class, getQueryWrapper(query)).stream()
                .map(e -> ObjectTransfer.transfer(e, MovieCollectModel.class))
                .collect(Collectors.toList()));
    }

    public Result<IPage<MovieCollectModel>> page(MovieCollectQuery query) {
        IPage<MovieCollectDO> page = new Page<>(query.getCurrent(), 10);
        movieCollectMapper.selectJoinPage(page, MovieCollectDO.class, getQueryWrapper(query));
        IPage<MovieCollectModel> result = new Page<>();
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
            MovieCollectModel model = ObjectTransfer.transfer(e, MovieCollectModel.class);
            model.setUser(userMap.get(e.getUserId()));
            model.setMovie(movieMap.get(e.getMovieId()));
            return model;
        }).collect(Collectors.toList()));
        return new Result<>(result);
    }

    public Result<?> add(MovieCollectModel model) {
        MovieCollectDO movieCollectDO = ObjectTransfer.transfer(model, MovieCollectDO.class);
        movieCollectMapper.insert(movieCollectDO);
        return Result.success();
    }

    public Result<?> update(MovieCollectModel model) {
        MovieCollectDO movieCollectDO = ObjectTransfer.transfer(model, MovieCollectDO.class);
        movieCollectMapper.updateById(movieCollectDO);
        return Result.success();
    }

    public Result<?> delete(Integer id) {
        movieCollectMapper.deleteById(id);
        return Result.success();
    }

    private MPJLambdaWrapper<MovieCollectDO> getQueryWrapper(MovieCollectQuery query) {
        return new MPJLambdaWrapper<MovieCollectDO>()
                .selectAll(MovieCollectDO.class)
                .eq(Objects.nonNull(query.getId()), MovieCollectDO::getId, query.getId())
                .eq(Objects.nonNull(query.getUserId()), MovieCollectDO::getUserId, query.getUserId())
                .eq(Objects.nonNull(query.getMovieId()), MovieCollectDO::getMovieId, query.getMovieId())
                ;
    }
}

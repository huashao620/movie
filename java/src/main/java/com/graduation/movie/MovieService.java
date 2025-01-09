package com.graduation.movie;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.graduation.collect.MovieCollectDO;
import com.graduation.collect.MovieCollectMapper;
import com.graduation.common.LoginUtil;
import com.graduation.common.Loginer;
import com.graduation.common.ObjectTransfer;
import com.graduation.common.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Resource
    private MovieMapper movieMapper;

    @Resource
    private MovieCollectMapper movieCollectMapper;

    public Result<List<MovieModel>> query(HttpServletRequest request, MovieQuery query) {
        Loginer loginer = LoginUtil.loginer(request);
        List<Integer> movieCollectList = new ArrayList<>();
        if (Objects.nonNull(loginer)) {
            movieCollectList.addAll(movieCollectMapper.selectList(new LambdaQueryWrapper<MovieCollectDO>()
                            .eq(MovieCollectDO::getUserId, loginer.getId()))
                    .stream().map(MovieCollectDO::getMovieId)
                    .collect(Collectors.toList()));
        }
        if (query.isOnlyCollect()) {
            if (CollectionUtil.isEmpty(movieCollectList)) {
                return new Result<>(new ArrayList<>());
            }
            query.setIdList(movieCollectList);
        }
        return new Result<>(movieMapper.selectJoinList(MovieDO.class, getQueryWrapper(query)).stream()
                .map(e -> {
                    MovieModel model = ObjectTransfer.transfer(e, MovieModel.class);
                    if (movieCollectList.contains(model.getId())) {
                        model.setIfCollect(true);
                    }
                    return model;
                })
                .collect(Collectors.toList()));
    }

    public Result<IPage<MovieModel>> page(MovieQuery query) {
        IPage<MovieDO> page = new Page<>(query.getCurrent(), 10);
        movieMapper.selectJoinPage(page, MovieDO.class, getQueryWrapper(query));
        IPage<MovieModel> result = new Page<>();
        result.setPages((int) page.getPages());
        result.setCurrent((int) Math.min(page.getCurrent(), page.getPages()));
        result.setTotal((int) page.getTotal());
        result.setRecords(page.getRecords().stream().map(e -> {
            MovieModel model = ObjectTransfer.transfer(e, MovieModel.class);
            return model;
        }).collect(Collectors.toList()));
        return new Result<>(result);
    }

    public Result<?> add(MovieModel model) {
        MovieDO movieDO = ObjectTransfer.transfer(model, MovieDO.class);
        movieMapper.insert(movieDO);
        return Result.success();
    }

    public Result<?> update(MovieModel model) {
        MovieDO movieDO = ObjectTransfer.transfer(model, MovieDO.class);
        movieMapper.updateById(movieDO);
        return Result.success();
    }

    public Result<?> delete(Integer id) {
        movieMapper.deleteById(id);
        return Result.success();
    }

    private MPJLambdaWrapper<MovieDO> getQueryWrapper(MovieQuery query) {
        return new MPJLambdaWrapper<MovieDO>()
                .selectAll(MovieDO.class)
                .eq(Objects.nonNull(query.getId()), MovieDO::getId, query.getId())
                .like(StrUtil.isNotBlank(query.getName()), MovieDO::getName, query.getName())
                .eq(StrUtil.isNotBlank(query.getType()), MovieDO::getType, query.getType())
                .like(StrUtil.isNotBlank(query.getIntroduction()), MovieDO::getIntroduction, query.getIntroduction())
                .like(StrUtil.isNotBlank(query.getImgUrl()), MovieDO::getImgUrl, query.getImgUrl())
                .like(StrUtil.isNotBlank(query.getDirector()), MovieDO::getDirector, query.getDirector())
                .like(StrUtil.isNotBlank(query.getActor()), MovieDO::getActor, query.getActor())
                .likeRight(Objects.nonNull(query.getYear()), MovieDO::getUpDate, query.getYear())
                .in(query.isOnlyCollect(), MovieDO::getId, query.getIdList())
                .orderByDesc(Objects.nonNull(query.getSortType()) && query.getSortType() == 1, MovieDO::getScore)
                .orderByDesc(Objects.nonNull(query.getSortType()) && query.getSortType() == 2, MovieDO::getUpDate)
                ;
    }
}

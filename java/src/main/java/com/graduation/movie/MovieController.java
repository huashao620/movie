package com.graduation.movie;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.graduation.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Resource
    private MovieService movieService;
    @Resource
    private MovieMapper movieMapper;

    @PostMapping("/query")
    public Result<List<MovieModel>> query(HttpServletRequest request, @RequestBody MovieQuery query) {
        return movieService.query(request, query);
    }

    @GetMapping("/type")
    public Result<List<String>> queryType() {
        return new Result<>(movieMapper.selectList(new QueryWrapper<MovieDO>().select("distinct(type) type"))
                .stream().map(MovieDO::getType).collect(Collectors.toList()));
    }

    @PostMapping("/page")
    public Result<IPage<MovieModel>> page(@RequestBody MovieQuery query) {
        return movieService.page(query);
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody MovieModel model) {
        return movieService.add(model);
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody MovieModel model) {
        return movieService.update(model);
    }

    @DeleteMapping("/delete")
    public Result<?> delete(Integer id) {
        return movieService.delete(id);
    }
}

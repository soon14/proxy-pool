package io.github.harvies.proxypool.controller;

import io.github.harvies.proxypool.domain.Proxy;
import io.github.harvies.proxypool.domain.ProxyResult;
import io.github.harvies.proxypool.dto.PageResult;
import io.github.harvies.proxypool.query.ProxyQuery;
import io.github.harvies.proxypool.repository.ProxyRepository;
import io.github.harvies.proxypool.service.CheckProxyService;
import io.github.harvies.proxypool.service.ProxyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author harvies
 */
@RequestMapping(value = "/proxy")
@RestController
@Api
public class ProxyController {
    @Autowired
    private ProxyService proxyService;
    @Autowired
    private CheckProxyService checkProxyService;
    @Autowired
    private ProxyRepository proxyRepository;

    @ApiOperation(value = "代理可用数量")
    @GetMapping(value = "/count")
    public long count(ProxyQuery proxyQuery) {
        return proxyService.count(proxyQuery);
    }

    @ApiOperation(value = "可用代理列表")
    @GetMapping(value = "/list")
    public PageResult<Proxy> list(ProxyQuery proxyQuery) {
        List<Proxy> proxyList = proxyService.list(proxyQuery);
        long availableNum = proxyService.count(proxyQuery);
        return PageResult.of(proxyList, availableNum);
    }

    @ApiOperation(value = "随机获取一个可用代理")
    @GetMapping(value = "/get")
    public Proxy get(ProxyQuery proxyQuery) {
        return proxyService.get(proxyQuery);
    }

    @ApiOperation(value = "稳定性减1")
    @GetMapping(value = "/decrementStability")
    public Long decrementStability(Proxy proxy) {
        return proxyService.decrementStability(proxy);
    }

    @ApiOperation(value = "稳定性加1")
    @GetMapping(value = "/incrementStability")
    public Long incrementStability(Proxy proxy) {
        return proxyService.incrementStability(proxy);
    }

    @ApiOperation(value = "保存一个代理")
    @PostMapping(value = "/save")
    public Proxy save(Proxy proxy) {
        return proxyService.save(proxy);
    }

    @ApiOperation(value = "删除一个代理")
    @GetMapping(value = "/delete/{id}")
    public Boolean delete(@PathVariable(value = "id") String id) {
        return proxyService.deleteById(id);
    }

    @ApiOperation(value = "按ID查找代理")
    @GetMapping(value = "/findById/{id}")
    public Proxy findById(@PathVariable(value = "id") String id) throws Exception {
        Optional<Proxy> proxyOptional = proxyRepository.findById(id);
        if (!proxyOptional.isPresent()) {
            throw new Exception("代理不存在!");
        }
        return proxyOptional.get();
    }

    @ApiOperation(value = "检测一个代理")
    @GetMapping(value = "/check/{id}")
    public ProxyResult check(@PathVariable(value = "id") String id) throws Exception {
        Optional<Proxy> proxyOptional = proxyRepository.findById(id);
        if (!proxyOptional.isPresent()) {
            throw new Exception("代理不存在!");
        }
        return checkProxyService.check(proxyOptional.get());
    }
}

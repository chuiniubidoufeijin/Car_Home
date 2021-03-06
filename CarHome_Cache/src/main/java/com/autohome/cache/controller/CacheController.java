package com.autohome.cache.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.autohome.cache.exception.CacheException;
import com.autohome.cache.service.CacheService;
import com.autohome.common.dto.ZsetDto;
import com.autohome.common.vo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @program: FmOpen
 * @description:
 * @author: Feri(邢朋辉)
 * @create: 2020-08-06 10:35
 */
@RestController
@RequestMapping("cache/api")
@Slf4j
public class CacheController {
    @Autowired
    private CacheService service;

    //实现常用的操作
    @SentinelResource(fallback = "saveError")
    @PostMapping("/savestr.do")
    public R saveStr2Redis(String key, long times, String value) throws CacheException {
        return R.ok(service.saveStr2Redis(key, times, value));
    }

    //降级方法
    public R saveError(String key, long times, String value) throws CacheException {
        log.error("缓存服务崩溃");
        return R.fail("服务器崩溃降级");
    }

    @PostMapping("/savelist.do")
    public R saveList2Redis(String key, long times, String... values) throws CacheException {
        return R.ok(service.saveStr2Redis(key, times, values));
    }

    @PostMapping("/saveset.do")
    public R saveSet2Redis(String key, long times, String... values) throws CacheException {
        return R.ok(service.saveSet2Redis(key, times, values));
    }

    @PostMapping("/savezset.do")
    public R saveScoreSet2Redis( @RequestBody ZsetDto zsetDto) throws CacheException {
        return R.ok(service.saveScoreSet2Redis(zsetDto.getKey(), zsetDto.getTimes(),zsetDto.getScore() , zsetDto.getValue()));
    }

    @PostMapping("/savehash.do")
    public R saveHash2Redis(String key, long times, String field, String value) throws CacheException {
        return R.ok(service.saveHash2Redis(key, times, field, value));
    }

    @GetMapping("/getstr.do")
    public R getStrFromRedis(String key) {
        return R.ok(service.getStrFromRedis(key));
    }

    @GetMapping("/getlistsize.do")
    public R getListSize(String key) {
        return R.ok(service.getListSize(key));
    }

    @GetMapping("/getlist.do")
    public R getListFromRedis(String key) {
        return R.ok(service.getListFromRedis(key).toString());
    }

    @GetMapping("/checkset.do")
    public R checkSet(String key, String val) {
        return R.ok(service.checkSet(key, val));
    }

    @GetMapping("/getset.do")
    public R getSetFromRedis(String key) {
        return R.ok(service.getSetFromRedis(key).toString());
    }

    /**
     * zset 自动排序
     *
     * @param key  指定zset的 key 名
     * @param flag 0 升序，非0 降序
     * @return 注意封装进去的数据必须为 string 否则 ribbon 请求服务时只能拿到集合的第1个
     */
    @GetMapping("/getzset.do")
    public R getScoreSetFromRedis(String key, int flag) {
        return R.ok(service.getScoreSetFromRedis(key, flag).toString());
    }

    @GetMapping("/gethash2.do")
    public R getHashFromRedis(String key, String field) {
        return R.ok(service.getHashFromRedis(key, field));
    }

    @GetMapping("/checkhash.do")
    public R checkHash(String key, String field) {
        return R.ok(service.checkHash(key, field));
    }

    @GetMapping("/gethash1.do")
    public R gethashFromRedis(String key) {
        return R.ok(service.gethashFromRedis(key).toString());
    }

    @GetMapping("/gethashstr.do")
    public R gethashStrFromRedis(String key) {
        return R.ok(service.gethashStrFromRedis(key).toString());
    }


    @GetMapping("/checkkey.do")
    public R checkKey(String key) {
        return R.ok(service.checkKey(key));
    }

    @GetMapping("/keys.do")
    public R keys(String prefile) {
        return R.ok(service.keys(prefile));
    }

    @PostMapping("/expire.do")
    public R expire(String key, long time, TimeUnit timeUnit) {
        return R.ok(service.expire(key, time, timeUnit));
    }

    @PostMapping("/delkeys")
    public R delKeys(String... keys) {
        return R.ok(service.delKey(keys));
    }

    @PostMapping("/lock.do")
    public R lock(String key) {
        return R.ok(service.lock(key));
    }

    @PostMapping("/locktime.do")
    public R lock(String key, long timeseconds) {
        return R.ok(service.lock(key, timeseconds));
    }

    @PostMapping("/unlock.do")
    public R unlock(String key) {
        return R.ok(service.unlock(key));
    }

}

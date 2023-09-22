package com.huancoder.market.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CacheUtils {
    @Autowired
    private CacheManager cacheManager;

    public Boolean isExistCache(String name) {
        Cache couponsCache = cacheManager.getCache(name);
        if (couponsCache != null) return true;
        return false;
    }
    public void clearMyCache(String name) {
        Cache myCache = cacheManager.getCache(name);
        if (myCache != null) {
            myCache.clear();
        }
    }
}

package com.example.binding.echo.core;


import com.example.binding.echo.manager.CacheLoadKeys;
import com.example.binding.echo.properties.EchoProperties;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * description: 默认的缓存加载器
 *
 * @author zhouxinlei
 * @since 2022-02-20 19:45:35
 */
@Slf4j
public class DefaultCacheLoader extends CacheLoader<CacheLoadKeys, Map<Serializable, Object>> {

    /**
     * 侦听执行器服务
     */
    private final ListeningExecutorService backgroundRefreshPools;

    public DefaultCacheLoader(EchoProperties.GuavaCache guavaCache) {
        this.backgroundRefreshPools = MoreExecutors.listeningDecorator(
                new ThreadPoolExecutor(guavaCache.getRefreshThreadPoolSize(), guavaCache.getRefreshThreadPoolSize(),
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<>()
                        )
        );
    }

    /**
     * 内存缓存不存在时， 调用时触发加载数据
     *
     * @param type 扩展参数
     * @return 加载后的数据
     */
    @Override
    public Map<Serializable, Object> load(@NonNull CacheLoadKeys type) {
        log.info("首次读取缓存: {}", type);
        return type.loadMap();
    }

    /**
     * 重新载入数据
     *
     * @param key      扩展参数
     * @param oldValue 原来的值
     * @return 重新加载后的数据
     */
    @Override
    public ListenableFuture<Map<Serializable, Object>> reload(@NonNull CacheLoadKeys key, @NonNull Map<Serializable, Object> oldValue) {
        return backgroundRefreshPools.submit(() -> {
//            RequestLocalContextHolder.setTenantId(key.getTenantId());
            return load(key);
        });
    }
}

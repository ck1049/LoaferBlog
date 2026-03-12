package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.model.entity.Configuration;

import java.util.Optional;

public interface ConfigurationService extends IService<Configuration> {
    Optional<Configuration> getByModuleAndKey(String module, String key);
    String getValueByModuleAndKey(String module, String key, String defaultValue);
    void updateByModuleAndKey(String module, String key, String value, String description);
}

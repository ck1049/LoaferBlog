package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.mapper.ConfigurationMapper;
import com.loafer.blog.model.entity.Configuration;
import com.loafer.blog.service.ConfigurationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfigurationServiceImpl extends ServiceImpl<ConfigurationMapper, Configuration> implements ConfigurationService {

    @Override
    public Optional<Configuration> getByModuleAndKey(String module, String key) {
        QueryWrapper<Configuration> wrapper = new QueryWrapper<>();
        wrapper.eq("module", module)
               .eq("key", key)
               .eq("deleted", 0);
        return Optional.ofNullable(baseMapper.selectOne(wrapper));
    }

    @Override
    public String getValueByModuleAndKey(String module, String key, String defaultValue) {
        return getByModuleAndKey(module, key)
                .map(Configuration::getValue)
                .orElse(defaultValue);
    }

    @Override
    public void updateByModuleAndKey(String module, String key, String value, String description) {
        Configuration configuration = getByModuleAndKey(module, key).orElse(new Configuration());
        configuration.setModule(module);
        configuration.setKey(key);
        configuration.setValue(value);
        configuration.setDescription(description);
        if (configuration.getId() == null) {
            save(configuration);
        } else {
            updateById(configuration);
        }
    }
}

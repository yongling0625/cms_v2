package online.lianxue.cms.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuyl
 */
@Configuration
public class MessageConverterConfig {

    @Bean
    public HttpMessageConverters fastJsonMessageConverters() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        //需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
        //添加fastJson的配置信息;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                // 防止循环引用
                SerializerFeature.DisableCircularReferenceDetect,
                // 空集合返回[],不返回null
                SerializerFeature.WriteNullListAsEmpty,
                // 空字符串返回"",不返回null
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteMapNullValue
        );
        //全局时间配置
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        //在convert中添加配置信息.
        fastJsonConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(0, fastJsonConverter);
        return new HttpMessageConverters(converters);
    }
}

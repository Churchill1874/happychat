package com.ent.happychat.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration //必须存在
@EnableSwagger2 //必须存在
@EnableSwaggerBootstrapUI
public class SwaggerConfig {

    @Bean
    public Docket frontDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                //.globalOperationParameters(setRequestHeaders())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ent.happychat.controller.player"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(getPars())
                .groupName("前台-客户端接口");
    }

    @Bean
    public Docket backDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                //.globalOperationParameters(setRequestHeaders())
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ent.happychat.controller.manage"))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(getPars())
                .groupName("后台-管理接口");
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("happychat", "http://127.0.0.1:8009/doc.html", "joy1cs1112@gmail.com");
        return new ApiInfoBuilder()
                .title("快聊测试API接口")//标题
                .description("API接口的描述")//文档接口的描述
                .contact(contact)
                .termsOfServiceUrl("www.baidu.com")
                .version("1.0")//版本号
                .build();
    }

    private List<Parameter> getPars(){
        ParameterBuilder tokenHeaderParamter = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        //header中的token参数非必填写,传空也可以
        tokenHeaderParamter
                .name("TOKEN-ID")
                .description("token")
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(false)
                .build();

        pars.add(tokenHeaderParamter.build());
        return pars;
    }

}

package br.com.ifce.easyflow.config;


import io.swagger.models.auth.In;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;

import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.ifce.easyflow.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, responseMessage())
                .globalResponseMessage(RequestMethod.PUT, responseMessage())
                .globalResponseMessage(RequestMethod.POST, responseMessage())
                .globalResponseMessage(RequestMethod.DELETE, responseMessage())
                .globalResponseMessage(RequestMethod.PATCH, responseMessage())
                .securitySchemes(List.of(new ApiKey("Token Access", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
                .securityContexts(Collections.singletonList(securityContext()));

    }

    private SecurityContext securityContext() {

        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope =
                new AuthorizationScope("ADMIN", "accessEverything");
        AuthorizationScope[] scopes = new AuthorizationScope[1];
        scopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Token Access", scopes));
    }

    private List<ResponseMessage> responseMessage() {
        return new ArrayList<>() {{
            add(new ResponseMessageBuilder()
                    .code(500)
                    .message("Internal Server Exception")
                    .build());
            add(new ResponseMessageBuilder()
                    .code(403)
                    .message("Permission denied to access this resource!")
                    .build());
            add(new ResponseMessageBuilder()
                    .code(200)
                    .message("Successful Request")
                    .build());
        }};
    }

    private ApiInfo getInfo() {
        return new ApiInfoBuilder()
                .title("EasyFlowApp - API")
                .description("EasyFlowApp api endpoints documentation with swagger.")
                .version("1.0.0")
                .build();
    }

}

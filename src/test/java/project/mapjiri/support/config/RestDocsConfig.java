package project.mapjiri.support.config;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@TestConfiguration
public class RestDocsConfig {

    private static final OperationRequestPreprocessor HOST_INFO = preprocessRequest(
            modifyUris()
                    .scheme("http")
                    .host("localhost")
                    .port(8080)
                    .removePort(),
            prettyPrint()
    );

    @Bean
    public RestDocumentationResultHandler restDocsMockMvcConfigurationCustomizer() {
        return MockMvcRestDocumentationWrapper.document(
                "{method-name}",
                HOST_INFO,
                preprocessResponse(prettyPrint())
        );
    }
}

package de.bpmn.springrestdocs.web;

import capital.scalable.restdocs.response.ResponseModifyingPreprocessors;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.SnippetRegistry;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
@WebMvcTest(SomeController.class)
public class SomeControllerIT  {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    protected MockMvc docMockMvc;

    @Autowired
    private WebApplicationContext context;


    @BeforeEach
    public void setupMVC(RestDocumentationContextProvider restDocumentation) {

        var resolvers = context.getBeansOfType(HandlerMethodArgumentResolver.class);



        List<String> snippetNames = Arrays.asList(
            SnippetRegistry.AUTO_AUTHORIZATION,
            SnippetRegistry.AUTO_PATH_PARAMETERS,
            SnippetRegistry.AUTO_REQUEST_PARAMETERS,
//            SnippetRegistry.AUTO_REQUEST_HEADERS,
            SnippetRegistry.AUTO_REQUEST_FIELDS,
            SnippetRegistry.AUTO_MODELATTRIBUTE,
            SnippetRegistry.AUTO_RESPONSE_FIELDS,
            SnippetRegistry.CURL_REQUEST,
            SnippetRegistry.HTTP_RESPONSE
        );

        docMockMvc = webAppContextSetup(context).
            alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper)).
            alwaysDo(commonDocument())
            .apply(documentationConfiguration(restDocumentation)
                .uris()
                .withScheme("http")
                .withHost("localhost")
                .withPort(8080)
                .and()
                .snippets()
                .withDefaults(
                    CliDocumentation.curlRequest(),
                    HttpDocumentation.httpRequest(),
                    HttpDocumentation.httpResponse(),
                    AutoDocumentation.requestFields(),
                    AutoDocumentation.responseFields(),
                    AutoDocumentation.pathParameters(),
                    AutoDocumentation.requestParameters(),
                    AutoDocumentation.modelAttribute(resolvers.values()),
                    AutoDocumentation.description(),
                    AutoDocumentation.methodAndPath(),
                    AutoDocumentation.requestHeaders(),
                    AutoDocumentation.sectionBuilder()
                        .snippetNames(snippetNames)
                        .skipEmpty(false)
                        .build()
                )
            ).build();
    }

    RestDocumentationResultHandler commonDocument() {
        return document("{class-name}/{method-name}",
            Preprocessors.preprocessResponse(
                ResponseModifyingPreprocessors.replaceBinaryContent(),
                ResponseModifyingPreprocessors.limitJsonArrayLength(objectMapper),
                Preprocessors.prettyPrint())
        );
    }

    String resolveOutputDir() {
        String outputDir = System.getProperties().getProperty(
            "org.springframework.restdocs.outputDir");
        if (outputDir == null) {
            outputDir = "build/generated-snippets";
        }
        return outputDir;
    }

    @Test
    public void shouldDo() throws Exception {
        //given
        var someHeaderValue = "someHeaderValue";
        var someRequestText = "someRequestText";

        //when
        var resultActions = docMockMvc.perform(get(
            "/some",
            someRequestText).header("someHeader", someHeaderValue));

        //then
        resultActions.
            andDo(print()).
            andExpect(status().isOk());
    }

}
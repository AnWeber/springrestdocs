package de.bpmn.springrestdocs.web;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.SnippetRegistry;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * BaseControllerTest
 */
public class BaseControllerTest {

  @Autowired
  protected ObjectMapper objectMapper;
  @Autowired
  protected WebApplicationContext context;

  protected MockMvc mockMvc;

  @BeforeEach
  void setUp(RestDocumentationContextProvider restDocumentation) {

    var resolvers = this.context.getBeansOfType(HandlerMethodArgumentResolver.class);

    this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper))
        .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation).uris().withScheme("https")
            .withHost("127.0.0.1").withPort(443).and().snippets()
            .withDefaults(
              CliDocumentation.curlRequest(),
              HttpDocumentation.httpRequest(),
              HttpDocumentation.httpResponse(),
              AutoDocumentation.pathParameters(),
              AutoDocumentation.requestParameters(),
              AutoDocumentation.modelAttribute(resolvers.values()),
              AutoDocumentation.requestHeaders(),
              AutoDocumentation.responseFields(),
              AutoDocumentation.description(),
              AutoDocumentation.methodAndPath(),

              AutoDocumentation
                .sectionBuilder()
                .skipEmpty(true)
                .snippetNames(
                        SnippetRegistry.AUTO_AUTHORIZATION,
                        SnippetRegistry.AUTO_PATH_PARAMETERS,
                        SnippetRegistry.AUTO_REQUEST_HEADERS,
                        SnippetRegistry.AUTO_REQUEST_PARAMETERS,
                        SnippetRegistry.AUTO_MODELATTRIBUTE,
                        SnippetRegistry.AUTO_REQUEST_FIELDS,
                        SnippetRegistry.AUTO_RESPONSE_FIELDS,
                        SnippetRegistry.AUTO_LINKS,
                        SnippetRegistry.AUTO_EMBEDDED,
                        SnippetRegistry.CURL_REQUEST,
                        SnippetRegistry.HTTP_RESPONSE)
                    .build()))
        .alwaysDo(
            document("{class-name}/{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())))
        .build();
  }
}
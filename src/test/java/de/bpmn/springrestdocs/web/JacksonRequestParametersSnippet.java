
package de.bpmn.springrestdocs.web;

import static capital.scalable.restdocs.SnippetRegistry.AUTO_REQUEST_FIELDS;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import capital.scalable.restdocs.i18n.SnippetTranslationResolver;
import capital.scalable.restdocs.jackson.FieldDescriptors;
import capital.scalable.restdocs.payload.AbstractJacksonFieldSnippet;
import capital.scalable.restdocs.request.RequestParametersSnippet;

public class JacksonRequestParametersSnippet extends AbstractJacksonFieldSnippet {

    private final boolean failOnUndocumentedFields;

    private Collection<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers;

    public JacksonRequestParametersSnippet() {
        this(null, false);
    }

    public JacksonRequestParametersSnippet(Collection<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers, boolean failOnUndocumentedFields) {
        super(AUTO_REQUEST_FIELDS, null);
        this.handlerMethodArgumentResolvers = handlerMethodArgumentResolvers;
        this.failOnUndocumentedFields = failOnUndocumentedFields;
    }
    public JacksonRequestParametersSnippet failOnUndocumentedFields(boolean failOnUndocumentedFields) {
        return new JacksonRequestParametersSnippet(null ,failOnUndocumentedFields);
    }

    @Override
    protected Type getType(HandlerMethod method) {
        for (MethodParameter param : method.getMethodParameters()) {
            if (isModelAttribute(param) || hasNoHandlerMethodArgumentResolver(param)) {
                if(isRequestFields(method))
                return getType(param);
            }
        }
        return null;
    }

    private boolean isRequestFields(HandlerMethod method) {
        RequestMapping requestMapping = method.getMethodAnnotation(RequestMapping.class);
        return requestMapping.method() == null || Arrays.stream(requestMapping.method()).allMatch(obj -> {
            return obj != RequestMethod.GET;
        });
    }

    private boolean isModelAttribute(MethodParameter param) {
        return param.getParameterAnnotation(ModelAttribute.class) != null;
    }

    private boolean hasNoHandlerMethodArgumentResolver(MethodParameter param) {
        if(this.handlerMethodArgumentResolvers != null){
            return this.handlerMethodArgumentResolvers
            .stream()
            .allMatch(obj -> !obj.supportsParameter(param));
        }
        return false;
    }


    private Type getType(final MethodParameter param) {
        if (isCollection(param.getParameterType())) {
            return (GenericArrayType) () -> firstGenericType(param);
        } else {
            return param.getParameterType();
        }
    }

    @Override
    public String getHeaderKey() {
        return "request-parameters";
    }

    @Override
    protected void enrichModel(Map<String, Object> model, HandlerMethod handlerMethod,
            FieldDescriptors fieldDescriptors, SnippetTranslationResolver translationResolver) {
        boolean isPageRequest = isPageRequest(handlerMethod);
        model.put("isPageRequest", isPageRequest);
        if (isPageRequest) {
            model.put("noContent", false);
        }
    }

    private boolean isPageRequest(HandlerMethod method) {
        for (MethodParameter param : method.getMethodParameters()) {
            if (isPageable(param)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPageable(MethodParameter param) {
        return RequestParametersSnippet.SPRING_DATA_PAGEABLE_CLASS.equals(
                param.getParameterType().getCanonicalName());
    }

    @Override
    protected boolean shouldFailOnUndocumentedFields() {
        return failOnUndocumentedFields;
    }

    @Override
    protected String[] getTranslationKeys() {

        return new String[]{
            "th-path",
            "th-type",
            "th-optional",
            "th-description",
            "no-request-body"
        };
    }
}

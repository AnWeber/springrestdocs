package de.bpmn.springrestdocs.web;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomeController {
    public class SomeClass {

        private final String someHeader;

        public SomeClass(String someHeader) {
            this.someHeader = someHeader;
        }

        public String getSomeHeader() {
            return someHeader;
        }
    }

    @Component
    public class SomeClassConverter implements Converter<String, SomeClass> {

        @Override
        public SomeClass convert(String source) {
            return new SomeClass(source);
        }
    }

    public class SomeRequest {

        /**
         * Defines some text
         */
        private String some;

        public String getSome() {
            return some;
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * Some description
     *
     * @param someClass   This is some header
     */
    @GetMapping("/some")
    void some(@RequestHeader("someHeader") SomeClass someClass,
              SomeRequest someRequest) {

    }
}
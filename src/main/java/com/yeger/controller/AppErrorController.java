package com.yeger.controller;

import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class AppErrorController implements ErrorController {

        private ErrorAttributes errorAttributes;

        private final static String ERROR_PATH = "/error";

        public AppErrorController(ErrorAttributes errorAttributes) {
            this.errorAttributes = errorAttributes;
        }

        @RequestMapping(value = ERROR_PATH, produces = "text/html")
        public ModelAndView errorHtml(HttpServletRequest request) {
            return new ModelAndView("/errors/error", getErrorAttributes(request, false));
        }

        @RequestMapping(value = ERROR_PATH)
        @ResponseBody
        public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
            Map<String, Object> body = getErrorAttributes(request, getTraceParameter(request));
            HttpStatus status = getStatus(request);
            return new ResponseEntity<>(body, status);
        }

        @Override
        public String getErrorPath() {
            return ERROR_PATH;
        }


        private boolean getTraceParameter(HttpServletRequest request) {
            String parameter = request.getParameter("trace");
            return parameter != null && !"false".equals(parameter.toLowerCase());
        }

        private Map<String, Object> getErrorAttributes(HttpServletRequest request,
                                                       boolean includeStackTrace) {
            RequestAttributes requestAttributes = new ServletRequestAttributes(request);
            return this.errorAttributes.getErrorAttributes(requestAttributes,
                    includeStackTrace);
        }

        private HttpStatus getStatus(HttpServletRequest request) {
            Integer statusCode = (Integer) request
                    .getAttribute("javax.servlet.error.status_code");
            if (statusCode != null) {
                try {
                    return HttpStatus.valueOf(statusCode);
                }
                catch (Exception ignored) {
                }
            }
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

}


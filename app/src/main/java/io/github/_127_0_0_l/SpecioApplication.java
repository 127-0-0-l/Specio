package io.github._127_0_0_l;

import io.github._127_0_0_l.services.HtmlProviderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(scanBasePackages = "io.github._127_0_0_l")
public class SpecioApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpecioApplication.class, args);

        HtmlProviderService htmlProviderService = context.getBean(HtmlProviderService.class);
        htmlProviderService.showHtml();
    }
}
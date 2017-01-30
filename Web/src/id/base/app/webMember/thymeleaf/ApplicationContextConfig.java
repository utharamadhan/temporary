package id.base.app.webMember.thymeleaf;

import id.base.app.thymeleaf.BaseDialect;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module.Feature;

@EnableWebMvc
@Configuration
@EnableTransactionManagement
@ComponentScan("id.base.app.thymeleaf")
@ImportResource({"classpath:applicationContext.xml","classpath:configurationContext.xml"})
@Import({WebSocketConfig.class})
public class ApplicationContextConfig extends WebMvcConfigurerAdapter{
	
	@Bean(name = "viewResolver")
	public ThymeleafViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setOrder(1);
		viewResolver.setRedirectHttp10Compatible(false);
		return viewResolver;
	}
	
	@Bean(name = "templateResolver")
	public ServletContextTemplateResolver templateResolver() {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setPrefix("/do/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("HTML5");
		templateResolver.setCacheable(false); //development only
		return templateResolver;
	}
	
	@Bean(name = "templateEngine")
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		templateEngine.setMessageSource(messageSource());
		templateEngine.addDialect(new BaseDialect());
		return templateEngine;
	}
	
	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**")
        .addResourceLocations("/resources/")
        .resourceChain(true).addResolver(
            new VersionResourceResolver().addContentVersionStrategy("/**"));
	}*/
	
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		source.setUseCodeAsDefaultMessage(true);
		source.setDefaultEncoding("UTF-8");
		source.setBasename("ApplicationResources");
		return source;
	}
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver commonsMultipart = new CommonsMultipartResolver();
//		commonsMultipart.setMaxUploadSize(SystemParameter.MAX_UPLOAD_FILE_SIZE);
		return commonsMultipart;
	}
	
	/*@Bean
	public FormattingConversionServiceFactoryBean getConversionService() {
		FormattingConversionServiceFactoryBean bean = new FormattingConversionServiceFactoryBean();
		Set<Formatter> set = new HashSet<Formatter>();
		set.add(new ThymeleafDateFormatter());
		bean.setFormatters(set);
		return bean;
	}*/
	
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        Hibernate4Module hbm = new Hibernate4Module();
        hbm.disable(Feature.USE_TRANSIENT_ANNOTATION);
        objectMapper.registerModule(hbm);
        converter.setObjectMapper(objectMapper);
        converters.add(converter);
        super.configureMessageConverters(converters);
    }
}

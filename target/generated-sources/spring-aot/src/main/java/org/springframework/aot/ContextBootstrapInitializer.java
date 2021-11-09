package org.springframework.aot;

import com.dummy.app.DummyAppApplication;
import com.dummy.app.mapper.DummyMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.MultipartConfigElement;
import javax.validation.Validator;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aot.beans.factory.BeanDefinitionRegistrar;
import org.springframework.aot.context.annotation.ImportAwareBeanPostProcessor;
import org.springframework.aot.context.annotation.InitDestroyBeanPostProcessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.boot.LazyInitializationExcludeFilter;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration;
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration;
import org.springframework.boot.autoconfigure.context.LifecycleProperties;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskSchedulingProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.autoconfigure.transaction.TransactionProperties;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.TomcatWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebSessionIdResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.web.servlet.TomcatServletWebServerFactoryCustomizer;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration;
import org.springframework.boot.availability.ApplicationAvailabilityBean;
import org.springframework.boot.context.properties.BoundConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.boot.validation.beanvalidation.MethodValidationExcludeFilter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.ErrorPageRegistrarBeanPostProcessor;
import org.springframework.boot.web.server.WebServerFactoryCustomizerBeanPostProcessor;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter;
import org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration;
import org.springframework.cloud.autoconfigure.LifecycleMvcEndpointAutoConfiguration;
import org.springframework.cloud.client.CommonsClientAutoConfiguration;
import org.springframework.cloud.client.ReactiveCommonsClientAutoConfiguration;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClient;
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration;
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerDefaultMappingsProviderAutoConfiguration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationConfiguration;
import org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties;
import org.springframework.cloud.client.serviceregistry.ServiceRegistryAutoConfiguration;
import org.springframework.cloud.commons.config.CommonsConfigAutoConfiguration;
import org.springframework.cloud.commons.config.DefaultsBindHandlerAdvisor;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.cloud.context.properties.ConfigurationPropertiesBeans;
import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.FunctionProperties;
import org.springframework.cloud.function.context.FunctionRegistry;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;
import org.springframework.cloud.function.core.FunctionInvocationHelper;
import org.springframework.cloud.function.json.JsonMapper;
import org.springframework.cloud.sleuth.autoconfig.SleuthAnnotationConfiguration;
import org.springframework.cloud.sleuth.autoconfig.SleuthBaggageProperties;
import org.springframework.cloud.sleuth.autoconfig.SleuthSpanFilterProperties;
import org.springframework.cloud.sleuth.autoconfig.SleuthTracerProperties;
import org.springframework.cloud.sleuth.autoconfig.TraceConfiguration;
import org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.brave.SamplerProperties;
import org.springframework.cloud.sleuth.autoconfig.brave.SleuthPropagationProperties;
import org.springframework.cloud.sleuth.autoconfig.brave.SleuthProperties;
import org.springframework.cloud.sleuth.autoconfig.brave.instrument.messaging.BraveMessagingAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.brave.instrument.rpc.BraveRpcAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.BraveHttpConfiguration;
import org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.client.BraveWebClientAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.instrument.circuitbreaker.SleuthCircuitBreakerProperties;
import org.springframework.cloud.sleuth.autoconfig.instrument.circuitbreaker.TraceCircuitBreakerAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.instrument.kafka.TracingKafkaAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.instrument.messaging.SleuthIntegrationMessagingProperties;
import org.springframework.cloud.sleuth.autoconfig.instrument.messaging.SleuthMessagingProperties;
import org.springframework.cloud.sleuth.autoconfig.instrument.messaging.TraceFunctionAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.instrument.reactor.SleuthReactorProperties;
import org.springframework.cloud.sleuth.autoconfig.instrument.reactor.TraceReactorAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.instrument.scheduling.SleuthSchedulingProperties;
import org.springframework.cloud.sleuth.autoconfig.instrument.scheduling.TraceSchedulingAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.instrument.tx.TraceTxAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.SleuthHttpProperties;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.SleuthWebProperties;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.TraceWebAutoConfiguration;
import org.springframework.cloud.sleuth.autoconfig.instrument.web.client.TraceWebAsyncClientAutoConfiguration;
import org.springframework.cloud.stream.binder.BinderFactory;
import org.springframework.cloud.stream.binder.BinderTypeRegistry;
import org.springframework.cloud.stream.binder.kafka.config.ExtendedBindingHandlerMappingsProviderConfiguration;
import org.springframework.cloud.stream.binding.AbstractBindingTargetFactory;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.cloud.stream.binding.BinderAwareRouter;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.cloud.stream.binding.BindingsLifecycleController;
import org.springframework.cloud.stream.binding.CompositeMessageChannelConfigurer;
import org.springframework.cloud.stream.binding.ContextStartAfterRefreshListener;
import org.springframework.cloud.stream.binding.DynamicDestinationsBindable;
import org.springframework.cloud.stream.binding.MessageChannelStreamListenerResultAdapter;
import org.springframework.cloud.stream.binding.MessageConverterConfigurer;
import org.springframework.cloud.stream.binding.MessageSourceBindingTargetFactory;
import org.springframework.cloud.stream.binding.StreamListenerAnnotationBeanPostProcessor;
import org.springframework.cloud.stream.config.BinderFactoryAutoConfiguration;
import org.springframework.cloud.stream.config.BindingHandlerAdvise;
import org.springframework.cloud.stream.config.BindingServiceConfiguration;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.cloud.stream.config.SpelExpressionConverterConfiguration;
import org.springframework.cloud.stream.config.SpringIntegrationProperties;
import org.springframework.cloud.stream.function.FunctionConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.cloud.stream.function.StreamFunctionProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.expression.Expression;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.integration.channel.DefaultHeaderChannelRegistry;
import org.springframework.integration.channel.MessagePublishingErrorHandler;
import org.springframework.integration.channel.NullChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.ConsumerEndpointFactoryBean;
import org.springframework.integration.config.GlobalChannelInterceptorProcessor;
import org.springframework.integration.config.IdGeneratorConfigurer;
import org.springframework.integration.config.IntegrationEvaluationContextFactoryBean;
import org.springframework.integration.config.IntegrationManagementConfiguration;
import org.springframework.integration.config.IntegrationManagementConfigurer;
import org.springframework.integration.config.IntegrationSimpleEvaluationContextFactoryBean;
import org.springframework.integration.config.annotation.MessagingAnnotationPostProcessor;
import org.springframework.integration.context.IntegrationProperties;
import org.springframework.integration.dsl.BaseIntegrationFlowDefinition;
import org.springframework.integration.dsl.context.IntegrationFlowBeanPostProcessor;
import org.springframework.integration.expression.SpelPropertyAccessorRegistrar;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.support.CollectionArgumentResolver;
import org.springframework.integration.handler.support.MapArgumentResolver;
import org.springframework.integration.handler.support.PayloadExpressionArgumentResolver;
import org.springframework.integration.handler.support.PayloadsArgumentResolver;
import org.springframework.integration.json.JsonNodeWrapperToJsonNodeConverter;
import org.springframework.integration.support.DefaultMessageBuilderFactory;
import org.springframework.integration.support.NullAwarePayloadArgumentResolver;
import org.springframework.integration.support.SmartLifecycleRoleController;
import org.springframework.integration.support.channel.BeanFactoryChannelResolver;
import org.springframework.integration.support.converter.DefaultDatatypeChannelMessageConverter;
import org.springframework.kafka.annotation.KafkaListenerAnnotationBeanPostProcessor;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.core.DestinationResolver;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.PathMatcher;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.method.support.CompositeUriComponentsContributor;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.RequestToViewNameTranslator;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.function.support.HandlerFunctionAdapter;
import org.springframework.web.servlet.function.support.RouterFunctionMapping;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.pattern.PathPatternParser;

public class ContextBootstrapInitializer implements ApplicationContextInitializer<GenericApplicationContext> {
  private ImportAwareBeanPostProcessor createImportAwareBeanPostProcessor() {
    Map<String, String> mappings = new LinkedHashMap<>();
    mappings.put("org.springframework.integration.config.IntegrationManagementConfiguration", "org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration$IntegrationManagementConfiguration$EnableIntegrationManagementConfiguration");
    return new ImportAwareBeanPostProcessor(mappings);
  }

  private InitDestroyBeanPostProcessor createInitDestroyBeanPostProcessor(
      ConfigurableBeanFactory beanFactory) {
    Map<String, List<String>> initMethods = new LinkedHashMap<>();
    initMethods.put("simpleDiscoveryProperties", List.of("init"));
    initMethods.put("org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationAutoConfiguration", List.of("init"));
    initMethods.put("sleuthAdvisorConfig", List.of("init"));
    Map<String, List<String>> destroyMethods = new LinkedHashMap<>();
    return new InitDestroyBeanPostProcessor(beanFactory, initMethods, destroyMethods);
  }

  @Override
  public void initialize(GenericApplicationContext context) {
    // infrastructure
    context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
    context.getBeanFactory().addBeanPostProcessor(createImportAwareBeanPostProcessor());
    context.getBeanFactory().addBeanPostProcessor(createInitDestroyBeanPostProcessor(context.getBeanFactory()));

    BeanDefinitionRegistrar.of("com.dummy.app.DummyAppApplication", DummyAppApplication.class)
        .instanceSupplier(DummyAppApplication::new).register(context);
    BeanDefinitionRegistrar.of("dummyMapper", DummyMapper.class)
        .instanceSupplier(DummyMapper::new).register(context);
    org.springframework.boot.autoconfigure.ContextBootstrapInitializer.registerAutoConfigurationPackages_BasePackages(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration", PropertyPlaceholderAutoConfiguration.class)
        .instanceSupplier(PropertyPlaceholderAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("propertySourcesPlaceholderConfigurer", PropertySourcesPlaceholderConfigurer.class).withFactoryMethod(PropertyPlaceholderAutoConfiguration.class, "propertySourcesPlaceholderConfigurer")
        .instanceSupplier(() -> PropertyPlaceholderAutoConfiguration.propertySourcesPlaceholderConfigurer()).register(context);
    org.springframework.boot.autoconfigure.websocket.servlet.ContextBootstrapInitializer.registerWebSocketServletAutoConfiguration_TomcatWebSocketConfiguration(context);
    org.springframework.boot.autoconfigure.websocket.servlet.ContextBootstrapInitializer.registerTomcatWebSocketConfiguration_websocketServletWebServerCustomizer(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.websocket.servlet.WebSocketServletAutoConfiguration", WebSocketServletAutoConfiguration.class)
        .instanceSupplier(WebSocketServletAutoConfiguration::new).register(context);
    org.springframework.boot.autoconfigure.web.servlet.ContextBootstrapInitializer.registerServletWebServerFactoryConfiguration_EmbeddedTomcat(context);
    org.springframework.boot.autoconfigure.web.servlet.ContextBootstrapInitializer.registerEmbeddedTomcat_tomcatServletWebServerFactory(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration", ServletWebServerFactoryAutoConfiguration.class)
        .instanceSupplier(ServletWebServerFactoryAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("servletWebServerFactoryCustomizer", ServletWebServerFactoryCustomizer.class).withFactoryMethod(ServletWebServerFactoryAutoConfiguration.class, "servletWebServerFactoryCustomizer", ServerProperties.class, ObjectProvider.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(ServletWebServerFactoryAutoConfiguration.class).servletWebServerFactoryCustomizer(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    BeanDefinitionRegistrar.of("tomcatServletWebServerFactoryCustomizer", TomcatServletWebServerFactoryCustomizer.class).withFactoryMethod(ServletWebServerFactoryAutoConfiguration.class, "tomcatServletWebServerFactoryCustomizer", ServerProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(ServletWebServerFactoryAutoConfiguration.class).tomcatServletWebServerFactoryCustomizer(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor", ConfigurationPropertiesBindingPostProcessor.class)
        .instanceSupplier(ConfigurationPropertiesBindingPostProcessor::new).customize((bd) -> bd.setRole(2)).register(context);
    org.springframework.boot.context.properties.ContextBootstrapInitializer.registerConfigurationPropertiesBinder_Factory(context);
    org.springframework.boot.context.properties.ContextBootstrapInitializer.registerConfigurationPropertiesBinder(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.context.properties.BoundConfigurationProperties", BoundConfigurationProperties.class)
        .instanceSupplier(BoundConfigurationProperties::new).customize((bd) -> bd.setRole(2)).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.context.properties.EnableConfigurationPropertiesRegistrar.methodValidationExcludeFilter", MethodValidationExcludeFilter.class)
        .instanceSupplier(() -> MethodValidationExcludeFilter.byAnnotation(ConfigurationProperties.class)).customize((bd) -> bd.setRole(2)).register(context);
    BeanDefinitionRegistrar.of("server-org.springframework.boot.autoconfigure.web.ServerProperties", ServerProperties.class)
        .instanceSupplier(ServerProperties::new).register(context);
    BeanDefinitionRegistrar.of("webServerFactoryCustomizerBeanPostProcessor", WebServerFactoryCustomizerBeanPostProcessor.class)
        .instanceSupplier(WebServerFactoryCustomizerBeanPostProcessor::new).customize((bd) -> bd.setSynthetic(true)).register(context);
    BeanDefinitionRegistrar.of("errorPageRegistrarBeanPostProcessor", ErrorPageRegistrarBeanPostProcessor.class)
        .instanceSupplier(ErrorPageRegistrarBeanPostProcessor::new).customize((bd) -> bd.setSynthetic(true)).register(context);
    org.springframework.boot.autoconfigure.web.servlet.ContextBootstrapInitializer.registerDispatcherServletAutoConfiguration_DispatcherServletConfiguration(context);
    org.springframework.boot.autoconfigure.web.servlet.ContextBootstrapInitializer.registerDispatcherServletConfiguration_dispatcherServlet(context);
    BeanDefinitionRegistrar.of("spring.mvc-org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties", WebMvcProperties.class)
        .instanceSupplier(WebMvcProperties::new).register(context);
    org.springframework.boot.autoconfigure.web.servlet.ContextBootstrapInitializer.registerDispatcherServletAutoConfiguration_DispatcherServletRegistrationConfiguration(context);
    org.springframework.boot.autoconfigure.web.servlet.ContextBootstrapInitializer.registerDispatcherServletRegistrationConfiguration_dispatcherServletRegistration(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration", DispatcherServletAutoConfiguration.class)
        .instanceSupplier(DispatcherServletAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration", TaskExecutionAutoConfiguration.class)
        .instanceSupplier(TaskExecutionAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("taskExecutorBuilder", TaskExecutorBuilder.class).withFactoryMethod(TaskExecutionAutoConfiguration.class, "taskExecutorBuilder", TaskExecutionProperties.class, ObjectProvider.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(TaskExecutionAutoConfiguration.class).taskExecutorBuilder(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    BeanDefinitionRegistrar.of("applicationTaskExecutor", ThreadPoolTaskExecutor.class).withFactoryMethod(TaskExecutionAutoConfiguration.class, "applicationTaskExecutor", TaskExecutorBuilder.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(TaskExecutionAutoConfiguration.class).applicationTaskExecutor(attributes.get(0)))).customize((bd) -> bd.setLazyInit(true)).register(context);
    BeanDefinitionRegistrar.of("spring.task.execution-org.springframework.boot.autoconfigure.task.TaskExecutionProperties", TaskExecutionProperties.class)
        .instanceSupplier(TaskExecutionProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration", ValidationAutoConfiguration.class)
        .instanceSupplier(ValidationAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("defaultValidator", LocalValidatorFactoryBean.class).withFactoryMethod(ValidationAutoConfiguration.class, "defaultValidator", ApplicationContext.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> ValidationAutoConfiguration.defaultValidator(attributes.get(0)))).customize((bd) -> bd.setRole(2)).register(context);
    BeanDefinitionRegistrar.of("methodValidationPostProcessor", MethodValidationPostProcessor.class).withFactoryMethod(ValidationAutoConfiguration.class, "methodValidationPostProcessor", Environment.class, Validator.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> ValidationAutoConfiguration.methodValidationPostProcessor(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    org.springframework.boot.autoconfigure.web.servlet.error.ContextBootstrapInitializer.registerErrorMvcAutoConfiguration_WhitelabelErrorViewConfiguration(context);
    org.springframework.boot.autoconfigure.web.servlet.error.ContextBootstrapInitializer.registerWhitelabelErrorViewConfiguration_error(context);
    org.springframework.boot.autoconfigure.web.servlet.error.ContextBootstrapInitializer.registerWhitelabelErrorViewConfiguration_beanNameViewResolver(context);
    org.springframework.boot.autoconfigure.web.servlet.error.ContextBootstrapInitializer.registerErrorMvcAutoConfiguration_DefaultErrorViewResolverConfiguration(context);
    org.springframework.boot.autoconfigure.web.servlet.error.ContextBootstrapInitializer.registerDefaultErrorViewResolverConfiguration_conventionErrorViewResolver(context);
    BeanDefinitionRegistrar.of("spring.web-org.springframework.boot.autoconfigure.web.WebProperties", WebProperties.class)
        .instanceSupplier(WebProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration", ErrorMvcAutoConfiguration.class).withConstructor(ServerProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> new ErrorMvcAutoConfiguration(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("errorAttributes", DefaultErrorAttributes.class).withFactoryMethod(ErrorMvcAutoConfiguration.class, "errorAttributes")
        .instanceSupplier(() -> context.getBean(ErrorMvcAutoConfiguration.class).errorAttributes()).register(context);
    BeanDefinitionRegistrar.of("basicErrorController", BasicErrorController.class).withFactoryMethod(ErrorMvcAutoConfiguration.class, "basicErrorController", ErrorAttributes.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(ErrorMvcAutoConfiguration.class).basicErrorController(attributes.get(0), attributes.get(1)))).register(context);
    org.springframework.boot.autoconfigure.web.servlet.error.ContextBootstrapInitializer.registerErrorMvcAutoConfiguration_errorPageCustomizer(context);
    org.springframework.boot.autoconfigure.web.servlet.error.ContextBootstrapInitializer.registerErrorMvcAutoConfiguration_preserveErrorControllerTargetClassPostProcessor(context);
    org.springframework.boot.autoconfigure.web.servlet.ContextBootstrapInitializer.registerWebMvcAutoConfiguration_EnableWebMvcConfiguration(context);
    BeanDefinitionRegistrar.of("requestMappingHandlerAdapter", RequestMappingHandlerAdapter.class).withFactoryMethod(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class, "requestMappingHandlerAdapter", ContentNegotiationManager.class, FormattingConversionService.class, org.springframework.validation.Validator.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class).requestMappingHandlerAdapter(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    BeanDefinitionRegistrar.of("requestMappingHandlerMapping", RequestMappingHandlerMapping.class).withFactoryMethod(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class, "requestMappingHandlerMapping", ContentNegotiationManager.class, FormattingConversionService.class, ResourceUrlProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class).requestMappingHandlerMapping(attributes.get(0), attributes.get(1), attributes.get(2)))).customize((bd) -> bd.setPrimary(true)).register(context);
    org.springframework.boot.autoconfigure.web.servlet.ContextBootstrapInitializer.registerEnableWebMvcConfiguration_welcomePageHandlerMapping(context);
    BeanDefinitionRegistrar.of("localeResolver", LocaleResolver.class).withFactoryMethod(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class, "localeResolver")
        .instanceSupplier(() -> context.getBean(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class).localeResolver()).register(context);
    BeanDefinitionRegistrar.of("themeResolver", ThemeResolver.class).withFactoryMethod(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class, "themeResolver")
        .instanceSupplier(() -> context.getBean(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class).themeResolver()).register(context);
    BeanDefinitionRegistrar.of("flashMapManager", FlashMapManager.class).withFactoryMethod(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class, "flashMapManager")
        .instanceSupplier(() -> context.getBean(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class).flashMapManager()).register(context);
    BeanDefinitionRegistrar.of("mvcConversionService", FormattingConversionService.class).withFactoryMethod(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class, "mvcConversionService")
        .instanceSupplier(() -> context.getBean(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class).mvcConversionService()).register(context);
    BeanDefinitionRegistrar.of("mvcValidator", org.springframework.validation.Validator.class).withFactoryMethod(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class, "mvcValidator")
        .instanceSupplier(() -> context.getBean(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class).mvcValidator()).register(context);
    BeanDefinitionRegistrar.of("mvcContentNegotiationManager", ContentNegotiationManager.class).withFactoryMethod(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class, "mvcContentNegotiationManager")
        .instanceSupplier(() -> context.getBean(WebMvcAutoConfiguration.EnableWebMvcConfiguration.class).mvcContentNegotiationManager()).register(context);
    BeanDefinitionRegistrar.of("mvcPatternParser", PathPatternParser.class).withFactoryMethod(WebMvcConfigurationSupport.class, "mvcPatternParser")
        .instanceSupplier(() -> context.getBean(WebMvcConfigurationSupport.class).mvcPatternParser()).register(context);
    BeanDefinitionRegistrar.of("mvcUrlPathHelper", UrlPathHelper.class).withFactoryMethod(WebMvcConfigurationSupport.class, "mvcUrlPathHelper")
        .instanceSupplier(() -> context.getBean(WebMvcConfigurationSupport.class).mvcUrlPathHelper()).register(context);
    BeanDefinitionRegistrar.of("mvcPathMatcher", PathMatcher.class).withFactoryMethod(WebMvcConfigurationSupport.class, "mvcPathMatcher")
        .instanceSupplier(() -> context.getBean(WebMvcConfigurationSupport.class).mvcPathMatcher()).register(context);
    BeanDefinitionRegistrar.of("viewControllerHandlerMapping", HandlerMapping.class).withFactoryMethod(WebMvcConfigurationSupport.class, "viewControllerHandlerMapping", FormattingConversionService.class, ResourceUrlProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(WebMvcConfigurationSupport.class).viewControllerHandlerMapping(attributes.get(0), attributes.get(1)))).register(context);
    BeanDefinitionRegistrar.of("beanNameHandlerMapping", BeanNameUrlHandlerMapping.class).withFactoryMethod(WebMvcConfigurationSupport.class, "beanNameHandlerMapping", FormattingConversionService.class, ResourceUrlProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(WebMvcConfigurationSupport.class).beanNameHandlerMapping(attributes.get(0), attributes.get(1)))).register(context);
    BeanDefinitionRegistrar.of("routerFunctionMapping", RouterFunctionMapping.class).withFactoryMethod(WebMvcConfigurationSupport.class, "routerFunctionMapping", FormattingConversionService.class, ResourceUrlProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(WebMvcConfigurationSupport.class).routerFunctionMapping(attributes.get(0), attributes.get(1)))).register(context);
    BeanDefinitionRegistrar.of("resourceHandlerMapping", HandlerMapping.class).withFactoryMethod(WebMvcConfigurationSupport.class, "resourceHandlerMapping", ContentNegotiationManager.class, FormattingConversionService.class, ResourceUrlProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(WebMvcConfigurationSupport.class).resourceHandlerMapping(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    BeanDefinitionRegistrar.of("mvcResourceUrlProvider", ResourceUrlProvider.class).withFactoryMethod(WebMvcConfigurationSupport.class, "mvcResourceUrlProvider")
        .instanceSupplier(() -> context.getBean(WebMvcConfigurationSupport.class).mvcResourceUrlProvider()).register(context);
    BeanDefinitionRegistrar.of("defaultServletHandlerMapping", HandlerMapping.class).withFactoryMethod(WebMvcConfigurationSupport.class, "defaultServletHandlerMapping")
        .instanceSupplier(() -> context.getBean(WebMvcConfigurationSupport.class).defaultServletHandlerMapping()).register(context);
    BeanDefinitionRegistrar.of("handlerFunctionAdapter", HandlerFunctionAdapter.class).withFactoryMethod(WebMvcConfigurationSupport.class, "handlerFunctionAdapter")
        .instanceSupplier(() -> context.getBean(WebMvcConfigurationSupport.class).handlerFunctionAdapter()).register(context);
    BeanDefinitionRegistrar.of("mvcUriComponentsContributor", CompositeUriComponentsContributor.class).withFactoryMethod(WebMvcConfigurationSupport.class, "mvcUriComponentsContributor", FormattingConversionService.class, RequestMappingHandlerAdapter.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(WebMvcConfigurationSupport.class).mvcUriComponentsContributor(attributes.get(0), attributes.get(1)))).register(context);
    BeanDefinitionRegistrar.of("httpRequestHandlerAdapter", HttpRequestHandlerAdapter.class).withFactoryMethod(WebMvcConfigurationSupport.class, "httpRequestHandlerAdapter")
        .instanceSupplier(() -> context.getBean(WebMvcConfigurationSupport.class).httpRequestHandlerAdapter()).register(context);
    BeanDefinitionRegistrar.of("simpleControllerHandlerAdapter", SimpleControllerHandlerAdapter.class).withFactoryMethod(WebMvcConfigurationSupport.class, "simpleControllerHandlerAdapter")
        .instanceSupplier(() -> context.getBean(WebMvcConfigurationSupport.class).simpleControllerHandlerAdapter()).register(context);
    BeanDefinitionRegistrar.of("handlerExceptionResolver", HandlerExceptionResolver.class).withFactoryMethod(WebMvcConfigurationSupport.class, "handlerExceptionResolver", ContentNegotiationManager.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(WebMvcConfigurationSupport.class).handlerExceptionResolver(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("mvcViewResolver", ViewResolver.class).withFactoryMethod(WebMvcConfigurationSupport.class, "mvcViewResolver", ContentNegotiationManager.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(WebMvcConfigurationSupport.class).mvcViewResolver(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("mvcHandlerMappingIntrospector", HandlerMappingIntrospector.class).withFactoryMethod(WebMvcConfigurationSupport.class, "mvcHandlerMappingIntrospector")
        .instanceSupplier(() -> context.getBean(WebMvcConfigurationSupport.class).mvcHandlerMappingIntrospector()).customize((bd) -> bd.setLazyInit(true)).register(context);
    BeanDefinitionRegistrar.of("viewNameTranslator", RequestToViewNameTranslator.class).withFactoryMethod(WebMvcConfigurationSupport.class, "viewNameTranslator")
        .instanceSupplier(() -> context.getBean(WebMvcConfigurationSupport.class).viewNameTranslator()).register(context);
    org.springframework.boot.autoconfigure.web.servlet.ContextBootstrapInitializer.registerWebMvcAutoConfiguration_WebMvcAutoConfigurationAdapter(context);
    BeanDefinitionRegistrar.of("defaultViewResolver", InternalResourceViewResolver.class).withFactoryMethod(WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.class, "defaultViewResolver")
        .instanceSupplier(() -> context.getBean(WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.class).defaultViewResolver()).register(context);
    BeanDefinitionRegistrar.of("viewResolver", ContentNegotiatingViewResolver.class).withFactoryMethod(WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.class, "viewResolver", BeanFactory.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.class).viewResolver(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("requestContextFilter", RequestContextFilter.class).withFactoryMethod(WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.class, "requestContextFilter")
        .instanceSupplier(() -> WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter.requestContextFilter()).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration", WebMvcAutoConfiguration.class)
        .instanceSupplier(WebMvcAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("formContentFilter", OrderedFormContentFilter.class).withFactoryMethod(WebMvcAutoConfiguration.class, "formContentFilter")
        .instanceSupplier(() -> context.getBean(WebMvcAutoConfiguration.class).formContentFilter()).register(context);
    org.springframework.boot.autoconfigure.aop.ContextBootstrapInitializer.registerAspectJAutoProxyingConfiguration_JdkDynamicAutoProxyConfiguration(context);
    BeanDefinitionRegistrar.of("org.springframework.aop.config.internalAutoProxyCreator", AnnotationAwareAspectJAutoProxyCreator.class)
        .instanceSupplier(AnnotationAwareAspectJAutoProxyCreator::new).customize((bd) -> {
      bd.setRole(2);
      bd.getPropertyValues().addPropertyValue("order", -2147483648);
    }).register(context);
    org.springframework.boot.autoconfigure.aop.ContextBootstrapInitializer.registerAopAutoConfiguration_AspectJAutoProxyingConfiguration(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.aop.AopAutoConfiguration", AopAutoConfiguration.class)
        .instanceSupplier(AopAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.availability.ApplicationAvailabilityAutoConfiguration", ApplicationAvailabilityAutoConfiguration.class)
        .instanceSupplier(ApplicationAvailabilityAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("applicationAvailability", ApplicationAvailabilityBean.class).withFactoryMethod(ApplicationAvailabilityAutoConfiguration.class, "applicationAvailability")
        .instanceSupplier(() -> context.getBean(ApplicationAvailabilityAutoConfiguration.class).applicationAvailability()).register(context);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonAutoConfiguration_Jackson2ObjectMapperBuilderCustomizerConfiguration(context);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJackson2ObjectMapperBuilderCustomizerConfiguration_standardJacksonObjectMapperBuilderCustomizer(context);
    BeanDefinitionRegistrar.of("spring.jackson-org.springframework.boot.autoconfigure.jackson.JacksonProperties", JacksonProperties.class)
        .instanceSupplier(JacksonProperties::new).register(context);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonAutoConfiguration_JacksonObjectMapperBuilderConfiguration(context);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonObjectMapperBuilderConfiguration_jacksonObjectMapperBuilder(context);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonAutoConfiguration_ParameterNamesModuleConfiguration(context);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerParameterNamesModuleConfiguration_parameterNamesModule(context);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonAutoConfiguration_JacksonObjectMapperConfiguration(context);
    org.springframework.boot.autoconfigure.jackson.ContextBootstrapInitializer.registerJacksonObjectMapperConfiguration_jacksonObjectMapper(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration", JacksonAutoConfiguration.class)
        .instanceSupplier(JacksonAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("jsonComponentModule", JsonComponentModule.class).withFactoryMethod(JacksonAutoConfiguration.class, "jsonComponentModule")
        .instanceSupplier(() -> context.getBean(JacksonAutoConfiguration.class).jsonComponentModule()).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration", ConfigurationPropertiesAutoConfiguration.class)
        .instanceSupplier(ConfigurationPropertiesAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration", LifecycleAutoConfiguration.class)
        .instanceSupplier(LifecycleAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("lifecycleProcessor", DefaultLifecycleProcessor.class).withFactoryMethod(LifecycleAutoConfiguration.class, "defaultLifecycleProcessor", LifecycleProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(LifecycleAutoConfiguration.class).defaultLifecycleProcessor(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("spring.lifecycle-org.springframework.boot.autoconfigure.context.LifecycleProperties", LifecycleProperties.class)
        .instanceSupplier(LifecycleProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration", PersistenceExceptionTranslationAutoConfiguration.class)
        .instanceSupplier(PersistenceExceptionTranslationAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("persistenceExceptionTranslationPostProcessor", PersistenceExceptionTranslationPostProcessor.class).withFactoryMethod(PersistenceExceptionTranslationAutoConfiguration.class, "persistenceExceptionTranslationPostProcessor", Environment.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> PersistenceExceptionTranslationAutoConfiguration.persistenceExceptionTranslationPostProcessor(attributes.get(0)))).register(context);
    org.springframework.boot.autoconfigure.http.ContextBootstrapInitializer.registerHttpMessageConvertersAutoConfiguration_StringHttpMessageConverterConfiguration(context);
    org.springframework.boot.autoconfigure.http.ContextBootstrapInitializer.registerStringHttpMessageConverterConfiguration_stringHttpMessageConverter(context);
    org.springframework.boot.autoconfigure.http.ContextBootstrapInitializer.registerJacksonHttpMessageConvertersConfiguration_MappingJackson2HttpMessageConverterConfiguration(context);
    org.springframework.boot.autoconfigure.http.ContextBootstrapInitializer.registerMappingJackson2HttpMessageConverterConfiguration_mappingJackson2HttpMessageConverter(context);
    org.springframework.boot.autoconfigure.http.ContextBootstrapInitializer.registerJacksonHttpMessageConvertersConfiguration(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration", HttpMessageConvertersAutoConfiguration.class)
        .instanceSupplier(HttpMessageConvertersAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("messageConverters", HttpMessageConverters.class).withFactoryMethod(HttpMessageConvertersAutoConfiguration.class, "messageConverters", ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(HttpMessageConvertersAutoConfiguration.class).messageConverters(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration", ProjectInfoAutoConfiguration.class).withConstructor(ProjectInfoProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> new ProjectInfoAutoConfiguration(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("spring.info-org.springframework.boot.autoconfigure.info.ProjectInfoProperties", ProjectInfoProperties.class)
        .instanceSupplier(ProjectInfoProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration", TaskSchedulingAutoConfiguration.class)
        .instanceSupplier(TaskSchedulingAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("scheduledBeanLazyInitializationExcludeFilter", LazyInitializationExcludeFilter.class).withFactoryMethod(TaskSchedulingAutoConfiguration.class, "scheduledBeanLazyInitializationExcludeFilter")
        .instanceSupplier(() -> TaskSchedulingAutoConfiguration.scheduledBeanLazyInitializationExcludeFilter()).register(context);
    BeanDefinitionRegistrar.of("taskSchedulerBuilder", TaskSchedulerBuilder.class).withFactoryMethod(TaskSchedulingAutoConfiguration.class, "taskSchedulerBuilder", TaskSchedulingProperties.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(TaskSchedulingAutoConfiguration.class).taskSchedulerBuilder(attributes.get(0), attributes.get(1)))).register(context);
    BeanDefinitionRegistrar.of("spring.task.scheduling-org.springframework.boot.autoconfigure.task.TaskSchedulingProperties", TaskSchedulingProperties.class)
        .instanceSupplier(TaskSchedulingProperties::new).register(context);
    org.springframework.boot.autoconfigure.integration.ContextBootstrapInitializer.registerIntegrationAutoConfiguration_IntegrationComponentScanConfiguration(context);
    BeanDefinitionRegistrar.of("org.springframework.integration.config.IntegrationManagementConfiguration", IntegrationManagementConfiguration.class)
        .instanceSupplier(IntegrationManagementConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("integrationManagementConfigurer", IntegrationManagementConfigurer.class).withFactoryMethod(IntegrationManagementConfiguration.class, "managementConfigurer", ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(IntegrationManagementConfiguration.class).managementConfigurer(attributes.get(0)))).customize((bd) -> bd.setRole(2)).register(context);
    org.springframework.boot.autoconfigure.integration.ContextBootstrapInitializer.registerIntegrationManagementConfiguration_EnableIntegrationManagementConfiguration(context);
    org.springframework.boot.autoconfigure.integration.ContextBootstrapInitializer.registerIntegrationAutoConfiguration_IntegrationManagementConfiguration(context);
    org.springframework.boot.autoconfigure.integration.ContextBootstrapInitializer.registerIntegrationAutoConfiguration_IntegrationTaskSchedulerConfiguration(context);
    org.springframework.boot.autoconfigure.integration.ContextBootstrapInitializer.registerIntegrationTaskSchedulerConfiguration_taskScheduler(context);
    org.springframework.boot.autoconfigure.integration.ContextBootstrapInitializer.registerIntegrationAutoConfiguration_IntegrationConfiguration(context);
    org.springframework.boot.autoconfigure.integration.ContextBootstrapInitializer.registerIntegrationConfiguration_defaultPollerMetadata(context);
    BeanDefinitionRegistrar.of("org.springframework.integration.internalMessagingAnnotationPostProcessor", MessagingAnnotationPostProcessor.class)
        .instanceSupplier(MessagingAnnotationPostProcessor::new).customize((bd) -> bd.setRole(2)).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.integration.IntegrationAutoConfiguration", IntegrationAutoConfiguration.class)
        .instanceSupplier(IntegrationAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("integrationGlobalProperties", IntegrationProperties.class).withFactoryMethod(IntegrationAutoConfiguration.class, "integrationGlobalProperties", org.springframework.boot.autoconfigure.integration.IntegrationProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> IntegrationAutoConfiguration.integrationGlobalProperties(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("spring.integration-org.springframework.boot.autoconfigure.integration.IntegrationProperties", org.springframework.boot.autoconfigure.integration.IntegrationProperties.class)
        .instanceSupplier(org.springframework.boot.autoconfigure.integration.IntegrationProperties::new).register(context);
    org.springframework.boot.autoconfigure.kafka.ContextBootstrapInitializer.registerKafkaAnnotationDrivenConfiguration_EnableKafkaConfiguration(context);
    BeanDefinitionRegistrar.of("org.springframework.kafka.config.internalKafkaListenerAnnotationProcessor", ResolvableType.forClassWithGenerics(KafkaListenerAnnotationBeanPostProcessor.class, Object.class, Object.class))
        .instanceSupplier(KafkaListenerAnnotationBeanPostProcessor::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.kafka.config.internalKafkaListenerEndpointRegistry", KafkaListenerEndpointRegistry.class)
        .instanceSupplier(KafkaListenerEndpointRegistry::new).register(context);
    org.springframework.boot.autoconfigure.kafka.ContextBootstrapInitializer.registerKafkaAnnotationDrivenConfiguration(context);
    org.springframework.boot.autoconfigure.kafka.ContextBootstrapInitializer.registerKafkaAnnotationDrivenConfiguration_kafkaListenerContainerFactoryConfigurer(context);
    org.springframework.boot.autoconfigure.kafka.ContextBootstrapInitializer.registerKafkaAnnotationDrivenConfiguration_kafkaListenerContainerFactory(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration", KafkaAutoConfiguration.class).withConstructor(KafkaProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> new KafkaAutoConfiguration(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("kafkaTemplate", ResolvableType.forClassWithGenerics(KafkaTemplate.class, Object.class, Object.class)).withFactoryMethod(KafkaAutoConfiguration.class, "kafkaTemplate", ProducerFactory.class, ProducerListener.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(KafkaAutoConfiguration.class).kafkaTemplate(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    BeanDefinitionRegistrar.of("kafkaProducerListener", ResolvableType.forClassWithGenerics(ProducerListener.class, Object.class, Object.class)).withFactoryMethod(KafkaAutoConfiguration.class, "kafkaProducerListener")
        .instanceSupplier(() -> context.getBean(KafkaAutoConfiguration.class).kafkaProducerListener()).register(context);
    BeanDefinitionRegistrar.of("kafkaConsumerFactory", ResolvableType.forClassWithGenerics(ConsumerFactory.class, Object.class, Object.class)).withFactoryMethod(KafkaAutoConfiguration.class, "kafkaConsumerFactory", ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(KafkaAutoConfiguration.class).kafkaConsumerFactory(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("kafkaProducerFactory", ResolvableType.forClassWithGenerics(ProducerFactory.class, Object.class, Object.class)).withFactoryMethod(KafkaAutoConfiguration.class, "kafkaProducerFactory", ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(KafkaAutoConfiguration.class).kafkaProducerFactory(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("kafkaAdmin", KafkaAdmin.class).withFactoryMethod(KafkaAutoConfiguration.class, "kafkaAdmin")
        .instanceSupplier(() -> context.getBean(KafkaAutoConfiguration.class).kafkaAdmin()).register(context);
    BeanDefinitionRegistrar.of("spring.kafka-org.springframework.boot.autoconfigure.kafka.KafkaProperties", KafkaProperties.class)
        .instanceSupplier(KafkaProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration", SqlInitializationAutoConfiguration.class)
        .instanceSupplier(SqlInitializationAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("spring.sql.init-org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties", SqlInitializationProperties.class)
        .instanceSupplier(SqlInitializationProperties::new).register(context);
    org.springframework.boot.sql.init.dependency.ContextBootstrapInitializer.registerDatabaseInitializationDependencyConfigurer_DependsOnDatabaseInitializationPostProcessor(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration", TransactionAutoConfiguration.class)
        .instanceSupplier(TransactionAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("platformTransactionManagerCustomizers", TransactionManagerCustomizers.class).withFactoryMethod(TransactionAutoConfiguration.class, "platformTransactionManagerCustomizers", ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(TransactionAutoConfiguration.class).platformTransactionManagerCustomizers(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("spring.transaction-org.springframework.boot.autoconfigure.transaction.TransactionProperties", TransactionProperties.class)
        .instanceSupplier(TransactionProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration", RestTemplateAutoConfiguration.class)
        .instanceSupplier(RestTemplateAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("restTemplateBuilderConfigurer", RestTemplateBuilderConfigurer.class).withFactoryMethod(RestTemplateAutoConfiguration.class, "restTemplateBuilderConfigurer", ObjectProvider.class, ObjectProvider.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(RestTemplateAutoConfiguration.class).restTemplateBuilderConfigurer(attributes.get(0), attributes.get(1), attributes.get(2)))).customize((bd) -> bd.setLazyInit(true)).register(context);
    BeanDefinitionRegistrar.of("restTemplateBuilder", RestTemplateBuilder.class).withFactoryMethod(RestTemplateAutoConfiguration.class, "restTemplateBuilder", RestTemplateBuilderConfigurer.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(RestTemplateAutoConfiguration.class).restTemplateBuilder(attributes.get(0)))).customize((bd) -> bd.setLazyInit(true)).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration$TomcatWebServerFactoryCustomizerConfiguration", EmbeddedWebServerFactoryCustomizerAutoConfiguration.TomcatWebServerFactoryCustomizerConfiguration.class)
        .instanceSupplier(EmbeddedWebServerFactoryCustomizerAutoConfiguration.TomcatWebServerFactoryCustomizerConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("tomcatWebServerFactoryCustomizer", TomcatWebServerFactoryCustomizer.class).withFactoryMethod(EmbeddedWebServerFactoryCustomizerAutoConfiguration.TomcatWebServerFactoryCustomizerConfiguration.class, "tomcatWebServerFactoryCustomizer", Environment.class, ServerProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(EmbeddedWebServerFactoryCustomizerAutoConfiguration.TomcatWebServerFactoryCustomizerConfiguration.class).tomcatWebServerFactoryCustomizer(attributes.get(0), attributes.get(1)))).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration", EmbeddedWebServerFactoryCustomizerAutoConfiguration.class)
        .instanceSupplier(EmbeddedWebServerFactoryCustomizerAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.reactive.WebSessionIdResolverAutoConfiguration", WebSessionIdResolverAutoConfiguration.class).withConstructor(ServerProperties.class, WebFluxProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> new WebSessionIdResolverAutoConfiguration(attributes.get(0), attributes.get(1)))).register(context);
    BeanDefinitionRegistrar.of("webSessionIdResolver", WebSessionIdResolver.class).withFactoryMethod(WebSessionIdResolverAutoConfiguration.class, "webSessionIdResolver")
        .instanceSupplier(() -> context.getBean(WebSessionIdResolverAutoConfiguration.class).webSessionIdResolver()).register(context);
    BeanDefinitionRegistrar.of("spring.webflux-org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties", WebFluxProperties.class)
        .instanceSupplier(WebFluxProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration", HttpEncodingAutoConfiguration.class).withConstructor(ServerProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> new HttpEncodingAutoConfiguration(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("characterEncodingFilter", CharacterEncodingFilter.class).withFactoryMethod(HttpEncodingAutoConfiguration.class, "characterEncodingFilter")
        .instanceSupplier(() -> context.getBean(HttpEncodingAutoConfiguration.class).characterEncodingFilter()).register(context);
    org.springframework.boot.autoconfigure.web.servlet.ContextBootstrapInitializer.registerHttpEncodingAutoConfiguration_localeCharsetMappingsCustomizer(context);
    BeanDefinitionRegistrar.of("org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration", MultipartAutoConfiguration.class).withConstructor(MultipartProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> new MultipartAutoConfiguration(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("multipartConfigElement", MultipartConfigElement.class).withFactoryMethod(MultipartAutoConfiguration.class, "multipartConfigElement")
        .instanceSupplier(() -> context.getBean(MultipartAutoConfiguration.class).multipartConfigElement()).register(context);
    BeanDefinitionRegistrar.of("multipartResolver", StandardServletMultipartResolver.class).withFactoryMethod(MultipartAutoConfiguration.class, "multipartResolver")
        .instanceSupplier(() -> context.getBean(MultipartAutoConfiguration.class).multipartResolver()).register(context);
    BeanDefinitionRegistrar.of("spring.servlet.multipart-org.springframework.boot.autoconfigure.web.servlet.MultipartProperties", MultipartProperties.class)
        .instanceSupplier(MultipartProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration", ConfigurationPropertiesRebinderAutoConfiguration.class)
        .instanceSupplier(ConfigurationPropertiesRebinderAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("configurationPropertiesBeans", ConfigurationPropertiesBeans.class).withFactoryMethod(ConfigurationPropertiesRebinderAutoConfiguration.class, "configurationPropertiesBeans")
        .instanceSupplier(() -> ConfigurationPropertiesRebinderAutoConfiguration.configurationPropertiesBeans()).register(context);
    BeanDefinitionRegistrar.of("configurationPropertiesRebinder", ConfigurationPropertiesRebinder.class).withFactoryMethod(ConfigurationPropertiesRebinderAutoConfiguration.class, "configurationPropertiesRebinder", ConfigurationPropertiesBeans.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(ConfigurationPropertiesRebinderAutoConfiguration.class).configurationPropertiesRebinder(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.autoconfigure.LifecycleMvcEndpointAutoConfiguration", LifecycleMvcEndpointAutoConfiguration.class)
        .instanceSupplier(LifecycleMvcEndpointAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("environmentManager", EnvironmentManager.class).withFactoryMethod(LifecycleMvcEndpointAutoConfiguration.class, "environmentManager", ConfigurableEnvironment.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(LifecycleMvcEndpointAutoConfiguration.class).environmentManager(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration", CompositeDiscoveryClientAutoConfiguration.class)
        .instanceSupplier(CompositeDiscoveryClientAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("compositeDiscoveryClient", CompositeDiscoveryClient.class).withFactoryMethod(CompositeDiscoveryClientAutoConfiguration.class, "compositeDiscoveryClient", List.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(CompositeDiscoveryClientAutoConfiguration.class).compositeDiscoveryClient(attributes.get(0)))).customize((bd) -> bd.setPrimary(true)).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration", SimpleDiscoveryClientAutoConfiguration.class)
        .instanceSupplier((instanceContext) -> {
          SimpleDiscoveryClientAutoConfiguration bean = new SimpleDiscoveryClientAutoConfiguration();
          instanceContext.method("setServer", ServerProperties.class)
              .resolve(context, false).ifResolved((attributes) -> bean.setServer(attributes.get(0)));
          instanceContext.method("setInet", InetUtils.class)
              .invoke(context, (attributes) -> bean.setInet(attributes.get(0)));
          return bean;
        }).register(context);
    BeanDefinitionRegistrar.of("simpleDiscoveryProperties", SimpleDiscoveryProperties.class).withFactoryMethod(SimpleDiscoveryClientAutoConfiguration.class, "simpleDiscoveryProperties", String.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(SimpleDiscoveryClientAutoConfiguration.class).simpleDiscoveryProperties(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("simpleDiscoveryClient", DiscoveryClient.class).withFactoryMethod(SimpleDiscoveryClientAutoConfiguration.class, "simpleDiscoveryClient", SimpleDiscoveryProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(SimpleDiscoveryClientAutoConfiguration.class).simpleDiscoveryClient(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.client.CommonsClientAutoConfiguration", CommonsClientAutoConfiguration.class)
        .instanceSupplier(CommonsClientAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.client.ReactiveCommonsClientAutoConfiguration", ReactiveCommonsClientAutoConfiguration.class)
        .instanceSupplier(ReactiveCommonsClientAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.client.loadbalancer.LoadBalancerDefaultMappingsProviderAutoConfiguration", LoadBalancerDefaultMappingsProviderAutoConfiguration.class)
        .instanceSupplier(LoadBalancerDefaultMappingsProviderAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("loadBalancerClientsDefaultsMappingsProvider", DefaultsBindHandlerAdvisor.MappingsProvider.class).withFactoryMethod(LoadBalancerDefaultMappingsProviderAutoConfiguration.class, "loadBalancerClientsDefaultsMappingsProvider")
        .instanceSupplier(() -> context.getBean(LoadBalancerDefaultMappingsProviderAutoConfiguration.class).loadBalancerClientsDefaultsMappingsProvider()).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationConfiguration", AutoServiceRegistrationConfiguration.class)
        .instanceSupplier(AutoServiceRegistrationConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("spring.cloud.service-registry.auto-registration-org.springframework.cloud.client.serviceregistry.AutoServiceRegistrationProperties", AutoServiceRegistrationProperties.class)
        .instanceSupplier(AutoServiceRegistrationProperties::new).register(context);
    org.springframework.cloud.client.serviceregistry.ContextBootstrapInitializer.registerAutoServiceRegistrationAutoConfiguration(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.client.serviceregistry.ServiceRegistryAutoConfiguration", ServiceRegistryAutoConfiguration.class)
        .instanceSupplier(ServiceRegistryAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.commons.config.CommonsConfigAutoConfiguration", CommonsConfigAutoConfiguration.class)
        .instanceSupplier(CommonsConfigAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("defaultsBindHandlerAdvisor", DefaultsBindHandlerAdvisor.class).withFactoryMethod(CommonsConfigAutoConfiguration.class, "defaultsBindHandlerAdvisor", DefaultsBindHandlerAdvisor.MappingsProvider[].class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(CommonsConfigAutoConfiguration.class).defaultsBindHandlerAdvisor(attributes.get(0)))).register(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBridgeConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBridgeConfiguration_braveTracer(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBridgeConfiguration_braveCurrentTraceContext(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBridgeConfiguration_braveSpanCustomizer(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBridgeConfiguration_bravePropagator(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBridgeConfiguration_compositePropagationFactorySupplier(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBridgeConfiguration_traceCompositeSpanHandler(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBridgeConfiguration_braveReactorContextBeanDefinitionRegistryPostProcessor(context);
    BeanDefinitionRegistrar.of("spring.sleuth.propagation-org.springframework.cloud.sleuth.autoconfig.brave.SleuthPropagationProperties", SleuthPropagationProperties.class)
        .instanceSupplier(SleuthPropagationProperties::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBaggageConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBaggageConfiguration_baggageKeys(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBaggageConfiguration_localKeys(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBaggageConfiguration_propagationKeys(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBaggageConfiguration_whiteListedMDCKeys(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBaggageConfiguration_baggagePropagationFactoryBuilder(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBaggageConfiguration_sleuthPropagationWithB3Baggage(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBaggageConfiguration_correlationScopeDecoratorBuilder(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveBaggageConfiguration_correlationScopeDecorator(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveSamplerConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveSamplerConfiguration_sleuthTraceSampler(context);
    BeanDefinitionRegistrar.of("spring.sleuth.sampler-org.springframework.cloud.sleuth.autoconfig.brave.SamplerProperties", SamplerProperties.class)
        .instanceSupplier(SamplerProperties::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.ContextBootstrapInitializer.registerBraveHttpConfiguration_BraveWebFilterConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.ContextBootstrapInitializer.registerBraveWebFilterConfiguration_braveSpanFromContextRetriever(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.ContextBootstrapInitializer.registerBraveHttpBridgeConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.ContextBootstrapInitializer.registerBraveHttpBridgeConfiguration_braveHttpClientHandler(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.ContextBootstrapInitializer.registerBraveHttpBridgeConfiguration_braveHttpServerHandler(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.BraveHttpConfiguration", BraveHttpConfiguration.class)
        .instanceSupplier(BraveHttpConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.ContextBootstrapInitializer.registerBraveHttpConfiguration_httpTracing(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.ContextBootstrapInitializer.registerBraveHttpConfiguration_sleuthHttpClientSampler(context);
    BeanDefinitionRegistrar.of("spring.sleuth.web-org.springframework.cloud.sleuth.autoconfig.instrument.web.SleuthWebProperties", SleuthWebProperties.class)
        .instanceSupplier(SleuthWebProperties::new).register(context);
    BeanDefinitionRegistrar.of("spring.sleuth.http-org.springframework.cloud.sleuth.autoconfig.instrument.web.SleuthHttpProperties", SleuthHttpProperties.class)
        .instanceSupplier(SleuthHttpProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.TraceConfiguration", TraceConfiguration.class)
        .instanceSupplier(TraceConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.ContextBootstrapInitializer.registerTraceConfiguration_defaultSpanNamer(context);
    org.springframework.cloud.sleuth.autoconfig.ContextBootstrapInitializer.registerTraceConfiguration_spanIgnoringSpanExporter(context);
    BeanDefinitionRegistrar.of("spring.sleuth.span-filter-org.springframework.cloud.sleuth.autoconfig.SleuthSpanFilterProperties", SleuthSpanFilterProperties.class)
        .instanceSupplier(SleuthSpanFilterProperties::new).register(context);
    BeanDefinitionRegistrar.of("spring.sleuth.tracer-org.springframework.cloud.sleuth.autoconfig.SleuthTracerProperties", SleuthTracerProperties.class)
        .instanceSupplier(SleuthTracerProperties::new).register(context);
    BeanDefinitionRegistrar.of("spring.sleuth.baggage-org.springframework.cloud.sleuth.autoconfig.SleuthBaggageProperties", SleuthBaggageProperties.class)
        .instanceSupplier(SleuthBaggageProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.SleuthAnnotationConfiguration", SleuthAnnotationConfiguration.class)
        .instanceSupplier(SleuthAnnotationConfiguration::new).customize((bd) -> bd.setRole(2)).register(context);
    org.springframework.cloud.sleuth.autoconfig.ContextBootstrapInitializer.registerSleuthAnnotationConfiguration_newSpanParser(context);
    org.springframework.cloud.sleuth.autoconfig.ContextBootstrapInitializer.registerSleuthAnnotationConfiguration_spelTagValueExpressionResolver(context);
    org.springframework.cloud.sleuth.autoconfig.ContextBootstrapInitializer.registerSleuthAnnotationConfiguration_noOpTagValueResolver(context);
    org.springframework.cloud.sleuth.autoconfig.ContextBootstrapInitializer.registerSleuthAnnotationConfiguration_sleuthAdvisorConfig(context);
    org.springframework.cloud.sleuth.autoconfig.ContextBootstrapInitializer.registerSleuthAnnotationConfiguration_reactorSleuthMethodInvocationProcessor(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.brave.BraveAutoConfiguration", BraveAutoConfiguration.class)
        .instanceSupplier(BraveAutoConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveAutoConfiguration_tracing(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveAutoConfiguration_tracer(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveAutoConfiguration_sleuthCurrentTraceContext(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveAutoConfiguration_sleuthCurrentTraceContextBuilder(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveAutoConfiguration_spanCustomizer(context);
    org.springframework.cloud.sleuth.autoconfig.brave.ContextBootstrapInitializer.registerBraveAutoConfiguration_sleuthContextListener(context);
    BeanDefinitionRegistrar.of("spring.sleuth-org.springframework.cloud.sleuth.autoconfig.brave.SleuthProperties", SleuthProperties.class)
        .instanceSupplier(SleuthProperties::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.client.ContextBootstrapInitializer.registerTraceWebClientAutoConfiguration_RestTemplateConfig(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.client.ContextBootstrapInitializer.registerRestTemplateConfig_tracingClientHttpRequestInterceptor(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.client.ContextBootstrapInitializer.registerRestTemplateConfig_traceRestTemplateCustomizer(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.client.ContextBootstrapInitializer.registerRestTemplateConfig_traceRestTemplateBeanPostProcessor(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.client.ContextBootstrapInitializer.registerTraceWebClientAutoConfiguration(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.brave.instrument.web.client.BraveWebClientAutoConfiguration", BraveWebClientAutoConfiguration.class)
        .instanceSupplier(BraveWebClientAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.commons.httpclient.HttpClientConfiguration", HttpClientConfiguration.class)
        .instanceSupplier(HttpClientConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.commons.util.UtilAutoConfiguration", UtilAutoConfiguration.class)
        .instanceSupplier(UtilAutoConfiguration::new).register(context);
    org.springframework.cloud.commons.util.ContextBootstrapInitializer.registerUtilAutoConfiguration_inetUtilsProperties(context);
    BeanDefinitionRegistrar.of("inetUtils", InetUtils.class).withFactoryMethod(UtilAutoConfiguration.class, "inetUtils", InetUtilsProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(UtilAutoConfiguration.class).inetUtils(attributes.get(0)))).register(context);
    org.springframework.cloud.function.cloudevent.ContextBootstrapInitializer.registerCloudEventsFunctionExtensionConfiguration(context);
    org.springframework.cloud.function.cloudevent.ContextBootstrapInitializer.registerCloudEventsFunctionExtensionConfiguration_nativeFunctionInvocationHelper(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration$JsonMapperConfiguration", ContextFunctionCatalogAutoConfiguration.JsonMapperConfiguration.class)
        .instanceSupplier(ContextFunctionCatalogAutoConfiguration.JsonMapperConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("jsonMapper", JsonMapper.class).withFactoryMethod(ContextFunctionCatalogAutoConfiguration.JsonMapperConfiguration.class, "jsonMapper", ApplicationContext.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(ContextFunctionCatalogAutoConfiguration.JsonMapperConfiguration.class).jsonMapper(attributes.get(0)))).register(context);
    org.springframework.cloud.function.context.config.ContextBootstrapInitializer.registerContextFunctionCatalogAutoConfiguration_PlainFunctionScanConfiguration(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration", ContextFunctionCatalogAutoConfiguration.class)
        .instanceSupplier(ContextFunctionCatalogAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("functionCatalog", FunctionRegistry.class).withFactoryMethod(ContextFunctionCatalogAutoConfiguration.class, "functionCatalog", List.class, JsonMapper.class, ConfigurableApplicationContext.class, FunctionInvocationHelper.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(ContextFunctionCatalogAutoConfiguration.class).functionCatalog(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3)))).register(context);
    org.springframework.cloud.function.context.config.ContextBootstrapInitializer.registerContextFunctionCatalogAutoConfiguration_functionRouter(context);
    BeanDefinitionRegistrar.of("spring.cloud.function-org.springframework.cloud.function.context.FunctionProperties", FunctionProperties.class)
        .instanceSupplier(FunctionProperties::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.messaging.ContextBootstrapInitializer.registerBraveMessagingAutoConfiguration_SleuthKafkaConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.messaging.ContextBootstrapInitializer.registerSleuthKafkaConfiguration_kafkaTracing(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.messaging.ContextBootstrapInitializer.registerSleuthKafkaConfiguration_sleuthKafkaAspect(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.messaging.ContextBootstrapInitializer.registerSleuthKafkaConfiguration_kafkaFactoryBeanPostProcessor(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.brave.instrument.messaging.BraveMessagingAutoConfiguration", BraveMessagingAutoConfiguration.class)
        .instanceSupplier(BraveMessagingAutoConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.messaging.ContextBootstrapInitializer.registerBraveMessagingAutoConfiguration_messagingTracing(context);
    BeanDefinitionRegistrar.of("spring.sleuth.messaging-org.springframework.cloud.sleuth.autoconfig.instrument.messaging.SleuthMessagingProperties", SleuthMessagingProperties.class)
        .instanceSupplier(SleuthMessagingProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.brave.instrument.rpc.BraveRpcAutoConfiguration", BraveRpcAutoConfiguration.class)
        .instanceSupplier(BraveRpcAutoConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.brave.instrument.rpc.ContextBootstrapInitializer.registerBraveRpcAutoConfiguration_rpcTracing(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.instrument.circuitbreaker.TraceCircuitBreakerAutoConfiguration", TraceCircuitBreakerAutoConfiguration.class)
        .instanceSupplier(TraceCircuitBreakerAutoConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.circuitbreaker.ContextBootstrapInitializer.registerTraceCircuitBreakerAutoConfiguration_traceCircuitBreakerFactoryAspect(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.circuitbreaker.ContextBootstrapInitializer.registerTraceCircuitBreakerAutoConfiguration_traceReactiveCircuitBreakerFactoryAspect(context);
    BeanDefinitionRegistrar.of("spring.sleuth.circuitbreaker-org.springframework.cloud.sleuth.autoconfig.instrument.circuitbreaker.SleuthCircuitBreakerProperties", SleuthCircuitBreakerProperties.class)
        .instanceSupplier(SleuthCircuitBreakerProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.instrument.kafka.TracingKafkaAutoConfiguration", TracingKafkaAutoConfiguration.class)
        .instanceSupplier(TracingKafkaAutoConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.kafka.ContextBootstrapInitializer.registerTracingKafkaAutoConfiguration_tracingKafkaPropagationSetter(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.kafka.ContextBootstrapInitializer.registerTracingKafkaAutoConfiguration_tracingKafkaPropagationGetter(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.kafka.ContextBootstrapInitializer.registerTracingKafkaAutoConfiguration_tracingKafkaProducerBeanPostProcessor(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.kafka.ContextBootstrapInitializer.registerTracingKafkaAutoConfiguration_tracingKafkaConsumerBeanPostProcessor(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.messaging.ContextBootstrapInitializer.registerTraceFunctionStreamConfiguration_KafkaOnlyStreamConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.messaging.ContextBootstrapInitializer.registerKafkaOnlyStreamConfiguration_traceKafkaFunctionMessageSpanCustomizer(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.messaging.ContextBootstrapInitializer.registerTraceFunctionAutoConfiguration_TraceFunctionStreamConfiguration(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.instrument.messaging.TraceFunctionAutoConfiguration", TraceFunctionAutoConfiguration.class)
        .instanceSupplier(TraceFunctionAutoConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.messaging.ContextBootstrapInitializer.registerTraceFunctionAutoConfiguration_traceFunctionAroundWrapper(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.messaging.ContextBootstrapInitializer.registerTraceSpringMessagingAutoConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.messaging.ContextBootstrapInitializer.registerTraceSpringMessagingAutoConfiguration_traceMessagingAspect(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.messaging.ContextBootstrapInitializer.registerTraceSpringMessagingAutoConfiguration_traceMessagePropagationSetter(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.messaging.ContextBootstrapInitializer.registerTraceSpringMessagingAutoConfiguration_traceMessagePropagationGetter(context);
    BeanDefinitionRegistrar.of("spring.sleuth.integration-org.springframework.cloud.sleuth.autoconfig.instrument.messaging.SleuthIntegrationMessagingProperties", SleuthIntegrationMessagingProperties.class)
        .instanceSupplier(SleuthIntegrationMessagingProperties::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerSkipPatternConfiguration_DefaultSkipPatternConfig(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerDefaultSkipPatternConfig_defaultSkipPatternBean(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerSkipPatternConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerSkipPatternConfiguration_sleuthSkipPatternProvider(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerTraceWebServletConfiguration_TraceTomcatConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerTraceTomcatConfiguration_traceTomcatWebServerFactoryCustomizer(context);
    org.springframework.cloud.sleuth.instrument.web.mvc.ContextBootstrapInitializer.registerSpanCustomizingAsyncHandlerInterceptor(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerTraceWebMvcConfigurer(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerTraceWebServletConfiguration_TraceWebMvcAutoConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerTraceWebServletConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerTraceWebServletConfiguration_traceWebAspect(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.web.ContextBootstrapInitializer.registerTraceWebServletConfiguration_traceWebFilter(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.instrument.web.TraceWebAutoConfiguration", TraceWebAutoConfiguration.class)
        .instanceSupplier(TraceWebAutoConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.reactor.ContextBootstrapInitializer.registerTraceReactorConfiguration_HooksRefresherConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.reactor.ContextBootstrapInitializer.registerHooksRefresherConfiguration_hooksRefresher(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.reactor.ContextBootstrapInitializer.registerTraceReactorAutoConfiguration_TraceReactorConfiguration(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.reactor.ContextBootstrapInitializer.registerTraceReactorConfiguration_traceHookRegisteringBeanFactoryPostProcessor(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.instrument.reactor.TraceReactorAutoConfiguration", TraceReactorAutoConfiguration.class)
        .instanceSupplier(TraceReactorAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("spring.sleuth.reactor-org.springframework.cloud.sleuth.autoconfig.instrument.reactor.SleuthReactorProperties", SleuthReactorProperties.class)
        .instanceSupplier(SleuthReactorProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.instrument.scheduling.TraceSchedulingAutoConfiguration", TraceSchedulingAutoConfiguration.class)
        .instanceSupplier(TraceSchedulingAutoConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.scheduling.ContextBootstrapInitializer.registerTraceSchedulingAutoConfiguration_traceSchedulingAspect(context);
    BeanDefinitionRegistrar.of("spring.sleuth.scheduled-org.springframework.cloud.sleuth.autoconfig.instrument.scheduling.SleuthSchedulingProperties", SleuthSchedulingProperties.class)
        .instanceSupplier(SleuthSchedulingProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.instrument.tx.TraceTxAutoConfiguration", TraceTxAutoConfiguration.class)
        .instanceSupplier(TraceTxAutoConfiguration::new).register(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.tx.ContextBootstrapInitializer.registerTraceTxAutoConfiguration_tracePlatformTransactionManagerBeanPostProcessor(context);
    org.springframework.cloud.sleuth.autoconfig.instrument.tx.ContextBootstrapInitializer.registerTraceTxAutoConfiguration_traceReactiveTransactionManagerBeanPostProcessor(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.sleuth.autoconfig.instrument.web.client.TraceWebAsyncClientAutoConfiguration", TraceWebAsyncClientAutoConfiguration.class)
        .instanceSupplier(TraceWebAsyncClientAutoConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.stream.binder.kafka.config.ExtendedBindingHandlerMappingsProviderConfiguration", ExtendedBindingHandlerMappingsProviderConfiguration.class)
        .instanceSupplier(ExtendedBindingHandlerMappingsProviderConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("kafkaExtendedPropertiesDefaultMappingsProvider", BindingHandlerAdvise.MappingsProvider.class).withFactoryMethod(ExtendedBindingHandlerMappingsProviderConfiguration.class, "kafkaExtendedPropertiesDefaultMappingsProvider")
        .instanceSupplier(() -> context.getBean(ExtendedBindingHandlerMappingsProviderConfiguration.class).kafkaExtendedPropertiesDefaultMappingsProvider()).register(context);
    org.springframework.cloud.stream.config.ContextBootstrapInitializer.registerContentTypeConfiguration(context);
    org.springframework.cloud.stream.config.ContextBootstrapInitializer.registerContentTypeConfiguration_integrationArgumentResolverMessageConverter(context);
    org.springframework.cloud.stream.config.ContextBootstrapInitializer.registerBinderFactoryAutoConfiguration(context);
    BeanDefinitionRegistrar.of("integrationMessageHandlerMethodFactory", MessageHandlerMethodFactory.class).withFactoryMethod(BinderFactoryAutoConfiguration.class, "messageHandlerMethodFactory", CompositeMessageConverter.class, org.springframework.validation.Validator.class, ConfigurableListableBeanFactory.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> BinderFactoryAutoConfiguration.messageHandlerMethodFactory(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    BeanDefinitionRegistrar.of("binderTypeRegistry", BinderTypeRegistry.class).withFactoryMethod(BinderFactoryAutoConfiguration.class, "binderTypeRegistry", ConfigurableApplicationContext.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BinderFactoryAutoConfiguration.class).binderTypeRegistry(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("messageConverterConfigurer", MessageConverterConfigurer.class).withFactoryMethod(BinderFactoryAutoConfiguration.class, "messageConverterConfigurer", BindingServiceProperties.class, CompositeMessageConverter.class, StreamFunctionProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BinderFactoryAutoConfiguration.class).messageConverterConfigurer(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    org.springframework.cloud.stream.binding.ContextBootstrapInitializer.registerBinderFactoryAutoConfiguration_channelFactory(context);
    BeanDefinitionRegistrar.of("messageSourceFactory", MessageSourceBindingTargetFactory.class).withFactoryMethod(BinderFactoryAutoConfiguration.class, "messageSourceFactory", CompositeMessageConverter.class, CompositeMessageChannelConfigurer.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BinderFactoryAutoConfiguration.class).messageSourceFactory(attributes.get(0), attributes.get(1)))).register(context);
    BeanDefinitionRegistrar.of("compositeMessageChannelConfigurer", CompositeMessageChannelConfigurer.class).withFactoryMethod(BinderFactoryAutoConfiguration.class, "compositeMessageChannelConfigurer", MessageConverterConfigurer.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BinderFactoryAutoConfiguration.class).compositeMessageChannelConfigurer(attributes.get(0)))).register(context);
    org.springframework.cloud.stream.config.ContextBootstrapInitializer.registerBindingServiceProperties(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.stream.function.FunctionConfiguration", FunctionConfiguration.class)
        .instanceSupplier(FunctionConfiguration::new).register(context);
    BeanDefinitionRegistrar.of("streamBridgeUtils", StreamBridge.class).withFactoryMethod(FunctionConfiguration.class, "streamBridgeUtils", FunctionCatalog.class, FunctionRegistry.class, BindingServiceProperties.class, ConfigurableApplicationContext.class, BinderAwareChannelResolver.NewDestinationBindingCallback.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(FunctionConfiguration.class).streamBridgeUtils(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4)))).register(context);
    BeanDefinitionRegistrar.of("functionBindingRegistrar", InitializingBean.class).withFactoryMethod(FunctionConfiguration.class, "functionBindingRegistrar", Environment.class, FunctionCatalog.class, StreamFunctionProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(FunctionConfiguration.class).functionBindingRegistrar(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    BeanDefinitionRegistrar.of("po", BeanFactoryPostProcessor.class).withFactoryMethod(FunctionConfiguration.class, "po", Environment.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(FunctionConfiguration.class).po(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("functionInitializer", InitializingBean.class).withFactoryMethod(FunctionConfiguration.class, "functionInitializer", FunctionCatalog.class, StreamFunctionProperties.class, BindingServiceProperties.class, ConfigurableApplicationContext.class, StreamBridge.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(FunctionConfiguration.class).functionInitializer(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3), attributes.get(4)))).register(context);
    org.springframework.cloud.stream.function.ContextBootstrapInitializer.registerFunctionConfiguration_supplierInitializer(context);
    BeanDefinitionRegistrar.of("spring.cloud.stream.function-org.springframework.cloud.stream.function.StreamFunctionProperties", StreamFunctionProperties.class)
        .instanceSupplier(StreamFunctionProperties::new).register(context);
    BeanDefinitionRegistrar.of("org.springframework.cloud.stream.config.SpelExpressionConverterConfiguration", SpelExpressionConverterConfiguration.class)
        .instanceSupplier(SpelExpressionConverterConfiguration::new).customize((bd) -> bd.setRole(2)).register(context);
    BeanDefinitionRegistrar.of("spelPropertyAccessorRegistrar", SpelPropertyAccessorRegistrar.class).withFactoryMethod(SpelExpressionConverterConfiguration.class, "spelPropertyAccessorRegistrar")
        .instanceSupplier(() -> SpelExpressionConverterConfiguration.spelPropertyAccessorRegistrar()).register(context);
    BeanDefinitionRegistrar.of("spelConverter", ResolvableType.forClassWithGenerics(Converter.class, String.class, Expression.class)).withFactoryMethod(SpelExpressionConverterConfiguration.class, "spelConverter")
        .instanceSupplier(() -> context.getBean(SpelExpressionConverterConfiguration.class).spelConverter()).register(context);
    org.springframework.cloud.stream.config.ContextBootstrapInitializer.registerBindingServiceConfiguration(context);
    BeanDefinitionRegistrar.of("globalErrorChannelCustomizer", BeanPostProcessor.class).withFactoryMethod(BindingServiceConfiguration.class, "globalErrorChannelCustomizer")
        .instanceSupplier(() -> context.getBean(BindingServiceConfiguration.class).globalErrorChannelCustomizer()).register(context);
    BeanDefinitionRegistrar.of("streamListenerAnnotationBeanPostProcessor", StreamListenerAnnotationBeanPostProcessor.class).withFactoryMethod(BindingServiceConfiguration.class, "streamListenerAnnotationBeanPostProcessor")
        .instanceSupplier(() -> BindingServiceConfiguration.streamListenerAnnotationBeanPostProcessor()).register(context);
    BeanDefinitionRegistrar.of("BindingHandlerAdvise", BindingHandlerAdvise.class).withFactoryMethod(BindingServiceConfiguration.class, "BindingHandlerAdvise", BindingHandlerAdvise.MappingsProvider[].class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BindingServiceConfiguration.class).BindingHandlerAdvise(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("binderFactory", BinderFactory.class).withFactoryMethod(BindingServiceConfiguration.class, "binderFactory", BinderTypeRegistry.class, BindingServiceProperties.class, ObjectProvider.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BindingServiceConfiguration.class).binderFactory(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    BeanDefinitionRegistrar.of("messageChannelStreamListenerResultAdapter", MessageChannelStreamListenerResultAdapter.class).withFactoryMethod(BindingServiceConfiguration.class, "messageChannelStreamListenerResultAdapter")
        .instanceSupplier(() -> context.getBean(BindingServiceConfiguration.class).messageChannelStreamListenerResultAdapter()).register(context);
    BeanDefinitionRegistrar.of("bindingService", BindingService.class).withFactoryMethod(BindingServiceConfiguration.class, "bindingService", BindingServiceProperties.class, BinderFactory.class, TaskScheduler.class, ObjectMapper.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BindingServiceConfiguration.class).bindingService(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3)))).register(context);
    org.springframework.cloud.stream.binding.ContextBootstrapInitializer.registerBindingServiceConfiguration_outputBindingLifecycle(context);
    org.springframework.cloud.stream.binding.ContextBootstrapInitializer.registerBindingServiceConfiguration_inputBindingLifecycle(context);
    BeanDefinitionRegistrar.of("bindingsLifecycleController", BindingsLifecycleController.class).withFactoryMethod(BindingServiceConfiguration.class, "bindingsLifecycleController", List.class, List.class, ObjectMapper.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BindingServiceConfiguration.class).bindingsLifecycleController(attributes.get(0), attributes.get(1), attributes.get(2)))).register(context);
    BeanDefinitionRegistrar.of("contextStartAfterRefreshListener", ContextStartAfterRefreshListener.class).withFactoryMethod(BindingServiceConfiguration.class, "contextStartAfterRefreshListener")
        .instanceSupplier(() -> context.getBean(BindingServiceConfiguration.class).contextStartAfterRefreshListener()).register(context);
    BeanDefinitionRegistrar.of("binderAwareChannelResolver", BinderAwareChannelResolver.class).withFactoryMethod(BindingServiceConfiguration.class, "binderAwareChannelResolver", BindingService.class, AbstractBindingTargetFactory.class, DynamicDestinationsBindable.class, BinderAwareChannelResolver.NewDestinationBindingCallback.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BindingServiceConfiguration.class).binderAwareChannelResolver(attributes.get(0), attributes.get(1), attributes.get(2), attributes.get(3)))).register(context);
    BeanDefinitionRegistrar.of("dynamicDestinationsBindable", DynamicDestinationsBindable.class).withFactoryMethod(BindingServiceConfiguration.class, "dynamicDestinationsBindable")
        .instanceSupplier(() -> context.getBean(BindingServiceConfiguration.class).dynamicDestinationsBindable()).register(context);
    BeanDefinitionRegistrar.of("binderAwareRouterBeanPostProcessor", BinderAwareRouter.class).withFactoryMethod(BindingServiceConfiguration.class, "binderAwareRouterBeanPostProcessor", List.class, DestinationResolver.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BindingServiceConfiguration.class).binderAwareRouterBeanPostProcessor(attributes.get(0), attributes.get(1)))).register(context);
    BeanDefinitionRegistrar.of("appListener", ResolvableType.forClassWithGenerics(ApplicationListener.class, ContextRefreshedEvent.class)).withFactoryMethod(BindingServiceConfiguration.class, "appListener", SpringIntegrationProperties.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> context.getBean(BindingServiceConfiguration.class).appListener(attributes.get(0)))).register(context);
    BeanDefinitionRegistrar.of("spring.cloud.stream.integration-org.springframework.cloud.stream.config.SpringIntegrationProperties", SpringIntegrationProperties.class)
        .instanceSupplier(SpringIntegrationProperties::new).register(context);
    BeanDefinitionRegistrar.of("integrationChannelResolver", BeanFactoryChannelResolver.class)
        .instanceSupplier(() -> new BeanFactoryChannelResolver()).register(context);
    BeanDefinitionRegistrar.of("integrationMessagePublishingErrorHandler", MessagePublishingErrorHandler.class)
        .instanceSupplier(() -> new MessagePublishingErrorHandler()).register(context);
    BeanDefinitionRegistrar.of("nullChannel", NullChannel.class)
        .instanceSupplier(NullChannel::new).register(context);
    BeanDefinitionRegistrar.of("errorChannel", PublishSubscribeChannel.class).withConstructor(boolean.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> new PublishSubscribeChannel(attributes.get(0, boolean.class)))).customize((bd) -> {
      bd.getConstructorArgumentValues().addIndexedArgumentValue(0, "#{T(org.springframework.integration.context.IntegrationContextUtils).getIntegrationProperties(beanFactory).getProperty('spring.integration.channels.error.requireSubscribers')}");
      bd.getPropertyValues().addPropertyValue("ignoreFailures", "#{T(org.springframework.integration.context.IntegrationContextUtils).getIntegrationProperties(beanFactory).getProperty('spring.integration.channels.error.ignoreFailures')}");
    }).register(context);
    BeanDefinitionRegistrar.of("_org.springframework.integration.errorLogger.handler", LoggingHandler.class).withConstructor(LoggingHandler.Level.class)
        .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> new LoggingHandler(attributes.get(0, LoggingHandler.Level.class)))).customize((bd) -> bd.getConstructorArgumentValues().addIndexedArgumentValue(0, LoggingHandler.Level.ERROR)).register(context);
    BeanDefinitionRegistrar.of("_org.springframework.integration.errorLogger", ConsumerEndpointFactoryBean.class)
        .instanceSupplier(ConsumerEndpointFactoryBean::new).customize((bd) -> {
      MutablePropertyValues propertyValues = bd.getPropertyValues();
      propertyValues.addPropertyValue("inputChannelName", "errorChannel");
      propertyValues.addPropertyValue("handler", new RuntimeBeanReference("_org.springframework.integration.errorLogger.handler"));
    }).register(context);
    BeanDefinitionRegistrar.of("integrationEvaluationContext", IntegrationEvaluationContextFactoryBean.class)
        .instanceSupplier(IntegrationEvaluationContextFactoryBean::new).customize((bd) -> bd.setRole(2)).register(context);
    BeanDefinitionRegistrar.of("integrationSimpleEvaluationContext", IntegrationSimpleEvaluationContextFactoryBean.class)
        .instanceSupplier(IntegrationSimpleEvaluationContextFactoryBean::new).customize((bd) -> bd.setRole(2)).register(context);
    BeanDefinitionRegistrar.of("org.springframework.integration.config.IdGeneratorConfigurer#0", IdGeneratorConfigurer.class)
        .instanceSupplier(IdGeneratorConfigurer::new).customize((bd) -> bd.setRole(2)).register(context);
    BeanDefinitionRegistrar.of("jsonNodeWrapperToJsonNodeConverter", JsonNodeWrapperToJsonNodeConverter.class)
        .instanceSupplier(JsonNodeWrapperToJsonNodeConverter::new).register(context);
    BeanDefinitionRegistrar.of("integrationLifecycleRoleController", SmartLifecycleRoleController.class)
        .instanceSupplier(() -> new SmartLifecycleRoleController()).register(context);
    BeanDefinitionRegistrar.of("messageBuilderFactory", DefaultMessageBuilderFactory.class)
        .instanceSupplier(DefaultMessageBuilderFactory::new).customize((bd) -> bd.getPropertyValues().addPropertyValue("readOnlyHeaders", "#{T(org.springframework.integration.context.IntegrationContextUtils).getIntegrationProperties(beanFactory).getProperty('spring.integration.readOnly.headers')}")).register(context);
    BeanDefinitionRegistrar.of("integrationHeaderChannelRegistry", DefaultHeaderChannelRegistry.class)
        .instanceSupplier(() -> new DefaultHeaderChannelRegistry()).register(context);
    BeanDefinitionRegistrar.of("globalChannelInterceptorProcessor", GlobalChannelInterceptorProcessor.class)
        .instanceSupplier(GlobalChannelInterceptorProcessor::new).customize((bd) -> bd.setRole(2)).register(context);
    BeanDefinitionRegistrar.of("datatypeChannelMessageConverter", DefaultDatatypeChannelMessageConverter.class)
        .instanceSupplier(DefaultDatatypeChannelMessageConverter::new).register(context);
    BeanDefinitionRegistrar.of("integrationListMessageHandlerMethodFactory", DefaultMessageHandlerMethodFactory.class)
        .instanceSupplier(DefaultMessageHandlerMethodFactory::new).customize((bd) -> {
      MutablePropertyValues propertyValues = bd.getPropertyValues();
      propertyValues.addPropertyValue("messageConverter", new RuntimeBeanReference("integrationArgumentResolverMessageConverter"));
      propertyValues.addPropertyValue("customArgumentResolvers", List.of(BeanDefinitionRegistrar.inner(PayloadExpressionArgumentResolver.class)
          .instanceSupplier(PayloadExpressionArgumentResolver::new).toBeanDefinition(), BeanDefinitionRegistrar.inner(NullAwarePayloadArgumentResolver.class).withConstructor(MessageConverter.class)
          .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> new NullAwarePayloadArgumentResolver(attributes.get(0)))).customize((bd_) -> bd_.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference("integrationArgumentResolverMessageConverter"))).toBeanDefinition(), BeanDefinitionRegistrar.inner(PayloadsArgumentResolver.class)
          .instanceSupplier(PayloadsArgumentResolver::new).toBeanDefinition(), BeanDefinitionRegistrar.inner(CollectionArgumentResolver.class).withConstructor(boolean.class)
          .instanceSupplier((instanceContext) -> instanceContext.create(context, (attributes) -> new CollectionArgumentResolver(attributes.get(0)))).customize((bd_) -> bd_.getConstructorArgumentValues().addIndexedArgumentValue(0, true)).toBeanDefinition(), BeanDefinitionRegistrar.inner(MapArgumentResolver.class)
          .instanceSupplier(MapArgumentResolver::new).toBeanDefinition()));
    }).register(context);
    org.springframework.integration.config.ContextBootstrapInitializer.registerConverterRegistrar(context);
    org.springframework.integration.config.ContextBootstrapInitializer.registerIntegrationConversionService(context);
    BeanDefinitionRegistrar.of("org.springframework.integration.dsl.context.IntegrationFlowBeanPostProcessor", IntegrationFlowBeanPostProcessor.class)
        .instanceSupplier(IntegrationFlowBeanPostProcessor::new).register(context);
    org.springframework.integration.dsl.context.ContextBootstrapInitializer.registerStandardIntegrationFlowContext(context);
    BeanDefinitionRegistrar.of("org.springframework.integration.dsl.BaseIntegrationFlowDefinition$ReplyProducerCleaner", BaseIntegrationFlowDefinition.ReplyProducerCleaner.class)
        .instanceSupplier(BaseIntegrationFlowDefinition.ReplyProducerCleaner::new).register(context);
  }
}

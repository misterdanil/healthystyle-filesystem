package org.healthystyle.filesystem.service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class RabbitConfig {
	public static final String FILESYSTEM_DIRECT_EXCHANGE = "filesystem-direct-exchange";
	public static final String FILE_UPLOAD_QUEUE = "file-upload-queue";
	public static final String FILE_UPLOAD_ROUTING_KEY = "file.upload";

	@Bean
	public Jackson2JsonMessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public DirectExchange fileSystemDirectExchange() {
		return new DirectExchange(FILESYSTEM_DIRECT_EXCHANGE);
	}

	@Bean
	public Queue fileUploadQueue() {
		return new Queue(FILE_UPLOAD_QUEUE, true, false, false);
	}

	@Bean
	public Binding fileUploadBinding() {
		return BindingBuilder.bind(fileUploadQueue()).to(fileSystemDirectExchange()).with(FILE_UPLOAD_ROUTING_KEY);
	}
}

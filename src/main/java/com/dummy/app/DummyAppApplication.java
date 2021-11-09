package com.dummy.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@NativeHint(trigger = BraveKafkaStreamsAutoConfiguration.class, types = @TypeHint(typeNames = {"brave.kafka.clients.TracingProducer", "brave.kafka.clients.TracingConsumer"}))
@SpringBootApplication
public class DummyAppApplication {

	public static void main(String... args) {
		SpringApplication.run(DummyAppApplication.class, args);
	}
}

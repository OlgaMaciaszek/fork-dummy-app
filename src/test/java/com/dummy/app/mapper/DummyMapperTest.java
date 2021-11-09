//package com.ferratum.ods.cuba2.mapper;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.Collections;
//import java.util.Map;
//
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.common.serialization.Deserializer;
//import org.apache.kafka.common.serialization.Serdes;
//import org.apache.kafka.common.serialization.Serializer;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//import org.springframework.kafka.test.EmbeddedKafkaBroker;
//import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.kafka.test.utils.KafkaTestUtils;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import com.dummy.app.api.InputClass;
//import com.dummy.app.api.OutputClass;
//import com.dummy.app.mapper.DummyMapper;
//
///**
// * Simple test class for {@link DummyMapper}.
// */
//@DirtiesContext
//@EnableAutoConfiguration
//@ExtendWith(SpringExtension.class)
//@EmbeddedKafka(
//        partitions = 1,
//        controlledShutdown = true,
//        topics = {
//                DummyMapperTest.TOPIC_INPUT,
//                DummyMapperTest.TOPIC_OUTPUT})
//@SpringBootTest(webEnvironment = WebEnvironment.NONE,
//        properties = {
//                "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
//                "spring.cloud.stream.kafka.binder.brokers=${spring.embedded.kafka.brokers}",
//                "spring.cloud.stream.bindings.dummyMapper-in-0.destination=" + DummyMapperTest.TOPIC_INPUT,
//                "spring.cloud.stream.bindings.dummyMapper-out-0.destination=" + DummyMapperTest.TOPIC_OUTPUT
//        }, classes = DummyMapper.class
//)
//class DummyMapperTest {
//
//    public static final String TOPIC_INPUT = "Dummy_Topic_Input";
//    public static final String TOPIC_OUTPUT = "Dummy_Topic_Output";
//
//    @Autowired
//    private EmbeddedKafkaBroker embeddedKafkaBroker;
//
//    @Test
//    void processInsert()  {
//        String key = "key";
//        InputClass inputClass = new InputClass();
//
//        Serializer<InputClass> inputSerializer = new JsonSerializer<>();
//        KafkaTemplate<String, InputClass> inputTemplate =
//                createKafkaTemplate(inputSerializer, TOPIC_INPUT, embeddedKafkaBroker);
//
//        Deserializer<OutputClass> outputDeserializer = new JsonDeserializer<>(OutputClass.class);
//        Consumer<String, OutputClass> consumer = createConsumer(outputDeserializer, TOPIC_OUTPUT, embeddedKafkaBroker);
//
//        //TODO: there is the error thrown: Caused by: java.lang.NullPointerException: null
//        inputTemplate.sendDefault("", inputClass);
//        assertThat(KafkaTestUtils.getRecords(consumer, 5000).isEmpty()).isTrue();
//
//        //HAPPY PATH
//        inputTemplate.sendDefault(key, inputClass);
//        ConsumerRecord<String, OutputClass> cr = KafkaTestUtils.getSingleRecord(consumer, TOPIC_OUTPUT, 5000);
//        assertThat(cr.key()).isEqualTo(key);
//    }
//
//    private static <T> Consumer<String, T> createConsumer(
//            Deserializer<T> deserializer, String topic, EmbeddedKafkaBroker embeddedKafkaBroker) {
//        Map<String, Object> consumerProps =
//                KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafkaBroker);
//        Consumer<String, T> consumer = (new DefaultKafkaConsumerFactory(
//                consumerProps, Serdes.String().deserializer(), deserializer)).createConsumer();
//        consumer.subscribe(Collections.singleton(topic));
//        return consumer;
//    }
//
//    private static <T> KafkaTemplate<String, T> createKafkaTemplate(
//            Serializer<T> serializer, String topic, EmbeddedKafkaBroker embeddedKafkaBroker) {
//        Map<String, Object> senderProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
//        DefaultKafkaProducerFactory<String, T> pf = new DefaultKafkaProducerFactory(
//                senderProps, Serdes.String().serializer(), serializer);
//        KafkaTemplate<String, T> template = new KafkaTemplate(pf, true);
//        template.setDefaultTopic(topic);
//        return template;
//    }
//}

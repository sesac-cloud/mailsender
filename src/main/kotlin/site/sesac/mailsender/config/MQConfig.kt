package site.sesac.mailsender.config

import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MQConfig {

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jsonMessageConverter()
        return rabbitTemplate
    }

    @Bean
    fun jsonMessageConverter(): Jackson2JsonMessageConverter {
        return Jackson2JsonMessageConverter()
    }

    @Bean
    fun prefetchOneContainerFactory(
        configurer: SimpleRabbitListenerContainerFactoryConfigurer, connectionFactory: ConnectionFactory?
    ): RabbitListenerContainerFactory<SimpleMessageListenerContainer> {
        val factory = SimpleRabbitListenerContainerFactory()
        configurer.configure(factory, connectionFactory)
        factory.setPrefetchCount(1)
        return factory
    }



    @Bean
    fun deadLetterExchange(): FanoutExchange {
        return FanoutExchange("MAIL-DLX")
    }

    @Bean
    fun deadLetterQueue(): Queue {
        return QueueBuilder.durable("MAIL-DLQ").build()
    }

    @Bean
    fun deadLetterBinding(): Binding {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange())
    }

    @Bean
    fun messagesQueue(): Queue {
        return QueueBuilder.durable("mail")
            .withArgument("x-dead-letter-exchange", "MAIL-DLX")
            .build()
    }

}
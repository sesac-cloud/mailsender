package site.sesac.mailsender.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import site.sesac.mailsender.data.MailMessage

private val logger = KotlinLogging.logger {}
@Component
class MessageListener (private val mailSender: SendMail)
{
    @RabbitListener(queues = ["mail"], containerFactory = "prefetchOneContainerFactory" )
    fun receiveMessage(message: org.springframework.amqp.core.Message) = try {
        val messageBody = String(message.body).substring(0, String(message.body).length-1 ).replace("""\""","").substring(1)

        val objectMapper = jacksonObjectMapper()
        val mailMessage: MailMessage = objectMapper.readValue(messageBody, MailMessage::class.java)
        logger.info { "${mailMessage.userMail} : Request Get Message" }

        if(mailMessage.mailType == "S"){
            mailSender.atSuccess(mailMessage.userMail)
        }
        else if(mailMessage.mailType == "F"){
            mailSender.atFail(mailMessage.userMail)
        }

        logger.info { "${mailMessage.userMail} : Process Done" }

    }catch (e : Exception){
        logger.error { "Process error" }
    }

//    @RabbitListener(queues = ["MAIL-DLQ"], containerFactory = "prefetchOneContainerFactory")
//    fun dlqListener(message: org.springframework.amqp.core.Message) {
//        val messageBody = String(message.body).substring(0, String(message.body).length-1 ).replace("""\""","").substring(1)
//
//        val objectMapper = jacksonObjectMapper()
//        val mailMessage: MailMessage = objectMapper.readValue(messageBody, MailMessage::class.java)
//        logger.info { "${mailMessage.userMail} : DLQ Request Get Message" }
//
//  }


}

package site.sesac.mailsender.service

import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class SendMail(
    @Autowired
    private val javaMailSender: JavaMailSender,
) {
    @Value("\${etc.servicename}")
    private lateinit var serviceName: String

    fun atSuccess(userMail : String ) {
        val mimeMessage: MimeMessage = javaMailSender.createMimeMessage()
        return try {

            val mimeMessageHelper = MimeMessageHelper(mimeMessage, false, "UTF-8")

            mimeMessageHelper.setTo(userMail) // 메일 수신자
            mimeMessageHelper.setSubject("[${serviceName}]사진이 완성되었어요!") // 메일 제목
            mimeMessageHelper.setText("${serviceName}에서 요청하신 사진이 완성되었어요! 지금 접속하셔서 확인해보세요!") // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage)
     //       logger.info("Success Mail Sending Success to ${userMail}")
        } catch (e: MessagingException) {
     //       logger.error("Success Mail Sending fail ${userMail}")
            throw RuntimeException(e)
        }

    }
    fun atFail(userMail : String ) {
        val mimeMessage: MimeMessage = javaMailSender.createMimeMessage()

        return try {
  val mimeMessageHelper = MimeMessageHelper(mimeMessage, false, "UTF-8")

            mimeMessageHelper.setTo(userMail) // 메일 수신자
            mimeMessageHelper.setSubject("[${serviceName}]사진 생성에 문제가 생겼어요.") // 메일 제목
            mimeMessageHelper.setText("${serviceName}에서 요청하신 사진을 생성하던 중에 문제가생겼어요. 다시 신청해주세요.") // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage)
      //      logger.info("Fail Mail Sending Success to ${userMail}")
        } catch (e: MessagingException) {
      //      logger.error("Fail Mail Sending fail ${userMail}")
            throw RuntimeException(e)
        }

    }

    fun atTokenError(userMail : String ) {
        val mimeMessage: MimeMessage = javaMailSender.createMimeMessage()

        return try {
            val mimeMessageHelper = MimeMessageHelper(mimeMessage, false, "UTF-8")

            mimeMessageHelper.setTo(userMail) // 메일 수신자
            mimeMessageHelper.setSubject("토큰교체필요") // 메일 제목
            mimeMessageHelper.setText("api 토큰교체 필요") // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage)
            //      logger.info("Fail Mail Sending Success to ${userMail}")
        } catch (e: MessagingException) {
            //      logger.error("Fail Mail Sending fail ${userMail}")
            throw RuntimeException(e)
        }

    }

}
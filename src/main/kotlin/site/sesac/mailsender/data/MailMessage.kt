package site.sesac.mailsender.data
import com.fasterxml.jackson.annotation.JsonProperty
data class MailMessage (
    @JsonProperty("mail_type")
    var mailType: String ,
    @JsonProperty("user_mail")
    var userMail : String

)

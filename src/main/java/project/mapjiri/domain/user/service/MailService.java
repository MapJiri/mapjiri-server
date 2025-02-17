package project.mapjiri.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import project.mapjiri.domain.user.dto.request.MailSendRequestDto;
import project.mapjiri.domain.user.dto.request.MailVerifyRequestDto;
import project.mapjiri.global.exception.MyErrorCode;
import project.mapjiri.global.exception.MyException;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSenderImpl mailSender;
    private final RedisService redisService;


    public void sendMail(MailSendRequestDto request) {
        String mail = request.getMail();

        SimpleMailMessage message = new SimpleMailMessage();
        Random random = new Random();
        String code = String.valueOf(random.nextInt(900000) + 100000);

        message.setTo(mail);
        message.setSubject("Mapjiri 인증 메일입니다.");
        message.setText("인증 코드는 " + code + "입니다.");
        mailSender.send(message);

        redisService.setCode(mail, code);
    }

    public boolean verifyCode(MailVerifyRequestDto request) {
        String mail = request.getMail();
        String code = request.getCode();

        String savedCode = redisService.getCode(mail);

        if (savedCode == null) {
            throw new MyException(MyErrorCode.INVALID_VERIFICATION_CODE);
        } else if (!savedCode.equals(code)) {
            throw new MyException(MyErrorCode.EXPIRED_OR_NOT_FOUND_VERIFICATION_CODE);
        }

        redisService.deleteCode(mail);
        redisService.setVerifiedMail(mail);

        return true;
    }
}

package com.max.restaurant.controller.command;

import com.max.restaurant.exceptions.CommandException;
import com.max.restaurant.exceptions.DAOException;
import com.max.restaurant.model.entity.User;
import com.max.restaurant.model.services.UserService;
import com.max.restaurant.utils.UtilsEmailNotificator;
import com.max.restaurant.utils.UtilsPasswordEncryption;
import com.max.restaurant.utils.UtilsReCaptchaVerifier;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

import static com.max.restaurant.utils.UtilsCommandNames.*;
import static com.max.restaurant.utils.UtilsEntityFields.USER_EMAIL;
import static com.max.restaurant.utils.UtilsExceptionMsgs.ENCRYPTION_EXC;
import static com.max.restaurant.utils.UtilsExceptionMsgs.FRONT_VALIDATION_EXC;
import static com.max.restaurant.utils.UtilsFileNames.LOGIN_PAGE;
import static com.max.restaurant.utils.UtilsFileNames.SIGN_UP_PAGE;
import static com.max.restaurant.utils.UtilsLoggerMsgs.*;
import static com.max.restaurant.utils.UtilsReCaptchaVerifier.reCAPTCHA_ATTR;

/**
 * Command to change password of user for a new one.<br>
 * The params of request, to call this command: action=lang
 */
public class ForgotPassCommand implements Command {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPassCommand.class);

    @Override
    public void executeGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.info(METHOD_STARTS_MSG, "executePost", "true");
    }

    @Override
    public void executePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, DAOException {
        LOGGER.error(METHOD_STARTS_MSG, "executePost", "true");
        String email = request.getParameter(USER_EMAIL);
        HttpSession session = request.getSession();
        String page;
        String notValid = preValidateUser(request);

        if (email == null) {
            String code = Optional.ofNullable(request.getParameter(EMAIL_CODE_ATTR)).orElse("");
            String codeOrigin = ((Integer) session.getAttribute(EMAIL_CODE_ATTR)).toString();
            email = (String) session.getAttribute(USER_EMAIL);
            if (code.equals(codeOrigin)) {
                try {
                    char[] newPass = request.getParameter(PASS_RECOVERY).toCharArray();
                    updatePassword(newPass, email);
                    session.setAttribute(UNSUCCESS_ATTR, SUCCESS_MSG);
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    LOGGER.error(METHOD_FAILED, "getNewEncryptedPass", e);
                    throw new CommandException(ENCRYPTION_EXC, e);
                }
            } else {
                session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG2);
                LOGGER.debug(TWO_PARAMS_MSG, UNSUCCESS_ATTR, UNSUCCESS_MSG);
            }
            page = request.getContextPath() + LOGIN_PAGE;
        } else {
            if (notValid == null) {
                email = email.strip().toLowerCase();
                UserService userService = new UserService();
                User user = userService.findUserByEmail(email);
                if (user != null) {
                    int emailCode = sendSecretPass(email);
                    session.setAttribute(EMAIL_CODE_ATTR, emailCode);
                    session.setAttribute(USER_EMAIL, email);
                    page = request.getContextPath() + LOGIN_PAGE;
                    response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                    return;
                } else {
                    session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG);
                    page = SIGN_UP_PAGE;
                    LOGGER.debug(TWO_PARAMS_MSG, UNSUCCESS_ATTR, UNSUCCESS_MSG);
                }

            } else {
                if (notValid.equals(reCAPTCHA_ATTR)) {
                    session.setAttribute(UNSUCCESS_ATTR, UNSUCCESS_MSG4);
                    LOGGER.debug(TWO_PARAMS_MSG, UNSUCCESS_ATTR, UNSUCCESS_MSG4);
                    page = request.getServletContext().getContextPath() + LOGIN_PAGE;
                } else {
                    LOGGER.error(METHOD_FAILED, "notValid" + notValid);
                    throw new CommandException(FRONT_VALIDATION_EXC);
                }
            }
        }
        LOGGER.debug(REDIRECT, page);
        response.sendRedirect(page);
    }

    private static void updatePassword(char[] newPassword, String email) throws InvalidKeySpecException, NoSuchAlgorithmException, DAOException {
        String newEncryptedPass = UtilsPasswordEncryption
                .getNewEncryptedPass(newPassword);
        UserService userService = new UserService();
        User user = userService.findUserByEmail(email);
        user.setPassword(newEncryptedPass);
        userService.updateUser(user);
        LOGGER.debug(UPDATE_MSG, "successful, user " + user.getEmail());
    }

    private int sendSecretPass(String email) throws CommandException {
        int emailCode = (int) (Math.random() * 100_000) + 100_000;
        String subject = "Password recovery";
        String emailText = "The Restaurant welcomes you!<br>Your secret code for password recovery is:<br><h2>" + emailCode + "</h2>";
        try {
            UtilsEmailNotificator.send(List.of(email), subject, emailText, null, null, null);
        } catch (EmailException | IOException e) {
            LOGGER.error(METHOD_FAILED, "email sending", e);
            throw new CommandException(ENCRYPTION_EXC, e);
        }
        return emailCode;
    }

    private static String preValidateUser(HttpServletRequest request) throws IOException {
        if (request.getParameter(USER_EMAIL) == null || request.getParameter(USER_EMAIL).isBlank()) {
            return USER_EMAIL;
        } else if (!UtilsReCaptchaVerifier.verify(request)) {
            return reCAPTCHA_ATTR;
//            return null;
        }
        return null;
    }
}

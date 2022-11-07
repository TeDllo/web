package ru.itmo.wp.filter;

import ru.itmo.wp.util.ImageUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;

public class CaptchaFilter extends HttpFilter {

    private final String CAPTCHA_IMG_PATH = "/home/tedllo/IdeaProjects/hw3/src/main/webapp/static/img/captcha/";

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = request.getSession();

        boolean needNewCaptcha = false;
        if (!"passed".equals(session.getAttribute("captchaStatus"))) {
            needNewCaptcha = needToRegenerateCaptcha(request);
        }

        if (request.getMethod().equals("GET") && needNewCaptcha) {
            String captchaStatus = (String) session.getAttribute("captchaStatus");

            if (!"passed".equals(captchaStatus)) {
                session.setAttribute("captchaStatus", "notPassed");

                Random random = new Random();
                Integer code = random.nextInt(900) + 100;
                session.setAttribute("answer", code);

                String form = """
                        <img src="data:image/png;base64,%s">
                        <form method="get" action="%s">
                            <input name="captcha">
                        </form>
                        """.formatted(Base64.getEncoder().encodeToString(ImageUtils.toPng(String.valueOf(code))),
                        request.getRequestURL());

                response.setContentType("text/html");
                try (OutputStream outputStream = response.getOutputStream()) {
                    outputStream.write(form.getBytes());
                }

                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean needToRegenerateCaptcha(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (session.getAttribute("captchaStatus") == null) {
            return true;
        }

        String captcha = request.getParameter("captcha");
        String answer = ((Integer) session.getAttribute("answer")).toString();

        boolean accepted = Objects.equals(captcha, answer);
        if (accepted) {
            session.setAttribute("captchaStatus", "passed");
        }

        return !accepted && captcha != null;
    }
}

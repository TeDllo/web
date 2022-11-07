package ru.itmo.wp.servlet;

import com.google.gson.Gson;
import ru.itmo.wp.classes.Message;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


public class StaticServlet extends HttpServlet {
    private final String STATIC_PATH = "/home/tedllo/IdeaProjects/hw3/src/main/webapp/static/";
    private final List<Message> messages = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        String[] filenames = uri.split("\\+");
        filenames[0] = filenames[0].substring(1);

        boolean isMimeSet = false;

        for (String filename : filenames) {
            File file = new File(STATIC_PATH + filename);
            if (!file.isFile()) {
                file = new File(getServletContext().getRealPath("/static/" + filename));
            }

            if (file.isFile()) {

                if (!isMimeSet) {
                    response.setContentType(getServletContext().getMimeType(file.getName()));
                    isMimeSet = true;
                }

                try (OutputStream outputStream = response.getOutputStream()) {
                    Files.copy(file.toPath(), outputStream);
                }

            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (request.getRequestURI()) {
            case "/message/auth" -> handleMessageAuth(request, response);
            case "/message/findAll" -> handleMessageFindAll(request, response);
            case "/message/add" -> handleMessageAdd(request, response);
            default -> {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }
        response.setContentType("application/json");
    }

    private void handleMessageAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = request.getParameter("user");

        if (user != null && !user.trim().isEmpty()) {
            request.getSession().setAttribute("user", user.trim());
        } else {
            user = (String) request.getSession().getAttribute("user");
        }

        String json = new Gson().toJson(user);
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    private void handleMessageFindAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Message> messages = null;

        if (request.getSession().getAttribute("user") != null) {
            messages = this.messages;
        }

        String json = new Gson().toJson(messages);
        response.getWriter().print(json);
        response.getWriter().flush();
    }

    private void handleMessageAdd(HttpServletRequest request, HttpServletResponse response) {
        String user = (String) request.getSession().getAttribute("user");
        String text = request.getParameter("text");
        if (text != null && !text.trim().isEmpty()) {
            messages.add(new Message(user, text));
        }
    }

}

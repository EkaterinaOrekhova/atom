package ru.atom.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import ru.atom.chat.model.User;
import ru.atom.chat.service.ChatService;

@Controller
@RequestMapping("chat")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    File file = new File("chathistory.txt");
    private Deque<String> messages = new ConcurrentLinkedDeque<>();
    private Map<String, String> usersOnline = new ConcurrentHashMap<>();

    @Autowired
    private ChatService chatService;

    /**
     * curl -X POST -i localhost:8080/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name) {
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry");
        }
        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("User already logged in");
        }
        usersOnline.put(name, name);
        messages.add(new SimpleDateFormat("EEE, d MMM HH:mm:ss",
            Locale.ENGLISH).format(new Date()) + " [" + name + "] logged in");

        chatService.login(name);

        return ResponseEntity.ok().build();

    }

    /**
     * curl -i localhost:8080/chat/chat
     */
    @RequestMapping(
            path = "chat",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> chat() {
        String responseBody = String.join("\n", messages);
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -i localhost:8080/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity online() {
        List<User> online = chatService.getOnlineUsers();

        String responseBody = online.stream()
            .map(User::getLogin)
            .collect(Collectors.joining("\n"));
        return ResponseEntity.ok().body(responseBody);

        //String responseBody = String.join("\n", usersOnline.keySet().stream().sorted().collect(Collectors.toList()));
        //return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/chat/logout -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "logout",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity logout(@RequestParam("name") String name) {
        if (usersOnline.containsKey(name)) {
            usersOnline.remove(name, name);
            messages.add(new SimpleDateFormat("EEE, d MMM HH:mm:ss",
                Locale.ENGLISH).format(new Date()) + " [" + name + "] logged out");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("User already logged out");
    }


    /**
     * curl -X POST -i localhost:8080/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @RequestMapping(
            path = "say",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity say(@RequestParam("name") String name, @RequestParam("msg") String msg) {

        String str = "";
        String [] tokens = {""};
        Pattern pattern = Pattern.compile("\\[.+\\]");
        Matcher matcher = pattern.matcher(messages.getLast());
        boolean flag = true;

        if (usersOnline.containsKey(name)) {

            if (messages.size() != 0) {
                while (matcher.find()) {
                    str = messages.getLast().substring(matcher.start() + 1, matcher.end() - 1);
                }

                if (messages.getLast().contains("]:")) {
                    tokens = messages.getLast().split("]: ");
                }

                if (str.equals(name) && tokens[tokens.length - 1].equals(msg)) {
                    flag = false;
                }
            }

            if (flag) {
                messages.add(new SimpleDateFormat("EEE, d MMM HH:mm:ss",
                    Locale.ENGLISH).format(new Date()) + " [" + name + "]: " + msg);
                return ResponseEntity.ok().build();
            } else {
                usersOnline.remove(name, name);
                messages.add(new SimpleDateFormat("EEE, d MMM HH:mm:ss",
                    Locale.ENGLISH).format(new Date()) + " [" + name + "] is spammer! You are logout!");
                return ResponseEntity.badRequest().body("User [" + name + "] is spammer! You are logout!");
            }
        }
        return ResponseEntity.badRequest().body("User doesn't log in");
    }

    /**
     * curl -X DELETE localhost:8080/chat/deleteMsg
     * Очистить все сообщения в чате
     */
    @RequestMapping(
        path = "deleteMsg",
        method = RequestMethod.DELETE,
        produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity deleteMsg() {
        String responseBody;

        if (messages.size() > 0) {
            messages.clear();
            responseBody = String.join("\n", "Chat is clear");
        } else {
            responseBody = String.join("\n", "No messages to delete");
        }
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -i localhost:8080/chat/saveMsg
     */
    @RequestMapping(
        path = "saveMsg",
        method = RequestMethod.GET,
        produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity saveMsg() {
        try (FileWriter writer = new FileWriter("chathistory.txt", false)) {
            for (String str: messages) {
                writer.append(str);
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return ResponseEntity.ok("History was saved!");
    }

    /**
     * curl -i localhost:8080/chat/loadMsg
     */
    @RequestMapping(
        path = "loadMsg",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity loadMsg() {
        try (FileReader fr = new FileReader("chathistory.txt")) {
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            if (line != null)  {
                messages.clear();
                while (line != null) {
                    messages.add(line);
                    line = reader.readLine();
                }
            } else {
                return ResponseEntity.badRequest().body("File is empty!");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return ResponseEntity.ok().body("History was uploaded!");
    }
}

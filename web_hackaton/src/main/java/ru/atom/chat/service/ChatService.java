package ru.atom.chat.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atom.chat.ChatController;
import ru.atom.chat.dao.MessageDao;
import ru.atom.chat.dao.UserDao;
import ru.atom.chat.model.Message;
import ru.atom.chat.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private MessageDao messageDao;

    @Nullable
    @Transactional
    public User getLoggedIn(@NotNull String name) {
        return userDao.getByLogin(name);
    }

    @Transactional
    public void login(@NotNull String login) {
        User user = new User();
        userDao.save(user.setLogin(login));
        Message message = new Message()
            .setUser(userDao.getByLogin(login))
            .setValue("Logined in");
        messageDao.save(message);
        log.info("[" + login + "] logged in");
    }

    @Transactional
    public void say(@NotNull String name, @NotNull String msg) {
        Message message = new Message()
            .setUser(userDao.getByLogin(name))
            .setValue(msg);

        System.out.println();
        messageDao.save(message);
        log.info("[" + name + "] added a message");
    }

    @NotNull
    @Transactional
    public List<User> getOnlineUsers() {
        return new ArrayList<>(userDao.findAll());
    }

    @NotNull
    @Transactional
    public void logout(@NotNull User user) {
        userDao.delete(user);
        log.info("[" + user.getLogin() + "] logouted");
    }

    @NotNull
    @Transactional
    public String getChat() {
        String res = "";
        for (Message message : messageDao.findAll()) {
            res = res + "[" + message.getUser().getLogin() + "]: " + message.getValue() + "\n";
        }
        return res;
    }
}
package com.example.ws_board.chatting.controller;

import com.example.ws_board.chatting.dtos.ChatMessageDto;
import com.example.ws_board.chatting.dtos.ChatRoom;
import com.example.ws_board.chatting.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ChatController {

    private final ChatService chatService;

    @RequestMapping("/chat/chatList")
    public List<ChatRoom> chatList() {
        List<ChatRoom> roomList = chatService.findAllRoom();
        log.info("roomList : " + roomList);

        return roomList;
    }

    @PostMapping("/chat/createRoom")
    public String createRoom(@RequestBody String name) {
        log.info("name : " + name);
        ChatRoom room  = chatService.createRoom(name);
        log.info("create room : " + room);

        return "chat/chatRoom";
    }

    @GetMapping("/chat/chatRoom")
    public ChatRoom chatRoom(@PathVariable String roomId) {
        ChatRoom room = chatService.findRoomById(roomId);

        return room;
    }
}

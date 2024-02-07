package com.example.ws_board.chatting.dtos;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String type; // 메시지 타입
    private String sender; // 채팅을 보낸 사람
    private String receiver; // 채팅을 받는 사람
    private Object data;

    public void setSender(String sender) { this.sender = sender; }

    public void newConnect() {
        this.type = "new";
    }

    public void closeConnect() {
        this.type = "close";
    }
}

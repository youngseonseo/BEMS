package energypa.bems.chat.controller;

import energypa.bems.chat.entity.ChatRoom;
import energypa.bems.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatService chatService;

//    @GetMapping("/room")
//    public String rooms(Model model) {
//        return "/chat/room_list";
//    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoom> rooms() {
        return chatService.findAllRooms();
    }

    @PostMapping("/room")
    @ResponseBody
    public ChatRoom method(@RequestParam String roomName) {
        return chatService.createRoom(roomName);
    }

//    @GetMapping("/room/enter/{roomId}")
//    public String roomDetail(@PathVariable String roomId, Model model) {
//        model.addAttribute("roomId", roomId);
//        return "chat/room_detail";
//    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatService.findRoom(roomId);
    }
}
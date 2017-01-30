package id.base.app.webMember.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebSocketController {
	
	@MessageMapping("/webSocket")
	@SendTo("/topic/showResult")
	public Map<String, Object> showResult(String input) throws Exception {
		Thread.sleep(2000);
		Map<String, Object> result = new HashMap<>();
		result.put("result", input);
		return result;
	}
	
	@RequestMapping("/start")
	public String start() {
		return "start";
	}
	
}

package behavioral.mediator;

public class MediatorDemo {
    public static void main(String[] args) {
        System.out.println("=== Mediator Pattern Demo ===\n");
        
        ChatMediator chatRoom = new ChatRoom();
        
        User user1 = new ConcreteUser(chatRoom, "Alice");
        User user2 = new ConcreteUser(chatRoom, "Bob");
        User user3 = new ConcreteUser(chatRoom, "Charlie");
        User user4 = new ConcreteUser(chatRoom, "Diana");
        
        chatRoom.addUser(user1);
        chatRoom.addUser(user2);
        chatRoom.addUser(user3);
        chatRoom.addUser(user4);
        
        user1.send("Hello everyone!");
        System.out.println();
        user3.send("Hi Alice!");
        System.out.println();
        user2.send("Welcome to the chat!");
        System.out.println();
        user4.send("Good morning everyone!");
        System.out.println();
        user1.send("Thanks everyone, glad to be here!");
    }
}

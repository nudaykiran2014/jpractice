package structural.decorator;

public class DecoratorDemo {
    public static void main(String[] args) {
        System.out.println("=== Decorator Pattern Demo ===\n");
        
        Coffee simpleCoffee = new SimpleCoffee();
        System.out.println(simpleCoffee.getDescription() + " - $" + simpleCoffee.getCost());
        
        Coffee milkCoffee = new MilkDecorator(new SimpleCoffee());
        System.out.println(milkCoffee.getDescription() + " - $" + milkCoffee.getCost());
        
        Coffee sugarMilkCoffee = new SugarDecorator(new MilkDecorator(new SimpleCoffee()));
        System.out.println(sugarMilkCoffee.getDescription() + " - $" + sugarMilkCoffee.getCost());
        
        Coffee fancyCoffee = new WhipDecorator(
            new SugarDecorator(
                new MilkDecorator(
                    new SimpleCoffee()
                )
            )
        );
        System.out.println(fancyCoffee.getDescription() + " - $" + fancyCoffee.getCost());
    }
}

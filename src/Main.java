import java.util.ArrayList;
import java.util.Scanner;
import  java.util.List;


public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {

        while (true) {
            System.out.println("1. Ввод информации о номерах и их стоимости");
            System.out.println("2. Регистрация клиента и заказ номера");
            System.out.println("3. Вывод списка свободных номеров");
            System.out.println("4. Счёт клиента за проживание");
            System.out.println("5. Выход");

            int choice = getIntInput("Выберите пункт меню: ");
            switch (choice) {
                case 1 -> changeInfoAboutRooms();
                case 2 -> registerClient();
                case 3 -> showFreeRooms();
                case 4 -> showClientBill();
                case 5 -> {
                    System.out.println("Выход.");
                    return;
                }
                default -> System.out.println("Неверный ввод");
            }
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Неправильный ввод! Введите число.");
            scanner.next();
            System.out.print(prompt);
        }
        return scanner.nextInt();
    }

    private static int getPositiveInt(String prompt) {
        while (true) {
            int value = getIntInput(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Число должно быть положительным!");
        }
    }

    private static int getIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = getIntInput(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Введите число от " + min + " до " + max + "!");
        }
    }

    private static String getNameInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            String input = scanner.next();
            if (input.matches("[a-zA-Zа-яА-Я]+")) {
                return input;
            }
            System.out.println("Используйте только буквы!");
            System.out.print(prompt);
        }
    }

    private static void changeInfoAboutRooms() {
        System.out.println("Выберите класс номеров: 1 - BASIC, 2 - PREMIUM, 3 - LUX");
        int name = scanner.nextInt();
        if (name > 3 || name < 1) {
            throw new IllegalArgumentException("Неверный выбор класса!");
        } else if (name == 1) {
            System.out.println("Текущая цена за день: " + RoomType.BASIC.getCostPerDay());
            System.out.println("Общее количество номеров: " + RoomType.BASIC.getNumberOfRoom());
            int newPrice = getPositiveInt("Введите новую цену за день: ");
            RoomType.BASIC.setCostPerDay(newPrice);
            int newRoomCount = getPositiveInt("Введите новое количество номеров: ");
            RoomType.BASIC.setNumberOfRoom(newRoomCount);
        } else if (name == 2) {
            System.out.println("Текущая цена за день: " + RoomType.PREMIUM.getCostPerDay());
            System.out.println("Общее количество номеров: " + RoomType.PREMIUM.getNumberOfRoom());
            System.out.println("Введите новую цену за день: ");
            RoomType.PREMIUM.setCostPerDay(scanner.nextInt());
            System.out.println("Введите новое количество номеров: ");
            RoomType.PREMIUM.setNumberOfRoom(scanner.nextInt());
        } else if (name == 3) {
            System.out.println("Текущая цена за день: " + RoomType.LUX.getCostPerDay());
            System.out.println("Общее количество номеров: " + RoomType.LUX.getNumberOfRoom());
            System.out.println("Введите новую цену за день: ");
            RoomType.LUX.setCostPerDay(scanner.nextInt());
            System.out.println("Введите новое количество номеров: ");
            RoomType.LUX.setNumberOfRoom(scanner.nextInt());
        }
    }

    private static void registerClient() {
        String name = getNameInput("Введите имя клиента: ");
        String surname = getNameInput("Введите фамилию клиента: ");

        int typeChoice = getIntInRange("Выберите тип номера: 1 - BASIC, 2 - PREMIUM, 3 - LUX\n", 1, 3);

        RoomType selectedType = null;
        switch (typeChoice) {
            case 1 -> selectedType = RoomType.BASIC;
            case 2 -> selectedType = RoomType.PREMIUM;
            case 3 -> selectedType = RoomType.LUX;
            default -> {
                System.out.println("Неверный выбор типа номера!");
                return;
            }
        }

        if (selectedType.getFreeRoomsCount() > 0) {
            System.out.println("Введите количество дней проживания:");
            int days = scanner.nextInt();

            if (selectedType.bookRoom()) {
                Client client = new Client(name, surname, selectedType.getCostPerDay() * days, selectedType.name(), days);
                Hotel.getInstance().addClient(client);
                System.out.println("Клиент успешно зарегистрирован! Номер забронирован.");
            }
        } else {
            System.out.println("Нет свободных номеров типа " + selectedType.name());
        }
    }

    private static void showFreeRooms() {
        System.out.println("Свободные номера:");
        System.out.println("BASIC: " + RoomType.BASIC.getFreeRoomsCount() + " из " + RoomType.BASIC.getNumberOfRoom());
        System.out.println("PREMIUM: " + RoomType.PREMIUM.getFreeRoomsCount() + " из " + RoomType.PREMIUM.getNumberOfRoom());
        System.out.println("LUX: " + RoomType.LUX.getFreeRoomsCount() + " из " + RoomType.LUX.getNumberOfRoom());
    }

    private static void showClientBill() {
        Hotel hotel = Hotel.getInstance();
        List<Client> clients = hotel.getClients();

        if (clients.isEmpty()) {
            System.out.println("В отеле нет зарегистрированных клиентов.");
            return;
        }

        System.out.println("Список зарегистрированных клиентов:");
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            System.out.println((i + 1) + ". " + client.getName() + " " + client.getSurname());
        }

        System.out.println("Введите фамилию клиента для просмотра счёта:");
        String searchSurname = scanner.next();

        Client foundClient = null;
        for (Client client : clients) {
            if (client.getSurname().equalsIgnoreCase(searchSurname)) {
                foundClient = client;
                break;
            }
        }

        if (foundClient != null) {
            System.out.println("Информация о клиенте:");
            System.out.println("Имя: " + foundClient.getName());
            System.out.println("Фамилия: " + foundClient.getSurname());
            System.out.println("Тип номера: " + foundClient.getRoomType());
            System.out.println("Количество дней: " + foundClient.getDaysStaying());
            System.out.println("Сумма к оплате: " + foundClient.getRentalCost());
        } else {
            System.out.println("Клиент с фамилией '" + searchSurname + "' не найден.");
        }
    }
}


enum RoomType {
    BASIC(20, 2500), PREMIUM(10, 5000), LUX(3, 15000);
    private int costPerDay;
    private int totalNumberOfRooms;
    private int occupiedRooms;

    RoomType(int totalNumberOfRooms, int costPerDay) {
        this.totalNumberOfRooms = totalNumberOfRooms;
        this.costPerDay = costPerDay;
        this.occupiedRooms = 0;
    }

    public boolean bookRoom() {
        if (occupiedRooms < totalNumberOfRooms) {
            occupiedRooms++;
            return true;
        }
        return false;
    }

    public int getFreeRoomsCount() {
        return totalNumberOfRooms - occupiedRooms;
    }

    public void setCostPerDay(int costPerDay) {
        this.costPerDay = costPerDay;
    }

    public void setNumberOfRoom(int totalNumberOfRooms) {
        if (totalNumberOfRooms < this.occupiedRooms) {
            throw new IllegalArgumentException("Нельзя установить количество номеров меньше уже занятых!");
        }
        this.totalNumberOfRooms = totalNumberOfRooms;
    }

    public int getCostPerDay() { return costPerDay; }

    public int getNumberOfRoom() { return totalNumberOfRooms; }

}


class Client {
    private String name;
    private String surname;
    private int rentalCost;
    private String roomType;
    private int daysStaying;

    public Client(String name, String surname, int rentalCost, String roomType, int daysStaying) {
        this.name = name;
        this.surname = surname;
        this.rentalCost = rentalCost;
        this.roomType = roomType;
        this.daysStaying = daysStaying;
    }

    public String getName() {return name;}

    public String getSurname() {return surname;}

    public int getRentalCost() {return rentalCost;}

    public String getRoomType() {return roomType;}

    public int getDaysStaying() { return daysStaying; }
}

class Hotel {
    private static Hotel instance;
    private List<Client> clients;

    private Hotel() {
        clients = new ArrayList<>();
    }

    public static Hotel getInstance() {
        if (instance == null) {
            instance = new Hotel();
        }
        return instance;
    }


    public void addClient(Client client) {
        clients.add(client);
    }

    public List<Client> getClients() {
        return clients;
    }
}
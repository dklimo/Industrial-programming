import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String host = "192.168.137.1";
        int port = 5000;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Подключился к серверу!");
            String dateStr = null;
            while(true) {
                System.out.println("\nВыбирете действие:");
                System.out.println("1 - Записаться на приём");
                System.out.println("2 - Активные записи");
                System.out.println("3 - Выход");
                System.out.print("Ваш выбор: ");

                String choice = scanner.nextLine();
                out.println(choice);
                switch (choice) {
                    case "1":

                        while (true) {
                            System.out.print("Введите дату приёма (dd-MM-yyyy): ");
                            dateStr = scanner.nextLine();
                            out.println(dateStr);
                            String freeSlots = in.readLine();
                            if (freeSlots.equals("[]")) {
                                System.out.println("В этот день не осталось свободного времени. Выберете другую дату.");
                                continue;
                            }
                            if ("Error".equals(freeSlots)) {
                                System.out.println("Дата некорректна. Попробуйте снова.");
                                continue;
                            }

                            System.out.println("Свободное время: " + freeSlots);
                            break;
                        }

                        while (true) {

                            System.out.print("Выберите время (HH:mm): ");
                            String timeStr = scanner.nextLine();
                            out.println(timeStr);


                            String responsetime = in.readLine();
                            if (responsetime.equals("Error")) {
                                System.out.println("Время не корректно или занято. Попробуйте снова.");
                                continue;
                            }
                            if (responsetime.equals("OK")) {
                                System.out.print("Введите ФИО: ");
                                String name = scanner.nextLine();

                                System.out.print("Введите телефон: ");
                                String phone = scanner.nextLine();

                                System.out.print("Введите жалобы: ");
                                String complaint = scanner.nextLine();

                                String appointmentData = String.join(",", dateStr, timeStr, name, phone, complaint);
                                out.println(appointmentData);

                                String response = in.readLine();
                                System.out.println("Ответ от клиники: " + response);
                                break;
                            }
                        }
                        break;
                    case "2":
                        System.out.print("Введите дату приёма: ");
                        String Mydate = scanner.nextLine();
                        out.println(Mydate);
                        String line;
                        while((line = in.readLine())!= null) {
                            if (line.equals("END")) {
                                break;
                            }
                            System.out.println(line);
                        }
                        break;
                    case  "3":
                        System.out.println("До свидания!");
                        return;
                    default:
                        System.out.println("Неккоректный выборо. Пожалуйста попробуйте снова.");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

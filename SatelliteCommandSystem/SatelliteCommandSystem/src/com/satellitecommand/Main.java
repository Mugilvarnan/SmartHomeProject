package com.satellitecommand;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SatelliteCommandSystem commandSystem = new SatelliteCommandSystem();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Satellite Command System");
        System.out.println("Available commands: rotate <direction>, activatePanels, deactivatePanels, collectData, status, exit");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                String result = commandSystem.executeCommand(input);
                System.out.println(result);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Exiting Satellite Command System.");
    }
}

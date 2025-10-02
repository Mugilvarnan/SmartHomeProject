# 🏠 Smart Home System

## Overview
This is a console-based Java application that simulates a **Smart Home System**.  
It allows users to control and monitor smart devices such as **lights, thermostats, and door locks** via a centralized hub.  
The system supports **scheduling, automation triggers, remote access, and device state management**, while applying multiple **design patterns** to ensure modularity and maintainability.

---

## Features
- Initialize and manage multiple devices with unique IDs and types.
- Turn devices **ON/OFF** or change device-specific settings (e.g., thermostat temperature).
- Schedule devices to perform actions at specific times.
- Automate tasks based on triggers (e.g., turn off lights when temperature exceeds a threshold).
- Add or remove devices dynamically.
- Monitor device status in real-time.
- Extend device behavior dynamically using decorators (e.g., energy monitoring).
- Integrate legacy devices seamlessly using adapters.
- Centralized hub (singleton) manages all communication between devices.

---

## Design Patterns Used

| Pattern Type       | Pattern Name           | How Used                                                                 |
|-------------------|----------------------|-------------------------------------------------------------------------|
| **Creational**    | Factory Method        | Dynamically create devices without modifying hub code.                  |
|                   | Singleton             | `SmartHomeHub` ensures only one hub instance exists.                   |
| **Structural**    | Proxy                 | Adds authentication and logging for device access.                      |
|                   | Adapter               | Integrates legacy devices with the system.                              |
|                   | Decorator             | Extends device functionality dynamically (e.g., energy monitoring).     |
| **Behavioral**    | Command               | Encapsulates actions (ON/OFF, set temperature) and decouples from UI.  |
|                   | State                 | Handles device states (ON/OFF, LOCKED/UNLOCKED).                        |
|                   | Observer              | Devices or automation engine react to events/triggers automatically.    |
|                   | Mediator              | `SmartHomeHub` coordinates communication between all devices.           |

---

## Classes Overview

- **Device.java** – Interface representing a smart device.
- **Light.java, Thermostat.java, DoorLock.java** – Implementations of different device types.
- **StatefulDevice.java** – Base class for devices with state management.
- **DeviceState.java, OnState.java, OffState.java** – Represents device states (State Pattern).
- **DeviceFactory.java** – Factory Method for creating devices.
- **DeviceProxy.java** – Proxy for access control and logging.
- **DeviceDecorator.java, EnergyMonitoredDevice.java** – Decorator pattern for adding extra features.
- **Event.java, EventListener.java, EventBus.java** – Observer pattern for automation triggers.
- **Command.java, TurnOnCommand.java, TurnOffCommand.java, SetTemperatureCommand.java** – Command pattern for device actions.
- **SmartHomeHub.java** – Singleton Mediator that coordinates all devices, scheduling, and triggers.
- **ScheduledTask.java, TriggerRule.java** – Represents scheduled commands and automation triggers.
- **LegacyDeviceAdapter.java** – Adapter to integrate older devices into the system.
- **SmartHomeSystem.java** – Main driver/demo program.

---

## Getting Started

### 1. Compile
From the project root:
```bash
javac -d out $(find src -name "*.java")
### 2. Run
java -cp out com.smarthome.SmartHomeSystem


DSA Assignment - Walk-In Registration System
============================================

Project Overview
----------------
This project is a console-based Java application for managing walk-in and standard-booking guest registration in a resort/hotel setting.

It supports:
- Registering new guests
- Managing a waiting queue (FIFO)
- Processing the next guest
- Cancelling guest registration by confirmation number
- Generating queue and processed-guest reports

Main implementation is in:
- /home/runner/work/DSA-Assignment---WalkInRegistration/DSA-Assignment---WalkInRegistration/src/control/WalkInRegistration.java

Data Structures Used
--------------------
1. Linked Queue (custom)
   - File: src/adt/LinkedQueue.java
   - Used for waiting guest queue (FIFO behavior)

2. Array List (custom)
   - File: src/adt/ArrayList.java
   - Used to store processed/registered guests

Core Modules
------------
- Entity:
  - Guest model and GuestType enum
  - File: src/Entity/Guest.java

- Boundary (UI):
  - Console input/output handling
  - File: src/boundary/WalkInRegistrationUI.java

- Control:
  - Main business logic for registration workflow
  - File: src/control/WalkInRegistration.java

- Utility:
  - Search by confirmation number
  - Sorting by arrival time / guest name
  - Input validation helpers

Program Entry Point
-------------------
- Main class: main.MainMenu
- File: src/main/MainMenu.java

Requirements
------------
- JDK 20 (project is configured with source/target 20)
- Apache Ant (NetBeans project format)

How to Build and Run
--------------------
Using Ant:
1. Open terminal in project root.
2. Build:
   ant clean jar
3. Run:
   ant run

Main Features
-------------
1. Register Walk-In Guest
2. Process Next Guest
3. View All Guest Queue
4. View Top/Front Guest
5. Cancel Guest Registration
6. Reports:
   - Current waiting queue report (filter by guest type)
   - Processed guest report (search/sorted view)

Notes
-----
- Confirmation numbers are auto-generated as 8-digit values.
- Queue is preloaded with dummy data for demonstration/testing.
- Housekeeping and Loyalty modules are placeholders in MainMenu.

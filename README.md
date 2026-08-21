# DSA Assignment - Walk-In Registration

## Overview
This is a console-based Java resort system built for a Data Structures and Algorithms assignment.  
The project currently includes:
- **Walk-In / Standard Booking Registration** module
- **Housekeeping & Task Log** module
- **Loyalty & Rewards** module

## Main Features

### Walk-In / Standard Booking Registration
- Register walk-in and standard-booking guests
- Manage guest queue using FIFO processing
- Process next guest registration
- Cancel registration by confirmation number
- Generate waiting queue and processed guest reports

### Housekeeping & Task Log
- Add and display housekeeping task records
- Update room cleaning status and process late checkout rescheduling
- Undo latest task status change with history tracking
- Generate room status and staff task summary reports

### Loyalty & Rewards
- View member profile
- Add points to member accounts
- Queue redemption requests
- Process next redemption request
- Generate member ranking and tier roster reports

## Data Structures Used
- `LinkedQueue` (custom): waiting queue for guests
- `ArrayQueue` (custom): redemption request queue
- `ArrayList` (custom): processed guest storage
- `LinkedHistoryStack` (custom): housekeeping task status undo history

## Project Structure
- `src/main/MainMenu.java` - application entry point
- `src/control/WalkInRegistration.java` - walk-in registration logic
- `src/control/HousekeepingControl.java` - housekeeping task and status logic
- `src/control/HousekeepingReportControl.java` - housekeeping reporting logic
- `src/control/LoyaltyAndRewardsControl.java` - loyalty business logic
- `src/boundary/` - console UI classes
- `src/adt/` - custom ADT implementations
- `src/utility/` - search, sort, validation, and filter helpers

## Requirements
- JDK 20
- Apache Ant

## Build and Run
From the project root:

```bash
ant clean jar
ant run
```

## Test
```bash
ant test
```

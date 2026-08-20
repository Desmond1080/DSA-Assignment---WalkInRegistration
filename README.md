# DSA Assignment - Walk-In Registration

## Overview
This is a console-based Java resort system built for a Data Structures and Algorithms assignment.  
The project currently includes:
- **Walk-In / Standard Booking Registration** module
- **Loyalty & Rewards** module

## Main Features

### Walk-In / Standard Booking Registration
- Register walk-in and standard-booking guests
- Manage guest queue using FIFO processing
- Process next guest registration
- Cancel registration by confirmation number
- Generate waiting queue and processed guest reports

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

## Project Structure
- `src/main/MainMenu.java` - application entry point
- `src/control/WalkInRegistration.java` - walk-in registration logic
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

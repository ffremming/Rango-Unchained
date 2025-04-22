# rango-unchained - Group Project in TDT4240

## Introduction

This is the repository of or game Rango Unchained. The game was made as a project for the subject TDT4240 at NTNU.
Rango Unchained is a android arcade-styled game, built for android phones. Compete against a friend or try to beat the online highscore in single player!


## Installation and Setup

### Prerequisites

Before you can build and run the project, ensure you have the following tools installed and configured in your system’s `PATH`.

#### Git

**Git** is a distributed version control system used to manage the source code of this project.

1. Download and install Git from the [official Git website](https://git-scm.com/).  
2. Verify installation:
   ```bash
   git --version


#### Java JDK 17 (Download from Oracle's website)


#### Gradle 8.12.1


#### Android studio
The easiest way to run the app is by using androd studios built in emulator.


#### Cloning the Repository

To clone this repository, you can use either SSH or HTTPS:

**SSH (requires SSH key setup):**
```bash
git clone git@gitlab.stud.idi.ntnu.no:programvarearkitektur-gr12/rango-unchained.git
```
Make sure your SSH key is added to your GitLab account. You can find instructions [here](https://docs.gitlab.com/ee/ssh/).

**HTTPS (uses username and password or personal access token):**
```bash
git clone https://gitlab.stud.idi.ntnu.no/programvarearkitektur-gr12/rango-unchained.git
```
When using HTTPS, you'll be prompted to enter your GitLab credentials.

## How to Play

You can run the game directly through Android Studio or by using the command line.
We highly recomend using a connected device, since the gameplay is not suitable for cumputer usage.

### Option 1: Command Line

Run the following command to launch the game on an Android emulator or connected device:

```bash
./gradlew android:run
```

Make sure an emulator is running **or** a physical Android device is connected via USB with developer mode enabled. It is important to only have ONE running device when running the game.

---

### Option 2: Using Android Studio

1. Open the project in **Android Studio**.
2. Let it finish indexing and syncing the Gradle files.
3. Click on the green **Run** button at the top toolbar.
4. Choose an existing emulator or create a new virtual device (AVD) if none is available.
5. The game will be installed and launched on the emulator.

Tip: You can create a new emulator via **Tools → Device Manager → Create Device**.
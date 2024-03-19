# Travel Diary Android App

## Overview

Welcome to the Travel Diary Android App, a project developed by [Md. Mazharul Islam](https://github.com/mazhar75) and **Md Arham Ahmmad Adil** (me) for the Project 250 course. This project designed to help users document their travel experiences. This application, developed using Kotlin and Java, integrates various technologies such as RoomDB, Firebase, and Firestore to provide a comprehensive travel diary experience.

## Features

### User Authentication

- Sign up and sign in securely to access personalized travel diaries.

### Diary Management

- Create and delete travel diaries.
- Pin/unpin diaries for easy access.
- Share/unshare diaries with others.
- Customize diary colors.
- Upload, delete, and display images within diaries.

### User Profile Settings

- Customize user profile settings to personalize the app experience.

### Diary Search

- Easily search for specific diary entries.

### Location Services

- Utilizes Google Maps API to provide current location information.

## Libraries Used

- Picasso: For efficient image loading and displaying.
- SDP (ScalableDP) and SSP (ScalableSP): For consistent UI across different screen sizes.

## Limitations

1. **Device-Dependent Data:** Diary content is stored locally using RoomDB, so content is only available on the device where it was created.
2. **Limited Image Visibility:** Users can't view images uploaded by other users for a specific diary.
3. **No Password Recovery:** Currently, there is no mechanism for password recovery.
4. **No Content Recovery:** Deleted content cannot be recovered.

### Some snapshots of the project would be found inside _app-ui-snapshots_ folder

### Prerequisites

- Android Studio
- Knowledge of Kotlin, Java programming language
- Firebase account and project setup
- RoomDB, DAO

### We took help from the following resources:
- ChatGPT
- YouTube


### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/travel-diary-app.git

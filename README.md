# WatchGames

WatchGames is an innovative project designed to bring classic and new games to your Apple Watch. Our goal is to create a seamless gaming experience directly from your wrist, starting with Tic Tac Toe and expanding to more games over time.

## Table of Contents

- [Overview](#overview)
- [Technologies](#technologies)
- [Setup](#setup)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Overview

WatchGames utilizes AWS Lambda Functions on the back-end to handle server operations efficiently. We store game data in DynamoDB, ensuring fast and reliable data access. The serverless architecture is connected through the AWS API Gateway, providing a full-fledged server environment.

The front end of WatchGames is developed in Swift and is designed to function entirely on an Apple Watch, offering a unique and convenient gaming experience.

### Current Game

- **Tic Tac Toe:** Our initial game release is the classic Tic Tac Toe, optimized for quick and easy play on your Apple Watch.

### Future Games

We plan to expand our game library, adding more fun and interactive games over time.

## Technologies

- **AWS Lambda:** For scalable and serverless back-end operations.
- **DynamoDB:** To store and manage game data.
- **AWS API Gateway:** To create and manage the API endpoints.
- **Swift:** For developing the Apple Watch front-end application.

## Setup

### Prerequisites

- AWS Account
- Xcode (latest version recommended)
- Apple Watch (for testing)

### Back-End Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/WatchGames.git
   cd WatchGames

2. **Deploy AWS Lambda Functions:**

- Follow the instructions in the `backend/README.md` file to set up and deploy the AWS Lambda functions.

3. **Set up DynamoDB:**

- Create a DynamoDB table according to the specifications in the `backend/README.md`.

4. **Configure API Gateway:**

- Set up API Gateway endpoints to connect to the Lambda functions.

### Front-End Setup

1. **Open the project in Xcode:**

- Open WatchGames.xcodeproj in Xcode.

2. **Configure the project:**

- Set the appropriate bundle identifier and team.
- Ensure the project targets the correct version of watchOS.

3. **Build and run on your Apple Watch:**

- Connect your Apple Watch and build the project.

### Usage
1. **Launch the App:**

- Open the WatchGames app on your Apple Watch.

2. **Start Playing:**

- Select Tic Tac Toe from the game menu and enjoy!

### Contributing

We welcome contributions from the community! To contribute:

1. **Fork the repository.**
2. **Create a new branch:**

  ```bash
  git checkout -b feature-branch-name
  ```

3. **Make your changes and commit them:**

  ```bash
  git commit -m "Description of changes"
  ```

4. **Push to the branch:**

  ```bash
  git push origin feature-branch-name
  ```

5. **Create a pull request.**

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

---

Thank you for checking out WatchGames! We hope you enjoy playing and look forward to your contributions.

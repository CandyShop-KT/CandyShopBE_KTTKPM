pipeline {
    agent any

    environment {
        TELEGRAM_CHAT_ID = credentials('TELEGRAM_CHAT_ID')
        IMAGE_NAME = "candyshop"
        CONTAINER_NAME = "candyshop"
        JAR_FILE = "CandyShop-0.0.1-SNAPSHOT.jar"
        HOST_PORT = "8081"
        CONTAINER_PORT = "8081"
    }

    stages {
        stage('Clone code') {
            steps {
                git branch: 'tram_retry', url: 'https://github.com/CandyShop-KT/CandyShopBE_KTTKPM.git'
            }
        }

        stage('Build .jar') {
            steps {
                bat '.\\mvnw.cmd clean install -DskipTests'
            }
        }

        stage('Build Docker image') {
            steps {
                bat "docker build -t %IMAGE_NAME% ."
            }
        }

        stage('Run Docker container') {
            steps {
                bat """
                    docker stop %CONTAINER_NAME% || echo Container not running
                    docker rm %CONTAINER_NAME% || echo Container not exist
                    docker run -d -p %HOST_PORT%:%CONTAINER_PORT% --name %CONTAINER_NAME% %IMAGE_NAME%
                """
            }
        }
    }

    post {
        success {
            echo "CI/CD hoàn tất. Ứng dụng đang chạy tại http://localhost:%HOST_PORT%"
            telegramSend(
                chatId: env.TELEGRAM_CHAT_ID,
                message: "CI/CD hoàn tất! Ứng dụng đang chạy tại http://localhost:%HOST_PORT% \nJob: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
            )
        }

        failure {
            echo "Có lỗi xảy ra trong pipeline!"
            telegramSend(
                chatId: env.TELEGRAM_CHAT_ID,
                message: " Build thất bại! Vui lòng kiểm tra Jenkins.\nJob: ${env.JOB_NAME} #${env.BUILD_NUMBER}"
            )
        }
    }
}

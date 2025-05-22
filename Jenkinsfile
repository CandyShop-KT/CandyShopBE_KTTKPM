pipeline {
    agent any

    environment {
        IMAGE_NAME = "candyshop"
        CONTAINER_NAME = "candyshop"
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

        stage('Deploy with docker-compose') {
            steps {
                bat 'docker-compose down'
                bat 'docker-compose up -d --build'
            }
        }
    }

    post {
        success {
            echo "CI/CD hoàn tất. Ứng dụng đang chạy tại http://localhost:%HOST_PORT%"
        }
        failure {
            echo "Có lỗi xảy ra trong pipeline!"
        }
    }
}

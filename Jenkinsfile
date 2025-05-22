pipeline {
    agent any

    environment {
        IMAGE_NAME = "candyshop"
        CONTAINER_NAME = "candyshop"
        JAR_FILE = "CandyShop-0.0.1-SNAPSHOT.jar"
    }

    stages {
        stage('Clone code') {
            steps {
                git branch: 'tram_retry', url: 'https://github.com/CandyShop-KT/CandyShopBE_KTTKPM.git'
            }
        }

        stage('Build .jar') {
            steps {
                bat '.\\mvnw.cmd clean install'
            }
        }

        stage('Build Docker image') {
            steps {
                bat 'docker build -t %IMAGE_NAME% .'
            }
        }

        stage('Run Docker container') {
            steps {
                bat '''
                    docker stop %CONTAINER_NAME% || echo Not running
                    docker rm %CONTAINER_NAME% || echo Not exist
                    docker run -d -p 8081:8081 --name %CONTAINER_NAME% %IMAGE_NAME%
                '''
            }
        }
    }

    post {
        success {
            echo "CI/CD hoàn tất. Ứng dụng đang chạy tại http://localhost:8081"
        }

        failure {
            echo "Có lỗi xảy ra trong pipeline!"
        }
    }
}

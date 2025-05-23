pipeline {
    agent any

    environment {
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

        stage('Detect unresolved merge conflict') {
            steps {
                script {
                    def conflictFound = bat(script: 'findstr /S /C:"<<<<<<<" *.java', returnStatus: true)
                    if (conflictFound == 0) {
                        error " Merge conflict chưa được resolve đầy đủ! Vui lòng kiểm tra lại code."
                    }
                }
            }
        }

        stage('Compile only (syntax check)') {
            steps {
                bat '.\\mvnw.cmd compile -DskipTests'
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
                    for /f "tokens=*" %%i in ('docker ps -aqf "name=%CONTAINER_NAME%"') do (
                        docker stop %%i
                        docker rm %%i
                    )
                    docker run -d -p %HOST_PORT%:%CONTAINER_PORT% --name %CONTAINER_NAME% %IMAGE_NAME%
                """
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

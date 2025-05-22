pipeline {
    agent any

    stages {
        stage('Clone code') {
            steps {
                git branch: 'tram_retry', url: 'https://github.com/CandyShop-KT/CandyShopBE_KTTKPM.git'
            }
        }

        stage('Build project') {
            steps {
                bat '.\\mvnw.cmd clean install'
            }
        }

        stage('Deploy (Run JAR)') {
            steps {
                bat '''
                    taskkill /F /IM java.exe /T || echo No java process found
                    if exist target\\CandyShop-0.0.1-SNAPSHOT.jar (
                        start /B java -jar target\\CandyShop-0.0.1-SNAPSHOT.jar
                    ) else (
                        echo JAR file not found
                        exit 1
                    )
                '''
            }
        }
    }
}

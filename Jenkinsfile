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

        stage('Run JAR') {
            steps {
                bat 'if exist target\\CandyShop-0.0.1-SNAPSHOT.jar (java -jar target\\CandyShop-0.0.1-SNAPSHOT.jar) else (echo JAR file not found && exit 1)'
            }
        }
    }
}

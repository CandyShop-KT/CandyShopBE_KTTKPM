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

        stage('Run Backend') {
            steps {
                bat 'start-backend.bat'
            }
        }
    }
}

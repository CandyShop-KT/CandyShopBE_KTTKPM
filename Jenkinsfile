pipeline {
    agent any

    stages {
        stage('Clone code') {
            steps {
                git 'https://github.com/CandyShop-KT/CandyShopBE_KTTKPM.git'
            }
        }

        stage('Build project') {
            steps {
                bat '.\\mvnw.cmd clean install'
            }
        }

        stage('Run JAR') {
            steps {
                bat 'java -jar target\\CandyShop-0.0.1-SNAPSHOT.jar'
            }
        }
    }
}

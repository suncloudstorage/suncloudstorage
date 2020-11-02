#!groovy
// Check ub1 properties
properties([disableConcurrentBuilds()])

pipeline {
    agent {
        label 'master'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
        timestamps()
    }
    stages {
        stage("See directories") {
            steps {
                sh 'ls -l'
            }
        }
        stage("Change directory") {
            steps {
                sh 'cd projects/suncloudstorage'
            }
        }
        stage("Pull last changes") {
            steps {
                sh 'git pull'
            }
        }
        stage("Build maven project") {
            steps {
                sh 'sh mvnw clean install -DskipTests -Dspring.config.additional-location=suncloudstorage.properties'
            }
        }
        stage("Build docker container") {
            steps {
                sh 'sudo docker build -t suncloudstorage/suncloudstorage .'
            }
        }
        stage("Run docker container") {
            steps {
                sh 'sudo docker run -p9100:9100 suncloudstorage/suncloudstorage'
            }
        }

    }
}
#!groovy
// Run docker build
properties([disableConcurrentBuilds()])

pipeline {
    agent {
        label 'master'
        }
    triggers { pollSCM('* * * * *') }
    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
        timestamps()
    }
    stages {
        stage("create docker image") {
            steps {
                readFile 'suncloudstorage123/suncloudstorage.properties	'
                sh 'ls -l'
                echo " ============== start building image =================="
                sh 'docker build -t suncloudstorage/suncloudstorage . '
            }
        }
    }
}
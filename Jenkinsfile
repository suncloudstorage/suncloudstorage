#!groovy
// Check ub1 properties
properties([disableConcurrentBuilds()])

pipeline {
    agent {
        dockerfile true
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
        timestamps()
    }
    stages {
        stage("Test") {
            steps {
                sh 'java -version'
            }
        }

    }
}
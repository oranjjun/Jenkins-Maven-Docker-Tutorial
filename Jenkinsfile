#!groovy​
pipeline {
    agent {
        docker {
            image 'maven:3-alpine'
            args '-v /home/.m2/settings.xml:/root/.m2/settings.xml'
            args '-v /home/.m2/settings-security.xml:/root/.m2/settings-security.xml'
        }
    }
    options {
        skipStagesAfterUnstable()
    }
    environment {
        CI = 'true'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Sonarqube') {
            when {
                branch 'master'
            }
            environment {
                scannerHome = tool 'SonarQubeScanner'
            }
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh "${scannerHome}/bin/sonar-scanner"
                }
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Deliver') {
            when {
                branch 'master'
            }
            steps {
                sh './jenkins/scripts/deliver.sh'
                sh 'mvn clean deploy -Dmaven.test.skip=true -Dnexus.host=' + env.NEXUS_HOST
            }
        }
    }
    post {
        always {
            emailext body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\nMore info at: ${env.BUILD_URL}",
                    recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                    subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}"

        }
    }
}
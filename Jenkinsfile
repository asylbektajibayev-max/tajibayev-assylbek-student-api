pipeline {
    agent { label 'docker' }
    tools { jdk 'midterm-jdk17' }
    options { skipDefaultCheckout(true); timestamps(); disableConcurrentBuilds() }
    triggers { pollSCM('H/2 * * * *') }
    environment {
        STUDENT_NAME = 'Tajibayev Assylbek'
        IMAGE = 'tajibayev-assylbek-student-api:v1.1-tajibayev-assylbek-38288'
    }
    stages {
        stage('Checkout') {
            steps { checkout scm; echo "Student: ${env.STUDENT_NAME}" }
        }
        stage('Build') {
            steps { sh 'chmod +x gradlew; ./gradlew --no-daemon clean assemble' }
        }
        stage('Test') {
            steps {
                sh './gradlew --no-daemon test'
            }
            post { always { archiveArtifacts allowEmptyArchive: true, artifacts: 'build/test-results/test/*.xml' } }
        }
        stage('Docker Build') {
            steps { sh 'docker build -t "$IMAGE" .' }
        }
        stage('Verify') {
            steps { sh 'docker image inspect "$IMAGE" --format "Image={{.Id}} Size={{.Size}} Created={{.Created}}"' }
        }
    }
    post {
        success { echo 'Build, tests and Docker image verification succeeded.' }
        failure { echo 'Pipeline failed. Inspect the first failed stage in the console output.' }
    }
}

pipeline {
    agent { label 'docker' }
    tools { jdk 'midterm-jdk17' }
    options { skipDefaultCheckout(true); timestamps(); disableConcurrentBuilds() }
    triggers { pollSCM('H/2 * * * *') }
    parameters {
        booleanParam(name: 'CONTROLLED_FAILURE', defaultValue: false, description: 'Enable only on the personal CI demonstration branch')
    }
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
                script {
                    if (params.CONTROLLED_FAILURE && env.BRANCH_NAME != 'feature/tajibayev-assylbek-ci-demo') {
                        error('Controlled failure is restricted to the personal test branch')
                    }
                    sh "./gradlew --no-daemon test ${params.CONTROLLED_FAILURE ? '-PcontrolledFailure=true' : ''}"
                }
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
